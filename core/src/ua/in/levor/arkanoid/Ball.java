package ua.in.levor.arkanoid;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Ball {
    public static final int DIAMETER = 20;
    public static final int DEFAULT_VELOCITY_X = 110;
    public static final int DEFAULT_VELOCITY_y = 150;

    private Texture texture;
    private Vector2 position;
    private Vector2 velocity;
    private Vector2 centerPosition;

    private boolean isActive = false;

    public Ball(Vector2 position, Vector2 velocity) {
        this.position = position;
        this.velocity = velocity;

        texture = new Texture("ball22.png");
        centerPosition = new Vector2();
    }

    public Vector2 getCenterPosition() {
        return centerPosition.set(position.x + DIAMETER / 2, position.y + DIAMETER / 2);
    }

    public Texture getTexture() {
        return texture;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void dispose() {
        texture.dispose();
    }

    public void setIsActive(boolean isActive) {
        if (this.isActive != isActive) {
            this.isActive = isActive;
        }
    }

    public void update(float dt) {
        if (isActive) {
            float translationX = velocity.x * dt;
            float translationY = velocity.y * dt;

            position.x += translationX;
            position.y += translationY;

            if (position.x + DIAMETER >= Arkanoid.WIDTH) {
                velocity.x *= -1;
                position.x = Arkanoid.WIDTH - DIAMETER;
            }
            if (position.x <= 0) {
                velocity.x *= -1;
                position.x = 0;
            }
            if (position.y + DIAMETER >= Arkanoid.HEIGHT) {
                velocity.y *= -1;
                position.y = Arkanoid.HEIGHT - DIAMETER;
            }
        }
    }

    public void checkPlatformIntersection(Platform platform) {
        Vector2 plPos = platform.getPosition();
        if (Intersector.distanceSegmentPoint(plPos.x, plPos.y + Platform.HEIGHT, plPos.x + Platform.WIDTH, plPos.y + Platform.HEIGHT,
                getCenterPosition().x, getCenterPosition().y) <= DIAMETER / 2) {

            Vector2 intersection = new Vector2();
            if (Intersector.intersectSegments(plPos.x, plPos.y + Platform.HEIGHT, plPos.x + Platform.WIDTH, plPos.y + Platform.HEIGHT,
                    getCenterPosition().x, getCenterPosition().y, getCenterPosition().x + DIAMETER / 2, getCenterPosition().y + DIAMETER / 2, intersection)) {
                float speed = (float) Math.sqrt(velocity.x * velocity.x + velocity.y * velocity.y);

                float segment;
                boolean isLeft;
                if (plPos.x + Platform.WIDTH / 2 > intersection.x) {                    //check if hit in left part of platform
                    segment = (plPos.x + Platform.WIDTH / 2 - intersection.x) / (intersection.x - plPos.x);
                    isLeft = true;
                } else {         //check if hit in right part of platform
                    segment = (intersection.x - plPos.x - Platform.WIDTH / 2) / (plPos.x + Platform.WIDTH - intersection.x);
                    isLeft = false;
                }
                float angle;
                if (segment < 1/4) {
                    angle = ((isLeft ? 95 : 90)) * MathUtils.degreesToRadians;
                } else if (segment < 2/3) {
                    angle = ((isLeft ? 110 : 70)) * MathUtils.degreesToRadians;
                } else if (segment < 3/2) {
                    angle = ((isLeft ? 125 : 55)) * MathUtils.degreesToRadians;
                } else if (segment < 4) {
                    angle = ((isLeft ? 140 : 40)) * MathUtils.degreesToRadians;
                } else {
                    angle = ((isLeft ? 160 : 20)) * MathUtils.degreesToRadians;
                }
                velocity.set(speed * MathUtils.cos(angle), speed * MathUtils.sin(angle));
//                velocity.y *= -1;
            }
        }
    }
    public Vector2 getVelocity() {
        return velocity;
    }
}
