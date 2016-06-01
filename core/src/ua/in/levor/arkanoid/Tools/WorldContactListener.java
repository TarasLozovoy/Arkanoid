package ua.in.levor.arkanoid.Tools;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import ua.in.levor.arkanoid.Ball;
import ua.in.levor.arkanoid.Brick;
import ua.in.levor.arkanoid.Platform;
import ua.in.levor.arkanoid.PowerUps.PowerUp;

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
                brick.handleHit();
                Ball ballObj = ((Ball)ball.getUserData());
                if (ballObj.getPowerUpType() == PowerUp.Type.BOMB) {
                    if (!brick.isDestroyed()) {
                        brick.handleHit();
                    }
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
