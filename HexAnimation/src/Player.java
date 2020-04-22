

import java.awt.Color;
import java.util.ArrayList;

public class Player {
	private String Name;
	private int HexesLinked;
	private int numBases;
	private int r, g, b;
	private int dr, dg, db;
	private int basesSacked;
	private int fightersKilled;
	private int fightersStarved;
	private int basesLinked;
	private int startAmt;
	boolean AI;
	
	//Base[] base = new Base[hexVenture.MAX_PLAYER_BASES];
	ArrayList<Base> base = new ArrayList<Base>();
	
	ArrayList<Move> moveList = new ArrayList<Move>();
	ArrayList<Integer> intBaseQueue = new ArrayList<Integer>();
	
	ArrayList<Double> hexHistory = new ArrayList<Double>();
	ArrayList<Double> menHistory = new ArrayList<Double>();
	ArrayList<Double> wageHistory = new ArrayList<Double>();
	
	Color colorShade =  new Color(0,0,100);
	Color colorActual =  new Color(0,0,100);
	
	public Player(){
		
	}

	public int getStartAmt() {
		return startAmt;
	}

	public void setStartAmt(int startAmt) {
		this.startAmt = startAmt;
	}

	public int getDr() {
		return dr;
	}

	public void setDr(int dr) {
		this.dr = dr;
	}

	public int getDg() {
		return dg;
	}

	public void setDg(int dg) {
		this.dg = dg;
	}

	public int getDb() {
		return db;
	}

	public void setDb(int db) {
		this.db = db;
	}

	public int getFightersStarved() {
		return fightersStarved;
	}

	public void setFightersStarved(int fightersStarved) {
		this.fightersStarved = fightersStarved;
	}

	public int getBasesSacked() {
		return basesSacked;
	}

	public void setBasesSacked(int basesSacked) {
		this.basesSacked = basesSacked;
	}

	public int getFightersKilled() {
		return fightersKilled;
	}

	public void setFightersKilled(int fightersKilled) {
		this.fightersKilled = fightersKilled;
	}

	public int getBasesLinked() {
		return basesLinked;
	}

	public void setBasesLinked(int basesLinked) {
		this.basesLinked = basesLinked;
	}

	public void setActualColor(int r, int g, int b){
		colorActual = new Color(r, g, b);
	}
	
	public void setShadeColor(int r, int g, int b){
		colorShade = new Color(r, g, b);
	}
	
	public int getR() {
		return r;
	}

	public void setR(int r) {
		this.r = r;
	}

	public int getG() {
		return g;
	}

	public void setG(int g) {
		this.g = g;
	}

	public int getB() {
		return b;
	}

	public void setB(int b) {
		this.b = b;
	}

	public int getNumBases() {
		return numBases;
	}

	public void setNumBases(int numbases) {
		numBases+=numbases;
	}
	
	public boolean isAI() {
		return AI;
	}

	public void setAI(boolean aI) {
		AI = aI;
	}
	
	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public Color getColorShade() {
		return colorShade;
	}

	public void setColorShade(Color colorShade) {
		this.colorShade = colorShade;
	}

	public Color getColorActual() {
		return colorActual;
	}
	
	public int getHexesLinked(){return HexesLinked;}
	
	public void setHexesLinked(int x){HexesLinked+=x;}
}