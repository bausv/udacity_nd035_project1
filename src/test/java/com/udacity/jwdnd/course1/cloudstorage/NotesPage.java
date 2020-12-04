package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class NotesPage extends HomePage {

    @FindBy(id = "nav-notes-tab")
    private WebElement tabNotes;

    @FindBy(id = "button-new-note")
    private WebElement newNoteButton;

    @FindBy(id = "note-title")
    private WebElement inputNoteTitle;

    @FindBy(id = "note-description")
    private WebElement inputNoteDescription;

    @FindBy(id = "button-save-note")
    private WebElement saveNoteButton;
    @FindBy(id = "noteTitle1")
    private WebElement noteTitle;

    @FindBy(id = "editNoteButton1")
    private WebElement editNoteButton;

    @FindBy(id = "deleteNote1")
    private WebElement deleteNoteButton;

    public NotesPage(WebDriver webDriver) {
        super(webDriver);
    }

    public void addNoteAndCheckOnPage(String title, String description) {
        openNotesTab();
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", newNoteButton);
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].value='" + title + "';", inputNoteTitle);
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].value='" + description + "';", inputNoteDescription);
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", saveNoteButton);
        openNotesTab();
        waitForElementAndCheckValue(this.noteTitle, "noteTitle1", title);
    }

    public NotesPage editNoteAndCheckOnPage() {
        openNotesTab();

        new WebDriverWait(webDriver, 5).until(ExpectedConditions.visibilityOf(editNoteButton)).getText();
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", editNoteButton);
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].value+=' - edited';", inputNoteTitle);
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", saveNoteButton);

        openNotesTab();
        waitForElementAndCheckValue(noteTitle, "noteTitle1", " - edited");
//        new WebDriverWait(webDriver, 5).until(ExpectedConditions.visibilityOf(noteTitle)).getText();
//        WebElement noteTitle1 = webDriver.findElement(By.id("noteTitle1"));
//        assertThat(noteTitle1).isNotNull();
//        assertThat(noteTitle1.getText()).isNotNull().endsWith(" - edited");
        return this;
    }

    public void deleteNoteAndCheckOnPage() {
        openNotesTab();
        new WebDriverWait(webDriver, 5).until(ExpectedConditions.visibilityOf(deleteNoteButton)).getText();
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", deleteNoteButton);
        openNotesTab();
        assertThatThrownBy(() -> webDriver.findElement(By.id("noteTitle1"))).isInstanceOf(NoSuchElementException.class).hasMessageContaining("noteTitle1");
    }

    private void openNotesTab() {
        openTab(this.tabNotes);
    }
}
