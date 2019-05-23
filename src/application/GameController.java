package application;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.UserClient;
import communications.PlayerChoice;
import communications.StartGameRequest;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import resources.DealerHand;
import resources.Player;

public class GameController{
	@FXML
	private Label lblPlayer1;
	@FXML
	private Label lblPlayer2;
	@FXML
	private Label lblPlayer3;
	@FXML
	private Label lblPlayer4;
	@FXML
	private Label lblPlayer5;

	private Label[] lblPlayersArray;

	@FXML
	private Label lblPlayer1CardSum;
	@FXML
	private Label lblPlayer2CardSum;
	@FXML
	private Label lblPlayer3CardSum;
	@FXML
	private Label lblPlayer4CardSum;
	@FXML
	private Label lblPlayer5CardSum;

	@FXML
	private Label lblPlayer1Balance;
	@FXML
	private Label lblPlayer2Balance;
	@FXML
	private Label lblPlayer3Balance;
	@FXML
	private Label lblPlayer4Balance;
	@FXML
	private Label lblPlayer5Balance;

	@FXML
	private Label lblPlayer1Bet;
	@FXML
	private Label lblPlayer2Bet;
	@FXML
	private Label lblPlayer3Bet;
	@FXML
	private Label lblPlayer4Bet;
	@FXML
	private Label lblPlayer5Bet;

	@FXML
	private Label lblDealer;
	@FXML
	private Label lblDealerCardSum;
	@FXML
	private Label lblUpdate;

	@FXML
	private ImageView iwPlayer1Card1;
	@FXML
	private ImageView iwPlayer1Card2;
	@FXML
	private ImageView iwPlayer2Card1;
	@FXML
	private ImageView iwPlayer2Card2;
	@FXML
	private ImageView iwPlayer3Card1;
	@FXML
	private ImageView iwPlayer3Card2;
	@FXML
	private ImageView iwPlayer4Card1;
	@FXML
	private ImageView iwPlayer4Card2;
	@FXML
	private ImageView iwPlayer5Card1;
	@FXML
	private ImageView iwPlayer5Card2;

	private ImageView[] iwCardsArray;

	@FXML
	private Label lblTime;
	@FXML
	private Label lblRounds;
	@FXML
	private Label lblTableId;
	@FXML
	private Label lblMinimumBet;

	@FXML
	private ProgressBar cheatHeatProgressBar;
	@FXML
	private Label lblCheatHeatPersent;

	@FXML
	private Button btnCheat;
	@FXML
	private Button btnDoNotCheat;
	@FXML
	private Button btnBust;

	@FXML
	private Button btnSplit;
	@FXML
	private Button btnDouble;
	@FXML
	private Button btnStay;
	@FXML
	private Button btnHit;

	@FXML
	private Button btnIncreaseBet25;
	@FXML
	private Button btnIncreaseBet50;
	@FXML
	private Button btnIncreaseBet100;
	@FXML
	private Button btnIncreaseBet500;
	@FXML
	private Button btnConfirmBet;
	@FXML
	private Button btnClearBet;

	@FXML
	private Button btnStartGame;
	@FXML
	private Button btnExit;

	private Main mainApp;
	private UserClient client;

	private boolean firstRound = true;

	private int balance = 0;
	private int minimumBet = 0;
	private int currentBet = 0;
	private int time = 0;

	private int numberOfPlayers = 0;

	private int rounds = 0;
	private int currentRound = 1;
	private int numberOfCheats = 0;
	private int cheatHeat = numberOfCheats / currentRound;
	private int counter = 0;
	private int doubleBet = 0;

