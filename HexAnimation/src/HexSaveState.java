import java.awt.Color;


public class HexSaveState {
	Color color;
	boolean fighter;
	boolean base;
	int player;
	int fighterRank, baseRank;
	
	public int getPlayer() {
		return player;
	}
	public void setPlayer(int player) {
		this.player = player;
	}
	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
	}
	public boolean isFighter() {
		return fighter;
	}
	public void setFighter(boolean fighter) {
		this.fighter = fighter;
	}
	public boolean isBase() {
		return base;
	}
	public void setBase(boolean base) {
		this.base = base;
	}
	public int getFighterRank() {
		return fighterRank;
	}
	public void setFighterRank(int fighterRank) {
		this.fighterRank = fighterRank;
	}
	public int getBaseRank() {
		return baseRank;
	}
	public void setBaseRank(int baseRank) {
		this.baseRank = baseRank;
	}
	
}
