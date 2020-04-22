import java.awt.Color;

public class Bonus {
	private int regNum;
	private int hexAmt;
	private boolean controlled;
	private int player;
	private int base;
	private int hexNum;
	Color color;
	double thickness;
	boolean changeThicknessDir;
	
	public Bonus(){

	}

	public boolean isChangeThicknessDir() {
		return changeThicknessDir;
	}

	public void setChangeThicknessDir(boolean changeThicknessDir) {
		this.changeThicknessDir = changeThicknessDir;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public double getThickness() {
		return thickness;
	}

	public void setThickness(double thickness) {
		this.thickness += thickness;
	}

	public int getHexNum() {
		return hexNum;
	}

	public void setHexNum(int hexNum) {
		this.hexNum = hexNum;
	}

	public boolean isControlled() {
		return controlled;
	}

	public void setControlled(boolean controlled) {
		this.controlled = controlled;
	}

	public int getPlayer() {
		return player;
	}

	public void setPlayer(int player) {
		this.player = player;
	}

	public int getBase() {
		return base;
	}

	public void setBase(int base) {
		this.base = base;
	}

	public int getRegNum() {
		return regNum;
	}

	public void setRegNum(int regNum) {
		this.regNum = regNum;
	}

	public int getHexAmt() {
		return hexAmt;
	}

	public void setHexAmt(int hexAmt) {
		this.hexAmt += hexAmt;
	}
	
}