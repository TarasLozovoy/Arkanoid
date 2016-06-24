package ua.in.levor.arkanoid.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

import ua.in.levor.arkanoid.Arkanoid;
import ua.in.levor.arkanoid.Helpers.AssetsHelper;
import ua.in.levor.arkanoid.Helpers.SkillsHelper;

public class PowerUp extends Sprite {
    public static final int WIDTH = 24;

    private World world;
    public Body b2body;

    private Type type;
    private Vector2 center;

    private boolean isReady = false;

    public PowerUp(World world, Vector2 centerPosition) {
        this.world = world;
        this.center = centerPosition;

        setBounds(0, 0, Arkanoid.scale(WIDTH), Arkanoid.scale(WIDTH));

        Array<Type> activePowerUps = new Array<Type>();
        for (Type type : Type.values()) {
            if (type.isActive()) {
                activePowerUps.add(type);
            }
        }

        int totalProbability = 0;
        for (Type type : activePowerUps) {
            totalProbability += type.getProbability();
        }
        int rand = new Random().nextInt(totalProbability);
        for (Type type : activePowerUps) {
            totalProbability -= type.getProbability();
            if (totalProbability <= rand) {
                this.type = type;
                set(Arkanoid.adjustSize(new Sprite(type.getTexture())));
                break;
            }
        }

        definePowerUp();
    }

    private void definePowerUp() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(center);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(Arkanoid.scale(WIDTH / 2), Arkanoid.scale(WIDTH / 2));

        fdef.shape = shape;
        fdef.isSensor = true;
        Fixture fixture = b2body.createFixture(fdef);
        fixture.setUserData(this);

        b2body.setLinearVelocity(0, -1.5f);
    }

    public void readyToUse() {
        isReady = true;
    }

    public boolean isReady() {
        return isReady;
    }

    public Type getType() {
        return type;
    }

    public void update(float dt) {
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
    }

    public void dispose() {
        getTexture().dispose();
    }

    public enum Type {
        BOMB, STEEL_BALL, FIRE_BALL, MAGNET, SLOW_DOWN, FREEZE, SPEED_UP, ADD_BALL;

        public Texture getTexture() {
            switch (this) {
                case BOMB:
                    return new Texture(Gdx.files.internal(AssetsHelper.POWER_UP_BOMB));
                case STEEL_BALL:
                    return new Texture(Gdx.files.internal(AssetsHelper.POWER_UP_STEEL_BALL));
                case FIRE_BALL:
                    return new Texture(Gdx.files.internal(AssetsHelper.POWER_UP_FIRE_BALL));
                case MAGNET:
                    return new Texture(Gdx.files.internal(AssetsHelper.POWER_UP_MAGNET));
                case SLOW_DOWN:
                    return new Texture(Gdx.files.internal(AssetsHelper.POWER_UP_SLOW));
                case FREEZE:
                    return new Texture(Gdx.files.internal(AssetsHelper.POWER_UP_FREEZE));
                case SPEED_UP:
                    return new Texture(Gdx.files.internal(AssetsHelper.POWER_UP_SPEED_UP));
                case ADD_BALL:
                    return new Texture(Gdx.files.internal(AssetsHelper.POWER_UP_ADDITIONAL_BALL));
            }
            return null;
        }

        public int getDuration() {
            switch (this) {
                case STEEL_BALL:
                    return SkillsHelper.getInstance().getSteelBallDuration();
                case BOMB:
                    return SkillsHelper.getInstance().getBombDuration();
                case FIRE_BALL:
                case MAGNET:
                    return SkillsHelper.getInstance().getMagnetDuration();
                case FREEZE:
                    return SkillsHelper.getInstance().getFreezeBallDuration();
                default:
                    return 20;
            }
        }

        public boolean isActive() {
            switch (this) {
                case STEEL_BALL:
                    return SkillsHelper.getInstance().getSteelBallLevel() == 1;
                case BOMB:
                    return SkillsHelper.getInstance().getBombLevel() == 1;
                case FIRE_BALL:
                    return false;
                case MAGNET:
                    return SkillsHelper.getInstance().getMagnetLevel() == 1;
                case SLOW_DOWN:
                    return SkillsHelper.getInstance().getSlowDownLevel() == 1;
                case FREEZE:
                    return SkillsHelper.getInstance().getFreezeBallLevel() == 1;
                case SPEED_UP:
                    return SkillsHelper.getInstance().getSpeedUpLevel() == 1;
                case ADD_BALL:
                    return SkillsHelper.getInstance().getDuplicateBallLevel() == 1;
                default:
                    throw new RuntimeException("Unsupported type");
            }
        }

        public int getProbability() {
            switch (this) {
                case STEEL_BALL:
                    return 10;
                case FREEZE:
                    return 10;
                case SPEED_UP:
                    return 10;
                case SLOW_DOWN:
                    return 10;
                case MAGNET:
                    return 10;
                case ADD_BALL:
                    return 10;
                case BOMB:
                    return 10;
                default:
                    return 10;
            }
        }
    }
}
