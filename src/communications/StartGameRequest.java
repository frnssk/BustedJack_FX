package communications;

import java.io.Serializable;

public class StartGameRequest implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int i = 0;
	
	public StartGameRequest(int i) {
		this.i = i;
	}

}
