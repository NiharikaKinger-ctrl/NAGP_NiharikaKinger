package utils;

import java.util.logging.Logger;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import pojo.Booking;

public class ApiUtils {
	private static final Logger logger = Logger.getLogger(ApiUtils.class.getName());
    private static String token;

    public static void setToken(String authToken) {
        token = authToken;
        logger.info("Token set for API requests");
    }

    private static RequestSpecification getRequestSpecification() {
        RequestSpecification spec = RestAssured.given()
                .baseUri("https://restful-booker.herokuapp.com")
                .contentType("application/json")
                .accept("application/json");
        if (token != null) {
            spec.header("Cookie", "token=" + token);
        }
        return spec;
    }

    // Get booking IDs
    public static Response getBookingIds() {
    	logger.info("Sending GET request to fetch booking IDs");
        return getRequestSpecification()
                .get("/booking")
                .then()
                .extract()
                .response();
    }

    // Get booking by ID
    public static Response getBookingById(int bookingID) {
    	logger.info("Sending GET request to fetch booking by ID");
        return getRequestSpecification()
                .get("/booking/" + bookingID)
                .then()
                .extract()
                .response();
    }

    // POST method
    public static Response post(String uri, Object body) {
   	logger.info("Sending Post request to Create booking");
        return getRequestSpecification()
                .body(body)
                .post(uri)
                .then()
                .extract()
                .response();
    }

    // PUT method
    public static Response put(String uri, Object body) {
    	logger.info("Sending Put request to Update booking");
        return getRequestSpecification()
                .body(body)
                .put(uri)
                .then()
                .extract()
                .response();
    }
    
    // PATCH method
    public static Response patch(String uri, Object body) {
    	logger.info("Sending Patch request to partial update the booking");
        return getRequestSpecification()
                .body(body)
                .patch(uri)
                .then()
                .extract()
                .response();
    }

 // DELETE method
    public static Response delete(int bookingID) {
    	logger.info("Sending Delete request to delete the booking");
    	return getRequestSpecification()
                .delete("/booking/" + bookingID)
                .then()
                .extract()
                .response();
    }
    // GET method
    public static Response get(String uri) {
        return getRequestSpecification()
                .get(uri)
                .then()
                .extract()
                .response();
    }
}


