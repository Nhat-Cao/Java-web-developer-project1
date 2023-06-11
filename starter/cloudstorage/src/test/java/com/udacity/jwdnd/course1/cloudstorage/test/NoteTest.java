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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class NoteTest {
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

        // change another account if run many tests
        username="testnote"+count;
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
    public void createNote(){
        String title = "Note 1";
        String description = "This is the note 1";

        HomePage homePage = new HomePage(driver);

        // create note
        homePage.noteTab.click();
        webDriverWait.until(ExpectedConditions.visibilityOf(homePage.addNoteBtn));
        homePage.addNoteBtn.click();
        webDriverWait.until(ExpectedConditions.visibilityOf(homePage.noteTitle));
        homePage.inputNoteData(title, description);

        // redirect to result page with successful message
        ResultPage resultPage = new ResultPage(driver);
        // back to home page
        resultPage.clickSuccessBack();

        // verify the note is added
        homePage.noteTab.click();
        webDriverWait.until(ExpectedConditions.elementToBeClickable(homePage.addNoteBtn));
        Assertions.assertEquals(title, homePage.noteTitles.get(0).getText());
        Assertions.assertEquals(description, homePage.noteDescriptions.get(0).getText());
    }

    @Test
    public void editNote(){
        // create note to edit
        createNote();

        String title = "Note 1 edited";
        String description = "This is the edited note";

        HomePage homePage = new HomePage(driver);

        // edit note
        homePage.noteTab.click();
        webDriverWait.until(ExpectedConditions.visibilityOf(homePage.noteEditBtns.get(0)));
        homePage.noteEditBtns.get(0).click();
        webDriverWait.until(ExpectedConditions.visibilityOf(homePage.noteTitle));
        homePage.inputNoteData(title, description);

        // redirect to result page with successful message
        ResultPage resultPage = new ResultPage(driver);
        // back to home page
        resultPage.clickSuccessBack();

        // verify the note is edited
        homePage.noteTab.click();
        webDriverWait.until(ExpectedConditions.elementToBeClickable(homePage.addNoteBtn));
        Assertions.assertEquals(title, homePage.noteTitles.get(0).getText());
        Assertions.assertEquals(description, homePage.noteDescriptions.get(0).getText());
    }

    @Test
    public void deleteNote(){
        // create note to delete
        createNote();

        HomePage homePage = new HomePage(driver);

        // delete note
        homePage.noteTab.click();
        webDriverWait.until(ExpectedConditions.visibilityOf(homePage.noteDeleteBtns.get(0)));
        homePage.noteDeleteBtns.get(0).click();

        // redirect to result page with successful message
        ResultPage resultPage = new ResultPage(driver);
        // back to home page
        resultPage.clickSuccessBack();

        // verify the note is deleted
        homePage.noteTab.click();
        Assertions.assertTrue(homePage.noteTitles.isEmpty());
        Assertions.assertTrue(homePage.noteDescriptions.isEmpty());
    }
}
