package com.python4d.hitcurl.game;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.repeat;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.scaleBy;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;

public class Level {
	public static final String TAG = Level.class.getName();

	public enum LetterInGame {
		h, i, t, c, u, r, l
	}

	public Rectangle BoundsOfGame;
	public Stage stage;
	CubesGroup[] lettres = new CubesGroup[7];
	private Grille grille;
	public int level;
	private Vector2 posGrille;
	public boolean isComplete;
	private long showSolution = 0;
	public int iPaid = 0;
	private int nbClue;

	public Level(Stage stage, int level, int nbClue) {
		this.stage = stage;
		this.level = level;
		this.nbClue = nbClue;
		init(level);

	}

	public Level(Stage stage, int level) {
		this(stage, level, 0);
	}

	// Initialise à partir d'un fichier la grille de jeu
	private void init(int level) {
		// Création de la grille

		grille = new Grille(level, nbClue);

		stage.addActor(grille);

		// ScreenFactor = 1.5f * wordController.screenFactor;
		grille.setTransform(true);
		grille.setPosition(stage.getWidth() / 2 - grille.getMinWidth() / 2f,
				stage.getHeight() / 2 - grille.getMinHeight() / 2f);
		grille.pack();
		posGrille = new Vector2(grille.getX(), grille.getY());

		Gdx.app.debug(TAG, "posion grille=" + posGrille);

		// Création des lettres en utilisant des cubes de couleurs Class
		// CubesGroups
		lettres[0] = new CubesGroup("h");
		lettres[1] = new CubesGroup("i");
		lettres[2] = new CubesGroup("t");
		lettres[3] = new CubesGroup("c");
		lettres[4] = new CubesGroup("u");
		lettres[5] = new CubesGroup("r");
		lettres[6] = new CubesGroup("l");
		for (CubesGroup l : lettres) {
			l.setTransform(true);
			l.getColor().a = 0.9f;
			stage.addActor(l);
		}

		float milieuGrilleX = (grille.getX() + grille.getX() + grille.getWidth()) / 2;

		float posYGrilleHaute = grille.getY() + grille.getHeight();
		lettres[0].setInitPos(new Vector2(milieuGrilleX - Constants.SIZE_CUBE * 3 - lettres[0].getOriginX(), posYGrilleHaute));
		lettres[0].setPosition(lettres[0].getInitPos().x, lettres[0].getInitPos().y);
		lettres[1].setInitPos(new Vector2(milieuGrilleX - Constants.SIZE_CUBE * 1 - lettres[1].getOriginX(), posYGrilleHaute));
		lettres[1].setPosition(lettres[1].getInitPos().x, lettres[1].getInitPos().y);
		lettres[2].setInitPos(new Vector2(milieuGrilleX + Constants.SIZE_CUBE - lettres[2].getOriginX(), posYGrilleHaute));
		lettres[2].setPosition(lettres[2].getInitPos().x, lettres[2].getInitPos().y);
		lettres[3].setInitPos(new Vector2(milieuGrilleX + Constants.SIZE_CUBE * 3.5f - lettres[3].getOriginX(), posYGrilleHaute));
		lettres[3].setPosition(lettres[3].getInitPos().x, lettres[3].getInitPos().y);

		float posYGrilleBasse = grille.getY() - Constants.SIZE_CUBE * 3;
		lettres[4].setInitPos(new Vector2(milieuGrilleX - Constants.SIZE_CUBE * 4f, posYGrilleBasse));
		lettres[4].setPosition(lettres[4].getInitPos().x, lettres[4].getInitPos().y);
		lettres[5].setInitPos(new Vector2(milieuGrilleX, posYGrilleBasse));
		lettres[5].setPosition(lettres[5].getInitPos().x, lettres[5].getInitPos().y);
		lettres[6].setInitPos(new Vector2(milieuGrilleX + Constants.SIZE_CUBE * 3, posYGrilleBasse));
		lettres[6].setPosition(lettres[6].getInitPos().x, lettres[6].getInitPos().y);

	}

