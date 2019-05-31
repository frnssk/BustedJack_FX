package application;

import java.io.IOException;
import java.util.ArrayList;

import client.UserClient;
import communications.PlayerChoice;
import communications.StartGameRequest;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import resources.DealerHand;
import resources.Player;

/**
 * Class used to control and communicate to the GameTable.fxml during a game
 * Updated UI and send button action to client 
 * @author Isak Eklund
 * @author Simon Lilja
 *
 */
public class GameController {
	@FXML private Label lblPlayer1;
	@FXML private Label lblPlayer2;
	@FXML private Label lblPlayer3;
	@FXML private Label lblPlayer4;
	@FXML private Label lblPlayer5;

	private Label[] lblPlayersArray;

	@FXML private Label lblPlayer1CardSum;
	@FXML private Label lblPlayer2CardSum;
	@FXML private Label lblPlayer3CardSum;
	@FXML private Label lblPlayer4CardSum;
	@FXML private Label lblPlayer5CardSum;

	@FXML private Label lblPlayer1Balance;
	@FXML private Label lblPlayer2Balance;
	@FXML private Label lblPlayer3Balance;
	@FXML private Label lblPlayer4Balance;
	@FXML private Label lblPlayer5Balance;

	@FXML private Label lblPlayer1Bet;
	@FXML private Label lblPlayer2Bet;
	@FXML private Label lblPlayer3Bet;
	@FXML private Label lblPlayer4Bet;
	@FXML private Label lblPlayer5Bet;

	@FXML private Label lblDealer;
	@FXML private Label lblDealerCardSum;
	@FXML private Label lblUpdate;

	@FXML private ImageView iwPlayer1Card1;
	@FXML private ImageView iwPlayer1Card2;
	@FXML private ImageView iwPlayer2Card1;
	@FXML private ImageView iwPlayer2Card2;
	@FXML private ImageView iwPlayer3Card1;
	@FXML private ImageView iwPlayer3Card2;
	@FXML private ImageView iwPlayer4Card1;
	@FXML private ImageView iwPlayer4Card2;
	@FXML private ImageView iwPlayer5Card1;
	@FXML private ImageView iwPlayer5Card2;

	private ImageView[] iwCardsArray;

	@FXML private Label lblTime;
	@FXML private Label lblRounds;
	@FXML private Label lblTableId;
	@FXML private Label lblMinimumBet;

	@FXML private ProgressBar cheatHeatProgressBar;
	@FXML private Label lblCheatHeatPercent;

	@FXML private Button btnCheat;
	@FXML private Button btnDoNotCheat;
	@FXML private Button btnBust;

	@FXML private Button btnSplit;
	@FXML private Button btnDouble;
	@FXML private Button btnStay;
	@FXML private Button btnHit;

	@FXML private Button btnIncreaseBet25;
	@FXML private Button btnIncreaseBet50;
	@FXML private Button btnIncreaseBet100;
	@FXML private Button btnIncreaseBet500;
	@FXML private Button btnConfirmBet;
	@FXML private Button btnClearBet;

	@FXML private Button btnStartGame;
	@FXML private Button btnExit;

	private Main mainApp;
	private UserClient client;

	private boolean firstRound = true;
	private boolean firstRoundBalance = true;

	private int balance = 0;
	private int minimumBet = 0;
	private int currentBet = 0;
	private int time = 0;
	private int gameControllerBalance = 0;

	private int numberOfPlayers = 0;

	private int rounds = 0;
	private int currentRound = 1;
	private int numberOfCheats = 0;
	private int cheatHeat = numberOfCheats / currentRound;
	private int counter = 0;
	private int doubleBet = 0;

