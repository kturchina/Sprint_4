package qa.scooter.ya.pages;

import java.time.Duration;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class MainPage {
    public final static By consentPopup = By.xpath("//div[starts-with(@class, 'App_CookieConsent')]");
    private final static By consentPopupButton = By.xpath("//button[starts-with(@class, 'App_CookieButton')]");

    private final static By faqAccordion = By.xpath("//div[@class='accordion']");

    public static WebElement consentPopup(WebDriver wd) {
        return wd.findElement(consentPopup);
    }

    public static void closeConsentPopup(WebDriver wd) {
        wd.findElements(consentPopupButton).forEach(WebElement::click);
    }

    public static WebElement faqQuestion(WebDriver wd, String question) {
        return wd.findElement(By.xpath("//div[contains(text(), '"+question+"')]"));
    }

    public static void faqQuestionClick(WebDriver wd, WebElement heading) {
        ((JavascriptExecutor)wd).executeScript("arguments[0].scrollIntoView();", wd.findElement(By.xpath("//div[starts-with(@class, 'Home_FAQ')]")));
        new Actions(wd)
                .click(new WebDriverWait(wd, Duration.ofSeconds(3))
                        .until(ExpectedConditions.elementToBeClickable(heading))).build().perform();
    }

    public static WebElement faqAnswer(WebDriver wd, String answer) {
        WebElement answerElement = wd.findElement(By.xpath("//div[@class='accordion__panel' and contains(normalize-space(), '"+answer+"')]"));
        new Actions(wd)
                .moveToElement(new WebDriverWait(wd, Duration.ofSeconds(3))
                        .until(ExpectedConditions.visibilityOf(answerElement)));
        return answerElement;
    }

    public static WebElement faqAccordion(WebDriver wd) {
        return wd.findElement(faqAccordion);
    }

    public static List<WebElement> faqAnswersVisible(WebDriver wd) {
        return wd.findElements(By.xpath("//div[@data-accordion-component='AccordionItemPanel' and not(@hidden)]"));
    }

    public static WebElement logoScooter(WebDriver wd) {
        return wd.findElement(By.xpath("//a[starts-with(@class, 'Header_LogoScooter')]"));
    }

    public static WebElement logoYandex(WebDriver wd) {
        return wd.findElement(By.xpath("//a[starts-with(@class, 'Header_LogoYandex')]"));
    }
}
