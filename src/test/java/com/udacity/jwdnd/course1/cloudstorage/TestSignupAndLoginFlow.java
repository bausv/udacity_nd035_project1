package com.udacity.jwdnd.course1.cloudstorage;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.from;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class TestSignupAndLoginFlow extends CloudStorageApplicationTests {

    @Test
    public void getLoginPage() throws InterruptedException {
        driver.get("http://localhost:" + this.port + "/login");
        Assertions.assertEquals("Login", driver.getTitle());
    }

    @Test
    public void verifyThatHomePageIsNotAccessibleWithoutLoggingIn() {
        driver.get(baseURL + "/signup");
        assertThat(driver).returns("Sign Up", from(WebDriver::getTitle));
        driver.get(baseURL + "/login");
        assertThat(driver).returns("Login", from(WebDriver::getTitle));
        driver.get(baseURL + "/home");
        assertThat(driver).returns("Login", from(WebDriver::getTitle));
    }

    @Test
    public void verifyThatUserCanAccessHomepageButNoLongerWhenLoggedOut() {
        createUserAndLogin();

        HomePage homePage = new HomePage(driver);
        assertThat(driver).returns("Home", from(WebDriver::getTitle));

        homePage.logout();

        assertThat(driver).returns("Login", from(WebDriver::getTitle));

        driver.get(baseURL + "/home");
        assertThat(driver).returns("Login", from(WebDriver::getTitle));

    }
}
