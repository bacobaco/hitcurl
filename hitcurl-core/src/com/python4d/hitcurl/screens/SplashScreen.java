package com.python4d.hitcurl.screens;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.forever;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveBy;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.python4d.hitcurl.HitcurL;
import com.python4d.hitcurl.game.Constants;
import com.python4d.hitcurl.game.CubesGroup;
import com.python4d.hitcurl.game.Logo3D;
import com.python4d.hitcurl.game.TextActor;

// SplashScreen and Options play,quit,reset
public class SplashScreen extends AbstractScreen {

	private ResetAllLevels reset = new ResetAllLevels();
	private static final String TAG = SplashScreen.class.getName();
	private static Stage stage, screen;
	private Skin skin;
	Image[] hitcurImages = new Image[7];
	private TextButton buttonClassementEasy,
			buttonClassementNormal,
			buttonClassementExpert,
			buttonPlayEasy,
			buttonPlayNormal,
			buttonPlayExpert,
			buttonQuit,
			buttonReset;
	// The global field
	ShapeRenderer debugRenderer;
	private int FirstWidth;
	private String hitcurlString = new String("hitcurl");
	private Logo3D logo3d;
	private int lastProgress;

	public SplashScreen(HitcurL game) {
		super(game);
		logo3d = new Logo3D("3d/logo.obj", Gdx.graphics.getWidth() / 2 - 100f, Gdx.graphics.getHeight() / 2 - 100f, 0.01f);
		logo3d.setMoveOnScreen(false);
	}

