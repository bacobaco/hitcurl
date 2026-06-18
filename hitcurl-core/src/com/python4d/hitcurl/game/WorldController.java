package com.python4d.hitcurl.game;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.forever;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.python4d.hitcurl.HitcurL;
import com.python4d.hitcurl.screens.SplashScreen;

public class WorldController extends InputAdapter implements Disposable {
	private static final String TAG = WorldController.class.getName();
	private InputMultiplexer inputMultiplexer;
	public Stage stage, hud;
	private HitcurL game;
	protected Level level;
	protected int nbClue = 3;
	// http://stackoverflow.com/questions/3793650/convert-boolean-to-int-in-java
	public int score = 1000 * ((Constants.DEBUG) ? 1 : 0);
	public long endTime;
	public float zoom = 1f;
	private int niveau = 0;
	private Skin skin;
	private TextButton boutonLevelD, boutonLevelG, solution;
	private Table levelTable;
	private Label labelLevel;
	public float screenFactor;
	private TextActor scoretext;
	private TextActor completetext;
	private TextActor copyright;
	private Table completeTable;
	private Vector2 firstDown;

	public static HashMap<Integer, String[]> levels;

	public WorldController(HitcurL game, int nbClue) {
		this.game = game;
		this.nbClue = nbClue;
		levels = RestoreLevels(nbClue);
		init();
	}

