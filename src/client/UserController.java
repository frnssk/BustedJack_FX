package client;

import java.util.ArrayList;

import communications.GameInfo;
import communications.LogOutRequest;
import communications.LoginRequest;
import communications.PlayerChoice;
import communications.RegisterRequest;
import resources.Player;
import resources.User;

/**
 * The main logic in the user application 
 * Hold the logic and connects the UI to the client 
 * @author Isak Eklund
 *
 */
public class UserController {
	private UserClient client;
	private UserInterface ui;
	private User user;
	private boolean nameOk = false;
	private String[] titles = {"Godfather", "Boss", "Gentleman", 
			"Grinder", "Gambler", "Peasant"}; 

	public UserController(UserClient client) {
		this.client = client;
		client.setUserController(this);
	}

	public void setUI(UserInterface ui) {
		this.ui = ui;
	}

	public void setRankAndTitle(int rank) {
		ui.setRank(rank);

		if(rank >= 1500) {
			ui.setTitle(titles[1]);
		} else if (rank >= 800) {
			ui.setTitle(titles[2]);
			ui.setNextTitle(titles[1]);
		} else if (rank >= 400) {
			ui.setTitle(titles[3]);
			ui.setNextTitle(titles[2]);
		} else if(rank >= 200) {
			ui.setTitle(titles[4]);
			ui.setNextTitle(titles[3]);
		} else if(rank >= 100) {
			ui.setTitle(titles[5]);
			ui.setNextTitle(titles[4]);
		} else if (rank < 100) {
			ui.setTitle(titles[6]);
			ui.setNextTitle(titles[5]);
		}
	}
	public void createRegisterRequest(String username, char[] password) {
		RegisterRequest request = new RegisterRequest(username, password);
		client.sendRegisterRequest(request);
	}

	public void createLoginRequest(String username, char[] password) {
		LoginRequest request = new LoginRequest(username, password);
		client.sendLoginRequest(request);
	}

	public void createLogOutRequest(String name) {
		client.sendLogOutRequest(new LogOutRequest(name));
	}

//	public void createGameInfo(int time, int rounds, int balance, int minimumBet) {
//		GameInfo gameInfo = new GameInfo(time, rounds, balance, minimumBet);
//		client.sendGameInfo(gameInfo);
//	}

	public void checkTableId(int tableId) {
		client.checkTableId(tableId);
	}

	public void sendPlayerChoice(PlayerChoice choice) {
		client.sendPlayerChoice(choice);
	}
	
	public void joinRandomTable() {
		client.sendRandomTableRequest();
	}

	public void updatePlayerList(ArrayList<Player> playerList) {
		System.out.println("[CONTROLLER] == Controller har mottagit lista. Antal = " + playerList.size());
		
		if(playerList.size() == 1)
			ui.setPlayer1Name(playerList.get(0).getUsername());
			System.out.println("Testing 1 spelare");
		if(playerList.size() == 2) {
			ui.setPlayer1Name(playerList.get(0).getUsername());
			ui.setPlayer2Name(playerList.get(1).getUsername());
			System.out.println("Testing 2 spelare");
		}
		if(playerList.size() == 3) {
			ui.setPlayer1Name(playerList.get(0).getUsername());
			ui.setPlayer2Name(playerList.get(1).getUsername());
			ui.setPlayer3Name(playerList.get(2).getUsername());
			System.out.println("Testing 3 spelare");
		}
		if(playerList.size() == 4) {
			ui.setPlayer1Name(playerList.get(0).getUsername());
			ui.setPlayer2Name(playerList.get(1).getUsername());
			ui.setPlayer3Name(playerList.get(2).getUsername());
			ui.setPlayer4Name(playerList.get(3).getUsername());
			System.out.println("Testing 4 spelare");
		}
		if(playerList.size() == 5) {
			ui.setPlayer1Name(playerList.get(0).getUsername());
			ui.setPlayer2Name(playerList.get(1).getUsername());
			ui.setPlayer3Name(playerList.get(2).getUsername());
			ui.setPlayer4Name(playerList.get(3).getUsername());
			ui.setPlayer5Name(playerList.get(4).getUsername());
		}

	}

	/**
	 * If user name comes back ok, checks if the user name fulfills the requirements.
	 * Shows error message if name is taken 
	 * @param available - an int that is checked and changes are made based on its outcome 
	 */
	public void checkCreatedUser(String available) {
		if(available.equals("USERNAME_FALSE")) {
			System.out.println("Username: ej ledigt");
			ui.errorMessageUsername();
		}else if(available.equals("PASSWORD_FALSE")) {
			System.out.println("Felaktigt lösenord");
			ui.errorMessagePassword();
		}else if(available.equals("USER_TRUE")) {
			System.out.println("Usernamne och lösenord OK");
			ui.updateUI(ui.mainMenuScreen());
		}else if(available.equals("LOGIN_SUCCES")) {
			System.out.println("Inloggad!!!");
			ui.updateUI(ui.mainMenuScreen());
		}else if(available.equals("LOGIN_FAIL")) {
			System.out.println("Felaktigt lösenord");
			ui.errorMessagePassword();
		}else if(available.equals("TABLE_TRUE")){
			System.out.println("RoomId ok");
			//Update ui
		}else if(available.equals("TABLE_FALSE")) {
			System.out.println("RoomId inte ok");
			//update ui
		}else if(available.equals("RANDOM_TRUE")) {
			System.out.println("du är tilllagd på bordet");
		}
	}


	//	public void connect(User user) {
	//		try {
	//			client.connect(user);
	//		} catch (IOException e) {
	//			e.printStackTrace();
	//		}
	//	}


}
