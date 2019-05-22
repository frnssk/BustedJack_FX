package communications;

import java.io.Serializable;

public class UpdateUI implements Serializable {

	private static final long serialVersionUID = -1341196767587622481L;
	private String message;
	
	public UpdateUI(String message) {
		this.message = message;
	}
	
	public String getText() {
		return message;
	}

}