	/**
	 * The method is used to initialize the class and set the correct value to different buttons and labels
	 */
	@FXML
	private void initialize() {
		btnStartGame.setDisable(false);
		btnExit.setDisable(true);
		btnBust.setDisable(true);

		iwCardsArray = new ImageView[10];
		iwCardsArray[0] = iwPlayer1Card1;
		iwCardsArray[1] = iwPlayer1Card2;
		iwCardsArray[2] = iwPlayer2Card1;
		iwCardsArray[3] = iwPlayer2Card2;
		iwCardsArray[4] = iwPlayer3Card1;
		iwCardsArray[5] = iwPlayer3Card2;
		iwCardsArray[6] = iwPlayer4Card1;
		iwCardsArray[7] = iwPlayer4Card2;
		iwCardsArray[8] = iwPlayer5Card1;
		iwCardsArray[9] = iwPlayer5Card2;

		lblPlayersArray = new Label[5];
		lblPlayersArray[0] = lblPlayer1;
		lblPlayersArray[1] = lblPlayer2;
		lblPlayersArray[2] = lblPlayer3;
		lblPlayersArray[3] = lblPlayer4;
		lblPlayersArray[4] = lblPlayer5;
	}

	public void setDealerCardValue(int value) {
		lblDealerCardSum.setText("" + value);
	}

	public void setRounds(int rounds) {
		this.rounds = rounds;
		lblRounds.setText("Rounds: " + rounds);
	}

	public void setTime(int min, int sec) {
		lblTime.setText("Time: " + min + ":" + sec);
	}

	public void setCheatHeat(int valueInPercent) {
		cheatHeatProgressBar.setProgress(valueInPercent);
		lblCheatHeatPercent.setText(valueInPercent + "%");
	}

	/**
	 * Updates the vale of the current balance
	 */
	private void updateBalance() {
		gameControllerBalance -= currentBet;
		doubleBet = currentBet;
	}

	public void setTableId(int tableId) {
		lblTableId.setText("Table ID: " + tableId);
	}
	
	public void setFirstRoundBalance(int balance) {
		if(firstRoundBalance) {
		gameControllerBalance = balance;
		firstRoundBalance = false;
		}
	}

	/**
	 * Used by the client to update labels with the info from the server
	 * @param rounds - Number of rounds
	 * @param minutes - Minutes to play
	 * @param minimumBet - The lowest bet used in the game
	 * @param balance - All players balance in the curret game
	 */
	public void setStartingInformation(int rounds, int minutes, int minimumBet, int balance) {
		setRounds(rounds);
		this.balance = balance;
		setFirstRoundBalance(balance);
		lblTime.setText("Timer: " + minutes);
		this.minimumBet = minimumBet;
		lblMinimumBet.setText("Minimum bet: " + minimumBet);

	}

	/**
	 * When the Start Game button is pressed. Checks that there is a correct number of players
	 * If there is a correct number of players the round starts, otherwise the player gets an error message
	 * @throws IOException
	 */
	@FXML
	private void handleStartGame() throws IOException {
		if(numberOfPlayers < 2) {
			try {
				mainApp.showAlert("Alert", "You need at least two players to start the game");
			} catch (IOException e) {
				e.printStackTrace();
			}
			btnStartGame.setDisable(true);
		}else {
			client.sendStartGame(new StartGameRequest(1));
		}
	}
	
	/**
	 * When the exit game button is pressed. Player is removed from the table and returnd to the main menu
	 * @throws IOException
	 */
	@FXML
	private void handleExit() throws IOException {
		//Code for exiting game
		mainApp.showMainMenu();
	}

	@FXML
	private void clearCurrentBet() {
		currentBet = 0;
		btnConfirmBet.setText("Confirm: " + currentBet);
	}

	/**
	 * Checks that the entered amount is correct and sends it to the server. 
	 * If amount is to high or to low the user get an error message
	 */
	@FXML
	private void confirmBet() {
		if(currentBet < minimumBet) {
			try {
				mainApp.showAlert("Bet to low", "Minimum bet for this table is " + minimumBet);
			} catch (IOException e) {
				e.printStackTrace();
			}
//		} else if(currentBet > balance) {
//			try {
//				mainApp.showAlert("Bet to high", "Your current balance is " + balance);
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
		} else {
			btnConfirmBet.setText("Waiting...");
			btnConfirmBet.setDisable(true);
			PlayerChoice choice = new PlayerChoice(4);
			choice.setBet(currentBet); 
			updateBalance();
			client.sendPlayerChoice(choice);
			currentBet = 0;
		}
	}