	// ==================================================INIT
	public void init() {
		stage = new Stage(new FitViewport(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT));
		hud = new Stage(new FitViewport(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT));
		screenFactor = 1.0f;

		level = new Level(stage, niveau, nbClue);
		level.RestorePosition(niveau);

		skin = Assets.getAssetManager().get(Constants.SKIN_OBJECTS, Skin.class);
		levelTable = new Table();

		levelTable.setFillParent(true);
		completeTable = new Table();
		completeTable.setFillParent(true);
		levelTable.bottom().center();
		if (Constants.DEBUG)
			levelTable.debugAll();
		skin.getFont("fingerpaint").setColor(1, 0, 0, 1);
		skin.getFont("fingerpaint").getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		labelLevel = new Label("Level " + niveau, skin);
		labelLevel.setColor(Color.YELLOW);
		labelLevel.setFontScale(screenFactor * 1.5f);
		solution = new TextButton("", skin, "solution");
		boutonLevelD = new TextButton("", skin, "BoutonJauneDroit");
		boutonLevelG = new TextButton("", skin, "BoutonJauneGauche");
		scoretext = new TextActor(skin.getFont("sketchfont"), "Score " + score);
		scoretext.setScale(screenFactor);
		copyright = new TextActor(skin.getFont("berlin"), "(c) Python4d & PP");
		copyright.setScale(0.5f * screenFactor);
		completetext = new TextActor(skin.getFont("goodgirl"), "Level Completed !");
		completetext.setScale(screenFactor);
		completetext.addAction(forever(sequence(fadeOut(1), fadeIn(1))));
		completeTable.add(completetext).center();
		completetext.setVisible(false);

		boutonLevelD.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Gdx.app.log("button droit", "clicked");
				NextLevel();
			};
		});
		boutonLevelG.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Gdx.app.log("button gauche", "clicked");
				PreviousLevel();
			};
		});
		solution.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Gdx.app.log("button solution", "clicked");
				if (!level.isComplete && score > 0) {
					level.Solution(Constants.TIME_SOLUTION);
					score -= Constants.SOLUTION_COST;
					endTime = System.currentTimeMillis() + score * 1000;
				}
			};
		});
		levelTable.setTransform(true);
		levelTable.add(boutonLevelG).size(boutonLevelG.getWidth() * screenFactor).expandX().center();
		levelTable.add(labelLevel).expandX().center();
		levelTable.add(boutonLevelD).size(boutonLevelD.getWidth() * screenFactor).expandX().center();
		levelTable.row().expandX();
		levelTable.add(scoretext).center().expandX().pad(10);
		levelTable.add(solution).size(scoretext.getHeight() * 2 * screenFactor).expandX().center();

		levelTable.add(copyright).right().bottom().pad(3);
		levelTable.bottom().pack();
		hud.addActor(levelTable);
		hud.addActor(completeTable);

		inputMultiplexer = new InputMultiplexer(stage, hud);
		inputMultiplexer.addProcessor(1, this);
		Gdx.input.setInputProcessor(inputMultiplexer);

		score = ((Constants.DEBUG) ? 1000 : RestoreScore());
		endTime = System.currentTimeMillis() + score * 1000;
	}

	// ==================================================UPDATE
	public void update(float deltaTime, boolean paused) {
		// Test les lettre dans la grille
		level.update(deltaTime);
		if (level.isComplete && !completetext.isVisible()) {
			completetext.setVisible(true);
			level.StopMove(true);
			if (level.iPaid == 0) {
				level.iPaid = Constants.SCORE_PAID * (7 - nbClue) * (niveau + 1);
				score += Constants.TIME_PAID;
				HitcurL.googleServices.submitScore(score, nbClue);
			}
			SaveGame();
		}
		if (!level.isComplete && !paused)
			score = (endTime > System.currentTimeMillis()) ? (int) ((endTime - System.currentTimeMillis()) / 1000) : 0;
		else
			endTime = System.currentTimeMillis() + score * 1000;
		scoretext.setText("Score " + score);
		labelLevel.setText("Level " + niveau);
		solution.setVisible(!(score < 10));
	}

	private void NextLevel() {
		level.SavePosition(niveau);
		if (++niveau >= Constants.NB_NIVEAU)
			niveau = 0;
		stage.clear();
		level = new Level(stage, niveau, nbClue);
		level.RestorePosition(niveau);
		completetext.setVisible(false);
	}

	private void PreviousLevel() {

		level.SavePosition(niveau);
		if (--niveau < 0)
			niveau = Constants.NB_NIVEAU - 1;
		stage.clear();
		level = new Level(stage, niveau, nbClue);
		level.RestorePosition(niveau);
		completetext.setVisible(false);
	}

	@Override
	public boolean scrolled(float amountX, float amountY) {
		Gdx.app.debug(TAG, "scrolled(float amountX, float amountY) #" + amountX + ", " + amountY);
		stage.clear();
		if (amountY > 0)
			NextLevel();
		else
			PreviousLevel();

		return false;
	}

	@Override
	public boolean keyDown(int keycode) {
		// ((AbstractScreen) game.getScreen()).myToast.makeText("Key pressed=" +
		// keycode, skin.getFont("berlin"), 2f);
		if (keycode == Keys.BACK || keycode == Keys.ESCAPE) {
			SaveGame();
			// switch to menu screen
			game.setScreen(new SplashScreen(game));
		}
		return super.keyDown(keycode);
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		Gdx.app.debug(TAG,
				"touchDown(int screenX, int screenY, int pointer, int button) #" + screenX + ", " + screenY + ", " + pointer + ", " + button);
		firstDown = new Vector2(screenX, screenY);

		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		Gdx.app.debug(TAG,
				"touchUp(int screenX, int screenY, int pointer, int button) #" + screenX + ", " + screenY + ", " + pointer + ", " + button
						+ " => Stage" + stage.screenToStageCoordinates(new Vector2(screenX, screenY)));
		if (screenX < firstDown.x && Math.abs(firstDown.x - screenX) > Gdx.graphics.getWidth() / 3)
			NextLevel();
		if (screenX > firstDown.x / 2 && Math.abs(firstDown.x - screenX) > Gdx.graphics.getWidth() / 3)
			PreviousLevel();

		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		Gdx.app.debug(TAG,
				"touchDragged(int screenX, int screenY, int pointer) #" + screenX + ", " + screenY + ", " + pointer);

		return false;
	}

	@Override
	public void dispose() {
		stage.dispose();
		hud.dispose();

	}

	/**
	 * Création de Grilles au format HashMap<Integer, String[]> Algorithme
	 * basique/bourin : on utilise le hasard pour placer les lettres... Plus les
	 * levels augmentes plus on fait de rotation sur les lettres
	 * 
	 * @param nbGrille
	 * @return
	 */
	public static HashMap<Integer, String[]> RandomGrille(int nbGrille)
	{
		final int nbVoisins = 3;
		final int nbMaxTentative = 64;
		int allTentatives = 0;

		HashMap<Integer, String[]> allGrilles = new HashMap<Integer, String[]>();

		for (int indiceNbGrille = 0; indiceNbGrille < nbGrille; indiceNbGrille++) {
			char[][] lettreH = new char[][] { { 'h', ' ', 'h' }, { 'h', 'h', 'h' }, { 'h', ' ', 'h' } };
			char[][] lettreI = new char[][] { { ' ', 'i', ' ' }, { ' ', 'i', ' ' }, { ' ', 'i', ' ' } };
			char[][] lettreT = new char[][] { { 't', 't', 't' }, { ' ', 't', ' ' }, { ' ', 't', ' ' } };
			char[][] lettreC = new char[][] { { 'c', 'c', ' ' }, { 'c', ' ', ' ' }, { 'c', 'c', ' ' } };
			char[][] lettreU = new char[][] { { 'u', ' ', 'u' }, { 'u', ' ', 'u' }, { 'u', 'u', 'u' } };
			char[][] lettreR = new char[][] { { 'r', 'r', ' ' }, { 'r', ' ', ' ' }, { 'r', ' ', ' ' } };
			char[][] lettreL = new char[][] { { 'l', ' ', ' ' }, { 'l', ' ', ' ' }, { 'l', 'l', 'l' } };
			Object[] lettres = new Object[] { lettreH, lettreI, lettreT, lettreC, lettreU, lettreR, lettreL };
			char[][] grille = new char[][] {
					{ '#', '#', '#', '#', '#', '#', '#', '#', '#', '#' },
					{ '#', '*', '*', '*', '*', '*', '*', '*', '*', '#' },
					{ '#', '*', '*', '*', '*', '*', '*', '*', '*', '#' },
					{ '#', '*', '*', '*', '*', '*', '*', '*', '*', '#' },
					{ '#', '*', '*', '*', '*', '*', '*', '*', '*', '#' },
					{ '#', '*', '*', '*', '*', '*', '*', '*', '*', '#' },
					{ '#', '*', '*', '*', '*', '*', '*', '*', '*', '#' },
					{ '#', '*', '*', '*', '*', '*', '*', '*', '*', '#' },
					{ '#', '*', '*', '*', '*', '*', '*', '*', '*', '#' },
					{ '#', '#', '#', '#', '#', '#', '#', '#', '#', '#' } };
			boolean ok = true;
			int indiceLettre = 0;
			Constants.ShuffleArray(lettres);
			Integer[] indicesLigne = { 3, 4, 2, 5, 1, 6, 0, 7 };
			Constants.ShuffleArray(indicesLigne);
			Integer[] indicesColonne = { 3, 4, 2, 5, 1, 6, 0, 7 };
			Constants.ShuffleArray(indicesColonne);
			int indiceTentative = 0;
			for (int iRot = nbGrille - indiceNbGrille; iRot < nbGrille; iRot++) {
				Constants.Rot3x3((char[][]) lettres[iRot % lettres.length]);
			}
			while (ok && indiceLettre < lettres.length && indiceTentative < nbMaxTentative)
			{
				indiceTentative++;
				int indiceLigne = indicesLigne[(indiceTentative - 1) % 8];// new
				// Random().nextInt(8);
				int indiceColonne = indicesColonne[(indiceTentative - 1) / 8];// new
				// Random().nextInt(8);
				char[][] grilleTest = new char[10][10];
				for (int i = 0; i < 10; i++) {
					for (int j = 0; j < 10; j++) {
						grilleTest[i][j] = grille[i][j];
					}
				}
				boolean lettre_ok = true;
				int zoneInfluence = 0;
				for (int i = 0; i < 3; i++) {
					for (int j = 0; j < 3; j++) {
						if (lettre_ok &&
								grilleTest[i + indiceLigne][j + indiceColonne] == '*' ||
								(grilleTest[i + indiceLigne][j + indiceColonne] != '*' && ((char[][]) lettres[indiceLettre])[i][j] == ' ')) {
							if (((char[][]) lettres[indiceLettre])[i][j] != ' ') {
								grilleTest[i + indiceLigne][j + indiceColonne] = ((char[][]) lettres[indiceLettre])[i][j];
								if (zoneInfluence <= nbVoisins) {
									char haut = '*', bas = '*', droite = '*', gauche = '*';
									if (i + indiceLigne - 1 > 0)
										haut = grilleTest[i + indiceLigne - 1][j + indiceColonne];
									if (i + indiceLigne + 1 < 8)
										bas = grilleTest[i + indiceLigne + 1][j + indiceColonne];
									if (j + indiceColonne - 1 > 0)
										gauche = grilleTest[i + indiceLigne][j + indiceColonne - 1];
									if (j + indiceColonne + 1 < 8)
										gauche = grilleTest[i + indiceLigne][j + indiceColonne + 1];
									if (haut != '*')
										zoneInfluence++;
									if (bas != '*')
										zoneInfluence++;
									if (droite != '*')
										zoneInfluence++;
									if (gauche != '*')
										zoneInfluence++;

								}
							}
						}
						else {
							lettre_ok = false;
							break;
						}
					}
				}
				if (lettre_ok && (zoneInfluence > nbVoisins || indiceLettre == 0)) {
					grille = grilleTest.clone();
					indiceLettre++;
					Gdx.app.debug(TAG, " " + indiceLettre + "=" + grille[0].toString());
				}
				assert (true);
			}
			String[] newGrille = new String[8];
			Gdx.app.debug(TAG, "Grille n°" + indiceNbGrille);
			for (int i = 0; i < 8; i++) {
				newGrille[i] = new String(grille[i + 1]).substring(1, 9);
				Gdx.app.debug(TAG, "" + newGrille[i]);
			}
			Gdx.app.debug(TAG, " Tentative =  " + indiceTentative);
			if (indiceTentative < nbMaxTentative)
			{

				allGrilles.put(indiceNbGrille, newGrille);
			}
			else
			{
				indiceNbGrille--;
			}
			allTentatives++;
		}
		Gdx.app.debug(TAG, " All Tentatives=  " + allTentatives);
		return allGrilles;

	}

	private HashMap<Integer, String[]> RestoreLevels(int nbClue) {

		HashMap<Integer, String[]> allGrilles = new HashMap<Integer, String[]>();

		Preferences prefs = Gdx.app.getPreferences("GRILLES" + nbClue);
		int nbGrilles = prefs.getInteger("nbGrilles", 0);
		if (nbGrilles == 0) {
			allGrilles = RandomGrille(Constants.NB_NIVEAU);
			prefs.putInteger("nbGrilles", Constants.NB_NIVEAU);
			for (int i = 0; i < Constants.NB_NIVEAU; i++) {
				for (int il = 0; il < 8; il++) {
					prefs.putString("Grille" + i + "-" + il, allGrilles.get(i)[il]);
				}
			}

		}
		else {

			for (int i = 0; i < nbGrilles; i++) {
				String[] str = new String[8];
				for (int il = 0; il < 8; il++) {
					str[il] = prefs.getString("Grille" + i + "-" + il);
				}
				allGrilles.put(i, str);
			}
		}
		prefs.flush();
		return allGrilles;
	}

	public void SaveGame() {
		level.SavePosition(niveau);
		SaveScore();
	}

	private void SaveScore() {
		Preferences prefs = Gdx.app.getPreferences("SCORE NBCLUE" + nbClue);

		prefs.putInteger("score", score);
		prefs.flush();

	}

	public int RestoreScore() {
		Preferences prefs = Gdx.app.getPreferences("SCORE NBCLUE" + nbClue);
		int score = prefs.getInteger("score", Constants.TIME_PAID);
		prefs.flush();
		endTime = System.currentTimeMillis() + score * 1000;
		return score;
	}
}