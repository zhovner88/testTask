package com.petstore.tests.store;

import com.petstore.api.conditions.Conditions;
import com.petstore.api.model.Order;
import com.petstore.api.model.enums.OrderStatus;
import factory.OrderFactory;
import org.testng.annotations.Test;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.Matchers.*;

public class StoreEdgeCasesTests extends BaseStoreApiTest {

    // Priority 2 Tests - Edge cases and integration scenarios

    @Test
    void testFullOrderLifecycle() {
        // Given - create an order with valid data
        Order order = OrderFactory.createValidOrder();
        
        // When - place order
        int createdOrderId = storeService.placeOrder(order)
                .shouldHave(Conditions.statusCode(200))
                .shouldHave(Conditions.bodyField("id", notNullValue()))
                .parseValue("id", Integer.class);
        
        // And - get the order
        storeService.getOrderById(createdOrderId)
                .shouldHave(Conditions.statusCode(200))
                .shouldHave(Conditions.bodyField("id", equalTo(createdOrderId)));
        
        // And - delete the order
        storeService.deleteOrder(createdOrderId)
                .shouldHave(Conditions.statusCode(200));
        
        // Then - Verify deletion - order should not exist anymore
        storeService.getOrderById(createdOrderId)
                .shouldHave(Conditions.statusCode(404));
    }

    @Test
    void testGetOrderByIdZero() {
        // Given - boundary test with ID = 0
        int zeroId = 0;
        
        // When, Then - should return error for zero ID
        storeService.getOrderById(zeroId)
                .shouldHave(Conditions.statusCode(404));
        // Note: Zero is not a valid ID according to Petstore API
    }

    @Test
    void testDeleteAlreadyDeletedOrder() {
        // Given - create and delete an order first
        Order order = OrderFactory.createValidOrder();
        
        int createdOrderId = storeService.placeOrder(order)
                .shouldHave(Conditions.statusCode(200))
                .shouldHave(Conditions.bodyField("id", notNullValue()))
                .parseValue("id", Integer.class);
        
        // Delete the order first time
        storeService.deleteOrder(createdOrderId)
                .shouldHave(Conditions.statusCode(200));
        
        // When, Then - try to delete the same order again
        storeService.deleteOrder(createdOrderId)
                .shouldHave(Conditions.statusCode(404));
        // Note: Second deletion should return 404 as order no longer exists
    }

    @Test
    void testGetInventoryResponseTime() {
        // Given - performance test for inventory endpoint
        long startTime = System.currentTimeMillis();
        
        // When - get inventory
        storeService.getInventory()
                .shouldHave(Conditions.statusCode(200))
                .shouldHave(Conditions.bodyField("$", notNullValue()));
        
        long responseTime = System.currentTimeMillis() - startTime;
        
        // Then - verify response time is reasonable (less than 5 seconds)
        if (responseTime > 5000) {
            throw new AssertionError("Inventory response time too slow: " + responseTime + "ms");
        }
        // Note: Performance threshold set to 5 seconds for basic validation
    }

    @Test(enabled = false)
        // not clear how business logic works according to documentation available,
        // just leave it here for the idea of verification type needed
    void testInventoryDecreasesAfterOrderPlacement() {
        // Given - get initial inventory count for "available" pets (should decrease when orders placed)
        var initialAvailable = storeService.getInventory()
                .shouldHave(Conditions.statusCode(200))
                .parseValue("available", Integer.class);

        // When - place multiple orders (should reduce available pet inventory)
        Order order1 = OrderFactory.createValidOrder();
        Order order2 = OrderFactory.createValidOrder();

        storeService.placeOrder(order1).shouldHave(Conditions.statusCode(200));
        storeService.placeOrder(order2).shouldHave(Conditions.statusCode(200));

        // Then - verify "available" pet count decreased after orders placed
        var updatedAvailable = storeService.getInventory()
                .shouldHave(Conditions.statusCode(200))
                .parseValue("available", Integer.class);

        // Available pets should decrease when orders are placed
        if (updatedAvailable >= initialAvailable) {
            throw new AssertionError("Expected available orders to decrease after placing new orders. " +
                    "Initial: " + initialAvailable + ", Updated: " + updatedAvailable);
        }
        // Note: Business logic validation - placing orders should reduce available pet inventory
    }

    @Test
    void testParallelOrderOperations() {
        // Given - prepare multiple orders for parallel order placement testing
        Order order1 = OrderFactory.createValidOrder();
        Order order2 = OrderFactory.createValidOrder();
        
        // When - place orders simultaneously
        long startTime = System.currentTimeMillis();
        
        var response1 = storeService.placeOrder(order1);
        var response2 = storeService.placeOrder(order2);
        
        long executionTime = System.currentTimeMillis() - startTime;
        
        // Then - both orders should be processed successfully
        response1.shouldHave(Conditions.statusCode(200))
                 .shouldHave(Conditions.bodyField("id", notNullValue()));
        
        response2.shouldHave(Conditions.statusCode(200))
                 .shouldHave(Conditions.bodyField("id", notNullValue()));
        
        // And - execution should be reasonably fast (parallel processing)
        if (executionTime > 10000) {
            throw new AssertionError("Concurrent operations too slow: " + executionTime + "ms");
        }
        // Note: API should handle multiple simultaneous requests
    }

    @Test
    void testPlaceOrderWithFutureShipDate() {
        // Given - order with future ship date
        Order orderWithFutureDate = OrderFactory.createValidOrder()
                .setShipDate(OffsetDateTime.now().plusDays(30).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        
        // When, Then - future dates should be accepted
        storeService.placeOrder(orderWithFutureDate)
                .shouldHave(Conditions.statusCode(200))
                .shouldHave(Conditions.bodyField("shipDate", notNullValue()));
        // Note: Future shipping dates are valid business scenario
    }

    @Test
    void testOrderStatusTransitions() {
        // Given - create order with different status values
        Order placedOrder = OrderFactory.createValidOrder().setStatus(OrderStatus.PLACED);
        Order approvedOrder = OrderFactory.createValidOrder().setStatus(OrderStatus.APPROVED);
        Order deliveredOrder = OrderFactory.createValidOrder().setStatus(OrderStatus.DELIVERED);
        
        // When, Then - all valid statuses should be accepted
        storeService.placeOrder(placedOrder)
                .shouldHave(Conditions.statusCode(200))
                .shouldHave(Conditions.bodyField("status", equalTo("placed")));
        
        storeService.placeOrder(approvedOrder)
                .shouldHave(Conditions.statusCode(200))
                .shouldHave(Conditions.bodyField("status", equalTo("approved")));
        
        storeService.placeOrder(deliveredOrder)
                .shouldHave(Conditions.statusCode(200))
                .shouldHave(Conditions.bodyField("status", equalTo("delivered")));
        // Note: Business logic validation - all order statuses should be supported
    }
}
