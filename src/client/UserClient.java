package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import application.CreateNewUserController;
import application.GameController;
import application.JoinTableController;
import application.LogInController;
import application.Main;
import communications.GameInfo;
import communications.LogOutRequest;
import communications.LoginRequest;
import communications.PlayerChoice;
import communications.RandomTableRequest;
import communications.RegisterRequest;
import communications.StartGameRequest;
import communications.StartingInformation;
import communications.TableID;
import communications.UpdateClientInformation;
import communications.UpdateUI;
import javafx.application.Platform;
import resources.DealerHand;
import resources.Player;
import resources.Table;
import resources.User;
import server.Server.ClientHandler;

/**
 *  Class responsible for all connection to the server, from the user side.  
 *  Contains one inner class for handling incoming data from the server 
 *  @author Isak Eklund
 */
public class UserClient {
	private Socket socket;
	private UserController controller;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private String ip;
	private int port;
	private User user;
	private boolean receiving = true;
	private Connection connection;
	private Object obj;
	private Main mainApp;
	private GameController gameController;
	private JoinTableController joinTableController;
	private LogInController logInController;
	private CreateNewUserController createNewUserController;

	/**
	 * Constructs the UserCLient object and connects to server on give IP and port
	 * @param ip - What IP address to connect to
	 * @param port - what port on the given IP address to connect to
	 * @throws IOException
	 */
	public UserClient(String ip, int port) throws IOException{
		this.ip = ip;
		this.port = port;
//				try {
//					socket = new Socket(ip, port);
//					output = new ObjectOutputStream(socket.getOutputStream());
//					input = new ObjectInputStream(socket.getInputStream());
//				}catch(IOException ioException) {
//					ioException.printStackTrace();
//				}
//				if(connection == null) {
//					connection = new Connection(socket);
//					connection.start();
//				}
//		connect();

	}
	
	public void setMain(Main main) {
		this.mainApp = main;
	}
	
	public void setGameController(GameController controller) {
		this.gameController = controller;
	}
	
	public void setJoinTableController(JoinTableController controller) {
		this.joinTableController = controller;
	}
	
