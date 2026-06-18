package com.python4d.hitcurl.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Disposable;

public class Assets implements Disposable, AssetErrorListener {
	public static final String TAG = Assets.class.getName();

	// Création de l'unique instance de la class Assets (le constructeur est
	// "private")
	public static final Assets instance = new Assets();

	private static AssetManager assetManager;

	public static AssetManager getAssetManager() {
		return assetManager;
	}

	public AssetTetris tetris;
	public AssetLetters letters;
	public AssetCubes cubes;
	public AssetFonts fonts;

	private Skin skin;

	public Skin getSkin() {
		return skin;
	}

	// singleton: prevent instantiation from other classes
	private Assets() {
	}

	public void init(AssetManager assetManager) {
		skin = new Skin(Gdx.files.internal(Constants.SKIN_OBJECTS));
		Assets.assetManager = assetManager;
		// set asset manager error handler
		assetManager.setErrorListener(this);
		// load skin donc texture/png
		assetManager.load(Constants.SKIN_OBJECTS, Skin.class);
		// Load fonts

		// start loading assets and wait until finished
		assetManager.finishLoading();
		Gdx.app.debug(TAG, "# of assets loaded: " + assetManager.getAssetNames().size);
		for (String a : assetManager.getAssetNames())
			Gdx.app.debug(TAG, "asset: " + a);

		TextureAtlas atlas = assetManager.get(Constants.TEXTURE_ATLAS_OBJECTS);

		// enable texture filtering for pixel smoothing
		for (Texture t : atlas.getTextures())
			t.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		// create game resource objects
		tetris = new AssetTetris(atlas);
		letters = new AssetLetters(atlas);
		cubes = new AssetCubes(atlas);
		fonts = new AssetFonts();
	}

	@Override
	public void error(@SuppressWarnings("rawtypes") AssetDescriptor asset, Throwable throwable) {
		Gdx.app.error(TAG, "Couldn't load asset '" + asset + "'", (Exception) throwable);
	}

	public class AssetTetris {
		public final AtlasRegion tetrisT;

		public AssetTetris(TextureAtlas atlas) {
			tetrisT = atlas.findRegion("tetris");
		}
	}

	public class AssetCubes {
		public final AtlasRegion blancAtlas, noirAtlas, jauneAtlas, vertAtlas, bleuAtlas, roseAtlas, rougeAtlas, grisAtlas, cielAtlas;

		public AssetCubes(TextureAtlas atlas) {
			blancAtlas = atlas.findRegion("cubeblanc");
			noirAtlas = atlas.findRegion("cubenoir");
			jauneAtlas = atlas.findRegion("cubejaune");
			vertAtlas = atlas.findRegion("cubevert");
			bleuAtlas = atlas.findRegion("cubebleu");
			roseAtlas = atlas.findRegion("cuberose");
			rougeAtlas = atlas.findRegion("cuberouge");
			grisAtlas = atlas.findRegion("cubegris");
			cielAtlas = atlas.findRegion("cubeciel");
		}
	}

	public class AssetLetters {
		public final AtlasRegion hAtlas, iAtlas, tAtlas, cAtlas, uAtlas,
				rAtlas, lAtlas;

		public AssetLetters(TextureAtlas atlas) {
			hAtlas = atlas.findRegion("h");
			iAtlas = atlas.findRegion("i");
			tAtlas = atlas.findRegion("t");
			cAtlas = atlas.findRegion("c");
			uAtlas = atlas.findRegion("u");
			rAtlas = atlas.findRegion("r");
			lAtlas = atlas.findRegion("l");
		}
	}

	public class AssetFonts {
		public final defaultFont defaultFont;
		public final BitmapFont goodgirlSmall;
		public final BitmapFont goodgirlNormal;
		public final BitmapFont goodgirlBig;
		public final BitmapFont sketchfont;

		public class defaultFont {
			protected BitmapFont font;

			public defaultFont() {
				font = new BitmapFont();
			}

			public Vector2 StrSize(String str) {
				GlyphLayout layout = new GlyphLayout(font, str);
				return new Vector2(layout.width, layout.height);
			}
		}

		public AssetFonts() {
			defaultFont = new defaultFont();

			goodgirlSmall = new BitmapFont(
					Gdx.files.internal("images/goodgirl.fnt"), false);
			goodgirlNormal = new BitmapFont(
					Gdx.files.internal("images/goodgirl.fnt"), false);
			goodgirlBig = new BitmapFont(
					Gdx.files.internal("images/goodgirl.fnt"), false);
			sketchfont = new BitmapFont(
					Gdx.files.internal("images/sketchblock.fnt"), false);
			// set font sizes
			goodgirlSmall.getData().setScale(0.75f);
			goodgirlNormal.getData().setScale(1.0f);
			goodgirlBig.getData().setScale(2.0f);
			// enable linear texture filtering for smooth fonts
			goodgirlSmall.getRegion().getTexture()
					.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			goodgirlNormal.getRegion().getTexture()
					.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			goodgirlBig.getRegion().getTexture()
					.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			sketchfont.getRegion().getTexture()
					.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		}
	}

	@Override
	public void dispose() {
		assetManager.dispose();
		fonts.goodgirlSmall.dispose();
		fonts.goodgirlNormal.dispose();
		fonts.goodgirlBig.dispose();
	}
}