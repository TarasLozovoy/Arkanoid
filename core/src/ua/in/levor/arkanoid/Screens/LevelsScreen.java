package ua.in.levor.arkanoid.Screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import ua.in.levor.arkanoid.Arkanoid;
import ua.in.levor.arkanoid.Helpers.GameHelper;

public class LevelsScreen implements Screen {
    private Skin skin;
    private Stage stage;
    private OrthographicCamera camera;
    private Viewport viewport;

    private Arkanoid game;

    public LevelsScreen(Arkanoid game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Arkanoid.WIDTH / 2, Arkanoid.HEIGHT / 2);

        create();
        GameHelper.getInstance().setLives(5);
    }

    public void create(){
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Arkanoid.WIDTH / 2, Arkanoid.HEIGHT / 2);
        viewport = new FitViewport(Arkanoid.WIDTH, Arkanoid.HEIGHT, camera);
        viewport.apply();

        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();

        stage = new Stage(viewport, game.batch);
        //Stage should control input:
        Gdx.input.setInputProcessor(stage);
        Gdx.input.setCatchBackKey(true);
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

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Fonts/DoctorJekyllNF.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 35;
        BitmapFont font = generator.generateFont(parameter);
        generator.dispose();
        skin.add("default", font);

        // Configure a TextButtonStyle and name it "default". Skin resources are stored by type, so this doesn't overwrite the font.
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
//        textButtonStyle.up = skin.newDrawable("white", Color.DARK_GRAY);
//        textButtonStyle.down = skin.newDrawable("white", Color.DARK_GRAY);

        textButtonStyle.font = skin.getFont("default");

        skin.add("default", textButtonStyle);

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        for (int i = 1; i <= 20; i++) {
            TextButton lvl = new TextButton(String.valueOf(i), textButtonStyle);
            final int level = i;
            lvl.addListener(new ChangeListener() {
                public void changed(ChangeEvent event, Actor actor) {
                    game.setScreen(new GameScreen(game, level));
                }
            });
            table.add(lvl).expand().pad(10);
            if (i % 4 == 0) {
                table.row();
            }
        }
        table.padBottom(100);
        TextButton backButton = new TextButton("< Back", textButtonStyle);
        backButton.setPosition(50, 50);
        backButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MenuScreen(game));
            }
        });
        stage.addActor(table);
        stage.addActor(backButton);
    }

    private void update(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {
            game.setScreen(new MenuScreen(game));
        }
    }

    @Override
    public void render(float delta) {
        update(delta);
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
