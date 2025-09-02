package com.petstore.tests;

import com.petstore.api.conditions.Conditions;
import com.petstore.api.model.Order;
import factory.OrderFactory;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.*;

public class StoreEdgeCasesTests extends BaseApiTest {

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
}
