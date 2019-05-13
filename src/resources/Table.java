package resources;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import communications.PlayerChoice;
import communications.TableID;
import resources.Card.Rank;
import server.Server.ClientHandler;
import server.TextWindow;

public class Table extends Thread implements Serializable {

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
	private boolean privateStatus;
	private TableID tableIDtest;

	private ArrayList<Player> playerList = new ArrayList<>(); //Holds all the players for the game
//	private ArrayList<Player> newPlayerList = new ArrayList<>();//holds an updated version of the PlayerList (players who get blackjack should not be able to continue playing)
	private HashMap<ClientHandler, Player> playerAndClient = new HashMap<>();
	private ArrayList<ClientHandler> clientList = new ArrayList<>();
	
	public Table(int numberOfMinutes, int numberOfRounds, int startingMoney, int minimumBet, boolean privateStatus) {
		this.numberOfMinutes = numberOfMinutes;
		this.numberOfRounds = numberOfRounds;
		this.startingMoney = startingMoney;
		this.minimumBet = minimumBet;
		this.privateStatus = privateStatus;
		dealer = new DealerHand();
		cheatShoe = new CheatShoe(6);
		regularShoe = new Shoe(6);
		tableRunning = false;
	}
	
	public void setPrivateStatus(boolean bool) {
		this.privateStatus = bool;
	}
	
	public int getMinutes() {
		return numberOfMinutes;
	}
	
	public int getRounds() {
		return numberOfRounds;
	}
	
	public int getMinimumBet() {
		return minimumBet;
	}
	
	public int getStartingMoney() {
		return startingMoney;
	}
	
	public boolean getPrivateStatus() {
		return privateStatus;
	}
	
	public void addClient(ClientHandler clientHandler) {
		clientList.add(clientHandler);
	}
	
	public void addClientAndPlayer(ClientHandler clientHandler, Player player) {
		playerAndClient.put(clientHandler, player);
	}
	
	public HashMap<ClientHandler, Player> getPlayerAndClient(){
		return playerAndClient;
	}

	public void addPlayer(Player player) {
		playerList.add(player);
		System.out.println("[TABLE] == Antal på bord = " + playerList.size());
	}
	