	private void rebuildStage() {
		skin = new Skin(Gdx.files.internal(Constants.SKIN_OBJECTS));
		// Création d'un cube unique
		CubesGroup cube = new CubesGroup("cube", new Object[][] { { new Vector2(0, 0), new String[] { "cubejaune" } } });
		Table tableLayer = new Table(skin);
		Table tableinLayer = new Table(skin);
		tableLayer.setFillParent(true);
		for (int i = 0; i < hitcurImages.length; i++)
			hitcurImages[i] = new Image(skin, hitcurlString.substring(i, i + 1));

		buttonPlayEasy = new TextButton("Easy Game", skin, "FondNoir");
		buttonPlayEasy.addAction(sequence(fadeOut(0), fadeIn(2)));
		buttonPlayEasy.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Gdx.app.log("button droit", "clicked");
				game.setScreen(new GameScreen(game, 6));
			};
		});
		buttonClassementEasy = new TextButton("HighScore", skin, "HighScore");
		buttonClassementEasy.getStyle().font.getData().setScale(0.5f);
		buttonClassementEasy.addAction(sequence(fadeOut(0), fadeIn(3)));
		buttonClassementEasy.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Gdx.app.log("button droit", "clicked Classement Easy");
				HitcurL.googleServices.showScores(Constants.EASY);
			};
		});
		buttonPlayNormal = new TextButton("Normal Game", skin, "FondNoir");
		buttonPlayNormal.addAction(sequence(fadeOut(0), fadeIn(2)));
		buttonPlayNormal.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Gdx.app.log("button droit", "clicked");
				game.setScreen(new GameScreen(game, 3));
			};
		});
		buttonClassementNormal = new TextButton("HighScore", skin, "HighScore");
		buttonClassementNormal.addAction(sequence(fadeOut(0), fadeIn(3)));
		buttonClassementNormal.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Gdx.app.log("button droit", "clicked Classement Normal");
				HitcurL.googleServices.showScores(Constants.NORMAL);
			};
		});
		buttonPlayExpert = new TextButton("Expert Game", skin, "FondNoir");
		buttonPlayExpert.addAction(sequence(fadeOut(0), fadeIn(2)));
		buttonPlayExpert.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Gdx.app.log("button droit", "clicked");
				game.setScreen(new GameScreen(game, 1));
			};
		});
		buttonClassementExpert = new TextButton("HighScore", skin, "HighScore");
		buttonClassementExpert.addAction(sequence(fadeOut(0), fadeIn(3)));
		buttonClassementExpert.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Gdx.app.log("button droit", "clicked Classement Expert");
				HitcurL.googleServices.showScores(Constants.EXPERT);
			};
		});
		buttonReset = new TextButton("Reset HitcurL", skin, "FondNoir");
		buttonReset.addAction(sequence(fadeOut(0), fadeIn(4)));
		buttonReset.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Gdx.app.log("button reset", "clicked");
				new Dialog("\n\n   Reset Game   ", skin) {
					{
						text("\n\n     Confirmez-vous ?   \n\n", skin.get("berlin", LabelStyle.class));
						button("  Oui  ", "YES").left();
						button("  Non  ", "NON").right();
					}

					@Override
					protected void result(Object object) {
						if (((String) object).equals("YES"))
							reset.start();
					}
				}.show(stage);
			};
		});

		buttonQuit = new TextButton("Quit", skin, "FondNoir");
		buttonQuit.addAction(sequence(fadeOut(0), fadeIn(6)));
		buttonQuit.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				Gdx.app.log("button droit", "clicked");
				Gdx.app.exit();
			};
		});

		if (Constants.DEBUG)
			tableLayer.debugAll();
		for (Image i : hitcurImages) {
			i.addAction(forever(sequence(fadeIn((float) Math.random() * 3), delay(2), fadeOut((float) Math.random()))));
			tableinLayer.add(i);

		}

		boolean signin = HitcurL.googleServices.isSignedIn();
		tableLayer.add(tableinLayer);
		TextActor textActor = new TextActor(new BitmapFont(), "v." + HitcurL.appVersion + " - (c) Python4D & PP");
		textActor.setColor(Color.YELLOW);
		tableLayer.row();
		tableLayer.add(textActor).right();
		tableLayer.row();
		tableLayer.add("\n");
		tableLayer.row();
		tableLayer.add(buttonPlayEasy).center();
		tableLayer.row();
		if (signin) {
			tableLayer.add(buttonClassementEasy).center();
			tableLayer.row();
		}
		tableLayer.add(buttonPlayNormal).center();
		tableLayer.row();
		if (signin) {
			tableLayer.add(buttonClassementNormal).center();
			tableLayer.row();
		}
		tableLayer.add(buttonPlayExpert).center();
		tableLayer.row();
		if (signin) {
			tableLayer.add(buttonClassementExpert).center();
			tableLayer.row();
		}
		tableLayer.add("\n");
		tableLayer.row();
		tableLayer.add(buttonReset).center();
		tableLayer.row();
		tableLayer.add(buttonQuit).center();

		stage.addActor(tableLayer);
		screen.addActor(cube);
		cube.addAction(forever(
				sequence(moveBy(0, stage.getHeight() - Constants.SIZE_CUBE, 1.0f),
						moveBy(stage.getWidth() - Constants.SIZE_CUBE, 0, 1.0f * stage.getWidth() / stage.getHeight()),
						moveBy(0, -stage.getHeight() + Constants.SIZE_CUBE, 1.0f),
						moveBy(-stage.getWidth() + Constants.SIZE_CUBE, 0, 1.0f * stage.getWidth() / stage.getHeight()))));

		stage.setDebugAll(Constants.DEBUG);

	}

	@Override
	public void render(float deltaTime) {

		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		// 3D render

		if (!reset.update()) {
			if (lastProgress != reset.progress()) {
				lastProgress = reset.progress();
				buttonReset.setText(String.format("%-2d", (int) reset.progress() * Constants.NB_NIVEAU / 100) + " Levels resetted ");
				Gdx.app.debug(TAG, "Progress ResetAll=" + reset.progress());
			}
		}
		stage.act(deltaTime);
		stage.draw();
		screen.act(deltaTime);
		screen.draw();
		logo3d.render(deltaTime);

	}

	@Override
	public void resize(int width, int height) {
		screen.getViewport().update(width, height, true);
		stage.getViewport().update(width, height, true);
		if (logo3d != null) logo3d.resize(width, height);
		Gdx.app.debug(TAG, "Screen Width & Height # " + stage.getViewport().getScreenWidth() + " & " + stage.getViewport().getScreenHeight());
		Gdx.app.debug(TAG, "World Width & Height # " + stage.getViewport().getWorldWidth() + " & " + stage.getViewport().getWorldHeight());

	}

	@Override
	public void show() {
		stage = new Stage(new FitViewport(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT));
		screen = new Stage(new FitViewport(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT));
		Gdx.input.setInputProcessor(stage);
		Gdx.input.setCatchKey(com.badlogic.gdx.Input.Keys.BACK, true);
		FirstWidth = Gdx.graphics.getWidth();
		rebuildStage();
	}

	// Hide est appelé par Game au changemement de Screen
	@Override
	public void hide() {
		stage.dispose();
		skin.dispose();
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	// Pour Android Eviter une fonction Static ou Variable prefs static
	// http://stackoverflow.com/questions/18411126/android-libgdx-prefs-getting-lost
	public class ResetAllLevels {
		private int[] game = { 1, 3, 6 };

		private int nb_niveaux = Constants.NB_NIVEAU;
		private int level = 0;
		private Preferences prefs;
		private boolean started;

		public ResetAllLevels() {
		}

		public void start() {
			started = true;

			for (Actor a : stage.getActors()) {
				a.setTouchable(Touchable.disabled);
			}
			level = 0;
			for (int j : game) {
				prefs = Gdx.app.getPreferences("SCORE NBCLUE" + j);
				prefs.clear();
				prefs.flush();
				prefs = Gdx.app.getPreferences("GRILLES" + j);
				prefs.clear();
				prefs.flush();
			}

		}

		/**
		 * @return FALSE si reset en cours
		 */
		protected boolean update() {
			if (started) {
				if (level > nb_niveaux) {
					started = !started;
					for (Actor a : stage.getActors()) {
						a.setTouchable(Touchable.enabled);
					}
					return true;
				}

				for (int j : game) {
					prefs = Gdx.app.getPreferences("LEVEL" + level + "NBCLUE" + j);
					prefs.clear();
					prefs.flush();
					prefs = Gdx.app.getPreferences("GRILLE" + level + "NBCLUE" + j);
					prefs.clear();
					prefs.flush();
				}
				level++;
				return false;
			}
			return true;
		}

		public int progress() {
			if (started)
				return (int) ((float) level / ((float) nb_niveaux + 1) * 100.0);
			else
				return 100;
		}
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

}
