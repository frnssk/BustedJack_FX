package communications;

import java.io.Serializable;

/**
 * Stores information sent from the server to update the ui label on what's happening in the game
 * @author Isak Eklund
 *
 */
public class UpdateUI implements Serializable {

	private static final long serialVersionUID = -1341196767587622481L;
	private String message;
	
	/**
	 * Constructor takes a string with the given message
	 * @param message - message to show in the UI during a game
	 */
	public UpdateUI(String message) {
		this.message = message;
	}
	
	public String getText() {
		return message;
	}

}
