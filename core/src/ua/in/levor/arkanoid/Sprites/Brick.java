package ua.in.levor.arkanoid.Sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
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
import com.badlogic.gdx.utils.Array;

import java.util.Random;

import ua.in.levor.arkanoid.Arkanoid;
import ua.in.levor.arkanoid.Helpers.BrickHelper;
import ua.in.levor.arkanoid.Helpers.GameHelper;
import ua.in.levor.arkanoid.Helpers.PowerUpHelper;
import ua.in.levor.arkanoid.Helpers.SkillsHelper;

public class Brick {
    public static final int TILE_SIZE = 24;
    public static final int TNT_BURST_RADIUS = 60;
    private TiledMapTileSet tileSet;

    protected World world;
    protected TiledMap map;
    protected Rectangle bounds;
    protected Body body;
    protected Fixture fixture;

    private Type type;
    private boolean destroy = false;

    private int freezeChainID = -2;

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
        fdef.friction = 0;
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

    public void frozeBrick(int freezeDeep, int freezeChainID) {
        if (isReadyToDestroy()) return;
        if (type == Type.ICE && freezeChainID != this.freezeChainID) {
            destroy();
        } else if (type == Type.DURABILITY1 && freezeChainID == -1){
            destroy();
        } else {
            getCell().setTile(tileSet.getTile(Type.ICE.getIdInMap()));
            type = Type.ICE;
        }

        if (freezeChainID < 0) {
            this.freezeChainID = new Random().nextInt(1000000);
        }

        if (freezeDeep < 1) return;
        Random random = new Random();
        float freezeProbability = SkillsHelper.getInstance().getFreezeBallFreezeNeighbourChance();
        if (freezeDeep <= SkillsHelper.getInstance().getFreezeBallFreezeChainLength() -1) {
            freezeProbability += SkillsHelper.getInstance().getFreezeBallFreezeChainProbabilityAddValue();
        }
        if (random.nextFloat() < freezeProbability) {
            Array<Brick> neighbourBricks = new Array<Brick>();
            for (Brick b : BrickHelper.getInstance().getAllBricksInRadius(40, body.getPosition())) {
                neighbourBricks.add(b);
            }
            Brick brickToFroze = neighbourBricks.get(random.nextInt(neighbourBricks.size));
            if (brickToFroze.getType() != Type.POWER) {
                if (brickToFroze.getType() != Type.ICE) {
                    brickToFroze.freezeChainID = this.freezeChainID;
                }
                brickToFroze.frozeBrick(freezeDeep - 1, this.freezeChainID);
            }
        }
    }

    public void handleHit() {
        if (destroy) return;
        checkSkills();
        switch (type) {
            case POWER:
                destroy();
                spawnNewPowerUp();
                checkGemSpawn();
                break;
            case RED:
                getCell().setTile(tileSet.getTile(Type.GRAY.getIdInMap()));
                type = Type.GRAY;
                break;
            case GRAY:
                getCell().setTile(tileSet.getTile(Type.BROWN3.getIdInMap()));
                type = Type.BROWN3;
                break;
            case BROWN3:
                getCell().setTile(tileSet.getTile(Type.BROWN2.getIdInMap()));
                type = Type.BROWN2;
                break;
            case BROWN2:
                getCell().setTile(tileSet.getTile(Type.DURABILITY1.getIdInMap()));
                type = Type.DURABILITY1;
                break;
            case DURABILITY1:
            case SLOW_DOWN:
            case SPEED_UP:
            case ICE:
            case HALF_WALL:
            case OOPS:
                destroy();
                break;
            case TNT:
                destroy();
                for (Brick b : BrickHelper.getInstance().getAllBricksInRadius(TNT_BURST_RADIUS, body.getPosition())) {
                    b.handleHit();
                }
                break;
            case WALL:
                //do nothing
                break;
            default:
                throw new RuntimeException("Unexpected block type!");
        }

        if (!destroy) {
            checkForDestroyOnHitSkill();
        }
    }

    private void checkForDestroyOnHitSkill() {
        Random random = new Random();
        if (random.nextFloat() < SkillsHelper.getInstance().getDestroyBrickOnHitChance()) {
            destroy();
        }
    }

    private void checkSkills() {
        Random random = new Random();
        if (random.nextFloat() < SkillsHelper.getInstance().getGoldFromBrickHitChance()) {
            GameHelper.getInstance().addGold(1);
        }
        if (random.nextFloat() < SkillsHelper.getInstance().getDetonationChance()) {
            for (Brick b : BrickHelper.getInstance().getAllBricksInRadius(SkillsHelper.getInstance().getDetonationRadius(), body.getPosition())) {
                b.handleHit();
            }
        }
        if (random.nextFloat() < SkillsHelper.getInstance().getPowerUpDropOnBrickHitChance()) {
            spawnNewPowerUp();
        }
    }

    private void checkGemSpawn() {
        Random random = new Random();
        if (random.nextFloat() < SkillsHelper.getInstance().getGemFromPowerUpBrickHitChance()) {
            GameHelper.getInstance().addGems(1);
        }
    }

    private void spawnNewPowerUp() {
        PowerUpHelper.getInstance().requestNewPowerUp(body.getPosition());
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

    public Type getType() {
        return type;
    }

    public enum Type {
        POWER(6),
        WALL(7), HALF_WALL(13),
        SPEED_UP(8), SLOW_DOWN(9),
        RED(5), GRAY(4), BROWN3(3), BROWN2(2), DURABILITY1(1), ICE(14),
        TNT(15),
        OOPS(16);

        int idInMap;

        Type(int idInMap) {
            this.idInMap = idInMap;
        }

        public int getIdInMap() {
            return idInMap;
        }
    }
}
