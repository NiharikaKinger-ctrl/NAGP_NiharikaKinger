package utils;

import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import java.util.logging.Logger;

public class AuthUtils {
	private static final Logger logger = Logger.getLogger(AuthUtils.class.getName());

    public static String createToken(String username, String password) {
    	logger.info("Creating token for user: " + username);
        String body = "{ \"username\": \"" + username + "\", \"password\": \"" + password + "\" }";
        Response response = given()
        		.baseUri("https://restful-booker.herokuapp.com")
        	    .contentType("application/json")
                .body(body)
                .post("/auth")
                .then()
                .extract()
                .response();

        try {
        	String token = response.jsonPath().getString("token");
        	logger.info("Token generated successfully");
        	return token;
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse token from response: " + response.asString(), e);
        }
    }
}