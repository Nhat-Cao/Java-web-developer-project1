package com.udacity.jwdnd.course1.cloudstorage.test;

import com.udacity.jwdnd.course1.cloudstorage.page.HomePage;
import com.udacity.jwdnd.course1.cloudstorage.page.LoginPage;
import com.udacity.jwdnd.course1.cloudstorage.page.ResultPage;
import com.udacity.jwdnd.course1.cloudstorage.page.SignupPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.BooleanSupplier;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CredentialTest {
    @LocalServerPort
    private int port;

    private WebDriver driver;
    WebDriverWait webDriverWait;

    static String username;
    static String password="123";

    static int count = 0;

    public void signupAndLogin(){
        // access signup page
        driver.get("http://localhost:" + this.port + "/signup");

        SignupPage signupPage = new SignupPage(driver);
        signupPage.signup("test", "test", username, password);

        Assertions.assertTrue(driver.findElement(By.id("success-msg")).getText().contains("You successfully signed up!"));

        //wait for redirect to login page
        webDriverWait.until(ExpectedConditions.titleContains("Login"));
        Assertions.assertEquals("Login", driver.getTitle());

        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(username, password);

        // home page is accessible
        webDriverWait.until(ExpectedConditions.titleContains("Home"));
        Assertions.assertEquals("Home", driver.getTitle());
    }

    @BeforeAll
    static void beforeAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void beforeEach() {
        this.driver = new ChromeDriver();
        this.webDriverWait = new WebDriverWait(driver, 2);

        // changes another account if runs many tests
        username="testcredential"+count;
        count++;
        signupAndLogin();
    }

    @AfterEach
    public void afterEach() {
        if (this.driver != null) {
            driver.quit();
        }
    }

    @Test
    public void createCredential(){
        String url = "http://localhost:8080/login";
        String username = "test";
        String password = "test";

        HomePage homePage = new HomePage(driver);

        // create credential
        homePage.credentialTab.click();
        webDriverWait.until(ExpectedConditions.visibilityOf(homePage.addCredentialBtn));
        homePage.addCredentialBtn.click();
        webDriverWait.until(ExpectedConditions.visibilityOf(homePage.credentialUrl));
        homePage.inputCredentialData(url, username, password);

        // redirect to result page with successful message
        ResultPage resultPage = new ResultPage(driver);
        // back to home page
        resultPage.clickSuccessBack();

        // verify the credential is added
        homePage.credentialTab.click();
        webDriverWait.until(ExpectedConditions.visibilityOf(homePage.credentialEditBtns.get(0)));
        // password is encrypted
        Assertions.assertNotEquals(password, homePage.credentialPasswords.get(0).getText());

        homePage.credentialEditBtns.get(0).click();
        webDriverWait.until(ExpectedConditions.visibilityOf(homePage.credentialUrl));
        Assertions.assertEquals(url, homePage.credentialUrl.getAttribute("value"));
        Assertions.assertEquals(username, homePage.credentialUsername.getAttribute("value"));
        Assertions.assertEquals(password, homePage.credentialPassword.getAttribute("value"));
        homePage.closeCredentialModal.click();
    }

    @Test
    public void editCredential() throws InterruptedException {
        // create credential to edit
        createCredential();

        String url = "http://localhost:8080/edited";
        String username = "edited";
        String password = "edited";

        HomePage homePage = new HomePage(driver);

        // edit credential
        homePage.credentialTab.click();
        webDriverWait.until(ExpectedConditions.visibilityOf(homePage.credentialEditBtns.get(0)));
        homePage.credentialEditBtns.get(0).click();
        webDriverWait.until(ExpectedConditions.visibilityOf(homePage.credentialUrl));
        homePage.inputCredentialData(url, username, password);

        // redirect to result page with successful message
        ResultPage resultPage = new ResultPage(driver);
        // back to home page
        resultPage.clickSuccessBack();

        // verify the credential is edited
        homePage.credentialTab.click();
        webDriverWait.until(ExpectedConditions.visibilityOf(homePage.credentialEditBtns.get(0)));
        // password is encrypted
        Assertions.assertNotEquals(password, homePage.credentialPasswords.get(0).getText());

        homePage.credentialEditBtns.get(0).click();
        webDriverWait.until(ExpectedConditions.visibilityOf(homePage.credentialUrl));
        Assertions.assertEquals(url, homePage.credentialUrl.getAttribute("value"));
        Assertions.assertEquals(username, homePage.credentialUsername.getAttribute("value"));
        Assertions.assertEquals(password, homePage.credentialPassword.getAttribute("value"));
        homePage.closeCredentialModal.click();
    }

    @Test
    public void deleteCredential() throws InterruptedException {
        // create credential to delete
        createCredential();

        HomePage homePage = new HomePage(driver);

        // delete credential
        homePage.credentialTab.click();
        webDriverWait.until(ExpectedConditions.visibilityOf(homePage.credentialDeleteBtns.get(0)));
        homePage.credentialDeleteBtns.get(0).click();

        // redirect to result page with successful message
        ResultPage resultPage = new ResultPage(driver);
        // back to home page
        resultPage.clickSuccessBack();

        // verify the credential is deleted
        homePage.credentialTab.click();
        Assertions.assertTrue(homePage.credentialUrls.isEmpty());
        Assertions.assertTrue(homePage.credentialUsernames.isEmpty());
        Assertions.assertTrue(homePage.credentialPasswords.isEmpty());
    }
}

