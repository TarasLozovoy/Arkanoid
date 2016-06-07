package ua.in.levor.arkanoid.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import ua.in.levor.arkanoid.Arkanoid;
import ua.in.levor.arkanoid.Helpers.AssetsHelper;
import ua.in.levor.arkanoid.Helpers.GameHelper;

public class MenuScreen implements Screen{
    private Skin skin;
    private Stage stage;
    private OrthographicCamera camera;
    private Viewport viewport;

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
        viewport = new FitViewport(Arkanoid.WIDTH, Arkanoid.HEIGHT, camera);
        viewport.apply();

        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();

        stage = new Stage(viewport, game.batch);
        //Stage should controll input:
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {
// A skin can be loaded via JSON or defined programmatically, either is fine. Using a skin is optional but strongly
        // recommended solely for the convenience of getting a texture, region, etc as a drawable, tinted drawable, etc.
        skin = new Skin();
        // Generate a 1x1 white texture and store it in the skin named "white".
        Pixmap pixmap = new Pixmap(200, 100, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.GREEN);
        pixmap.fill();

        skin.add("white", new Texture(pixmap));

        // Store the default libgdx font under the name "default".
//        BitmapFont bitmapFont = new BitmapFont();
//        bitmapFont.getData().scale(0.5f);
//
//        skin.add("default",bitmapFont);
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(AssetsHelper.FONT_DOCTOR_JEKYLL));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 25;
        BitmapFont font = generator.generateFont(parameter); // font size 12 pixels
        generator.dispose(); // don't forget to dispose to avoid memory leaks!
        skin.add("default", font);

        // Configure a TextButtonStyle and name it "default". Skin resources are stored by type, so this doesn't overwrite the font.
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.newDrawable("white", Color.DARK_GRAY);
        textButtonStyle.down = skin.newDrawable("white", Color.DARK_GRAY);

        textButtonStyle.font = skin.getFont("default");

        skin.add("default", textButtonStyle);

        float xPosition = Arkanoid.WIDTH / 2 - 100;

        // Create a button with the "default" TextButtonStyle. A 3rd parameter can be used to specify a name other than "default".
        TextButton playButton = new TextButton("Play", textButtonStyle);
        playButton.setPosition(xPosition, 620);
        playButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new GameScreen(game, 1));
            }
        });

        TextButton skillsButton = new TextButton("Skills", textButtonStyle);
        skillsButton.setPosition(xPosition, 500);
        skillsButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                ((TextButton) actor).setText("Skills pressed");
            }
        });

        TextButton levelsButton = new TextButton("Select level", textButtonStyle);
        levelsButton.setPosition(xPosition, 380);
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
		skin.dispose();
    }
}