	@FXML
	private void setNextBet25() {
		currentBet += 25;
		btnConfirmBet.setText("Confirm: " + currentBet);
	}

	@FXML
	private void setNextBet50() {
		currentBet += 50;
		btnConfirmBet.setText("Confirm: " + currentBet);
	}

	@FXML
	private void setNextBet100() {
		currentBet += 100;
		btnConfirmBet.setText("Confirm: " + currentBet);
	}

	@FXML
	private void setNextBet500() {
		currentBet += 500;
		btnConfirmBet.setText("Confirm: " + currentBet);
	}

	/**
	 * Sends the players cheatchoice to the server and activates buttons to continue playing 
	 */
	@FXML 
	private void handleCheat() {
//		numberOfCheats++;
	//	setCheatHeat(cheatHeat);
		PlayerChoice choice = new PlayerChoice(5);
		choice.setCheatChoice(true); //Tells server that we will cheat 

		btnIncreaseBet25.setDisable(false);
		btnIncreaseBet50.setDisable(false);
		btnIncreaseBet100.setDisable(false);
		btnIncreaseBet500.setDisable(false);
		btnClearBet.setDisable(false);

		btnConfirmBet.setText("Confirm: ");
		btnConfirmBet.setDisable(false);
		client.sendPlayerChoice(choice);
		System.out.println("[GAME_CONTORLLER] == Du har tryck CHEAT.");
	}

	/**
	 * Sends the players cheatchoice to the server and activates buttons to continue playing 
	 */
	@FXML
	private void handleDoNotCheat() {
//		setCheatHeat(cheatHeat);
		PlayerChoice choice = new PlayerChoice(5);
		choice.setCheatChoice(false);//Tells server that we will NOT cheat 

		btnIncreaseBet25.setDisable(false);
		btnIncreaseBet50.setDisable(false);
		btnIncreaseBet100.setDisable(false);
		btnIncreaseBet500.setDisable(false);
		btnClearBet.setDisable(false);

		btnConfirmBet.setText("Confirm: ");
		btnConfirmBet.setDisable(false);
		client.sendPlayerChoice(choice);
		System.out.println("[GAME_CONTORLLER] == Du har tryck DO NOT CHEAT.");
	}

	@FXML 
	private void handleBust() {
		System.out.println("[GAME_CONTORLLER] == Du har tryck BUST.");
	}

	@FXML 
	private void handleSplit() {
		PlayerChoice playerChoice = new PlayerChoice(6);
		playerChoice.setSplitChoice(true);
		client.sendPlayerChoice(playerChoice);
		System.out.println("[GAME_CONTORLLER] == Du har tryck SPLIT.");
	}

	/**
	 * Check that payer bet is above the lowest given bet and under the player balance
	 * If a correct value is entered, bet is sent to sever 
	 */
	@FXML 
	private void handleDouble() {
//		if(doubleBet > gameControllerBalance) {
//			try {
//				mainApp.showAlert("Bet to high", "Your current balance is " + balance);
//			} catch (IOException e) {
//				e.printStackTrace();
//			}			
//		}else {
			PlayerChoice playerChoice = new PlayerChoice(3);
			client.sendPlayerChoice(playerChoice);
			System.out.println("[GAME_CONTORLLER] == Du har tryck DOUBLE.");
	//	}
	}

	@FXML 
	private void handleStay() {
		PlayerChoice playerChoice = new PlayerChoice(2);
		client.sendPlayerChoice(playerChoice);
		System.out.println("[GAME_CONTORLLER] == Du har tryck STAY.");
	}

