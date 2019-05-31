package application;

import client.UserClient;
import java.io.IOException;

import client.UserClient;
import communications.GameInfo;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

/**
 * Class used to control and communicate to the CreateTableScreen.fxml
 * Updated UI and send button action to client 
 * @author Isak Eklund
 * @author Christoffer Palvin
 */
public class CreateTableController {

	@FXML private TextField tfBalance;
	@FXML private TextField tfRounds;
	@FXML private Button btnCreateTable;
	@FXML private Button btnBack;
	@FXML private TextField tfTime; 
	@FXML private TextField tfMinimumBet;
	@FXML private CheckBox checkBoxPrivateMatch;
	private Main mainApp;
	private UserClient client;

	/**
	 * Sends user back to main menu
	 * @throws IOException
	 */
	@FXML
	public void handleBtnBack() throws IOException {
		mainApp.showMainMenu();
	}

	/**
	 * Takes parameters from UI and sends a GameInfo object to the client
	 * Shows error message if there is anything wrong
	 * @throws IOException
	 */
	@FXML
	public void handleBtnCreateTable() throws IOException {
		int rounds = Integer.parseInt(tfRounds.getText());
		int time = Integer.parseInt(tfTime.getText());
		int minimumBet = Integer.parseInt(tfMinimumBet.getText());
		int balance = Integer.parseInt(tfBalance.getText());
		boolean privateMatch = checkBoxPrivateMatch.isSelected();
		
		if(balance < minimumBet) {
			try {
				mainApp.showAlert("Alert", "Balance needs to be higher than the minimum bet");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if(time == 0 || rounds == 0 || minimumBet == 0 || balance == 0 || tfTime.getText().trim().isEmpty() ||
				tfRounds.getText().trim().isEmpty() || tfMinimumBet.getText().trim().isEmpty() || tfBalance.getText().trim().isEmpty()) {
			try {
				mainApp.showAlert("Alert", "You need to fill all the parameters with a valid number");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else{
			GameInfo gameInfo = new GameInfo(time, rounds, balance, minimumBet, privateMatch);
			client.sendGameInfo(gameInfo);
			mainApp.showGame();
		}
	}
	
	/**
	 * Connects main class to the UI
	 * @param main
	 */
	public void setMain(Main main) {
		mainApp = main;
	}

	/**
	 * Connets the client to the UI
	 * @param client
	 */
	public void setClient(UserClient client) {
		this.client = client;
	}
}
