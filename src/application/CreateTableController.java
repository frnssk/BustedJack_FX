package application;

import client.UserClient;
import java.io.IOException;

import client.UserClient;
import communications.GameInfo;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

public class CreateTableController {

    @FXML
    private TextField tfBalance;

    @FXML
    private TextField tfRounds;

    @FXML
    private Button btnCreateTable;

    @FXML
    private Button btnBack;

    @FXML
    private TextField tfTime;

    @FXML
    private TextField tfMinimumBet;

    @FXML
    private CheckBox checkBoxPrivateMatch;
    
    private static Main mainApp;
    private UserClient client;
    
    public void createGameInfo(int rounds, int time, int minimumBet, int balance) {
    	GameInfo gameInfo = new GameInfo(rounds, time, minimumBet, balance);
    	client.sendGameInfo(gameInfo);
    }
    
    @FXML
    public void handleBtnBack() throws IOException {
    	mainApp.showMainMenu();
    }
    
    @FXML
    public void handleBtnCreateTable() throws IOException {
    	int rounds = Integer.parseInt(tfRounds.getText());
    	int time = Integer.parseInt(tfTime.getText());
    	int minimumBet = Integer.parseInt(tfMinimumBet.getText());
    	int balance = Integer.parseInt(tfBalance.getText());
    	
    	createGameInfo(rounds, time, minimumBet, balance);  
    	mainApp.showGame();
    }
    
	public static void setMain(Main main) {
		mainApp = main;
	}

	public void setClient(UserClient client) {
		this.client = client;
	}
}
