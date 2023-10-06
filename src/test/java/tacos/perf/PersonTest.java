package tacos.perf;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 批量插入数据（Person对象版）
 */
@SpringBootTest
public class PersonTest {

    public static final int BATCH_SIZE = 100;
    public static final int TOTAL_COUNT = 10000;
    public static final int ARRAY_LEN = 5;
    public static final String[] PHONE_TYPE = new String[]{"home", "work", "cell"};

    @Autowired
    private PersonRepository personRepository;

    @BeforeEach
    public void cleanup() {
        personRepository.deleteAll();
    }

    @Test
    public void insertDataPOJO() {
        Faker faker = new Faker();
        List<Person> data = new ArrayList<>();
        for (int i = 0; i < TOTAL_COUNT; i++) {
            // 每条数据中含有ARRAY_LEN数量的颜色数组
            List<String> colors = new ArrayList<>();
            for (int j = 0; j < ARRAY_LEN; j++) {
                colors.add(faker.color().name());
            }

            // 生成POJO
            Date bDay = faker.date().birthday();
            int age = (int) ((new Date().getTime() - bDay.getTime()) / 3600 / 24 / 365 / 1000);

            List<Phone> phones = new ArrayList<>();
            for (int j = 0; j < PHONE_TYPE.length; j++) {
                Phone phone = new Phone(PHONE_TYPE[j], faker.phoneNumber().phoneNumber());
                phones.add(phone);
            }

            Person person = new Person(
                    faker.name().fullName(),
                    faker.address().fullAddress(),
                    new java.sql.Date(bDay.getTime()),
                    colors,
                    age,
                    new BigDecimal(faker.number().numberBetween(10, 100)),
                    phones);
            data.add(person);
            // 使用批量方式插入以提高效率
            if (i % BATCH_SIZE == 0) {
                personRepository.saveAll(data);
                data.clear();
            }
        }
        if (data.size() > 0) {
            personRepository.saveAll(data);
        }
        System.out.println(String.format("====%d documents generated!", TOTAL_COUNT));
    }
}