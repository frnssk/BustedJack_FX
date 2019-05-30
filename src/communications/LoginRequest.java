package communications;

import java.io.Serializable;

/**
 * Gets the username and passwod so the server can check if login is successful 
 * @author Isak Eklund
 *
 */
public class LoginRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6571868936731252455L;
	private String username;
	private char[] password;
	
	/**
	 * Constructor takes all relevant parameters
	 * @param username - The entered username
	 * @param password - The entered password
	 */
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
