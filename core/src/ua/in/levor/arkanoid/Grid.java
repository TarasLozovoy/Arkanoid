package ua.in.levor.arkanoid;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Grid {
    private Array<Brick> bricks = new Array<Brick>();

    public Grid(Array<String> values) {
        Vector2 position = new Vector2(0, Arkanoid.HEIGHT - Brick.HEIGHT * 3);
        for (String row: values) {
            for (String s : row.split("")) {
                if (s != null && s.length() > 0) {
                    int i = Integer.parseInt(s);
                    if (i > 0) {
                        Brick b = new Brick(i, new Vector2(position));
                        bricks.add(b);
                    }
                    position.x += Brick.WIDTH;
                }
            }
            position.y -= Brick.HEIGHT;
            position.x = 0;
        }
    }

    public Array<Brick> getBricks() {
        return bricks;
    }

    public Brick getNearestBrick(Vector2 ballCenter) {
        Brick nearestBrick = null;
        float minDistance = Float.MAX_VALUE;
        for (Brick d : bricks) {
            Rectangle r = d.getRect();
            float top = Intersector.distanceSegmentPoint(r.x, r.y, r.x + Brick.WIDTH, r.y, ballCenter.x, ballCenter.y);
            float bottom = Intersector.distanceSegmentPoint(r.x, r.y + Brick.HEIGHT, r.x + Brick.WIDTH, r.y + Brick.HEIGHT, ballCenter.x, ballCenter.y);
            float left = Intersector.distanceSegmentPoint(r.x, r.y, r.x + Brick.WIDTH, r.y + Brick.HEIGHT, ballCenter.x, ballCenter.y);
            float right = Intersector.distanceSegmentPoint(r.x + Brick.WIDTH, r.y, r.x + Brick.WIDTH, r.y + Brick.HEIGHT, ballCenter.x, ballCenter.y);

            float minY = Math.min(top, bottom);
            float minX = Math.min(left, right);
            float min = Math.min(minX, minY);

            if (min < minDistance) {
                minDistance = min;
                nearestBrick = d;
            }
        }

        return  nearestBrick;
    }
}
