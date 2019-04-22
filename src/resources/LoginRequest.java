package resources;

import java.io.Serializable;

public class LoginRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6571868936731252455L;
	private String username;
	private char[] password;
	
	public LoginRequest(String username, char[] password) {
		this.username = username;
		this.password = password;
	}
	
	public String getUsername() {
		return username;
	}
	
	public char[] getPassword() {
		return password;
	}

}
