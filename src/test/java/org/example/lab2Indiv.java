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

public class lab2Indiv {
    private WebDriver chromeDriver; // Об'єкт для керування браузером
    private static final String baseUrl = "https://rozetka.com.ua/"; // Основна URL сторінка

    // Виконується перед усіма тестами, налаштовує браузер
    @BeforeClass(alwaysRun = true)
    public void SetUp(){
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


    // 1 Тест перевіряє, чи існує кнопка з логотипом на головній сторінці
    @Test
    public void testClickOnButton(){
        WebElement cartButton = chromeDriver.findElement(By.xpath("/html/body/rz-app-root/div/div/rz-main-header/header/div/div/a/picture/img")); // Знаходимо кнопку "Корзина"
        Assert.assertNotNull(cartButton); // Перевіряємо, що кнопка знайдена
        cartButton.click(); // Натискаємо на кнопку
        Assert.assertNotEquals(chromeDriver.getCurrentUrl(), baseUrl); // Перевіряємо, що URL змінився
    }

    // 2 Тест перевіряє введення тексту у поле пошуку і перевіряє наявність введених даних
    @Test
    public void testSearchField(){
        WebElement searchField = chromeDriver.findElement(By.xpath("//input[@name='search']")); // Знаходимо поле пошуку
        Assert.assertNotNull(searchField); // Перевіряємо, що поле пошуку знайдено

        String inputValue = "laptop"; // Введення значення
        searchField.sendKeys(inputValue); // Вводимо текст
        Assert.assertEquals(searchField.getAttribute("value"), inputValue); // Перевіряємо, що текст введено в поле
        searchField.sendKeys(Keys.ENTER); // Натискаємо Enter

        // Перевіряємо, що URL змінився після пошуку
        Assert.assertNotEquals(chromeDriver.getCurrentUrl(), baseUrl);
    }

    // 3 Тест для знаходження елементу за допомогою XPath
    @Test
    public void testFindProductByText() {
        // Використовуємо функцію для пошуку елементу за частиною тексту
        WebElement product = chromeDriver.findElement(By.xpath("/html/body/rz-app-root/div/div/rz-main-header/header/div/div/rz-fat-menu-header-btn/button")); // Знаходимо елемент з частиною тексту
        Assert.assertNotNull(product); // Перевіряємо, що елемент знайдений
        product.click(); // Кликаємо по елементу
        Assert.assertNotEquals(chromeDriver.getCurrentUrl(), baseUrl); // Перевіряємо, що URL змінився
    }


    // 4 Тест перевіряє, чи змінюється URL після кліку на певний елемент
    @Test
    public void testUrlChangeAfterClick() {
        String initialUrl = chromeDriver.getCurrentUrl(); // Отримуємо початковий URL

        // Шукаємо елемент, по якому потрібно натискати
        WebElement someElement = chromeDriver.findElement(By.xpath("/html/body/rz-app-root/div/div/rz-main-page/div/aside/rz-main-page-sidebar/rz-fat-menu/div[2]/ul/li[4]/a"));

        // Перевіряємо, чи елемент доступний для кліку
        if (someElement.isEnabled() && someElement.isDisplayed()) {
            someElement.click(); // Клікаємо на елемент
            // Перевіряємо, чи змінився URL після кліку
            String newUrl = chromeDriver.getCurrentUrl();
            if (!newUrl.equals(initialUrl)) {
                System.out.println("URL змінився після кліку: " + newUrl);
                Assert.assertNotEquals(newUrl, initialUrl, "URL не змінився після кліку.");
            } else {
                Assert.fail("URL не змінився після кліку.");
            }
        } else {
            Assert.fail("Елемент для кліку недоступний.");
        }
    }

    // 5 Тест перевіряє, чи змінився URL після кліку на рекламний банер
    @Test
    public void testUrlAfterClickingProduct() {
        WebElement productLink = chromeDriver.findElement(By.xpath("//html/body/rz-app-root/div/div/rz-main-page/div/main/rz-main-page-content/rz-top-slider/rz-slider/div[1]/div[2]/a/img"));
        String currentUrl = chromeDriver.getCurrentUrl(); // Зберігаємо поточний URL
        productLink.click(); // Кликаємо на товар
        Assert.assertNotEquals(chromeDriver.getCurrentUrl(), currentUrl); // Перевіряємо, що URL змінився
    }

    // 6 Тест перевіряє, чи існує пошукове поле
    @Test
    public void testSearchFieldExists(){
        WebElement searchField = chromeDriver.findElement(By.name("search")); // Знаходимо поле пошуку за ім'ям
        Assert.assertNotNull(searchField); // Перевіряємо, що поле пошуку знайдено
    }

    // 7 Тест перевіряє, чи працює посилання на сторінку "Про нас"
    @Test
    public void testAboutUsLink() {
        WebElement aboutUsLink = chromeDriver.findElement(By.linkText("Про нас"));
        aboutUsLink.click();
        Assert.assertTrue(chromeDriver.getCurrentUrl().contains("about"), "Не вдалося перейти на сторінку 'Про нас'.");
    }

    // 8 Тест перевіряє, чи відображається довідкова інформація
    @Test
    public void testContactInfoPresence() {
        WebElement contactInfo = chromeDriver.findElement(By.xpath("/html/body/rz-app-root/div/div/rz-main-page/div/aside/rz-main-page-sidebar/rz-menu-help-center/a"));
        Assert.assertTrue(contactInfo.isDisplayed(), "Контактна інформація не відображається.");
    }

    // 9 Тест перевіряє, чи достатньо швидко завантажується сторінка
    @Test
    public void testPageLoadSpeed() {
        long startTime = System.currentTimeMillis();
        chromeDriver.get("https://rozetka.com.ua/");
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        Assert.assertTrue(duration < 5000, "Сторінка завантажується занадто довго.");
    }

}
