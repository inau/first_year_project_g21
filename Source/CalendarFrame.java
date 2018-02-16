import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import javax.swing.*;
import java.util.ArrayList;
import java.sql.*;

public class CalendarFrame extends JComponent {
	
	/**
	 * Declaring instance variables
	 */
	private int cols = 32, rows = 10, colSize = 30, rowSize = 30, year = 2011;
	private final int[] monthLengths = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
	private final Color[] colorCode = {Color.black, Color.magenta, Color.yellow, Color.red};
	private String[] vehicles = {"Car1", "Car2", "Car3", "Car4", "Car5", "Car6", "Car7", "Car8", "Car9"};
	
	// Month variables
	private String currentMonth = "January";
	private final HashMap<String, Integer> monthIntCodes = new HashMap<>();
	private final String[] monthNames = {	"January", "February", "March", "April", "May",
			"June", "July", "August", "September", "Oktober",
			"November", "December"};
	private final Integer[] monthNumbers = {0 , 1, 2 , 3 , 4 , 5 ,6 ,7 , 8 , 9 ,10 , 11};
	
	private Reservation[][] guiCoords = new Reservation[cols][rows];
	
	// collaborators
	private Controller c;
	private ConsolePanel cp;
	
	/**
	 * Constructs the calendar frame and initializes the HashMap
	 * containing month string - month numeric values.
	 */
	public CalendarFrame(Controller c, ConsolePanel cp) {
		// Initiates the HashMap keeping track of month names and numeric values
		// aswell as storing references for controller and console instances
		for(int i = 0; i <12 ; i++)
			monthIntCodes.put(monthNames[i], monthNumbers[i]);
		this.c = c;
		this.cp = cp;
		populateReservations();
	}
	
	/**
	 * Populate the calendar with reservations
	 */
	public void populateReservations() {
		//Purge current list
		for(int i = 0 ; i < cols ; i++) {
			for(int j = 0 ; j < rows ; j++) {
				guiCoords[i][j] = null;
			}
		}
		//Get new resultset to populate list
		ResultSet rs = c.showReservationsMonth(monthIntCodes.get(currentMonth), year);
		Reservation res;
		try {
		while(rs.next()) {
			int car = rs.getInt("car");
			res = new Reservation(	rs.getInt("reservation_id"),
									car,
									rs.getString("reserved_from"),
									rs.getString("reserved_to"),
									rs.getInt("customer")
							);
			int d = res.timeOfReservation();
			for(int i = res.dayStart(); i < res.dayStart()+d+1 ; i++) {
					guiCoords[i][car] = res;
			}
			}
		}
		catch (SQLException exn) {
			cp.updateBotConsole("Error populating reservation list" + exn);
		}
		repaint();
	}
	
	/**
	 * Updates the calendar year
	 * @param yr: the year passed by the combobox
	 */
	public void updateCalendarYear(String yr) {
		year = Integer.parseInt(yr);
		updateCalendarMonth(currentMonth);
		populateReservations();
	}
	
	/**
	 * Updates calendar, based on the last input from the drop down pane.
	 * @param month: name of the value in the drop down pane
	 */
	public void updateCalendarMonth(String month) {
	// This statement ensures that when the month is February
	// a leap year check is initiated.
		currentMonth = month;
		int z = 0;
		if(month.equals("February") && year%4==0 && year%100!=0 || month.equals("February") && year%400 == 0){
			z = 1;
			}
			else {
				z = 0;
			}
		
		cols = monthLengths[monthIntCodes.get(month)]+1+z;
		populateReservations();
	}
	