	@FXML 
	private void handleHit() {
		PlayerChoice playerChoice = new PlayerChoice(1);
		client.sendPlayerChoice(playerChoice);
		System.out.println("[GAME_CONTORLLER] == Du har tryck HIT.");
	}

	/**
	 * Updates the labels with player names based on number of players. 
	 * @param playerList - List of all active players
	 */
	public void updatePlayerList(ArrayList<Player> playerList) {
		System.out.println("[GAME_CONTROLLER] == GameController har mottagit lista. Antal = " + playerList.size());
		numberOfPlayers = playerList.size();
		setPlayersLabel(1, playerList);

		if(numberOfPlayers == 1) 
			setPlayersLabel(1, playerList);
		if(numberOfPlayers == 2) 
			setPlayersLabel(2, playerList);
		if(numberOfPlayers == 3) 
			setPlayersLabel(3, playerList);
		if(numberOfPlayers == 4) 
			setPlayersLabel(4, playerList);
		if(numberOfPlayers == 5) 
			setPlayersLabel(5, playerList);
	}

	/**
	 * Loops through the number of players and sets correct name to correct label
	 * @param numberOfPlayers
	 * @param playerList
	 */
	private void setPlayersLabel(int numberOfPlayers, ArrayList<Player> playerList) {
		for(int i = 0; i < numberOfPlayers; i++) {
			lblPlayersArray[i].setText(playerList.get(i).getUsername());
		}
	}

	/**
	 * Updates centered label with text about whats happening in the game
	 * @param message
	 */
	public void updateRoundMessage(String message) {
		lblUpdate.setText(message);
	}

	/**
	 * Updates player UI with the lates info, so all labels are up to date with what other players are doing 
	 * @param playerList - List of all players at the table
	 * @param dealer - Info about the dealer and dealers cards
	 */
	public void updateRoundInformation(ArrayList<Player> playerList, DealerHand dealer) {
		System.out.println("[GAME_CONTROLLER] == GameController har mottagit updateRoundInformation");
		
		if(firstRound) {
			btnCheat.setDisable(false);
			btnDoNotCheat.setDisable(false);
			btnStartGame.setDisable(true);
			firstRound = false;
			System.out.println("[GAME_CONTROLLER] == FirstRounds initiated");
		}

		if(numberOfPlayers == 2) {
			updateRound2Players(playerList, dealer);
		}
		if(numberOfPlayers == 3) {
			updateRound3Players(playerList, dealer);
		}
		if(numberOfPlayers == 4) {
			updateRound4Players(playerList, dealer);
		}
		if(numberOfPlayers == 5) {	
			updateRound5Players(playerList, dealer);
		}
	}
	
	/**
	 * Update if there is 2 players
	 * @param playerList - Info from active players 
	 * @param dealer - Info from dealer
	 */
	public void updateRound2Players(ArrayList<Player> playerList, DealerHand dealer) {
		lblPlayer1Balance.setText("Balance: " + playerList.get(0).getBalance());
		lblPlayer1Bet.setText("Bet: " + playerList.get(0).getBet());

		int hands = playerList.get(0).getNumberOfHands();
		int i = 0;
		if(hands == 1) {
			lblPlayer1CardSum.setText("" + playerList.get(0).getHand(i).getCardValue() + playerList.get(0).getHand(i).getCurrentScore());
		}else if(hands == 2) {
			if(playerList.get(0).getHand(i).getDisplayValue()) {
				lblPlayer1CardSum.setText("" + playerList.get(0).getHand(i).getCardValue() + playerList.get(0).getHand(i).getCurrentScore());
			}else if(playerList.get(0).getHand(i+1).getDisplayValue()) {
				lblPlayer1CardSum.setText("" + playerList.get(0).getHand(i + 1).getCardValue() + playerList.get(0).getHand(i + 1).getCurrentScore());
			}
		}
		System.out.println(playerList.get(0).getUsername() + ", SUMMA= " + playerList.get(0).getBalance() + ", BET= " + playerList.get(0).getBet());

		lblPlayer2Balance.setText("Balance: " + playerList.get(1).getBalance());
		lblPlayer2Bet.setText("Bet: " + playerList.get(1).getBet());
		
		hands = playerList.get(1).getNumberOfHands();
		i = 0;
		if(hands == 1) {
			lblPlayer2CardSum.setText("" + playerList.get(1).getHand(i).getCardValue() + playerList.get(1).getHand(i).getCurrentScore());
		}else if(hands == 2) {
			if(playerList.get(1).getHand(i).getDisplayValue()) {
				lblPlayer2CardSum.setText("" + playerList.get(1).getHand(i).getCardValue() + playerList.get(1).getHand(i).getCurrentScore());
			}else if(playerList.get(1).getHand(i+1).getDisplayValue()) {
				lblPlayer2CardSum.setText("" + playerList.get(1).getHand(i + 1).getCardValue() + playerList.get(1).getHand(i + 1).getCurrentScore());
			}
		}
		System.out.println(playerList.get(1).getUsername() + ", SUMMA= " + playerList.get(1).getBalance() + ", BET= " + playerList.get(1).getBet());
		lblDealerCardSum.setText("" + dealer.getValue());
	}
	
