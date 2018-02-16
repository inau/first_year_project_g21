import java.util.ArrayList;
/**
 * @author Group21
 *
 */
public class Customer extends Data {
	
	/**
	 * Initialize instance variables
	 */
	private String name;
	private String address;
	private String phone;
	private String email;
	
	/**
	 * Creates a new customer in array + DB
	 * @param id	Customer ID
	 * @param name	Name of the customer
	 * @param address	Address of the customer
	 * @param phone		Phone of the customer
	 * @param email		E-mail of the customer
	 */
	public Customer(int id, String name, String address, String phone, String email){
		super(id);
		this.name = name;
		this.address = address;
		this.phone = phone;
		this.email = email;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * @param phone the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	
	public void testPrint(){
		super.testPrint();
		System.out.println("name: " + name + ", address: " + address);
	}
	
}
