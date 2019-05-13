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
import resources.DealerHand;
import resources.Player;

public class GameController {
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
	
//	@FXML
//	private Label lblPlayerArray[];

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

	private int balance = 0;
	private int minimumBet = 0;
	private int currentBet = 0;
	private int time = 0;

	private int numberOfPlayers = 0;

	private int rounds = 0;
	private int currentRound = 1;
	private int numberOfCheats = 0;
	private int cheatHeat = numberOfCheats / currentRound;

	@FXML
	private void initialize() {
	}

	public void setDealerCardValue(int value) {
		lblDealerCardSum.setText("" + value);
	}

	public void setStartingBalance(int balance) {
		this.balance = balance;
	}

	public void setRounds(int rounds) {
		this.rounds = rounds;
		lblRounds.setText("Rounds: " + rounds);
	}

	public void updateRounds() {
		lblRounds.setText("Rounds: " + (rounds--));
		currentRound ++;
		setCheatHeat(cheatHeat);
	}

	public void setTime(int time) {
		//Code to come
		lblTime.setText("Time: " + time);
	}

	public void setCheatHeat(int valueInPercent) {
		cheatHeatProgressBar.setProgress(valueInPercent);
		lblCheatHeatPersent.setText(valueInPercent + "%");
	}

	//	private void updateBalance() {
	//		balance -= currentBet;
	//		lblPlayer1Balance.setText("Balance: " + balance);
	//	}

	public void setTableId(int tableId) {
		lblTableId.setText("Table ID: " + tableId);
	}

	public void setStartingInformation(int rounds, int minutes, int minimumBet) {
		setRounds(rounds);
		setTime(minutes);
		this.minimumBet = minimumBet;
		lblMinimumBet.setText("Minimum bet: " + minimumBet);

	}

	//true will disable, false will enable
	public void disableAllButtons(boolean bool) { 
		btnCheat.setDisable(bool);
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
	}

	@FXML
	private void handleStartGame() throws IOException {
		client.sendStartGame(new StartGameRequest(1));
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
		} else {
			PlayerChoice choice = new PlayerChoice(4, cheatHeat);
			choice.setBet(currentBet); 
			lblPlayer1Bet.setText("Bet: " + currentBet);
			//updateBalance();
			client.sendPlayerChoice(choice);
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
		PlayerChoice choice = new PlayerChoice(5, cheatHeat);
		choice.setCheatChoice(true);
		client.sendPlayerChoice(choice);
	}

	@FXML
	private void handleDoNotCheat() {
		setCheatHeat(cheatHeat);
		PlayerChoice choice = new PlayerChoice(5, cheatHeat);
		choice.setCheatChoice(false);
		client.sendPlayerChoice(choice);
	}

	@FXML 
	private void handleBust() {

	}

	@FXML 
	private void handleSplit() {

	}

	@FXML 
	private void handleDouble() {
		client.sendPlayerChoice(new PlayerChoice(3, cheatHeat));
	}

	@FXML 
	private void handleStay() {
		client.sendPlayerChoice(new PlayerChoice(2, cheatHeat));
	}

	@FXML 
	private void handleHit() {
		client.sendPlayerChoice(new PlayerChoice(1, cheatHeat));
		System.out.println("[GAME_CONTORLLER] == Någon har tryck HIT.");
	}

	public void updatePlayerList(ArrayList<Player> playerList) {
		System.out.println("[GAME_CONTROLLER] == GameController har mottagit lista. Antal = " + playerList.size());
		numberOfPlayers = playerList.size();
//		lblPlayerArray = new Label[numberOfPlayers];

		if(numberOfPlayers == 1) {
			lblPlayer1.setText(playerList.get(0).getUsername());
		}
		if(numberOfPlayers == 2) {
			lblPlayer1.setText(playerList.get(0).getUsername());
			lblPlayer2.setText(playerList.get(1).getUsername());
		}
		if(numberOfPlayers == 3) {
			lblPlayer1.setText(playerList.get(0).getUsername());
			lblPlayer2.setText(playerList.get(1).getUsername());
			lblPlayer3.setText(playerList.get(2).getUsername());
		}
		if(numberOfPlayers == 4) {
			lblPlayer1.setText(playerList.get(0).getUsername());
			lblPlayer2.setText(playerList.get(1).getUsername());
			lblPlayer3.setText(playerList.get(2).getUsername());
			lblPlayer4.setText(playerList.get(3).getUsername());
		}
		if(numberOfPlayers == 5) {
			lblPlayer1.setText(playerList.get(0).getUsername());
			lblPlayer2.setText(playerList.get(1).getUsername());
			lblPlayer3.setText(playerList.get(2).getUsername());
			lblPlayer4.setText(playerList.get(3).getUsername());
			lblPlayer5.setText(playerList.get(4).getUsername());
		}

	}
	
