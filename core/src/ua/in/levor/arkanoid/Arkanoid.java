package ua.in.levor.arkanoid;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Arkanoid extends Game {
	public static final int WIDTH = 480;
	public static final int HEIGHT = 800;
	public static final int GRAVITY = 150;
	public static final String TITLE = "My cool Arkanoid";

	SpriteBatch batch;

	@Override
	public void create () {
		batch = new SpriteBatch();
		this.setScreen(new GameScreen(this));
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
}
