
public class Move {
	
	int base;
	int fighter;
	int selectedHex;
	boolean fighterPurchased;
	boolean baseUpgrade;
	
	public Move(int b, int f, int sH, boolean pur, boolean up){
		base = b;
		fighter = f;
		selectedHex = sH;
		fighterPurchased = pur;
		baseUpgrade = up;
	}

	public int getBase() {
		return base;
	}

	public void setBase(int base) {
		this.base = base;
	}

	public int getFighter() {
		return fighter;
	}

	public void setFighter(int fighter) {
		this.fighter = fighter;
	}

	public int getSelectedHex() {
		return selectedHex;
	}

	public void setSelectedHex(int selectedHex) {
		this.selectedHex = selectedHex;
	}

	public boolean isFighterPurchased() {
		return fighterPurchased;
	}

	public void setFighterPurchased(boolean fighterPurchased) {
		this.fighterPurchased = fighterPurchased;
	}

	public boolean isBaseUpgrade() {
		return baseUpgrade;
	}

	public void setBaseUpgrade(boolean baseUpgrade) {
		this.baseUpgrade = baseUpgrade;
	}
	
}
