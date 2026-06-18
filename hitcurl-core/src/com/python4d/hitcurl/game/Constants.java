package com.python4d.hitcurl.game;

import java.util.HashMap;
import java.util.Random;

import com.badlogic.gdx.math.Vector2;

public class Constants {
	private static final String TAG = WorldController.class.getName();
	public static final boolean DEBUG = false;
	public static final int TEMPO_TOUCHE = 10;
	// Visible game world
	public static final float VIEWPORT_WIDTH = 600.0f;
	// Visible game world
	public static final float VIEWPORT_HEIGHT = 800.0f;

	// GUI Width
	public static final float VIEWPORT_HUD_WIDTH = VIEWPORT_WIDTH;
	// GUI Height
	public static final float VIEWPORT_HUD_HEIGHT = VIEWPORT_HEIGHT;

	// Location of description file for texture atlas
	public static final String TEXTURE_ATLAS_OBJECTS = "images/hitcurl.atlas";

	// Location of description file JSON for skin
	public static final String SKIN_OBJECTS = "images/hitcurl.json";
	public static final float SIZE_CUBE = 24.0f;

	public static enum DIFFICULTE {
		EASY(6),
		NORMAL(3),
		EXPERT(1);

		private int nbclue = 0;

		DIFFICULTE(int nbclue) {
			this.nbclue = nbclue;
		}

		public int getNbClue() {
			return nbclue;
		}
	}

	public static final int EASY = 6;
	public static final int NORMAL = 3;
	public static final int EXPERT = 1;

	public static final String LEVEL_01 = "";
	public static final int NB_NIVEAU = 23;
	protected static final float AD_BANNER_HEIGHT = 50.0f;
	protected static final int TIME_SOLUTION = 3;
	public static final int SCORE_PAID = 10;
	public static final int TIME_PAID = 100;
	protected static final int SOLUTION_COST = 22;

	// Implementing Fisher–Yates shuffle
	// http://www.tutorialspoint.com/java/java_generics.htm
	public static <T> void ShuffleArray(T[] ar)
	{
		Random rnd = new Random();
		for (int i = ar.length - 1; i > 0; i--)
		{
			int index = rnd.nextInt(i + 1);
			// Simple swap
			T a = ar[index];
			ar[index] = ar[i];
			ar[i] = a;
		}
	}

