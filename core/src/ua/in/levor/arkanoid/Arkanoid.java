package ua.in.levor.arkanoid;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import ua.in.levor.arkanoid.Helpers.GameHelper;
import ua.in.levor.arkanoid.Helpers.SkillsHelper;
import ua.in.levor.arkanoid.Screens.MenuScreen;

public class Arkanoid extends Game {
	public static final int WIDTH = 480;
	public static final int HEIGHT = 800;
	public static final float PPM = 100;  	//pixels per meter
	public static final String TITLE = "My cool Arkanoid";

	public static final short DEFAULT_BIT = 1;
	public static final short BALL_BIT = 2;
	public static final short BRICK_BIT = 4;
	public static final short PLATFORM_BIT = 8;
	public static final short DESTROYED_BIT = 16;

	public SpriteBatch batch;

	@Override
	public void create () {
		init();
		batch = new SpriteBatch();
		this.setScreen(new MenuScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}

	@Override
	public void dispose() {
		super.dispose();
		batch.dispose();
	}

	private void init() {
		SkillsHelper.getInstance().init();
		GameHelper.getInstance().init();

		// TODO: 6/3/16 remove when working with DB
		GameHelper gameHelper = GameHelper.getInstance();
		gameHelper.setGold(300);
		gameHelper.setGems(10);
	}

	//methods for making scaling easier
	public static float scale(float valueToBeScaled) {
		return valueToBeScaled/PPM;
	}

	public static float unscale(float valueToBeScaled) {
		return valueToBeScaled * PPM;
	}

	public static Image adjustSize(Image imageToBeScaled) {
		imageToBeScaled.setSize(scale(imageToBeScaled.getWidth()), scale(imageToBeScaled.getHeight()));
		return imageToBeScaled;
	}

	public static Sprite adjustSize(Sprite spriteToBeScaled) {
		spriteToBeScaled.setSize(scale(spriteToBeScaled.getWidth()), scale(spriteToBeScaled.getHeight()));
		return spriteToBeScaled;
	}

	public static Button adjustSize(Button buttonToBeScaled) {
		buttonToBeScaled.setSize(scale(buttonToBeScaled.getWidth()), scale(buttonToBeScaled.getHeight()));;
		return buttonToBeScaled;
	}
}
