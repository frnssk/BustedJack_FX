package communications;

import java.io.Serializable;
import java.util.ArrayList;

import resources.DealerHand;
import resources.Player;

/**
 * Object including all info for updating each clients UI
 * @author Simon Lilja
 *
 */

public class UpdateClientInformation implements Serializable {

	private static final long serialVersionUID = 1L;
	private ArrayList<Player> playerlist;
	private DealerHand dealerhand;

	public UpdateClientInformation(ArrayList<Player> playerlist, DealerHand dealerhand) {
		this.playerlist=playerlist;
		this.dealerhand=dealerhand;
	}
	public ArrayList<Player> getPlayerList() {
		return this.playerlist;
	}
	public DealerHand getDealerHand() {
		return this.dealerhand;
	}

}