	public ArrayList<Player> getPlayerList(){
		return playerList;
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
	
	//TESTING
	public void setPlayerChoice(Player player, PlayerChoice playerChoice) {
		player.setPlayerChoice(playerChoice);
	}
	
	public void updateTableInformation() {
		for(int i = 0; i < clientList.size(); i++) {
//			clientList.get(i).updateTableInformation(playerList); //already sending ArrayList with another purpose
		}
	}

	public void run() {
		try {
		TextWindow.println("[TABLE=" + getTableId() + "]"+ " >> tråd för bordet startad.");
		startGame();				//starts game and sets the balance for every player
		testingChoices();
		checkCheatChoice();			//controls that every player made a choice
		checkBets();
//		checkBetsMade();			//controls that every player made a bet
//		checkWhatPlayerBet();		//controls how much everyone bet
		dealCardToPlayers();		//deals 1 card each
		dealCardToDealer();			//deals one face-down-card to the dealer
		dealCardToPlayers();		//deals a second card to all the players
		printAllCards();			//TESTING
		resetPlayerCheatChoice();	//resets the cheat-choice for everyone
		dealCardToDealer();			//deals a second face-down-card to the dealer
		checkForBlackjack();		//checks if anyone hit 21 in their first 2 cards
		flipDealerCard();			//flips the first card the dealer got, face-up
		checkForSplit();			//check all the players and if they can split, and lets them if they want
		checkInsurance();			//checks if the dealer got an ace, and if any player wants to buy insurance
		checkPlayerChoices();		//lets the players play each hand
		letPlayerBust();			//lets the players bust each other
		letDealerPlay();			//if the dealer is <17, he keeps on hitting
		compareDealerToPlayers();	//checks whether or not the players beat the dealer
		payout();					//pays out if players won, takes the money if they lost
		}catch(InterruptedException e) {}
	}
	
	private void printAllCards() {
		for(int i = 0; i < playerList.size(); i++) {
			TextWindow.println("Antal kort på handen för " + playerList.get(i).getUsername() + ": " + playerList.get(i).getHand(0).size());
			TextWindow.println("Korten: " + playerList.get(i).getHand(0).toString());
			TextWindow.println("Summa: " + playerList.get(i).getHand(0).getCurrentScore());
		}
	}

	private void startGame() {
		TextWindow.println("[TABLE=" + getTableId() + "]" + " metod 1 (sätter startsumma) startad.");
		for(int i = 0; i < playerList.size(); i++) {
			playerList.get(i).setStartingBalance(startingMoney);
		}
		regularShoe.shuffle();
		cheatShoe.shuffle();
		TextWindow.println("[TABLE=" + getTableId() + "]" + " metod 1 avslutad, spelarnas startsumma satt till: " + startingMoney);
//		updateTableInformation();
	}
	
	private void testingChoices() {
		TextWindow.println("[TABLE=" + getTableId() + "]" + " metod 2 startad.");
		boolean allPlayersReady = false;
		boolean[] allPlayerChoices = new boolean[playerList.size()];
		while(!allPlayersReady) {
			for(int i = 0; i < playerList.size(); i++) {
				allPlayerChoices[i] = playerList.get(i).getHasMadeCheatChoice();
			}
			allPlayersReady = areAllTrue(allPlayerChoices);
		}
		TextWindow.println("[TABLE=" + getTableId() + "] metod 2 avslutad.");
		TextWindow.println("[TABLE=" + getTableId() + "] >> GREAT SUCCES");
		
	}

	//checks that all players has made a choice to cheat or not
	private void checkCheatChoice() {
		TextWindow.println("[TABLE=" + getTableId() + "]" + " metod 2 (kollar fusk-val) startad.");
		boolean allPlayersReady = false;
		boolean[] allPlayerChoices = new boolean[playerList.size()];
		while(!allPlayersReady) {
			for(int i = 0; i < playerList.size(); i++) {
				allPlayerChoices[i] = playerList.get(i).getHasMadeCheatChoice();
			}
			allPlayersReady = areAllTrue(allPlayerChoices);
		}
		TextWindow.println("[TABLE=" + getTableId() + "] metod 2 (kollar fusk-val) avslutad.");
		TextWindow.println("[TABLE=" + getTableId() + "] >> GREAT SUCCES");
//		updateTableInformation();
	}

	//the new, updated method to use
	private void checkBets() {
		TextWindow.println("[TABLE=" + getTableId() + "] >> metod 3 (kollar insatser) startad.");
//		for(int i = 0; i < playerList.size(); i++) {
//			playerList.get(i).setGrayOut(true);
//		}
		for(int i = 0; i < playerList.size(); i++) {
//			playerList.get(i).setButtonsAreGray(false);
			TextWindow.println("Inside for-loop");
			while(!playerList.get(i).getHasMadeBet()) {
				TextWindow.println("Inside while-loop");
				int bet = playerList.get(i).getBet();
				int newBalance = playerList.get(i).getBalance() - bet;
				playerList.get(i).setBalance(newBalance);
				TextWindow.println("End of while-loop");
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
//			playerList.get(i).setButtonsAreGray(true);
			
		}
		TextWindow.println("[TABLE=" + getTableId() + "] >> metod 3 (kollar insatser) avslutad.");
//		updateTableInformation();
	}
	
	//checks that all players has made a bet
//	private void checkBetsMade() {
//		boolean allPlayersMadeBet = false;
//		boolean[] allPlayersHasMadeBet = new boolean[playerList.size()];
//		while(!allPlayersMadeBet) {
//			for(int i = 0; i < playerList.size(); i++) {
//				allPlayersHasMadeBet[i] = playerList.get(i).getBetMade();
//			}
//			allPlayersMadeBet = areAllTrue(allPlayersHasMadeBet);
//		}
//	}

	//used to set the bet for each player
//	private void checkWhatPlayerBet(){
//		int[] betAmount = new int[playerList.size()];
//		for(int i = 0; i < playerList.size(); i++) {
//			betAmount[i] = playerList.get(i).getBet();
//			try {
//				Thread.sleep(1500);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
//		//should update the UI for every player with what bet they made
//	}

	//deals a single card to each of the players, depending on what choice they made
	private void dealCardToPlayers() throws InterruptedException {
		TextWindow.println("[TABLE=" + getTableId() + "] >> metod 4 (delar ut kort) startar.");
		TextWindow.println("Size of playerlist: " + playerList.size());
		for(int i = 0; i < playerList.size(); i++) {
			int hands = playerList.get(i).getNumberOfHands();
			TextWindow.println(playerList.get(i).getUsername() + " har " + hands + " antal händer.");
			for(int j = 0; j < hands; j++) {
				boolean cheatChoice = playerList.get(i).getCheatChoice();
				TextWindow.println("PlayerChoice = " + cheatChoice);
				if(cheatChoice) {
					Card card = cheatShoe.dealCard();
					Hand hand = playerList.get(i).getHand(j);
					hand.addCard(card);
//					playerList.get(i).getHand(j).addCard(card);
					TextWindow.println("Lägger till kort, " + card.toString() + " från fusklek hos: " + playerList.get(i).getUsername());
					Thread.sleep(1000);
					TextWindow.println("Loop1: " + i);
					TextWindow.println("Loop2: " + j);
				}else{
					Card card = regularShoe.dealCard();
					playerList.get(i).getHand(j).addCard(card);
					TextWindow.println("Lägger till kort , " + card.toString() + " från vanlig lek hos: " + playerList.get(i).getUsername());
				}
				try {
					Thread.sleep(1500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		TextWindow.println("[TABLE=" + getTableId() + "] >> metod 4 (delar ut kort) avslutad.");
	}

	//used to reset players cheat-choice so no player only receives cards from the cheat-shoe
	public void resetPlayerCheatChoice() throws InterruptedException {
		TextWindow.println("[TABLE=" + getTableId() + "] >> metod 5 (reset playerChoice) startad.");
		for(int i = 0; i < playerList.size(); i++) {
			playerList.get(i).setCheatChoice(false);
		}
		TextWindow.println("[TABLE=" + getTableId() + "] >> metod 5 (reset playerChoice avslutad.");
		Thread.sleep(2000);
	}

	//deals a single card to the dealer
	private void dealCardToDealer() throws InterruptedException {
		TextWindow.println("[TABLE=" + getTableId() + "] >> metod 6 (delar ut kort till dealer) startad.");
		Card card = regularShoe.dealCard();
		card.setVisibility(false);
		dealer.addCard(card);
		TextWindow.println("[TABLE=" + getTableId() + "] >> metod 6 (delar ut kort till dealer) avslutad.");
		Thread.sleep(2000);
	}

	//checks if a player has BlackJack, hand by hand, if not - he can continue playing
	private void checkForBlackjack() throws InterruptedException {
		TextWindow.println("[TABLE=" + getTableId() + "] >> metod 7 (kollar efter BlackJack) startad.");
		for(int i = 0; i < playerList.size(); i++) {
			int hands = playerList.get(i).getNumberOfHands();
			for(int j = 0; j < hands; j++) {
				if(playerList.get(i).getHand(j).getCurrentScore() == 21) {
					playerList.get(i).getHand(j).setBlackjack(true);
				}
			}
		}
		TextWindow.println("[TABLE=" + getTableId() + "] >> metod 7 (kollar efter BlackJack) avslutad.");
		Thread.sleep(2000);
	}

	// checks if firsts two cards has the value of 21
//	private void checkBlackJack() {		
//		for(int i = 0; i < playerList.size(); i++) {
//			int hands = playerList.get(i).getNumberOfHands();
//			for(int j = 0; j < hands; j++) {
//				if(playerList.get(i).getHand(j).size() == 2)
//					if(playerList.get(i).getHand(j).getCurrentScore() == 21){
//						playerList.get(i).getHand(j).setBlackjack(true);					
//					}
//			}
//		}
//	}

	//never used
	private void checkIsPlayerBust() {
		for(int i = 0; i < playerList.size(); i++) {
			int hands = playerList.get(i).getNumberOfHands();
			for(int j = 0; j < hands; j++) {
				if(playerList.get(i).getHand(j).getCurrentScore() > 21){
					playerList.get(i).getHand(j).setBustedHand(true);
				}
			}
		}
	}

	//should flip the first card the dealer got
	private void flipDealerCard() {
		TextWindow.println("[TABLE=" + getTableId() + "] >> metod 8 (flippar dealerns första kort) startad.");
		dealer.getCard(0).setVisibility(true);
		TextWindow.println("[TABLE=" + getTableId() + "] >> metod 8 (flippar dealerns första kort) avslutad.");
	}

	private void checkForSplit(){
		TextWindow.println("[TABLE=" + getTableId() + "] >> metod 9 (kollar efter split) startad.");
		//		boolean allTrue = false;
//		boolean[] allPlayerReady = null;
		boolean allPlayersAllHandsChecked = false;

		while(!allPlayersAllHandsChecked) {
			for(int i = 0; i < playerList.size(); i++) {							//loops all the players
				for(int j = 0; j < playerList.get(i).getNumberOfHands(); j++) {		//loops all the hand of all the players
					TextWindow.println("Inside inner for-loop...");
					if(playerList.get(i).getHand(j).ableToSplit()) {				//checks if a player can split a hand
						TextWindow.println(playerList.get(i).getUsername() + " kan splitta sin hand.");
						//wait until player has made a choice
						while(!playerList.get(i).getHand(j).getSplitChoice()) {
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
						if(playerList.get(i).getHand(j).wantToSplit()) {					//checks if a player wants to split a hand
							Card card = playerList.get(i).getHand(j).getCard();				//gets a card from the hand
							playerList.get(i).addNewHand();									//creates new hand
							int numberOfHands = playerList.get(i).getNumberOfHands();		//checks how many hands a player has
							playerList.get(i).getHand(numberOfHands).addCard(card);			//adds the card to the newest hand
							if(playerList.get(i).getHand(j).size() == 1) { 					//if a player only has one card in one hand - adds new card
								playerList.get(i).getHand(j).addCard(regularShoe.dealCard());	//deals the actual card
							}
						}
					}
				}
			}
			int max = 0;
			for (int i = 0; i < playerList.size(); i++){
				for(int j = 0; j < playerList.get(i).getNumberOfHands(); j++) {
					if(playerList.get(i).getNumberOfHands() > max)
						max = playerList.get(i).getNumberOfHands();
				}
			}
			boolean[][] test = new boolean[playerList.size()][max];
			//makes new array but loads no values into it
			allPlayersAllHandsChecked = areAllTrue2d(test);
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			TextWindow.println("[TABLE=" + getTableId() + "] kollar efter splits...");
		}
		TextWindow.println("[TABLE=" + getTableId() + "] >> metod 9 (kollar efter split) avslutad.");
	}

	private void checkInsurance(){
		TextWindow.println("[TABLE=" + getTableId() + "] >> metod 10 (kollar om insurance behövs) startad.");
		if(dealer.getCard(0).getRank() == Rank.ACE) {
			for(int i = 0; i < playerList.size(); i++) {
				for(int j = 0; j < playerList.get(i).getNumberOfHands(); j++) {
					//					boolean hasMadeChoice = playerLi;
					while(!playerList.get(i).getHand(j).getHasMadeInsuranceChoice()) {
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					if(playerList.get(i).getHand(j).getInsuranceChoice()) {
						int insuranceCost = playerList.get(i).getHand(j).getBet()/2;
						int playerBalance = playerList.get(i).getBalance();
						int newBalance = playerBalance - insuranceCost;
						playerList.get(i).setBalance(newBalance);
						if(dealer.getCard(1).getValue() == 10) {
							newBalance = insuranceCost*2;
							playerList.get(i).setBalance(newBalance);
						}
					}
				}
			}
		}
		TextWindow.println("[TABLE=" + getTableId() + "] >> metod 10 (kollar om insurance behövs) avslutad.");
	}

	private void checkPlayerChoices() {
		TextWindow.println("[TABLE=" + getTableId() + "] >> metod 11 (kollar vilka val spelare har gjort) startad.");
		boolean allPlayersReady = false;
		boolean[] allPlayersAreReady = new boolean[playerList.size()];

		while(!allPlayersReady) {
			for(int i = 0; i < playerList.size(); i++) {
				for(int j = 0; j <  playerList.get(i).getNumberOfHands(); j++) {
					int choice = playerList.get(i).getHand(i).getPlayChoice();
					int handValue = playerList.get(i).getHand(j).getCurrentScore();
					boolean hasMadeEndingChoice = playerList.get(i).getHand(j).getHasMadeEndingChoice();
					while(handValue < 21 || !hasMadeEndingChoice) {
						if(choice == 1)	{		//hit
							if(playerList.get(i).getCheatChoice())
								playerList.get(i).getHand(j).addCard(cheatShoe.dealCard());
							else
								playerList.get(i).getHand(j).addCard(regularShoe.dealCard());
						}else if(choice == 2) {
							playerList.get(i).getHand(j).setGrayOut();
						}else if(choice == 3) {
							int bet = playerList.get(i).getHand(i).getBet();
							playerList.get(i).getHand(j).setBet(bet * 2);
							playerList.get(i).getHand(j).addCard(regularShoe.dealCard());
						}
					}
					playerList.get(i).getHand(j).setHasMadeEndingChoice(true);
				}
			}
			int max = 0;
			for (int i = 0; i < playerList.size(); i++){
				for(int j = 0; j < playerList.get(i).getNumberOfHands(); j++) {
					if(playerList.get(i).getNumberOfHands() > max)
						max = playerList.get(i).getNumberOfHands();
				}
			}
			boolean[][] test = new boolean[playerList.size()][max];
			allPlayersReady = areAllTrue2d(test);
		}
		TextWindow.println("[TABLE=" + getTableId() + "] >> metod 11 (kollar vilka val spelare har gjort) avslutad.");
	}

	private void letPlayerBust() {
		for(int i = 0; i < playerList.size(); i++) {
			Player player = playerList.get(i);
			if(player.getBustChoice()) {
				Player playerToBust = player.getPlayerToBust();
				Player bustedPlayer = bustPlayer(player, playerToBust);
				if(playerToBust.getBustedStatus()) {
					//player already busted, can't bust twice
				}
				if(bustedPlayer == null) {
					//both players okey
				}
			}

		}
	}

	//the "battle" between the player who busted another, and the busted
	private Player bustPlayer(Player player1, Player player2) {
		int player1CheatHeat = player1.getCheatHeat();
		int player2CheatHeat = player2.getCheatHeat();

		if(willPlayerBust(player1CheatHeat)) {
			return player1;
		}else if(willPlayerBust(player2CheatHeat)) {
			return player2;
		}else {
			return null;
		}
	}

	//generates a random number(1-100 to simulate %), and checks if the players cheatheat is lower/higher
	private boolean willPlayerBust(int cheatHeatTest) {
		int chance = new Random().nextInt(100)+1;

		return cheatHeatTest < chance;
	}

	//as long as the dealer has less than 17, keeps on adding a card
	private void letDealerPlay(){
		while(dealer.getValue() < 17) {
			dealer.addCard(regularShoe.dealCard());
			try {
				Thread.sleep(1500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}


	//compare the scores of all the players to the dealer
	private void compareDealerToPlayers() {
		//		boolean[] playerWin = new boolean[playerList.size()];
		for(int i = 0; i < playerList.size(); i++) {
			int hands = playerList.get(i).getNumberOfHands();
			for(int j = 0; j < hands; j++) {
				int win = compareHands(playerList.get(i).getHand(j).getCurrentScore(), dealer.getValue());
				playerList.get(i).getHand(j).setHandIsWin(win);
				//				playerList.get(i).getHand(j).setHandIsWin(compareHands(playerList.get(i).getHand(j).getCurrentScore(), dealer.getValue()));
			}
		}
	}

	//should return true if player win, false if dealer wins
	private int compareHands(int playerHandValue, int dealerHandValue) {
		if(playerHandValue == 21 && dealerHandValue != 21) {
			return 1;
		}else if(playerHandValue < 22 && dealerHandValue > 21) {
			return 1;
		}else if(playerHandValue < 22 && (dealerHandValue < playerHandValue)) {
			return 1;
		}else if(playerHandValue == dealerHandValue) {
			return 0;
		}else {
			return -1;
		}
		//check all the winning conditions
	}

	private void payout() {
		for(int i = 0; i < playerList.size(); i++) {
			int hands = playerList.get(i).getNumberOfHands();
			for(int j = 0; j < hands; j++) {
				int win = playerList.get(i).getHand(j).isHandWin();
				if(win == 1) {
					int bet = playerList.get(i).getHand(j).getBet();
					int payout = bet * 2;
					if(playerList.get(i).getHand(j).hasBlackjack()) {
						payout = bet + (bet/2);

						playerList.get(i).getHand(j).setPayout(payout);
					}
				}
			}
		}
	}

	//checks if all the values in a boolean array are true
	public boolean areAllTrue(boolean[] array){
		for(boolean b : array) if(!b) {
			return false;
		}
		return true;
	}

	public boolean areAllTrue2d(boolean[][] array) {
		TextWindow.println("Inside 'areAllTrue2d'-method.");
		boolean allTrue = false;
		for(int i = 0; i < array.length; i++) {
			for(int j = 0; j < array[i].length; j++) {
				if(array[i][j] == true) {
					allTrue = true;
				}else {
					return false;
				}
			}
		}
		return allTrue;
	}

}
