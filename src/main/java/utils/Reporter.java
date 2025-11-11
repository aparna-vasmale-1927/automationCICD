package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Reporter {

	public static ExtentReports extent;
	public static ExtentTest test;
	private static String currentTestName = "";

	/**
	 * Initializes and sets up the Extent Report.
	 *
	 * @param filename The base name for the report file.
	 */
	public static void setupReport(String filename) {
		if (extent == null) {
			String reportDir = System.getProperty("user.dir") + File.separator + "reports";
			createDirectory(reportDir);

			String reportPath = reportDir + File.separator + filename + ".html";
			ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
			sparkReporter.config().setTheme(Theme.STANDARD);
			sparkReporter.config().setDocumentTitle("Automation Test Report");
			sparkReporter.config().setReportName("Execution Report");

			extent = new ExtentReports();
			extent.attachReporter(sparkReporter);
		}
	}

	/**
	 * Creates a new test node in the report for each test method.
	 *
	 * @param testName The name of the test method.
	 */
	public static void createTest(String testName) {
		if (extent != null) {
			test = extent.createTest(testName);
			currentTestName = testName.replaceAll("[^a-zA-Z0-9_\\-]", "_"); // clean filename
		} else {
			System.out.println("ExtentReports is not initialized. Call setupReport() first.");
		}
	}

	/**
	 * Logs informational steps during the test execution.
	 *
	 * @param stepDescription The description of the step.
	 */
	public static void logStep(String stepDescription) {
		if (test != null) {
			test.log(Status.INFO, stepDescription);
		} else {
			System.out.println("ExtentTest object is null. Logging to console: " + stepDescription);
		}
	}

	/**
	 * Logs a passed test step in the report.
	 *
	 * @param stepDescription The description of the step that passed.
	 */
	public static void logPass(String stepDescription) {
		if (test != null) {
			test.log(Status.PASS, MarkupHelper.createLabel(stepDescription, ExtentColor.GREEN));
		}
	}

	/**
	 * Logs a failed test step, saves a screenshot, and embeds it in the report.
	 *
	 * @param stepDescription The description of the failed step.
	 * @param driver          The WebDriver instance to capture the screenshot.
	 */
	public static void logFail(String stepDescription, WebDriver driver) {
		if (test != null) {
			try {
				test.log(Status.FAIL, MarkupHelper.createLabel(stepDescription, ExtentColor.RED));
				String screenshotPath = saveScreenshotToFile(driver);
				test.addScreenCaptureFromPath(screenshotPath, "Failed Step Screenshot");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Flushes and writes the report to disk.
	 */
	public static void flushReport() {
		if (extent != null) {
			extent.flush();
		}
	}

	/**
	 * Creates the specified directory if it doesn't exist.
	 *
	 * @param dirPath The directory path.
	 */
	private static void createDirectory(String dirPath) {
		File directory = new File(dirPath);
		if (!directory.exists()) {
			directory.mkdirs();
		}
	}

	/**
	 * Saves a screenshot to disk using the current test name.
	 *
	 * @param driver The WebDriver instance used to take the screenshot.
	 * @return Relative path to the saved screenshot.
	 * @throws Exception If screenshot capture or file saving fails.
	 */
	private static String saveScreenshotToFile(WebDriver driver) throws Exception {
		String screenshotDir = System.getProperty("user.dir") + File.separator + "screenshots";
		createDirectory(screenshotDir);

		// Timestamp to avoid file overwrite
		String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmssSSS").format(new Date());
		String screenshotName = currentTestName + "_" + timestamp + ".png";
		File screenshotFile = new File(screenshotDir + File.separator + screenshotName);

		File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		org.openqa.selenium.io.FileHandler.copy(srcFile, screenshotFile);

		// Return relative path for report embedding
		return "../screenshots/" + screenshotName;
	}
}
