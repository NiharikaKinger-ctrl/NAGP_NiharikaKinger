package base;

import io.restassured.RestAssured;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import utils.ConfigManager;
import reports.ExtentTestManager;

public class BaseTest {

    @BeforeSuite
    public void setup() {
        // Initialize RestAssured base URI
        String baseURI = ConfigManager.getProperty("baseURI");
        RestAssured.baseURI = baseURI;
        System.out.println("Base URI: " + baseURI);

        // Initialize ExtentReports
        ExtentTestManager.setupExtentReports();
    }

    @AfterSuite
    public void tearDown() {
        // Flush ExtentReports
        ExtentTestManager.flush();
    }
}
