package resources;

import java.io.Serializable;
import java.util.*;

public class Table implements Serializable {

	private static final long serialVersionUID = 8653911597750749092L;
	private int numberOfPlayers;
	private int numberOfMinutes;
	private int numberOfRounds;
	private int startingMoney;
	private int minimumBet;
	private Shoe regularShoe;
	private CheatShoe cheatShoe;
	private DealerHand dealer;
	private int tableID;
	private boolean tableRunning;
	
	private ArrayList<Player> playerList = new ArrayList<>(); //Holds all the players for the game
	
	public Table(int numberOfMinutes, int numberOfRounds, int startingMoney, int minimumBet) {
//		this.numberOfPlayers = numberOfPlayers;
		this.numberOfMinutes = numberOfMinutes;
		this.numberOfRounds = numberOfRounds;
		this.startingMoney = startingMoney;
		this.minimumBet = minimumBet;
		cheatShoe = new CheatShoe(6);
		regularShoe = new Shoe(6);
		tableRunning = false;
	}
	
	public void addPlayer(Player player) {
		playerList.add(player);
	}
	
	public boolean checkTableStarted() {
		return tableRunning;
	}
	
	public void setTableId(int id) {
		this.tableID = id;
	}
	
	public int getTableId() {
		return tableID;
	}
	
	public int getNumberOfPlayers() {
		return this.playerList.size();
	}
	
	public void run() {
		startGame();
	}
	
	public void startGame() {
		for(int i = 0; i < playerList.size(); i++) {
			playerList.get(i).setStartingBalance(startingMoney);
		}
	}

}
