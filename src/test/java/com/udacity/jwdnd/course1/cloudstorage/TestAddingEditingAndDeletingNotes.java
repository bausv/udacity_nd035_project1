package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.model.Credentials;
import com.udacity.jwdnd.course1.cloudstorage.model.Notes;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.NotesService;
import com.udacity.jwdnd.course1.cloudstorage.services.exception.CredentialsExistException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.from;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class TestAddingEditingAndDeletingNotes extends CloudStorageApplicationTests {

    @Autowired
    private NotesService notesService;

    @Test
    public void loginExistingUserCreateNoteAndVerifyNoteDetailsAreVisible() {
        given_anExistingLoggedInUser();
        when_aNoteIsCreated();
        then_theNoteIsVisible();
    }

    @Test
    public void loginExistingUserWithExistingNotesEditNoteAndVerifyChangesAppear() {
        given_anExistingLoggedInUserWithNote();
        when_aNoteIsEdited();
        then_theChangedCredentialsIsVisible();

    }

    @Test
    public void loginExistingUserWithExistingNotesDeleteNoteAndVerifyItDisappeared() {
        given_anExistingLoggedInUserWithNote();
        when_aNoteIsDeleted();
        then_theCredentialDisappeared();

    }

    private void given_anExistingLoggedInUserWithNote() {
        User user = given_anExistingLoggedInUser();
        Notes note = new Notes(null, "title", "description", user.getUserId());
        notesService.addNewNote(user, note);
    }

    private NotesPage when_aNoteIsCreated() {
        NotesPage homePage = processToHomePage(NotesPage.class);
        homePage.addNoteAndCheckOnPage("title", "description");
        return homePage;
    }

    private void when_aNoteIsEdited() {
        processToHomePage(NotesPage.class).editNoteAndCheckOnPage();
    }

    private void when_aNoteIsDeleted() {
        processToHomePage(NotesPage.class).deleteNoteAndCheckOnPage();
    }

    private void then_theNoteIsVisible() {
        // nothing to do here - already checked in NotesPage.addNoteAndCheckOnPage()
    }

    private void then_theChangedCredentialsIsVisible() {
        // nothing to do here - already checked in NotesPage.editNoteAndCheckOnPage()
    }

    private void then_theCredentialDisappeared() {
        // nothing to do here - already checked in NotesPage.deleteNoteAndCheckOnPage()
    }

}