	public void update(float deltaTime) {
		if (showSolution > System.currentTimeMillis())
			assert true; // NOP
		else {
			if (showSolution < System.currentTimeMillis()) {
				for (Actor l : lettres)
					l.setVisible(true);
				grille.ShowSolution(false);
				showSolution = 0;
			}
		}
		for (CubesGroup l : lettres) {
			if (l.isClicked()) {
				l.setClicked(false);
				float nbPixelCube = Constants.SIZE_CUBE;
				float deltaMiddleX = l.getOriginX() * l.getScaleX() - l.getOriginX();
				float deltaMiddleY = l.getOriginY() * l.getScaleY() - l.getOriginY();
				if (l.getX() > posGrille.x + deltaMiddleX - nbPixelCube / 2
						&& l.getY() > posGrille.y + deltaMiddleY - nbPixelCube / 2
						&& l.getX() < (posGrille.x + deltaMiddleX + grille.getWidth() - nbPixelCube / 2)
						&& l.getY() < (posGrille.y + deltaMiddleY + grille.getHeight() - nbPixelCube / 2)) {

					int nb_blocsx = (int) ((l.getX() - posGrille.x - deltaMiddleX + nbPixelCube / 2) / nbPixelCube);
					int nb_blocsy = (int) ((l.getY() - posGrille.y - deltaMiddleY + nbPixelCube / 2) / nbPixelCube);

					l.setPosition(posGrille.x + deltaMiddleX + nb_blocsx * nbPixelCube, posGrille.y + deltaMiddleY + nb_blocsy * nbPixelCube);

					l.setInGrille(true);

					// if (!IsOnWhite(l))
					// l.addAction(sequence(alpha(0.2f, 0.2f), alpha(0.5f,
					// 0.1f), run(new Runnable() {
					// public void run() {
					// System.out.println("Action complete!");
					// }
					// })));
					// else
					// l.getColor().a = 0.9f;
					Gdx.app.debug(TAG, "delta=" + new Vector2(deltaMiddleX, deltaMiddleY)
							+ "\n  nb_bloc =\t" + new Vector2(nb_blocsx, nb_blocsy)
							+ "\n  nbPixelCube =\t" + nbPixelCube
							+ "\n  PosGrille =\t" + posGrille
							+ "\n  LetterPosXY =\t" + new Vector2(l.getX(), l.getY())
							+ "\n  LetterWidth =\t" + new Vector2(l.getWidth(), l.getHeight()));

					isComplete = IsComplete();

				} else {
					l.setInGrille(false);
					isComplete = false;
				}
			}
		}
	}

	private boolean IsOnWhite(CubesGroup l) {
		Actor act = new Actor();
		int nb = 0;
		boolean isOnWhite = true;
		for (Actor actor : l.getChildren()) {
			Vector2 coords = new Vector2(actor.localToStageCoordinates(new Vector2(0, 0)));
			coords = grille.stageToLocalCoordinates(new Vector2(coords.x, coords.y));
			act = grille.hit(coords.x, coords.y, false);
			Gdx.app.debug(TAG, "hit(IsOnWhite) " + new Vector2(coords.x, coords.y) + new Vector2(actor.getX(), actor.getY())
					+ grille.stageToLocalCoordinates(new Vector2(coords.x, coords.y))
					+ " Lettre(" + nb + ")=\t" + actor + " =>> Act  hit val= " + act + "\tLettre sur des cases Blanches? " + isOnWhite);
			if (act != null) {
				if (!actor.getName().endsWith(act.getName()) || (!act.getName().equals("*") && act.getName().equals(actor.getName()))) {
					isOnWhite = false;
				}
			}
			nb++;
		}
		return isOnWhite;
	}

