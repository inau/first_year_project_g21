import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import java.sql.*;


public class ControllerTest {
	Controller controller;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}
	
	public ControllerTest() {
		controller = new Controller();
				
	}
	
	@Test
	public void TestAll() {
		addCustomerTest1();
		addCustomerTest2();
		addCustomerTest3();
		addCustomerTest4();
		delCustomerTest1();
		delCustomerTest2();
		delCustomerTest3();
		editCustomerTest1();
		editCustomerTest2();
		editCustomerTest3();
		editCustomerTest4();
		editCustomerTest5();
		newReservationTest1();
		newReservationTest2();
		newReservationTest3();
		newReservationTest4();
		delReservationTest1();
		delReservationTest2();
		editReservationTest1();
		editReservationTest2();
		editReservationTest3();
		editReservationTest4();
		editReservationTest5();
		showReservationsMonthTest();
	}
	
	@Test
	public void addCustomerTest1() {
		boolean b = controller.addCustomer("Pual Richard", "", "47574738",
				"none@domain.com");
		assertFalse(b);
	}

	@Test
	public void addCustomerTest2() {
		boolean b = controller
				.addCustomer("", "Rosevej 64 2660 Brøndby Strand", "47574738",
						"none@domain.com");
		assertFalse(b);
	}

	@Test
	public void addCustomerTest3() {
		boolean b = controller
				.addCustomer("Paul Richard", "Rosevej 64 2660 Brøndby Strand",
						"47574738", "none@domain.com");
		assertTrue(b);
	}

	@Test
	public void addCustomerTest4() {
		boolean b = controller.addCustomer("Paul Richard",
				"Rosevej 64 2660 Brøndby Strand", "", "none@domain.com");
		assertFalse(b);
	}

	@Test
	public void delCustomerTest1() {
		boolean b = controller.delCustomer(-1);
		assertFalse(b);
	}

	@Test
	public void delCustomerTest2() {
		boolean b = controller.delCustomer(3);
		assertTrue(b);
	}

	@Test
	public void delCustomerTest3() {
		boolean b = controller.delCustomer(0);
		assertFalse(b);
	}

	@Test
	public void editCustomerTest1() {
		boolean b = controller
				.changeCustomer(0, "Paul Richard",
						"Rosevej 64 2660 Brøndby Strand", "47574738",
						"none@domain.com");
		assertFalse(b);
	}

	@Test
	public void editCustomerTest2() {
		boolean b = controller
				.changeCustomer(-1, "Paul Richard",
						"Rosevej 64 2660 Brøndby Strand", "47574738",
						"none@domain.com");
		assertFalse(b);
	}

	@Test
	public void editCustomerTest3() {
		boolean b = controller
				.changeCustomer(2, "Paul Richard",
						"Rosevej 64 2660 Brøndby Strand", "47574738",
						"none@doamin.com");
		assertTrue(b);
	}

	@Test
	public void editCustomerTest4() {
		boolean b = controller.changeCustomer(2, "",
				"Rosevej 64 2660 Brøndby Strand", "47574738", "");
		assertFalse(b);
	}

	@Test
	public void editCustomerTest5() {
		boolean b = controller.changeCustomer(2, "Paul Richard",
				"Rosevej 64 2660 Brøndby Strand", "47574738", "");
		assertTrue(b);
	}

	@Test
	public void newReservationTest1() {
		boolean b = controller.newReservation(-1, "20111217", "20111219", 1);
		assertFalse(b);
	}

	@Test
	public void newReservationTest2() {
		boolean b = controller.newReservation(1, "20111220", "20111219", 1);
		assertTrue(b);
	}

	@Test
	public void newReservationTest3() {
		boolean b = controller.newReservation(1, "20111217", "20111219", 1);
		assertTrue(true);
	}

	@Test
	public void newReservationTest4() {
		boolean b = controller.newReservation(1, "20111217", "20111219", -1);
		assertFalse(b);
	}

	@Test
	public void delReservationTest1() {
		boolean b = controller.deleteReservation(22);
		assertTrue(b);
	}

	@Test
	public void delReservationTest2() {
		boolean b = controller.deleteReservation(-1);
		assertFalse(b);
	}

	@Test
	public void editReservationTest1() {
		boolean b = controller.changeReservation(23, 1, "17122011", "19122011",
				2);
		assertTrue(true);
	}

	@Test
	public void editReservationTest2() {
		boolean b = controller.changeReservation(-1, 1, "17122011", "19122011",
				2);
		assertFalse(b);
	}

	@Test
	public void editReservationTest3() {
		boolean b = controller.changeReservation(23, -1, "17122011",
				"19122011", 2);
		assertFalse(b);
	}

	@Test
	public void editReservationTest4() {
		boolean b = controller.changeReservation(23, 1, "17122011", " ", 2);
		assertFalse(b);
	}

	@Test
	public void editReservationTest5() {
		boolean b = controller.changeReservation(23, 1, "19122011", "17122011",
				2);
		assertTrue(b);
	}
	
	@Test
	public void showReservationsMonthTest() {
		try {
		ResultSet r = controller.showReservationsMonth(2,2011);
		while(r.next()) {
			assertNull(r);
		}}
		catch(SQLException exn) {
			
		}
	}
	
	@Test
	public void showReservationsMonthTest2() {
		try {
			ResultSet r = controller.showReservationsMonth(0,2011);
			while(r.next()) {
				assertNotNull(r);
			}
		}
		catch(SQLException exn) {
			
		}
	}
	
	@Test
	public void showReservationsMonthTest3() {
		try {
			ResultSet r = controller.showReservationsMonth(13, 2011);
			while(r.next()) {
				assertNull(r);
			}
		}
		catch(SQLException exn) {
			
		}
	}
	
	@Test
	public void searchCarsTest1() {
		try {
			ResultSet r = controller.searchCars(-1, "01122011", "31122011");
			while(r.next()) {
				assertNull(r);
			}
		}
		catch(SQLException exn) {
		}
	}
	
	@Test
	public void searchCarsTest2() {
		try {
			ResultSet r = controller.searchCars(1, "01122011","31122011");
			while(r.next()) {
				assertNotNull(r);
			}
		}
		catch(SQLException exn) {
			
		}
	}
}
