package communications;

import java.io.Serializable;

public class RegisterRequest implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2001217030614121593L;
	private String username;
	private char[] password;
	
	public RegisterRequest(String username, char[] password) {
		this.username = username;
		this.password = password;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public void setPasswrod(char[] password) {
		this.password = password;
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public char[] getPassword() {
		return this.password;
	}

}