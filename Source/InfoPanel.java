import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.ParseException;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.MaskFormatter;

/**
 * A class used to build the east side of the Gui - displaying the information
 * of the clicked reservation on the center canvas
 * @author Ivan
 */
public class InfoPanel extends JPanel {

	private JTextField 	custName = new JTextField("Click reservations"),
						resId = new JTextField("to get data"),
						startDate = new JTextField("to update"), endDate = new JTextField("the reservation"),
						carType = new JTextField("fields"), from, to;
	int customer_id = 0;
	boolean maintenance;
	JDialog resEditWin;
	private int lastResId = 0;
	String carInput = "1";
	private JButton editInfo = new JButton("Edit Reservation");
	private JButton delInfo = new JButton("Delete Reservation");
	public JPanel flowPanel = new JPanel();
	Controller c;
	CalendarFrame cf;
	ConsolePanel cp;
	
	/**
	 * Constructor for the panel
	 */
	public InfoPanel(Controller c, CalendarFrame cf, ConsolePanel cp){
		this.c = c;
		this.cf = cf;
		this.cp = cp;
	}
	
	/**
	 * A build method that generates a panel
	 * @return returns a JPanel with a predefined design
	 */
	public JPanel build(){
		//Setups the fields to be non editable
		setupFields(custName);
		setupFields(resId);
		setupFields(startDate);
		setupFields(endDate);
		setupFields(carType);
		//Builds panels to smoothen the look
		flowPanel.setLayout(new FlowLayout());
		JPanel fieldPanel = new JPanel();
		fieldPanel.setLayout(new GridLayout(0,2));
		JPanel splitPanel = new JPanel();
		splitPanel.setLayout(new BorderLayout(2,1));
		splitPanel.add(fieldPanel, BorderLayout.NORTH);
		//adds components to the panels
		JComponent[] fields = {	new JLabel("Customer Name: "), custName,
								new JLabel("Reservation Id: "), resId,
								new JLabel("Reservation Start: "), startDate,
								new JLabel("Reservation End: "), endDate,
								new JLabel("Car Type: "), carType};
		JPanel grid = new JPanel();
		grid.setLayout(new GridLayout(2,0));
		JPanel flow = new JPanel();
		//adds buttons, and more smoothing
		grid.add(editInfo);
		grid.add(delInfo);
		flow.setLayout(new FlowLayout());
		flow.add(grid);
		splitPanel.add(flow, BorderLayout.SOUTH);
		//adding actionlisteners to buttons
		editInfo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				cp.updateBotConsole("Edit reservation id "+lastResId);
				try {
					editReservation();
					}
					catch (ParseException exn) {
					cp.updateBotConsole("Error editting reservation");
					}
				}
				});
		
		delInfo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				if(lastResId != 0) {
					deleteReservation(lastResId);
					cf.populateReservations();
				}
				else {
					cp.updateBotConsole("no id stored for deletion yet");
				}
			}
			});	
		
		for(int i = 0; i < fields.length ; i++){
			fieldPanel.add(fields[i]);
			}
			flowPanel.add(splitPanel);
			flowPanel.setBorder(BorderFactory.createTitledBorder("Reservation Information:"));
		return flowPanel;
	}
	
	/**
	 * A method used to make fields non-editable
	 * @param jtf is the textfield to setup
	 */
	private void setupFields(JTextField jtf) {
		jtf.setEditable(false);
		jtf.setBackground(Color.white);
	}
	
	/**
	 * Creates a dialogue window with input fields for a new reservation
	 */
	private void editReservation() throws ParseException { 
		JComboBox car = new JComboBox<>();
   		for(int i = 1; i < 10; i++)
   			car.addItem(""+i);
   		 MaskFormatter mf1 = new MaskFormatter("########");
   		 from = new JFormattedTextField(mf1);
         to = new JFormattedTextField(mf1);
         JCheckBox change = new JCheckBox("Maintenance");
         JButton apply = new JButton("Apply Changes");
         car.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					carInput = e.getItem().toString();
				}
         });
         
         final JComponent[] inputs = new JComponent[]
         {new JLabel("Car: "),car,
          new JLabel("Reserved from: "), from,
          new JLabel("Reserved to: "), to,
          change, apply
         }; 
         
         resEditWin = new JDialog();
         JPanel buttonPanel = new JPanel();
         JPanel flow = new JPanel();
         flow.setLayout(new FlowLayout());
         buttonPanel.setLayout(new GridLayout(0,2));
         flow.add(buttonPanel);
         resEditWin.add(flow);
         for(int i = 0; i < inputs.length; i++) {
        	 buttonPanel.add(inputs[i]);
         }
         
         change.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
        		if(maintenance)
        		maintenance = true;
        		else
        		maintenance = false;
        	}
         	});
         
         
         resEditWin.setAlwaysOnTop(true);
         resEditWin.setVisible(true);
         resEditWin.setSize(250, 175);
         resEditWin.setLocation(300, 200);
         resEditWin.setResizable(false);
         
         apply.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
						finalizeReservation(lastResId, carInput, from, to, maintenance);	
						resEditWin.dispose();
						cf.repaint();
					}});
    }
	
	/**
	 * Finalizes a new reservation, with the given parameters
	 * @param res_id - the last id stored
	 * @param carInput - car id for the reservation
	 * @param from - date string of format ddmmyyyy
	 * @param to - date string of format ddmmyyyy
	 */
	public void finalizeReservation(int res_id, String carInput, JTextField from, JTextField to, boolean maintenance) {
		if(maintenance) {
		c.changeReservation(res_id,
							Integer.parseInt(carInput),
							from.getText(),
							to.getText(),
							customer_id);
		}
		else { c.changeReservation(res_id,
				Integer.parseInt(carInput),
				from.getText(),
				to.getText(),
				0);
		}
		cf.populateReservations();
	}
	
	/**
	 * deletes a reservation based on the input id
	 * @param id - id of reservation
	 */
	public void deleteReservation(int id) {
		c.deleteReservation(id);
	}
	
	/**
	 * Changes the info fields based on a reservation
	 * @param res - reservation to fetch data from
	 */
	public void setInfo(Reservation res){
		lastResId = res.getId();
		custName.setText(""+res.getCustomer());
		resId.setText(""+res.getId());
		startDate.setText(res.getReserved_from());
		endDate.setText(res.getReserved_to());
		carType.setText(""+res.getCar());
		customer_id = res.getCustomer();
	}

	/**
	 * Returns the preferred size of the panel
	 */
	public Dimension getPreferredSize() { 
	    return new Dimension(75, 150); 
	  }
}