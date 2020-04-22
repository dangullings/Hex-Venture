

public class Fighter {
	private int Rank;
	private int Cost;
	private int X;
	private int Y;
	private int hexNum;
	private boolean isMoved = true;
	private boolean isAlive;
	private int baseNum;
	private boolean Defend;
	private String move;
	private double explodeRadius = .00001;
	private float explodeTran = 1.0f;
	private float explodePenR = .0185f;
	private boolean explodeGraphic;
	private boolean selected;
	private int index;
	
	public Fighter() {
		
	}
	
	public void drawHighlightGraphic(int p, double x, double y, double perT, double perP){
		//StdDraw.drawBloom(x, y, explodeRadius*.75, .5f, player);
		StdDraw.drawSelect(p, x, y, explodeRadius, explodeTran, explodePenR, "fighter", Rank);
		explodeRadius += .1;
        explodeTran -= perT;
        explodePenR -= perP;
        if (explodeTran <= 0.0)
        	explodeGraphic = false;
	}
	
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public double getExplodeRadius() {
		return explodeRadius;
	}

	public void setExplodeRadius(double explodeRadius) {
		this.explodeRadius = explodeRadius;
	}

	public float getExplodeTran() {
		return explodeTran;
	}

	public void setExplodeTran(float explodeTran) {
		this.explodeTran = explodeTran;
	}

	public float getExplodePenR() {
		return explodePenR;
	}

	public void setExplodePenR(float explodePenR) {
		this.explodePenR = explodePenR;
	}

	public boolean isExplodeGraphic() {
		return explodeGraphic;
	}

	public void setExplodeGraphic(boolean explodeGraphic) {
		this.explodeGraphic = explodeGraphic;
	}

	public boolean isDefend() {
		return Defend;
	}

	public void setDefend(boolean defend) {
		Defend = defend;
	}

	public int getBaseNum() {
		return baseNum;
	}

	public void setBaseNum(int baseNum) {
		this.baseNum = baseNum;
	}

	public int getRank() {
		return Rank;
	}

	public void setRank(int rank) {
		Rank = rank;
	}

	public int getHexNum() {
		return hexNum;
	}

	public void setHexNum(int hexNum) {
		this.hexNum = hexNum;
	}

	public int getCost() {
		return Cost;
	}

	public void setCost(int cost) {
		Cost = cost;
	}

	public int getX() {
		return X;
	}

	public void setX(int x) {
		X = x;
	}

	public int getY() {
		return Y;
	}

	public void setY(int y) {
		Y = y;
	}

	public boolean isMoved() {
		return isMoved;
	}

	public void setMoved(boolean isMoved) {
		this.isMoved = isMoved;
	}

	public boolean isAlive() {
		return isAlive;
	}

	public void setAlive(boolean isAlive) {
		this.isAlive = isAlive;
	}

	public String getMove() {
		return move;
	}

	public void setMove(String move) {
		this.move = move;
	}
	
}
