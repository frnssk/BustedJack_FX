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
	private UserHandler userHandler;
	private ArrayList<Table> activeTables = new ArrayList<>();
	private HashMap<Integer, Table> activeTables2 = new HashMap<>();
	private int tableIdCounter;

	//	private LinkedList<Callback> listeners = new LinkedList<>();

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
//					System.out.println(user.getUsername());
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
	 * Called every time a new user i registered, to keep the offline-list of users up to date
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
	 * Used when a user tries to join a table
	 * makes sure a user can't join a table that doesn't exist
	 */
	public boolean doesTableExist(int tableId) {
		return activeTables2.containsKey(tableId);
	}
	
	/*
	 * Sets a unique ID to specific table
	 */
	public synchronized void setTableId(Table table) {
		table.setTableId(tableIdCounter);
		activeTables2.put(tableIdCounter, table);
		tableIdCounter++;
		activeTables.add(table);
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
		private User user;
		private Object obj;
		private boolean isOnline = true;
		//		private UserHandler userHandler;

		public ClientHandler(Socket socket) throws IOException {
			this.socket = socket;
			start();
			TextWindow.println("ClientHandler started");
		}

		/*
		 * Never used
		 */
		public void updateActiveUsers(LinkedList<User> activeUsers) {
			try {
				output.writeObject(activeUsers);
				output.flush();
			}catch(IOException ioException) {
				ioException.printStackTrace();
			}
		}


		/*
		 * Checks whether or not a user is registered
		 */
		public boolean isUserRegistered(String name) {
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
		public boolean passwordMatchUser(String username, char[] password) {
			char[] array = userPasswords.get(username);
			return Arrays.equals(array, password);
		}

		/*
		 * Checks whether or not a username is already in use
		 * Used to make sure a new user doesn't take an existing users name
		 */
		public boolean checkUsernameAvailability(String name) {
			for(int i = 0; i < registeredUsers.size(); i++) {
				String compareName = registeredUsers.get(i).getUsername();
//				User compare = registeredUsers.get(i);
//				String compareName = compare.getUsername();
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
			if(password.length < 6 || password.length >= 12) {
				return false;
			}else{
				return true;
			}
		}


		/*
		 * Keeps on running as long as the client is still connected
		 * Reads objects from the client and depending on the type of object the server acts accordingly
		 */
		public void run() {
			try(ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
					ObjectInputStream input = new ObjectInputStream(socket.getInputStream())){
				while(isOnline) {
					try {
						obj = input.readObject();
						String choice = "";

						/**
						 * This if-statement is used to register new users, so they
						 * are able to login and play the game
						 */
						if(obj instanceof RegisterRequest) {
							RegisterRequest registerRequest = (RegisterRequest)obj;
							if(checkUsernameAvailability(registerRequest.getUsername())) {
								TextWindow.println(registerRequest.getUsername() + " är ledigt."); //Assistance
								if(isPasswordOkay((registerRequest.getPassword()))) {
									TextWindow.println((registerRequest.getUsername()) + " har angett ett godkänt lösenord."); //Assistance
									User temporary = new User(registerRequest.getUsername());
									temporary.setPassword(registerRequest.getPassword());
									//registeredUsers.add(temporary); //currently used elsewhere (in the uspdateUserDatabase())
									userPasswords.put(registerRequest.getUsername(), registerRequest.getPassword());
									updateUserDatabase(temporary);
//									TextWindow.println("User-objekt skapat för " + registerRequest.getUsername());//Assistance

									//Adds the user and client to the UserHandler-HashMap
									UserHandler.newUserConnect(temporary, this); 
//									TextWindow.println(temporary.getUsername() + " tillagd i UserHandler.");

									choice = "USER_TRUE";
								}else { 
									TextWindow.println(registerRequest.getUsername() + " har angett ett icke godkänt lösenord."); //Assistance
									choice = "PASSWORD_FALSE";
								}

							}else {
								choice = "USERNAME_FALSE";
							}
							output.writeObject(choice);
							output.flush();
						}

						/*
						 * Logins user
						 */
						else if(obj instanceof LoginRequest) {
							choice = "";
							LoginRequest loginRequest = (LoginRequest)obj;
							if(isUserRegistered(loginRequest.getUsername())) {
								TextWindow.println(loginRequest.getUsername() + " finns."); //Assistance
								if(passwordMatchUser(loginRequest.getUsername(), loginRequest.getPassword())){
									choice = "LOGIN_SUCCES";
//									TextWindow.println(loginRequest.getUsername() + " är inloggad."); //Assistance
									output.writeObject(choice);
									output.flush();

									//Adds the user and client to the UserHandler-HashMap
									User user = getUser(loginRequest.getUsername());
									UserHandler.addNewActiveUser(user, this);
								}else {
									choice = "LOGIN_FAIL";
									output.writeObject(choice);
									output.flush();
									TextWindow.println(loginRequest.getUsername() + " kan inte sitt lösenord HAHAHA"); //Assistance
								}
							}
						}
						
						/*
						 * Disconnects the client, and stops the current clienthandler-loop
						 */
						else if(obj instanceof LogOutRequest) {
							LogOutRequest logout = (LogOutRequest)obj;
							isOnline = false;
//							TextWindow.println("Client disconnected.");
							String name = logout.getUserName();
							User user = getUser(name);
							UserHandler.removeActiveUser(this);
						}

						/*
						 * Take the information stored in the GameInfo-object, extracts it and creates a new Table-object
						 */
						else if(obj instanceof GameInfo) {
							GameInfo gameInfo = (GameInfo)obj;
							Table table = new Table(gameInfo.getTime(), gameInfo.getRounds(), gameInfo.getBalance(), gameInfo.getMinBet());
							setTableId(table);
//							table.addPlayer(new Player(UserHandler.getUser(this).getUsername()));
							User user = UserHandler.getUser(this);
							Player player = new Player(user.getUsername());
							table.addPlayer(player);
							table.addPlayerAndClient(player, this); //Assistance/testing
							TextWindow.println(player.getUsername() + " tillagd på Table " + table.getTableId());
							//send the tableId to the client
						}
						
						/*
						 * Adds players to a table, if it exists
						 */
						else if(obj instanceof Integer) {
							int tableId = (Integer)obj;
							if(doesTableExist(tableId)) {
								TextWindow.println("Bord med id: " + tableId + " finns.");
								choice = "TABLE_TRUE";
								Table table = activeTables2.get(tableId);
								if(table.getNumberOfPlayers() < 5 && !table.checkTableStarted()) {
									User user = UserHandler.getUser(this);
									Player player = new Player(user.getUsername());
									table.addPlayer(player);
									table.addPlayerAndClient(player, this); //Assistance/testing
									TextWindow.println(player.getUsername() + " tillagd på Table " + table.getTableId());
								}
							}else {
								choice = "TABLE_FALSE";
								TextWindow.println("Bord med id: " + tableId + " finns ej.");
							}
							output.writeObject(choice);
							output.flush();
							TextWindow.println("GREAT SUCCES, TWO THUMBS UP - BORAT STYLE");
						}
						
						else if(obj instanceof PlayerChoice) {
							PlayerChoice playChoice = (PlayerChoice)obj;
							TextWindow.println("NÅGON HAR TRYCKT" + playChoice.getChoice());
						}

					} catch (ClassNotFoundException | IOException e) {
						e.printStackTrace();
						break;
					}
				}
				TextWindow.println("DEAD");

			}catch(Exception ioException) {
				ioException.printStackTrace();
			}
		}

	}

	/*
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
			//			updateActiveUsers();
		}

		//Used to get which user is connected to a specific client / clienthandler
		public synchronized static User getUser(ClientHandler clientHandler) {
			return activeUsers.get(clientHandler);
		}

		//Used to remove a user when he disconnects
		public synchronized static void removeActiveUser(ClientHandler clientHandler) {
			activeUsers.remove(clientHandler, getUser(clientHandler));
			TextWindow.println("NEW LOGOUT = " + getUser(clientHandler).getUsername() + " är ej längre aktiv.");
			//updateActiveUsers();
		}

		//returns whether or not a user is online
//		public synchronized static boolean isUserOnline(User user) {
//			return activeUsers.containsKey(user);
//		}

		/*
		 * returns the clienthandler connected to a specific user
		 */
		//		public synchronized static ClientHandler getClientHandler(User user) {
		//			return activeUsers.get(user);
		//		}

		public void updateActiveUsers() {

		}

	}
}


