import java.sql.*;

/**
 * 
 * @author Group 21
 * 
 */
public class ReservationDb {

	/**
	 * Initiate instance variables
	 */

	private final Statement dbStatement;
	private ConsolePanel cp;

	/**
	 * Class constructor
	 * @param conn	What connection to use
	 * @throws SQLException
	 */
	
	public ReservationDb(Connection conn, ConsolePanel cp) throws SQLException {
		this.cp = cp;
		this.dbStatement = conn.createStatement();
	}

	/**
	 * Gets all reservations from db
	 * @return	a RestultSet with all reservations
	 */
	public ResultSet getReservations() {
		ResultSet result = null;
		String query = "SELECT * FROM reservations " + "ORDER BY car";
		try {
			boolean ok = dbStatement.execute(query);
			if (ok) {
				result = dbStatement.getResultSet();
				return result;
			}
		} catch (SQLException exn) {
			cp.updateBotConsole("Can't read from database: " + exn);
		}
		return result;
	}
	
	/**
	 * 
	 * @param startDate	When the reservation start
	 * @param endDate	When the reservation ends
	 * @return	returns the reservation between two dates.
	 */
	public ResultSet getReservationBetween(Date startDate, Date endDate) {
		ResultSet result = null;
		String query = "SELECT * FROM reservations WHERE reserved_from >= '"+ startDate +"' " +
				"AND reserved_to <= '"+ endDate +"' " + "ORDER BY reserved_from";
		try {
			boolean ok = dbStatement.execute(query);
			if (ok) {
				result = dbStatement.getResultSet();
				return result;
			}
		} catch (SQLException exn) {
			cp.updateBotConsole("Can't read from database: " + exn);
		} 
		return result;
	}
	
/**
 * This method returns reservations which is between two intervals
 * @param startDateFrom	the first date in the interval
 * @param startDateTo	the second date in the interval
 * @param endDateFrom	the first date in the second interval
 * @param endDateTo		the second date in the second interval
 * @return				returns reservations between two intervals
 */
	public ResultSet getReservationBetweenExpand(Date startDateFrom, Date startDateTo, Date endDateFrom, Date endDateTo) {
		ResultSet result = null;
		String query = "SELECT * FROM reservations WHERE reserved_from BETWEEN '"+ startDateFrom +"' AND '"+ startDateTo+"' " +
				"AND reserved_to BETWEEN '"+ endDateFrom +"' AND '"+ endDateTo+"' " + "ORDER BY reserved_from";
		try {
			boolean ok = dbStatement.execute(query);
			if (ok) {
				result = dbStatement.getResultSet();
				return result;
			}
		} catch (SQLException exn) {
			cp.updateBotConsole("Can't read from database: " + exn);
		} 
		return result;
	}
	
	/**
	 * Gets a single reservation
	 * @return	a RestultSet with a reservation
	 */
	public ResultSet getReservation(int id) {
		ResultSet result = null;
		String query = "SELECT * FROM reservations " + "ORDER BY car WHERE reservation_id = '"+id+"'";
		try {
			boolean ok = dbStatement.execute(query);
			if (ok) {
				result = dbStatement.getResultSet();
				return result;
			}
		} catch (SQLException exn) {
			cp.updateBotConsole("Can't read from database: " + exn);
		} 
		return result;
	}
	
	/**
	 * 
	 * @param car			Int which car id.
	 * @param startDate		Date what date the reservation starts
	 * @param endDate		Date what date the reservation ends
	 * @return				True if the car is not in use, false otherwise
	 */
	public boolean checkReservation(int car, Date startDate, Date endDate){
		String query = "SELECT * FROM reservations WHERE car = '"+car+"' AND reserved_from >= '"+ startDate +"' " +
				"AND reserved_to <= '"+ endDate +"' " + "ORDER BY reserved_from";
		try {
			ResultSet result = dbStatement.executeQuery(query);
//			while (result.next()) {
//		        String id = result.getString(1);
//		       }
			result.last();
			int rowCount = result.getRow();
			if (rowCount == 0) {
				return true;
			}
			else {
				return false;
			}
		}
		catch (SQLException exn) {
			cp.updateBotConsole("Can't read from database: " + exn);
				return false;
		}
	}
	
	
	public ResultSet searchReservation(int carType, Date startDate, Date endDate) {
		ResultSet result = null;
		String query = "SELECT * FROM reservations, car " + "WHERE car.car_id = reservations.car " +
				"AND car.type = '"+carType+"' AND reservations.reserved_from >= '"+ startDate +"' " +
				"AND reservations.reserved_to <= '"+ endDate +"' ";
		try {
			boolean ok = dbStatement.execute(query);
			if (ok) {
				result = dbStatement.getResultSet();
				return result;
			}
		} catch (SQLException exn) {
			cp.updateBotConsole("Can't read from database: " + exn);
		} 
		return result;
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
		String query = "UPDATE reservations SET car = '"+car+"', reserved_from = '"+reserved_from+"', " +
					   "reserved_to = '"+reserved_to+"', customer = '"+customer+"' WHERE reservation_id = '"+id+"'";
		try {
			int ok = dbStatement.executeUpdate(query);
			if (ok == 1) {
				return true;
			}
			else {
				return false;
			}
		}
		catch (SQLException exn) {
			cp.updateBotConsole("Can't read from database: " + exn);
				return false;
		}
	}
	
	/**
	 * Deletes a reservation in DB
	 * @param id	reservation id
	 * @return	True if deleted, flase otherwise
	 */
	public boolean delReservation(int id){
		String query = "DELETE FROM reservations WHERE reservation_id = '"+id+"'";
		try {
			int ok = dbStatement.executeUpdate(query);
			if (ok == 1) {
				return true;
			}
			else{
				return false;
			}
		}
		catch (SQLException exn) {
			cp.updateBotConsole("Can't read from database: " + exn);
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
		String query = "INSERT INTO reservations (car, reserved_from, reserved_to, customer)" + " VALUES " +
					   "('"+car+"', '"+reserved_from+ "', '"+reserved_to+ "', '"+customer+"')";
		try {
			int ok = dbStatement.executeUpdate(query);
			if (ok == 1) {
				return true;
			}
			else {
				return false;
			}
		}
		catch (SQLException exn) {
			cp.updateBotConsole("Can't read from database: " + exn);
				return false;
		}
	}
}