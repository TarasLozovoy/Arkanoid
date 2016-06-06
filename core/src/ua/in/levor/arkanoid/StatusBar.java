package ua.in.levor.arkanoid;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import ua.in.levor.arkanoid.Helpers.GameHelper;
import ua.in.levor.arkanoid.Screens.MenuScreen;

public class StatusBar implements Disposable{
    public Stage stage;
    private Viewport viewport;
    private GameHelper gameHelper;

    private long gold;
    private int gems;
    private int lives;

    private TextButton goldLabel;
    private TextButton gemsLabel;
    private TextButton livesLabel;

    public StatusBar(SpriteBatch spriteBatch) {
        gameHelper = GameHelper.getInstance();

        viewport = new StretchViewport(Arkanoid.WIDTH, Arkanoid.HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, spriteBatch);
        Gdx.input.setInputProcessor(stage);

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Fonts/DoctorJekyllNF.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameter.size = 20;
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = generator.generateFont(parameter);

        gold = gameHelper.getGold();
        gems = gameHelper.getGems();
        lives = gameHelper.getLives();

        livesLabel = new TextButton("Lives: " + String.format("%02d", lives), textButtonStyle);
        goldLabel = new TextButton("Gold: " + String.format("%02d", gold), textButtonStyle);
        gemsLabel = new TextButton("Gems: " + String.format("%02d", gems), textButtonStyle);
        gemsLabel.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("gems");
            }
        });


        parameter.size = 25;
        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = generator.generateFont(parameter);
        textButtonStyle.fontColor = Color.CHARTREUSE;
        TextButton menu = new TextButton("Menu", textButtonStyle);
        menu.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                Arkanoid instance = ((Arkanoid) Gdx.app.getApplicationListener());
                instance.setScreen(new MenuScreen(instance));
            }
        });

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        update();

        table.add(livesLabel).expandX();
        table.add(goldLabel).expandX();
        table.add(gemsLabel).expandX();
        table.add(menu).align(Align.right).padRight(2f);

        stage.addActor(table);

        generator.dispose();
    }

    public void update() {
        gold = gameHelper.getGold();
        gems = gameHelper.getGems();
        lives = gameHelper.getLives();
        livesLabel.setText("Lives " + String.format("%02d", lives));
        goldLabel.setText("Gold " + String.format("%05d", gold));
        gemsLabel.setText("Gems " + String.format("%03d", gems));
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
