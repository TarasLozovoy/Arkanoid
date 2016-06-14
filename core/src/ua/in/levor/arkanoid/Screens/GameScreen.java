package ua.in.levor.arkanoid.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
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
import ua.in.levor.arkanoid.DB.DBHelper;
import ua.in.levor.arkanoid.Helpers.AssetsHelper;
import ua.in.levor.arkanoid.Helpers.BrickHelper;
import ua.in.levor.arkanoid.Helpers.GameHelper;
import ua.in.levor.arkanoid.Helpers.PowerUpHelper;
import ua.in.levor.arkanoid.Sprites.Ball;
import ua.in.levor.arkanoid.Sprites.Brick;
import ua.in.levor.arkanoid.Sprites.Platform;
import ua.in.levor.arkanoid.Sprites.PowerUp;
import ua.in.levor.arkanoid.StatusBar;
import ua.in.levor.arkanoid.Tools.B2WorldCreator;
import ua.in.levor.arkanoid.Tools.WorldContactListener;

public class GameScreen implements DefaultScreen {
    private Arkanoid game;
    private OrthographicCamera camera;
    private Viewport gamePort;
    private GameState gameState;
    private GameState oldgameState;

    private Vector3 touchPosition;

    private Array<Ball> balls = new Array<Ball>();
    private Platform platform;
    private Array<PowerUp> powerUps = new Array<PowerUp>();
    public Sprite statusBarBg;

    private Sprite bg;
    private int currentLevel;
    //powerUps
    private PowerUpHelper powerUpHelper;

    private float powerUpTimer;
    private PowerUp.Type powerUpType;
    private Sprite activePowerUp;
    private Texture activePowerUpBlank;
    private boolean showingPowerUp = false;
    //box2d
    private World world;


    private Box2DDebugRenderer b2dr;
    private TmxMapLoader mapLoader;

    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private StatusBar statusBar;

    public GameScreen(Arkanoid game, int level) {
        this.game = game;
        this.currentLevel = level;
        BrickHelper.getInstance().clear();
        GameHelper.getInstance().setLives(2);
        camera = new OrthographicCamera();
        gamePort = new StretchViewport(Arkanoid.WIDTH / Arkanoid.PPM, Arkanoid.HEIGHT / Arkanoid.PPM, camera);

        statusBar = new StatusBar(game.batch);
        gameState = GameState.RUNNING;

        powerUpHelper = PowerUpHelper.getInstance();

        touchPosition = new Vector3();
        bg = new Sprite(new Texture(Gdx.files.internal(AssetsHelper.PART_1_BACKGROUND)));
        statusBarBg = new Sprite(new Texture(Gdx.files.internal(AssetsHelper.STATUS_BAR_BLACKOUT_BACKGROUND)));
        activePowerUpBlank = new Texture(Gdx.files.internal(AssetsHelper.BLANK));
        activePowerUp = new Sprite(activePowerUpBlank);
        Arkanoid.adjustSize(bg);
        Arkanoid.adjustSize(statusBarBg);
        Arkanoid.adjustSize(activePowerUp);

        mapLoader = new TmxMapLoader();
        map = mapLoader.load("Levels/level" + currentLevel + ".tmx");
        renderer = new OrthogonalTiledMapRenderer(map,  1 / Arkanoid.PPM);

        camera.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        world = new World(new Vector2(0, 0), true);
        b2dr = new Box2DDebugRenderer();

//        Ball ball = new Ball(world, new Vector2(Arkanoid.WIDTH/2, 30.5f));
//        balls.add(ball);

        platform = new Platform(world);
        spawnNewBall();

        world.setContactListener(new WorldContactListener());

        new B2WorldCreator(world, map);

        Gdx.input.setCatchBackKey(true);

        statusBar.initPauseStage();
        statusBar.initLevelClearedStage();
        statusBar.initGameOverStage();
    }

    @Override
    public void show() {

    }

    public void update(float dt) {
        if (gameState != GameState.RUNNING) return;
        handleInput(dt);
        camera.update();
        world.step(1 / 60f, 6, 2);

        updateBall(dt);
        statusBar.update();

        renderer.setView(camera);

        updatePowerUps(dt);
        updateBricks(dt);
    }

    private void updateBall(float dt) {
        Array<Ball> destroyableBalls = new Array<Ball>();
        for (Ball ball : balls) {
            ball.update(dt);
            if (ball.isBelowYZero()) {
                destroyableBalls.add(ball);
            }
        }

        for (Ball ball : destroyableBalls) {
            balls.removeValue(ball, true);
            ball.hide();
            world.destroyBody(ball.b2body);
            ball.dispose();
            if (balls.size == 0) {
                GameHelper.getInstance().consumeLife();
                if (GameHelper.getInstance().getLives() > 0) {
                    spawnNewBall();
                } else {
                    gameState = GameState.GAME_OVER;
                }
            }
        }
    }

    private void spawnNewBall() {
        Ball ball;
        ball = new Ball(world, new Vector2(Arkanoid.unscale(platform.b2body.getPosition().x) + Platform.WIDTH / 2, 120.5f));
        ball.setIsActive(false);
        balls.add(ball);
    }

