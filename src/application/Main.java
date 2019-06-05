package application;

import java.io.IOException;

import client.UserClient;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene; 
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * The main class for controlling JavaFX and FXML files.
 * Used to create a main stage and switching scene depending on user action
 * @author Isak Eklund
 * @author Christoffer Palvin
 */
public class Main extends Application {
	private UserClient client;
	private Stage primaryStage;
	private AnchorPane mainLayout;
	private String username;

	/**
	 * Creates the primary stage and connects the client to the server. 
	 * Shows the start view in the UI
	 */
	public void start(Stage primaryStage) throws Exception {

		client = new UserClient("10.2.6.91", 1200);
		this.primaryStage = primaryStage;
		showStartView();
	}

	/**
	 * Sets the StartScreen.fxml as current UI
	 * @throws IOException
	 */
	public void showStartView() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("StartScreen.fxml"));
		mainLayout = loader.load();

		StartScreenController controller = loader.getController();
		controller.setMain(this);

		Scene scene = new Scene(mainLayout, 1000, 600);
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
	}

	/**
	 * Sets the LogIn.fxml as current UI
	 * @throws IOException
	 */
	public void showLoginView() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("LogIn.fxml"));
		mainLayout = loader.load();

		LogInController controller = loader.getController();
		controller.setClient(client);
		controller.setMain(this);
		client.setLogInController(controller);

		Scene scene = new Scene(mainLayout, 1000, 600);
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
	}

	/**
	 * Sets the CreateNewUser.fxml as current UI
	 * @throws IOException
	 */
	public void showCreateNewUser() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("CreateNewUser.fxml"));
		mainLayout = loader.load();

		CreateNewUserController controller = loader.getController();
		controller.setClient(client);
		controller.setMain(this);
		client.setCreateNewUserController(controller);

		Scene scene = new Scene(mainLayout, 1000, 600);
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
	}

	/**
	 * Sets the MainMenu.fxml as current UI
	 * @throws IOException
	 */
	public void showMainMenu() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("MainMenu.fxml"));
		mainLayout = loader.load();

		MainMenuController controller = loader.getController();
		controller.setClient(client);
		controller.setMain(this);
		controller.setWelcome(username);

		Scene scene = new Scene(mainLayout, 1000, 600);
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
	}

	/**
	 * Sets the JoinTable.fxml as current UI
	 * @throws IOException
	 */
	public void showJoinTable() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("JoinTable.fxml"));
		mainLayout = loader.load();

		JoinTableController controller = loader.getController();
		controller.setClient(client);
		controller.setMain(this);
		client.setJoinTableController(controller);

		Scene scene = new Scene(mainLayout, 1000, 600);
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
	}

	/**
	 * Sets the CreateTableSceen.fxml as current UI
	 * @throws IOException
	 */
	public void showCreateTabel() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("CreateTableScreen.fxml"));
		mainLayout = loader.load();

		CreateTableController controller = loader.getController();
		controller.setClient(client);
		controller.setMain(this);

		Scene scen = new Scene(mainLayout, 1000, 600);
		primaryStage.setScene(scen);
		primaryStage.setResizable(false);
		primaryStage.show();
	}

	/**
	 * Sets the ProfileScreen.fxml as current UI
	 * @throws IOException
	 */
	public void showProfile() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("ProfileScreen.fxml"));
		mainLayout = loader.load();

		ProfileScreenController controller = loader.getController();
		controller.setMain(this);

		Scene scene = new Scene(mainLayout, 1000, 600);
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
	}

	/**
	 * Sets the GameTable.fxml as current UI
	 * @throws IOException
	 */
	public void showGame() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("GameTable.fxml"));
		mainLayout = loader.load();

		GameController controller = loader.getController();

		controller.setClient(client);
		controller.setMain(this);
		client.setGameController(controller);

		Scene scene = new Scene(mainLayout, 1200, 700);
		primaryStage.setScene(scene);
		primaryStage.setResizable(true);
		primaryStage.show();
	}

	/**
	 * Sets the GameOver.fxml as current UI
	 * @throws IOException
	 */
	public void showGameOver() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("GameOver.fxml"));
		mainLayout = loader.load();

		GameOverController controller = loader.getController();

		controller.setMain(this);

		Scene scene = new Scene(mainLayout, 1000, 600);
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
	}

	/**
	 * Used to show error message 
	 * @param title - Title from the error message
	 * @param message - The actual massage to show the user 
	 * @throws IOException
	 */
	public void showAlert(String title, String message) throws IOException {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.initOwner(getPrimaryStage());
		alert.setTitle(title);
		alert.setHeaderText("");
		alert.setContentText(message);
		alert.showAndWait();
	}

	/**
	 * 
	 * @return - The primary stage
	 */
	public Stage getPrimaryStage() {
		return primaryStage;
	}

	/**
	 * Sets current username
	 * @param username
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * 
	 * @return - Current username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Main method for starting the UI
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}
}


