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

    public void init(){
        // TODO: 6/3/16 init from DB
    }

    private int goldFromBrickHitLevel = 100;

    public float getGoldFromBrickHitChance() {
        return goldFromBrickHitLevel * 0.05f;
    }
}
