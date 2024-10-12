package tests;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

import base.BaseTest;
import io.restassured.response.Response;
import pojo.Booking;
import pojo.BookingDates;
import reports.ExtentTestManager;
import utils.ApiUtils;
import utils.AuthUtils;
import utils.ConfigManager;

public class BookingTests extends BaseTest {
	private static final Logger logger = Logger.getLogger(BookingTests.class.getName());
	// Shared variable to store the booking ID
    private int bookingId;
    private String token;
    
    @BeforeClass
    public void setUp() throws IOException {
    	LogManager.getLogManager().readConfiguration(getClass().getClassLoader().getResourceAsStream("logging.properties"));
        
    	// Generate token
       token = AuthUtils.createToken(
                ConfigManager.getProperty("username"), 
                ConfigManager.getProperty("password")
        );
     // Set token in ApiUtils
        ApiUtils.setToken(token);
        logger.info("Token generated and set successfully :" + token);
        Assert.assertNotNull(token, "Token should not be null");
        
     // Setup FileHandler for logging in BookingTests.log
        FileHandler fh = new FileHandler("logs/BookingTests.log");
        fh.setFormatter(new SimpleFormatter());
        logger.addHandler(fh);
    }
   
    @Test(priority=0)
    public void testGetBookingIds() {
        // Create a test instance in ExtentReports
        ExtentTestManager.createTest("GetBookingIds");

        // Fetch the response from the API
        Response response = ApiUtils.getBookingIds();

        // Log the response to the console and file
        logger.info("Booking IDs: " + response.asString());

        // Log the response in the ExtentReports
        ExtentTestManager.getTest().log(Status.INFO, "Response: " + response.asString());

        // Assert that the status code is 200
        Assert.assertEquals(response.getStatusCode(), 200);
        response.prettyPeek();
    }
    
    @Test(priority=1)
    public void testCreateBooking() {
    	ExtentTestManager.createTest("CreateBooking");
    	
    	 // Create Booking object
        Booking booking = new Booking();
        booking.setFirstname("John");
        booking.setLastname("Doe");
        booking.setTotalprice(111);
        booking.setDepositpaid(true);

        BookingDates bookingDates = new BookingDates();
        bookingDates.setCheckin("2024-04-04");
        bookingDates.setCheckout("2024-05-05");

        booking.setBookingdates(bookingDates);
        booking.setAdditionalneeds("Breakfast");

        Response response = ApiUtils.post("/booking", booking);
        logger.info("Create booking Response: " + response.asString());
        
        // Check if ExtentTestManager is not null before logging
        if (ExtentTestManager.getTest() != null) {
            ExtentTestManager.getTest().log(Status.INFO, "Response: " + response.asString());
        }
        Assert.assertEquals(response.getStatusCode(), 200);
        response.prettyPeek();
        
     // Extract the booking ID from the response and store it in the shared variable
        bookingId = response.jsonPath().getInt("bookingid");
        }
    
   @Test(priority=2 ,dependsOnMethods = "testCreateBooking")
    public void testGetBookingById() {
    	ExtentTestManager.createTest("GetBookingById");
        // Fetch the booking details using the dynamically generated booking ID
        Response response = ApiUtils.getBookingById(bookingId);
        logger.info("Get Booking by ID: " + response.asString());
        ExtentTestManager.getTest().log(Status.INFO, "Response: " + response.asString());
        
        Assert.assertEquals(response.getStatusCode(), 200);
        response.prettyPeek();
        
     // Validate the response using JSON path assertions
        Assert.assertEquals(response.jsonPath().getString("firstname"), "John");
        Assert.assertEquals(response.jsonPath().getString("lastname"), "Doe");
        Assert.assertEquals(response.jsonPath().getInt("totalprice"), 111);
        Assert.assertTrue(response.jsonPath().getBoolean("depositpaid"));
        Assert.assertEquals(response.jsonPath().getString("bookingdates.checkin"), "2024-04-04");
        Assert.assertEquals(response.jsonPath().getString("bookingdates.checkout"), "2024-05-05");
        Assert.assertEquals(response.jsonPath().getString("additionalneeds"), "Breakfast");

    }

