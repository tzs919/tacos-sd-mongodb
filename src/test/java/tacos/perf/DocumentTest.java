package tacos.perf;

import com.github.javafaker.Faker;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 批量插入数据（Document版本）
 */
public class DocumentTest {

    public static final int BATCH_SIZE = 100;
    public static final int TOTAL_COUNT = 10000;
    public static final int ARRAY_LEN = 5;
    public static final String[] PHONE_TYPE = new String[]{"home", "work", "cell"};

    private static MongoDatabase mongoDatabase;

    @BeforeAll
    static public void beforeClass() {
        MongoClient mongoClient = MongoClients.create();
        mongoDatabase = mongoClient.getDatabase("demo");
    }

    @Test
    public void insertDataDocument() {
        MongoCollection<Document> coll = mongoDatabase.getCollection("Person", Document.class);
        Faker faker = new Faker();
        List<Document> data = new ArrayList<>();
        for (int i = 0; i < TOTAL_COUNT; i++) {
            // 每条数据中含有ARRAY_LEN数量的颜色数组
            List<String> colors = new ArrayList<>();
            for (int j = 0; j < ARRAY_LEN; j++) {
                colors.add(faker.color().name());
            }

            Date bDay = faker.date().birthday();
            int age = (int) ((new Date().getTime() - bDay.getTime()) / 3600 / 24 / 365 / 1000);

            List<Document> phones = new ArrayList<>();
            for (int j = 0; j < PHONE_TYPE.length; j++) {
                Document phone = new Document("type", PHONE_TYPE[j]);
                phone.append("number", faker.phoneNumber().phoneNumber());
                phones.add(phone);
            }
            Document person = new Document();
            person.put("name", faker.name().fullName());
            person.put("address", faker.address().fullAddress());
            person.put("birthday", bDay);
            person.put("favouriteColor", colors);
            person.put("age", age);
            person.put("amount", new BigDecimal(faker.number().numberBetween(10, 100)));
            person.put("phones", phones);
            data.add(person);
            // 使用批量方式插入以提高效率
            if (i % BATCH_SIZE == 0) {
                coll.insertMany(data);
                data.clear();
            }
        }
        if (data.size() > 0) {
            coll.insertMany(data);
        }
        System.out.println(String.format("====%d documents generated!", TOTAL_COUNT));
    }
}
