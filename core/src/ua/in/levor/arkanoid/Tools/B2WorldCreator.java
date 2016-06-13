package ua.in.levor.arkanoid.Tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;

import ua.in.levor.arkanoid.Sprites.Brick;
import ua.in.levor.arkanoid.Helpers.BrickHelper;

public class B2WorldCreator{
    public static final int RED_BRICKS_LAYER = 2;
    public static final int GRAY_BRICKS_LAYER = 3;
    public static final int YELLOW3_BRICKS_LAYER = 4;
    public static final int YELLOW2_BRICKS_LAYER = 5;
    public static final int DURABILITY_1_BRICKS_LAYER = 6;
    public static final int POWERUP_BRICKS_LAYER = 1;
    public static final int UNDESTROYABLE_WALL_BRICKS_LAYER = 7;
    public static final int SLOW_DOWN_BRICKS_LAYER = 8;
    public static final int SPEED_UP_BRICKS_LAYER = 9;
    public static final int HALF_WALL_BRICKS_LAYER = 10;
    public static final int ICE_BRICKS_LAYER = 11;
    public static final int TNT_BRICKS_LAYER = 12;
    public static final int OOPS_BRICKS_LAYER = 13;

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

        //creates yellow3 bricks body and fixtures
        for (MapObject object : map.getLayers().get(YELLOW3_BRICKS_LAYER).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject)object).getRectangle();
            brickHelper.addBrick(new Brick(world, map, rect, Brick.Type.BROWN3));
        }

        //creates yellow2 bricks body and fixtures
        for (MapObject object : map.getLayers().get(YELLOW2_BRICKS_LAYER).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject)object).getRectangle();
            brickHelper.addBrick(new Brick(world, map, rect, Brick.Type.BROWN2));
        }

        //creates yellow1 bricks body and fixtures
        for (MapObject object : map.getLayers().get(DURABILITY_1_BRICKS_LAYER).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject)object).getRectangle();
            brickHelper.addBrick(new Brick(world, map, rect, Brick.Type.DURABILITY1));
        }

        //creates yellow bricks body and fixtures
        for (MapObject object : map.getLayers().get(POWERUP_BRICKS_LAYER).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject)object).getRectangle();
            brickHelper.addBrick(new Brick(world, map, rect, Brick.Type.POWER));
        }

        //creates walls bricks body and fixtures
        for (MapObject object : map.getLayers().get(UNDESTROYABLE_WALL_BRICKS_LAYER).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject)object).getRectangle();
            brickHelper.addBrick(new Brick(world, map, rect, Brick.Type.WALL));
        }

        //creates speed up bricks body and fixtures
        for (MapObject object : map.getLayers().get(SPEED_UP_BRICKS_LAYER).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject)object).getRectangle();
            brickHelper.addBrick(new Brick(world, map, rect, Brick.Type.SPEED_UP));
        }

        //creates slow down bricks body and fixtures
        for (MapObject object : map.getLayers().get(SLOW_DOWN_BRICKS_LAYER).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject)object).getRectangle();
            brickHelper.addBrick(new Brick(world, map, rect, Brick.Type.SLOW_DOWN));
        }

        for (MapObject object : map.getLayers().get(DURABILITY_1_BRICKS_LAYER).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject)object).getRectangle();
            brickHelper.addBrick(new Brick(world, map, rect, Brick.Type.DURABILITY1));
        }

        for (MapObject object : map.getLayers().get(HALF_WALL_BRICKS_LAYER).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject)object).getRectangle();
            brickHelper.addBrick(new Brick(world, map, rect, Brick.Type.HALF_WALL));
        }

        for (MapObject object : map.getLayers().get(ICE_BRICKS_LAYER).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject)object).getRectangle();
            brickHelper.addBrick(new Brick(world, map, rect, Brick.Type.ICE));
        }

        for (MapObject object : map.getLayers().get(TNT_BRICKS_LAYER).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject)object).getRectangle();
            brickHelper.addBrick(new Brick(world, map, rect, Brick.Type.TNT));
        }

        for (MapObject object : map.getLayers().get(OOPS_BRICKS_LAYER).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject)object).getRectangle();
            brickHelper.addBrick(new Brick(world, map, rect, Brick.Type.OOPS));
        }
    }
}