    @Override
    public void render(float delta) {
        if (gameState != oldgameState) {
            oldgameState = gameState;
            System.out.println(gameState);
        }
        update(delta);
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        bg.draw(game.batch);
        game.batch.draw(statusBarBg, 0, -0.45f, statusBarBg.getWidth(), statusBarBg.getHeight());
        activePowerUp.draw(game.batch);
        for (Ball ball : balls) {
            ball.draw(game.batch);
        }
        platform.draw(game.batch);

        for (PowerUp powerUp : powerUps) {
            powerUp.draw(game.batch);
        }
        game.batch.end();

        renderer.render();
//        b2dr.render(world, camera.combined);

        game.batch.setProjectionMatrix(statusBar.statusStage.getCamera().combined);
        statusBar.statusStage.draw();

        if (gameState == GameState.PAUSED) {
            Gdx.input.setInputProcessor(statusBar.pauseStage);
            statusBar.pauseStage.act();
            statusBar.pauseStage.draw();
        } else if (gameState == GameState.LEVEL_CLEARED) {
            Gdx.input.setInputProcessor(statusBar.levelClearedStage);
            statusBar.levelClearedStage.act();
            statusBar.levelClearedStage.draw();
        } else if (gameState == GameState.GAME_OVER) {
            Gdx.input.setInputProcessor(statusBar.gameOverStage);
            statusBar.gameOverStage.act();
            statusBar.gameOverStage.draw();
        } else if (gameState == GameState.RUNNING) {
            Gdx.input.setInputProcessor(statusBar.statusStage);
        }
    }

    private void handleInput(float dt) {
        if (Gdx.input.isTouched()) {
            touchPosition.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPosition);
            platform.updatePosition(touchPosition.x);
            for (Ball ball : balls) {
                ball.setIsActive(true);
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {
            game.setScreen(new MenuScreen(game));
        }
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
    }

    @Override
    public void pause() {
        DBHelper.getInstance().updateGold(GameHelper.getInstance().getGold());
//        if (gameState == GameState.RUNNING) {
//            gameState = GameState.PAUSED;
//        }
    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        DBHelper.getInstance().updateGold(GameHelper.getInstance().getGold());
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

        //checking for oops block activation
        if (BrickHelper.getInstance().isOopsActive()) {
            BrickHelper.getInstance().updateOopsBlockTimer(dt);
            platform.setAlpha(0.01f);
        } else {
            platform.setAlpha(1f);
        }

        if (BrickHelper.getInstance().isAllBricksDestroyed()) {
            levelCleared();
        }
    }

    private void levelCleared() {
        gameState = GameState.LEVEL_CLEARED;
        GameHelper.getInstance().addGold(100);
        DBHelper.getInstance().updateGold(GameHelper.getInstance().getGold());
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
            if (powerUp.isReady() && powerUp.getTexture() != null) {
                toBeDestroyed.add(powerUp);
                if (!handleNewPowerUp(powerUp.getType())) {
                    powerUpType = powerUp.getType();
                    showActivePowerUp(powerUpTimer > 0);
                    powerUpTimer = 20;
                }
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
            for (Ball ball : balls) {
                ball.setPowerUp(powerUpType);
            }
        } else if (powerUpType != null){
            for (Ball ball : balls) {
                ball.setPowerUp(null);
            }
            powerUpType = null;
            showActivePowerUp(false);
        }
    }

    private boolean handleNewPowerUp(PowerUp.Type type) {
        switch (type) {
            case SLOW:
                for (Ball ball : balls) {
                    ball.changeSpeed(0.9f);
                }
                return true;
            case SPEED_UP:
                for (Ball ball : balls) {
                    ball.changeSpeed(1.1f);
                }
                return true;
            case ADD_BALL:
                spawnNewBall();
                return true;
        }
        return false;
    }

    private void showActivePowerUp(boolean replaceCurrent) {
        if (powerUpType == null && showingPowerUp) {
            activePowerUp.setTexture(activePowerUpBlank);
            showingPowerUp = false;
        } else if (powerUpType != null && (!showingPowerUp || replaceCurrent)) {
            Sprite sprite = new Sprite(powerUpType.getTexture());
            Arkanoid.adjustSize(sprite);
            activePowerUp.set(sprite);
            activePowerUp.setPosition(0, Arkanoid.scale(Arkanoid.HEIGHT) - activePowerUp.getHeight());
            showingPowerUp = true;
        }
    }

    @Override
    public void setState(GameState state) {
        gameState = state;
    }

    @Override
    public void proceedToNextLevel() {
        // TODO: 6/14/16 add checking if end of part
        game.setScreen(new GameScreen(game, currentLevel + 1));
    }
    public void retry() {
        game.setScreen(new GameScreen(game, currentLevel));
    }

    @Override
    public void dispose() {
        for (Ball ball : balls) {
            ball.dispose();
        }
        platform.dispose();
        bg.getTexture().dispose();
    }
}
