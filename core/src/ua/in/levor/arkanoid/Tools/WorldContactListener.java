package ua.in.levor.arkanoid.Tools;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.WorldManifold;

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

                if (ballObj.getPowerUpType() == PowerUp.Type.BOMB) {
                    WorldManifold manifold = contact.getWorldManifold();
                    Vector2 intersection = manifold.getPoints()[manifold.getNumberOfContactPoints() - 1];
                    for (Brick b : BrickHelper.getInstance().getAllBricksInRadius(PowerUp.BOMB_BURST_RADIUS, intersection)) {
                        b.handleHit();
                    }
                } else if (ballObj.getPowerUpType() == PowerUp.Type.STEEL_BALL) {
                    brick.handleHit();
                    brick.handleHit();
                } else if (ballObj.getPowerUpType() == PowerUp.Type.FIRE_BALL) {
                    brick.handleHit();
                    brick.destroy();
                } else {
                    brick.handleHit();
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
