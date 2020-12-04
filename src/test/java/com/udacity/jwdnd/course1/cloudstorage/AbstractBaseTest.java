package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.mapper.TestDeleteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class AbstractBaseTest {

    protected static final Logger log = LoggerFactory.getLogger(AbstractBaseTest.class);

    @Autowired
    private TestDeleteMapper deleter;

    @Autowired
    private UserService userService;

    public User getUser(String userName, String password, String firstName, String lastName) {
        User user = new User(null, userName, "salt", password, firstName, lastName);
        return userService.createAndGetUser(user);
    }

    public User getDefaultUser() {
        return getUser("username", "pass", "first", "last");
    }

    @BeforeEach
    public void beforeEach() {
        clearDatabase();
    }

    @AfterEach
    public void afterEach() {
        clearDatabase();
    }

    private void clearDatabase() {
        log.debug("deleting all data from database...");
        int total =
                deleter.deleteAllCredentials() + deleter.deleteAllFiles() + deleter.deleteAllNotes() + deleter.deleteAllUsers();
        log.debug("deleted " + total + " rows");
    }
}
