package ua.in.levor.arkanoid;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Platform {
    public static final int WIDTH = 100;
    public static final int HEIGHT = 20;
    public static final int SPEED = 1500;

    private Texture texture;
    private Vector2 position;
    private Rectangle hitArea;

    private Vector2 rightPoint;
    private Vector2 leftPoint;

    public Platform(Vector2 position) {
        this.position = position;

        texture = new Texture("CRASH.png");
        rightPoint = new Vector2();
        leftPoint = new Vector2();
    }

    public Vector2 getPosition() {
        return position;
    }

    public Texture getTexture() {
        return texture;
    }

    public void dispose() {
        texture.dispose();
    }

    public void updatePosition(float x) {
        position.x = x;
    }

    public Vector2 getRightPoint() {
        return rightPoint.set(position.x, position.y);
    }

    public Vector2 getLeftPoint() {
        return leftPoint.set(position.x + WIDTH, position.y);
    }
}
