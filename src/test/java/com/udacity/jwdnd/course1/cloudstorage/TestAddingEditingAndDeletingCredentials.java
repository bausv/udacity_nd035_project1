package com.udacity.jwdnd.course1.cloudstorage;

import static org.assertj.core.api.Assertions.*;
import com.udacity.jwdnd.course1.cloudstorage.model.Credentials;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.exception.CredentialsExistException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class TestAddingEditingAndDeletingCredentials extends CloudStorageApplicationTests {

    @Autowired
    private CredentialService credentialService;

    @Test
    public void loginExistingUserCreateCredentialsAndVerifyCredentialDetailsAreVisible() {
        given_anExistingLoggedInUser();
        when_aCrendetialIsCreated();
        then_theCrendentialIsVisible();
    }

    @Test
    public void loginExistingUserWithExistingCredentialsEditCredentialAndVerifyChangesAppear() {
        given_anExistingLoggedInUserWithCredentials();
        when_aCredentialIsEdited();
        then_theChangedCredentialsIsVisible();
    }

    @Test
    public void loginExistingUserWithExistingCredentialsDeleteCredentialAndVerifyItDisappeared() {
        given_anExistingLoggedInUserWithCredentials();
        when_aCredentialIsDeleted();
        then_theCredentialDisappeared();
    }

    private List<Credentials> given_anExistingLoggedInUserWithCredentials() {
        User user = given_anExistingLoggedInUser();
        Credentials cred = new Credentials(null, "url", user.getUsername(), null, user.getPassword(), user.getUserId());
        List<Credentials> creds = null;
        try {
            assertThat(credentialService).returns(CredentialService.CREDENTIAL_ADDED, from(cs -> {
                try {
                    return cs.addOrEditCredential(cred, user);
                } catch (CredentialsExistException e) {
                    throw new RuntimeException(e);
                }
            }));
            creds = credentialService.getCredentialsForUser(user);
        } catch (Exception e) {
            assertThat(e).doesNotThrowAnyException();
        }
        assertThat(creds).isNotNull().isNotEmpty().usingElementComparatorOnFields("url", "username", "password").contains(cred);
        return creds;
    }

    private CredentialPage when_aCrendetialIsCreated() {
        CredentialPage homePage = processToHomePage(CredentialPage.class);
        homePage.addCredAndCheckOnPage("http://some.url", "someUser", "somePass");
        return homePage;
    }

    private void when_aCredentialIsEdited() {
        processToHomePage(CredentialPage.class).editCredAndCheckOnPage();
    }

    private void when_aCredentialIsDeleted() {
        processToHomePage(CredentialPage.class).deleteCredAndCheckOnPage();
    }

    private void then_theCrendentialIsVisible() {
        // nothing to do here - already checked in CredentialPage.addCredAndCheckOnPage()
    }

    private void then_theChangedCredentialsIsVisible() {
        // nothing to do here - already checked in CredentialPage.editCredAndCheckOnPage()
    }

    private void then_theCredentialDisappeared() {
        // nothing to do here - already checked in CredentialPage.deleteCredAndCheckOnPage()
    }

}