	/**
	 * Update if there is 3 players
	 * @param playerList - Info from active players 
	 * @param dealer - Info from dealer
	 */
	public void updateRound3Players(ArrayList<Player> playerList, DealerHand dealer) {
		lblPlayer1Balance.setText("Balance: " + playerList.get(0).getBalance());
		lblPlayer1Bet.setText("Bet: " + playerList.get(0).getBet());
		
		int hands = playerList.get(0).getNumberOfHands();
		int i = 0;
		if(hands == 1) {
			lblPlayer1CardSum.setText("" + playerList.get(0).getHand(i).getCardValue() + playerList.get(0).getHand(i).getCurrentScore());
		}else if(hands == 2) {
			if(playerList.get(0).getHand(i).getDisplayValue()) {
				lblPlayer1CardSum.setText("" + playerList.get(0).getHand(i).getCardValue() + playerList.get(0).getHand(i).getCurrentScore());
			}else if(playerList.get(0).getHand(i+1).getDisplayValue()) {
				lblPlayer1CardSum.setText("" + playerList.get(0).getHand(i + 1).getCardValue() + playerList.get(0).getHand(i + 1).getCurrentScore());
			}
		}

		lblPlayer2Balance.setText("Balance: " + playerList.get(1).getBalance());
		lblPlayer2Bet.setText("Bet: " + playerList.get(1).getBet());
		
		hands = playerList.get(1).getNumberOfHands();
		i = 0;
		if(hands == 1) {
			lblPlayer2CardSum.setText("" + playerList.get(1).getHand(i).getCardValue() + playerList.get(1).getHand(i).getCurrentScore());
		}else if(hands == 2) {
			if(playerList.get(1).getHand(i).getDisplayValue()) {
				lblPlayer2CardSum.setText("" + playerList.get(1).getHand(i).getCardValue() + playerList.get(1).getHand(i).getCurrentScore());
			}else if(playerList.get(1).getHand(i+1).getDisplayValue()) {
				lblPlayer2CardSum.setText("" + playerList.get(1).getHand(i + 1).getCardValue() + playerList.get(1).getHand(i + 1).getCurrentScore());
			}
		}

		lblPlayer3Balance.setText("Balance: " + playerList.get(2).getBalance());
		lblPlayer3Bet.setText("Bet: " + playerList.get(2).getBet());

		hands = playerList.get(2).getNumberOfHands();
		i = 0;
		if(hands == 1) {
			lblPlayer3CardSum.setText("" + playerList.get(2).getHand(i).getCardValue() + playerList.get(2).getHand(i).getCurrentScore());
		}else if(hands == 2) {
			if(playerList.get(2).getHand(i).getDisplayValue()) {
				lblPlayer3CardSum.setText("" + playerList.get(2).getHand(i).getCardValue() + playerList.get(2).getHand(i).getCurrentScore());
			}else if(playerList.get(2).getHand(i+1).getDisplayValue()) {
				lblPlayer3CardSum.setText("" + playerList.get(2).getHand(i + 1).getCardValue() + playerList.get(2).getHand(i + 1).getCurrentScore());
			}
		}
		lblDealerCardSum.setText("" + dealer.getValue());
	}
	
