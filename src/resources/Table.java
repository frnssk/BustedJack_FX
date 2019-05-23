package resources;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import communications.GameOver;
import communications.PlayerChoice;
import communications.StartingInformation;
import communications.TableID;
import communications.UpdateClientInformation;
import communications.UpdateUI;
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
	private ArrayList<ClientHandler> clientList = new ArrayList<>(); //Holds all the clientHandlers
	private HashMap<ClientHandler, Player> playerAndClient = new HashMap<>(); //Holds all the clientHandlers, with corresponding players

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
	/*
	 * Returns the game-information
	 */
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

	/*
	 * Adds a new player to the table 
	 */
	public void addPlayer(Player player) {
		playerList.add(player);
		System.out.println("[TABLE] == Antal på bord = " + playerList.size());
	}

	/*
	 * Adds a new client to the table
	 */
	public void addClient(ClientHandler clientHandler) {
		clientList.add(clientHandler);
	}

	/*
	 * Adds a client+player
	 */
	public void addClientAndPlayer(ClientHandler clientHandler, Player player) {
		playerAndClient.put(clientHandler, player);
	}

	/*
	 * Returns the hashMap containing clients/players
	 */
	public HashMap<ClientHandler, Player> getPlayerAndClient(){
		return playerAndClient;
	}

	/*
	 * returns the arrayList of players
	 */
	public ArrayList<Player> getPlayerList(){
		return playerList;
	}

	/*
	 * returns if the table-thread is running
	 */
	public boolean checkTableStarted() {
		return this.isAlive();
	}

	/*
	 * Used by server to set the ID
	 */
	public void setTableId(int id) {
		this.tableID = id;
	}

	/*
	 * returns the ID
	 */
	public int getTableId() {
		return tableID;
	}

	/*
	 * returns the amount of players, used to not add to many
	 */
	public int getNumberOfPlayers() {
		return this.playerList.size();
	}

	//TESTING
	/*
	 * Never used?
	 */
	public void setPlayerChoice(Player player, PlayerChoice playerChoice) {
		player.setPlayerChoice(playerChoice);
	}

	/*
	 * should be updated with the correct object
	 */
	public void updateTableInformation() {
		System.out.println("Uppdaterar alla klienter");
		UpdateClientInformation updateClientInformation = new UpdateClientInformation(playerList, dealer);
		for(int i = 0; i < clientList.size(); i++) {
			clientList.get(i).output(updateClientInformation);
		}
	}

	public void run() {
		try {
			TextWindow.println("[TABLE=" + getTableId() + "]"+ " >> tråd för bordet startad.");
			TextWindow.println("[TABLE=" + getTableId() + "]"+ " >> BustedJack 2019.");
			startGame();				//starts game and sets the balance for every player
			sendStartingInformation();	//updates all the clients with time/rounds/miniBet
			int i = 0;
			while( i <= numberOfRounds) {
				//		testingChoices();
				checkCheatChoice();			//controls that every player made a choice
				checkBets();
				//		checkBetsMade();			//controls that every player made a bet
				//		checkWhatPlayerBet();		//controls how much everyone bet
				dealCardToPlayers();		//deals 1 card each
				dealCardToDealer();			//deals one face-down-card to the dealer
				dealCardToPlayers();		//deals a second card to all the players
				resetPlayerCheatChoice();	//resets the cheat-choice for everyone
				dealCardToDealer();			//deals a second face-down-card to the dealer
				printAllCards();			//TESTING
				checkForBlackjack();		//checks if anyone hit 21 in their first 2 cards
				flipDealerCard();			//flips the first card the dealer got, face-up
				//			checkForSplit();			//check all the players and if they can split, and lets them if they want
				//			checkInsurance();	//checks if the dealer got an ace, and if any player wants to buy insurance
				checkPlayerChoices();		//lets the players play each hand
				//		letPlayerBust();			//lets the players bust each other
				letDealerPlay();			//if the dealer is <17, he keeps on hitting
				compareDealerToPlayers();	//checks whether or not the players beat the dealer
				payout();					//pays out if players won, takes the money if they lost
				reset();
				i++;
				numberOfRounds -= 1;
				sendStartingInformation();
			}
			for(int j = 0; j < clientList.size(); j++) {
				clientList.get(j).output(new GameOver());
			}
		}catch(InterruptedException e) {}
	}

	/*
	 * Used to test that the deal-methods are working
	 */
	private void printAllCards() {
		for(int i = 0; i < playerList.size(); i++) {
			TextWindow.println("Antal kort på handen för " + playerList.get(i).getUsername() + ": " + playerList.get(i).getHand(0).size());
			TextWindow.println("Korten: " + playerList.get(i).getHand(0).toString());
			TextWindow.println("Summa: " + playerList.get(i).getHand(0).getCurrentScore());
		}
		TextWindow.println("Summa dealer: " + dealer.getValue() + ", KORT= " + dealer.toString());
	}

	/*
	 * Starts the game, sets all the balances and shuffles the shoes
	 */
	private void startGame() {
		TextWindow.println("[TABLE=" + getTableId() + "]" + " metod 1 (sätter startsumma) startad.");
		for(int i = 0; i < playerList.size(); i++) {
			playerList.get(i).setBalance(startingMoney);
		}
		regularShoe.shuffle();
		cheatShoe.shuffle();
		TextWindow.println("[TABLE=" + getTableId() + "]" + " metod 1 avslutad, spelarnas startsumma satt till: " + startingMoney);
		updateTableInformation();
		for(int i = 0; i < clientList.size(); i++) {
			clientList.get(i).output(new UpdateUI("Make a cheat-choice"));
		}
	}

	/*
	 * send timer + rounds + minibet
	 */
	public void sendStartingInformation() {
		StartingInformation startInfo = new StartingInformation(this.getMinutes(), this.getRounds(), this.getMinimumBet(), this.getStartingMoney());
		for(int i = 0; i < clientList.size(); i++) {
			clientList.get(i).output(startInfo);
		}
	}

	//checks that all players has made a choice to cheat or not
	private void checkCheatChoice() throws InterruptedException {
		TextWindow.println("[TABLE=" + getTableId() + "]" + " metod 2 (kollar fusk-val) startad.");
		boolean allPlayersReady = false;
		boolean[] allPlayerChoices = new boolean[playerList.size()];
		while(!allPlayersReady) {
			for(int i = 0; i < playerList.size(); i++) {
				if(!playerList.get(i).isPlayerIsOut()) {
					allPlayerChoices[i] = playerList.get(i).getHasMadeCheatChoice();
					Thread.sleep(500);
					updateTableInformation();
				}else {
					allPlayerChoices[i] = true;
					//					playerList.get(i).setHasMadeCheatChoice(true);
				}
			}
			allPlayersReady = areAllTrue(allPlayerChoices);
		}
		TextWindow.println("[TABLE=" + getTableId() + "] metod 2 (kollar fusk-val) avslutad.");
		TextWindow.println("[TABLE=" + getTableId() + "] >> GREAT SUCCES");
		updateTableInformation();
		for(int i = 0; i < clientList.size(); i++) {
			clientList.get(i).output(new UpdateUI("Place your bets"));
		}
	}

	//the new, updated method to use
	private void checkBets() {
		TextWindow.println("[TABLE=" + getTableId() + "] >> metod 3 (kollar insatser) startad.");
		for(int i = 0; i < playerList.size(); i++) {
			if(!playerList.get(i).isPlayerIsOut()) {
				TextWindow.println("Inside for-loop");
				Player player = playerList.get(i);
				while(!player.getHasMadeBet()) {
					TextWindow.println("Inside while-loop, wating for bets");
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					updateTableInformation();
				}
				int bet = player.getBet();
				playerList.get(i).getHand(0).setBet(bet);
				TextWindow.println("[TABLE] >> " + player.getUsername() + ", bet = " + player.getBet());
				int newBalance = player.getBalance();
				newBalance -= bet;
				player.setBalance(newBalance);
				TextWindow.println("[TABLE] >> " + player.getUsername() + ", summa = " + player.getBalance());
			}else {
				playerList.get(i).setHasMadeBet(true);
			}
		}
		TextWindow.println("[TABLE=" + getTableId() + "] >> metod 3 (kollar insatser) avslutad.");
		for(int i = 0; i < clientList.size(); i++) {
			clientList.get(i).output(new UpdateUI("Dealing cards..."));
		}
	}

	//deals a single card to each of the players, depending on what choice they made
	private void dealCardToPlayers() throws InterruptedException {
		TextWindow.println("[TABLE=" + getTableId() + "] >> metod 4 (delar ut kort) startar.");
		TextWindow.println("Size of playerlist: " + playerList.size());
		for(int i = 0; i < playerList.size(); i++) {
			if(!playerList.get(i).isPlayerIsOut()) {
				int hands = playerList.get(i).getNumberOfHands();
				TextWindow.println(playerList.get(i).getUsername() + " har " + hands + " antal händer.");
				for(int j = 0; j < hands; j++) {
					boolean cheatChoice = playerList.get(i).getCheatChoice();
					TextWindow.println("PlayerChoice = " + cheatChoice);
					if(cheatChoice) {
						Card card = cheatShoe.dealCard();
						playerList.get(i).getHand(j).addCard(card);
						TextWindow.println("Lägger till kort, " + card.toString() + " från fusklek hos: " + playerList.get(i).getUsername());
						Thread.sleep(500);
					}else{
						Card card = regularShoe.dealCard();
						playerList.get(i).getHand(j).addCard(card);
						TextWindow.println("Lägger till kort , " + card.toString() + " från vanlig lek hos: " + playerList.get(i).getUsername());
					}
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					updateTableInformation();
				}
			}
		}
		TextWindow.println("[TABLE=" + getTableId() + "] >> metod 4 (delar ut kort) avslutad.");
	}

	//used to reset players cheat-choice so no player only receives cards from the cheat-shoe
	public void resetPlayerCheatChoice() throws InterruptedException {
		TextWindow.println("[TABLE=" + getTableId() + "] >> metod 5 (reset playerChoice) startad.");
		for(int i = 0; i < playerList.size(); i++) {
			if(!playerList.get(i).isPlayerIsOut())
				playerList.get(i).setCheatChoice(false);
		}
		TextWindow.println("[TABLE=" + getTableId() + "] >> metod 5 (reset playerChoice avslutad.");
		Thread.sleep(500);
	}

	//deals a single card to the dealer
	private void dealCardToDealer() throws InterruptedException {
		TextWindow.println("[TABLE=" + getTableId() + "] >> metod 6 (delar ut kort till dealer) startad.");
		Card card = regularShoe.dealCard();
		card.setVisibility(false);
		dealer.addCard(card);
		TextWindow.println("[TABLE=" + getTableId() + "] >> metod 6 (dealer f�r kort " + card.toString());
		TextWindow.println("[TABLE=" + getTableId() + "] >> metod 6 (delar ut kort till dealer) avslutad.");
		Thread.sleep(500);
		updateTableInformation();
	}

	//checks if a player has BlackJack, hand by hand, if not - he can continue playing
	private void checkForBlackjack() throws InterruptedException {
		TextWindow.println("[TABLE=" + getTableId() + "] >> metod 7 (kollar efter BlackJack) startad.");
		for(int i = 0; i < playerList.size(); i++) {
			if(!playerList.get(i).isPlayerIsOut()) {
				int hands = playerList.get(i).getNumberOfHands();
				for(int j = 0; j < hands; j++) {
					if(playerList.get(i).getHand(j).getCurrentScore() == 21) {
						playerList.get(i).getHand(j).setBlackjack(true);
						clientList.get(i).output(new UpdateUI(playerList.get(i).getUsername() + " got Black Jack!!!"));
					}
//					updateTableInformation();
				}
			}
		}
		TextWindow.println("[TABLE=" + getTableId() + "] >> metod 7 (kollar efter BlackJack) avslutad.");
		Thread.sleep(500);
	}

	//never used yet
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
		TextWindow.println("[TABLE=" + getTableId() + "] >> DEALER = " + dealer.getValue());
		updateTableInformation();
	}

	private void checkInsurance(){
		TextWindow.println("[TABLE=" + getTableId() + "] >> metod 10 (kollar om insurance behövs) startad.");
		if(dealer.getCard(0).getRank() == Rank.ACE) {
			for(int i = 0; i < playerList.size(); i++) {
				for(int j = 0; j < playerList.get(i).getNumberOfHands(); j++) {
					while(!playerList.get(i).getHand(j).getHasMadeInsuranceChoice()) {
						try {
							Thread.sleep(500);
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
					updateTableInformation();
				}
			}
		}
		TextWindow.println("[TABLE=" + getTableId() + "] >> metod 10 (kollar om insurance behövs) avslutad.");
	}

	private void checkPlayerChoices() throws InterruptedException {
		TextWindow.println("[TABLE=" + getTableId() + "] >> metod 11 (kollar vilka val spelare har gjort) startad.");

		for(int i = 0; i < playerList.size(); i++) {
			if(!playerList.get(i).isPlayerIsOut()) {
				//				TextWindow.println("[TABLE=" + getTableId() + "] >> " + playerList.get(i).getUsername() + "s tur.");
				for(int j = 0; j < playerList.get(i).getNumberOfHands(); j++) {
					for(int k = 0; k < clientList.size(); k++) {
						clientList.get(k).output(new UpdateUI(playerList.get(i).getUsername() + "'s turn to play hand: " + (j+1) + "..."));
					}
					boolean keepPlaying = true;
					playerList.get(i).getHand(j).setDisplayValue(true);
					
					updateTableInformation();
					
					playerList.get(i).setPlayerChoice(new PlayerChoice(0));
					while(keepPlaying) {
						if(playerList.get(i).getHand(j).hasBlackjack()) {
							TextWindow.println("[TABLE=" + getTableId() + "] >> " + playerList.get(i).getUsername() + " har blackjack");
							keepPlaying = false;
						}
						int choice = playerList.get(i).getHand(j).getPlayerChoice().getChoice();
						TextWindow.println("while-keepPlaying");
						TextWindow.println("int choice = " + choice);
						if(choice == 1) {
							Card card = regularShoe.dealCard();
							TextWindow.println(playerList.get(i).getUsername() + " har valt HIT");
							playerList.get(i).getHand(j).addCard(card);
							TextWindow.println("[TABLE] Lägger till kort: " + card.toString() + " hos " + playerList.get(i).getUsername() + " i hand: " + j);
							TextWindow.println("[TABLE] Summa för: " + playerList.get(i).getUsername() + ", : " + playerList.get(i).getHand(j).getCurrentScore());
							playerList.get(i).setPlayerChoice(new PlayerChoice(0));
							choice = 0;
							updateTableInformation();
							if(playerList.get(i).getHand(j).getCurrentScore() >= 21) {
								if(playerList.get(i).getHand(j).getCurrentScore() > 21) {
									for(int k = 0; k < clientList.size(); k++) {
										clientList.get(i).output(new UpdateUI(playerList.get(i).getUsername() + " got fat!"));
										updateTableInformation();
									}
								}
								Thread.sleep(1500);
								playerList.get(i).getHand(j).setFinished(true);
								playerList.get(i).getHand(j).setDisplayValue(false);
								updateTableInformation();
								//
								//								choice = 0;
								keepPlaying = false;	
							}

						}else if(choice == 2) {
							playerList.get(i).setPlayerChoice(new PlayerChoice(0));
							TextWindow.println("FUCK SHIT UP");
							//						playerList.get(i).getHand(j).setFinished(true);
							//
							choice = 0;
							playerList.get(i).getHand(j).setDisplayValue(false);
//							playerList.get(i).getHand(j+1).setDisplayValue(true);
							updateTableInformation();
							keepPlaying = false;
						}else if(choice == 3) {
							int bet = playerList.get(i).getBet();
							playerList.get(i).getHand(j).setBet(bet*2);
							//
							playerList.get(i).setBet(bet*2);
							int newBalance = playerList.get(i).getBalance();
							newBalance -= bet;
							playerList.get(i).setBalance(newBalance);
							Card card = regularShoe.dealCard();
							playerList.get(i).getHand(j).addCard(card);
							TextWindow.println("[TABLE] Lägger till kort: " + card.toString() + " hos " + playerList.get(i).getUsername());
							TextWindow.println("[TABLE] Summa för: " + playerList.get(i).getUsername() + ", : " + playerList.get(i).getHand(j).getCurrentScore());
							playerList.get(i).setPlayerChoice(new PlayerChoice(0));
							//						playerList.get(i).getHand(j).setFinished(true);
							choice = 0;
							playerList.get(i).getHand(j).setDisplayValue(false);
							updateTableInformation();
							keepPlaying = false;
						}else if(choice == 6) {
							boolean splitChoice = playerList.get(i).getHand(j).getSplitChoice();
							TextWindow.println("SplitChoice = " + splitChoice);
							if(splitChoice) {					//checks if a player wants to split a hand
								TextWindow.println("Inside if-statement");
								Card card = playerList.get(i).getHand(j).getCard();				//gets a card from the hand
								playerList.get(i).addNewHand();									//creates new hand
								int numberOfHands = playerList.get(i).getNumberOfHands();		//checks how many hands a player has
								playerList.get(i).getHand(numberOfHands-1).addCard(card);			//adds the card to the newest hand
								Card card1 = regularShoe.dealCard();
								Card card2 = regularShoe.dealCard();
								playerList.get(i).getHand(j).addCard(card1);
								playerList.get(i).getHand(j+1).addCard(card2);
								playerList.get(i).getHand(j+1).setBet(playerList.get(i).getHand(j).getBet());
								TextWindow.println("[TABLE] >> Lägger till " + card1.toString() + " i hand 1, och " + card2.toString() + " i hand2");
								int newBalance = playerList.get(i).getBalance();
								newBalance -= playerList.get(i).getHand(j).getBet();
								playerList.get(i).setBalance(newBalance);
								TextWindow.println("Antal händer= " + playerList.get(i).getNumberOfHands());
								playerList.get(i).setPlayerChoice(new PlayerChoice(0));
								updateTableInformation();
							}
						}
						if(!keepPlaying) {
							TextWindow.println("[TABLE] " + playerList.get(i).getUsername() + " har spelat klart hand: " + j);
							playerList.get(i).getHand(j).setDisplayValue(false);;
							choice = 0;
							updateTableInformation();
						}
						Thread.sleep(1000);
					}
					TextWindow.println("Runda slut för " + playerList.get(i).getUsername());
					playerList.get(i).setPlayerChoice(new PlayerChoice(0));
					TextWindow.println("Test choice" + playerList.get(i).getHand(j).getPlayerChoice().getChoice());

				}
			}
		}
		TextWindow.println("[TABLE=" + getTableId() + "] >> metod 11 (kollar vilka val spelare har gjort) avslutad.");
	}

	//	private void letPlayerBust() {
	//		for(int i = 0; i < playerList.size(); i++) {
	//			Player player = playerList.get(i);
	//			if(player.getBustChoice()) {
	//				Player playerToBust = player.getPlayerToBust();
	//				Player bustedPlayer = bustPlayer(player, playerToBust);
	//				if(playerToBust.getBustedStatus()) {
	//					//player already busted, can't bust twice
	//				}
	//				if(bustedPlayer == null) {
	//					//both players okey
	//				}
	//			}
	//
	//		}
	//	}

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
		TextWindow.println("[TABLE=" + getTableId() + "] >> metod 12 (l�ter dealer spela) startad.");
		while(dealer.getValue() < 17) {
			Card card = regularShoe.dealCard();
			dealer.addCard(card);
			TextWindow.println("Dealern f�r " + card.toString());
			updateTableInformation();
			try {
				Thread.sleep(1500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		for(int i = 0; i < clientList.size(); i++) {
			clientList.get(i).output(new UpdateUI("Dealer got " + dealer.getValue()));
		}
	}


	//compare the scores of all the players to the dealer
	private void compareDealerToPlayers() {
		TextWindow.println("[TABLE=" + getTableId() + "] >> metod 13 (j�mf�r spelare mot dealer) startar.");
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
		TextWindow.println("[TABLE=" + getTableId() + "] >> metod 14 (delar ut pengar) startar.");
		for(int i = 0; i < playerList.size(); i++) {
			int hands = playerList.get(i).getNumberOfHands();
			for(int j = 0; j < hands; j++) {
				int win = playerList.get(i).getHand(j).isHandWin();
				if(win == 1) {
					int bet = playerList.get(i).getHand(j).getBet();
					int payout = bet * 2;
					playerList.get(i).setBalance(playerList.get(i).getBalance() + payout);
					TextWindow.println(playerList.get(i).getUsername() + " vann");
					if(playerList.get(i).getHand(j).hasBlackjack()) {
						payout = bet + (bet/2);
						playerList.get(i).getHand(j).setPayout(payout);
						playerList.get(i).setBalance(playerList.get(i).getBalance() + payout);
						TextWindow.println(playerList.get(i).getUsername() + " fick black jack");
					}
				}else if(win == 0) {
					int bet = playerList.get(i).getHand(j).getBet();
					int payout = bet;
					playerList.get(i).setBalance(playerList.get(i).getBalance() + payout);
				}
			}
			if(this.getMinimumBet() > playerList.get(i).getBalance()) {
				playerList.get(i).setPlayerIsOut(true);
			}
		}
		updateTableInformation();
	}

	// reset values of all players to start a new round
	private void reset() throws InterruptedException {
		TextWindow.println("[TABLE=" + getTableId() + "] >> metod 15 Reset v�rden");
		dealer.clear();
		for(int i = 0; i < playerList.size() ; i++) {
			//			int hands =  playerList.get(i).getNumberOfHands();			
			//			for(int j = hands -1; j > 0; j--) {
			playerList.get(i).setPlayerChoice(new PlayerChoice(0));
			playerList.get(i).setBet(0);
			playerList.get(i).removeHands();
			playerList.get(i).addNewHand();
			playerList.get(i).setHasMadeCheatChoice(false);
			playerList.get(i).setHasMadeBet(false);
			//				playerList.get(i).getHand(j).setHandIsWin(0);
			//				playerList.get(i).getHand(j).setBlackjack(false);
			//				playerList.get(i).getHand(j).clear();

			updateTableInformation();
		}
		updateTableInformation();
		Thread.sleep(5000);
		for(int i = 0; i < clientList.size(); i++) {
			clientList.get(i).output(new UpdateUI("Shuffling deck..."));
		}
		Thread.sleep(2000);
		for(int i = 0; i < clientList.size(); i++) {
			clientList.get(i).output(new UpdateUI("Make a cheat-choice"));
		}
	}
	//		updateTableInformation();
	//	}

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
