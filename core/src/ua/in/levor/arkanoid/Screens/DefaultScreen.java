package ua.in.levor.arkanoid.Screens;

import com.badlogic.gdx.Screen;

public interface DefaultScreen extends Screen{
    enum GameState {RUNNING, PAUSED, GAME_OVER, LEVEL_CLEARED;}
    void setState(GameState state);
    void proceedToNextLevel();
}
