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

    // Individual field validation methods - each tests one specific field
    
    public static Order createOrderWithIdNullValue() {
        return createValidOrder().setId(null);
    }
    
    public static Order createOrderWithNegativePetId() {
        return createValidOrder().setPetId(-1);
    }
    
    public static Order createOrderWithNegativeQuantity() {
        return createValidOrder().setQuantity(-5);
    }
    
    public static Order createOrderWithInvalidDate() {
        return createValidOrder().setShipDate("invalid-date-format");
    }
    
    public static Order createOrderWithNullStatus() {
        return createValidOrder().setStatus(null);
    }

    public static Order createOrderWithPetId(int petId) {
        return createValidOrder().setPetId(petId);
    }
    
    public static Order createOrderWithXssInDate() {
        return createValidOrder()
                .setShipDate("<script>alert('xss')</script>");
    }
    
    private static OrderStatus getRandomStatus() {
        OrderStatus[] statuses = OrderStatus.values();
        return statuses[ThreadLocalRandom.current().nextInt(statuses.length)];
    }
}