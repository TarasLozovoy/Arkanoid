package ua.in.levor.arkanoid.Tools;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.WorldManifold;
import com.badlogic.gdx.utils.Array;

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
            if (object.getUserData() != null && Brick.class.isAssignableFrom(object.getUserData().getClass())) {
                Brick brick = ((Brick)object.getUserData());
                Ball ballObj = ((Ball)ball.getUserData());

                WorldManifold manifold = contact.getWorldManifold();
                Vector2 intersection = manifold.getPoints()[manifold.getNumberOfContactPoints() - 1];

                boolean wallHit = brick.getType() == Brick.Type.HALF_WALL && intersection.y < brick.getBody().getPosition().y
                        || brick.getType() == Brick.Type.WALL;
                if (wallHit && ballObj.getPowerUpType() != PowerUp.Type.FREEZE) return;

                if (ballObj.getPowerUpType() == PowerUp.Type.BOMB) {
                    for (Brick b : BrickHelper.getInstance().getAllBricksInRadius(PowerUp.BOMB_BURST_RADIUS, intersection)) {
                        b.handleHit();
                    }
                } else if (ballObj.getPowerUpType() == PowerUp.Type.STEEL_BALL) {
                    brick.handleHit();
                    brick.handleHit();
                } else if (ballObj.getPowerUpType() == PowerUp.Type.FIRE_BALL) {
                    brick.handleHit();
                    brick.destroy();
                } else if (ballObj.getPowerUpType() == PowerUp.Type.FREEZE && brick.getType() != Brick.Type.ICE
                        && brick.getType() != Brick.Type.POWER) {
                    // TODO: 6/8/16 make brick frozen
                    brick.frozeBrick();
                } else {
                    brick.handleHit();
                }

                if (brick.getType() == Brick.Type.SPEED_UP) {
                    ballObj.changeSpeed(1.1f);
                } else if (brick.getType() == Brick.Type.SLOW_DOWN) {
                    ballObj.changeSpeed(0.9f);
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
