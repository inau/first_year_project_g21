import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.sql.*;
import java.text.ParseException;

/**
 * A class that initiates the program(main string)
 * aswell as all the sub components needed for the GUI.
 */

public class Gui extends JComponent {

	private static final double version = 1.3;
	private JFrame mainFrame;
	private ConsolePanel conPanel;
	private Controller controller;
	private CalendarFrame cFrame;
	private MonthPanel monthPanel;
	private InfoPanel infoPanel;
	private CustomerWindow cWindow;
	
	String carInput = "";
	JTextField from, to;
	JDialog resWindow;
	
	/**
	 * Main string
	 */
	public static void main(String[] args) {
		new Gui();
		}
	
	/**
	 * Constructor for the GUI class, calls the makeFrame method and
	 * initiates the corresponders.
	 */
	public Gui(){
		initiateVariables();
		makeFrame();
	}
	
	/**
	 * A method that initiates the components needed to run the program
	 * and its functions, if not successful terminates with an error message
	 */
	private void initiateVariables() {
		try {
			conPanel = new ConsolePanel();
			controller = new Controller(conPanel);
			cFrame = new CalendarFrame(controller, conPanel);
			infoPanel = new InfoPanel(controller, cFrame, conPanel);
			monthPanel = new MonthPanel();
			cWindow = new CustomerWindow(controller, conPanel);
		}
		catch (Exception ex) {
			JOptionPane.showMessageDialog(null,	"Can't initiate database connection\n" +
												"Please make sure you are connected to the internet\n" +
												"before running application", "Critical application error",
												JOptionPane.ERROR_MESSAGE);
		}
		
	}
	
