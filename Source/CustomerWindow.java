import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import javax.swing.*;
import java.sql.*;
import java.util.HashMap;

/**
 * A class that defines the look and methods of a customer window
 * @author Ivan
 */
public class CustomerWindow extends JFrame {
	
	/**
	 * Creates instances of the panels needed to build the customer window
	 */
	JFrame cWindow;
	JPanel cWestPane = new JPanel();
	JPanel flow = new JPanel();
	JScrollPane scPane = new JScrollPane(flow);
	Controller c;
	ConsolePanel cp;
	ResultSet rs;
	int lastId = 0;
	HashMap<JButton, Integer> idsDel = new HashMap<>();
	HashMap<JButton, Integer> idsEdit = new HashMap<>();
	HashMap<JButton, Integer> idsGet = new HashMap<>();
	
	/**
	 * Constructor for the customer window
	 * @param c: is the reference to our controller instance, instanciated in the Gui class.
	 */
	public CustomerWindow(Controller c, ConsolePanel cp) {
		this.c = c;
		this.cp = cp;
		build();
	}
	
	/**
	 * builds the actual customer window frame
	 */
	public void build() {
		flow.add(cWestPane);
		flow.setLayout(new FlowLayout());
		cWindow = new JFrame("Customers (Ordered by Customer name)");
		cWestPane.setLayout(new GridLayout(0,6));
		cWindow.setDefaultCloseOperation(HIDE_ON_CLOSE);
		cWindow.add(scPane);
		cWindow.pack();
		cWindow.setSize(getPreferredSize());
		
	}
	
	/**
	 * Updates the window, by rebuilding the list and storing the Jbuttons as keys, id's as values
	 */
	public void updateCustomerWindow() {
		cWestPane.removeAll();
		
		for(int i = 0 ;i < idsDel.size() ; i++){
		idsDel.remove(i);
		}
		for(int i = 0 ;i < idsEdit.size() ; i++){
		idsEdit.remove(i);
		}
		for(int i = 0 ;i < idsGet.size() ; i++){
		idsGet.remove(i);
		}
		
		
		try {
			rs = c.showCustomers();
					
			while(rs.next()) {	
				String[] jLb = {	"Name",
									"Address",
									"Phone",
									"E-mail",
								};
				
				JTextArea[] jTx = { 	new JTextArea(rs.getString("name")),
										new JTextArea(rs.getString("address")),
										new JTextArea(rs.getString("phone")),
										new JTextArea(rs.getString("email"))									
				};
				
				JPanel splitPane = new JPanel();
				splitPane.setLayout(new BorderLayout());
				splitPane.setBorder(BorderFactory.createTitledBorder(""+rs.getString("name").charAt(0)));
				
				JPanel cButPane = new JPanel();
				cButPane.setLayout(new FlowLayout());
				
				JPanel cLinePane = new JPanel();
				cLinePane.setLayout(new BoxLayout(cLinePane, 3));
				
				splitPane.add(cLinePane, BorderLayout.NORTH);
				splitPane.add(cButPane, BorderLayout.SOUTH);
				
				//Make sure all the fields are non editable and added to the pane
				for(int i = 0; i < jTx.length ; i++) {
					jTx[i].setSize(100,25);
					cLinePane.add(jTx[i]);
					jTx[i].setBorder(BorderFactory.createTitledBorder(jLb[i]));
					jTx[i].setEditable(false);
					jTx[i].setBackground(splitPane.getBackground());
				}
				
				JButton[] jBt = {	new JButton("Edit"),
									new JButton("Delete"),
									new JButton("Get Id")
						};

				for(int i = 0 ; i < jBt.length ; i++) {		
					cButPane.add(jBt[i]);
						if(i == 0){
							idsEdit.put(jBt[i], rs.getInt("customer_id"));	
							jBt[i].addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent e) {
									editCustomer(e);
								}
								});
							}
						if(i == 1) {
							idsDel.put(jBt[i], rs.getInt("customer_id"));
							jBt[i].addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent e) {
									deleteCustomer(e);
								}
								});
						}
						if(i == 2) {
							idsGet.put(jBt[i], rs.getInt("customer_id"));
							jBt[i].addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent e) {
									lastId = getCustomerId(e);
									cWindow.setVisible(false);
								}
								});
						}
				}
				cWestPane.add(splitPane);
				}
			
				
				
			}
			catch (SQLException exn) {
				System.out.println("Error: Customer database not available " + exn);
			}
		finally {
			cWindow.setVisible(true);
		}
	}
	
	/**
	 * Returns the last clicked customer Id
	 * @return integer that represents the customers database entry
	 */
	public Integer getLastID() {
		return lastId;
	}
	
	/**
	 * Edits the customer
	 * @param 'e' is the triggering event
	 */
	private void editCustomer(ActionEvent e) {
        JTextField name = new JTextField();
        JTextField address = new JTextField();
        JTextField phone = new JTextField();
        JTextField email = new JTextField();
        final JComponent[] inputs = new JComponent[]
        {new JLabel("New Name: "),name,
         new JLabel("New Address: "), address,
         new JLabel("New Phone number: "), phone,
         new JLabel("New E-mail: "), email,
    };   
        JOptionPane.showMessageDialog(null, inputs, "Edit a customer", JOptionPane.INFORMATION_MESSAGE);
        
        String[] newInput = {	name.getText(),
        						address.getText(),
        						phone.getText(),
        						email.getText()
        };
        
        c.changeCustomer(idsEdit.get(e.getSource()), newInput[0], newInput[1], newInput[2], newInput[3]);
        updateCustomerWindow();
	}
	
	/**
	 * Gets the customer id
	 * @param 'e' passed from the triggering JButton
	 * @return id from collection 'idsGet'
	 */
	private Integer getCustomerId(ActionEvent e) {
		return idsGet.get(e.getSource());
	}
	
	/**
	 * Deletes the customer matching the passed id
	 * @param 'e' is the id linked to the data row
	 */
	private void deleteCustomer(ActionEvent e) {
		c.delCustomer(idsDel.get(e.getSource()));
		updateCustomerWindow();
	}
	
	/**
	 * Sets the default size for the window, when opened
	 */
	public Dimension getPreferredSize() { 
	    return new Dimension(1300, 700); 
	  }
	
	/**
	 * sets the default maximum size for the window when opened
	 */
	public Dimension getMaximumSize() {
		return getPreferredSize();
	}
	
}
