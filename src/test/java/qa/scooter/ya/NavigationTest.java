package qa.scooter.ya;

import org.junit.Test;
import qa.scooter.ya.pages.MainPage;
import qa.scooter.ya.pages.OrderPage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class NavigationTest extends BaseSeleniumTest {
    @Test
    public void testLogoNav() {
        OrderPage.orderButton(wd).click();
        assertTrue("Navigated to order page", wd.getCurrentUrl().endsWith("/order"));

        MainPage.logoScooter(wd).click();
        assertEquals("Navigated to main page", SCOOTER_HOME, wd.getCurrentUrl());
    }

    @Test
    public void testLogoYaNav() {
        String myHandle = wd.getWindowHandle();
        MainPage.logoYandex(wd).click();
        assertTrue("Some new window routed to yandex was opened", wd.getWindowHandles().size() > 1);
        wd.getWindowHandles().forEach(h -> {
            if (!myHandle.equalsIgnoreCase(h))
                wd.switchTo().window(h).close();
        });
        wd.switchTo().window(myHandle);
    }
}
