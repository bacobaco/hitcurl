package com.python4d.hitcurl.android;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.games.PlayGames;
import com.google.android.gms.games.PlayGamesSdk;
import com.python4d.hitcurl.HitcurL;
import com.python4d.hitcurl.game.Constants;
import com.python4d.hitcurl.game.IGoogleServices;

public class AndroidLauncher extends AndroidApplication implements IActivityRequestHandler, IGoogleServices {

	protected AdView adView;
	private boolean mIsSignedIn = false;
	private final static AdRequest adr = new AdRequest.Builder().build();
	private static final int REQUEST_CODE_UNUSED = 9002;
	private final int SHOW_ADS = 1;
	private final int HIDE_ADS = 0;

	protected Handler handler = new Handler(android.os.Looper.getMainLooper(), msg -> {
		switch (msg.what) {
			case SHOW_ADS:
				if (adView != null) adView.setVisibility(View.VISIBLE);
				break;
			case HIDE_ADS:
				if (adView != null) adView.setVisibility(View.GONE);
				break;
		}
		return true;
	});

	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);

		// Initialize Google Play Games SDK
		PlayGamesSdk.initialize(this);
		checkSignInStatus();

		// Initialize AdMob
		MobileAds.initialize(this, initializationStatus -> {});

		// Create the layout
		RelativeLayout layout = new RelativeLayout(this);

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

		// Create the libgdx View
		com.badlogic.gdx.backends.android.AndroidApplicationConfiguration config = new com.badlogic.gdx.backends.android.AndroidApplicationConfiguration();
		config.useImmersiveMode = true;
		View gameView = initializeForView(new HitcurL((IGoogleServices) this, getString(R.string.app_version)), config);

		// Create and setup the AdMob view
		adView = new AdView(this);
		adView.setAdUnitId("ca-app-pub-1008481061910472/6901250516");
		adView.setAdSize(AdSize.BANNER);
		adView.loadAd(adr);

		// Add the libgdx view
		layout.addView(gameView);

		// Add the AdMob view
		RelativeLayout.LayoutParams adParams =
				new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
						RelativeLayout.LayoutParams.WRAP_CONTENT);
		adParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		adParams.addRule(RelativeLayout.CENTER_IN_PARENT);

		layout.addView(adView, adParams);
		showAds(false);
		// Hook it all up
		setContentView(layout);
	}

	private void checkSignInStatus() {
		PlayGames.getGamesSignInClient(this).isAuthenticated().addOnCompleteListener(task -> {
			mIsSignedIn = task.isSuccessful() && task.getResult().isAuthenticated();
		});
	}

	@Override
	public void showAds(boolean show) {
		handler.sendEmptyMessage(show ? SHOW_ADS : HIDE_ADS);
	}

	@Override
	public void signIn() {
		try {
			runOnUiThread(() -> {
				PlayGames.getGamesSignInClient(this).signIn().addOnCompleteListener(task -> {
					mIsSignedIn = task.isSuccessful() && task.getResult().isAuthenticated();
				});
			});
		} catch (Exception e) {
			Gdx.app.log("MainActivity", "Log in failed: " + e.getMessage() + ".");
		}
	}

	@Override
	public void signOut() {
		mIsSignedIn = false;
	}

	@Override
	public void rateGame() {
		String str = "https://play.google.com/store/apps/details?id=org.fortheloss.plunderperil";
		startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(str)));
	}

	@Override
	public void submitScore(long score, int nbclue) {
		String leaderboardId = null;
		if (nbclue == Constants.EASY) {
			leaderboardId = getString(R.string.leaderboard_highscoreeasy);
		} else if (nbclue == Constants.NORMAL) {
			leaderboardId = getString(R.string.leaderboard_highscorenormal);
		} else if (nbclue == Constants.EXPERT) {
			leaderboardId = getString(R.string.leaderboard_highscoreexpert);
		}

		if (leaderboardId != null) {
			PlayGames.getLeaderboardsClient(this).submitScore(leaderboardId, score);
		}
	}

	@Override
	public void showScores(int nbclue) {
		String leaderboardId = null;
		if (nbclue == Constants.EASY) {
			leaderboardId = getString(R.string.leaderboard_highscoreeasy);
		} else if (nbclue == Constants.NORMAL) {
			leaderboardId = getString(R.string.leaderboard_highscorenormal);
		} else if (nbclue == Constants.EXPERT) {
			leaderboardId = getString(R.string.leaderboard_highscoreexpert);
		}

		if (leaderboardId != null) {
			PlayGames.getLeaderboardsClient(this)
				.getLeaderboardIntent(leaderboardId)
				.addOnSuccessListener(intent -> startActivityForResult(intent, REQUEST_CODE_UNUSED));
		}
	}

	@Override
	public boolean isSignedIn() {
		return mIsSignedIn;
	}
}
