package qa.scooter.ya;

import org.junit.Test;
import org.openqa.selenium.WebElement;
import qa.scooter.ya.pages.MainPage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CookieConsentPopupTest extends BaseSeleniumTest {

    @Test
    public void testCookiePopupPresents() {
        WebElement popup = MainPage.consentPopup(wd);
        assertTrue("Cookie consent popup must be displayed", popup.isDisplayed());
    }

    @Test
    public void testCookiePopupStateChanges() {
        MainPage.closeConsentPopup(wd);
        assertEquals("Cookie consent popup gets removed from ui", 0, wd.findElements(MainPage.consentPopup).size());
        wd.navigate().refresh();
        assertEquals("Cookie consent popup state stored in cookie or any other persistent storage", 0, wd.findElements(MainPage.consentPopup).size());
    }
}
