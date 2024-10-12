package reports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentTestManager {
    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> testThreadLocal = new ThreadLocal<>();

    public static void setupExtentReports() {
        // Initialize the SparkReporter
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter("extent-spark.html");
        sparkReporter.config().setTheme(Theme.STANDARD);
        sparkReporter.config().setDocumentTitle("Extent Reports");
        sparkReporter.config().setReportName("Test Report");

        // Initialize ExtentReports and attach the SparkReporter
        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);

        // General information related to the report
        extent.setSystemInfo("OS", "Windows");
        extent.setSystemInfo("User", "Tester");
    }

    public static synchronized ExtentTest getTest() {
        return testThreadLocal.get();
    }

    public static synchronized ExtentTest createTest(String name) {
        ExtentTest test = extent.createTest(name);
        testThreadLocal.set(test);
        return test;
    }

    public static synchronized void flush() {
        if (extent != null) {
            extent.flush();
        }
    }
}

