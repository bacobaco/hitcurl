package com.python4d.hitcurl.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.Disposable;

public class WorldRenderer implements Disposable {
	private static final String TAG = WorldRenderer.class.getName();

	// On récupére l'adresse de worldController pour pouvoir dessiner les objets
	// définis/dessinés dans level
	private WorldController worldController;

	public WorldRenderer(WorldController worldController) {
		this.worldController = worldController;
		init();
	}

	private void init() {
	}

	// ==========================================================RENDER
	public void render(boolean paused) {
		renderWorld();
		renderGui();

	}

	private void renderGui() {

		worldController.hud.act(Gdx.graphics.getDeltaTime());
		worldController.hud.draw();
	}

	private void renderWorld() {
		worldController.stage.act(Gdx.graphics.getDeltaTime());
		worldController.stage.draw();
	}

	// ======================================================RESIZE
	public void resize(int width, int height) {
		// resize the stage
		worldController.stage.getViewport().update(width, height, false);
		((OrthographicCamera) worldController.stage.getCamera()).zoom = 1 / (worldController.stage.getWidth() / (Constants.SIZE_CUBE * 12f));
		worldController.hud.getViewport().update(width, height, true);

		Gdx.app.debug(TAG,
				"RESIZE => Width & Height #" + Gdx.graphics.getWidth() + " & "
						+ Gdx.graphics.getHeight());
	}

	@Override
	public void dispose() {

	}
}