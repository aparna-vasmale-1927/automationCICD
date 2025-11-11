package Pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import utils.BaseUtils;

public class Login extends BaseUtils {
    public Login ()
    {
        PageFactory.initElements(driver,this);
    }

    @FindBy(xpath="//input[@id='username']")
    private WebElement username;

    @FindBy(xpath="//input[@id='password']")
    private WebElement password;

    @FindBy(xpath="//button[@type='submit']")
    private WebElement loginButton;


    public void enterUsername(String user) {
        username.sendKeys(user);
    }
    public void enterPassword(String pass) {
        password.sendKeys(pass);
    }
    public void clickLoginButton() {
        loginButton.click();
    }
    public void login(String user, String pass) {
        enterUsername(user);
        enterPassword(pass);
        clickLoginButton();
    }






}
