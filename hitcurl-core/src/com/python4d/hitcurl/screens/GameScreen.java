package com.python4d.hitcurl.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.python4d.hitcurl.HitcurL;
import com.python4d.hitcurl.game.Constants;
import com.python4d.hitcurl.game.WorldController;
import com.python4d.hitcurl.game.WorldRenderer;

public class GameScreen extends AbstractScreen {
	private static final String TAG = GameScreen.class.getName();
	private WorldController worldController;
	private WorldRenderer worldRenderer;
	public boolean paused = false;
	protected int nbClue;

	public GameScreen(HitcurL game, int nbClue) {
		super(game);
		this.nbClue = nbClue;
	}

	@Override
	public void show() {
		// <?
		// Visiblement il faut remettre directement par une commande bas niveau
		// le Viewport au dimension actuelle
		// peut-être une explication ici
		// https://github.com/libgdx/libgdx/issues/1661
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		// ?>
		float ratioX = Gdx.graphics.getWidth() / Constants.VIEWPORT_WIDTH;
		float ratioY = Gdx.graphics.getHeight() / Constants.VIEWPORT_HEIGHT;
		screenFactor = Math.min(ratioX, ratioY);
		
		worldController = new WorldController(game, nbClue);
		worldRenderer = new WorldRenderer(worldController);
		Gdx.input.setCatchKey(com.badlogic.gdx.Input.Keys.BACK, true);

	}

	@Override
	public void render(float deltaTime) {
		worldController.update(deltaTime, paused);
		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		// Clears the screen
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		// Render game world to screen
		worldRenderer.render(paused);
	}

	@Override
	public void resize(int width, int height) {
		worldRenderer.resize(width, height);
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
		paused = true;
		worldController.SaveGame();
		Gdx.input.setCatchKey(com.badlogic.gdx.Input.Keys.BACK, false);
	}

	@Override
	public void resume() {
		super.resume();
		worldController.RestoreScore();
		// Only called on Android!
		paused = false;
	}

}