	public boolean IsComplete() {
		Actor act = new Actor();

		Actor act_under = new Actor();
		int nb = 0;
		boolean complete = true;
		for (Actor actor : grille.getChildren()) {

			// On clique à l'endroit de chaque case de grille et on récupère
			// l'actor qui s'y trouve
			act = grille.getStage().hit((actor.getX() + 1) + posGrille.x, (1 + actor.getY()) + posGrille.y, true);
			// S'il y a un actor à cet endroit
			if (act.getName() != null) {
				// Si cet actor n'a pas le nom prévu par la grille
				// <!act.getName().endsWith(grille.getVal()[nb]) || (>
				// ou (si cet actor trouvé n'est pas égal à une case noire mais
				// une case non encore occupé
				if (!act.getName().equals("*") && act.getName().equals(grille.getVal()[nb]))
					complete = false;
				Gdx.app.debug(TAG, "hit(IsComplete) " + new Vector2(actor.getX(), actor.getY()) + " GrilleInStage(" + nb + ")=\t" + act
						+ " =>> Grille val= " + grille.getVal()[nb] + "\tact.getName()? " + act.getName());

				if (grille.getVal()[nb].equals("*") && !act.getName().endsWith("*")) {
					act.addAction(
							repeat(3, sequence(scaleBy(0.1f, 0.1f, 0.1f),
									scaleBy(-0.1f, -0.1f, 0.1f))));
				}
				// On va regarder si sous un cube il n'y a pas un autre cube...
				act.setTouchable(Touchable.childrenOnly);
				act_under = grille.getStage().hit((actor.getX() + 1) + posGrille.x, (1 + actor.getY()) + posGrille.y, true);
				if (act_under != null) {
					if (act_under.getName().startsWith("cube-")) {
						act.addAction(
								repeat(3, sequence(scaleBy(0.1f, 0.1f, 0.1f),
										scaleBy(-0.1f, -0.1f, 0.1f))));
					}
				}
				act.setTouchable(Touchable.enabled);

			}
			nb++;
		}
		Gdx.app.debug(TAG, "Grille Complete? " + complete);
		return complete;
	}

	public void SavePosition(int niveau) {
		Preferences prefs = Gdx.app.getPreferences("LEVEL" + niveau + "NBCLUE" + nbClue);

		for (CubesGroup l : lettres) {
			prefs.putFloat(l.getName() + "x", l.getX());
			prefs.putFloat(l.getName() + "y", l.getY());
			prefs.putFloat(l.getName() + "rot", l.getRotation());

		}
		prefs.putBoolean("isComplete", isComplete);
		prefs.putInteger("isPaid", iPaid);
		prefs.flush();
	}

	public void RestorePosition(int niveau) {
		Preferences prefs = Gdx.app.getPreferences("LEVEL" + niveau + "NBCLUE" + nbClue);

		for (CubesGroup l : lettres) {
			float pos = 0.0f;
			pos = prefs.getFloat(l.getName() + "x", 0);
			if (pos != 0.0f)
				l.setX(pos);
			pos = prefs.getFloat(l.getName() + "y", 0);
			if (pos != 0f)
				l.setY(pos);
			l.setRotation(prefs.getFloat(l.getName() + "rot", 0));
		}
		isComplete = prefs.getBoolean("isComplete", false);
		iPaid = prefs.getInteger("isPaid", 0);
	}

	public void Solution(long timeSec) {
		for (Actor l : lettres)
			l.setVisible(false);
		grille.ShowSolution(true);
		showSolution = timeSec * 1000 + System.currentTimeMillis();

	}

	public boolean isShowingSolution() {
		return showSolution > System.currentTimeMillis();
	}

	public void StopMove(boolean b) {
		for (Actor l : lettres)
			l.setTouchable(Touchable.disabled);

	}

	public void fadeOutPuzzle(float duration) {
		if (grille != null) {
			grille.addAction(com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut(duration));
		}
		for (CubesGroup l : lettres) {
			if (l != null) {
				l.addAction(com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut(duration));
			}
		}
	}

}