	/**
	 * Update if there is 4 players
	 * @param playerList - Info from active players 
	 * @param dealer - Info from dealer
	 */
	public void updateRound4Players(ArrayList<Player> playerList, DealerHand dealer) {
		lblPlayer1Balance.setText("Balance: " + playerList.get(0).getBalance());
		lblPlayer1Bet.setText("Bet: " + playerList.get(0).getBet());

		int hands = playerList.get(0).getNumberOfHands();
		int i = 0;
		if(hands == 1) {
			lblPlayer1CardSum.setText("" + playerList.get(0).getHand(i).getCardValue() + playerList.get(0).getHand(i).getCurrentScore());
		}else if(hands == 2) {
			if(playerList.get(0).getHand(i).getDisplayValue()) {
				lblPlayer1CardSum.setText("" + playerList.get(0).getHand(i).getCardValue() + playerList.get(0).getHand(i).getCurrentScore());
			}else if(playerList.get(0).getHand(i+1).getDisplayValue()) {
				lblPlayer1CardSum.setText("" + playerList.get(0).getHand(i + 1).getCardValue() + playerList.get(0).getHand(i + 1).getCurrentScore());
			}
		}

		lblPlayer2Balance.setText("Balance: " + playerList.get(1).getBalance());
		lblPlayer2Bet.setText("Bet: " + playerList.get(1).getBet());

		hands = playerList.get(1).getNumberOfHands();
		i = 0;
		if(hands == 1) {
			lblPlayer2CardSum.setText("" + playerList.get(1).getHand(i).getCardValue() + playerList.get(1).getHand(i).getCurrentScore());
		}else if(hands == 2) {
			if(playerList.get(1).getHand(i).getDisplayValue()) {
				lblPlayer2CardSum.setText("" + playerList.get(1).getHand(i).getCardValue() + playerList.get(1).getHand(i).getCurrentScore());
			}else if(playerList.get(1).getHand(i+1).getDisplayValue()) {
				lblPlayer2CardSum.setText("" + playerList.get(1).getHand(i + 1).getCardValue() + playerList.get(1).getHand(i + 1).getCurrentScore());
			}
		}

		lblPlayer3Balance.setText("Balance: " + playerList.get(2).getBalance());
		lblPlayer3Bet.setText("Bet: " + playerList.get(2).getBet());

		hands = playerList.get(2).getNumberOfHands();
		i = 0;
		if(hands == 1) {
			lblPlayer3CardSum.setText("" + playerList.get(2).getHand(i).getCardValue() + playerList.get(2).getHand(i).getCurrentScore());
		}else if(hands == 2) {
			if(playerList.get(2).getHand(i).getDisplayValue()) {
				lblPlayer3CardSum.setText("" + playerList.get(2).getHand(i).getCardValue() + playerList.get(2).getHand(i).getCurrentScore());
			}else if(playerList.get(2).getHand(i+1).getDisplayValue()) {
				lblPlayer3CardSum.setText("" + playerList.get(2).getHand(i + 1).getCardValue() + playerList.get(2).getHand(i + 1).getCurrentScore());
			}
		}

		lblPlayer4Balance.setText("Balance: " + playerList.get(3).getBalance());
		lblPlayer4Bet.setText("Bet: " + playerList.get(3).getBet());

		hands = playerList.get(3).getNumberOfHands();
		i = 0;
		if(hands == 1) {
			lblPlayer4CardSum.setText("" + playerList.get(3).getHand(i).getCardValue() + playerList.get(3).getHand(i).getCurrentScore());
		}else if(hands == 2) {
			if(playerList.get(3).getHand(i).getDisplayValue()) {
				lblPlayer4CardSum.setText("" + playerList.get(3).getHand(i).getCardValue() + playerList.get(3).getHand(i).getCurrentScore());
			}else if(playerList.get(3).getHand(i+1).getDisplayValue()) {
				lblPlayer4CardSum.setText("" + playerList.get(3).getHand(i + 1).getCardValue() + playerList.get(3).getHand(i + 1).getCurrentScore());
			}
		}
		lblDealerCardSum.setText("" + dealer.getValue());
	}
	
