package ua.in.levor.arkanoid;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class GameScreen implements Screen {
    private Arkanoid game;
    private OrthographicCamera camera;

    private Vector3 touchPosition;
    private Ball ball;
    private Platform platform;
    private Grid grid;

    private Texture bg;

    private Array<Brick> bricks = new Array<Brick>();
    private Array<PowerUp> powerUps = new Array<PowerUp>();

    public GameScreen(Arkanoid game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false,Arkanoid.WIDTH, Arkanoid.HEIGHT);

        touchPosition = new Vector3();
        bg = new Texture("level1.png");

        Array<String> values = new Array<String>();
        values.add("00000000000000000000");
        values.add("00000000000000000000");
        values.add("00000000000000000000");
        values.add("00000000000000000000");
        values.add("00000000000000000000");
        values.add("00000000000000000000");
        values.add("00000000000000000000");
        values.add("00000000000000000000");
        values.add("00000000000000000000");
        values.add("50505050505050505050");
        values.add("05050505050505050505");
        values.add("50505050505050505050");
        values.add("00000001000000000000");
        values.add("00000010000010000000");
        values.add("00005100001000001000");
        values.add("00001000000000000000");
        values.add("00010000500010000000");
        values.add("00100000050010000000");
        values.add("00050000000000000500");
        values.add("00505050905050505050");
        values.add("00050505050505050505");
        values.add("00505050505050505090");
        values.add("00900000000000000000");
        values.add("00000000000000000000");
        values.add("00010000000000000500");
        grid = new Grid(values);
        bricks = grid.getBricks();

        platform = new Platform(new Vector2((float) Arkanoid.WIDTH / 2 - Platform.WIDTH / 2, 10));
        ball = new Ball(new Vector2(platform.getPosition().x + Platform.WIDTH - 2 * Ball.DIAMETER / 3, platform.getPosition().y + Platform.HEIGHT),
                new Vector2(Ball.DEFAULT_VELOCITY_X, Ball.DEFAULT_VELOCITY_y));

        System.out.println("GameScreen opened");
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.batch.draw(bg, 0, 0);
        for (Brick brick : bricks) {
            game.batch.draw(brick.getTexture(), brick.getPosition().x, brick.getPosition().y);
        }

        for (PowerUp powerUp : powerUps) {
            game.batch.draw(powerUp.getTexture(), powerUp.getPosition().x, powerUp.getPosition().y);
        }
        game.batch.draw(platform.getTexture(), platform.getPosition().x, platform.getPosition().y);
        game.batch.draw(ball.getTexture(), ball.getPosition().x, ball.getPosition().y);
        game.batch.end();

        handleInput();

        updateBall(delta);
        updatePowerUps(delta);
    }

    private void updateBall(float dt) {
        ball.update(dt);

        Vector2 center = ball.getCenterPosition();
        Brick nearestBrick = grid.getNearestBrick(center);

        Rectangle r = nearestBrick.getRect();
        float top = Intersector.distanceSegmentPoint(r.x, r.y, r.x + Brick.WIDTH, r.y, center.x, center.y);
        float bottom = Intersector.distanceSegmentPoint(r.x, r.y + Brick.HEIGHT, r.x + Brick.WIDTH, r.y + Brick.HEIGHT, center.x, center.y);
        float left = Intersector.distanceSegmentPoint(r.x, r.y, r.x + Brick.WIDTH, r.y + Brick.HEIGHT, center.x, center.y);
        float right = Intersector.distanceSegmentPoint(r.x + Brick.WIDTH, r.y, r.x + Brick.WIDTH, r.y + Brick.HEIGHT, center.x, center.y);

        float minY = Math.min(top, bottom);
        float minX = Math.min(left, right);

        if (!nearestBrick.isHitResistEnabled()) {
            if (Math.min(minX, minY) < Ball.DIAMETER / 2) {
                Brick.State state = nearestBrick.handleHit();

                switch (state) {
                    case SPAWN_BOMB:
                        spawnPowerUp(PowerUp.Type.BOMB, nearestBrick.getPosition());
                    case DESTROYED:
                        bricks.removeValue(nearestBrick, true);
                        nearestBrick.dispose();
                        break;
                }
            }

            if (minX < Ball.DIAMETER / 2 && minY < Ball.DIAMETER / 2) {
                //calculating angle
                Vector2 vectorToBrickCenter = new Vector2(nearestBrick.getCenter().x - ball.getCenterPosition().x, nearestBrick.getCenter().y - ball.getCenterPosition().y);
                double angle = MathUtils.radiansToDegrees * (MathUtils.atan2(vectorToBrickCenter.y, vectorToBrickCenter.x)
                        + MathUtils.atan2(ball.getVelocity().y, ball.getVelocity().x)) - 90;
                while (angle < 0) {
                    angle += 360;
                }
                while (angle > 360) {
                    angle -= 360;
                }
                System.out.println("angle: " + angle);
                //finding quadrant
                if (ball.getCenterPosition().x < nearestBrick.getCenter().x && ball.getCenterPosition().y > nearestBrick.getCenter().y
                        || ball.getCenterPosition().x > nearestBrick.getCenter().x && ball.getCenterPosition().y < nearestBrick.getCenter().y) {
                    //quadrant top-left or bottom-right
                    if (angle > 0 && angle < 180) {  //old 135
                        System.out.println(1);
                        changeBallYDirection(center, r, minY);
                        System.out.println("----------------");
                    } else if (angle <= 360 && angle >= 180) { //old 225
                        System.out.println(2);
                        changeBallXDirection(center, r, minX);
                        System.out.println("----------------");
                    }
                } else {
                    //quadrant top-right or bottom-left
                    if (angle > 0 && angle < 180) { //old 135
                        System.out.println(3);
                        changeBallYDirection(center, r, minY);
                        System.out.println("----------------");
                    } else if (angle <= 360 && angle >= 180) { //old 225
                        System.out.println(4);
                        changeBallXDirection(center, r, minX);
                        System.out.println("----------------");
                    }
                }

                if (angle <= 5 && angle >= 355) {
                    System.out.println(5);
                    changeBallYDirection(center, r, minY);
                    ball.getVelocity().x *= -1;
                    System.out.println("----------------");
                }
            } else if ((top <= Ball.DIAMETER / 2 || bottom <= Ball.DIAMETER / 2) && minY < minX) {
                System.out.println(6);
                changeBallYDirection(center, r, minY);
                System.out.println("----------------");
            } else if ((left <= Ball.DIAMETER / 2 || right <= Ball.DIAMETER / 2)) {
                System.out.println(7);
                changeBallXDirection(center, r, minX);
                System.out.println("----------------");
            }
        }


        ball.checkPlatformIntersection(platform);
        if (ball.getPosition().y <= 0) {
            game.setScreen(new GameScreen(game));
//            ball.getVelocity().y *= -1;
        }
    }

    private void changeBallXDirection(Vector2 center, Rectangle r, float minX) {
        float shiftX = 2 * (Ball.DIAMETER / 2 - minX);
        ball.getVelocity().x *= -1;
        ball.getPosition().x += shiftX * 1.1 * (ball.getVelocity().x < 0 ? -1 : 1);
        System.out.println("X: " + shiftX);

        float top = Intersector.distanceSegmentPoint(r.x, r.y, r.x + Brick.WIDTH, r.y, center.x, center.y);
        float bottom = Intersector.distanceSegmentPoint(r.x, r.y + Brick.HEIGHT, r.x + Brick.WIDTH, r.y + Brick.HEIGHT, center.x, center.y);
        float  minY = Math.min(top, bottom);
        if (minY < Ball.DIAMETER / 2) {
            float shiftY = 2 * (Ball.DIAMETER / 2 - minY);
            ball.getPosition().y -= shiftY * 1.1 * (ball.getVelocity().y < 0 ? -1 : 1);
            System.out.println("+Y: " + shiftY);
        }
    }

    private void changeBallYDirection(Vector2 center, Rectangle r, float minY) {
        float shiftY = 2 * (Ball.DIAMETER / 2 - minY);
        ball.getVelocity().y *= -1;
        ball.getPosition().y += shiftY * 1.1 * (ball.getVelocity().y < 0 ? -1 : 1);
        System.out.println("Y: " + shiftY);

        float left = Intersector.distanceSegmentPoint(r.x, r.y, r.x + Brick.WIDTH, r.y + Brick.HEIGHT, center.x, center.y);
        float right = Intersector.distanceSegmentPoint(r.x + Brick.WIDTH, r.y, r.x + Brick.WIDTH, r.y + Brick.HEIGHT, center.x, center.y);
        float minX = Math.min(left, right);
        if (minX < Ball.DIAMETER / 2) {
            float shiftX = 2 * (Ball.DIAMETER / 2 - minX);
            ball.getPosition().x -= shiftX * 1.1 * (ball.getVelocity().x < 0 ? -1 : 1);
            System.out.println("+X: " + shiftX);
        }
    }

    private void spawnPowerUp(PowerUp.Type type, Vector2 position) {
        powerUps.add(new PowerUp(type, position));
    }

    private void updatePowerUps(float dt) {
        Array<PowerUp> destroyable = new Array<PowerUp>();
        for (PowerUp powerUp : powerUps) {
            powerUp.update(dt);
            if (Intersector.intersectSegments(powerUp.getLeftLine1(), powerUp.getLeftLine2(), platform.getLeftPoint(), platform.getRightPoint(), null)
                    || Intersector.intersectSegments(powerUp.getRightLine1(), powerUp.getRightLine2(), platform.getLeftPoint(), platform.getRightPoint(), null)) {
                // TODO: 5/27/16 add power up action
                destroyable.add(powerUp);
            }
            if (powerUp.getPosition().y + powerUp.getHeight() < 0) {
                destroyable.add(powerUp);
            }
        }
        for (PowerUp powerUp :destroyable) {
            powerUps.removeValue(powerUp, true);
            powerUp.dispose();
        }
    }

    private void handleInput() {
        if (Gdx.input.isTouched()) {
            touchPosition.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPosition);
            platform.updatePosition(touchPosition.x - Platform.WIDTH / 2);
//            if (touchPosition.x > platform.getPosition().x + Platform.WIDTH / 2 + 15) {
//                platform.updatePosition(platform.getPosition().x + Platform.SPEED * Gdx.graphics.getDeltaTime());
//            } else if (touchPosition.x < platform.getPosition().x + Platform.WIDTH / 2 - 15) {
//                platform.updatePosition(platform.getPosition().x - Platform.SPEED * Gdx.graphics.getDeltaTime());
//            }

            ball.setIsActive(true);
        }

        if (platform.getPosition().x < 0 - 2) {
            platform.updatePosition(0 - 2);
        }
        if (platform.getPosition().x > Arkanoid.WIDTH - Platform.WIDTH + 2) {
            platform.updatePosition(Arkanoid.WIDTH - Platform.WIDTH + 2);
        }
    }

    @Override
    public void resize(int width, int height) {

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
        for (int i = 0; i < bricks.size; i++) {
            bricks.get(i).dispose();
        }
        ball.dispose();
        platform.dispose();
        bg.dispose();
    }
}
