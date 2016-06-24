package ua.in.levor.arkanoid.Tools;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.WorldManifold;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

import ua.in.levor.arkanoid.Helpers.SkillsHelper;
import ua.in.levor.arkanoid.Sprites.Ball;
import ua.in.levor.arkanoid.Sprites.Brick;
import ua.in.levor.arkanoid.Helpers.BrickHelper;
import ua.in.levor.arkanoid.Sprites.Platform;
import ua.in.levor.arkanoid.Sprites.PowerUp;

public class WorldContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();
        if (fixB.getUserData() != null && Ball.class.isAssignableFrom(fixB.getUserData().getClass()) ||
                fixA.getUserData() != null && Ball.class.isAssignableFrom(fixA.getUserData().getClass())) {
            Fixture ball = (fixA.getUserData() != null && Ball.class.isAssignableFrom(fixA.getUserData().getClass())) ? fixA : fixB;
            Fixture object = ball == fixA ? fixB : fixA;

            Ball ballObj = ((Ball)ball.getUserData());
            WorldManifold manifold = contact.getWorldManifold();
            if (manifold == null || manifold.getNumberOfContactPoints() < 1) return;
            Vector2 intersection = manifold.getPoints()[manifold.getNumberOfContactPoints() - 1];

            //ball - brick
            if (object.getUserData() != null && Brick.class.isAssignableFrom(object.getUserData().getClass())) {
                Brick brick = ((Brick)object.getUserData());

                Random random = new Random();

                boolean wallHit = brick.getType() == Brick.Type.HALF_WALL && intersection.y < brick.getBody().getPosition().y
                        || brick.getType() == Brick.Type.WALL;
                boolean bombDestroysWall = ballObj.getPowerUpType() == PowerUp.Type.BOMB && SkillsHelper.getInstance().isBombDestroysWall();
                if (!bombDestroysWall && wallHit && ballObj.getPowerUpType() != PowerUp.Type.FREEZE) return;

                if (ballObj.getPowerUpType() == PowerUp.Type.BOMB) {
                    for (Brick b : BrickHelper.getInstance().getAllBricksInRadius(SkillsHelper.getInstance().getBombRadius(), intersection)) {
                        b.handleHit();
                        if (random.nextFloat() < SkillsHelper.getInstance().getBombShrapnelChance() && !b.isReadyToDestroy()) {
                            b.handleHit();
                        }
                    }
                } else if (ballObj.getPowerUpType() == PowerUp.Type.STEEL_BALL) {
                    brick.handleHit();
                    brick.handleHit();
                    if (random.nextFloat() < SkillsHelper.getInstance().getSteelBallPunchThroughChance()) {
                        Array<Brick> neighbourBricks = new Array<Brick>();
                        for (Brick b : BrickHelper.getInstance().getAllBricksInRadius(40, intersection)) {
                            neighbourBricks.add(b);
                        }
                        neighbourBricks.get(random.nextInt(neighbourBricks.size)).handleHit();
                    }
                } else if (ballObj.getPowerUpType() == PowerUp.Type.FIRE_BALL) {
                    brick.handleHit();
                    brick.destroy();
                } else if (ballObj.getPowerUpType() == PowerUp.Type.FREEZE
                        && brick.getType() != Brick.Type.POWER
                        && brick.getType() != Brick.Type.OOPS
                        && brick.getType() != Brick.Type.TNT) {
                    brick.frozeBrick(SkillsHelper.getInstance().getFreezeBallFreezeChainLength(), -1);
                } else {
                    brick.handleHit();
                }

                if (brick.getType() == Brick.Type.SPEED_UP) {
                    ballObj.changeSpeed(1.1f);
                } else if (brick.getType() == Brick.Type.SLOW_DOWN) {
                    ballObj.changeSpeed(0.9f);
                }
                else if (brick.getType() == Brick.Type.OOPS) {
                    if (random.nextFloat() > SkillsHelper.getInstance().getDeactivateOopsBlockLevelChance()) {
                        BrickHelper.getInstance().handleOopsBlockHit();
                    } else {
                        // TODO: 6/22/16 add sound of deactivated oops block
                    }
                }
            } else if (object.getUserData() != null && Platform.class.isAssignableFrom(object.getUserData().getClass())) {
                //ball - platform
                Platform platform = (Platform) object.getUserData();
                if (ballObj.getPowerUpType() == PowerUp.Type.MAGNET) {
                    ballObj.setIsActive(false);
                    ballObj.b2body.setLinearVelocity(0, 0);
                    ballObj.nonActiveBallXDiff = intersection.x - (platform.b2body.getPosition().x + platform.getWidth() / 2);
                }
            }
        }
        //powerUp - platform
        if (fixB.getUserData() != null && PowerUp.class.isAssignableFrom(fixB.getUserData().getClass()) ||
                fixA.getUserData() != null && PowerUp.class.isAssignableFrom(fixA.getUserData().getClass())) {
            Fixture powerUp = PowerUp.class.isAssignableFrom(fixA.getUserData().getClass()) ? fixA : fixB;
            Fixture object = powerUp == fixA ? fixB : fixA;
            if (object.getUserData() != null && Platform.class.isAssignableFrom(object.getUserData().getClass())) {
                ((PowerUp) powerUp.getUserData()).readyToUse();
            }
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
