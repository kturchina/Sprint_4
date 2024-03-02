package qa.scooter.ya;

import java.util.Arrays;
import java.util.Collection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.WebElement;
import qa.scooter.ya.pages.MainPage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class FaqAccordionTest extends BaseSeleniumTest {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
            { "Сколько это стоит? И как оплатить?",
                    "Сутки — 400 рублей. Оплата курьеру — наличными или картой."},
            { "Хочу сразу несколько самокатов! Так можно?",
                    "Пока что у нас так: один заказ — один самокат. Если хотите покататься с друзьями, можете просто сделать несколько заказов — один за другим."},
            { "Как рассчитывается время аренды?",
                    "Допустим, вы оформляете заказ на 8 мая. Мы привозим самокат 8 мая в течение дня. Отсчёт времени аренды начинается с момента, когда вы оплатите заказ курьеру. Если мы привезли самокат 8 мая в 20:30, суточная аренда закончится 9 мая в 20:30."},
            { "Можно ли заказать самокат прямо на сегодня?",
                    "Только начиная с завтрашнего дня. Но скоро станем расторопнее."},
            { "Можно ли продлить заказ или вернуть самокат раньше?",
                    "Пока что нет! Но если что-то срочное — всегда можно позвонить в поддержку по красивому номеру 1010."},
            { "Вы привозите зарядку вместе с самокатом?",
                    "Самокат приезжает к вам с полной зарядкой. Этого хватает на восемь суток — даже если будете кататься без передышек и во сне. Зарядка не понадобится."},
            { "Можно ли отменить заказ?",
                    "Да, пока самокат не привезли. Штрафа не будет, объяснительной записки тоже не попросим. Все же свои."} ,
            //todo: create ticket to fix typo
            { "Я жизу за МКАДом, привезёте?",
                    "Да, обязательно. Всем самокатов! И Москве, и Московской области."}
        });
    }

    private final String question;
    private final String answer;

    public FaqAccordionTest(String question, String answer) {
        this.question = question;
        this.answer = answer;
        MainPage.closeConsentPopup(wd);
    }


    @Test
    public void testFaqPresents() {
        WebElement faq = MainPage.faqAccordion(wd);
        assertTrue("FAQ must be visible", faq.isDisplayed());
    }

    @Test
    public void testFaqContents() {
        MainPage.closeConsentPopup(wd);
        WebElement heading = MainPage.faqQuestion(wd, question);
        assertTrue("Question: "+question+"; must be visible", heading.isDisplayed());

        MainPage.faqQuestionClick(wd, heading);

        WebElement answerElement = MainPage.faqAnswer(wd, answer);
        assertTrue("Answer: "+answer+"; must visible after click on question: "+question, answerElement.isDisplayed());

        assertEquals("Other answers must be hidden", 1, MainPage.faqAnswersVisible(wd).size());
    }
}
