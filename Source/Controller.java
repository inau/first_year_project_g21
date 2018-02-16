import java.sql.*;
/**
 * Methods called by the action listeners from the Gui.
 * @author Group 21.
 * @version 1.0.
 */

public class Controller {
	private final int[] months = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
	private ConsolePanel cp;
	private Sql sql;
	
	public Controller(ConsolePanel cp) {
		this.cp = cp;
		sql = new Sql(cp);
	}
	
	/**
	 * Show all costumers currently in the database.
	 * @return Resultset. Return a resultset of all the costumers.
	 */
	public ResultSet showCustomers() {
		return sql.getCustomers();
	}
	
	/**
	 * Return a resultset of the customer if available.
	 * @param id int. The id of the customer.
	 * @return resultset. The information of the customer.
	 */
	public ResultSet showCustomer(int id) {
		return sql.getCustomer(id);
	}
	
	/**
	 * Show all reservations in the database.
	 * @return Resultset. Return a resultset of all the reservations.
	 */
	public ResultSet showReservations() {
		return sql.getReservations();
	}
	
	/**
	 * Return a resultset of a whole months reservation.
	 * @param month int. The month of the reservations searched for.
	 * @param year int. The year of the reservations searched for.
	 * @return Resultset. The resultset of all the reservations of the particular month.
	 */
	public ResultSet showReservationsMonth(int month, int year) {
		int startDay = 01;
		int startYear = year;
		int startMonth = month+1;
		int endDay = 0;
		int endMonth = month+1;
		int endYear = year;
		//Check if the year of the reservation is a leap year and if the month is February before determining if the input is valid. 
		if(month==1 && year%4==0 && year%100!=0 || month==1 && year%400==0)
			endDay = 29;
		else
			endDay = months[month];
		return sql.getReservationBetweenExpand(	dateChange(startDay, startMonth, startYear),
												dateChange(endDay, startMonth, startYear),
												dateChange(startDay, endMonth, endYear),
												dateChange(endDay, endMonth, endYear));
	}
	
	/**
	 * Change the input strings to date objects and parse it to the sql to search which cars are available, if any, in the span
	 * between the reserved from date to the reserved to date.
	 * @param reservedFrom String. A string containing the date the reservation starts.
	 * @param reservedTo String. A string containing the date the reservation ends.
	 * @return ResultSet. A resultset of the available cars in the period between the start and end date.
	 */
	 public ResultSet searchCars(int car, String reservedFrom, String reservedTo) {
	 int dayBeginning = dayToInt(reservedFrom);
	 int monthBeginning = monthToInt(reservedFrom);
	 int yearBeginning = yearToInt(reservedFrom);
	 int dayEnd = dayToInt(reservedTo);
	 int monthEnd = monthToInt(reservedTo);
	 int yearEnd = yearToInt(reservedTo);
	 return sql.searchReservation(car, dateChange(dayBeginning, monthBeginning, yearBeginning),
	 dateChange(dayEnd, monthEnd, yearEnd));
	 }
	
	/**
	 * Add a new costumer to the database.
	 * @param name String. A string containing the name of the costumer to be added.
	 * @param address String. The address of the costumer to be added.
	 * @param phone String. The phone number of the costumer to be added.
	 * @param email String. The email of of the costumer to be added.
	 * @return boolean. True if the costumer has been added to the database.
	 */
	public boolean addCustomer(String name, String address, String phone, String email) {
		if(name.length()>0 && address.length()>0 && phone.length()>0) { // controls that the name and address strings are not empty.
				if(sql.addCustomer(name, address, phone, email)==true)
					return true;
				else
					return false;
		}
		else
			return false;
	}
	/**
	 * Change details a certain costumer.
	 * @param id int. The id of the costumer.
	 * @param name String. The name of the costumer, new or original.
	 * @param phone String. The phone number of the costumer, new or original.
	 * @param email String. The email of the costumer, new or original.
	 * @return boolean. True if the details has been updated.
	 */
	public boolean changeCustomer(int id, String name, String address, String phone, String email) {
		if(name.length()>0 && address.length()>0 && phone.length()>0) {
			if (sql.changeCustomer(id, name, address, phone, email)==true)
				return true;
			else
				return false;
		}
		else
			return false;
	}
	/**
	 * Delete a certain costumer.
	 * @param id int. The id of the costumer.
	 * @return boolean. True if the costumer has been removed from the database.
	 */
	public boolean delCustomer(int id) {
		if(id>0) {
			if (sql.delCustomer(id)==true)
				return true;
			else
				return false;
		}
		else
			return false;
	}
	/**
	 * Create a new reservation.
	 * @param carNumber int. Representing a certain car.
	 * @param reservedFrom String. The date of the start of the reservation.
	 * @param reservedTo String. The date of the end of the reservation.
	 * @param costumer int. The id of the costumer.
	 * @return boolean. Return true if the new reservation has been created.
	 */
	public boolean newReservation(int carNumber, String reservedFrom, String reservedTo, int customer) {		
		int dayBeginning = dayToInt(reservedFrom);
		int monthBeginning = monthToInt(reservedFrom);
		int yearBeginning = yearToInt(reservedFrom);
		int dayEnd = dayToInt(reservedTo);
		int monthEnd = monthToInt(reservedTo);
		int yearEnd = yearToInt(reservedTo);	
			if(dateCheck(dayBeginning, monthBeginning, yearBeginning)==true && dateCheck(dayEnd, monthEnd, yearEnd)==true) {
				sql.addReservation(carNumber,dateChange(dayBeginning,monthBeginning,yearBeginning) , dateChange(dayEnd, monthEnd, yearEnd), customer);
				return true;
			}
			else
				return false;		
	}
	/**
	 * Change a certain reservation.
	 * @param id int. Id of the reservation.
	 * @param carNumber int. The number of the reserved car.
	 * @param reservedFrom String. The date of the start of the reservation.
	 * @param reservedTo String. The date of the end of the reservation.
	 * @return boolean. True if the reservation has been changed.
	 */
	public boolean changeReservation(int id, int carNumber, String reservedFrom, String reservedTo, int customer) {
		int dayBeginning = dayToInt(reservedFrom);
		int monthBeginning = monthToInt(reservedFrom);
		int yearBeginning = yearToInt(reservedFrom);
		int dayEnd = dayToInt(reservedTo);
		int monthEnd = monthToInt(reservedTo);
		int yearEnd = yearToInt(reservedTo);
		if(dateCheck(dayBeginning, monthBeginning, yearBeginning)==true &&
						dateCheck(dayEnd, monthEnd, yearEnd)==true) {
				sql.changeReservation(	id, carNumber,
										dateChange(dayBeginning, monthBeginning, yearBeginning),
										dateChange(dayEnd, monthEnd, yearEnd),
										customer);

				return true;
			}
			else
				return false;
	}
	/**
	 * Delete a certain reservation.
	 * @param id int. The id of the reservation.
	 * @return boolean. True if the reservation has been deleted.
	 */
	public boolean deleteReservation(int id) {
		if(sql.delReservation(id)==true)
			return true;
		else
			return false;
	}
		
