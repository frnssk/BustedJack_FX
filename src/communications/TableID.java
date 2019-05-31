package communications;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class TableID implements Serializable{
	private static final long serialVersionUID = 1L;
	private int id;
	
	public TableID(int id) {
		this.id = id;
	}
	
	public int getID() {
		return id;
	}

}