	@FXML
	private void initialize() {
		//		disableAllButtons(true);
		btnStartGame.setDisable(false);

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

	//	public void setStartingBalance(int balance) {
	//		this.balance = balance;
	//	}

	public void setRounds(int rounds) {
		this.rounds = rounds;
		lblRounds.setText("Rounds: " + rounds);
	}

	//Never used
	public void updateRounds() {
		lblRounds.setText("Rounds: " + (rounds--));
		currentRound ++;
		setCheatHeat(cheatHeat);
	}

	public void setTime(int min, int sec) {
		lblTime.setText("Time: " + min + ":" + sec);
	}

	public void setCheatHeat(int valueInPercent) {
		cheatHeatProgressBar.setProgress(valueInPercent);
		lblCheatHeatPersent.setText(valueInPercent + "%");
	}

	private void updateBalance() {
		balance -= currentBet;
		doubleBet = currentBet;
		//			lblPlayer1Balance.setText("Balance: " + balance);
	}

	public void setTableId(int tableId) {
		lblTableId.setText("Table ID: " + tableId);
	}

	public void setStartingInformation(int rounds, int minutes, int minimumBet, int balance) {
		setRounds(rounds);
		this.balance = balance;
		lblTime.setText("Timer: " + minutes);
		this.minimumBet = minimumBet;
		lblMinimumBet.setText("Minimum bet: " + minimumBet);

	}

	//true will disable, false will enable
	public void disableAllButtons(boolean bool) { 
		btnCheat.setDisable(bool);
		btnDoNotCheat.setDisable(bool);
		btnBust.setDisable(bool);
		btnSplit.setDisable(bool);
		btnDouble.setDisable(bool);
		btnStay.setDisable(bool);
		btnHit.setDisable(bool);
		btnIncreaseBet25.setDisable(bool);
		btnIncreaseBet50.setDisable(bool);
		btnIncreaseBet100.setDisable(bool);
		btnIncreaseBet500.setDisable(bool);
		btnConfirmBet.setDisable(bool);
		btnClearBet.setDisable(bool);
		btnStartGame.setDisable(bool);
		btnExit.setDisable(bool);
	}

	@FXML
	private void handleStartGame() throws IOException {
		if(numberOfPlayers < 2) {
			try {
				mainApp.showAlert("Alert", "You need atleast two players to start the game");
			} catch (IOException e) {
				e.printStackTrace();
			}
			btnStartGame.setDisable(true);
		}else {
			client.sendStartGame(new StartGameRequest(1));
		}
	}
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

	@FXML
	private void confirmBet() {
		if(currentBet < minimumBet) {
			try {
				mainApp.showAlert("Bet to low", "Minimum bet for this table is " + minimumBet);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if(currentBet > balance) {
			try {
				mainApp.showAlert("Bet to high", "Your current balance is " + balance);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			btnConfirmBet.setText("Waiting...");
			btnConfirmBet.setDisable(true);
			//		btnCheat.setDisable(true);
			//		btnDoNotCheat.setDisable(true);
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

	@FXML 
	private void handleCheat() {
		numberOfCheats++;
		setCheatHeat(cheatHeat);
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

	@FXML
	private void handleDoNotCheat() {
		setCheatHeat(cheatHeat);
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

	@FXML 
	private void handleDouble() {
		if(doubleBet > balance) {
			try {
				mainApp.showAlert("Bet to high", "Your current balance is " + balance);
			} catch (IOException e) {
				e.printStackTrace();
			}			
		}else {
			PlayerChoice playerChoice = new PlayerChoice(3);
			client.sendPlayerChoice(playerChoice);
			System.out.println("[GAME_CONTORLLER] == Du har tryck DOUBLE.");
		}
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

	private void setPlayersLabel(int numberOfPlayers, ArrayList<Player> playerList) {
		for(int i = 0; i < numberOfPlayers; i++) {
			lblPlayersArray[i].setText(playerList.get(i).getUsername());
		}
	}

	public void updateRoundMessage(String message) {
		lblUpdate.setText(message);
	}

	public void updateRoundInformation(ArrayList<Player> playerList, DealerHand dealer) {
		System.out.println("[GAME_CONTROLLER] == GameController har mottagit updateRoundInformation");

		//	this.balance = playerList.get(0).getBalance();
		//	System.out.println("[GAME_CONTROLLER] == Balance = " + balance);

		if(firstRound) {
			btnCheat.setDisable(false);
			btnDoNotCheat.setDisable(false);
			btnStartGame.setDisable(true);
			firstRound = false;
			System.out.println("[GAME_CONTROLLER] == FirstRounds initiated");
		}

		if(numberOfPlayers == 2) {
			lblPlayer1Balance.setText("Balance: " + playerList.get(0).getBalance());
			lblPlayer1Bet.setText("Bet: " + playerList.get(0).getBet());


			int hands = playerList.get(0).getNumberOfHands();
			int i = 0;
			if(hands == 1) {
				lblPlayer1CardSum.setText("" + playerList.get(0).getHand(i).getCurrentScore());
			}else if(hands == 2) {
				if(playerList.get(0).getHand(i).getDisplayValue()) {
					lblPlayer1CardSum.setText("" + playerList.get(0).getHand(i).getCurrentScore());
				}else if(playerList.get(0).getHand(i+1).getDisplayValue()) {
					lblPlayer1CardSum.setText("" + playerList.get(0).getHand(i + 1).getCurrentScore());
				}
			}
			System.out.println(playerList.get(0).getUsername() + ", SUMMA= " + playerList.get(0).getBalance() + ", BET= " + playerList.get(0).getBet());

			lblPlayer2Balance.setText("Balance: " + playerList.get(1).getBalance());
			lblPlayer2Bet.setText("Bet: " + playerList.get(1).getBet());
			
			hands = playerList.get(1).getNumberOfHands();
			i = 0;
			if(hands == 1) {
				lblPlayer2CardSum.setText("" + playerList.get(1).getHand(i).getCurrentScore());
			}else if(hands == 2) {
				if(playerList.get(1).getHand(i).getDisplayValue()) {
					lblPlayer2CardSum.setText("" + playerList.get(1).getHand(i).getCurrentScore());
				}else if(playerList.get(1).getHand(i+1).getDisplayValue()) {
					lblPlayer2CardSum.setText("" + playerList.get(1).getHand(i + 1).getCurrentScore());
				}
			}
			System.out.println(playerList.get(1).getUsername() + ", SUMMA= " + playerList.get(1).getBalance() + ", BET= " + playerList.get(1).getBet());

			lblDealerCardSum.setText("" + dealer.getValue());

		}
		if(numberOfPlayers == 3) {
			lblPlayer1Balance.setText("Balance: " + playerList.get(0).getBalance());
			lblPlayer1Bet.setText("Bet: " + playerList.get(0).getBet());
			
			int hands = playerList.get(0).getNumberOfHands();
			int i = 0;
			if(hands == 1) {
				lblPlayer1CardSum.setText("" + playerList.get(0).getHand(i).getCurrentScore());
			}else if(hands == 2) {
				if(playerList.get(0).getHand(i).getDisplayValue()) {
					lblPlayer1CardSum.setText("" + playerList.get(0).getHand(i).getCurrentScore());
				}else if(playerList.get(0).getHand(i+1).getDisplayValue()) {
					lblPlayer1CardSum.setText("" + playerList.get(0).getHand(i + 1).getCurrentScore());
				}
			}

			lblPlayer2Balance.setText("Balance: " + playerList.get(1).getBalance());
			lblPlayer2Bet.setText("Bet: " + playerList.get(1).getBet());
			
			hands = playerList.get(1).getNumberOfHands();
			i = 0;
			if(hands == 1) {
				lblPlayer2CardSum.setText("" + playerList.get(1).getHand(i).getCurrentScore());
			}else if(hands == 2) {
				if(playerList.get(1).getHand(i).getDisplayValue()) {
					lblPlayer2CardSum.setText("" + playerList.get(1).getHand(i).getCurrentScore());
				}else if(playerList.get(1).getHand(i+1).getDisplayValue()) {
					lblPlayer2CardSum.setText("" + playerList.get(1).getHand(i + 1).getCurrentScore());
				}
			}

			lblPlayer3Balance.setText("Balance: " + playerList.get(2).getBalance());
			lblPlayer3Bet.setText("Bet: " + playerList.get(2).getBet());

			hands = playerList.get(2).getNumberOfHands();
			i = 0;
			if(hands == 1) {
				lblPlayer3CardSum.setText("" + playerList.get(2).getHand(i).getCurrentScore());
			}else if(hands == 2) {
				if(playerList.get(2).getHand(i).getDisplayValue()) {
					lblPlayer3CardSum.setText("" + playerList.get(2).getHand(i).getCurrentScore());
				}else if(playerList.get(2).getHand(i+1).getDisplayValue()) {
					lblPlayer3CardSum.setText("" + playerList.get(2).getHand(i + 1).getCurrentScore());
				}
			}

			lblDealerCardSum.setText("" + dealer.getValue());
		}
		if(numberOfPlayers == 4) {
			lblPlayer1Balance.setText("Balance: " + playerList.get(0).getBalance());
			lblPlayer1Bet.setText("Bet: " + playerList.get(0).getBet());

			int hands = playerList.get(0).getNumberOfHands();
			int i = 0;
			if(hands == 1) {
				lblPlayer1CardSum.setText("" + playerList.get(0).getHand(i).getCurrentScore());
			}else if(hands == 2) {
				if(playerList.get(0).getHand(i).getDisplayValue()) {
					lblPlayer1CardSum.setText("" + playerList.get(0).getHand(i).getCurrentScore());
				}else if(playerList.get(0).getHand(i+1).getDisplayValue()) {
					lblPlayer1CardSum.setText("" + playerList.get(0).getHand(i + 1).getCurrentScore());
				}
			}

			lblPlayer2Balance.setText("Balance: " + playerList.get(1).getBalance());
			lblPlayer2Bet.setText("Bet: " + playerList.get(1).getBet());

			hands = playerList.get(1).getNumberOfHands();
			i = 0;
			if(hands == 1) {
				lblPlayer2CardSum.setText("" + playerList.get(1).getHand(i).getCurrentScore());
			}else if(hands == 2) {
				if(playerList.get(1).getHand(i).getDisplayValue()) {
					lblPlayer2CardSum.setText("" + playerList.get(1).getHand(i).getCurrentScore());
				}else if(playerList.get(1).getHand(i+1).getDisplayValue()) {
					lblPlayer2CardSum.setText("" + playerList.get(1).getHand(i + 1).getCurrentScore());
				}
			}

			lblPlayer3Balance.setText("Balance: " + playerList.get(2).getBalance());
			lblPlayer3Bet.setText("Bet: " + playerList.get(2).getBet());

			hands = playerList.get(2).getNumberOfHands();
			i = 0;
			if(hands == 1) {
				lblPlayer3CardSum.setText("" + playerList.get(2).getHand(i).getCurrentScore());
			}else if(hands == 2) {
				if(playerList.get(2).getHand(i).getDisplayValue()) {
					lblPlayer3CardSum.setText("" + playerList.get(2).getHand(i).getCurrentScore());
				}else if(playerList.get(2).getHand(i+1).getDisplayValue()) {
					lblPlayer3CardSum.setText("" + playerList.get(2).getHand(i + 1).getCurrentScore());
				}
			}

			lblPlayer4Balance.setText("Balance: " + playerList.get(3).getBalance());
			lblPlayer4Bet.setText("Bet: " + playerList.get(3).getBet());


			hands = playerList.get(3).getNumberOfHands();
			i = 0;
			if(hands == 1) {
				lblPlayer4CardSum.setText("" + playerList.get(3).getHand(i).getCurrentScore());
			}else if(hands == 2) {
				if(playerList.get(3).getHand(i).getDisplayValue()) {
					lblPlayer4CardSum.setText("" + playerList.get(3).getHand(i).getCurrentScore());
				}else if(playerList.get(3).getHand(i+1).getDisplayValue()) {
					lblPlayer4CardSum.setText("" + playerList.get(3).getHand(i + 1).getCurrentScore());
				}
			}

			lblDealerCardSum.setText("" + dealer.getValue());
		}
		if(numberOfPlayers == 5) {
			lblPlayer1Balance.setText("Balance: " + playerList.get(0).getBalance());
			lblPlayer1Bet.setText("Bet: " + playerList.get(0).getBet());
			lblPlayer1CardSum.setText("" + playerList.get(0).getHand(0).getCurrentScore());

			lblPlayer2Balance.setText("Balance: " + playerList.get(1).getBalance());
			lblPlayer2Bet.setText("Bet: " + playerList.get(1).getBet());
			lblPlayer2CardSum.setText("" + playerList.get(1).getHand(0).getCurrentScore());

			lblPlayer3Balance.setText("Balance: " + playerList.get(2).getBalance());
			lblPlayer3Bet.setText("Bet: " + playerList.get(2).getBet());
			lblPlayer3CardSum.setText("" + playerList.get(2).getHand(0).getCurrentScore());

			lblPlayer4Balance.setText("Balance: " + playerList.get(3).getBalance());
			lblPlayer4Bet.setText("Bet: " + playerList.get(3).getBet());
			lblPlayer4CardSum.setText("" + playerList.get(3).getHand(0).getCurrentScore());

			lblPlayer5Balance.setText("Balance: " + playerList.get(4).getBalance());
			lblPlayer5Bet.setText("Bet: " + playerList.get(4).getBet());
			lblPlayer5CardSum.setText("" + playerList.get(4).getHand(0).getCurrentScore());

			lblDealerCardSum.setText("" + dealer.getValue());
		}
	}

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

	public void setMain(Main main) {
		mainApp = main;
	}

	public void setClient(UserClient client) {
		this.client = client;
	}

}

