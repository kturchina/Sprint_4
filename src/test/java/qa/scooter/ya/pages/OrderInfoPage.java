package qa.scooter.ya.pages;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class OrderInfoPage {
    public static WebElement orderInfo(WebDriver wd) {
        return new WebDriverWait(wd, Duration.ofSeconds(3))
                .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[starts-with(@class, 'Track_OrderInfo')]")));
    }

    public static WebElement input(WebDriver wd) {
        return new WebDriverWait(wd, Duration.ofSeconds(3))
                .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[starts-with(@class, 'Track_Form')]//input")));
    }

    public static WebElement info(WebDriver wd, String title) {
        return wd.findElement(By.xpath("//div[starts-with(@class, 'Track_Title') and text()='"+title+"']/following-sibling::*"));
    }
}
