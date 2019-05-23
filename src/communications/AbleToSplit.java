package communications;

import java.io.Serializable;

public class AbleToSplit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean ableToSplit;
	
	public AbleToSplit(boolean ableToSplit) {
		this.setAbleToSplit(ableToSplit);
	}

	public boolean getAbleToSplit() {
		return ableToSplit;
	}

	public void setAbleToSplit(boolean ableToSplit) {
		this.ableToSplit = ableToSplit;
	}

}
