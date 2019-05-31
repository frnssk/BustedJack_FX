package server;

import java.awt.BorderLayout;

import javax.swing.*;
import resources.Callback;

/**
 * Makes the server prints viewable in a separate window
 * @author Isak Eklund
 * @author Rasmus Öberg
 *
 */
public class ServerUI extends JPanel implements Callback {
	private Server server;
	private JTextArea taLiveUpdate = new JTextArea();
	private JScrollPane spLiveLog = new JScrollPane(taLiveUpdate);
	private JPanel pane = new JPanel();
	
	public ServerUI(Server server) {
		this.server = server;
		
		pane.setLayout(new BorderLayout());
		taLiveUpdate.setEditable(false);
		spLiveLog.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		pane.add(taLiveUpdate, BorderLayout.CENTER);
	}
	
	public void logActivity(String string) {
		taLiveUpdate.append(string + "\n");
	}

}
