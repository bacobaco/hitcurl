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
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
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

	private static final String TAG = SplashScreen.class.getName();
	private static Stage stage, screen;
	private Skin skin;
	Image[] hitcurImages = new Image[7];
	private TextButton buttonPlayEasy,
			buttonPlayNormal,
			buttonPlayExpert,
			buttonQuit;
	private Label labelEasyScore,
			labelNormalScore,
			labelExpertScore;
	// The global field
	ShapeRenderer debugRenderer;
	private int FirstWidth;
	private String hitcurlString = new String("hitcurl");
	private Logo3D logo3d;
	private int lastProgress;

	public SplashScreen(HitcurL game) {
		super(game);
		float posX = Gdx.graphics.getWidth() * 0.30f;
		float posY = Gdx.graphics.getHeight() * 0.40f;
		logo3d = new Logo3D("3d/logo.obj", posX, posY, 0.01f);
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

		// Lecture des records locaux
		Preferences localPrefs = Gdx.app.getPreferences("local_highscores");
		int easyHighScore = localPrefs.getInteger("highscore_" + Constants.EASY, 0);
		int normalHighScore = localPrefs.getInteger("highscore_" + Constants.NORMAL, 0);
		int expertHighScore = localPrefs.getInteger("highscore_" + Constants.EXPERT, 0);

		buttonPlayEasy = new TextButton("Easy Game", skin, "FondNoir");
		buttonPlayEasy.addAction(sequence(fadeOut(0), fadeIn(2)));
		buttonPlayEasy.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Gdx.app.log("button droit", "clicked");
				game.setScreen(new GameScreen(game, 6));
			};
		});
		labelEasyScore = new Label("High-Score = " + easyHighScore, skin);
		labelEasyScore.setFontScale(0.5f);
		labelEasyScore.setColor(Color.YELLOW);
		labelEasyScore.addAction(sequence(fadeOut(0), fadeIn(2)));
		TextButton buttonResetEasy = new TextButton("Reset", skin, "HighScore");
		buttonResetEasy.getLabel().setFontScale(0.35f);
		buttonResetEasy.addAction(sequence(fadeOut(0), fadeIn(2)));
		buttonResetEasy.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				showResetConfirmDialog(Constants.EASY, "Easy");
			}
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
		labelNormalScore = new Label("High-Score = " + normalHighScore, skin);
		labelNormalScore.setFontScale(0.5f);
		labelNormalScore.setColor(Color.YELLOW);
		labelNormalScore.addAction(sequence(fadeOut(0), fadeIn(2)));
		TextButton buttonResetNormal = new TextButton("Reset", skin, "HighScore");
		buttonResetNormal.getLabel().setFontScale(0.35f);
		buttonResetNormal.addAction(sequence(fadeOut(0), fadeIn(2)));
		buttonResetNormal.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				showResetConfirmDialog(Constants.NORMAL, "Normal");
			}
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
		labelExpertScore = new Label("High-Score = " + expertHighScore, skin);
		labelExpertScore.setFontScale(0.5f);
		labelExpertScore.setColor(Color.YELLOW);
		labelExpertScore.addAction(sequence(fadeOut(0), fadeIn(2)));
		TextButton buttonResetExpert = new TextButton("Reset", skin, "HighScore");
		buttonResetExpert.getLabel().setFontScale(0.35f);
		buttonResetExpert.addAction(sequence(fadeOut(0), fadeIn(2)));
		buttonResetExpert.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				showResetConfirmDialog(Constants.EXPERT, "Expert");
			}
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

		tableLayer.add(tableinLayer);
		TextActor textActor = new TextActor(new BitmapFont(), HitcurL.appVersion + " - (c) Bacoland");
		textActor.setColor(Color.YELLOW);
		tableLayer.row();
		tableLayer.add(textActor).right();
		tableLayer.row();
		tableLayer.add("\n");
		tableLayer.row();
		
		tableLayer.add(buttonPlayEasy).center();
		tableLayer.row();
		tableLayer.add(labelEasyScore).center();
		tableLayer.row();
		tableLayer.add(buttonResetEasy).center().size(80, 24).padBottom(15);
		tableLayer.row();
		
		tableLayer.add(buttonPlayNormal).center();
		tableLayer.row();
		tableLayer.add(labelNormalScore).center();
		tableLayer.row();
		tableLayer.add(buttonResetNormal).center().size(80, 24).padBottom(15);
		tableLayer.row();
		
		tableLayer.add(buttonPlayExpert).center();
		tableLayer.row();
		tableLayer.add(labelExpertScore).center();
		tableLayer.row();
		tableLayer.add(buttonResetExpert).center().size(80, 24).padBottom(15);
		tableLayer.row();
		
		tableLayer.add("\n");
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
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		// 3D render

		stage.act(deltaTime);
		stage.draw();
		screen.act(deltaTime);
		screen.draw();
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_DEPTH_BUFFER_BIT);
		logo3d.render(deltaTime);

	}

	@Override
	public void resize(int width, int height) {
		screen.getViewport().update(width, height, true);
		stage.getViewport().update(width, (int) ((float) width / (float) FirstWidth * (float) height));
		((OrthographicCamera) stage.getCamera()).zoom = 1 / (stage.getWidth() / (25 * Constants.SIZE_CUBE));
		Gdx.app.debug(TAG, "Screen Width & Height # " + stage.getViewport().getScreenWidth() + " & " + stage.getViewport().getScreenHeight());
		Gdx.app.debug(TAG, "World Width & Height # " + stage.getViewport().getWorldWidth() + " & " + stage.getViewport().getWorldHeight());

	}

	@Override
	public void show() {
		stage = new Stage();
		screen = new Stage();
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

	private void resetGameMode(int nbClue) {
		Preferences prefs = Gdx.app.getPreferences("SCORE NBCLUE" + nbClue);
		prefs.clear();
		prefs.flush();
		
		prefs = Gdx.app.getPreferences("GRILLES" + nbClue);
		prefs.clear();
		prefs.flush();
		
		for (int level = 0; level <= Constants.NB_NIVEAU; level++) {
			prefs = Gdx.app.getPreferences("LEVEL" + level + "NBCLUE" + nbClue);
			prefs.clear();
			prefs.flush();
			prefs = Gdx.app.getPreferences("GRILLE" + level + "NBCLUE" + nbClue);
			prefs.clear();
			prefs.flush();
		}

		Preferences localPrefs = Gdx.app.getPreferences("local_highscores");
		localPrefs.remove("highscore_" + nbClue);
		localPrefs.flush();

		if (nbClue == Constants.EASY) {
			labelEasyScore.setText("High-Score = 0");
		} else if (nbClue == Constants.NORMAL) {
			labelNormalScore.setText("High-Score = 0");
		} else if (nbClue == Constants.EXPERT) {
			labelExpertScore.setText("High-Score = 0");
		}
	}

	private void showResetConfirmDialog(final int nbClue, String modeName) {
		Dialog dialog = new Dialog("", skin) {
			@Override
			protected void result(Object object) {
				if ((Boolean) object) {
					resetGameMode(nbClue);
				}
			}
		};

		// Fond sombre semi-transparent très propre
		dialog.setBackground(skin.newDrawable("black", new Color(0.05f, 0.05f, 0.07f, 0.9f)));
		
		Table contentTable = dialog.getContentTable();
		contentTable.pad(35);

		// Titre d'avertissement pro en rouge
		Label titleLabel = new Label("CONFIRMATION", skin, "berlin");
		titleLabel.setColor(Color.RED);
		titleLabel.setFontScale(0.75f);
		contentTable.add(titleLabel).padBottom(15);
		contentTable.row();

		// Message d'explication
		Label label = new Label("Voulez-vous vraiment recommencer\nle mode " + modeName + " ?\n(Progression et High-Score remis à zéro)", skin, "berlin");
		label.setAlignment(com.badlogic.gdx.utils.Align.center);
		label.setFontScale(0.55f);
		label.setColor(Color.WHITE);
		contentTable.add(label).padBottom(25);

		// Styles personnalisés et propres pour des boutons plats de couleur unie (sans déformation)
		TextButton.TextButtonStyle yesStyle = new TextButton.TextButtonStyle();
		yesStyle.up = skin.newDrawable("black", new Color(0.18f, 0.65f, 0.28f, 1.0f)); // Vert plat
		yesStyle.down = skin.newDrawable("black", new Color(0.12f, 0.5f, 0.2f, 1.0f));
		yesStyle.font = skin.getFont("berlin");
		yesStyle.fontColor = Color.WHITE;

		TextButton.TextButtonStyle noStyle = new TextButton.TextButtonStyle();
		noStyle.up = skin.newDrawable("black", new Color(0.28f, 0.3f, 0.35f, 1.0f)); // Gris ardoise plat
		noStyle.down = skin.newDrawable("black", new Color(0.2f, 0.22f, 0.25f, 1.0f));
		noStyle.font = skin.getFont("berlin");
		noStyle.fontColor = Color.WHITE;

		// Création des boutons plats
		TextButton btnYes = new TextButton("OUI", yesStyle);
		TextButton btnNo = new TextButton("NON", noStyle);
		
		// Alignement et taille du texte
		btnYes.getLabel().setFontScale(0.6f);
		btnNo.getLabel().setFontScale(0.6f);

		dialog.button(btnYes, true);
		dialog.button(btnNo, false);

		Table buttonTable = dialog.getButtonTable();
		buttonTable.padBottom(20);
		// Format rectangulaire propre 120x40
		buttonTable.getCell(btnYes).size(120, 40).padRight(15);
		buttonTable.getCell(btnNo).size(120, 40).padLeft(15);

		dialog.show(stage);
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

}