	public static boolean Rot3x3(char[][] tab)
	{
		if (tab.length != 3 || tab[0].length != 3)
			return false;
		char[][] tabCopy = new char[3][3];
		tabCopy[0][2] = tab[0][0];
		tabCopy[1][2] = tab[0][1];
		tabCopy[2][2] = tab[0][2];

		tabCopy[0][1] = tab[1][0];
		tabCopy[1][1] = tab[1][1];
		tabCopy[2][1] = tab[1][2];

		tabCopy[0][0] = tab[2][0];
		tabCopy[1][0] = tab[2][1];
		tabCopy[2][0] = tab[2][2];

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				tab[i][j] = tabCopy[i][j];
			}
		}
		return true;
	}

	public static HashMap<String, String[]> CouleurDuCube = new HashMap<String, String[]>();
	static
	{
		String[] cubejaune = { "cubejaune", "cubejaune3D" };
		CouleurDuCube.put("h", cubejaune);
		String[] cubevert = { "cubevert", "cubevert3D" };
		CouleurDuCube.put("i", cubevert);
		String[] cubebleu = { "cubebleu", "cubebleu3D" };
		CouleurDuCube.put("t", cubebleu);
		String[] cuberose = { "cuberose", "cuberose3D" };
		CouleurDuCube.put("c", cuberose);
		String[] cuberouge = { "cuberouge", "cuberouge3D" };
		CouleurDuCube.put("u", cuberouge);
		String[] cubegris = { "cubegris", "cubegris3D" };
		CouleurDuCube.put("r", cubegris);
		String[] cubeciel = { "cubeciel", "cubeciel3D" };
		CouleurDuCube.put("l", cubeciel);
		String[] cubenoir = { "cubenoir" };
		CouleurDuCube.put("*", cubenoir);
	}
	public static HashMap<String, Object[][]> lettres = new HashMap<String, Object[][]>();
	static
	{
		lettres.put("h", new Object[][] {
				{ new Vector2(0, 0), CouleurDuCube.get("h") },
				{ new Vector2(2, 0), CouleurDuCube.get("h") },
				{ new Vector2(0, 1), CouleurDuCube.get("h") },
				{ new Vector2(1, 1), CouleurDuCube.get("h") },
				{ new Vector2(2, 1), CouleurDuCube.get("h") },
				{ new Vector2(0, 2), CouleurDuCube.get("h") },
				{ new Vector2(2, 2), CouleurDuCube.get("h") } });
		lettres.put("i", new Object[][] {
				{ new Vector2(0, 0), CouleurDuCube.get("i") },
				{ new Vector2(0, 1), CouleurDuCube.get("i") },
				{ new Vector2(0, 2), CouleurDuCube.get("i") } });
		lettres.put("t", new Object[][] {
				{ new Vector2(1, 0), CouleurDuCube.get("t") },
				{ new Vector2(1, 1), CouleurDuCube.get("t") },
				{ new Vector2(0, 2), CouleurDuCube.get("t") },
				{ new Vector2(1, 2), CouleurDuCube.get("t") },
				{ new Vector2(2, 2), CouleurDuCube.get("t") } });
		lettres.put("c", new Object[][] {
				{ new Vector2(0, 0), CouleurDuCube.get("c") },
				{ new Vector2(1, 0), CouleurDuCube.get("c") },
				{ new Vector2(0, 1), CouleurDuCube.get("c") },
				{ new Vector2(0, 2), CouleurDuCube.get("c") },
				{ new Vector2(1, 2), CouleurDuCube.get("c") } });
		lettres.put("u", new Object[][] {
				{ new Vector2(0, 0), CouleurDuCube.get("u") },
				{ new Vector2(1, 0), CouleurDuCube.get("u") },
				{ new Vector2(2, 0), CouleurDuCube.get("u") },
				{ new Vector2(0, 1), CouleurDuCube.get("u") },
				{ new Vector2(2, 1), CouleurDuCube.get("u") },
				{ new Vector2(0, 2), CouleurDuCube.get("u") },
				{ new Vector2(2, 2), CouleurDuCube.get("u") }, });
		lettres.put("r", new Object[][] {
				{ new Vector2(0, 0), CouleurDuCube.get("r") },
				{ new Vector2(0, 1), CouleurDuCube.get("r") },
				{ new Vector2(0, 2), CouleurDuCube.get("r") },
				{ new Vector2(1, 2), CouleurDuCube.get("r") } });
		lettres.put("l", new Object[][] {
				{ new Vector2(0, 0), CouleurDuCube.get("l") },
				{ new Vector2(0, 1), CouleurDuCube.get("l") },
				{ new Vector2(0, 2), CouleurDuCube.get("l") },
				{ new Vector2(1, 0), CouleurDuCube.get("l") },
				{ new Vector2(2, 0), CouleurDuCube.get("l") } });
	}

	public static HashMap<Integer, String[]> levels = new HashMap<Integer, String[]>();
	static
	{
		levels.put(0, new String[] {
				"*h*h*i**",
				"*hhh*i**",
				"*h*h*i**",
				"*tttcc**",
				"*utuc*rr",
				"lutuccr*",
				"luuu**r*",
				"lll*****" });
		levels.put(1, new String[] {
				"*h*huuu*",
				"*hhhu*u*",
				"*hthuru*",
				"l*tccrrr",
				"ltttc***",
				"lllcc***",
				"*iii****",
				"********" });
		levels.put(2, new String[] {
				"**lll***",
				"*ilt*hhh",
				"*ilttth*",
				"*ictchhh",
				"**cccuuu",
				"****rr*u",
				"****ruuu",
				"****r***" });
		levels.put(3, new String[] {
				"uuu*****",
				"**ui*lll",
				"uuuirrrl",
				"**hihtrl",
				"**hhhttt",
				"*chcht**",
				"*ccc****",
				"********" });
		levels.put(4, new String[] {
				"t****lll",
				"tttruuul",
				"ti*rrrul",
				"*i**uuu*",
				"hihcc***",
				"hhhc****",
				"h*hcc***",
				"********" });
		levels.put(5, new String[] {
				"rlll****",
				"rrrl****",
				"uuulh*h*",
				"u*u*hhh*",
				"ucuch*h*",
				"tccciii*",
				"ttt*****",
				"t*******" });
		levels.put(6, new String[] {
				"********",
				"t**r****",
				"tttrrr**",
				"th*hcc**",
				"lhhhcuuu",
				"lh*hcc*u",
				"lll**uuu",
				"*****iii*" });
		levels.put(7, new String[] {
				"********",
				"*lll****",
				"*lccc***",
				"*lchch**",
				"**thhh**",
				"rrthuhu*",
				"rtttu*u*",
				"riiiuuu*" });
		levels.put(8, new String[] {
				"ccc*****",
				"chch*rr*",
				"lhhhuru*",
				"lh*huru*",
				"llltuuu*",
				"***ttti*",
				"***t**i*",
				"******i*" });
		levels.put(9, new String[] {
				"***rrr**",
				"****uru*",
				"****u*u*",
				"*hhhuuu*",
				"l*hiiit*",
				"lhhhttt*",
				"lll**ctc",
				"*****ccc" });
		levels.put(10, new String[] {
				"lll*****",
				"lhhh****",
				"l*h**rr*",
				"*hhhuru*",
				"**ccuru*",
				"tttcuuu*",
				"*tcc****",
				"*tiii***" });
		levels.put(11, new String[] {
				"*lll**t*",
				"***lttt*",
				"*iclchth",
				"*iccchhh",
				"*iuuuh*h",
				"**uru***",
				"**uru***",
				"**rr****" });
		levels.put(12, new String[] {
				"l*******",
				"l*tttcc*",
				"llltic**",
				"h*hticc*",
				"hhhuiurr",
				"h*hu*ur*",
				"***uuur*",
				"********" });
		levels.put(13, new String[] {
				"**rrrttt",
				"**icrct*",
				"*liccct*",
				"*liuuu**",
				"*lll*u**",
				"h*huuu**",
				"hhh*****",
				"h*h*****" });
		levels.put(14, new String[] {
				"********",
				"lllhhhrr",
				"lttth*ri",
				"l*thhhri",
				"*ctcuuui",
				"*cccu***",
				"****uuu*",
				"********" });
		levels.put(15, new String[] {
				"********",
				"*uuu*tcc",
				"*u*tttc*",
				"*uuu*tcc",
				"*iiirrrl",
				"***hhhrl",
				"****hlll",
				"***hhh**" });
		levels.put(16, new String[] {
				"ccc*****",
				"c*clll**",
				"tttl*uuu",
				"*thlhuiu",
				"*thhhuiu",
				"**hrh*i*",
				"***rrr**",
				"********" });
		levels.put(17, new String[] {
				"h*h*****",
				"hhh*****",
				"hchc**t*",
				"lcccttt*",
				"lrrr*it*",
				"lllruiu*",
				"****uiu*",
				"****uuu*" });
		levels.put(18, new String[] {
				"*****ttt",
				"*i*uuut*",
				"*i*u*ut*",
				"*ihuhu**",
				"*lhhhr**",
				"*lh*hrrr",
				"*lllccc*",
				"****c*c*" });
		levels.put(19, new String[] {
				"****rrr*",
				"***hhhr*",
				"*ttth***",
				"**thhh**",
				"**tuuucc",
				"*liiiuc*",
				"*l*uuucc",
				"*lll****" });
		levels.put(20, new String[] {
				"********",
				"*****t**",
				"rrrttt*i",
				"*uruhthi",
				"lu*uhhhi",
				"luuuhchc",
				"lll**ccc",
				"********" });
		levels.put(21, new String[] {
				"uuurrr**",
				"u*iiir**",
				"uuuhhh**",
				"**cchttt",
				"*lchhht*",
				"*lcc**t*",
				"*lll****",
				"********" });
		levels.put(22, new String[] {
				"h*hl****",
				"hhhl*ttt",
				"h*hlllti",
				"**uuu*ti",
				"**urucci",
				"**uru*c*",
				"**rr*cc*",
				"********" });
		levels.put(23, new String[] {
				"****hhh*",
				"lll**h**",
				"lcc*hhh*",
				"lcuuurr*",
				"*cc*ur**",
				"**uuurt*",
				"*iiittt*",
				"******t*" });
		levels.put(24, new String[] {
				"*****t**",
				"ccc**t**",
				"cic*ttt*",
				"*ih*hrr*",
				"*ihhhrl*",
				"*uhuhrl*",
				"*u*ulll*",
				"*uuu****" });
		levels.put(25, new String[] {
				"******rr",
				"**ttturu",
				"***tiuru",
				"***tiuuu",
				"**ccih*h",
				"***clhhh",
				"**cclh*h",
				"****lll*" });
		levels.put(26, new String[] {
				"*ccc****",
				"*chch***",
				"**hhhiii",
				"rrhthuuu",
				"rtttlllu",
				"r**tluuu",
				"****l***",
				"********" });
		levels.put(27, new String[] {
				"i*******",
				"i**rrr**",
				"i*hhhr**",
				"ttthlll*",
				"*thhhulu",
				"*tccculu",
				"**c*cuuu",
				"********" });
		levels.put(28, new String[] {
				"********",
				"*hhhrrr*",
				"**htttr*",
				"*hhhtccc",
				"*uuutclc",
				"*u*iiil*",
				"*uuulll*",
				"********" });
		levels.put(29, new String[] {
				"********",
				"*lll****",
				"uluh*h**",
				"uluhhhr*",
				"uuuhthr*",
				"*icctrr*",
				"*icttt**",
				"*icc****" });
		levels.put(30, new String[] {
				"********",
				"****i***",
				"**rrittt",
				"**rhiht*",
				"**rhhht*",
				"uuuhchcl",
				"u***cccl",
				"uuu**lll" });
		levels.put(31, new String[] {
				"***rr***",
				"***ruuu*",
				"***ru*u*",
				"****uhuh",
				"**lllhhh",
				"*tttlhih",
				"**tclci*",
				"**tccci*" });
		levels.put(32, new String[] {
				"****t***",
				"**ttt***",
				"*hhht***",
				"**hiii**",
				"*hhhlll*",
				"uuuccclr",
				"u*uc*clr",
				"u*u***rr" });
		levels.put(33, new String[] {
				"********",
				"*uuu****",
				"lllut***",
				"luuuttt*",
				"lhhht***",
				"rrh*cc**",
				"rhhhc***",
				"riiicc**" });
		levels.put(34, new String[] {
				"********",
				"*u*u****",
				"lu*u****",
				"luuucc**",
				"lllrc***",
				"hhhrcct*",
				"*hrrttt*",
				"hhhiiit*" });
		levels.put(35, new String[] {
				"***c*c**",
				"***cccri",
				"***uuuri",
				"**tu*rri",
				"tttuuu**",
				"*hthlll*",
				"*hhhl***",
				"*h*hl***" });
		levels.put(36, new String[] {
				"********",
				"********",
				"***ttt**",
				"*ccctuuu",
				"hchctu*u",
				"hhhr*ulu",
				"h*hrrrl*",
				"iii*lll*" });
		levels.put(37, new String[] {
				"********",
				"********",
				"*cc*ttt*",
				"*chhht**",
				"*cch*t**",
				"l*hhhuuu",
				"lrrriiiu",
				"lllr*uuu" });
		levels.put(38, new String[] {
				"**lll***",
				"*uuul***",
				"*u*ul***",
				"huhut***",
				"hhh*ttt*",
				"hihctc**",
				"*irccc**",
				"*irrr***" });
		levels.put(39, new String[] {
				"hhhttt**",
				"*hccti**",
				"hhhcti**",
				"rrccli**",
				"ruuul***",
				"rulll***",
				"*uuu****",
				"********" });
		levels.put(40, new String[] {
				"***ccct*",
				"***clct*",
				"***ilttt",
				"***illlr",
				"***ihhhr",
				"**uuuhrr",
				"**u*hhh*",
				"**uuu***" });
		levels.put(41, new String[] {
				"*uuu****",
				"*uiu****",
				"*uiurrrl",
				"**ihhhrl",
				"**cchlll",
				"**chhht*",
				"**ccttt*",
				"******t*" });
		levels.put(42, new String[] {
				"**uuu***",
				"*iu*ttt*",
				"*iuuut**",
				"*ihhht**",
				"*rrhcc**",
				"*rhhhcl*",
				"*r**ccl*",
				"****lll*" });
		levels.put(43, new String[] {
				"**llliii",
				"**lt*hhh",
				"**lttth*",
				"**ctchhh",
				"**ccc***",
				"*ruuu***",
				"*rrru***",
				"**uuu***" });
		levels.put(44, new String[] {
				"*il*****",
				"*iluuu**",
				"*illlu**",
				"*ccuuu**",
				"*chhh***",
				"*cchttt*",
				"*rhhht**",
				"*rrr*t**" });
		levels.put(45, new String[] {
				"ccc*****",
				"chch****",
				"*hhhru*u",
				"ihlhru*u",
				"itlrruuu",
				"itlll***",
				"ttt*****",
				"********" });
		levels.put(46, new String[] {
				"********",
				"lll*****",
				"luuu****",
				"lurrriii",
				"*uuurccc",
				"**hhhctc",
				"***httt*",
				"**hhh*t*" });
		levels.put(47, new String[] {
				"********",
				"**lll*t*",
				"**l*ttt*",
				"**lccct*",
				"uuuchchi",
				"urrrhhhi",
				"uuurh*hi",
				"********" });
		levels.put(48, new String[] {
				"********",
				"**lll***",
				"**luuu*t",
				"**lu*ttt",
				"rrruuu*t",
				"ihrhccc*",
				"ihhhc*c*",
				"ih*h****" });
		levels.put(49, new String[] {
				"********",
				"********",
				"****uuu*",
				"lll*urrr",
				"lcccuuur",
				"lc*cthhh",
				"**ttt*h*",
				"*iiithhh" });
		levels.put(50, new String[] {
				"********",
				"*****lll",
				"***ruuul",
				"***ruiul",
				"*trruiu*",
				"*tcchih*",
				"tttchhh*",
				"**cch*h*" });
		levels.put(51, new String[] {
				"********",
				"*rr*****",
				"uru*****",
				"uruiii**",
				"uuuhhh**",
				"ccl*httt",
				"c*lhhht*",
				"cclll*t*" });
		levels.put(52, new String[] {
				"*iiit***",
				"**ttt***",
				"*lllthhh",
				"*lccc*h*",
				"*lc*chhh",
				"****uuu*",
				"****urrr",
				"****uuur" });

	}

}
