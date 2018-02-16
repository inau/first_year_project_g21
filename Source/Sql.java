import java.sql.*;
/*
 * Sql Branch
 */
public class Sql {
 
	//Instance variable
	private ConsolePanel cp;
	private Connection conn;
	
	/*
	 * Class constructor. Fetches info from DB to array.
	 */
	public Sql(ConsolePanel cp)
	{
		this.cp = cp;
		openDb();
//		testMethod();
	}
	
	/**
	 * This is a testing method
	 */
	public void testMethod(){
		//addReservation(3, new Date(2011).valueOf("2011-12-20"), new Date(2011).valueOf("2012-12-20"), 1);
		if(checkReservation(69, new Date(2011).valueOf("2025-12-01"), new Date(2011).valueOf("2025-12-11")))
			System.out.println("No reservations found");
		else
			System.out.println("This car is already reserved in this period");
	}
	
	/**
	 * This opens a new connection to the database
	 */
	private void openDb() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://mysql.itu.dk/gpp_21_db?autoReconnect=true", "gpp_21_usr", "gpp_21_pwd");
        } catch (SQLException exn) {
            cp.updateBotConsole("Cannot open connection: " + exn);
        } catch (ClassNotFoundException exn) {
            cp.updateBotConsole("Cannot find database driver: " + exn);
        }
    } 
	
	/**
	 * This method closes the connection assuming that there's an existing connection. 
	 */
	public void closeDb()
	{
		try {
			if (conn != null)
				conn.close();   
		} catch (SQLException exn) { 
			cp.updateBotConsole("Cannot close the connection: " + exn);
		}
	}
	
	/**
	 * This method changes the customer in the database.
	 * @param id	ID of the customer.
	 * @param name	Name of the customer.
	 * @param address	Address of the customer
	 * @param phone	Phone of the customer
	 * @param email	Email of the customer
	 * @return	True if the update was sucessfull, false otherwise.
	 */
	public boolean changeCustomer(int id, String name, String address, String phone, String email){
		try{
			CustomerDb customerSql = new CustomerDb(conn, cp);
			if(customerSql.changeCustomer(id, name, address, phone, email)){
				return true;
			}
			else{
				return false;
			}
		}
		catch(SQLException exn){
				cp.updateBotConsole("Error in SQL" + exn);
				return false;
		}
	}
	
	/**
	 * Adds a new customer
	 * @param name	name of the customer
	 * @param address	address of the customer
	 * @param phone	phone of the customer
	 * @param email	email  of the customer
	 * @return	True if the customer is added, false otherwise
	 */
	public boolean addCustomer(String name, String address, String phone, String email){
		try{
			CustomerDb customerSql = new CustomerDb(conn, cp);
			if(customerSql.addCustomer(name, address, phone, email)){
				return true;
			}
			else{
				return false;
			}
		}
		catch(SQLException exn){
			cp.updateBotConsole("Error in SQL" + exn);
			return false;
		}
	}
	
	/**
	 * Deletes a customer
	 * @param id	ID of the customer
	 * @return	True if customer is deleted, false otherwise.
	 */
	public boolean delCustomer(int id){
		try{
			CustomerDb customerSql = new CustomerDb(conn, cp);
			if(customerSql.delCustomer(id)){
				//updateLocal();
				return true;
			}
			else{
				return false;
			}
		}
		catch(SQLException exn){
			cp.updateBotConsole("Error in SQL" + exn);
			return false;
		}
	}
	
	/**
	 * Gets customers
	 * @return	ResultSet of customers
	 */
	public ResultSet getCustomers(){
		try{
			CustomerDb customerSql = new CustomerDb(conn, cp);
				return customerSql.getCustomers();
		}
		catch(SQLException exn){
			cp.updateBotConsole("Error in SQL" + exn);
			return null;
		}
	}
	
	/**
	 * Gets a customer
	 * @return	ResultSet of a customer
	 */
	public ResultSet getCustomer(int id){
		try{
			CustomerDb customerSql = new CustomerDb(conn, cp);
				return customerSql.getCustomer(id);
		}
		catch(SQLException exn){
			cp.updateBotConsole("Error in SQL" + exn);
			return null;
		}
	}
	
	/**
	 * Gets reservations
	 * @return	ResultSet of reservations
	 */
	public ResultSet getReservations(){
		try{
			ReservationDb reservationSql = new ReservationDb(conn, cp);
				return reservationSql.getReservations();
		}
		catch(SQLException exn){
			cp.updateBotConsole("Error in SQL" + exn);
			return null;
		}
	}
	
	/**
	 * Gets a reservation
	 * @return	ResultSet of a reservation
	 */
	public ResultSet getReservation(int id){
		try{
			ReservationDb reservationSql = new ReservationDb(conn, cp);
				return reservationSql.getReservation(id);
		}
		catch(SQLException exn){
			cp.updateBotConsole("Error in SQL" + exn);
			return null;
		}
	}
	
	/**
	 * 
	 * @param startDate	When the reservation start
	 * @param endDate	When the reservation ends
	 * @return	returns the reservation between two dates.
	 */
	public ResultSet getReservationBetween(Date startDate, Date endDate){
		try{
			ReservationDb reservationSql = new ReservationDb(conn, cp);
				return reservationSql.getReservationBetween(startDate, endDate);
		}
		catch(SQLException exn){
			cp.updateBotConsole("Error in SQL" + exn);
			return null;
		}
	}
	
	/**
	 * This method returns reservations which is between two intervals
	 * @param startDateFrom	the first date in the interval
	 * @param startDateTo	the second date in the interval
	 * @param endDateFrom	the first date in the second interval
	 * @param endDateTo		the second date in the second interval
	 * @return				returns reservations between two intervals
	 */
	public ResultSet getReservationBetweenExpand(	Date startDateFrom,
													Date startDateTo,
													Date endDateFrom,
													Date endDateTo
													) {
		try{
			ReservationDb reservationSql = new ReservationDb(conn, cp);
				return reservationSql.getReservationBetweenExpand(	startDateFrom,
																	startDateTo,
																	endDateFrom,
																	endDateTo
																	);
		}
		catch(SQLException exn){
			cp.updateBotConsole("Error in SQL" + exn);
			return null;
		}
	}
	
	public ResultSet searchReservation(int carType, Date startDate, Date endDate){
		try{
			ReservationDb reservationSql = new ReservationDb(conn, cp);
				return reservationSql.searchReservation(carType, startDate, endDate);
		}
		catch(SQLException exn){
			cp.updateBotConsole("Error in SQL" + exn);
			return null;
		}
	}
	
	/**
	 * 
	 * @param car			int car id
	 * @param startDate		date start date
	 * @param endDate		date end date
	 * @return				True if a car is reserved, false otherwise.
	 */
	public boolean checkReservation(int car, Date startDate, Date endDate){
		try{
			ReservationDb reservationSql = new ReservationDb(conn, cp);
			if(reservationSql.checkReservation(car, startDate, endDate)){
				return true;
			}
			else{
				return false;
			}
		}
		catch(SQLException exn){
			cp.updateBotConsole("Error in SQL" + exn);
			return false;
		}
	}
	
	/**
	 * change a reservation in DB
	 * @param id	reservation name
	 * @param car	car id
	 * @param reserved_from	Reservation start
	 * @param reserved_to	Reservation end
	 * @param customer	Customer id
	 * @return	True if changed, false otherwise
	 */
	public boolean changeReservation(int id, int car, Date reserved_from, Date reserved_to, int customer){
		try{
			ReservationDb reservationSql = new ReservationDb(conn, cp);
			if(reservationSql.changeReservation(id, car, reserved_from, reserved_to, customer)){
				return true;
			}
			else{
				return false;
			}
		}
		catch(SQLException exn){
			cp.updateBotConsole("Error in SQL" + exn);
			return false;
		}
	}
	
	
	
	/**
	 * Adds a reservation in DB
	 * @param car	Car ID
	 * @param reserved_from	Reservation start
	 * @param reserved_to	Reservation end
	 * @param customer	Customer ID
	 * @return	True if added, false otherwise.
	 */
	public boolean addReservation(int car, Date reserved_from, Date reserved_to, int customer){
		try{
			ReservationDb reservationSql = new ReservationDb(conn, cp);
			if(reservationSql.addReservation(car, reserved_from, reserved_to, customer)){
				return true;
			}
			else{
				return false;
			}
		}
		catch(SQLException exn){
			cp.updateBotConsole("Error in SQL" + exn);
			return false;
		}
	}
	
	/**
	 * Deletes a reservation in DB
	 * @param id	reservation id
	 * @return	True if deleted, flase otherwise
	 */
	public boolean delReservation(int id){
		try{
			ReservationDb reservationSql = new ReservationDb(conn, cp);
			if(reservationSql.delReservation(id)){
				return true;
			}
			else{
				return false;
			}
		}
		catch(SQLException exn){
			cp.updateBotConsole("Error in SQL" + exn);
			return false;
		}
	}
	
	/**
	 * Test method to test SQL stuff
	 */
	public void testArray(){
		try{
			CustomerDb testDb = new CustomerDb(conn, cp);
			if(testDb.changeCustomer(9, "Ny Kurwa 2", "Kurwa by", "6969696969", "no email")){
				System.out.println("Changing customer.. NPS");
			}
			else{
				System.out.println("Changing customer.. Fail");
			}
			if(testDb.delCustomer(13)){
				System.out.println("Deleting.. NPS");
			}
			else{
				System.out.println("Deleting.. Fail");
			}
			if(testDb.addCustomer("fg", "huracity", "9999", "Emailgoeshere")){
					System.out.println("Adding customer.. NPS");
				}
				else{
					System.out.println("Adding customer.. Fail");
				}
		}
		catch (SQLException exn) {
			System.out.println("Sql error wtf?" +exn);
		}
	}
}