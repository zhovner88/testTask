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
    
    public static Order createOrderWithPetId(int petId) {
        return createValidOrder().setPetId(petId);
    }
    
    public static Order createOrderWithStatus(OrderStatus status) {
        return createValidOrder().setStatus(status);
    }
    
    public static Order createOrderWithId(int id) {
        return createValidOrder().setId(id);
    }
    
    public static Order createOrderWithQuantity(int quantity) {
        return createValidOrder().setQuantity(quantity);
    }
    
    }

    public static Order createOrderWithNullId() {
        return createValidOrder().setId(null);
    }

    public static Order createOrderWithNegativePetId() {
        return createValidOrder().setPetId(-1);
    }

    public static Order createOrderWithNegativeQuantity() {
        return createValidOrder().setQuantity(-5);
    }

    public static Order createOrderWithInvalidShipDate() {
        return createValidOrder().setShipDate("invalid-date");
    }

    public static Order createOrderWithNullStatus() {
        return createValidOrder().setStatus(null);
    }

    public static Order createOrderWithNullComplete() {
        return createValidOrder().setComplete(null);
    }
    
    private static OrderStatus getRandomStatus() {
        OrderStatus[] statuses = OrderStatus.values();
        return statuses[ThreadLocalRandom.current().nextInt(statuses.length)];
    }
}