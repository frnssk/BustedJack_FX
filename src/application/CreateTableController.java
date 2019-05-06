package application;

import client.UserClient;

public class CreateTableController {
	private static Main mainApp;
	private UserClient client;

	public void setMain(Main main) {
		mainApp = main;
	}

	public void setClient(UserClient client) {
		this.client = client;
	}
}
