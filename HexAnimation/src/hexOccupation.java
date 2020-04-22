

public class hexOccupation {
	private int player;
	private int base;
	private int fighter;
	private int playerDeath;
	private int fighterRank;
	private int playerOutpost;
	
	public int getPlayerOutpost() {
		return playerOutpost;
	}

	public void setPlayerOutpost(int playerOutpost) {
		this.playerOutpost = playerOutpost;
	}

	public int getPlayerDeath() {
		return playerDeath;
	}

	public int getFighterRank() {
		return fighterRank;
	}

	public void setFighterRank(int fighterRank) {
		this.fighterRank = fighterRank;
	}

	public void setPlayerDeath(int playerDeath) {
		this.playerDeath = playerDeath;
	}

	private String occupiedBy;
	
	public hexOccupation(){
		
	}

	public String getOccupiedBy() {
		return occupiedBy;
	}

	public void setOccupiedBy(String occupiedBy) {
		this.occupiedBy = occupiedBy;
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

	public int getFighter() {
		return fighter;
	}

	public void setFighter(int fighter) {
		this.fighter = fighter;
	}	
}