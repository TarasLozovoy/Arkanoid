package ua.in.levor.arkanoid;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import ua.in.levor.arkanoid.Helpers.GameHelper;

public class StatusBar implements Disposable{
    public Stage stage;
    private Viewport viewport;
    private GameHelper gameHelper;

    private long gold;
    private int gems;
    private int lives;

    private Label goldLabel;
    private Label gemsLabel;
    private Label livesLabel;

    public StatusBar(SpriteBatch spriteBatch) {
        gameHelper = GameHelper.getInstance();

        viewport = new StretchViewport(Arkanoid.WIDTH, Arkanoid.HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, spriteBatch);

        gold = gameHelper.getGold();
        gems = gameHelper.getGems();
        lives = gameHelper.getLives();
        livesLabel = new Label("Lives: " + String.format("%02d", lives), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        goldLabel = new Label("Gold: " + String.format("%02d", gold), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        gemsLabel = new Label("Gems: " + String.format("%02d", gems), new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        update();

        table.add(livesLabel).expandX().padTop(Arkanoid.scale(10));
        table.add(goldLabel).expandX().padTop(Arkanoid.scale(10));
        table.add(gemsLabel).expandX().padTop(Arkanoid.scale(10));

        stage.addActor(table);
    }

    public void update() {
        gold = gameHelper.getGold();
        gems = gameHelper.getGems();
        lives = gameHelper.getLives();
        livesLabel.setText("Lives: " + String.format("%02d", lives));
        goldLabel.setText("Gold: " + String.format("%05d", gold));
        gemsLabel.setText("Gems: " + String.format("%03d", gems));
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