	/**
	 * Method that paints the canvas
	 */
	public void paint(Graphics g){
		//The for loop draws the colored rows 
		int z = 1;
		for(int j = 0; j < rows ;j++){
			z = j%2;
			if(z == 0){
				g.setColor(Color.lightGray);
				g.fillRect(0, j*rowSize, cols*colSize, j*rowSize+rowSize);
			}
	         else {
	                g.setColor(Color.white);
	                g.fillRect(0, j*rowSize, cols*colSize, j*rowSize+rowSize);
	            }
		}
		//Sets the 1st column & row to be lightgray
		g.setColor(Color.lightGray);
		g.fillRect(0, 0, cols*colSize, rowSize);
		g.fillRect(0, 0, colSize, rows*rowSize);
		
		//The for loop draws the grid
		for(int i = 0; i < rows ; i++){
			g.setColor(Color.black);
			g.drawLine(0, i*rowSize, cols*colSize, i*rowSize);
			}
		for(int j = 0; j < cols ;j++){	
				g.drawLine(j*colSize, 0, j*colSize, cols*colSize);
				}
		
		//draws the monthday on canvas
		int d = 0;
			for(int i = 0; i < cols ; i++){
				d++;
				g.setColor(Color.black);
				g.drawString(""+d, (i+1)*colSize+10, 27);
				}
			
		//draws the name of the vehicle	
			for(int i = 0; i < vehicles.length ; i++){
				g.setColor(Color.black);
				g.drawString(vehicles[i], 2 , rowSize+20 + (i*rowSize));
				}
		
//		//draw the booking array for this week, based on the sql arrays (UNDER CONTRUCTION - reworked)
//		int[] days = {2 , 4 , 6 , 3 , 5 , 7 , 12 ,  6, 5 , 9}; //DUMMY DATA
//		int[] startDate = {4 , 5 , 7 , 9 , 11 , 14 , 15, 7 , 13 , 18 };
//		
//		for(int i = 0; i < days.length ; i++) {
//			g.setColor(colorCode[i%4]);
//			g.fill3DRect(colSize+5+startDate[i]*colSize, (i+1)*rowSize+10, days[i]*colSize-10, rowSize-15, true);
//			}
		
		//A loop, that draws reservations, based on the returned data from populateReservations()
		for(int i = 1; i < rows ; i++) {
			for(int j = 1; j < cols; j++) {
				if(guiCoords[j][i] != null) {
						int c = guiCoords[j][i].getCar();
						int cu = guiCoords[j][i].getCustomer();
						boolean noMaintenance = true;
							if((c == 1 || c == 2 || c == 3) && cu != 0) {
								g.setColor(colorCode[1]);
							}
							else if((c == 4 || c == 5 || c == 6) && cu != 0) {
								g.setColor(colorCode[2]);
							}
							else if((c == 7 || c == 8 || c == 9) && cu != 0) {
								g.setColor(colorCode[3]);
							}
							else if(cu == 0) {
								g.setColor(colorCode[0]);
							}
						
						
						
						if(guiCoords[j][i].monthStart() != monthIntCodes.get(currentMonth)+1) {
							g.fill3DRect(	guiCoords[j][i].dayStart()*colSize,
											(i)*rowSize+10,
											monthLengths[monthIntCodes.get(currentMonth)]*colSize,
											rowSize-15,
											true				
								);}
						else if(guiCoords[j][i].monthEnd() != monthIntCodes.get(currentMonth)+1) {
							g.fill3DRect(	guiCoords[j][i].dayStart()*colSize,
											(i)*rowSize+10,
											monthLengths[monthIntCodes.get(currentMonth)]*colSize,
											rowSize-15,
											true
							);}
						else if(guiCoords[j][i].monthStart() == monthIntCodes.get(currentMonth)+1 ||
								guiCoords[j][i].monthEnd() == monthIntCodes.get(currentMonth)+1) {
							g.fill3DRect(	5+guiCoords[j][i].dayStart()*colSize,
											(i)*rowSize+10,
											-10+guiCoords[j][i].timeOfReservation()*colSize,
											rowSize-15,
											true
							);}
						}
						}
					
				}

		//defines the size of the clipping area
		g.setClip(0, 0, cols*colSize+1, rows*rowSize+1);
		
		//draws a black rectangle around the panel
		g.setColor(Color.black);
		g.drawRect(0, 0, cols*colSize, rows*rowSize);
		
		this.setSize(getPreferredSize());
	}
	
	/**
	 * returns the reservation matching the coordinates of the click event
	 * @param e is the event
	 * @return a reservation
	 */
	public Reservation isClicked(MouseEvent e) {
		int i = e.getX()/colSize, j = e.getY()/rowSize;
		if(guiCoords[i][j] != null) {
			populateReservations();
			return guiCoords[i][j];
		}
		else {
			return null;
		}
	}
	
	
	/**
	 * sets the preferred size of the canvas
	 */
	public Dimension getPreferredSize() { 
	    return new Dimension(cols*colSize, rows*rowSize); 
	  }
	
	/**
	 * set the maximum size as the preferred size of the canvas
	 */
	public Dimension getMaximumSize() {
		return getPreferredSize();
	}
}