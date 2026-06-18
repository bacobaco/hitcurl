package com.python4d.hitcurl.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.Disposable;

public class WorldRenderer implements Disposable {
	private static final String TAG = WorldRenderer.class.getName();

	// On récupére l'adresse de worldController pour pouvoir dessiner les objets
	// définis/dessinés dans level
	private WorldController worldController;
	
	private com.badlogic.gdx.graphics.g2d.SpriteBatch batch;
	private com.badlogic.gdx.graphics.Texture backgroundTexture;
	private int currentLoadedLevel = -1;
	private float animTime = 0;

	public WorldRenderer(WorldController worldController) {
		this.worldController = worldController;
		init();
	}

	private void init() {
		batch = new com.badlogic.gdx.graphics.g2d.SpriteBatch();
	}

	// ==========================================================RENDER
	public void render(boolean paused) {
		renderBackground();
		renderWorld();
		renderGui();

	}

	private void renderBackground() {
		int level = worldController.niveau; // Récupère le numéro du niveau actuel
		if (level != currentLoadedLevel) {
			currentLoadedLevel = level;
			animTime = 0; // Reset l'animation au changement de niveau
			if (backgroundTexture != null) {
				backgroundTexture.dispose();
				backgroundTexture = null;
			}
			
			// Tente de charger backgrounds/level_X.jpg ou .png
			String path = "backgrounds/level_" + level + ".jpg";
			if (Gdx.files.internal(path).exists()) {
				backgroundTexture = new com.badlogic.gdx.graphics.Texture(Gdx.files.internal(path));
			} else {
				String pathPng = "backgrounds/level_" + level + ".png";
				if (Gdx.files.internal(pathPng).exists()) {
					backgroundTexture = new com.badlogic.gdx.graphics.Texture(Gdx.files.internal(pathPng));
				} else if (Gdx.files.internal("badlogic.jpg").exists()) {
					// Fallback de sécurité si l'image du niveau n'est pas encore créée
					backgroundTexture = new com.badlogic.gdx.graphics.Texture(Gdx.files.internal("badlogic.jpg"));
				}
			}
			
			if (backgroundTexture != null) {
				backgroundTexture.setFilter(com.badlogic.gdx.graphics.Texture.TextureFilter.Linear, com.badlogic.gdx.graphics.Texture.TextureFilter.Linear);
			}
		}

		if (backgroundTexture != null) {
			float scale = 1.0f;
			if (worldController.level.isComplete) {
				animTime += Gdx.graphics.getDeltaTime();
				// Variation lente entre 1.0 et 1.2 (20% de zoom)
				scale = 1.1f + 0.1f * (float)Math.sin(animTime * 0.4f); 
			}

			batch.begin();
			float w = Gdx.graphics.getWidth();
			float h = Gdx.graphics.getHeight();
			// On dessine l'image centrée pour que le zoom soit harmonieux
			batch.draw(backgroundTexture, 
					0, 0,            // Position
					w/2f, h/2f,      // Origine (centre)
					w, h,            // Taille
					scale, scale,    // Echelle (Zoom)
					0,               // Rotation
					0, 0,            // SrcX, SrcY
					backgroundTexture.getWidth(), backgroundTexture.getHeight(), 
					false, false);   // Flip
			batch.end();
		}
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
		worldController.stage.getViewport().update(width, height, true);
		((OrthographicCamera) worldController.stage.getCamera()).zoom = 1 / (worldController.stage.getWidth() / (Constants.SIZE_CUBE * 12f));
		worldController.hud.getViewport().update(width, height, true);

		Gdx.app.debug(TAG,
				"RESIZE => Width & Height #" + Gdx.graphics.getWidth() + " & "
						+ Gdx.graphics.getHeight());
	}

	@Override
	public void dispose() {
		if (batch != null) batch.dispose();
		if (backgroundTexture != null) backgroundTexture.dispose();
	}
}