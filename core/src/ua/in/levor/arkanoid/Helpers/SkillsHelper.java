package ua.in.levor.arkanoid.Helpers;


public class SkillsHelper {
    private static SkillsHelper instance;

    private SkillsHelper(){
    }

    public static SkillsHelper getInstance() {
        if (instance == null) {
            instance = new SkillsHelper();
        }
        return instance;
    }

    private int goldFromBrickHitLevel;
    private static float GOLD_FROM_BRICK_HIT_MULTIPLIER = 0.05f / 100;

    private int gemFromPowerUpBrickHitLevel;
    private static float GEM_FROM_POWER_UP_HIT_MULTIPLIER = 0.05f / 100;

    private int goldMultiplierLevel;
    private static float GOLD_MULTIPLIER_MULTIPLIER = 5f / 100;

    private int destroyBrickOnHitLevel;
    private static float DESTROY_BRICK_ON_HIT_MULTIPLIER = 0.5f / 100;

    private int detonationLevel;                                                //max = 20
    private static float DETONATION_MULTIPLIER = 0.1f / 100;

    private int detonationRadiusLevel;                                          //max = 10
    private static int DETONATION_RADIUS_MULTIPLIER = 5;

    private int additionalLifeLevel;
    private static int ADDITIONAL_LIFE_MULTIPLIER = 1;

    private int powerUpDropOnBrickHitLevel;
    private static float POWER_UP_DROP_ON_BRICK_HIT_MULTIPLIER = 0.15f / 100;

    private int deactivateOopsBrickLevel;
    private static float DEACTIVATE_OOPS_BLOCK_LEVEL_MULTIPLIER = 2.5f / 100;

    //steel ball
    private int steelBallLevel;

    private int steelBallDurationLevel;
    private static int STEEL_BALL_DURATION_MULTIPLIER = 1;

    private int steelBallPunchNeighbourLevel;
    private static float STEEL_BALL_PUNCH_NEIGHBOUR_MULTIPLIER = 2.5f / 100;

    //freeze ball
    private int freezeBallLevel;

    private int freezeBallDurationLevel;
    private static int FREEZE_BALL_DURATION_MULTIPLIER = 1;

    private int freezeBallFreezeNeighbourLevel;
    private static float FREEZE_BALL_FREEZE_NEIGHBOUR_MULTIPLIER = 2.5f / 100;

    private int freezeBallFreezeChainProbabilityLevel;
    private static float FREEZE_BALL_FREEZE_CHAIN_PROBABILITY_MULTIPLIER = 2.5f / 100;

    private int freezeBallFreezeChainLengthLevel;
    private static int FREEZE_BALL_FREEZE_CHAIN_LENGTH_MULTIPLIER = 1;

    //speed up
    private int speedUpLevel;

    private int speedUpRatioLevel;
    private static float SPEED_UP_RATIO_MULTIPLIER = 0.01f;

    //slow down
    private int slowDownLevel;

    private int slowDownRatioLevel;
    private static float SLOW_DOWN_RATIO_MULTIPLIER = 0.01f;

    //magnet
    private int magnetLevel;

    private int magnetDurationLevel;
    private static int MAGNET_DURATION_MULTIPLIER = 1;

    private int magnetAttractionLevel;
    private static float MAGNET_ATTRACTION_MULTIPLIER = 30f / 100; //25 per sec per level

    //+ ball
    private int duplicateBallLevel;

    private int tripleBallProbabilityLevel;
    private static float TRIPLE_BALL_PROBABILITY_MULTIPLIER = 1.5f / 100;

    private int quadripleBallProbabilityLevel;
    private static float QUADRIPLE_BALL_PROBABILITY_MULTIPLIER = 2.5f / 100;

    //bomb
    private int bombLevel;

    private int bombDurationLevel;
    private static int BOMB_DURATION_MULTIPLIER = 1;

    private int bombRadiusLevel;                                            //max = 20
    private static int BOMB_RADIUS_MULTIPLIER = 2;

    private int bombWallDestroyLevel;

    private int bombShrapnelLevel;                                          //max = 20
    private static float BOMB_SHRAPNEL_MULTIPLIER = 1.5f / 100;

    public void init(){
        // TODO: 6/3/16 init from DB
        goldFromBrickHitLevel = 0;
        gemFromPowerUpBrickHitLevel = 0;
        goldMultiplierLevel = 0;
        destroyBrickOnHitLevel = 0;
        detonationLevel = 0;
        detonationRadiusLevel = 0;
        additionalLifeLevel = 0;
        powerUpDropOnBrickHitLevel = 0;
        deactivateOopsBrickLevel = 0;

        //steel ball
        steelBallLevel = 0;
        steelBallDurationLevel = 0;
        steelBallPunchNeighbourLevel = 0;

        //freeze ball
        freezeBallLevel = 0;
        freezeBallDurationLevel = 0;
        freezeBallFreezeNeighbourLevel = 0;
        freezeBallFreezeChainProbabilityLevel = 0;
        freezeBallFreezeChainLengthLevel = 0;

        //speed up
        speedUpLevel = 0;
        speedUpRatioLevel = 0;

        //slow down
        slowDownLevel = 0;
        slowDownRatioLevel = 0;

        //magnet
        magnetLevel = 1;
        magnetDurationLevel = 20;
        magnetAttractionLevel = 20;

        //+ ball
        duplicateBallLevel = 1;
        tripleBallProbabilityLevel = 20;
        quadripleBallProbabilityLevel = 20;

        //bomb
        bombLevel = 0;
        bombDurationLevel = 0;
        bombRadiusLevel = 0;
        bombWallDestroyLevel = 0;
        bombShrapnelLevel = 0;
    }

