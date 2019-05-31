package server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
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
import java.util.Random;

import communications.GameInfo;
import communications.LogOutRequest;
import communications.LoginRequest;
import communications.PlayerChoice;
import communications.RandomTableRequest;
import communications.RegisterRequest;
import communications.StartGameRequest;
import communications.TableID;
import resources.Player;
import resources.Table;
import resources.User;

/**
 * Server which handles a number of clients and the users
 * @author Rasmus Öberg
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
			TextWindow.println("ALLA REGISTRERADE ANVÄNDARE.");
			TextWindow.println("----------------------------");
			int i = 1;
			try {
				while(keepReading) {
					User user = (User)objectIn.readObject();
					registeredUsers.add(user);
					userPasswords.put(user.getUsername(), user.getPassword());
					TextWindow.println(i + "	" + user.getUsername());
					i++;
					objectIn = new ObjectInputStream(fileIn);
				}
			}catch(EOFException e) {
				keepReading = false;
				
				TextWindow.println("------------------");
				TextWindow.println("SLUT PÅ ANVÄNDARE.");
				TextWindow.println("TOTALT ANTAL REGISTRERADE ANVÄNDARE = " + registeredUsers.size());
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
						TextWindow.println("[SERVER] >> Client connected");
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

	/**
	 * Starts a new Thread for every client, so that multiple users can use 
	 * the server concurrently with a unique client
	 * @author RasmusOberg
	 * @author Simon Lilja
	 */
	public class ClientHandler extends Thread {
		/**
		 * 
		 */
		private Socket socket;
		private ObjectOutputStream output;
		private ObjectInputStream input;
		private Object obj;
		private boolean isOnline = true;

		public ClientHandler(Socket socket) throws IOException {
			this.socket = socket;
			this.socket.setTcpNoDelay(true);
			output = new ObjectOutputStream(socket.getOutputStream());
			input = new ObjectInputStream(socket.getInputStream());
			start();
			TextWindow.println("[SERVER] >> New ClientHandler started");
		}
		
		public void output(Object obj) {
			try {
				output.writeObject(obj);
				output.flush();
				output.reset();
			} catch (IOException e) {
				e.printStackTrace();
			}	
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
						addPlayerToTable(tableId, this);
					}
					
					else if(obj instanceof RandomTableRequest) {
						addPlayerOnRandomTable(this);
					}

					else if(obj instanceof PlayerChoice) {
						PlayerChoice playerChoice = (PlayerChoice)obj;
						makePlayerChoice(playerChoice, this);
					}
					
					else if(obj instanceof StartGameRequest) {
						Table table = clientAndTable.get(this);
						table.start();
						TextWindow.println("[SERVER] >> StartGameRequest mottagen, startar bord: " + table.getTableId() + ".");
					}

					output.writeObject(choice);
					output.flush();
					input.reset();
				} catch (ClassNotFoundException | IOException e) {
//					e.printStackTrace();
				}
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		/*
		 * Used to register a new user so that they can log in 
		 * with the existing user next time
		 */
		public synchronized String registerNewUser(RegisterRequest registerRequest) {
			String choice = "";
			if(checkUsernameAvailability(registerRequest.getUsername())) {
				TextWindow.println("[SERVER] >> " + registerRequest.getUsername() + " är ledigt."); //Assistance
				if(isPasswordOkay((registerRequest.getPassword()))) {
					TextWindow.println(("[SERVER] >> " + registerRequest.getUsername()) + " har angett ett godkänt lösenord."); //Assistance
					User temporary = new User(registerRequest.getUsername());
					temporary.setPassword(registerRequest.getPassword());
					userPasswords.put(registerRequest.getUsername(), registerRequest.getPassword());
					updateUserDatabase(temporary);
					UserHandler.newUserConnect(temporary, this);
					choice = "USER_TRUE";
				}else { 
					TextWindow.println("[SERVER] >> " + registerRequest.getUsername() + " har angett ett icke godkänt lösenord."); //Assistance
					choice = "PASSWORD_FALSE";
				}
			}else {
				choice = "USERNAME_FALSE";
			}
			return choice;
		}
		
		/*
		 * Used to log in existing users
		 */
		public synchronized String loginUser(LoginRequest loginRequest) {
			String choice = "";
			if(isUserRegistered(loginRequest.getUsername())) {
				TextWindow.println("[SERVER] >> " + loginRequest.getUsername() + " is a registered user."); //Assistance
				if(passwordMatchUser(loginRequest.getUsername(), loginRequest.getPassword())){
					choice = "LOGIN_SUCCES";
					User user = getUser(loginRequest.getUsername());
					UserHandler.addNewActiveUser(user, this);
				}else {
					choice = "LOGIN_FAIL";
					TextWindow.println("[SERVER] >> " + loginRequest.getUsername() + " entered the wrong password."); //Assistance
				}
			}else {
				choice = "LOGIN_NOT_EXIST";
			}
			return choice;
		}
		
		/**
		 * Checks whether or not a user is registered, to make sure name duplicates doesn't occur
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
		
		private boolean passwordMatchUser(String username, char[] password) {
			char[] array = userPasswords.get(username);
			return Arrays.equals(array, password);
		}

		/**
		 * Used to make sure that 2 users doesn't register the same username
		 * @param name - The name being tested 
		 * @return - boolean 
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

		public boolean isPasswordOkay(char[] password) {
			if(password.length < 6 || password.length > 11) {
				return false;
			}else{
				return true;
			}
		}
		
		/**
		 * Used to logout a user/client
		 * @param logoutRequest
		 * @param clientHandler - to know which client it is
		 */
		public void logoutUser(LogOutRequest logoutRequest, ClientHandler clientHandler) {
			clientHandler.isOnline = false;
			TextWindow.println("[SERVER] >> " + logoutRequest.getUserName() + " disconnected.");
			UserHandler.removeActiveUser(this);
		}
		
		/**
		 * Used to create a new table
		 * @param clientHandler - the user/client which creates it
		 * @param gameInfo - the parametres the user used
		 */
		public synchronized void createNewTableAndAddPlayer(ClientHandler clientHandler, GameInfo gameInfo) {
			Table table = new Table(gameInfo.getTime(), gameInfo.getRounds(), gameInfo.getBalance(), gameInfo.getMinBet(), gameInfo.getPrivateMatchStatus());
			setTableId(table);
			User user = UserHandler.getUser(this);
			Player player = new Player(user.getUsername());
			TextWindow.println("[SERVER] >> Nytt bord skapat av \"" + player.getUsername() + "\", med ID: " + table.getTableId());
			table.addPlayer(player);
			table.addClient(this);
			table.addClientAndPlayer(clientHandler, player);
			TextWindow.println("[SERVER] >> \"" + player.getUsername() + "\" tillagd på Table nummer: " + table.getTableId());
			ArrayList<Player> playerList = new ArrayList<>();
			playerList.add(player);
			playersOnTable.put(table, playerList); //adds the table and its list of player in a hash-map
			ArrayList<ClientHandler> clientList = new ArrayList<>();
			clientList.add(this); 
			clientsOnTable.put(table, clientList);
			clientAndTable.put(clientHandler, table);
			try {
				clientHandler.output.writeObject(new TableID(table.getTableId()));
				clientHandler.output.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
			updateList(clientList, playerList);
		}
		
		/**
		 * Gives a table a unique ID, to keep track of how many tables are active
		 * and to enable multiple tables
		 * @param table - The table which needs an ID
		 */
		public synchronized void setTableId(Table table) {
			table.setTableId(tableIdCounter);
			activeTables2.put(tableIdCounter, table);
			tableIdCounter++;
		}
		
		/**
		 * Used when a user wants to join a specific table
		 * @param tableId - the ID of the table he wants to join
		 * @param clientHandler - the user/client who made the choice
		 */
		public synchronized void addPlayerToTable(int tableId, ClientHandler clientHandler) {
			String choice = "";
			if(doesTableExist(tableId)) {
				TextWindow.println("[SERVER] >> Bord med id: " + tableId + " finns.");
				choice = "TABLE_TRUE";
				
				try {
					clientHandler.output.writeObject(choice);
				} catch (IOException e) {}
				
				Table table = activeTables2.get(tableId);
				if(table.getNumberOfPlayers() < 5) {
					addPlayerOnExistingTable(this, table);
				}else
					choice = "TABLE_FULL";
				try {
					clientHandler.output.writeObject(new TableID(table.getTableId()));
					clientHandler.output.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}else {
				choice = "TABLE_FALSE";
				try {
					clientHandler.output.writeObject(choice);
				} catch (IOException e) {}
				TextWindow.println("[SERVER] >> Bord med id: " + tableId + " finns ej.");
			}
		}
		
		/**
		 * The actual adding of a player to a table
		 * @param clientHandler - the user/client who wants to join
		 * @param table - the table in question
		 */
		public synchronized void addPlayerOnExistingTable(ClientHandler clientHandler, Table table) {
			User user = UserHandler.getUser(clientHandler);
			Player player = new Player(user.getUsername());
			table.addPlayer(player);
			table.addClient(this);
			table.addClientAndPlayer(this, player);
			TextWindow.println("[SERVER] >> \"" + player.getUsername() + "\" tillagd på Table " + table.getTableId());
			ArrayList<Player> playerList = playersOnTable.get(table);
			playerList.add(player);
			ArrayList<ClientHandler> clientList = clientsOnTable.get(table);
			clientList.add(clientHandler);
			clientAndTable.put(clientHandler, table);
			updateList(clientList, playerList);
		}
		
		/**
		 * Used when a player wants to join any table
		 * @param clientHandler - the user/client who wants to join
		 */
		public void addPlayerOnRandomTable(ClientHandler clientHandler) {
			String choice = "";
			User user = UserHandler.getUser(clientHandler);
			Player player = new Player(user.getUsername());
			int numberOfTables = activeTables2.size();
			Random rand = new Random();
			int randomTable = rand.nextInt(numberOfTables);
			Table table = activeTables2.get(randomTable);
			ArrayList<Player> playerList = null;
			ArrayList<ClientHandler> clientList = null;
			if(!table.getPrivateStatus() && (table.getPlayerList().size() < 5)) {
				TextWindow.println("\"" + player.getUsername() + "\" tillagd på Table " + table.getTableId());
				playerList = playersOnTable.get(table);
				playerList.add(player);
				clientList = clientsOnTable.get(table);
				clientList.add(clientHandler);
				clientAndTable.put(clientHandler, table);
				table.addPlayer(player);
				table.addClient(this);
				table.addClientAndPlayer(this, player);
				choice = "RANDOM_TRUE";
				try {
					clientHandler.output.writeObject(choice);
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					clientHandler.output.writeObject(new TableID(table.getTableId()));
					clientHandler.output.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}else {
				choice = "RANDOM_FALSE";
				try {
					clientHandler.output.writeObject(choice);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			updateList(clientList, playerList);
//			return choice;
		}

		/**
		 * Makes sure noone can join a table that doesn't exist
		 * @param tableId
		 * @return
		 */
		public boolean doesTableExist(int tableId) {
			return activeTables2.containsKey(tableId);
		}

		/**
		 * Used to convey the choice a player made to the table
		 * @param playerChoice - the choice he made
		 * @param clientHandler - the client who made it
		 */
		public void makePlayerChoice(PlayerChoice playerChoice, ClientHandler clientHandler) {
			Table table = clientAndTable.get(clientHandler);
			HashMap<ClientHandler, Player> clientAndPlayer = table.getPlayerAndClient();
			Player player = clientAndPlayer.get(clientHandler);
			player.setPlayerChoice(playerChoice);
			TextWindow.println("[SERVER] >> " + player.getUsername() + " har gjort ett val: " + playerChoice.getChoice());
		}

		/**
		 * Used to update the names of the players around the table
		 * @param clientList - the list of clients who should receive it
		 * @param playerList - the actual list of players who are being displayed
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

		/**
		 * Used to update a client of what is going on on the table
		 * @param playerList
		 */
		public void updateTableInformation(ArrayList<Player> playerList) {
			try {
				output.writeObject(playerList);
				output.flush();
			}catch(IOException ex) {
				ex.printStackTrace();
			}
		}
	}


	/**
	 * HashMap containing all the active users/clients
	 * @author rasmusoberg
	 *
	 */
	private static class UserHandler {
		private static HashMap<ClientHandler, User> activeUsers = new HashMap<>();

		/**
		 * Used to add a newly registered user
		 * @param user - the user
		 * @param clientHandler - the client
		 */
		public synchronized static void newUserConnect(User user, ClientHandler clientHandler) {
			activeUsers.put(clientHandler, user);
			TextWindow.println("[NEW USER] >> \"" + user.getUsername() + "\" är nu registrerad.");
		}

		/**
		 * Used to register a newly logged on user
		 * @param user 
		 * @param clientHandler
		 */
		public synchronized static void addNewActiveUser(User user, ClientHandler clientHandler) {
			activeUsers.put(clientHandler, user);
			TextWindow.println("[NEW LOGIN] >> \"" + user.getUsername() + "\" är aktiv.");
		}

		public synchronized static HashMap<ClientHandler, User> getActiveUsers(){
			return activeUsers;
		}

		public synchronized static User getUser(ClientHandler clientHandler) {
			return activeUsers.get(clientHandler);
		}

		/**
		 * Used when a user logs off to remove from the Hashmap
		 * @param clientHandler - the client which logged off
		 */
		public synchronized static void removeActiveUser(ClientHandler clientHandler) {
			activeUsers.remove(clientHandler, getUser(clientHandler));
			TextWindow.println("[NEW LOGOUT] >> " + getUser(clientHandler).getUsername() + " är ej längre aktiv.");
		}
	}
}


