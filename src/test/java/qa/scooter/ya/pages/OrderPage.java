package qa.scooter.ya.pages;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class OrderPage {

    public static class FormPart1 {
        public final String firstName;
        public final String lastName;
        public final String address;
        public final String metroStation;
        public final String phone;

        public FormPart1(String firstName, String lastName, String address, String metroStation, String phone) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.address = address;
            this.metroStation = metroStation;
            this.phone = phone;
        }
    }

    public static class FormPart2 {
        public final String date;
        public final String duration;
        public final List<String> colours;
        public final String comment;

        public FormPart2(String date, String duration, List<String> colours, String comment) {
            this.date = date;
            this.duration = duration;
            this.colours = colours;
            this.comment = comment;
        }
    }

    private final static By orderButton = By.xpath("//div[starts-with(@class, 'Header_Header')]//button[text() = 'Заказать']");
    private final static By orderBigButton = By.xpath("//div[starts-with(@class, 'Home_FinishButton')]//button[text() = 'Заказать']");
    private final static By orderForm = By.xpath("//div[starts-with(@class, 'Order_Content')]");
    private final static By orderNextButton = By.xpath("//button[text() = 'Далее']");

    private final static By inputError = By.xpath("//div[starts-with(@class, 'Input_ErrorMessage')]");
    private final static By input = By.xpath("//input[@type='text']");
    private final static By submit = By.xpath("//div[starts-with(@class, 'Order_Buttons')]//button[text() = 'Заказать']");
    private final static By back = By.xpath("//div[starts-with(@class, 'Order_Buttons')]//button[text() = 'Назад']");

    public static WebElement orderButton(WebDriver wd) {
        return wd.findElement(orderButton);
    }
    public static WebElement orderBigButton(WebDriver wd) {
        return wd.findElement(orderBigButton);
    }

    public static WebElement orderNextButton(WebDriver wd) {
        return wd.findElement(orderNextButton);
    }

    public static WebElement orderForm(WebDriver wd) {
        return wd.findElement(orderForm);
    }

    public static List<WebElement> inputErrors(WebDriver wd) {
        return wd.findElements(inputError);
    }
    public static List<WebElement> inputs(WebDriver wd) {
        return wd.findElements(input);
    }

    public static WebElement orderBack(WebDriver wd) {
        return wd.findElement(back);
    }

    public static void fillFormPart1(WebDriver wd, FormPart1 data) {
        wd.findElement(By.xpath("//input[@placeholder='* Имя']")).sendKeys(data.firstName);
        wd.findElement(By.xpath("//input[@placeholder='* Фамилия']")).sendKeys(data.lastName);
        wd.findElement(By.xpath("//input[@placeholder='* Адрес: куда привезти заказ']")).sendKeys(data.address);
        wd.findElement(By.xpath("//input[@placeholder='* Станция метро']")).click();
        new Actions(wd)
                .moveToElement(new WebDriverWait(wd, Duration.ofSeconds(3))
                        .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(), '"+data.metroStation+"')]"))))
                .click()
                .build().perform();
        wd.findElement(By.xpath("//input[@placeholder='* Телефон: на него позвонит курьер']")).sendKeys(data.phone);
    }

    public static void fillFormPart2(WebDriver wd, FormPart2 data) {
        wd.findElement(By.xpath("//input[@placeholder='* Когда привезти самокат']")).sendKeys(data.date);
        wd.findElement(By.xpath("//body")).click();
        wd.findElement(By.xpath("//div[contains(text(), '* Срок аренды')]")).click();
        new Actions(wd)
                .click(new WebDriverWait(wd, Duration.ofSeconds(3))
                        .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='Dropdown-option' and text() = '"+data.duration+"']")))).build().perform();;
        data.colours.forEach(colour -> wd.findElement(By.xpath("//label[@for='"+colour+"']")).click());
        wd.findElement(By.xpath("//input[@placeholder='Комментарий для курьера']")).sendKeys(data.comment);
    }

    public static WebElement formPart2Header(WebDriver wd) {
        return new WebDriverWait(wd, Duration.ofSeconds(3))
                .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[text() = 'Про аренду']")));
    }

    public static WebElement submit(WebDriver wd) {
        return wd.findElement(submit);
    }

    public static WebElement modal(WebDriver wd, String header) {
        return new WebDriverWait(wd, Duration.ofSeconds(3))
                .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[starts-with(@class, 'Order_ModalHeader') and contains(text(), '"+header+"')]")));
    }

    public static String getOrderNumber(WebDriver wd) {
        return new WebDriverWait(wd, Duration.ofSeconds(3))
                .until(driver -> wd.findElements(By.xpath("//div[starts-with(@class, 'Order_Text')]")).stream()
                        .map(el -> {
                            try {
                                Matcher m = Pattern.compile("[0-9]+").matcher(el.getText());
                                return m.find() ? m.group() : null;
                            }
                            catch (IllegalStateException ignore) {}
                            return null;
                        })
                        .filter(Objects::nonNull)
                        .findFirst()
                        .orElse(null));
    }
}
