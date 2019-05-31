package communications;

/**
 * Create Achievement objects to send from server to client when a player have reached an Achievement.
 * @author Isak Eklund
 * @author Christoffer Palvin
 */
public class Achievement {
	private String achievement;
	
	public void setAchievement(String achievement) {
		this.achievement = achievement;
	}
	
	public String getAchievement() {
		return achievement;
	}

}
