package resources;

public class Player {
	private int startingBalance;
	private int laidBet;
	private Hand hand;
	private String username;
	
	public Player(String username) {
		this.username = username;
		this.hand = new Hand();
	}
	
	public void setStartingBalance(int startingBalance) {
		this.startingBalance = startingBalance;
	}
	
	public String getUsername() {
		return username;
	}

}
