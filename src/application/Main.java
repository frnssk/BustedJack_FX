package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {

	public void start(Stage primaryStage) throws Exception {
		
		Parent root = FXMLLoader.load(getClass().getResource("gameTable.fxml"));
		root = FXMLLoader.load(getClass().getResource("images/Spelbord_2.1.png"));
		primaryStage.setTitle("BustedJack");
		Scene scene = new Scene(root,1000,600);
//		String css = MainBlackJack.class.getResource("Demos.css").toExternalForm();
//		scene.getStylesheets().add(css);
		
		primaryStage.setScene(scene);
		primaryStage.show();
			
		

	}
	public static void main(String[] args) {
		launch(args);
	}
}


