package communications;

import java.io.Serializable;

/**
 * Holds info on a player that wants to log out
 * @author Isak Eklund
 *
 */
public class LogOutRequest implements Serializable {
	private String username;
	
	/**
	 * Constructor for the object. 
	 * @param name - String with the username that wants to log out
	 */
	public LogOutRequest(String name) {
		this.username = name;
	}
	
	public String getUserName() {
		return username;
	}

	private static final long serialVersionUID = 2900146325645039838L;

}
