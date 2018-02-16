
/**
 * @author Group21
 *
 */
public class Reservation extends Data {
	
	/**
	 * Initialize instance variables
	 */
	private int car;
	private String reserved_from;
	private String reserved_to;
	private int customer;
	private int startDay;
	private int startMonth;
	private int startYear;
	private int endDay;
	private int endMonth;
	private int endYear;
	private int reservedTime = 1;
	
	/**
	 * Creates a new reservation in array + DB
	 * @param id	Customer ID
	 * @param car	ID of car
	 * @param reserved_from	start date of reservation
	 * @param reserved_to	End date of reservation
	 * @param customer	ID of customer
	 */
	public Reservation(int id, int car, String reserved_from, String reserved_to, int customer){
		super(id);
		this.car = car;
		this.reserved_from = reserved_from;
		this.reserved_to = reserved_to;
		this.customer = customer;
		startDateToInt();
		endDateToInt();
		timeReserved();
		
	}
	
	
	/**
	 * @return the car
	 */
	public int getCar() {
		return car;
	}


	/**
	 * @param car the car to set
	 */
	public void setCar(int car) {
		this.car = car;
	}

	/**
	 * @param reserved_from the reserved_from to set
	 */
	public void setReserved_from(String reserved_from) {
		this.reserved_from = reserved_from;
		startDateToInt();
	}
	
	/**
	 * @param reserved_to the reserved_to to set
	 */
	public void setReserved_to(String reserved_to) {
		this.reserved_to = reserved_to;
		endDateToInt();
	}

	/**
	 * Return the start day of the reservation.
	 * @return int. The day the reservation starts.
	 */
	public int dayStart() {
		return startDay;
	}
	
	/**
	 * Return the start month of the reservation.
	 * @return int. The month the reservation starts.
	 */
	public int monthStart() {
		return startMonth;
	}
	
	/**
	 * Return the start year of the reservation.
	 * @return int. The year the reservation starts.
	 */
	public int yearStart() {
		return startYear;
	}
	
	/**
	 * Return the day the reservation ends.
	 * @return int. The day the reservation ends.
	 */
	public int dayEnd() {
		return endDay;
	}
	
	/**
	 * Return the month the reservation ends.
	 * @return int. The month the reservation ends.
	 */
	public int monthEnd() {
		return endMonth;
	}
	
	/**
	 * Return the year the reservation ends.
	 * @return int. The year the reservation ends.
	 */
	public int yearEnd() {
		return endYear;
	}
	
	/**
	 * @return the customer
	 */
	public int getCustomer() {
		return customer;
	}


	/**
	 * @param customer the customer to set
	 */
	public void setCustomer(int customer) {
		this.customer = customer;
	}

	
	public void testPrint(){
		super.testPrint();
		System.out.println("reserved from: " + reserved_from + ", reserved to: " + reserved_to);
	}
	/**
	 * Return the reserved time.
	 * @return int. The total amount of days the reservation is.
	 */
	public int timeOfReservation() {
		return reservedTime;
	}
	/**
	 * Calculate the total days the length of the reservation and stores it in reservedTime.
	 */
	private void timeReserved() {
		if(endYear<startYear) {
			for(int i=startYear; i<endYear && startMonth<=endMonth; i++) { //checks if the reservation is more than 11 months long.
				if(i%4==0 && i%100!=0 || i%400==0)//Checks if the year or years of the reservation is are leap years.
					reservedTime += 366;
				else
					reservedTime += 365;
			}
		}
		if(endMonth<startMonth) {
			int[] months = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
			reservedTime -= startDay;
			for(int i=startMonth; i<endMonth; i++) {
				if(endYear%4==0 && endYear%100!=0 && i==2 || endYear%400==0 && i==2)
					reservedTime += 29;
				else
					reservedTime += months[i];
			}		
			reservedTime += endDay;
		}
		if (endMonth==startMonth)
			reservedTime += endDay-startDay;
	}
	/**
	 * Change the date reserved_from from a string to 3 ints containing the day, month and year separately.
	 */
	private void startDateToInt() {
		String dayStart = "";
		String monthStart = "";
		String yearStart = "";
		for(int i=0; i<4; i++) 
			yearStart += reserved_from.charAt(i);
		
		for(int i=5; i<7 ; i++) 
			monthStart +=reserved_from.charAt(i);
		
		for(int i=8; i<10; i++) 
			dayStart += reserved_from.charAt(i);
			
		startDay = Integer.parseInt(dayStart);
		startMonth = Integer.parseInt(monthStart);
		startYear = Integer.parseInt(yearStart);
	}
	/**
	 * Change the date reserved_to from a string to 3 ints, containing the day, month and year separately.
	 */
	private void endDateToInt() {
		String dayEnd = "";
		String monthEnd = "";
		String yearEnd = "";
		for(int i=0; i<4; i++) 
			yearEnd += reserved_to.charAt(i);
		
		for(int i=5; i<7; i++)
			monthEnd += reserved_to.charAt(i);
		
		for(int i=8; i<10; i++)
			dayEnd += reserved_to.charAt(i);
		
		endDay = Integer.parseInt(dayEnd);
		endMonth = Integer.parseInt(monthEnd);
		endYear = Integer.parseInt(yearEnd);
	}


	public String getReserved_from() {
		return reserved_from;
	}


	public String getReserved_to() {
		return reserved_to;
	}
}
