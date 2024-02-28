package qa.scooter.ya;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public abstract class BaseSeleniumTest {
    public static final String SCOOTER_HOME = "https://qa-scooter.praktikum-services.ru/";
    protected static WebDriver wd;

    @BeforeClass
    public static void setup() {
        wd = new FirefoxDriver();
//        wd = new ChromeDriver();
    }

    @Before
    public void start() {
        wd.get(SCOOTER_HOME);
    }


    @AfterClass
    public static void teardown() {
        wd.close();
    }
}
