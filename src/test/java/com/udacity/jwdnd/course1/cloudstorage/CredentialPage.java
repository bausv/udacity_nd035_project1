package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class CredentialPage extends HomePage {

    public static final String CREDENTIAL_ELEMENT = "cred1";
    public static final String CHANGED_VALUE = " - edited";

    @FindBy(id = "nav-credentials-tab")
    private WebElement tabCreds;

    @FindBy(id = "button-new-cred")
    private WebElement newCredButton;

    @FindBy(id = "credential-url")
    private WebElement inputCredUrl;

    @FindBy(id = "credential-username")
    private WebElement inputCredUser;

    @FindBy(id = "credential-password")
    private WebElement inputCredPassword;

    @FindBy(id = "button-save-cred")
    private WebElement saveCredButton;

    @FindBy(id = "cred1")
    private WebElement credUrl;

    @FindBy(id = "editCredButton1")
    private WebElement editCredButton;

    @FindBy(id = "deleteCred1")
    private WebElement deleteCredButton;

    public CredentialPage(WebDriver webDriver) {
        super(webDriver);
    }

    public void addCredAndCheckOnPage(String url, String user, String password) {
        given_theCredentialsTab();
        when_aNewCredentialIsAdded(url, user, password);
        then_theNewCredentialIsAvailableOnTheTab(url);
    }

    public CredentialPage editCredAndCheckOnPage() {
        given_theCredentialsTabWithASpecificElement(CREDENTIAL_ELEMENT);
        when_theCredentialIsEdited(CHANGED_VALUE);
        then_isTheChangeReflectedOnTheTab(CHANGED_VALUE);
        return this;
    }

    public void deleteCredAndCheckOnPage() {
        given_theCredentialsTabWithASpecificElement(CREDENTIAL_ELEMENT);
        when_theCredentialIsDeleted();
        then_theCredTabDoesntContainTheElement(CREDENTIAL_ELEMENT);
    }

    private void given_theCredentialsTab() {
        openCredTab();
    }

    private void given_theCredentialsTabWithASpecificElement(String elementId) {
        given_theCredentialsTab();
        assertThat(webDriver.findElement(By.id(elementId))).isNotNull();
    }

    private void when_aNewCredentialIsAdded(String url, String user, String password) {
        clickButton(this.newCredButton);
        setValue(this.inputCredUrl, url);
        setValue(inputCredUser, user);
        setValue(inputCredPassword, password);
        clickButton(saveCredButton);
    }

    private void when_theCredentialIsEdited(String changedValue) {
        new WebDriverWait(webDriver, 5).until(ExpectedConditions.visibilityOf(editCredButton)).getText();
        clickButton(editCredButton);
        changeValue(inputCredUrl, changedValue);
        clickButton(saveCredButton);
    }

    private void when_theCredentialIsDeleted() {
        new WebDriverWait(webDriver, 5).until(ExpectedConditions.visibilityOf(deleteCredButton)).getText();
        clickButton(deleteCredButton);
    }

    private void then_theNewCredentialIsAvailableOnTheTab(String url) {
        openCredTab();
        waitForElementAndCheckValue(credUrl, CREDENTIAL_ELEMENT, url);
    }

    private void then_isTheChangeReflectedOnTheTab(String changedValue) {
        openCredTab();
        waitForElementAndCheckValue(credUrl, CREDENTIAL_ELEMENT, changedValue);
    }

    private void then_theCredTabDoesntContainTheElement(String elementId) {
        openCredTab();
        assertThatThrownBy(() -> webDriver.findElement(By.id(elementId))).isInstanceOf(NoSuchElementException.class).hasMessageContaining(elementId);
    }

    private void openCredTab() {
        this.openTab(tabCreds);
    }
}
