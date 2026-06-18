package com.python4d.hitcurl.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.python4d.hitcurl.HitcurL;
import com.python4d.hitcurl.game.Constants;
import com.python4d.hitcurl.game.Logo3D;
import com.python4d.hitcurl.game.WorldController;
import com.python4d.hitcurl.game.WorldRenderer;

public class GameScreen extends AbstractScreen {
	private static final String TAG = GameScreen.class.getName();
	private WorldController worldController;
	private WorldRenderer worldRenderer;
	public boolean paused = false;
	private Logo3D logo3d;
	protected int nbClue;

	public GameScreen(HitcurL game, int nbClue) {
		super(game);
		logo3d = new Logo3D("3d/logo.obj", Gdx.graphics.getWidth() / 2 - 75f, -Gdx.graphics.getHeight() / 2 + 25f, 0.01f);
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
		screenFactor = Gdx.graphics.getWidth() / Constants.VIEWPORT_WIDTH;
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
		// 3D render
		logo3d.render(deltaTime);
		// Render game world to screen
		worldRenderer.render(paused);
	}

	@Override
	public void resize(int width, int height) {
		worldRenderer.resize(width, height);
		if (logo3d != null) {
			logo3d.resize(width, height);
		}
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
