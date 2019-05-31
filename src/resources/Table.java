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

/**
 * Class that represents a table, where a game of Busted Jack is played
 * @author Rasmus Öberg
 * @author Simon Lilja
 * @autohr Isak Eklund
 *
 */
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

	/**
	 * Constructor which instantiates the table with a few given parameters
	 * @param numberOfMinutes - the number of minutes the game should go on
	 * @param numberOfRounds - the number of rounds the game should go on
	 * @param startingMoney - the amount of money everyone starts with
	 * @param minimumBet - the minimum amount of money everyone must bet
	 * @param privateStatus - the value that decides if you can join by random or only by tableID
	 */
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

	public void addPlayer(Player player) {
		playerList.add(player);
		System.out.println("[TABLE] == Antal på bord = " + playerList.size());
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

	public ArrayList<Player> getPlayerList(){
		return playerList;
	}

	public boolean checkTableStarted() {
		return this.isAlive();
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

	public void setPlayerChoice(Player player, PlayerChoice playerChoice) {
		player.setPlayerChoice(playerChoice);
	}

	/**
	 * Used to update all the clients on the table with the latest choice anyone has made
	 */
	public void updateTableInformation() {
		System.out.println("Uppdaterar alla klienter");
		UpdateClientInformation updateClientInformation = new UpdateClientInformation(playerList, dealer);
		for(int i = 0; i < clientList.size(); i++) {
			clientList.get(i).output(updateClientInformation);
		}
	}

	/**
	 * The thread which runs the table, keeps on going as long as
	 * the number of rounds is not reached
	 */
	public void run() {
		try {
			TextWindow.println("[TABLE=" + getTableId() + "]"+ " >> tråd för bordet startad.");
			startGame();				
			sendStartingInformation();	
			int i = 0;
			while( i < numberOfRounds) {
				checkCheatChoice();			
				checkBets();
				dealCardToPlayers();	
				dealCardToDealer();			
				dealCardToPlayers();		
				resetPlayerCheatChoice();	
				dealCardToDealer();			
				printAllCards();			
				checkForBlackjack();		
				flipDealerCard();			
				//			checkInsurance();	
				checkPlayerChoices();		
				//		letPlayerBust();			
				letDealerPlay();			
				compareDealerToPlayers();	
				payout();					
				reset();
<<<<<<< HEAD
		//		i++;
=======
>>>>>>> development
				numberOfRounds -= 1;
				sendStartingInformation();
			}
			for(int j = 0; j < clientList.size(); j++) {
				clientList.get(j).output(new GameOver());
			}
		}catch(InterruptedException e) {}
	}

	/**
	 * Used for debugging
	 */
	private void printAllCards() {
		for(int i = 0; i < playerList.size(); i++) {
			TextWindow.println("Antal kort på handen för " + playerList.get(i).getUsername() + ": " + playerList.get(i).getHand(0).size());
			TextWindow.println("Korten: " + playerList.get(i).getHand(0).toString());
			TextWindow.println("Summa: " + playerList.get(i).getHand(0).getCurrentScore());
		}
		TextWindow.println("Summa dealer: " + dealer.getValue() + ", KORT= " + dealer.toString());
	}

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

	public void sendStartingInformation() {
		StartingInformation startInfo = new StartingInformation(this.getMinutes(), this.getRounds(), this.getMinimumBet(), this.getStartingMoney());
		for(int i = 0; i < clientList.size(); i++) {
			clientList.get(i).output(startInfo);
		}
	}

	/**
	 * Used to know what cheat-choice a player made, to deal from the correct shoe
	 * @throws InterruptedException
	 */
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

	/**
	 * Used to set the cheat-choice of all players to false, so that after the first 2 cards, all cards they
	 * get come from the regular deck
	 * @throws InterruptedException
	 */
	public void resetPlayerCheatChoice() throws InterruptedException {
		TextWindow.println("[TABLE=" + getTableId() + "] >> metod 5 (reset playerChoice) startad.");
		for(int i = 0; i < playerList.size(); i++) {
			if(!playerList.get(i).isPlayerIsOut())
				playerList.get(i).setCheatChoice(false);
		}
		TextWindow.println("[TABLE=" + getTableId() + "] >> metod 5 (reset playerChoice avslutad.");
		Thread.sleep(500);
	}

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
				}
			}
		}
		TextWindow.println("[TABLE=" + getTableId() + "] >> metod 7 (kollar efter BlackJack) avslutad.");
		Thread.sleep(500);
	}

	/**
	 * Haven't gotten this far as to implement the bust-function
	 */
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
	
	/**
	 * The first card the dealer gets is dealt face-down, this flips it over to face-up
	 * @throws InterruptedException
	 */
	private void flipDealerCard() {
		TextWindow.println("[TABLE=" + getTableId() + "] >> metod 8 (flippar dealerns första kort) startad.");
		dealer.getCard(0).setVisibility(true);
		TextWindow.println("[TABLE=" + getTableId() + "] >> metod 8 (flippar dealerns första kort) avslutad.");
		TextWindow.println("[TABLE=" + getTableId() + "] >> DEALER = " + dealer.getValue());
		updateTableInformation();
	}

	/**
	 * Haven't got this far as to implement the insurance-function
	 */
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

	/**
	 * The "play"-method
	 * @throws InterruptedException
	 */
	private void checkPlayerChoices() throws InterruptedException {
		TextWindow.println("[TABLE=" + getTableId() + "] >> metod 11 (kollar vilka val spelare har gjort) startad.");

		for(int i = 0; i < playerList.size(); i++) {
			if(!playerList.get(i).isPlayerIsOut()) {
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
								keepPlaying = false;	
							}

						}else if(choice == 2) {
							playerList.get(i).setPlayerChoice(new PlayerChoice(0));
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
							//if(bet > balance)
							//	sendErrorMessage to client[i]
							playerList.get(i).setBalance(newBalance);
							Card card = regularShoe.dealCard();
							playerList.get(i).getHand(j).addCard(card);
							TextWindow.println("[TABLE] Lägger till kort: " + card.toString() + " hos " + playerList.get(i).getUsername());
							TextWindow.println("[TABLE] Summa för: " + playerList.get(i).getUsername() + ", : " + playerList.get(i).getHand(j).getCurrentScore());
							playerList.get(i).setPlayerChoice(new PlayerChoice(0));
							choice = 0;
							playerList.get(i).getHand(j).setDisplayValue(false);
							updateTableInformation();
							keepPlaying = false;
						}else if(choice == 6) {
							boolean splitChoice = playerList.get(i).getHand(j).getSplitChoice();
							TextWindow.println("SplitChoice = " + splitChoice);
							if(splitChoice) {				
								TextWindow.println("Inside if-statement");
								Card card = playerList.get(i).getHand(j).getCard();				
								playerList.get(i).addNewHand();									
								int numberOfHands = playerList.get(i).getNumberOfHands();		
								playerList.get(i).getHand(numberOfHands-1).addCard(card);			
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

	/**
	 * Not yet fully implemented
	 */
//		private void letPlayerBust() {
//			for(int i = 0; i < playerList.size(); i++) {
//				Player player = playerList.get(i);
//				if(player.getBustChoice()) {
//					Player playerToBust = player.getPlayerToBust();
//					Player bustedPlayer = bustPlayer(player, playerToBust);
//					if(playerToBust.getBustedStatus()) {
//						//player already busted, can't bust twice
//					}
//					if(bustedPlayer == null) {
//						//both players okey
//					}
//				}
//	
//			}
//		}

	/**
	 * Not yet fully implemented, should simulate that one player "busts" another because of cheating too much
	 * @param player1 - the player who busted another
	 * @param player2 - the busted player
	 * @return - the player who gets busted
	 */
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

	/**
	 * The "bust-battle", randomizes who wins and who loses
	 * @param cheatHeatTest - the current cheatheat of a player
	 * @return
	 */
	private boolean willPlayerBust(int cheatHeatTest) {
		int chance = new Random().nextInt(100)+1;

		return cheatHeatTest < chance;
	}

	/**
	 * In blackjack, the dealer must keep on hitting until he reaches at least 17
	 * This simulates that
	 */
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


	/**
	 * Checks if a player beat the dealer, to know if a player should win money or lose money
	 */
	private void compareDealerToPlayers() {
		TextWindow.println("[TABLE=" + getTableId() + "] >> metod 13 (j�mf�r spelare mot dealer) startar.");
		for(int i = 0; i < playerList.size(); i++) {
			int hands = playerList.get(i).getNumberOfHands();
			for(int j = 0; j < hands; j++) {
				int win = compareHands(playerList.get(i).getHand(j).getCurrentScore(), dealer.getValue());
				playerList.get(i).getHand(j).setHandIsWin(win);
			}
		}
	}

	/**
	 * The actual comparison, based on blackjack-rules
	 * @param playerHandValue - value of the players cards
	 * @param dealerHandValue - values of the dealers cards
	 * @return
	 */
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

	/**
	 * Starts a new round, resetting all the values
	 * @throws InterruptedException
	 */
	private void reset() throws InterruptedException {
		TextWindow.println("[TABLE=" + getTableId() + "] >> metod 15 Reset v�rden");
		dealer.clear();
		for(int i = 0; i < playerList.size() ; i++) {
			playerList.get(i).setPlayerChoice(new PlayerChoice(0));
			playerList.get(i).setBet(0);
			playerList.get(i).removeHands();
			playerList.get(i).addNewHand();
			playerList.get(i).setHasMadeCheatChoice(false);
			playerList.get(i).setHasMadeBet(false);
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
