package utils;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.util.logging.Logger;

public class BaseUtils extends GenerateTestData {

    public static WebDriver driver;
    private static final Logger LOGGER = LoggerUtil.getLogger();

    /* Logger Methods */
    public static void logStep(String message) {
        LOGGER.info(message);
        Reporter.logStep(message);
    }

    public static void logException(String exception) {
        LOGGER.severe(exception);
    }

    /* Assertion Methods */
    public static void assertEquals(Object expected, Object actual, String message) {
        Assert.assertEquals(actual, expected, message);
    }

    public static void assertTrue(boolean condition, String message) throws Exception {
        try {
            if (!condition) {
                Assert.assertTrue(condition, message);
                fail(message);
            }
        } catch (Exception e) {
            logStep(e.getMessage());
        }
    }

    /* Element Interaction Methods */
    public static void click(WebElement element) {
        try {
            Actions actions = new Actions(driver);
            actions.moveToElement(element).click().perform();
        } catch (Exception e) {
            logStep("Failed to click element: " + e.getMessage());
        }
    }
    /* Element Interaction Methods */
    public static void sendText(WebElement element, String text) {
        try {
            Actions actions = new Actions(driver);
            actions.moveToElement(element).click().sendKeys(text).perform();
        } catch (Exception e) {
            logStep("Failed to send text: " + text + " " + e.getMessage());
        }
    }

    /* Wait Methods */
    public static void waitForElement(WebElement element, int timeoutInSeconds) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
            wait.until(ExpectedConditions.visibilityOf(element));
        } catch (Exception e) {
            logStep("Element not visible: " + e.getMessage());
        }
    }

    public static void waitForUIToLoad(WebDriver driver, int timeoutInSeconds) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
            wait.until(webDriver -> ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete"));
            logStep("UI has fully loaded.");
        } catch (TimeoutException e) {
            logStep("Timeout occurred while waiting for the UI to load: " + e.getMessage());
        } catch (Exception e) {
            logStep("An unexpected error occurred while waiting for the UI to load: " + e.getMessage());
        }
    }

    public static void waitForClickable(WebElement element, int timeoutInSeconds) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
            wait.until(ExpectedConditions.elementToBeClickable(element));
        } catch (Exception e) {
            logStep("Element not clickable: " + e.getMessage());
        }
    }

    /* Popup Handling */
    public static String handlePopup(boolean accept) {
        try {
            Alert alert = driver.switchTo().alert();
            String message = alert.getText();
            if (accept) {
                alert.accept();
            } else {
                alert.dismiss();
            }
            return message;
        } catch (NoAlertPresentException e) {
            logStep("No popup present: " + e.getMessage());
            return null;
        }
    }

    /* Dropdown Handling */
    public static void selectByVisibleText(WebElement element, String text) {
        try {
            Select dropdown = new Select(element);
            dropdown.selectByVisibleText(text);
            logStep("Selected '" + text + "' from dropdown.");
        } catch (Exception e) {
            logStep("Failed to select from dropdown: " + e.getMessage());
        }
    }

    public static void selectByValue(WebElement element, String value) {
        try {
            Select dropdown = new Select(element);
            dropdown.selectByValue(value);
            logStep("Selected value '" + value + "' from dropdown.");
        } catch (Exception e) {
            logStep("Failed to select value from dropdown: " + e.getMessage());
        }
    }

    public static void selectByIndex(WebElement element, int index) {
        try {
            Select dropdown = new Select(element);
            dropdown.selectByIndex(index);
            logStep("Selected index '" + index + "' from dropdown.");
        } catch (Exception e) {
            logStep("Failed to select index from dropdown: " + e.getMessage());
        }
    }

    /* Element Visibility */
    public static boolean isElementVisible(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (Exception e) {
            logStep("Element not visible: " + e.getMessage());
            return false;
        }
    }

    public static boolean isElementEnabled(WebElement element) {
        try {
            return element.isEnabled();
        } catch (Exception e) {
            logStep("Element not enabled: " + e.getMessage());
            return false;
        }
    }

    /* Retrieve Text */
    public static String getElementText(WebElement element) {
        try {
            return element.getText();
        } catch (Exception e) {
            logStep("Failed to retrieve text: " + e.getMessage());
            return null;
        }
    }

    /* Screenshot Capture */
    public static void takeScreenshot(String filePath) {
        try {
            TakesScreenshot screenshot = (TakesScreenshot) driver;
            File srcFile = screenshot.getScreenshotAs(OutputType.FILE);
            File destFile = new File(filePath);
            Files.copy(srcFile.toPath(), destFile.toPath());
            logStep("Screenshot saved at: " + filePath);
        } catch (IOException e) {
            logStep("Failed to save screenshot: " + e.getMessage());
        } catch (Exception e) {
            logStep("An error occurred while taking a screenshot: " + e.getMessage());
        }
    }

    /* Scroll to Top or Bottom */
    public static void scrollToTop() {
        try {
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 0);");
            logStep("Scrolled to the top of the page.");
        } catch (Exception e) {
            logStep("Failed to scroll to the top: " + e.getMessage());
        }
    }

    public static void scrollToBottom() {
        try {
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
            logStep("Scrolled to the bottom of the page.");
        } catch (Exception e) {
            logStep("Failed to scroll to the bottom: " + e.getMessage());
        }
    }

    protected static void fail(String message) throws Exception {
        LOGGER.info(message);
        Reporter.logFail(message, driver);
        Reporter.logStep(message);
    }
}