    public float getGoldFromBrickHitChance() {
        if (goldFromBrickHitLevel > 20) {
            return 0.0125f;
        }
        return goldFromBrickHitLevel * GOLD_FROM_BRICK_HIT_MULTIPLIER;
    }

    public float getGemFromPowerUpBrickHitChance() {
        return gemFromPowerUpBrickHitLevel * GEM_FROM_POWER_UP_HIT_MULTIPLIER;
    }

    public float getGoldMultiplier() {
        return 1 + goldMultiplierLevel * GOLD_MULTIPLIER_MULTIPLIER;
    }

    public float getDestroyBrickOnHitChance() {
        return destroyBrickOnHitLevel * DESTROY_BRICK_ON_HIT_MULTIPLIER;
    }

    public float getDetonationChance() {
        return detonationLevel * DETONATION_MULTIPLIER;
    }

    public int getDetonationRadius() {
        return 40 + detonationRadiusLevel * DETONATION_RADIUS_MULTIPLIER;
    }

    public int getAdditionalLives(){
        return additionalLifeLevel  * ADDITIONAL_LIFE_MULTIPLIER;
    }

    public float getPowerUpDropOnBrickHitChance() {
        return powerUpDropOnBrickHitLevel * POWER_UP_DROP_ON_BRICK_HIT_MULTIPLIER;
    }

    public float getDeactivateOopsBlockLevelChance() {
        return deactivateOopsBrickLevel * DEACTIVATE_OOPS_BLOCK_LEVEL_MULTIPLIER;
    }

    //steel ball
    public int getSteelBallLevel() {
        return steelBallLevel;
    }

    public int getSteelBallDuration() {
        return 10 + steelBallDurationLevel * STEEL_BALL_DURATION_MULTIPLIER;
    }

    public float getSteelBallPunchThroughChance() {
        return steelBallPunchNeighbourLevel * STEEL_BALL_PUNCH_NEIGHBOUR_MULTIPLIER;
    }

    //freeze ball
    public int getFreezeBallLevel() {
        return freezeBallLevel;
    }

    public int getFreezeBallDuration() {
        return 10 + freezeBallDurationLevel * FREEZE_BALL_DURATION_MULTIPLIER;
    }

    public float getFreezeBallFreezeNeighbourChance() {
        return freezeBallFreezeNeighbourLevel * FREEZE_BALL_FREEZE_NEIGHBOUR_MULTIPLIER;
    }

    public float getFreezeBallFreezeChainProbabilityAddValue() {
        return freezeBallFreezeChainProbabilityLevel * FREEZE_BALL_FREEZE_CHAIN_PROBABILITY_MULTIPLIER;
    }

    public int getFreezeBallFreezeChainLength() {
        return 5 + freezeBallFreezeChainLengthLevel * FREEZE_BALL_FREEZE_CHAIN_LENGTH_MULTIPLIER;
    }

    //speed up
    public int getSpeedUpLevel() {
        return speedUpLevel;
    }

    public float getSpeedUpRatio() {
        System.out.println(speedUpRatioLevel * SPEED_UP_RATIO_MULTIPLIER);
        return 1.1f + speedUpRatioLevel * SPEED_UP_RATIO_MULTIPLIER;
    }

    //slow down
    public int getSlowDownLevel() {
        return slowDownLevel;
    }

    public float getSLowDownRatio() {
        return 0.9f - slowDownRatioLevel * SLOW_DOWN_RATIO_MULTIPLIER;
    }

    //magnet
    public int getMagnetLevel() {
        return magnetLevel;
    }

    public int getMagnetDuration() {
        return 10 + magnetDurationLevel * MAGNET_DURATION_MULTIPLIER;
    }

    public float getMagnetAttractionPower() {
        return magnetAttractionLevel * MAGNET_ATTRACTION_MULTIPLIER;
    }

    //+ ball
    public int getDuplicateBallLevel() {
        return duplicateBallLevel;
    }

    public float getTripleBallChance() {
        return tripleBallProbabilityLevel * TRIPLE_BALL_PROBABILITY_MULTIPLIER;
    }

    public float getQuadripleBallChance() {
        return getTripleBallChance() * quadripleBallProbabilityLevel * QUADRIPLE_BALL_PROBABILITY_MULTIPLIER;
    }

    //bomb
    public int getBombLevel() {
        return bombLevel;
    }

    public int getBombDuration() {
        return 10 + bombDurationLevel * BOMB_DURATION_MULTIPLIER;
    }

    public float getBombRadius() {
        return 40 + bombRadiusLevel * BOMB_RADIUS_MULTIPLIER;
    }

    public boolean isBombDestroysWall() {
        return bombWallDestroyLevel == 1;
    }

    public float getBombShrapnelChance() {
        return bombShrapnelLevel * BOMB_SHRAPNEL_MULTIPLIER;
    }
}
