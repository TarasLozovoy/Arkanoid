package ua.in.levor.arkanoid;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class PowerUp {
    public static final int GRAVITY = 150;

    private Type type;
    private Vector2 position;
    private Texture texture;
    private int width;
    private int height;

    private Vector2 leftLine1;
    private Vector2 leftLine2;
    private Vector2 rightLine1;
    private Vector2 rightLine2;

    public PowerUp(Type type, Vector2 position) {
        this.type = type;
        this.position = position;

        switch (type) {
            case BOMB:
                texture = new Texture("bomb.png");
                width = height = 40;
                break;
        }

        leftLine1 = new Vector2();
        leftLine2 = new Vector2();
        rightLine1 = new Vector2();
        rightLine2 = new Vector2();
    }

    public void update(float dt) {
        position.y -= GRAVITY * dt;
    }

    public Type getType() {
        return type;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Texture getTexture() {
        return texture;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Vector2 getLeftLine1() {
        return leftLine1.set(position.x, position.y);
    }

    public Vector2 getLeftLine2() {
        return leftLine2.set(position.x, position.y + height);
    }

    public Vector2 getRightLine1() {
        return rightLine1.set(position.x + width, position.y);
    }

    public Vector2 getRightLine2() {
        return rightLine2.set(position.x + width, position.y + height);
    }

    public void dispose() {
        texture.dispose();
    }

    enum Type {BOMB}
}
