package Tests;

import Pages.Login;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import utils.BaseTest;
import utils.ReadExcelData;

import java.io.IOException;

public class LoginTest extends BaseTest {

    @Test
    public void loginTest() throws IOException {

        Login loginPage = PageFactory.initElements(driver, Login.class);
        openFEUrl();
        loginPage.login("varunbhosale7+0123456789@gmail.com", "Varun@123");
    }

    @DataProvider(name="login_data")
    public Object[][] getNegativeData()
    {
        ReadExcelData readExcel=ReadExcelData.getInstance();
        readExcel.setPath(System.getProperty("user.dir")+"/src/main/resources/");
        return ReadExcelData.getExcelDataIn2DArray("login_data.xlsx","Sheet1");
    }


    @Test(dataProvider = "login_data")
    public void loginTestWithDataProvider(String username, String password) throws IOException {
        Login loginPage = PageFactory.initElements(driver, Login.class);
        openFEUrl();
        loginPage.login(username, password);
    }

}
