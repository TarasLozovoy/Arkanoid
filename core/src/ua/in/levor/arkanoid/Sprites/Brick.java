package ua.in.levor.arkanoid.Sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import java.util.Random;

import ua.in.levor.arkanoid.Arkanoid;
import ua.in.levor.arkanoid.Helpers.GameHelper;
import ua.in.levor.arkanoid.Helpers.PowerUpHelper;
import ua.in.levor.arkanoid.Helpers.SkillsHelper;

public class Brick {
    public static final int TILE_SIZE = 24;
    private TiledMapTileSet tileSet;

    protected World world;
    protected TiledMap map;
    protected TiledMapTile tile;
    protected Rectangle bounds;
    protected Body body;
    protected Fixture fixture;

    private Type type;
    private boolean destroy = false;

    public Brick(World world, TiledMap map, Rectangle bounds, Type type) {
        this.world = world;
        this.map = map;
        this.bounds = bounds;
        this.type = type;

        tileSet = map.getTileSets().getTileSet("bricks");

        BodyDef bDef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        bDef.type = BodyDef.BodyType.StaticBody;
        bDef.position.set(Arkanoid.scale(bounds.x + bounds.width / 2), Arkanoid.scale(bounds.y + bounds.height / 2));

        body = world.createBody(bDef);

        shape.setAsBox(Arkanoid.scale(bounds.getWidth() / 2), Arkanoid.scale(bounds.getHeight() / 2));
        fdef.shape = shape;
        fixture = body.createFixture(fdef);
        fixture.setUserData(this);

        setCategoryFilter(Arkanoid.BRICK_BIT);
    }

    public void setCategoryFilter(short filterBit) {
        Filter filter = new Filter();
        filter.categoryBits = filterBit;
        fixture.setFilterData(filter);
        body.setAwake(true);
    }

    public TiledMapTileLayer.Cell getCell() {
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(0);
        return layer.getCell((int) Arkanoid.unscale(body.getPosition().x) / TILE_SIZE, (int) Arkanoid.unscale(body.getPosition().y) / TILE_SIZE);
    }

    public World getWorld() {
        return world;
    }

    public void handleHit() {
        spawnCoin();
        switch (type) {
            case POWER:
                // TODO: 6/1/16 add powerUp spawn
                destroy();
                PowerUpHelper.getInstance().requestNewPowerUp(body.getPosition());
                break;
            case RED:
                getCell().setTile(tileSet.getTile(Type.GRAY.getIdInMap()));
                type = Type.GRAY;
                break;
            case GRAY:
                getCell().setTile(tileSet.getTile(Type.YELLOW3.getIdInMap()));
                type = Type.YELLOW3;
                break;
            case YELLOW3:
                getCell().setTile(tileSet.getTile(Type.YELLOW2.getIdInMap()));
                type = Type.YELLOW2;
                break;
            case YELLOW2:
                getCell().setTile(tileSet.getTile(Type.YELLOW1.getIdInMap()));
                type = Type.YELLOW1;
                break;
            case YELLOW1:
                destroy();
                break;
            default:
                throw new RuntimeException("Unexpected block type!");
        }
    }

    private void spawnCoin() {
        if (new Random().nextInt(100) + 1 < (int)SkillsHelper.getInstance().getGoldFromBrickHitChance() * 100) {
            GameHelper.getInstance().setGold(GameHelper.getInstance().getGold() + 1);
        }
    }

    public void destroy() {
        getCell().setTile(null);
        setCategoryFilter(Arkanoid.DESTROYED_BIT);
        destroy = true;
    }

    public boolean isReadyToDestroy() {
        return destroy;
    }

    public Body getBody() {
        return body;
    }

    public enum Type {
        POWER(6),
        RED(5), GRAY(4), YELLOW3(3), YELLOW2(2), YELLOW1(1);

        int idInMap;

        Type(int idInMap) {
            this.idInMap = idInMap;
        }

        public int getIdInMap() {
            return idInMap;
        }
    }
}
