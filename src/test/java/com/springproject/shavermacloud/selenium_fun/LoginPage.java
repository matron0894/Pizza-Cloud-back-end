package com.springproject.shavermacloud.selenium_fun;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

@Slf4j
public class LoginPage {

    /**
     * конструктор класса, занимающийся инициализацией полей класса
     */
    private final WebDriver driver;

    public LoginPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this.driver = driver;
    }


    @FindBy(xpath = "//*[contains(@id, 'passp-field-login')]")
    private WebElement loginField;


//    @FindBy(xpath = "//*[contains(text(), 'Войти')]")
    @FindBy(xpath = "//*[@id='passp:sign-in']")
    private WebElement loginBtn;


    @FindBy(xpath = "//*[contains(@id, 'passp-field-passwd')]")
    private WebElement passwdField;


    public void inputLogin(String login) {
        loginField.sendKeys(login);
    }

    public void clickLoginBtn() {
        loginBtn.click();
    }

    public void inputPasswd(String passwd) {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
        passwdField.sendKeys(passwd);
    }
}