	public void updateRoundInformation(ArrayList<Player> playerList, DealerHand dealer) {
		
		if(numberOfPlayers == 2) {
			lblPlayer1Balance.setText("Balance: " + playerList.get(0).getBalance());
			lblPlayer1Bet.setText("Bet :" + playerList.get(0).getBet());
			lblPlayer1CardSum.setText("" + playerList.get(0).getHand(0).getCurrentScore());
			
			lblPlayer2Balance.setText("Balance: " + playerList.get(1).getBalance());
			lblPlayer2Bet.setText("Bet :" + playerList.get(1).getBet());
			lblPlayer2CardSum.setText("" + playerList.get(1).getHand(0).getCurrentScore());
			
			lblDealerCardSum.setText("" + dealer.getValue());
		
		}
		if(numberOfPlayers == 3) {
			lblPlayer1Balance.setText("Balance: " + playerList.get(0).getBalance());
			lblPlayer1Bet.setText("Bet :" + playerList.get(0).getBet());
			lblPlayer1CardSum.setText("" + playerList.get(0).getHand(0).getCurrentScore());
			
			lblPlayer2Balance.setText("Balance: " + playerList.get(1).getBalance());
			lblPlayer2Bet.setText("Bet :" + playerList.get(1).getBet());
			lblPlayer2CardSum.setText("" + playerList.get(1).getHand(0).getCurrentScore());
			
			lblPlayer3Balance.setText("Balance: " + playerList.get(2).getBalance());
			lblPlayer3Bet.setText("Bet :" + playerList.get(2).getBet());
			lblPlayer3CardSum.setText("" + playerList.get(2).getHand(0).getCurrentScore());
			
			lblDealerCardSum.setText("" + dealer.getValue());
		}
		if(numberOfPlayers == 4) {
			lblPlayer1Balance.setText("Balance: " + playerList.get(0).getBalance());
			lblPlayer1Bet.setText("Bet :" + playerList.get(0).getBet());
			lblPlayer1CardSum.setText("" + playerList.get(0).getHand(0).getCurrentScore());
			
			lblPlayer2Balance.setText("Balance: " + playerList.get(1).getBalance());
			lblPlayer2Bet.setText("Bet :" + playerList.get(1).getBet());
			lblPlayer2CardSum.setText("" + playerList.get(1).getHand(0).getCurrentScore());
			
			lblPlayer3Balance.setText("Balance: " + playerList.get(2).getBalance());
			lblPlayer3Bet.setText("Bet :" + playerList.get(2).getBet());
			lblPlayer3CardSum.setText("" + playerList.get(2).getHand(0).getCurrentScore());
			
			lblPlayer4Balance.setText("Balance: " + playerList.get(3).getBalance());
			lblPlayer4Bet.setText("Bet :" + playerList.get(3).getBet());
			lblPlayer4CardSum.setText("" + playerList.get(3).getHand(0).getCurrentScore());
			
			lblDealerCardSum.setText("" + dealer.getValue());
		}
		if(numberOfPlayers == 5) {
			lblPlayer1Balance.setText("Balance: " + playerList.get(0).getBalance());
			lblPlayer1Bet.setText("Bet :" + playerList.get(0).getBet());
			lblPlayer1CardSum.setText("" + playerList.get(0).getHand(0).getCurrentScore());
			
			lblPlayer2Balance.setText("Balance: " + playerList.get(1).getBalance());
			lblPlayer2Bet.setText("Bet :" + playerList.get(1).getBet());
			lblPlayer2CardSum.setText("" + playerList.get(1).getHand(0).getCurrentScore());
			
			lblPlayer3Balance.setText("Balance: " + playerList.get(2).getBalance());
			lblPlayer3Bet.setText("Bet :" + playerList.get(2).getBet());
			lblPlayer3CardSum.setText("" + playerList.get(2).getHand(0).getCurrentScore());
			
			lblPlayer4Balance.setText("Balance: " + playerList.get(3).getBalance());
			lblPlayer4Bet.setText("Bet :" + playerList.get(3).getBet());
			lblPlayer4CardSum.setText("" + playerList.get(3).getHand(0).getCurrentScore());
			
			lblPlayer5Balance.setText("Balance: " + playerList.get(4).getBalance());
			lblPlayer5Bet.setText("Bet :" + playerList.get(4).getBet());
			lblPlayer5CardSum.setText("" + playerList.get(4).getHand(0).getCurrentScore());
			
			lblDealerCardSum.setText("" + dealer.getValue());
			
			
		}
		
		
	}
	

	public void updateStartingMoney(int startingMoney) {

		if(numberOfPlayers == 1) {
			lblPlayer1Balance.setText("Balance: " + startingMoney);
		}
		if(numberOfPlayers == 2) {
			lblPlayer1Balance.setText("Balance: " + startingMoney);
			lblPlayer2Balance.setText("Balance: " + startingMoney);
		}
		if(numberOfPlayers == 3) {
			lblPlayer1Balance.setText("Balance: " + startingMoney);
			lblPlayer2Balance.setText("Balance: " + startingMoney);
			lblPlayer3Balance.setText("Balance: " + startingMoney);
		}
		if(numberOfPlayers == 4) {
			lblPlayer1Balance.setText("Balance: " + startingMoney);
			lblPlayer2Balance.setText("Balance: " + startingMoney);
			lblPlayer3Balance.setText("Balance: " + startingMoney);
			lblPlayer4Balance.setText("Balance: " + startingMoney);
		}
		if(numberOfPlayers == 5) {
			lblPlayer1Balance.setText("Balance: " + startingMoney);
			lblPlayer2Balance.setText("Balance: " + startingMoney);
			lblPlayer3Balance.setText("Balance: " + startingMoney);
			lblPlayer4Balance.setText("Balance: " + startingMoney);
			lblPlayer5Balance.setText("Balance: " + startingMoney);
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

