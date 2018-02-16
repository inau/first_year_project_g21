import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class MonthPanel extends JComponent {
	String[] monthNames = {	"January", "February", "March", "April", "May",
							"June", "July", "August", "September", "Oktober",
							"November", "December"};
	
	String[] years = new String[25];
	JComboBox yearsCb;
	JComboBox monthsCb;
	
	JPanel monthPanel = new JPanel();
	
	/**
	 * constructor that adds the years to the combo box
	 */
	public MonthPanel(){
		for(int i = 0; i < 25 ; i++)
		years[i] = ""+(2011+i);
	}
	
	/**
	 * Builds the panel
	 * @return the predefined panel
	 */
	public JPanel build() {
			monthPanel.setLayout(new FlowLayout());
			JPanel buttonPanel = new JPanel();
			buttonPanel.setLayout(new GridLayout(0,1));
	
			yearsCb = new JComboBox(years);
			monthsCb = new JComboBox(monthNames);
		
			buttonPanel.add(yearsCb);
			buttonPanel.add(monthsCb);
	
			monthPanel.add(buttonPanel);
	
			monthPanel.setBorder(BorderFactory.createTitledBorder("Navigation:"));
			return monthPanel;
	}
}