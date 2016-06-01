package ua.in.levor.arkanoid;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Platform extends Sprite {
    public static final int WIDTH = 100;
    public static final int HEIGHT = 20;

    public World world;
    public Body b2body;

    public Platform(World world) {
        super(Arkanoid.adjustSize(new Sprite(new Texture(Gdx.files.internal("CRASH.png")))));
        this.world = world;
        definePlatform();

        setBounds(0, 0, Arkanoid.scale(WIDTH), Arkanoid.scale(HEIGHT));
        float xBody = b2body.getPosition().x - getWidth() / 2;
        float yBody = b2body.getPosition().y - getHeight() / 2;
        setPosition(xBody, yBody);
    }

    private void definePlatform() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(Arkanoid.scale(Arkanoid.WIDTH / 2), Arkanoid.scale(10));
        bdef.type = BodyDef.BodyType.StaticBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(Arkanoid.scale(WIDTH / 2), Arkanoid.scale(HEIGHT / 2));
        fdef.filter.categoryBits = Arkanoid.PLATFORM_BIT;
        fdef.density = 1f;

        fdef.shape = shape;
        Fixture fixture = b2body.createFixture(fdef);
        fixture.setUserData(this);
    }

    public void dispose() {
        getTexture().dispose();
    }

    public void updatePosition(float x) {
        b2body.setTransform(x, b2body.getPosition().y, b2body.getAngle());

        if (b2body.getPosition().x - Arkanoid.scale(WIDTH / 2) < 0) {
            b2body.setTransform(0 + Arkanoid.scale(WIDTH / 2), b2body.getPosition().y, b2body.getAngle());
        }
        if (b2body.getPosition().x + Arkanoid.scale(WIDTH / 2) > Arkanoid.scale(Arkanoid.WIDTH)) {
            b2body.setTransform(Arkanoid.scale(Arkanoid.WIDTH) - Arkanoid.scale(WIDTH / 2), b2body.getPosition().y, b2body.getAngle());
        }

        float xBody = b2body.getPosition().x - getWidth() / 2;
        float yBody = b2body.getPosition().y - getHeight() / 2;
        setPosition(xBody, yBody);
    }
}
