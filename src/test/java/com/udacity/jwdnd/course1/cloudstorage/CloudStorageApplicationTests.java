package com.udacity.jwdnd.course1.cloudstorage;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.from;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class CloudStorageApplicationTests extends AbstractCloudStorageTest {

    public void testCreateEditAndDeleteNote() {
        createAndCheckNewNote().editNoteAndCheckOnPage().deleteNoteAndCheckOnPage();
    }

    public void testCreateEditAndDeleteCredential() {
        createAndCheckNewCred().editCredAndCheckOnPage().deleteCredAndCheckOnPage();
    }

    private CredentialPage createAndCheckNewCred() {
        CredentialPage homePage = processToHomePage(CredentialPage.class);
        homePage.addCredAndCheckOnPage("http://some.url", "someUser", "somePass");
        return homePage;
    }

    private NotesPage createAndCheckNewNote() {
        NotesPage homePage = processToHomePage(NotesPage.class);
        homePage.addNoteAndCheckOnPage("TestTitle", "a description for this note");
        return homePage;
    }

    protected <T> T processToHomePage(Class<T> clazz) {
        createUserAndLogin();
        driver.get(baseURL + "/home");
        try {
            Constructor<T> constructor = clazz.getConstructor(WebDriver.class);
            T t = constructor.newInstance(driver);
            return t;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }


}
