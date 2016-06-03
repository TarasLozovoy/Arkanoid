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

import ua.in.levor.arkanoid.Arkanoid;

public class Ball extends Sprite {
    public static final int DIAMETER = 18;
    public static final int RADIUS = DIAMETER / 2;
    public static final float DEFAULT_VELOCITY_X = 2f;
    public static final float DEFAULT_VELOCITY_y = 1.9f;

    public World world;
    public Body b2body;

    private PowerUp.Type powerUpType = null;
    private boolean isActive = false;

    public Ball(World world) {
        super(Arkanoid.adjustSize(new Sprite(new Texture(Gdx.files.internal("ball22.png")))));
        setScale(24 / DIAMETER);
        this.world = world;
        defineBall();

        setBounds(0, 0, Arkanoid.scale(DIAMETER), Arkanoid.scale(DIAMETER));
        setOrigin(Arkanoid.scale(RADIUS), Arkanoid.scale(RADIUS));
    }

    private void defineBall() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(Arkanoid.scale(Arkanoid.WIDTH / 2), Arkanoid.scale(30.5f));
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

        if (y + getHeight() > Arkanoid.scale(Arkanoid.HEIGHT)) {
            y = Arkanoid.scale(Arkanoid.HEIGHT) - getHeight();
            b2body.setLinearVelocity(b2body.getLinearVelocity().x, b2body.getLinearVelocity().y * -1);
        }
//        else if (y < 0) {
//            y = 0;
//            b2body.setLinearVelocity(b2body.getLinearVelocity().x, b2body.getLinearVelocity().y * -1);
//        }


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
        }
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
}
