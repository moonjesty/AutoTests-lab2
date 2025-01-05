package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.Keys;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;

public class lab2Base {
    private WebDriver chromeDriver; // Об'єкт для керування браузером
    private static final String baseUrl = "https://www.nmu.org.ua/ua/"; // Основна URL сторінка

    // Виконується перед усіма тестами, налаштовує браузер
    @BeforeClass(alwaysRun = true)
    public void SetUp(){
        // Налаштування драйвера для Chrome
        WebDriverManager.chromedriver().setup();
        ChromeOptions chromeOptions = new ChromeOptions();

        chromeOptions.addArguments("--start-fullscreen"); // Запуск у повноекранному режимі
        chromeOptions.setImplicitWaitTimeout(Duration.ofSeconds(15)); // Таймаут на пошук елементів

        this.chromeDriver = new ChromeDriver(chromeOptions); // Створення екземпляру браузера
    }

    // Виконується перед кожним тестом, переходить на основну сторінку
    @BeforeMethod
    public void preconditions() {
        chromeDriver.get(baseUrl); // Перехід на головну сторінку
    }

    // Виконується після всіх тестів, закриває браузер
    @AfterClass(alwaysRun = true)
    public void tearDown(){
        chromeDriver.quit(); // Закриття браузера
    }

    // Тест перевіряє, чи існує заголовок на сторінці
    @Test
    public void testHeaderExists(){
        WebElement header = chromeDriver.findElement(By.id("header")); // Знаходимо заголовок за ID
        Assert.assertNotNull(header); // Перевіряємо, що заголовок знайдений
    }

    // Тест перевіряє, чи можна натискати на кнопку "Для студентів"
    @Test
    public void testClickOnForStudent(){
        WebElement forStudentButton = chromeDriver.findElement(By.xpath(" /html/body/center/div[4]/div/div[1]/ul/li[4]/a")); // Знаходимо кнопку

        Assert.assertNotNull(forStudentButton); // Перевіряємо, що кнопка знайдена
        forStudentButton.click(); // Натискаємо на кнопку
        Assert.assertNotEquals(chromeDriver.getCurrentUrl(), baseUrl); // Перевіряємо, що URL змінився
    }

    // Тест перевіряє наявність поля пошуку на сторінці "Для студентів"
    @Test
    public void testSearchFieldOnForStudentPage(){
        String studentPageUrl = "content/student_life/students/"; // URL сторінки для студентів
        chromeDriver.get(baseUrl+studentPageUrl); // Перехід на сторінку

        WebElement searchField = chromeDriver.findElement(By.tagName("input")); // Знаходимо поле пошуку
        Assert.assertNotNull(searchField); // Перевіряємо, що поле пошуку існує

        // Виводимо атрибути поля пошуку в консоль
        System.out.println(String.format("Name attribute: %s", searchField.getDomAttribute("name"))+
                String.format("\nID attribute: %s",  searchField.getDomAttribute("id"))+
                String.format("\nType attribute: %s",  searchField.getDomAttribute("type"))+
                String.format("\nValue attribute: %s",  searchField.getDomAttribute("value"))+
                String.format("\nPosition: (%d;%d)",  searchField.getLocation().x, searchField.getLocation().y)+
                String.format("\nSize: %dx%d",  searchField.getSize().height, searchField.getSize().width)
        );

        String inputValue = "I need info"; // Вводимо текст у поле пошуку
        searchField.sendKeys(inputValue); // Вводимо текст
        Assert.assertEquals(searchField.getText(), inputValue); // Перевіряємо, що текст введено в поле
        searchField.sendKeys(Keys.ENTER); // Натискаємо Enter

        Assert.assertNotEquals(chromeDriver.getCurrentUrl(), studentPageUrl); // Перевіряємо, що URL змінився
    }

    // Тест перевіряє роботу слайдера на сторінці
    @Test
    public void testSlider(){
        WebElement nextButton = chromeDriver.findElement(By.className("next")); // Кнопка "Наступний"
        WebElement nextButtonByCss = chromeDriver.findElement(By.cssSelector("a.next")); // Кнопка "Наступний" через CSS
        Assert.assertEquals(nextButton, nextButtonByCss); // Перевіряємо, чи елементи рівні

        WebElement previousButton = chromeDriver.findElement(By.className("prev")); // Кнопка "Попередній"

        // Проводимо тестування слайдера
        for(int i = 0; i < 20; i++){
            if(nextButton.getDomAttribute("class").contains("disabled")) { // Якщо кнопка "Наступний" заблокована
                previousButton.click(); // Натискаємо "Попередній"
                Assert.assertFalse(previousButton.getDomAttribute("class").contains("disabled")); // Перевіряємо, що кнопка "Попередній" не заблокована
                Assert.assertFalse(nextButton.getDomAttribute("class").contains("disabled")); // Перевіряємо, що кнопка "Наступний" не заблокована
            }

            else if(previousButton.getDomAttribute("class").contains("disabled")) { // Якщо кнопка "Попередній" заблокована
                nextButton.click(); // Натискаємо "Наступний"
                Assert.assertFalse(previousButton.getDomAttribute("class").contains("disabled")); // Перевіряємо, що кнопка "Попередній" не заблокована
                Assert.assertFalse(nextButton.getDomAttribute("class").contains("disabled")); // Перевіряємо, що кнопка "Наступний" не заблокована
            }

            else  {
                Assert.assertFalse(previousButton.getDomAttribute("class").contains("disabled")); // Перевіряємо, що кнопка "Попередній" не заблокована
                Assert.assertFalse(nextButton.getDomAttribute("class").contains("disabled")); // Перевіряємо, що кнопка "Наступний" не заблокована
                nextButton.click(); // Натискаємо "Наступний"
            }
        }
    }
}
