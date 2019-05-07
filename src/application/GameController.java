package application;

import client.UserClient;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class GameController {
	@FXML
	private Label lblPlayer1Bet;
	@FXML
	private Label lblPlayer3Bet;
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
	private Button btnIncreaseBet50;
	@FXML
	private Button btnIncreaseBet100;
	@FXML
	private Button btnIncreaseBet500;
	@FXML
	private Button btnClearBet;
	@FXML
	private Label lblPlayer4Balance;
	@FXML
	private Label lblPlayer2Bet;
	@FXML
	private Label lblPlayer1Balance;
	@FXML
	private Label lblPlayer3Balance;
	@FXML
	private Label lblPlayer5Balance;

	private static Main mainApp;
	private UserClient client;

	@FXML
	void clearCurrentBet() {

	}

	@FXML
	void confirmBet() {

	}

	@FXML
	void setNextBet100() {

	}

	@FXML
	void setNextBet25() {

	}

	@FXML
	void setNextBet50() {

	}

	@FXML
	void setNextBet500() {

	}
	
	public static void setMain(Main main) {
		mainApp = main;
	}

	public void setClient(UserClient client) {
		this.client = client;
	}

}

