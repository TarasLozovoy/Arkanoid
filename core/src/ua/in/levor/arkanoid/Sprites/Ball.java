package ua.in.levor.arkanoid.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import java.util.Random;

import ua.in.levor.arkanoid.Arkanoid;
import ua.in.levor.arkanoid.Helpers.AssetsHelper;

public class Ball extends Sprite {
    public static final int DIAMETER = 18;
    public static final int RADIUS = DIAMETER / 2;
    public static final float DEFAULT_VELOCITY_X = 0.5f;
    public static final float DEFAULT_VELOCITY_y = 2.5f;

    public World world;
    public Body b2body;

    private Vector2 position;
    private PowerUp.Type powerUpType = null;
    private boolean isActive = false;
    private float absSpeed;

    public Ball(World world, float x, float y) {
        this(world, new Vector2(x, y));
    }

    public Ball(World world, Vector2 position) {
        super(Arkanoid.adjustSize(new Sprite(new Texture(Gdx.files.internal(AssetsHelper.BALL_PART_1)))));
        setScale(24 / DIAMETER);
        this.world = world;
        this.position = position;
        defineBall();

        setBounds(position.x, position.y, Arkanoid.scale(DIAMETER), Arkanoid.scale(DIAMETER));
        setOrigin(Arkanoid.scale(RADIUS), Arkanoid.scale(RADIUS));
    }

    private void defineBall() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(Arkanoid.scale(position.x), Arkanoid.scale(position.y));
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(Arkanoid.scale(RADIUS));
        fdef.filter.categoryBits = Arkanoid.BALL_BIT;
        fdef.filter.maskBits = Arkanoid.DEFAULT_BIT | Arkanoid.PLATFORM_BIT | Arkanoid.BRICK_BIT;
        fdef.restitution = 1f;
        fdef.friction = 0f;
        fdef.density = 0f;

        fdef.shape = shape;
        Fixture fixture = b2body.createFixture(fdef);
        fixture.setUserData(this);
    }

    public void update(float dt) {
        float x = b2body.getPosition().x - getWidth() / 2;
        float y = b2body.getPosition().y - getHeight() / 2;
        if (x < 0) {
            x = 0;
            b2body.setLinearVelocity(b2body.getLinearVelocity().x * -1, b2body.getLinearVelocity().y);
        } else if (x + getWidth() > Arkanoid.scale(Arkanoid.WIDTH)) {
            x = Arkanoid.scale(Arkanoid.WIDTH) - getWidth();
            b2body.setLinearVelocity(b2body.getLinearVelocity().x * -1, b2body.getLinearVelocity().y);
        }

        if (y + getHeight() > Arkanoid.scale(Arkanoid.HEIGHT - 30)) {
            y = Arkanoid.scale(Arkanoid.HEIGHT - 30) - getHeight();
            b2body.setLinearVelocity(b2body.getLinearVelocity().x, b2body.getLinearVelocity().y * -1);
        }
//        else if (y < 0) {
//            y = 0;
//            b2body.setLinearVelocity(b2body.getLinearVelocity().x, b2body.getLinearVelocity().y * -1);
//        }

        b2body.setTransform(x + getWidth() / 2, y + getHeight() / 2, b2body.getAngle());

        float velocityX = Math.abs(b2body.getLinearVelocity().x);
        float velocityY = Math.abs(b2body.getLinearVelocity().y);
        System.out.println(" x: " + velocityX + "y: " + velocityY);
        System.out.println(" xpos: " + x + "ypos: " + y);

        if (Math.abs(velocityY/(velocityX + velocityY)) < 0.15) {
            float difX = velocityX * 0.01f;
            b2body.setLinearVelocity(b2body.getLinearVelocity().x + (b2body.getLinearVelocity().x > 0 ? -difX : difX),
                    b2body.getLinearVelocity().y + (b2body.getLinearVelocity().y > 0 ? difX : -difX) * 1.15f);
            System.out.println("Low Y speed! x: " + velocityX + "y: " + velocityY);
        } else if (Math.abs(velocityX) < 0.5) {
            b2body.setLinearVelocity(b2body.getLinearVelocity().x * 1.1f,
                    b2body.getLinearVelocity().y);
        }

        //normalizing speed
        if (calcAbsSpeed() < absSpeed * 0.97) {
            b2body.setLinearVelocity(b2body.getLinearVelocity().x * 1.02f, b2body.getLinearVelocity().y * 1.02f);
            System.out.println("Increasing speed! x: " + b2body.getLinearVelocity().x + "y: " + b2body.getLinearVelocity().y
                    + " current: " + calcAbsSpeed() + " needed: " + absSpeed);
        } else if (calcAbsSpeed() > absSpeed / 0.97) {
            b2body.setLinearVelocity(b2body.getLinearVelocity().x / 1.02f, b2body.getLinearVelocity().y / 1.02f);
            System.out.println("Decreasing speed! x: " + b2body.getLinearVelocity().x + "y: " + b2body.getLinearVelocity().y
                    + " current: " + calcAbsSpeed() + " needed: " + absSpeed);
        }

        setPosition(x, y);
        if (isActive) {
            rotate(0.5f);
        }
    }

    public void dispose() {
        getTexture().dispose();
    }

    public void setIsActive(boolean isActive) {
        if (!this.isActive && isActive) {
            this.isActive = isActive;
            b2body.applyLinearImpulse(new Vector2(DEFAULT_VELOCITY_X, DEFAULT_VELOCITY_y), b2body.getWorldCenter(), true);
            absSpeed = calcAbsSpeed();
        }
    }

    public boolean isActive() {
        return isActive;
    }

    public Ball cloneBall() {
        float x = b2body.getPosition().x - getWidth() / 2;
        float y = b2body.getPosition().y - getHeight() / 2;
        Ball ball = new Ball(world, Arkanoid.unscale(x), Arkanoid.unscale(y));
        float random = new Random().nextFloat();
        float xVelocity = b2body.getLinearVelocity().x;
        float yVelocity = b2body.getLinearVelocity().y;
        ball.b2body.applyLinearImpulse(new Vector2(-(xVelocity + (xVelocity > 0 ? +random : -random)),
                        yVelocity + (yVelocity > 0 ? -random : +random)),
                b2body.getWorldCenter(), true);
        ball.isActive = true;
        ball.absSpeed = absSpeed;
        return ball;
    }

    public boolean isBelowYZero() {
        return b2body.getPosition().y - getHeight() / 2 < 0;
    }

    public void setPowerUp(PowerUp.Type type) {
        this.powerUpType = type;
    }

    public PowerUp.Type getPowerUpType() {
        return powerUpType;
    }

    private float calcAbsSpeed() {
        return (float) Math.sqrt(Math.pow(b2body.getLinearVelocity().x, 2) + Math.pow(b2body.getLinearVelocity().y, 2));
    }

    public void changeSpeed(float mult) {
        absSpeed *= mult;
    }

    public void hide() {
        setAlpha(0f);
    }
}
