package ua.in.levor.arkanoid;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;

public class Brick {
    public static final int WIDTH = 24;
    public static final int HEIGHT = 24;
    public static final int HIT_RESIST_TIME = 200000000;

    private Texture texture;
    private int durability;
    private Vector2 position;
    private Vector2 center;
    private Rectangle rect;

    private long hitTime = 0;

    public Brick(int durability, Vector2 position) {
        this.durability = durability;
        this.position = position;

        rect = new Rectangle(position.x, position.y, WIDTH, HEIGHT);

        updateDurability(durability);
        center = new Vector2();
    }

    private void updateDurability(int durability) {
        switch (durability) {
            case 1 :
                texture = new Texture("BR_3.png");
                break;
            case 2 :
                texture = new Texture("BR_4.png");
                break;
            case 3 :
                texture = new Texture("BR_5.png");
                break;
            case 4 :
                texture = new Texture("BR_2.png");
                break;
            case 5 :
                texture = new Texture("BR_1.png");
                break;
            case 9 :
                texture = new Texture("BR_power.png");
                break;
        }
    }

    public Texture getTexture() {
        return texture;
    }

    public int getDurability() {
        return durability;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Rectangle getRect() {
        return rect;
    }

    public void dispose() {
        texture.dispose();
    }

    @Override
    public String toString() {
        return "position: " + position.x + " - " + position.y + " , durability: " + durability;
    }

    /**
     * @return true if diamond is destroyed, false otherwise
     */
    public State handleHit() {
        if (TimeUtils.nanoTime() - hitTime < HIT_RESIST_TIME) return State.ALIVE;
        hitTime = TimeUtils.nanoTime();

        if (durability == 1) {
            return State.DESTROYED;
        }

        if (durability == 9) {
            return State.SPAWN_BOMB;
        }
        durability--;
        updateDurability(durability);
        return State.ALIVE;
    }

    public boolean isHitResistEnabled() {
        return TimeUtils.nanoTime() - hitTime < HIT_RESIST_TIME;
    }

    public Vector2 getCenter() {
        return center.set(position.x + WIDTH / 2, position.y + HEIGHT / 2);
    }

    enum State {ALIVE, DESTROYED, SPAWN_BOMB}
}
