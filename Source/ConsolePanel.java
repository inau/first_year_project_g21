import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.peer.LightweightPeer;

import javax.swing.*;

public class ConsolePanel extends JPanel{
	
	private JPanel flow;
	private JTextArea console;
	private JTextField topConsole;
	
	public ConsolePanel() {
	}
	
	/**
	 * Updates the bottom console field with the input string
	 * @param input - string to replace with
	 */
	public void updateBotConsole(String input){
		console.setText(""+input);
	}
	
	/**
	 * Builds a panel with a predefined design
	 * @return a panel with a predefined design
	 */
	public JPanel build() {
		flow = new JPanel();
		flow.setLayout(new GridLayout());
		console = new JTextArea("",3,1);
		console.setEditable(false);
		console.setBackground(flow.getBackground());
		console.setDisabledTextColor(Color.black);
		console.setBorder(BorderFactory.createTitledBorder("Program State"));

		flow.add(console);
				
		return flow;
	}
}
		