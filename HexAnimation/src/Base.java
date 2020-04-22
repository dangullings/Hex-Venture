
import java.util.ArrayList;
import java.util.List;

public class Base {
	private int Col;
	private int Row;
	private int HexNum;
	private int Rank;
	private int Income;
	private int Expense;
	private int tempExpense;
	private int Balance;
	private int HexesLinked;
	private boolean isAlive;
	private boolean selected;
	private int Money;
	private int Savings;
	private int Bonus;
	private int numFighters;
	private int highestRankedFighter;
	private double explodeRadius = .00001;
	private float explodeTran = 1.0f;
	private float explodePenR = .0185f;
	private boolean explodeGraphic;

	ArrayList<Integer> divSurroundings = new ArrayList<Integer>();
	ArrayList<Integer> vulSurroundings = new ArrayList<Integer>();
	ArrayList<Integer> intFighterQueue = new ArrayList<Integer>();
	
	//Fighter[] fighter = new Fighter[hexVenture.MAX_BASE_FIGHTERS];
	ArrayList<Fighter> fighter = new ArrayList<Fighter>();
	
	public Base(){
		
	}
	
	public void drawHighlightGraphic(int p, double x, double y, double perT, double perP){
		//StdDraw.drawBloom(x, y, explodeRadius*.75, .5f, player);
		StdDraw.drawSelect(p, x, y, explodeRadius, explodeTran, explodePenR, "base", Rank);
		explodeRadius += .1;
        explodeTran -= perT;
        explodePenR -= perP;
        if (explodeTran <= 0.0)
        	explodeGraphic = false;
	}
	
	public int getHighestRankedFighter() {
		return highestRankedFighter;
	}

	public void setHighestRankedFighter(int highestRankedFighter) {
		this.highestRankedFighter = highestRankedFighter;
	}

	public int getNumFighters() {
		return numFighters;
	}

	public void setNumFighters(int numfighters) {
		numFighters+=numfighters;
	}
	
	public int getBonus() {
		return Bonus;
	}

	public void setBonus(int bonus) {
		Bonus+=bonus;
	}

	public int getSavings() {
		return Savings;
	}

	public void setSavings(int savings) {
		Savings = savings;
	}
	
	public int getMoney() {
		return Money;
	}

	public void setMoney(int money) {
		Money+=money;
	}
	
	public int getCol() {
		return Col;
	}

	public void setCol(int col) {
		Col = col;
	}

	public int getRow() {
		return Row;
	}

	public void setRow(int row) {
		Row = row;
	}
	
	public boolean isAlive() {
		return isAlive;
	}

	public void setAlive(boolean isAlive) {
		this.isAlive = isAlive;
	}

	public int getHexNum() {
		return HexNum;
	}

	public void setHexNum(int hexNum) {
		HexNum = hexNum;
	}

	public int getRank() {
		return Rank;
	}

	public void setRank(int rank) {
		Rank = rank;
	}

	public int getIncome() {
		return Income;
	}

	public void setIncome(int income) {
		Income = income;
	}

	public int getTempExpense() {
		return tempExpense;
	}

	public void setTempExpense(int tempExpense) {
		this.tempExpense = tempExpense;
	}

	public int getExpense() {
		return Expense;
	}

	public void setExpense(int expense) {
		Expense+=expense;
	}

	public int getBalance() {
		return Balance;
	}

	public void setBalance(int balance) {
		Balance = balance;
	}

	public int getHexesLinked() {
		return HexesLinked;
	}

	public void setHexesLinked(int hexesLinked) {
		HexesLinked+=hexesLinked;
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
	
}
