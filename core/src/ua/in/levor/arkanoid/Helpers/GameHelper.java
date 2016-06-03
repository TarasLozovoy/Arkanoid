package ua.in.levor.arkanoid.Helpers;

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
        // TODO: 6/3/16 init from DB
    }

    public long getGold() {
        return gold;
    }

    public void setGold(long gold) {
        this.gold = gold;
    }

    public int getGems() {
        return gems;
    }

    public void setGems(int gems) {
        this.gems = gems;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }
}
