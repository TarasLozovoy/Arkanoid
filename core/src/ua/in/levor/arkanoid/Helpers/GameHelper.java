package ua.in.levor.arkanoid.Helpers;

import ua.in.levor.arkanoid.DB.DBHelper;

public class GameHelper {
    private static GameHelper instance;
    private long gold;
    private int gems;
    private int lives;

    private GameHelper(){}

    public static GameHelper getInstance() {
        if (instance == null) {
            instance = new GameHelper();
        }
        return instance;
    }

    public void init() {
        gold = DBHelper.getInstance().getGoldFromDB();
        gems = DBHelper.getInstance().getGemsFromDB();
    }

    public long getGold() {
        return gold;
    }

    public void addGold(long value) {
        gold += (value * SkillsHelper.getInstance().getGoldMultiplier());
    }

    public int getGems() {
        return gems;
    }

    public void addGems(int value) {
        gems += value;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public void consumeLife() {
        lives--;
    }
}
