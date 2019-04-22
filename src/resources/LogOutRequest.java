package resources;

import java.io.Serializable;

public class LogOutRequest implements Serializable {
	private String username;
	
	public LogOutRequest(String name) {
		this.username = name;
	}
	
	public String getUserName() {
		return username;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 2900146325645039838L;

}
