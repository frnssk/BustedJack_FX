package server;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

import communications.GameInfo;
import communications.LogOutRequest;
import communications.LoginRequest;
import communications.PlayerChoice;
import communications.RegisterRequest;
import resources.Player;
import resources.Table;
import resources.User;

/*
 * @author RasmusOberg
 */
public class Server {

	private LinkedList<User> registeredUsers = new LinkedList<>(); //LinkedList to hold all registered users
	private HashMap<String, char[]> userPasswords = new HashMap<>(); //HashMap that holds all usernames and passwords
	private HashMap<Integer, Table> activeTables2 = new HashMap<>();
	private int tableIdCounter;
	private HashMap<Table, ArrayList<Player>> playersOnTable = new HashMap<>();//holds a table and a list of all players on that table
	private HashMap<Table, ArrayList<ClientHandler>> clientsOnTable = new HashMap<>();//holds a table and a list of all clients
	private HashMap<ClientHandler, Table> clientAndTable = new HashMap<>();

	/*
	 * Constructor to instantiate the server
	 */
	public Server(int port) {
		//		clients = new UserHandler();
		new ClientReceiver(port).start();
		TextWindow.println("Server started"); //Assistance
		readUsersFromFile();
	}

	/*
	 * Used every time the server starts to read in all the registered users
	 */
	public void readUsersFromFile() {
		try {
			FileInputStream fileIn = new FileInputStream("files/userlist.dat");
			ObjectInputStream objectIn = new ObjectInputStream(fileIn);
			boolean keepReading = true;
			TextWindow.println("----------------------------");
			TextWindow.println("ALLA REGISTRERADE ANVÄNDARE");
			TextWindow.println("----------------------------");
			try {
				while(keepReading) {
					User user = (User)objectIn.readObject();
					registeredUsers.add(user);
					userPasswords.put(user.getUsername(), user.getPassword());
					objectIn = new ObjectInputStream(fileIn);
					TextWindow.println(user.getUsername());
				}
			}catch(EOFException e) {
				keepReading = false;
				TextWindow.println("------------------");
				TextWindow.println("SLUT PÅ ANVÄNDARE.");
				TextWindow.println("------------------");
			}
		}catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/*
	 * Called every time a new user is registered, to keep the offline-list of users up to date
	 */
	public void updateUserDatabase(User user) {
		try(FileOutputStream fos = new FileOutputStream("files/userlist.dat", true);
				ObjectOutputStream oos = new ObjectOutputStream(fos)){
			registeredUsers.add(user);
			oos.writeObject(user);
			oos.flush();
			for(int i = 0; i < registeredUsers.size(); i++) {
				System.out.println(registeredUsers.get(i).getUsername());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/*
	 * Inner class which listens after new connections / clients trying to connect
	 * When a client connects, it creates a new instance of ClientHandler to 
	 * handle the client
	 */
	private class ClientReceiver extends Thread{
		private int port;

		public ClientReceiver(int port) {
			this.port = port;
		}

		public void run() {
			Socket socket = null;

			try(ServerSocket serverSocket = new ServerSocket(port)){
				while(true) {
					try {
						socket = serverSocket.accept();
						TextWindow.println("Client connected");
						new ClientHandler(socket);
					}catch(IOException ioException) {
						ioException.printStackTrace();
						if(socket!=null) {
							socket.close();
						}
					}
				}
			}catch(IOException ioException) {
				ioException.printStackTrace();
			}
		}
	}

	/*
	 * Starts a new Thread for every client
	 * @author RasmusOberg
	 */
	public class ClientHandler extends Thread{
		private Socket socket;
		private ObjectOutputStream output;
		private ObjectInputStream input;
		private Object obj;
		private boolean isOnline = true;

		public ClientHandler(Socket socket) throws IOException {
			this.socket = socket;
			output = new ObjectOutputStream(socket.getOutputStream());
			input = new ObjectInputStream(socket.getInputStream());
			start();
			TextWindow.println("ClientHandler started");
		}

		/*
		 * Keeps on running as long as the client is still connected
		 * Reads objects from the client and depending on the type of object the server acts accordingly
		 */
		public void run() {
			while(isOnline) {
				try {
					obj = input.readObject();
					String choice = "";

					/**
					 * This is used to decode what the client is sending
					 * Depending on what kind of object, the server responds
					 * accordingly
					 */
					if(obj instanceof RegisterRequest) {
						RegisterRequest registerRequest = (RegisterRequest)obj;
						choice = registerNewUser(registerRequest);
					}

					else if(obj instanceof LoginRequest) {
						LoginRequest loginRequest = (LoginRequest)obj;
						choice = loginUser(loginRequest);
					}

					else if(obj instanceof LogOutRequest) {
						LogOutRequest logoutRequest = (LogOutRequest)obj;
						logoutUser(logoutRequest, this);
					}

					else if(obj instanceof GameInfo) {
						GameInfo gameInfo = (GameInfo)obj;
						createNewTableAndAddPlayer(this, gameInfo);
					}

					else if(obj instanceof Integer) {
						int tableId = (Integer)obj;
						choice = addPlayerToTable(tableId, this);
					}

					else if(obj instanceof PlayerChoice) {
						PlayerChoice playerChoice = (PlayerChoice)obj;
						makePlayerChoice(playerChoice, this);
						TextWindow.println(UserHandler.getUser(this).getUsername() + " HAR TRYCKT = " + playerChoice.getChoice());
					}

					output.writeObject(choice);
					output.flush();
				} catch (ClassNotFoundException | IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		/*
		 * used when a new user connects
		 */
		public String registerNewUser(RegisterRequest registerRequest) {
			String choice = "";
			if(checkUsernameAvailability(registerRequest.getUsername())) {
				TextWindow.println(registerRequest.getUsername() + " är ledigt."); //Assistance
				if(isPasswordOkay((registerRequest.getPassword()))) {
					TextWindow.println((registerRequest.getUsername()) + " har angett ett godkänt lösenord."); //Assistance
					User temporary = new User(registerRequest.getUsername());
					temporary.setPassword(registerRequest.getPassword());
					userPasswords.put(registerRequest.getUsername(), registerRequest.getPassword());
					updateUserDatabase(temporary);
					//Adds the user and client to the UserHandler-HashMap
					UserHandler.newUserConnect(temporary, this);
					choice = "USER_TRUE";
				}else { 
					TextWindow.println(registerRequest.getUsername() + " har angett ett icke godkänt lösenord."); //Assistance
					choice = "PASSWORD_FALSE";
				}
			}else {
				choice = "USERNAME_FALSE";
			}
			return choice;
		}
		
		/*
		 * used to log in existing users
		 */
		public String loginUser(LoginRequest loginRequest) {
			String choice = "";
			if(isUserRegistered(loginRequest.getUsername())) {
				TextWindow.println(loginRequest.getUsername() + " is a registered user."); //Assistance
				if(passwordMatchUser(loginRequest.getUsername(), loginRequest.getPassword())){
					choice = "LOGIN_SUCCES";
					User user = getUser(loginRequest.getUsername());
					UserHandler.addNewActiveUser(user, this);
				}else {
					choice = "LOGIN_FAIL";
					TextWindow.println(loginRequest.getUsername() + " entered the wrong password."); //Assistance
				}
			}
			return choice;
		}
		
		/*
		 * Checks whether or not a user is registered
		 */
		private boolean isUserRegistered(String name) {
			for(int i = 0; i < registeredUsers.size(); i++) {
				User compare = registeredUsers.get(i);
				String compareName = compare.getUsername();
				if(compareName.equals(name)) {
					return true;
				}
			}
			return false;
		}
		
		/*
		 * Checks if the given password matches the password that is stored
		 * Used to log in users
		 */
		private boolean passwordMatchUser(String username, char[] password) {
			char[] array = userPasswords.get(username);
			return Arrays.equals(array, password);
		}

		/*
		 * Checks whether or not a username is already in use
		 * Used to make sure a new user doesn't take an existing users name
		 */
		private boolean checkUsernameAvailability(String name) {
			for(int i = 0; i < registeredUsers.size(); i++) {
				String compareName = registeredUsers.get(i).getUsername();
				if(compareName.equals(name)) {
					return false;
				}
			}
			return true;
		}

		/*
		 * 
		 */
		public User getUser(String name) {
			for(int i = 0; i < registeredUsers.size(); i++) {
				User compare = registeredUsers.get(i);
				String compareName = compare.getUsername();
				if(compareName.equals(name)) {
					return compare;
				}
			}
			return null;
		}

		/*
		 * Checks whether or not a password is the required length
		 */
		public boolean isPasswordOkay(char[] password) {
			if(password.length < 6 || password.length > 11) {
				return false;
			}else{
				return true;
			}
		}
		
		/*
		 * used to log out a user
		 */
		public void logoutUser(LogOutRequest logoutRequest, ClientHandler clientHandler) {
			clientHandler.isOnline = false;
			TextWindow.println(logoutRequest.getUserName() + " disconnected.");
			UserHandler.removeActiveUser(this);
		}
		
		/*
		 * used to create a new table
		 */
		public void createNewTableAndAddPlayer(ClientHandler clientHandler, GameInfo gameInfo) {
			Table table = new Table(gameInfo.getTime(), gameInfo.getRounds(), gameInfo.getBalance(), gameInfo.getMinBet());
			setTableId(table);
			User user = UserHandler.getUser(this);
			Player player = new Player(user.getUsername());
			table.addPlayer(player);
			table.addClient(this);
			TextWindow.println(player.getUsername() + " tillagd på Table " + table.getTableId());
			ArrayList<Player> playerList = new ArrayList<>();
			playerList.add(player);
			playersOnTable.put(table, playerList); //adds the table and its list of player in a hash-map
			ArrayList<ClientHandler> clientList = new ArrayList<>();
			clientList.add(this); 
			clientsOnTable.put(table, clientList);
			clientAndTable.put(clientHandler, table);
			updateList(clientList, playerList);
		}
		
		/*
		 * Sets a unique ID to specific table
		 */
		public synchronized void setTableId(Table table) {
			table.setTableId(tableIdCounter);
			activeTables2.put(tableIdCounter, table);
			tableIdCounter++;
		}
		
		/*
		 * used to find the table corresponding to the ID provided by the user
		 * checks if the table even exists, if it does and all conditions are met - tries to add the player
		 */
		public String addPlayerToTable(int tableId, ClientHandler clientHandler) {
			String choice = "";
			if(doesTableExist(tableId)) {
				TextWindow.println("Bord med id: " + tableId + " finns.");
				choice = "TABLE_TRUE";
				Table table = activeTables2.get(tableId);
				if(table.getNumberOfPlayers() < 5 && !table.checkTableStarted()) {
					addPlayerOnExistingTable(this, table);
				}
			}else {
				choice = "TABLE_FALSE";
				TextWindow.println("Bord med id: " + tableId + " finns ej.");
			}
			return choice;
		}
		
		/*
		 * used to add a player to an existing table
		 */
		public void addPlayerOnExistingTable(ClientHandler clientHandler, Table table) {
			User user = UserHandler.getUser(clientHandler);
			Player player = new Player(user.getUsername());
			table.addPlayer(player);
			table.addClient(this);
			TextWindow.println(player.getUsername() + " tillagd på Table " + table.getTableId());
			ArrayList<Player> playerList = playersOnTable.get(table);
			playerList.add(player);
			ArrayList<ClientHandler> clientList = clientsOnTable.get(table);
			clientList.add(clientHandler);
			clientAndTable.put(clientHandler, table);
			updateList(clientList, playerList);
		}

		/*
		 * Used when a user tries to join a table
		 * makes sure a user can't join a table that doesn't exist
		 */
		public boolean doesTableExist(int tableId) {
			return activeTables2.containsKey(tableId);
		}

		/*
		 * used to set the choice a user made, converts it to the correct player and sets the choice
		 */
		public void makePlayerChoice(PlayerChoice playerChoice, ClientHandler clientHandler) {
			Table table = clientAndTable.get(clientHandler);
			Player player = table.getPlayerAndClient().get(clientHandler);
			player.setPlayerChoice(playerChoice);
		}

		/*
		 * used to update the player-list on all the clients whenever a new one connects
		 */
		public void updateList(ArrayList<ClientHandler> clientList, ArrayList<Player> playerList) {
			for(int i = 0; i < clientList.size(); i++) {
				try {
					ClientHandler ch = clientList.get(i);
					ch.output.writeObject((ArrayList<Player>)playerList.clone());
					ch.output.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		/*
		 * used to update all the clients with the latest actions on their table
		 */
		public void updateTableInformation(Table table) {
			try {
				output.writeObject(table);
				output.flush();
			}catch(IOException ex) {
				ex.printStackTrace();
			}
		}
	}


	/*
	 * HashMap containing all the online users
	 * @author RasmusOberg
	 */
	private static class UserHandler {
		private static HashMap<ClientHandler, User> activeUsers = new HashMap<>();

		//connects a new client
		public synchronized static void newUserConnect(User user, ClientHandler clientHandler) {
			activeUsers.put(clientHandler, user);
			TextWindow.println("NEW USER = " + user.getUsername() + " är nu registrerad.");
		}

		//adds new user to activeUsers-HashMap
		public synchronized static void addNewActiveUser(User user, ClientHandler clientHandler) {
			activeUsers.put(clientHandler, user);
			TextWindow.println("NEW LOGIN = " + user.getUsername() + " är aktiv.");
		}

		public synchronized static HashMap<ClientHandler, User> getActiveUsers(){
			return activeUsers;
		}

		//Used to get which user is connected to a specific client / clienthandler
		public synchronized static User getUser(ClientHandler clientHandler) {
			return activeUsers.get(clientHandler);
		}

		//Used to remove a user when he disconnects
		public synchronized static void removeActiveUser(ClientHandler clientHandler) {
			activeUsers.remove(clientHandler, getUser(clientHandler));
			TextWindow.println("NEW LOGOUT = " + getUser(clientHandler).getUsername() + " är ej längre aktiv.");
		}
	}
}


