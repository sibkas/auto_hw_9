package task1;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import task1.data.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.*;
import static java.nio.channels.FileChannel.open;

public class CardOrderTest {

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll () {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    void setup() {

        Selenide.open("http://localhost:9999");
    }

    @Test
    void shouldReplanMeeting() {
        // Генерируем данные один раз и сохраняем в переменные
        String city = DataGenerator.generateCity();
        String name = DataGenerator.generateName();
        String phone = DataGenerator.generatePhone();
        String firstDate = DataGenerator.generateDate(3, "dd.MM.yyyy");
        String secondDate = DataGenerator.generateDate(5, "dd.MM.yyyy"); // новая дата для перепланирования

        // 1. Первый заказ с первой датой
        $("[data-test-id='city'] input").setValue(city);
        var dateInput = $("[data-test-id='date'] input");
        dateInput.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE);
        dateInput.setValue(firstDate);
        $("[data-test-id='name'] input").setValue(name);
        $("[data-test-id='phone'] input").setValue(phone);
        $("[data-test-id='agreement'] .checkbox__box").click();
        $("button.button_view_extra").click();

        String expectedText = "Встреча успешно запланирована на " + firstDate;
        $("[data-test-id='success-notification']")
                //.should(Condition.exist)
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.text(expectedText));


        // 2. Повторный заказ с той же информацией, но с другой датой
        // очищаем и вводим заново
        $("[data-test-id='city'] input").sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE);
        $("[data-test-id='city'] input").setValue(city);
        dateInput.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE);
        dateInput.setValue(secondDate);
        $("[data-test-id='name'] input").sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE);
        $("[data-test-id='name'] input").setValue(name);
        $("[data-test-id='phone'] input").setValue(phone);
        SelenideElement checkboxBox = $("[data-test-id='agreement'] .checkbox__box");
        SelenideElement checkboxInput = $("[data-test-id='agreement'] input[type='checkbox']");

        // Кликаем, только если чекбокс не выбран
        if (!checkboxInput.isSelected()) {
            checkboxBox.click();
        }

        $("button.button_view_extra").click();


        // 3. Проверяем появление окна перепланирования
        $("[data-test-id='replan-notification']")
                .shouldBe(Condition.visible, Duration.ofSeconds(15));


        // 4. Кликаем кнопку перепланировать
        $("[data-test-id='replan-notification'] button.button_view_extra").click();


        // 5. Проверяем уведомление об успешном перепланировании
        String replannedText = "Встреча успешно запланирована на " + secondDate;
        $("[data-test-id='success-notification']")
                //.should(Condition.exist)
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.text(replannedText));
    }



}
