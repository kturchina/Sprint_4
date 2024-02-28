package qa.scooter.ya;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import qa.scooter.ya.pages.MainPage;
import qa.scooter.ya.pages.OrderInfoPage;
import qa.scooter.ya.pages.OrderPage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class OrderTest extends BaseSeleniumTest {

    public OrderTest() {
        MainPage.closeConsentPopup(wd);
    }

    @Test
    public void testOrderFormOpens() {
        MainPage.closeConsentPopup(wd);
        OrderPage.orderButton(wd).click();
        assertTrue("Navigated to order page", wd.getCurrentUrl().endsWith("/order"));
        assertTrue("Order form is visible", OrderPage.orderForm(wd).isDisplayed());

        wd.navigate().back();
        assertFalse("navigated back hides form", wd.getCurrentUrl().endsWith("/order"));

        OrderPage.orderBigButton(wd).click();
        assertTrue("Navigated to order page", wd.getCurrentUrl().endsWith("/order"));
        assertTrue("Order form is visible", OrderPage.orderForm(wd).isDisplayed());
    }

    @Test
    public void testValidationTriggersOnNext() {
        OrderPage.orderButton(wd).click();
        OrderPage.orderNextButton(wd).click();

        List<WebElement> errors = OrderPage.inputErrors(wd);
        List<WebElement> inputs = OrderPage.inputs(wd);
        assertEquals("Form errors must be shown for each field", inputs.size(), errors.stream()
                .filter(WebElement::isDisplayed).count());
    }

    @Test
    public void testOrder1() {
        testOrderHappyCase(new OrderPage.FormPart1(
                "Рандом",
                "Иванов",
                "Мшары",
                "Сокольники",
                "+7915123123"
        ), new OrderPage.FormPart2(
                LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("DD.MM.YYYY")),
                //ahtung! contains latin character
                "трое суток",
                List.of(),
                "Сокольники"
        ));
    }

    @Test
    public void testOrder2() {
        testOrderHappyCase(new OrderPage.FormPart1(
                "Семен",
                "Семенов",
                "Энск, дом 5",
                "Лубянка",
                "79151231231"
        ), new OrderPage.FormPart2(
                LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("DD.MM.YYYY")),
                "сутки",
                List.of("grey"),
                "Сокольники"
        ));
    }

    private void testOrderHappyCase(OrderPage.FormPart1 part1, OrderPage.FormPart2 part2) {
        OrderPage.orderButton(wd).click();

        OrderPage.fillFormPart1(wd, part1);

        OrderPage.orderNextButton(wd).click();
        assertTrue("Form part2", OrderPage.formPart2Header(wd).isDisplayed());

        OrderPage.orderBack(wd).click();
        assertTrue("Name must be remembered", wd.findElement(By.xpath("//input[@value='"+part1.firstName+"']")).isDisplayed());
        assertTrue("Last Name must be remembered", wd.findElement(By.xpath("//input[@value='"+part1.lastName+"']")).isDisplayed());
        assertTrue("Address must be remembered", wd.findElement(By.xpath("//input[@value='"+part1.address+"']")).isDisplayed());
        assertTrue("Station must be remembered", wd.findElement(By.xpath("//input[@value='"+part1.metroStation+"']")).isDisplayed());
        assertTrue("Phone must be remembered", wd.findElement(By.xpath("//input[@value='"+part1.phone+"']")).isDisplayed());

        OrderPage.orderNextButton(wd).click();
        assertTrue("Form part2", OrderPage.formPart2Header(wd).isDisplayed());

        OrderPage.fillFormPart2(wd, part2);
        OrderPage.submit(wd).click();

        assertTrue("Modal window must be shown", OrderPage.modal(wd, "Хотите оформить заказ?").isDisplayed());

        OrderPage.modal(wd, "Хотите оформить заказ?").findElement(By.xpath("//button[text() = 'Да']")).click();

        assertTrue("Order confirmed must be shown", OrderPage.modal(wd, "Заказ оформлен").isDisplayed());

        String orderNumber = OrderPage.getOrderNumber(wd);

        assertFalse("Order number must present", orderNumber.isEmpty());

        OrderPage.modal(wd, "Заказ оформлен").findElement(By.xpath("//button[text() = 'Посмотреть статус']")).click();
        assertTrue("Order info must be shown", OrderInfoPage.orderInfo(wd).isDisplayed());
        assertEquals("Order number is set in input field", OrderInfoPage.input(wd).getAttribute("value"), orderNumber);

        assertEquals("Order info must match", part1.firstName, OrderInfoPage.info(wd, "Имя").getText());
        assertEquals("Order info must match", part1.phone, OrderInfoPage.info(wd, "Телефон").getText());
        assertEquals("Order info must match", part2.comment, OrderInfoPage.info(wd, "Комментарий").getText());
        if (part2.colours.isEmpty())
            assertEquals("Order info must match", "любой", OrderInfoPage.info(wd, "Цвет").getText());
        else {
            assertNotEquals("Order info must match", "любой", OrderInfoPage.info(wd, "Цвет").getText());
        }
    }
}
