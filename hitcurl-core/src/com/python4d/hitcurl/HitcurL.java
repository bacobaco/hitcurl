package com.python4d.hitcurl;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.FPSLogger;
import com.python4d.hitcurl.game.Assets;
import com.python4d.hitcurl.game.IGoogleServices;
import com.python4d.hitcurl.screens.SplashScreen;

public class HitcurL extends Game {

	// @see http://fortheloss.org/tutorial-set-up-google-services-with-libgdx/
	public static IGoogleServices googleServices;
	public static String appVersion;
	// a libgdx helper class that logs the current FPS each second
	private boolean paused;
	private FPSLogger fpsLogger;
	private static final String TAG = HitcurL.class.getName();

	public HitcurL(IGoogleServices googleServices, String appVersion)
	{
		super();
		this.googleServices = googleServices;
		this.appVersion = appVersion;
	}

	@Override
	public void create() {
		// Set Libgdx log level to DEBUG
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		Gdx.app.log(TAG, "Creating game");
		// Load assets
		Assets.instance.init(new AssetManager());
		fpsLogger = new FPSLogger();
		Gdx.input.setCatchKey(com.badlogic.gdx.Input.Keys.BACK, true);
		setScreen(new SplashScreen(this));
	}

	@Override
	public void render() {
		super.render();
		// output the current FPS
		fpsLogger.log();
	}
}
