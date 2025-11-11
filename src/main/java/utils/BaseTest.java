package utils;

import org.testng.ITestResult;
import org.testng.annotations.*;
import org.testng.ITestContext;

import java.lang.reflect.Method;

/**
 * BaseTest class provides common setup and teardown functionality
 * for all test classes. It manages browser lifecycle and reporting.
 */
public class BaseTest extends BrowserManager {

    // Flag to ensure suite initialization happens only once
    private static boolean isSuiteInitialized = false;

    /**
     * Initializes the test suite and reporting before any tests run.
     * @param context TestNG context for the suite
     */
    @BeforeSuite
    public void setUpSuite(ITestContext context) {
        logStep("Current working directory: " + System.getProperty("user.dir"));

        if (!isSuiteInitialized) {
            String suiteName = context.getSuite().getName();
            logStep("Suite name: " + suiteName);
            Reporter.setupReport(suiteName); // Setup reporting for the suite
            isSuiteInitialized = true;
        }
    }

    /**
     * Sets up logging and browser before each test method.
     * @param method The test method about to run
     * @throws Exception
     */
    @BeforeMethod(alwaysRun = true)
    public void setUpLog(Method method) throws Exception {
        Reporter.createTest(method.getName());  // Create a test entry in the report
//        LoggerUtil.setLogFileName(method.getName());
        browserRun(); // Launch browser
    }

    /**
     * Handles reporting and browser teardown after each test method.
     * @param result The result of the test method
     */
    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        logResultStatus(result); // Log test result status
        driver.quit(); // Close browser
    }

    /**
     * Logs the status of the test result to the report.
     * @param result The result of the test method
     */
    private void logResultStatus(ITestResult result) {
        switch (result.getStatus()) {
            case ITestResult.SUCCESS:
                Reporter.logPass("**** " + result.getName() + " has PASSED ****");
                break;
            case ITestResult.FAILURE:
                Reporter.logFail("**** " + result.getName() + " has FAILED ****", driver);
                break;
            case ITestResult.SKIP:
                Reporter.logStep("**** " + result.getName() + " has been SKIPPED ****");
                break;
        }
    }

    /**
     * Flushes the report after the suite execution is complete.
     */
    @AfterSuite(alwaysRun = true)
    public void tearDownSuite() {
        Reporter.flushReport();
    }
}
