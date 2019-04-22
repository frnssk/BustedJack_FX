package resources;

import java.io.Serializable;

public class User implements Serializable {

	private static final long serialVersionUID = -1211450706513529840L;
	private String username;
	private char[] password;
	private int rating;
	private int balance;
	private String title;
	
	public User(String name) {
		this.username = name;
		this.rating = 1200;
		this.balance = 0;
		this.title = null;
	}
	
	public void setPassword(char[] password) {
		this.password = password;
	}
	
	public char[] getPassword() {
		return password;
	}
	
	public void setRating(int rating) {
		this.rating = rating;
	}
	
	public void setBalance(int balance) {
		this.balance = balance;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getUsername() {
		return username;
	}
	
}