	/**
	 * Update if there is 5 players
	 * @param playerList - Info from active players 
	 * @param dealer - Info from dealer
	 */
	public void updateRound5Players(ArrayList<Player> playerList, DealerHand dealer) {
		lblPlayer1Balance.setText("Balance: " + playerList.get(0).getBalance());
		lblPlayer1Bet.setText("Bet: " + playerList.get(0).getBet());

		int hands = playerList.get(0).getNumberOfHands();
		int i = 0;
		if(hands == 1) {
			lblPlayer1CardSum.setText("" + playerList.get(0).getHand(i).getCardValue() + playerList.get(0).getHand(i).getCurrentScore());
		}else if(hands == 2) {
			if(playerList.get(0).getHand(i).getDisplayValue()) {
				lblPlayer1CardSum.setText("" + playerList.get(0).getHand(i).getCardValue() + playerList.get(0).getHand(i).getCurrentScore());
			}else if(playerList.get(0).getHand(i+1).getDisplayValue()) {
				lblPlayer1CardSum.setText("" + playerList.get(0).getHand(i + 1).getCardValue() + playerList.get(0).getHand(i + 1).getCurrentScore());
			}
		}

		lblPlayer2Balance.setText("Balance: " + playerList.get(1).getBalance());
		lblPlayer2Bet.setText("Bet: " + playerList.get(1).getBet());

		hands = playerList.get(1).getNumberOfHands();
		i = 0;
		if(hands == 1) {
			lblPlayer2CardSum.setText("" + playerList.get(1).getHand(i).getCardValue() + playerList.get(1).getHand(i).getCurrentScore());
		}else if(hands == 2) {
			if(playerList.get(1).getHand(i).getDisplayValue()) {
				lblPlayer2CardSum.setText("" + playerList.get(1).getHand(i).getCardValue() + playerList.get(1).getHand(i).getCurrentScore());
			}else if(playerList.get(1).getHand(i+1).getDisplayValue()) {
				lblPlayer2CardSum.setText("" + playerList.get(1).getHand(i + 1).getCardValue() + playerList.get(1).getHand(i + 1).getCurrentScore());
			}
		}

		lblPlayer3Balance.setText("Balance: " + playerList.get(2).getBalance());
		lblPlayer3Bet.setText("Bet: " + playerList.get(2).getBet());

		hands = playerList.get(2).getNumberOfHands();
		i = 0;
		if(hands == 1) {
			lblPlayer3CardSum.setText("" + playerList.get(2).getHand(i).getCardValue() + playerList.get(2).getHand(i).getCurrentScore());
		}else if(hands == 2) {
			if(playerList.get(2).getHand(i).getDisplayValue()) {
				lblPlayer3CardSum.setText("" + playerList.get(2).getHand(i).getCardValue() + playerList.get(2).getHand(i).getCurrentScore());
			}else if(playerList.get(2).getHand(i+1).getDisplayValue()) {
				lblPlayer3CardSum.setText("" + playerList.get(2).getHand(i + 1).getCardValue() + playerList.get(2).getHand(i + 1).getCurrentScore());
			}
		}

		lblPlayer4Balance.setText("Balance: " + playerList.get(3).getBalance());
		lblPlayer4Bet.setText("Bet: " + playerList.get(3).getBet());

		hands = playerList.get(3).getNumberOfHands();
		i = 0;
		if(hands == 1) {
			lblPlayer4CardSum.setText("" + playerList.get(3).getHand(i).getCardValue() + playerList.get(3).getHand(i).getCurrentScore());
		}else if(hands == 2) {
			if(playerList.get(3).getHand(i).getDisplayValue()) {
				lblPlayer4CardSum.setText("" + playerList.get(3).getHand(i).getCardValue() + playerList.get(3).getHand(i).getCurrentScore());
			}else if(playerList.get(3).getHand(i+1).getDisplayValue()) {
				lblPlayer4CardSum.setText("" + playerList.get(3).getHand(i + 1).getCardValue() + playerList.get(3).getHand(i + 1).getCurrentScore());
			}
		}
		
		lblPlayer5Balance.setText("Balance: " + playerList.get(4).getBalance());
		lblPlayer5Bet.setText("Bet: " + playerList.get(4).getBet());

		hands = playerList.get(4).getNumberOfHands();
		i = 0;
		if(hands == 1) {
			lblPlayer5CardSum.setText("" + playerList.get(4).getHand(i).getCardValue() + playerList.get(4).getHand(i).getCurrentScore());
		}else if(hands == 2) {
			if(playerList.get(4).getHand(i).getDisplayValue()) {
				lblPlayer5CardSum.setText("" + playerList.get(4).getHand(i).getCardValue() + playerList.get(4).getHand(i).getCurrentScore());
			}else if(playerList.get(4).getHand(i+1).getDisplayValue()) {
				lblPlayer5CardSum.setText("" + playerList.get(4).getHand(i + 1).getCardValue() + playerList.get(4).getHand(i + 1).getCurrentScore());
			}
		}
		lblDealerCardSum.setText("" + dealer.getValue());
	}

