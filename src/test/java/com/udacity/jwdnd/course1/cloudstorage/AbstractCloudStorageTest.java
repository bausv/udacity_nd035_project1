package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NotesMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.LocalServerPort;

public abstract class AbstractCloudStorageTest extends AbstractBaseTest {

    protected static final Logger log = LoggerFactory.getLogger(AbstractCloudStorageTest.class);

    @LocalServerPort
    protected int port;

    protected WebDriver driver;
    protected String baseURL;

    @Autowired
    protected NotesMapper notesMapper;

    @Autowired
    protected UserService userService;

    @BeforeAll
    static void beforeAll() {
        WebDriverManager.chromedriver().timeout(5000);
        WebDriverManager.chromedriver().setup();
        //WebDriverManager.firefoxdriver().setup();
    }

    @BeforeEach
    public void beforeEach() {
        super.beforeEach();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless", "--disable-gpu", "--window-size=1920,1200","--ignore-certificate-errors");
        this.driver = new ChromeDriver(options);
        //this.driver = new FirefoxDriver();
        baseURL = "http://localhost:" + port;
    }

    @AfterEach
    public void afterEach() {
        super.afterEach();
        if (this.driver != null) {
            driver.quit();
        }
    }

    protected User given_anExistingLoggedInUser() {
        return createUserAndLogin();
    }

    protected User createUserAndLogin() {
        String username = "someUser";
        String password = "somePassword";
        String messageText = "Hello!";


        driver.get(baseURL + "/signup");

        SignupPage signupPage = new SignupPage(driver);
        signupPage.signup("UserFirst", "UserLast", username, password);

        User user = userService.getUser(username);

        driver.get(baseURL + "/login");

        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(username, password);
        return user;
    }
}
