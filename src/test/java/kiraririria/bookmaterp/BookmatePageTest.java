package kiraririria.bookmaterp;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import kiraririria.discordipc.IPCClient;
import kiraririria.discordipc.entities.RichPresence;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Selenide.*;

public class BookmatePageTest {
    BookmatePage mainPage = new BookmatePage();
    private String cach = "";
    @BeforeAll
    public static void setUpAll() {
        Configuration.browserSize = "1280x800";
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @BeforeEach
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--headless");
        options.addArguments("--user-data-dir=C:\\Users\\Ivan\\AquaProjects\\BookmateRP\\build\\cookies");
        Configuration.browserCapabilities = options;
        open("https://bookmate.ru/@b3667744535/books/all");
    }
    @Test
    public void handle()
    {
        IPCClient client = DiscordIPC.StartIPC();
        RichPresence.Builder builder = DiscordIPC.Builder;
        OffsetDateTime latestActivity = OffsetDateTime.now();
        while (true)
        {
            SelenideElement latest = mainPage.booksList.$$(new By.ByClassName("book")).get(0);
            int procent = Integer.parseInt(latest.$(new By.ByTagName("span")).text().replace("%",""));
            SelenideElement img = latest.$(new By.ByTagName("img"));
            List<String> info = Arrays.asList(img.getAttribute("alt").split(","));
            String src = img.getAttribute("src").split("\\?")[0];
            Selenide.refresh();
            if (!(cach.equals(procent+src)))
            {
                cach=procent+src;
                builder.setLargeImage(src);
                builder.setDetails("Читает: "+info.get(0));
                builder.setState(info.stream().skip(1).collect(Collectors.joining("")));
                builder.setParty("idparty", procent,100);
                client.sendRichPresence(builder.build());
                latestActivity = OffsetDateTime.now();
            }
            else
            {
                if (Duration.between(latestActivity, OffsetDateTime.now()).toMinutes()>1)
                {
                    client.sendRichPresence(DiscordIPC.Original);
                }
            }
            sleep(5000);
        }
    }

}
