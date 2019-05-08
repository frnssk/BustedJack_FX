package client;

import java.awt.Dimension;
import java.io.IOException;

import javax.swing.JFrame;

/*
 * This class will create an instance of the client, controller and user interface
 * Contains main method for the client side of the application
 * Is used to start the program
 */

public class StartClient {
	public static void main(String[] args) throws IOException {
		UserClient client = new UserClient("localhost", 1200); //IP and port are hard coded for testing
		UserController controller = new UserController(client);
		UserInterface gui = new UserInterface(controller);
		
		JFrame frame = new JFrame();
		frame.setPreferredSize(new Dimension(1000,600));
		frame.setResizable(false);
		frame.setVisible(true);
		frame.pack();
		frame.add(gui);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