	/**
	 * 	Check if the date is an actual date.
	 * @param day int. The day of the reservation start or end.
	 * @param month int. The month of the reservation start or end.
	 * @param year int. The year of the reservation start or end.
	 */
	private boolean dateCheck(int day,int month,int year) {
		//Check if the year of the reservation is a leap year and if the month is February before determining if the input is valid. 
		if(month==2 && year%4==0 && year%100!=0 || month==2 && year%400==0) {
			if(day>0 && day<months[month-1]+2) {
					if(year>=2011 && year<9999) {
						return true;
					}
					else
						return false;
				}
					else
						return false;
			}
		else if(year>=2011 && year<9999) {
			if(month>0 && month<13) {
				if(day>0 && day<months[month-1]+1) {
					return true;
				}
				else
					return false;
			}
			else
				return false;
		}
		else
			return false;
	}
	
	/**
	 * Change the date represented as integers to a date object.
	 * @param day int. The day of the reservation start or end.
	 * @param month int. The month of the reservation start or end.
	 * @param year int. The year of the month start or end.
	 * @return Date. A date object containing the date of the reservation start or end.
	 */
	private Date dateChange(int day, int month, int year) {
		Date date = new Date(2011);
		String dayString = ""+day;
		String monthString = ""+month;
		String dateString = "";
		if(dayString.length()<2 && monthString.length()<2) 
			dateString = ""+year+"-0"+month+"-0"+day;
		else if(dayString.length()<2 && monthString.length()==2) 
			dateString = ""+year+"-"+month+"-0"+day;
		else if(dayString.length()==2 && monthString.length()<2)
			dateString = ""+year+"-0"+month+"-"+day;
		else
			dateString = ""+year+"-"+month+"-"+day;
		date = date.valueOf(dateString);
		return date;
	}
	
		/**
	 * Parse the day of the date string to an integer.
	 * @param reserved String. String containing the date of the reservation start or end.
	 * @return int. The day the reservation start or end.
	 */
	private int dayToInt(String reserved) {
		String dayString = "";
		int dayInt = 0;
		for(int i= 0; i<2; i++)
			dayString += reserved.charAt(i);
		dayInt = Integer.parseInt(dayString);
		return dayInt;
	}
	
	/**
	 * Parse the month of the date string to an integer.
	 * @param reserved String. The string containing the date the reservation start or end.
	 * @return int. The month the reservation start or end.
	 */
	private int monthToInt(String reserved) {
		String monthString = "";
		int monthInt = 0;
		for(int i= 2; i<4; i++)
			monthString += reserved.charAt(i);
		monthInt = Integer.parseInt(monthString);
		return monthInt;
	}
	
	/**
	 * Parse the year of the date to an integer.
	 * @param reserved String. The string containing the date of the reservation start or end.
	 * @return int. The year the reservation start or end.
	 */
	private int yearToInt(String reserved) {
		String yearString = "";
		int yearInt = 0;
		for(int i= 4; i<8; i++)
			yearString += reserved.charAt(i);
		yearInt = Integer.parseInt(yearString);
		return yearInt;
	}
}