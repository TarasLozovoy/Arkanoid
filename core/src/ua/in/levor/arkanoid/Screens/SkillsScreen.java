package ua.in.levor.arkanoid.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;
import ua.in.levor.arkanoid.Arkanoid;
import ua.in.levor.arkanoid.StatusBar;

public class SkillsScreen implements DefaultScreen{

    private TiledMap map;
    private TiledMapRenderer renderer;
    private OrthographicCamera camera;
    private OrthoCamController cameraController;

    private Arkanoid game;
    private StatusBar statusBar;

    public SkillsScreen(Arkanoid game) {
        this.game = game;

        camera = new OrthographicCamera();

        create();
    }

    public void create(){
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Arkanoid.WIDTH / 2, Arkanoid.HEIGHT / 2);
        camera.position.set(camera.viewportWidth, camera.viewportHeight, 0);

        map = new TmxMapLoader().load("1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1);

        statusBar = new StatusBar(game.batch, true);
        cameraController = new OrthoCamController(camera, map);
        Gdx.input.setInputProcessor(cameraController);
        Gdx.input.setCatchBackKey(true);
    }

    @Override
    public void show() {

    }

    private void update(float dt) {
        handleInput(dt);
        statusBar.update();
    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(1f, 1f, 1f, 0.5f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();

        renderer.setView(camera);
        renderer.render();

        game.batch.setProjectionMatrix(statusBar.statusStage.getCamera().combined);
        statusBar.draw();
        statusBar.statusStage.draw();
    }

    @Override
    public void resize(int width, int height) {
        camera.viewportHeight = height;
        camera.viewportWidth = width;
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
        map.dispose();
    }

    private void handleInput(float dt) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {
            game.setScreen(new MenuScreen(game));
        }
    }

    @Override
    public void setState(GameState state) {}

    @Override
    public void proceedToNextLevel() {}

    public class OrthoCamController extends InputAdapter {
        final OrthographicCamera camera;
        final Vector3 curr = new Vector3();
        final Vector3 last = new Vector3(-1, -1, -1);
        final Vector3 delta = new Vector3();

        int mapWidth;
        int mapHeight;

        public OrthoCamController (OrthographicCamera camera, TiledMap map) {
            this.camera = camera;

            MapProperties prop = map.getProperties();

            int width = prop.get("width", Integer.class);
            int height = prop.get("height", Integer.class);
            int tilePixelWidth = prop.get("tilewidth", Integer.class);
            int tilePixelHeight = prop.get("tileheight", Integer.class);

            mapWidth = width * tilePixelWidth;
            mapHeight = height * tilePixelHeight;

        }

        @Override
        public boolean touchDragged (int x, int y, int pointer) {
            camera.unproject(curr.set(x, y, 0));
            if (!(last.x == -1 && last.y == -1 && last.z == -1)) {
                camera.unproject(delta.set(last.x, last.y, 0));
                delta.sub(curr);
                camera.position.add(delta.x, delta.y, 0);
            }
            last.set(x, y, 0);

            //check for edges
            if (camera.position.x - camera.viewportWidth / 2 < 0) {
                camera.position.x = camera.viewportWidth / 2;
            }
            if (camera.position.y - camera.viewportHeight / 2 < 0) {
                camera.position.y = camera.viewportHeight / 2;
            }
            if (camera.position.x + camera.viewportWidth / 2 > mapWidth) {
                camera.position.x = mapWidth - camera.viewportWidth / 2;
            }
            if (camera.position.y + camera.viewportHeight / 2 > mapHeight) {
                camera.position.y = mapHeight - camera.viewportHeight / 2;
            }


            return false;
        }

        @Override
        public boolean touchUp (int x, int y, int pointer, int button) {
            last.set(-1, -1, -1);
            return false;
        }
    }
}
