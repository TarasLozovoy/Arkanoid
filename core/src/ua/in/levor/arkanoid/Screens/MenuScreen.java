package ua.in.levor.arkanoid.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import ua.in.levor.arkanoid.Arkanoid;
import ua.in.levor.arkanoid.Helpers.AssetsHelper;
import ua.in.levor.arkanoid.Helpers.GameHelper;

public class MenuScreen implements DefaultScreen{
    private Stage stage;
    private OrthographicCamera camera;
    private Viewport viewport;
    private Sprite bg;

    private Arkanoid game;

    public MenuScreen(Arkanoid game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Arkanoid.WIDTH / 2, Arkanoid.HEIGHT / 2);

        create();
        GameHelper.getInstance().setLives(5);
        Gdx.input.setCatchBackKey(false);
    }

    public void create(){
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Arkanoid.WIDTH / 2, Arkanoid.HEIGHT / 2);
        viewport = new StretchViewport(Arkanoid.WIDTH, Arkanoid.HEIGHT, camera);
        viewport.apply();

        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();

        stage = new Stage(viewport, game.batch);
        //Stage should control input:
        Gdx.input.setInputProcessor(stage);

        bg = new Sprite(new Texture(Gdx.files.internal(AssetsHelper.MENU_BACKGROUND)));
    }

    @Override
    public void show() {
        Skin skin = new Skin();
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(AssetsHelper.FONT_DOCTOR_JEKYLL));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 25;
        parameter.color = Color.TAN;
        BitmapFont font = generator.generateFont(parameter); // font size 12 pixels
        generator.dispose(); // don't forget to dispose to avoid memory leaks!

        Texture buttonTexture = new Texture(AssetsHelper.MENU_BUTTON);
        Sprite button = new Sprite(buttonTexture);
        skin.add("button", buttonTexture);

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
//        textButtonStyle.up = skin.newDrawable("white", Color.DARK_GRAY);
//        textButtonStyle.down = skin.newDrawable("white", Color.DARK_GRAY);
        textButtonStyle.up = skin.getDrawable("button");
        textButtonStyle.over = skin.newDrawable("button", Color.LIGHT_GRAY);

        textButtonStyle.font = font;

        float xPosition = Arkanoid.WIDTH / 2 - 100;

        TextButton playButton = new TextButton("Play", textButtonStyle);
        playButton.setPosition(xPosition, 600);
        playButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new GameScreen(game, 1));
            }
        });

        TextButton skillsButton = new TextButton("Skills", textButtonStyle);
        skillsButton.setPosition(xPosition, 525);
        skillsButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                ((TextButton) actor).setText("Skills pressed");
            }
        });

        TextButton levelsButton = new TextButton("Select level", textButtonStyle);
        levelsButton.setPosition(xPosition, 450);
        levelsButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new LevelsScreen(game));
            }
        });


        stage.addActor(playButton);
        stage.addActor(skillsButton);
        stage.addActor(levelsButton);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
        bg.draw(game.batch);
        game.batch.end();

        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    @Override
    public void setState(GameState state) {

    }
}
