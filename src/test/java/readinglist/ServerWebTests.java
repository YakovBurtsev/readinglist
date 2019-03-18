package readinglist;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

@ActiveProfiles("default")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ServerWebTests {

    private static HtmlUnitDriver browser;

    @Value("${local.server.port}")
    private int port;

    @BeforeClass
    public static void setup() {
        browser = new HtmlUnitDriver();
        browser.manage().timeouts()
                .implicitlyWait(10, TimeUnit.SECONDS);
    }

    @AfterClass
    public static void closeBrowser() {
        browser.quit();
    }

    @Test
    public void addBookToEmptyList() {
        String baseUrl = "http://localhost:" + port;
        browser.get(baseUrl);

        assertEquals(baseUrl + "/login", browser.getCurrentUrl());
        browser.findElementByName("username").sendKeys("yakov");
        browser.findElementByName("password").sendKeys("yakov");
        browser.findElementByTagName("form").submit();

        assertEquals(baseUrl + "/", browser.getCurrentUrl());
        assertEquals("You have no books in your book list",
                browser.findElementByTagName("div").getText());

        browser.findElementByName("title").sendKeys("BOOK TITLE");
        browser.findElementByName("author").sendKeys("BOOK AUTHOR");
        browser.findElementByName("isbn").sendKeys("1234567890");
        browser.findElementByName("description").sendKeys("DESCRIPTION");
        browser.findElementById("add-book-form").submit();

        WebElement dl =
                browser.findElementByCssSelector("dt.bookHeadline");
        assertEquals("BOOK TITLE by BOOK AUTHOR (ISBN: 1234567890)",
                dl.getText());
        WebElement dt =
                browser.findElementByCssSelector("dd.bookDescription");
        assertEquals("DESCRIPTION", dt.getText());

        browser.findElementById("logout").submit();
        assertEquals(baseUrl + "/login?logout", browser.getCurrentUrl());
    }

}
