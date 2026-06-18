package com.python4d.hitcurl.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;
import com.python4d.hitcurl.HitcurL;
import com.python4d.hitcurl.game.Constants;

public class DesktopLauncher {
	private static boolean bCreateAtlas = true;

	public static void main(String[] arg) {

		if (bCreateAtlas == true) {
			Settings settings = new Settings();
			settings.maxWidth = 1024;
			settings.maxHeight = 1024;
			TexturePacker.processIfModified(settings, "./images",
					"../hitcurl-android/assets/images", "hitcurl.atlas");
		}
		Lwjgl3ApplicationConfiguration cfg = new Lwjgl3ApplicationConfiguration();

		cfg.setTitle("HitcurL");
		cfg.setWindowedMode((int) Constants.VIEWPORT_WIDTH, (int) Constants.VIEWPORT_HEIGHT);

		new Lwjgl3Application(new HitcurL(new DesktopGoogleServices(), "alpha"), cfg);
	}
}
