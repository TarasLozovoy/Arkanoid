package ua.in.levor.arkanoid.Tools;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import ua.in.levor.arkanoid.Ball;
import ua.in.levor.arkanoid.Brick;

public class WorldContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();
        if (fixB.getUserData().equals(Ball.BALL_IDENTIFIER) ||
                fixA.getUserData().equals(Ball.BALL_IDENTIFIER)) {
            Fixture ball = (fixA.getUserData() != null && fixA.getUserData().equals(Ball.BALL_IDENTIFIER)) ? fixA : fixB;
            Fixture object = ball == fixA ? fixB : fixA;
            if (object.getUserData() != null && Brick.class.isAssignableFrom(object.getUserData().getClass())) {
                ((Brick)object.getUserData()).handleHit();
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
