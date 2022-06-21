package com.springproject.prodcloud.controller;

import com.springproject.prodcloud.selenium_fun.ConfProperties;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;

public class LoginTest {

    private static WebDriver driver;


    @BeforeClass
    public static void setup() {
        //определение пути до драйвера и его настройка
        System.setProperty("webdriver.chrome.driver", ConfProperties.getProperty("chromedriver"));
        //создание экземпляра драйвера
        driver = new ChromeDriver();
        // profilePage = new ProfilePage(driver);
        //окно разворачивается на полный экран
        driver.manage().window().maximize();
        //задержка на выполнение теста = 10 сек.
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(10));

        //получение ссылки на страницу входа из файла настроек
        driver.get("http://localhost:8080/login");

    }


    @Test
    public void loginTest() {

        WebElement in_login = driver.findElement(By.id("username"));
        in_login.sendKeys("a");

        WebElement in_pass = driver.findElement(By.id("password"));
        in_pass.sendKeys("a");

        WebElement loginBtn = driver.findElement(By.id("loginBtn"));
        loginBtn.click();

//        WebDriverWait dynamicElement =  new WebDriverWait(driver, 20);
//        dynamicElement.until(ExpectedConditions.presenceOfElementLocated(By.id("shavForm")));

        String title = driver.getTitle();
        Assert.assertEquals("Shaverma Cloud: Create your food", title);
    }

    @Test
    public void logoutTest() {
        WebElement logoutBtn = driver.findElement(By.id("logoutBtn"));
        logoutBtn.click();
    }


    @AfterClass
    public static void tearDown() {
        driver.quit();
    }

}