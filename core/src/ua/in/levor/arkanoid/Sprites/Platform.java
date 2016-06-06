package ua.in.levor.arkanoid.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import ua.in.levor.arkanoid.Arkanoid;
import ua.in.levor.arkanoid.Tools.BodyEditorLoader;

public class Platform extends Sprite {
    public static final int WIDTH = 100;
    public static final int HEIGHT = 20;

    public World world;
    public Body b2body;

    public Platform(World world) {
        super(Arkanoid.adjustSize(new Sprite(new Texture(Gdx.files.internal("basic_platform.png")))));
        this.world = world;
        definePlatform();

        setBounds(0, 0, Arkanoid.scale(WIDTH), Arkanoid.scale(HEIGHT));
        float xBody = b2body.getPosition().x;
        float yBody = b2body.getPosition().y;
        setPosition(xBody, yBody);
    }

    private void definePlatform() {
        BodyEditorLoader loader = new BodyEditorLoader(Gdx.files.internal("Platform/basic_platform"));

        BodyDef bdef = new BodyDef();
        bdef.position.set(Arkanoid.scale(Arkanoid.WIDTH / 2 - WIDTH / 2), Arkanoid.scale(10));
        bdef.type = BodyDef.BodyType.StaticBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
//        PolygonShape shape = new PolygonShape();
//        shape.setAsBox(Arkanoid.scale(WIDTH / 2), Arkanoid.scale(HEIGHT / 2));
        fdef.filter.categoryBits = Arkanoid.PLATFORM_BIT;
        fdef.density = 1f;

        loader.attachFixture(b2body, "basic_platform", fdef, Arkanoid.scale(WIDTH));

//        fdef.shape = shape;
//        Fixture fixture = b2body.createFixture(fdef);
        Array<Fixture> fixtures = b2body.getFixtureList();
        for (int i = 0; i < fixtures.size; i++) {
            fixtures.get(i).setUserData(this);
        }

    }

    public void dispose() {
        getTexture().dispose();
    }

    public void updatePosition(float x) {
        b2body.setTransform(x, b2body.getPosition().y, b2body.getAngle());

        if (b2body.getPosition().x < 0) {
            b2body.setTransform(0, b2body.getPosition().y, b2body.getAngle());
        }
        if (b2body.getPosition().x + Arkanoid.scale(WIDTH) > Arkanoid.scale(Arkanoid.WIDTH)) {
            b2body.setTransform(Arkanoid.scale(Arkanoid.WIDTH) - Arkanoid.scale(WIDTH), b2body.getPosition().y, b2body.getAngle());
        }

        float xBody = b2body.getPosition().x;
        float yBody = b2body.getPosition().y;
        setPosition(xBody, yBody);
    }
}
