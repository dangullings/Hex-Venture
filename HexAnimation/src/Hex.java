import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

class Hex {
	int[] neighbor = new int[6];
	int[] line = new int[6];
	int[] regLine = new int[6];
	String[] oldState = new String[6]; 
	Color color;
	int column;
	int row;
	int R, G, B;
	int setR, setG, setB;
	int regionBonus;
	int defendRank;
	int threatRank;
	int sumRank;
	int lossPotential;
	int index;
	double ox, oy;
	double oox, ooy;
	double x, y;
	double xv, yv;
	double speed;
	double mag;
	double startX, startY;
	double dist, totalDist;
	boolean fighterCover;
	boolean isLinked;
	boolean isTallyLinked;
	boolean isHighlighted;
	boolean isVisible;
	boolean blank;
	boolean isReal;
	boolean isNew;
	boolean hasDeath;
	boolean hasFalseIncome;
	
	hexOccupation occupation;
	hexOccupation[] oldOccupation = new hexOccupation[6];
	ArrayList<HexSaveState> hexSaveState = new ArrayList<HexSaveState>();
	
	public void reset(){
		x = startX;
		y = startY;
		
		totalDist = Math.sqrt(((ox-x) * (ox-x)) + ((oy-y) * (oy-y)));
	}
	
	public void moveToTarget(){
    	dist = Math.sqrt(((ox-x) * (ox-x)) + ((oy-y) * (oy-y)));

    	if (dist < 1){
			x = ox;
			y = oy;
    		return;
    	}
    	
    	xv = ox - x;
    	yv = oy - y;
    	mag = Math.sqrt(xv * xv + yv * yv);

    	speed = (((dist+2) / totalDist) * 40) + 1;
    	xv = xv * speed / mag;
    	yv = yv * speed / mag;

    	
    	x += xv;
    	y += yv;
	}
	
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public boolean isBlank() {
		return blank;
	}

	public void setBlank(boolean blank) {
		this.blank = blank;
	}

	public double getOox() {
		return oox;
	}

	public void setOox(double oox) {
		this.oox = oox;
	}

	public double getOoy() {
		return ooy;
	}

	public void setOoy(double ooy) {
		this.ooy = ooy;
	}

	public double getStartX() {
		return startX;
	}

	public void setStartX(double startX) {
		this.startX = startX;
	}

	public double getStartY() {
		return startY;
	}

	public void setStartY(double startY) {
		this.startY = startY;
	}

	public double getOx() {
		return ox;
	}
	public void setOx(double ox) {
		this.ox = ox;
	}
	public double getOy() {
		return oy;
	}
	public void setOy(double oy) {
		this.oy = oy;
	}
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	public double getXv() {
		return xv;
	}
	public void setXv(double xv) {
		this.xv = xv;
	}
	public double getYv() {
		return yv;
	}
	public void setYv(double yv) {
		this.yv = yv;
	}
	public boolean isHasFalseIncome() {
		return hasFalseIncome;
	}
	public void setHasFalseIncome(boolean hasFalseIncome) {
		this.hasFalseIncome = hasFalseIncome;
	}
	public boolean isHasDeath() {
		return hasDeath;
	}
	public void setHasDeath(boolean hasDeath) {
		this.hasDeath = hasDeath;
	}
	public int getRegionBonus() {
		return regionBonus;
	}
	public void setRegionBonus(int regionBonus) {
		this.regionBonus = regionBonus;
	}
	public boolean isNew() {
		return isNew;
	}
	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}
	public boolean isReal() {
		return isReal;
	}
	public void setReal(boolean isReal) {
		this.isReal = isReal;
	}
	public boolean isHighlighted() {
		return isHighlighted;
	}
	public void setHighlighted(boolean isHighlighted) {
		this.isHighlighted = isHighlighted;
	}

	public void setColor(int r, int g, int b){
		color = new Color(r, g, b);
	}
	
	public void setColor(Color color){
		this.color = color;
	}
	
	public void setSetColor(int r, int g, int b){
		setR = r;
		setG = g;
		setB = b;
	}
	
	public Color getColor(){
		return color;
	}
	
	public int getR() {
		return R;
	}
	public void setR(int r) {
		R += r;
	}
	public int getG() {
		return G;
	}
	public void setG(int g) {
		G += g;
	}
	public int getB() {
		return B;
	}
	public void setB(int b) {
		B += b;
	}
	public int getSetR() {
		return setR;
	}
	public void setSetR(int setR) {
		this.setR = setR;
	}
	public int getSetG() {
		return setG;
	}
	public void setSetG(int setG) {
		this.setG = setG;
	}
	public int getSetB() {
		return setB;
	}
	public void setSetB(int setB) {
		this.setB = setB;
	}
	public int[] getNeighbor() {
		return neighbor;
	}
	public boolean isTallyLinked() {
		return isTallyLinked;
	}
	public void setTallyLinked(boolean isTallyLinked) {
		this.isTallyLinked = isTallyLinked;
	}
	public boolean isVisible() {
		return isVisible;
	}
	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}
	public void setNeighbor(int[] neighbor) {
		this.neighbor = neighbor;
	}
	public int getColumn() {
		return column;
	}
	public void setColumn(int column) {
		this.column = column;
	}
	public int getRow() {
		return row;
	}
	public void setRow(int row) {
		this.row = row;
	}
	public boolean isLinked() {
		return isLinked;
	}
	public void setLinked(boolean isLinked) {
		this.isLinked = isLinked;
	}
	public int getDefendRank() {
		return defendRank;
	}
	public void setDefendRank(int defendRank) {
		this.defendRank = defendRank;
	}
	public int getThreatRank() {
		return threatRank;
	}
	public void setThreatRank(int threatRank) {
		this.threatRank = threatRank;
	}
	public int getLossPotential() {
		return lossPotential;
	}
	public void setLossPotential(int lossPotential) {
		this.lossPotential = lossPotential;
	}
	public int getSumRank() {
		return sumRank;
	}
	public void setSumRank(int sumRank) {
		this.sumRank = sumRank;
	}
	public boolean isFighterCover() {
		return fighterCover;
	}
	public void setFighterCover(boolean fighterCover) {
		this.fighterCover = fighterCover;
	}
	
}