package com.udacity.jwdnd.course1.cloudstorage;

import static org.assertj.core.api.Assertions.*;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HomePage {

    public static final String SET_VALUE = "arguments[0].value='";
    public static final String CHANGE_VALUE = "arguments[0].value+='";

    @FindBy(id = "logout-button")
    private WebElement logoutButton;

    protected WebDriver webDriver;


    public HomePage(WebDriver webDriver) {
        this.webDriver = webDriver;
        PageFactory.initElements(webDriver, this);
    }

    public void logout() {
        this.logoutButton.submit();
    }

    protected void waitForElementAndCheckValue(WebElement element, String id, String expected) {
        new WebDriverWait(webDriver, 5).until(ExpectedConditions.visibilityOf(element)).getText();
        WebElement noteTitle1 = webDriver.findElement(By.id(id));
        assertThat(noteTitle1).isNotNull();
        assertThat(noteTitle1.getText()).isNotNull().endsWith(expected);
    }

    protected void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected void clickButton(WebElement button) {
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", button);
    }

    protected void setValue(WebElement element, String value) {
        setOrChangeValue(element, value, SET_VALUE);
    }

    protected void changeValue(WebElement element, String value) {
        setOrChangeValue(element, value, CHANGE_VALUE);
    }

    protected void setOrChangeValue(WebElement element, String url, String operation) {
        ((JavascriptExecutor) webDriver).executeScript(operation + url + "';", element);
    }

    protected void openTab(WebElement tab) {
        new WebDriverWait(webDriver, 5).until(ExpectedConditions.visibilityOf(tab));
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", tab);
    }
}