	/**
	 * Updated all players in beginning of round
	 * @param player
	 * @param balance
	 * @param currentBet
	 * @param cardValue
	 * @param cheatHeat
	 */
	public void updatePlayerInfo(int player, int balance, int currentBet, int cardValue, int cheatHeat) {
		switch(player) {
		case 1:
			lblPlayer1Balance.setText("Balance: " + balance);
			lblPlayer1Bet.setText("Bet: " + currentBet);
			lblPlayer1CardSum.setText("" + cardValue);
			cheatHeatProgressBar.setProgress((double) cheatHeat); //Only for player 1
			break;
		case 2:
			lblPlayer2Balance.setText("Balance: " + balance);
			lblPlayer2Bet.setText("Bet: " + currentBet);
			lblPlayer2CardSum.setText("" + cardValue);
			break;
		case 3:
			lblPlayer3Balance.setText("Balance: " + balance);
			lblPlayer3Bet.setText("Bet: " + currentBet);
			lblPlayer3CardSum.setText("" + cardValue);
			break;
		case 4:
			lblPlayer4Balance.setText("Balance: " + balance);
			lblPlayer4Bet.setText("Bet: " + currentBet);
			lblPlayer4CardSum.setText("" + cardValue);
			break;
		case 5:
			lblPlayer5Balance.setText("Balance: " + balance);
			lblPlayer5Bet.setText("Bet: " + currentBet);
			lblPlayer5CardSum.setText("" + cardValue);
			break;
		}
	}
	
	/**
	 * Shows a new scen when the game is over
	 * @throws IOException
	 */
	public void showGameOver() throws IOException {
		mainApp.showGameOver();
	}

	/**
	 * connects the main class to the user interface
	 * @param main
	 */
	public void setMain(Main main) {
		mainApp = main;
	}

	/**
	 * sets the client to the user interface
	 * @param client
	 */
	public void setClient(UserClient client) {
		this.client = client;
	}

	/**
	 * Used to see if the player can choose split or not 
	 * @param bool
	 */
	public void ableToSplit(boolean bool) {
		if(bool) {
			btnSplit.setDisable(true);
		}else {
			btnSplit.setDisable(false);
		}	
	}
}

