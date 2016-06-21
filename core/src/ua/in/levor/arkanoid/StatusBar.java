package ua.in.levor.arkanoid;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import ua.in.levor.arkanoid.Helpers.GameHelper;
import ua.in.levor.arkanoid.Helpers.AssetsHelper;
import ua.in.levor.arkanoid.Screens.DefaultScreen;
import ua.in.levor.arkanoid.Screens.GameScreen;
import ua.in.levor.arkanoid.Screens.MenuScreen;

public class StatusBar implements Disposable{
    public Stage statusStage;
    private Viewport viewport;
    private GameHelper gameHelper;
    private SpriteBatch batch;

    public Stage pauseStage;
    public Stage levelClearedStage;
    public Stage gameOverStage;

    private long gold;
    private int gems;
    private int lives;

    private Texture ballSprite;
    private Texture goldSprite;
    private Texture gemsSprite;
    private Texture pauseButtonSprite;
    private Texture dialogBGSprite;
    private Texture statusBarBg;

    private TextButton goldLabel;
    private TextButton gemsLabel;
    private TextButton livesLabel;

    public StatusBar(SpriteBatch spriteBatch) {
        this.batch = spriteBatch;
        gameHelper = GameHelper.getInstance();

        viewport = new StretchViewport(Arkanoid.WIDTH, Arkanoid.HEIGHT, new OrthographicCamera());
        statusStage = new Stage(viewport, spriteBatch);
        Gdx.input.setInputProcessor(statusStage);

        statusBarBg = new Texture(Gdx.files.internal(AssetsHelper.STATUS_BAR_BLACKOUT_BACKGROUND));

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Fonts/DoctorJekyllNF.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameter.size = 20;
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = generator.generateFont(parameter);

        gold = gameHelper.getGold();
        gems = gameHelper.getGems();
        lives = gameHelper.getLives();

        livesLabel = new TextButton("x " + lives, textButtonStyle);
        ballSprite = new Texture(Gdx.files.internal(AssetsHelper.BALL_PART_1));
        Image ballImg = new Image(ballSprite);
        ballImg.setScale(ballImg.getScaleX() * 16 / 24, ballImg.getScaleY() * 16 / 24);
        Group livesGroup = new Group();
        livesGroup.addActor(ballImg);
        livesGroup.addActor(livesLabel);
        ballImg.setPosition(-ballImg.getWidth() / 2, 0);
        ballImg.setAlign(Align.center);
        livesLabel.setPosition(ballImg.getWidth() / 2, 0);
        livesLabel.padTop(5f);
        livesLabel.align(Align.bottom | Align.right);

        //gold
        goldLabel = new TextButton("" + gold, textButtonStyle);
        goldSprite = new Texture(Gdx.files.internal(AssetsHelper.GOLD_COIN));
        Image goldImg = new Image(goldSprite);
        Group goldGroup = new Group();
        goldGroup.addActor(goldImg);
        goldGroup.addActor(goldLabel);
        goldImg.setPosition(-goldImg.getWidth() / 2, 0);
        goldImg.setAlign(Align.center);
        goldLabel.setPosition(goldImg.getWidth(), 0);
//        goldLabel.padTop(5f);
        goldLabel.align(Align.bottom | Align.right);

        //gems
        gemsLabel = new TextButton("" + gems, textButtonStyle);
        gemsLabel.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("gems: " + gems);
            }
        });
        gemsSprite = new Texture(Gdx.files.internal(AssetsHelper.DIAMOND));
        Image gemImage = new Image(gemsSprite);
        Group gemsGroup = new Group();
        gemsGroup.addActor(gemImage);
        gemsGroup.addActor(gemsLabel);
        gemImage.setPosition(-gemImage.getWidth() / 2, 0);
        gemImage.setAlign(Align.center);
        gemsLabel.setPosition(gemImage.getWidth(), 0);
        gemsLabel.padTop(5f);
        gemsLabel.align(Align.bottom | Align.right);

        //dialog bg
        dialogBGSprite = new Texture(Gdx.files.internal(AssetsHelper.DIALOG_BG));

        //pause button
        pauseButtonSprite = new Texture(Gdx.files.internal(AssetsHelper.PAUSE_BUTTON));
        Skin skin = new Skin();
        parameter.size = 25;
        parameter.color = Color.TAN;
        BitmapFont font = generator.generateFont(parameter);

        skin.add("button", pauseButtonSprite);
        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.getDrawable("button");
        textButtonStyle.over = skin.newDrawable("button", Color.LIGHT_GRAY);

        textButtonStyle.font = font;

        TextButton pauseButton = new TextButton("", textButtonStyle);
        pauseButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                Arkanoid gameInstance = ((Arkanoid) Gdx.app.getApplicationListener());
                gameInstance.getCurrentScreen().setState(DefaultScreen.GameState.PAUSED);
            }
        });


        Table table = new Table();
        table.top();
        table.setFillParent(true);

        table.add(livesGroup).expandX().padTop(15f);
        table.add(goldGroup).expandX().padTop(25f);
        table.add(gemsGroup).expandX().padTop(15f);
        table.add(pauseButton).align(Align.right).padRight(2f);

        update();

        statusStage.addActor(table);

        generator.dispose();
    }

    public void draw() {
        batch.begin();
        batch.draw(statusBarBg, 0, Arkanoid.HEIGHT - statusBarBg.getHeight());
        batch.end();
    }

    public void showDialog(DefaultScreen.GameState state) {
        if (state != DefaultScreen.GameState.RUNNING) {
            batch.begin();
            batch.draw(dialogBGSprite, 0, 0);
            batch.end();
            switch (state) {
                case PAUSED:
                    Gdx.input.setInputProcessor(pauseStage);
                    pauseStage.act();
                    pauseStage.draw();
                    break;
                case LEVEL_CLEARED:
                    Gdx.input.setInputProcessor(levelClearedStage);
                    levelClearedStage.act();
                    levelClearedStage.draw();
                    break;
                case GAME_OVER:
                    Gdx.input.setInputProcessor(gameOverStage);
                    gameOverStage.act();
                    gameOverStage.draw();
                    break;
            }
        }
    }

    public void initPauseStage() {
        pauseStage = new Stage(viewport, batch);
        Skin skin = new Skin();
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(AssetsHelper.FONT_DOCTOR_JEKYLL));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 25;
        parameter.color = Color.TAN;
        BitmapFont font = generator.generateFont(parameter); // font size 12 pixels

        Texture buttonTexture = new Texture(AssetsHelper.MENU_BUTTON);
        skin.add("button", buttonTexture);

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.getDrawable("button");
        textButtonStyle.over = skin.newDrawable("button", Color.LIGHT_GRAY);

        textButtonStyle.font = font;

        float xPosition = Arkanoid.WIDTH / 2 - buttonTexture.getWidth() / 2;

        TextButton resumeButton = new TextButton("Resume", textButtonStyle);
        resumeButton.setPosition(xPosition, 400);
        resumeButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                Arkanoid gameInstance = ((Arkanoid) Gdx.app.getApplicationListener());
                gameInstance.getCurrentScreen().setState(DefaultScreen.GameState.RUNNING);
            }
        });

        TextButton tryAgainButton = new TextButton("Start over", textButtonStyle);
        tryAgainButton.setPosition(xPosition, resumeButton.getY() - 65);
        tryAgainButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                Arkanoid gameInstance = ((Arkanoid) Gdx.app.getApplicationListener());
                ((GameScreen) gameInstance.getCurrentScreen()).retry();
            }
        });

        TextButton menuButton = new TextButton("Main menu", textButtonStyle);
        menuButton.setPosition(xPosition, tryAgainButton.getY() - 65);
        menuButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                Arkanoid gameInstance = ((Arkanoid) Gdx.app.getApplicationListener());
                gameInstance.setScreen(new MenuScreen(gameInstance));
            }
        });


        parameter.size = 45;
        parameter.color = Color.TAN;
        BitmapFont fontLabel = generator.generateFont(parameter);
        generator.dispose(); // don't forget to dispose to avoid memory leaks!
        Label.LabelStyle labelStyle = new Label.LabelStyle(fontLabel, Color.TAN);
        Label label = new Label("Game paused", labelStyle);
        label.setPosition(Arkanoid.WIDTH / 2 - label.getWidth() / 2, 500);

        pauseStage.addActor(label);
        pauseStage.addActor(resumeButton);
        pauseStage.addActor(tryAgainButton);
        pauseStage.addActor(menuButton);
    }

    public void initLevelClearedStage() {
        levelClearedStage = new Stage(viewport, batch);
        Skin skin = new Skin();
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(AssetsHelper.FONT_DOCTOR_JEKYLL));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 25;
        parameter.color = Color.TAN;
        BitmapFont font = generator.generateFont(parameter);

        Texture buttonTexture = new Texture(AssetsHelper.MENU_BUTTON);
        skin.add("button", buttonTexture);

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.getDrawable("button");
        textButtonStyle.over = skin.newDrawable("button", Color.LIGHT_GRAY);

        textButtonStyle.font = font;

        float xPosition = Arkanoid.WIDTH / 2 - buttonTexture.getWidth() / 2;

        TextButton nextLevelButton = new TextButton("Next Level", textButtonStyle);
        nextLevelButton.setPosition(xPosition, 400);
        nextLevelButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                Arkanoid gameInstance = ((Arkanoid) Gdx.app.getApplicationListener());
                gameInstance.getCurrentScreen().proceedToNextLevel();
            }
        });

        TextButton tryAgainButton = new TextButton("Start over", textButtonStyle);
        tryAgainButton.setPosition(xPosition, nextLevelButton.getY() - 65);
        tryAgainButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                Arkanoid gameInstance = ((Arkanoid) Gdx.app.getApplicationListener());
                ((GameScreen) gameInstance.getCurrentScreen()).retry();
            }
        });

        TextButton menuButton = new TextButton("Main menu", textButtonStyle);
        menuButton.setPosition(xPosition, tryAgainButton.getY() - 65);
        menuButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                Arkanoid gameInstance = ((Arkanoid) Gdx.app.getApplicationListener());
                gameInstance.setScreen(new MenuScreen(gameInstance));
            }
        });

        parameter.size = 45;
        parameter.color = Color.TAN;
        BitmapFont fontLabel = generator.generateFont(parameter);
        generator.dispose(); // don't forget to dispose to avoid memory leaks!
        Label.LabelStyle labelStyle = new Label.LabelStyle(fontLabel, Color.TAN);
        Label levelClearedLabel = new Label("Level cleared", labelStyle);
        levelClearedLabel.setPosition(Arkanoid.WIDTH / 2 - levelClearedLabel.getWidth() / 2, 500);

        levelClearedStage.addActor(levelClearedLabel);
        levelClearedStage.addActor(nextLevelButton);
        levelClearedStage.addActor(tryAgainButton);
        levelClearedStage.addActor(menuButton);
    }

    public void initGameOverStage() {
        gameOverStage = new Stage(viewport, batch);
        Skin skin = new Skin();
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(AssetsHelper.FONT_DOCTOR_JEKYLL));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 25;
        parameter.color = Color.TAN;
        BitmapFont font = generator.generateFont(parameter);

        Texture buttonTexture = new Texture(AssetsHelper.MENU_BUTTON);
        skin.add("button", buttonTexture);

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.getDrawable("button");
        textButtonStyle.over = skin.newDrawable("button", Color.LIGHT_GRAY);

        textButtonStyle.font = font;

        float xPosition = Arkanoid.WIDTH / 2 - buttonTexture.getWidth() / 2;

        TextButton tryAgainButton = new TextButton("Try again", textButtonStyle);
        tryAgainButton.setPosition(xPosition, 400);
        tryAgainButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                Arkanoid gameInstance = ((Arkanoid) Gdx.app.getApplicationListener());
                ((GameScreen) gameInstance.getCurrentScreen()).retry();
            }
        });

        TextButton menuButton = new TextButton("Main menu", textButtonStyle);
        menuButton.setPosition(xPosition, tryAgainButton.getY() - 65);
        menuButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                Arkanoid gameInstance = ((Arkanoid) Gdx.app.getApplicationListener());
                gameInstance.setScreen(new MenuScreen(gameInstance));
            }
        });

        parameter.size = 45;
        parameter.color = Color.TAN;
        BitmapFont fontLabel = generator.generateFont(parameter);
        generator.dispose(); // don't forget to dispose to avoid memory leaks!
        Label.LabelStyle labelStyle = new Label.LabelStyle(fontLabel, Color.TAN);
        Label gameOverLabel = new Label("Game over", labelStyle);
        gameOverLabel.setPosition(Arkanoid.WIDTH / 2 - gameOverLabel.getWidth() / 2, 500);

        gameOverStage.addActor(gameOverLabel);
        gameOverStage.addActor(tryAgainButton);
        gameOverStage.addActor(menuButton);
    }

    public void update() {
        gold = gameHelper.getGold();
        gems = gameHelper.getGems();
        lives = gameHelper.getLives();
        livesLabel.setText(" x " + String.format("%02d", lives));
        goldLabel.setText("" + gold);
        gemsLabel.setText("" + gems);
    }

    @Override
    public void dispose() {
        statusStage.dispose();
        gameOverStage.dispose();
        levelClearedStage.dispose();
        pauseStage.dispose();
        goldSprite.dispose();
        gemsSprite.dispose();
        ballSprite.dispose();
        dialogBGSprite.dispose();
        statusBarBg.dispose();
    }
}
