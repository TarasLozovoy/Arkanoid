package ua.in.levor.arkanoid;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import ua.in.levor.arkanoid.Helpers.GameHelper;
import ua.in.levor.arkanoid.Helpers.AssetsHelper;
import ua.in.levor.arkanoid.Screens.MenuScreen;

public class StatusBar implements Disposable{
    public Stage stage;
    private Viewport viewport;
    private GameHelper gameHelper;

    private long gold;
    private int gems;
    private int lives;

    private Texture ballSprite;
    private Texture goldSprite;
    private Texture gemsSprite;

    private TextButton goldLabel;
    private TextButton gemsLabel;
    private TextButton livesLabel;

    public StatusBar(SpriteBatch spriteBatch) {
        gameHelper = GameHelper.getInstance();

        viewport = new StretchViewport(Arkanoid.WIDTH, Arkanoid.HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, spriteBatch);
        Gdx.input.setInputProcessor(stage);

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
        goldLabel.padTop(5f);
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

        parameter.size = 25;
        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = generator.generateFont(parameter);
        textButtonStyle.fontColor = Color.CHARTREUSE;
        TextButton menu = new TextButton("Menu", textButtonStyle);
        menu.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                Arkanoid instance = ((Arkanoid) Gdx.app.getApplicationListener());
                instance.setScreen(new MenuScreen(instance));
            }
        });

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        table.add(livesGroup).expandX().padTop(15f);
        table.add(goldGroup).expandX().padTop(15f);
        table.add(gemsGroup).expandX().padTop(15f);
        table.add(menu).align(Align.right).padRight(2f);

        update();

        stage.addActor(table);

        generator.dispose();
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
        stage.dispose();
        goldSprite.dispose();
        gemsSprite.dispose();
        ballSprite.dispose();
    }
}
