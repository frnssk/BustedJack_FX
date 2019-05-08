package application;

import client.UserClient;
import communications.PlayerChoice;
import communications.StartGameRequest;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

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
	private ProgressBar cheatHeatProgressBar;
	@FXML
	private Label lblCheatHeatPersent;
	
	@FXML
	private Button btnCheat;
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

	private static Main mainApp;
	private UserClient client;
	
	private int balance = 0;
	private int minimumBet = 0;
	private int currentBet = 0;
	private int time = 0;
	
	private int rounds = 1;
	private int numberOfCheats = 0;
	private int cheatHeat = numberOfCheats / rounds;

	@FXML
	private void initialize() {
	}
	
    public void setDealerCardValue(int value) {
    	lblDealerCardSum.setText("" + value);
    }
    
    public void setStartingBalance(int balance) {
    	this.balance = balance;
    }
    
    public void setMinimumBet(int minBet) {
    	this.minimumBet = minBet;
    }
    
    public void setRounds(int rounds) {
    	this.rounds = rounds;
    	lblRounds.setText("Rounds: " + rounds);
    }
    
    public void updateRounds() {
    	lblRounds.setText("Rounds: " + (rounds--));
    }
    
    public void setTime(int time) {
    	//Code to come
    	lblTime.setText("Time: " + time);
    }
    
    @FXML
    private void handleStartGame() {
    	client.sendStartGame(new StartGameRequest());
    }
	
	@FXML
	private void clearCurrentBet() {
		currentBet = 0;
    	btnConfirmBet.setText("Confirm: " + currentBet);
	}

	@FXML
	private void confirmBet() {
		PlayerChoice choice = new PlayerChoice(4, cheatHeat);
		choice.setBet(currentBet); 
    	lblPlayer1Bet.setText("Bet: " + currentBet);
    	updateBalance();
    	client.sendPlayerChoice(choice);
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
		client.sendPlayerChoice(new PlayerChoice(5, cheatHeat));
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
	}
	
	public void setCheatHeat(int valueInPercent) {
		cheatHeatProgressBar.setProgress(valueInPercent);
		lblCheatHeatPersent.setText(valueInPercent + "%");
	}
	
    private void updateBalance() {
    	balance -= currentBet;
    	lblPlayer1Balance.setText("Balance: " + balance);
    }
   
    
    public void setPlayerName(int number, String username) {
    	switch(number) {
    	case 1:
    		lblPlayer1.setText(username);
    		break;
    	case 2:
    		lblPlayer2.setText(username);
    		break;
    	case 3:
    		lblPlayer3.setText(username);
    		break;
    	case 4:
    		lblPlayer4.setText(username);
    		break;
    	case 5:
    		lblPlayer5.setText(username);
    		break;
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
	
	public static void setMain(Main main) {
		mainApp = main;
	}

	public void setClient(UserClient client) {
		this.client = client;
	}

}

