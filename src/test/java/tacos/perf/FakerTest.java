package tacos.perf;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;

import java.util.Locale;

public class FakerTest {

    @Test
    public void fakerTest() {
        Faker faker = new Faker(Locale.CHINA);

        System.out.println(faker.address().streetAddress());
//
//        System.out.println(faker.name().fullName());
//
//        System.out.println(faker.book().title());
//
//        System.out.println(faker.phoneNumber().cellPhone());
//
//        System.out.println(faker.app().name());
//
//        System.out.println(faker.color().name());
//
//        System.out.println(faker.date().birthday());
//
//        System.out.println(faker.idNumber().invalid());
    }
}
