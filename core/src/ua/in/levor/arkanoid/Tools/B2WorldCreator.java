package ua.in.levor.arkanoid.Tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;

import ua.in.levor.arkanoid.Sprites.Brick;
import ua.in.levor.arkanoid.Helpers.BrickHelper;

public class B2WorldCreator{
    public static final int RED_BRICKS_LAYER = 1;
    public static final int GRAY_BRICKS_LAYER = 2;
    public static final int YELLOW_BRICKS_LAYER = 3;
    public static final int POWERUP_BRICKS_LAYER = 4;

    private BrickHelper brickHelper;
    public B2WorldCreator(World world, TiledMap map) {
        brickHelper = BrickHelper.getInstance();
        //creates red bricks body and fixtures
        for (MapObject object : map.getLayers().get(RED_BRICKS_LAYER).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject)object).getRectangle();
            brickHelper.addBrick(new Brick(world, map, rect, Brick.Type.RED));

        }

        //creates gray bricks body and fixtures
        for (MapObject object : map.getLayers().get(GRAY_BRICKS_LAYER).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject)object).getRectangle();
            brickHelper.addBrick(new Brick(world, map, rect, Brick.Type.GRAY));
        }

        //creates yellow bricks body and fixtures
        for (MapObject object : map.getLayers().get(YELLOW_BRICKS_LAYER).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject)object).getRectangle();
            brickHelper.addBrick(new Brick(world, map, rect, Brick.Type.YELLOW3));
        }

        //creates yellow bricks body and fixtures
        for (MapObject object : map.getLayers().get(POWERUP_BRICKS_LAYER).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject)object).getRectangle();
            brickHelper.addBrick(new Brick(world, map, rect, Brick.Type.POWER));
        }
    }
}