    @Test(priority=3, dependsOnMethods = "testCreateBooking")
    public void testUpdateBooking() {
    	ExtentTestManager.createTest("UpdateBooking");
    	  // Ensure the booking ID is set
        Assert.assertNotEquals(bookingId, 0, "Booking ID should not be zero");

        // Update Booking object
        Booking booking = new Booking();
        booking.setFirstname("Niharika");
        booking.setLastname("Kinger");
        booking.setTotalprice(222);
        booking.setDepositpaid(true);

        BookingDates bookingDates = new BookingDates();
        bookingDates.setCheckin("2023-12-12");
        bookingDates.setCheckout("2024-01-01");

        booking.setBookingdates(bookingDates);
        booking.setAdditionalneeds("Breakfast and Dinner");
        
        // Send the PUT request to update the booking using ApiUtils
        Response updateResponse = ApiUtils.put("/booking/" + bookingId, booking);
        logger.info("Update booking Response: " + updateResponse.asString());
        ExtentTestManager.getTest().log(Status.INFO, "Response: " + updateResponse.asString());
        
        // Assert the response status code
        Assert.assertEquals(updateResponse.getStatusCode(), 200);
        updateResponse.prettyPeek();

        // Validate the update by fetching the updated booking details
        Response getResponse = ApiUtils.get("/booking/" + bookingId);
        Assert.assertEquals(getResponse.getStatusCode(), 200);
        Assert.assertEquals(getResponse.jsonPath().getString("firstname"), "Niharika");
        Assert.assertEquals(getResponse.jsonPath().getString("lastname"), "Kinger");
        Assert.assertEquals(getResponse.jsonPath().getInt("totalprice"), 222);
        Assert.assertEquals(getResponse.jsonPath().getBoolean("depositpaid"), true);
        Assert.assertEquals(getResponse.jsonPath().getString("bookingdates.checkin"), "2023-12-12");
        Assert.assertEquals(getResponse.jsonPath().getString("bookingdates.checkout"), "2024-01-01");
        Assert.assertEquals(getResponse.jsonPath().getString("additionalneeds"), "Breakfast and Dinner");  
}
    
    @Test(priority=4,dependsOnMethods = "testCreateBooking")
    public void testPartialUpdateBooking() {
    	ExtentTestManager.createTest("PartialUpdateBooking");
        // Partial update payload
        String body = "{ \"firstname\": \"James updated\", \"lastname\": \"Brown updated\" }";
        Response response = ApiUtils.patch("/booking/" + bookingId, body);
        logger.info("Partial Update Response: " + response.asString());

        // Check if ExtentTestManager is not null before logging
        if (ExtentTestManager.getTest() != null) {
            ExtentTestManager.getTest().log(Status.INFO, "Partial Update Response: " + response.asString());
        }
        Assert.assertEquals(response.getStatusCode(), 200);
        response.prettyPeek();

        // Validate the updated booking details
        String firstname = response.jsonPath().getString("firstname");
        String lastname = response.jsonPath().getString("lastname");
        Assert.assertEquals(firstname, "James updated", "Firstname should be updated to James");
        Assert.assertEquals(lastname, "Brown updated", "Lastname should be updated to Brown");
    }
    
    @Test (priority=5,dependsOnMethods = "testCreateBooking")
    public void testDeleteBooking() {
    	ExtentTestManager.createTest("DeleteBooking");
        Assert.assertNotEquals(bookingId, 0, "Booking ID should not be zero");
        Response response = ApiUtils.delete(bookingId);
        logger.info("Delete Response: " + response.asString());
        ExtentTestManager.getTest().log(Status.INFO, "Response: " + response.asString());
        Assert.assertEquals(response.getStatusCode(), 201);
        response.prettyPeek();

    }
}

