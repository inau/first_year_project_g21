import java.sql.*;

/**
 * 
 * @author Group21
 * 
 */
public class CustomerDb {

	/**
	 * Initiate instance variables
	 */

	private final Statement dbStatement;
	private ConsolePanel cp;

	/**
	 * 
	 * @param conn what connection to use
	 * @throws SQLException
	 */
	public CustomerDb(Connection conn, ConsolePanel cp) throws SQLException {
		this.cp = cp;
		this.dbStatement = conn.createStatement();

	}
	
	/**
	 * 
	 * @return	A RestultSet with all the customers from the database
	 */
	public ResultSet getCustomers() {
		ResultSet result = null;
		String query = "SELECT * FROM customer " + "ORDER BY name";
		try {
			boolean ok = dbStatement.execute(query);
			if (ok) {
				result = dbStatement.getResultSet();
				return result;
			}
		} 
		catch (SQLException exn) {
			cp.updateBotConsole("Can't read from database: " + exn);
		} 
		return result;
	}
	
/**
 * 
 * @param id	int id of the customer
 * @return		A RestultSet with a specific customer
 */
	public ResultSet getCustomer(int id) {
		ResultSet result = null;
		String query = "SELECT * FROM customer " + "ORDER BY name WHERE customer_id = '"+id+"'";
		try {
			boolean ok = dbStatement.execute(query);
			if (ok) {
				result = dbStatement.getResultSet();
				return result;
			}
		} 
		catch (SQLException exn) {
			cp.updateBotConsole("Can't read from database: " + exn);
		} 
		return result;
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
		String query = "UPDATE customer SET name = '"+name+"', address = '"+address+"', phone = '"+phone+"', email = '"+email+"' WHERE customer_id = '"+id+"'";
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
	 * Deletes a customer
	 * @param id	ID of the customre
	 * @return	True if customer is deleted, false otherwise.
	 */
	public boolean delCustomer(int id){
		String query = "DELETE FROM customer WHERE customer_id = '"+id+"'";
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
	 * Adds a new customer
	 * @param name	name of the customer
	 * @param address	address of the customer
	 * @param phone	phone of the customer
	 * @param email	email  of the customer
	 * @return	True if the customer is added, false otherwise
	 */
	public boolean addCustomer(String name, String address, String phone, String email){
		String query = "INSERT INTO customer (name, address, phone, email)" + " VALUES ('"+name+"', '"+address+ "', '"+phone+ "', '"+email+"')";
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