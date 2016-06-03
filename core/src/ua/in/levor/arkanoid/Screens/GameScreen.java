package ua.in.levor.arkanoid.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import ua.in.levor.arkanoid.Arkanoid;
import ua.in.levor.arkanoid.Sprites.Ball;
import ua.in.levor.arkanoid.Sprites.Brick;
import ua.in.levor.arkanoid.Helpers.BrickHelper;
import ua.in.levor.arkanoid.Sprites.Platform;
import ua.in.levor.arkanoid.Sprites.PowerUp;
import ua.in.levor.arkanoid.Helpers.PowerUpHelper;
import ua.in.levor.arkanoid.StatusBar;
import ua.in.levor.arkanoid.Tools.B2WorldCreator;
import ua.in.levor.arkanoid.Tools.WorldContactListener;

public class GameScreen implements Screen {
    private Arkanoid game;
    private OrthographicCamera camera;
    private Viewport gamePort;

    private Vector3 touchPosition;
    private Ball ball;
    private Platform platform;
    private Array<PowerUp> powerUps = new Array<PowerUp>();

    private Sprite bg;

    private TextureAtlas atlas;

    //powerUps
    private PowerUpHelper powerUpHelper;
    private float powerUpTimer;
    private PowerUp.Type powerUpType;


    //box2d
    private World world;
    private Box2DDebugRenderer b2dr;

    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    private StatusBar statusBar;

    public GameScreen(Arkanoid game) {
        this.game = game;
        camera = new OrthographicCamera();
        gamePort = new StretchViewport(Arkanoid.WIDTH / Arkanoid.PPM, Arkanoid.HEIGHT / Arkanoid.PPM, camera);

        statusBar = new StatusBar(game.batch);

        powerUpHelper = PowerUpHelper.getInstance();

        touchPosition = new Vector3();
        bg = new Sprite(new Texture(Gdx.files.internal("level1.png")));
        Arkanoid.adjustSize(bg);

        atlas = new TextureAtlas("Bricks/bricks.txt");

        mapLoader = new TmxMapLoader();
        map = mapLoader.load("Levels/level1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map,  1 / Arkanoid.PPM);

        camera.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        world = new World(new Vector2(0, 0), true);
        b2dr = new Box2DDebugRenderer();

        ball = new Ball(world);
        platform = new Platform(world);

        world.setContactListener(new WorldContactListener());

        new B2WorldCreator(world, map);

        System.out.println("GameScreen opened");
    }

    @Override
    public void show() {

    }

    public void update(float dt) {
        handleInput(dt);
        camera.update();
        world.step(1 / 60f, 6, 2);

        ball.update(dt);
        if (ball.isBelowYZero()) {
            game.setScreen(new GameScreen(game));
        }
        statusBar.update();

        renderer.setView(camera);

        updatePowerUps(dt);
        updateBricks(dt);
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(1f, 1f, 1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        bg.draw(game.batch);
        ball.draw(game.batch);
        platform.draw(game.batch);

        for (PowerUp powerUp : powerUps) {
            powerUp.draw(game.batch);
        }

        game.batch.end();

        renderer.render();
        b2dr.render(world, camera.combined);

        game.batch.setProjectionMatrix(statusBar.stage.getCamera().combined);
        statusBar.stage.draw();
    }

    private void handleInput(float dt) {
        if (Gdx.input.isTouched()) {
            touchPosition.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPosition);
            platform.updatePosition(touchPosition.x);
            ball.setIsActive(true);
        }
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
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

    private void updateBricks(float dt) {
        Array<Brick> destroyArray = new Array<Brick>();
        for (Brick br: BrickHelper.getInstance().getAllBricks()) {
            if (br.isReadyToDestroy()) {
                destroyArray.add(br);
            }
        }

        for (Brick br : destroyArray) {
            BrickHelper.getInstance().getAllBricks().removeValue(br, true);
        }
    }

    private void updatePowerUps(float dt) {
        if (powerUpHelper.getRequestedPowerUpsPositions().size > 0) {
            for (Vector2 pos : powerUpHelper.getRequestedPowerUpsPositions()) {
                PowerUp powerUp = new PowerUp(world, pos);
                powerUps.add(powerUp);
            }
            powerUpHelper.clearRequestedPowerUpsPositions();
        }

        Array<PowerUp> toBeDestroyed = new Array<PowerUp>();
        for (PowerUp powerUp : powerUps) {
            powerUp.update(dt);
            if (powerUp.isReady()) {
                toBeDestroyed.add(powerUp);
                // TODO: 6/1/16 add powerUp action
                powerUpTimer = 20;
                powerUpType = powerUp.getType();
            } else if (powerUp.b2body.getPosition().y < 0) {
                toBeDestroyed.add(powerUp);
            }
        }

        for (PowerUp powerUp : toBeDestroyed) {
            powerUps.removeValue(powerUp, true);
            powerUp.dispose();
        }

        if (powerUpTimer > 0) {
            System.out.println(powerUpTimer);
            powerUpTimer -= dt;
            ball.setPowerUp(powerUpType);
        } else {
            ball.setPowerUp(null);
        }
    }

    @Override
    public void dispose() {
        ball.dispose();
        platform.dispose();
        bg.getTexture().dispose();
    }
}
