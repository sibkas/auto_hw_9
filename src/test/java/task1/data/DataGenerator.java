package task1.data;

import com.github.javafaker.Faker;
import java.util.Locale;


public class DataGenerator {
    private static final Faker faker = new Faker(new Locale("ru"));

    private DataGenerator() {

    }

    public static String generateCity() {

        return "Красноярск"; // город фиксирован
    }

    public static String generateName() {
        return faker.name().firstName() + " " + faker.name().lastName();
    }

    public static String generatePhone() {
        return "+7" + faker.number().digits(10);
    }

    // Метод для генерации даты
    public static String generateDate(int daysFromNow, String pattern) {
        return java.time.LocalDate.now().plusDays(daysFromNow).format(java.time.format.DateTimeFormatter.ofPattern(pattern));
    }



}
