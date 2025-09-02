package factory;

import com.github.javafaker.Faker;
import com.petstore.api.model.Order;
import com.petstore.api.model.enums.OrderStatus;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

public class OrderFactory {

    private static final Faker faker = new Faker();

    public static Order createValidOrder() {
        return new Order()
                .setId(ThreadLocalRandom.current().nextInt(1, 1000))
                .setPetId(ThreadLocalRandom.current().nextInt(1, 100))
                .setQuantity(ThreadLocalRandom.current().nextInt(1, 10))
                .setShipDate(OffsetDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))
                .setStatus(getRandomStatus())
                .setComplete(faker.bool().bool());
    }

    // TO-DO. Add validations for each field, instead of  whole request. It's not clear what can go wrong
    // add each field test independently
    public static Order createOrderWithInvalidData() {
        return new Order()
                .setId(null)
                .setPetId(-1)
                .setQuantity(-5)
                .setShipDate("invalid-date")
                .setStatus(null)
                .setComplete(null);
    }
    
    public static Order createOrderWithPetId(int petId) {
        return createValidOrder().setPetId(petId);
    }
    
    private static OrderStatus getRandomStatus() {
        OrderStatus[] statuses = OrderStatus.values();
        return statuses[ThreadLocalRandom.current().nextInt(statuses.length)];
    }
}