	/**
	 * Creates the main window frame for the program
	 */
	private void makeFrame(){
		mainFrame = new JFrame("RentIT "+version);
		JPanel masterPanel = new JPanel();
		masterPanel.setLayout(new BorderLayout());
		
		//Building the quick access bar
		JPanel qaBarFlow = new JPanel();
		JPanel qaBar = new JPanel();
		qaBarFlow.setLayout(new FlowLayout());
		qaBar.setLayout(new BorderLayout());
		qaBar.add(qaBarFlow, BorderLayout.WEST);
		qaBar.setBorder(BorderFactory.createRaisedBevelBorder());
		
		JButton[] qaButtons = quickAccessBar();
		for(int i = 0; i < qaButtons.length; i++) {
			qaBarFlow.add(qaButtons[i]);
			//Adds action listeners to the 4 main buttons
			if(i == 3){qaButtons[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					conPanel.updateBotConsole("Search cars initiated");
					try {
						searchVehicles();
					}
					catch (ParseException exn) {
						conPanel.updateBotConsole("Input error when searching cars " +exn);
					}
				}
				});}
			else if(i == 2){qaButtons[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					conPanel.updateBotConsole("Customer window activated");
					cWindow.updateCustomerWindow();
				}
				});}
			else if(i == 0){qaButtons[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					conPanel.updateBotConsole("User started creating a new customer");
					try {
						createCustomer();
					}
					catch (ParseException exn) {
						conPanel.updateBotConsole("Input error: " + exn);
					}
				}
				});}
			else if(i == 1){qaButtons[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					conPanel.updateBotConsole("User started creating a new reservation");
					try {
					createReservation();
					}
					catch (ParseException exn) {
						conPanel.updateBotConsole("Input error trying to parse date");
					}
				
					}
				});}
		}
		
		//Adds a MouseListener to the calendar frame
		cFrame.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				cFrame.populateReservations();
				Reservation lastRes = cFrame.isClicked(e);
				if(lastRes != null){
					infoPanel.setInfo(lastRes);
					cFrame.populateReservations();
					conPanel.updateBotConsole("User clicked on a reservation");
				}
				else {
					conPanel.updateBotConsole("User clicked on a empty field");
				}
				
		}
		});
		
		//Adds all the sub components to the main window frame
		mainFrame.add(masterPanel);
		  masterPanel.setLayout(new BorderLayout());
			JPanel cFramePanel = new JPanel();
			//Center panel is set up with flow layout and borders
			cFramePanel.setBorder(BorderFactory.createTitledBorder("Calendar Frame:"));
			cFramePanel.setLayout(new FlowLayout());
			cFramePanel.add(cFrame);
			cFrame.setSize(cFramePanel.getPreferredSize());
		  //The rest of the components are added at their respective spots
		  masterPanel.add(cFramePanel, BorderLayout.CENTER);
		  masterPanel.add(infoPanel.build(), BorderLayout.EAST);
		  masterPanel.add(monthPanel.build(), BorderLayout.WEST);
		  masterPanel.add(conPanel.build(), BorderLayout.SOUTH);
		  masterPanel.add(qaBar, BorderLayout.NORTH);
		 
		  //Creates listeners for the monthpanel
		  monthPanel.yearsCb.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					conPanel.updateBotConsole("User changed the year");
					cFrame.updateCalendarYear(e.getItem().toString());
				}
				});
			
			monthPanel.monthsCb.addItemListener(new ItemListener() {
					public void itemStateChanged(ItemEvent e) {
						conPanel.updateBotConsole("User changed the month");
						cFrame.updateCalendarMonth(e.getItem().toString());
				}
				});
		
			
		mainFrame.setDefaultCloseOperation(3);
		mainFrame.pack();
		mainFrame.setVisible(true);
	}	
	
	/**
	 * Creates a dialogue window with input fields for a new reservation
	 */
	private void createReservation() throws ParseException {
		JComboBox car = new JComboBox<>();
		for(int i = 1; i < 10 ; i++)
			car.addItem(""+i);
		 
		carInput = "1";
		 
		MaskFormatter mf1 = new MaskFormatter("########");
		from = new JFormattedTextField(mf1);
        to = new JFormattedTextField(mf1);
        JButton customer = new JButton("Get Customer");
        JButton acknowledge = new JButton("Acknowledge");
        car.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				carInput = e.getItem().toString();
				}
         });
         
        
        final JComponent[] inputs = new JComponent[] {
        new JLabel("Car: "),car,
        new JLabel("Reserved from: "), from,
        new JLabel("Reserved to: "), to,
        new JLabel("Customer: "), customer,
        acknowledge
        };  
        //generates the window itself
        resWindow = new JDialog();
        JPanel buttonPanel = new JPanel();
        JPanel flow = new JPanel();
        flow.setLayout(new FlowLayout());
        buttonPanel.setLayout(new GridLayout(0,2));
        flow.add(buttonPanel);
        resWindow.add(flow);
        for(int i = 0; i < inputs.length; i++) {
        	buttonPanel.add(inputs[i]);
         }
        resWindow.setAlwaysOnTop(true);
        resWindow.setVisible(true);
        resWindow.setSize(250, 175);
        resWindow.setResizable(false);
        
        acknowledge.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				finalizeReservation(carInput, from, to);
				resWindow.dispose();
		}});
         
        customer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cWindow.updateCustomerWindow();
				}}); 
	}
	
	/**
	 * Finalizes a reservation - gets parameters passed from another method
	 * @param carInput - the car to reserve
	 * @param from - date string of format ddmmyyyy
	 * @param to - date string of format ddmmyyyy
	 */
	private void finalizeReservation(String carInput, JTextField from, JTextField to) {
		Integer id = cWindow.getLastID();
		controller.newReservation(Integer.parseInt(carInput), from.getText(), to.getText(), id);
		cFrame.populateReservations();
	}
	
	/**
	 * Creates a dialogue window with input fields for a new customer
	 */
	private void createCustomer() throws ParseException {
		
		 MaskFormatter mfName = new MaskFormatter("???????????????");  
		 MaskFormatter mfCombo = new MaskFormatter("******************************"); //Creates a 30 char letter mask
		 MaskFormatter mfPhone = new MaskFormatter("################");
		 
		 JFormattedTextField name1 = new JFormattedTextField(mfName);
		 name1.setFocusLostBehavior (3); //removes the auto clear field if input doesnt have the full length
		 JFormattedTextField name2 = new JFormattedTextField(mfName);
		 name2.setFocusLostBehavior (3);
         JFormattedTextField address = new JFormattedTextField(mfCombo);
         address.setFocusLostBehavior (3);
         JFormattedTextField phone = new JFormattedTextField(mfPhone);
         phone.setFocusLostBehavior (3);
         JFormattedTextField email = new JFormattedTextField(mfCombo);
         email.setFocusLostBehavior (3);
         
         JLabel[] labels = {	new JLabel("Customer First Name: * "),
        		 new JLabel("Customer Last Name: * "),
        				 new JLabel("Address: *"),
        						 new JLabel("Phone Number: * "),
        								 new JLabel("E-mail: ")        		 
         };
         JComponent[] inputs = new JComponent[]
         {labels[0], name1, labels[1], name2, labels[2], address, labels[3], phone, labels[4], email,
     };   
         JOptionPane.showMessageDialog(null, inputs, "Create a new customer", JOptionPane.INFORMATION_MESSAGE);
         if(name1.getText() != null && name2.getText() != null && phone.getText() != null) {
        	 controller.addCustomer(name1.getText()+" "+name2.getText(), address.getText(), phone.getText(), email.getText());
         }
         else {
        	 conPanel.updateBotConsole("Error creating a new customer,\n please make sure the required fields are not empty");
         }
	}
	
	/**
	 * Searches for a car of a certain class ina specific time interval 
	 * @throws ParseException
	 */
	private void searchVehicles() throws ParseException {
		 MaskFormatter mf1 = new MaskFormatter("########");
		 JTextField carClass = new JTextField("from 1-3");
         JFormattedTextField startDate = new JFormattedTextField(mf1);
         JFormattedTextField endDate = new JFormattedTextField(mf1);
         final JComponent[] inputs = new JComponent[]
         {new JLabel("Vehicle class: "),carClass,
          new JLabel("Start date: "), startDate,
          new JLabel("End date: "), endDate,
     };   
         JOptionPane.showMessageDialog(null, inputs, "Search for a vehicle type", JOptionPane.INFORMATION_MESSAGE);
         searchVehiclesResult(Integer.parseInt(carClass.getText()), startDate.getText(), endDate.getText());
	}
	
	/**
	 * Returns a frame with the available cars of the type
	 * @param car of a certain type - ranging from 1 to 3
	 * @param reserved_from - a date string of format ddmmyyyy
	 * @param reserved_to - a date string of format ddmmyyyy
	 */
	private void searchVehiclesResult(int car, String reserved_from, String reserved_to) {
		ResultSet rs = controller.searchCars(car, reserved_from, reserved_to);
		int[][] cars = new int[3][3];
		int d = 1;
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j<3 ; j++){
				cars[i][j] = d;
				d++;
		}
		}
		boolean[] searchResult = {false, false, false};
		String result[] = {	" is available for the duration",
							" is available for the duration",
							" is available for the duration"};
		try {
			while(rs.next()) {
				if(rs.getInt("type") == 1) {
						searchResult[rs.getInt("car_id")-1%3] = true;
							if(searchResult[rs.getInt("car_id")-1%3] == true)
								result[rs.getInt("car_id")-1%3] =
								"Car "+rs.getInt("car_id")+" is not available for the duration";
				}
				else if(rs.getInt("type") == 2) {
						searchResult[rs.getInt("car_id")-1%3] = true;
							if(searchResult[rs.getInt("car_id")-1%3] == true)
								result[rs.getInt("car_id")-1%3] =
								"Car "+rs.getInt("car_id")+" is not available for the duration";
				}
				else if(rs.getInt("type") == 3) {
					searchResult[rs.getInt("car_id")-1%3] = true;
							if(searchResult[rs.getInt("car_id")-1%3] == true)
								result[rs.getInt("car_id")-1%3] =
								"Car "+rs.getInt("car_id")+" is not available for the duration";
				}
			}
			
			for(int z = 0; z < 3; z++) {
				if(searchResult[z] == false)
					result[z] = "Car "+cars[car-1][z]+result[z];
			}
			
			JOptionPane.showMessageDialog(null, result, "Results:", JOptionPane.INFORMATION_MESSAGE);
		}
		catch (SQLException exn) {
			System.out.println("Cannot access database: " + exn);
			}
	}
	
	/**
	 * Builds the quick access bar buttons
	 * @return: Returns an array of JButtons
	 */
	private JButton[] quickAccessBar(){
		
		JButton[] qaButtons = {
				new JButton("New Customer"),
				new JButton("New Reservation"),
				new JButton("Show Customers"),
				new JButton("Search Vehicles")};
				
		return qaButtons;
	}
}