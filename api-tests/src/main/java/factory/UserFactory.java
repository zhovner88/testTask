package factory;

import com.github.javafaker.Faker;
import com.petstore.api.model.User;

import java.util.concurrent.ThreadLocalRandom;

public class UserFactory {
    
    private static final Faker faker = new Faker();
    
    public static User createRandomUser() {
        return new User()
                .setId(ThreadLocalRandom.current().nextInt(1, 10000))
                .setFirstName(faker.name().firstName())
                .setLastName(faker.name().lastName())
                .setEmail(faker.internet().emailAddress())
                .setPassword(faker.internet().password(8, 20))
                .setPhone(faker.phoneNumber().cellPhone())
                .setUserStatus(ThreadLocalRandom.current().nextInt(0, 3));
    }

}