	public void setLogInController(LogInController controller) {
		this.logInController = controller;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setUserController(UserController userController) {
		this.controller = userController;
	}
	
	public void setCreateNewUserController(CreateNewUserController controller) {
		this.createNewUserController = controller;
	}

	/**
	 * Connects to the server and sends a User object
	 * @param user - Tells the server what user is connecting 
	 * @throws IOException
	 */
	public void connect() {
		if(connection == null) {
			try {
				socket = new Socket(ip, port);
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				connection = new Connection(socket);
				connection.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
			receiving = true;
		}
		//		try {
		//			socket = new Socket(ip, port);
		//			output = new ObjectOutputStream(socket.getOutputStream());
		//			input = new ObjectInputStream(socket.getInputStream());
		//			receiving = true;
		//		}catch(IOException ioException) {
		//			ioException.printStackTrace();
		//		}
		//		if(connection == null) {
		//			connection = new Connection(socket);
		//			connection.start();
		//		}
	}

	public void disconnect() {
		try {
			receiving = false;
			connection = null;
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendLoginRequest(LoginRequest request) {
		try {
			connect();
			output.writeObject(request);
			output.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendRegisterRequest(RegisterRequest request) {
		try {
			connect();
			output.writeObject(request);
			output.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendLogOutRequest(LogOutRequest logOutRequest) {
		try {
			output.writeObject(logOutRequest);
			output.flush();
			disconnect();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendGameInfo(GameInfo gameInfo) {
		try {
			output.writeObject(gameInfo);
			output.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void checkTableId(int tableId) {
		try {
			output.writeObject(tableId);
			output.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendRandomTableRequest() {
		try {
			output.writeObject(new RandomTableRequest());
			output.flush();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendPlayerChoice(PlayerChoice choice) {
		try {
			output.writeObject(choice);
			output.flush();
			output.reset();
		}catch(IOException ioException) {}
	}
	
	public void sendStartGame(StartGameRequest request) {
		try {
			System.out.println("[CLIENT] == Klient mottagit StartGameRequest, skickar till server.");
			output.writeObject(request);
			output.flush();
			System.out.println("[CLIENT] == Skickat.");
		}catch(IOException ioException) {}
	}


	/**
	 * Inner class that let the client listen for incoming data from the server
	 * Runs on a separate thread 
	 * @author Isak Eklund
	 *
	 */
	private class Connection extends Thread {

		public Connection(Socket socket) throws IOException {
			output = new ObjectOutputStream(socket.getOutputStream());
			input = new ObjectInputStream(socket.getInputStream());
		}

		public void run() {
			while(receiving) {
				try {
					obj = input.readObject();

					//For checking user name availability
					if(obj instanceof String) {
						String available = (String) obj; //byta namn? används till mer än att kolla namn
						if(available.equals("LOGIN_SUCCES") || available.equals("LOGIN_FAIL") || available.equals("LOGIN_NOT_EXIST")) {
							System.out.println("[CLIENT] == " + available);
							logInController.checkLogIn(available);
						} else if(available.equals("USERNAME_FALSE") || available.equals("PASSWORD_FALSE") || available.equals("USER_TRUE")) {
							System.out.println("[CLIENT] == " + available);
							createNewUserController.checkRequest(available);
						} else if(available.equals("TABLE_TRUE") || available.equals("TABLE_FALSE") || available.equals("RANDOM_FALSE") || available.equals("RANDOM_TRUE")) {
							System.out.println("[CLIENT] == " + available);
							joinTableController.checkTableId(available);
						} 
					}
					else if(obj instanceof ArrayList<?>) {
						ArrayList<Player> playerList = (ArrayList)obj;
						System.out.println("[CLIENT] == Antal spelare i PlayerList = " + playerList.size());
						
						Platform.runLater(() -> {
								gameController.updatePlayerList(playerList);			
						});
						System.out.println("[CLIENT] == Lista mottagen, skickad till controller. Antal = " + playerList.size());
						try {
							Thread.sleep(500);
						}catch(InterruptedException ex) {}
					}
					else if(obj instanceof HashMap<?,?>) {
						HashMap<ClientHandler, User> list = new HashMap<>();
						System.out.println(list.toString());
					}
					else if(obj instanceof StartingInformation) {
						System.out.println("[CLIENT] == Recieved Startin Information");
						StartingInformation info = (StartingInformation) obj;
						int rounds = info.getNumberOfRounds();
						int time = info.getNumberOfMinutes();
						int minBet = info.getMinimumBet();
						int balance = info.getStartingMoney();
						Platform.runLater(() -> {
							gameController.setStartingInformation(rounds, time, minBet, balance);
						});
					}
					else if(obj instanceof TableID) {
						TableID tableID = (TableID)obj;
						int tableIdToShow = tableID.getID();
						Platform.runLater(() -> {
							gameController.setTableId(tableIdToShow);			
						});
						System.out.println("[CLIENT] == TABLE ID TO JOIN ON = " + tableIdToShow);
					}
					else if(obj instanceof UpdateClientInformation) {
						System.out.println("[CLIENT] == UpdateRoundInfo mottagen");
						UpdateClientInformation updateClientInformation = (UpdateClientInformation)obj;
						ArrayList<Player> playerList = 	updateClientInformation.getPlayerList();
						DealerHand dealer = updateClientInformation.getDealerHand();
						Platform.runLater(() -> {
							gameController.updateRoundInformation(playerList, dealer);
							System.out.println("[CLIENT] == UpdateRoundInfo skickad till gameController");
						});
					}
					else if(obj instanceof UpdateUI) {
						System.out.println("[CLIENT] == UpdateUI mottagen");
						UpdateUI update = (UpdateUI) obj;
						Platform.runLater(() -> {
							gameController.updateRoundMessage(update.getText());
							System.out.println("[CLIENT] == UpdateRoundInfo skickad till gameController");
						});
					}
				}catch(IOException | ClassNotFoundException e) {
//					e.printStackTrace();
				}
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}



}