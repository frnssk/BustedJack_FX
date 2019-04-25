package communications;

import java.util.ArrayList;
import java.util.Random;

public class TableID {
	private int id = 0;
//	private ArrayList<Integer> listOfID = new ArrayList<>();
	
	public TableID() {
		this.id = getID();
	}
	
	private int getID() {
		return id++;
	}

}
