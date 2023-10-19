package kiraririria.bookmaterp;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.UnreachableBrowserException;

import static com.codeborne.selenide.Selenide.*;

public class YandexAuthorizationPage {

    @BeforeAll
    public static void setUpAll() {
        Configuration.browserSize = "1280x800";
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @BeforeEach
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--user-data-dir=C:\\Users\\Ivan\\AquaProjects\\BookmateRP\\build\\cookies");
        Configuration.browserCapabilities = options;
        open("https://bookmate.ru/@b3667744535/books/all");
    }
    public boolean isBrowserClosed(WebDriver driver)
    {
        boolean isClosed = false;
        try {
            driver.getTitle();
        } catch(UnreachableBrowserException ubex) {
            isClosed = true;
        }
        return isClosed;
    }
    @Test
    public void authorization()
    {
        //Selenide.executeJavaScript("alert('Пожалуйста, войдите в аккаунт Яндекс+. После чего закройте браузер');");
        while (true)
        {
            try
            {
                webdriver().driver().getWebDriver().getTitle();
                sleep(10000);
            }
            catch(UnreachableBrowserException ubex)
            {
                break;
            }
        }
    }
}
