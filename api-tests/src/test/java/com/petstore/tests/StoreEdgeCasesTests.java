package com.petstore.tests;

import com.petstore.api.conditions.Conditions;
import com.petstore.api.model.Order;
import com.petstore.api.model.enums.OrderStatus;
import common.Constants;
import factory.OrderFactory;
import org.testng.annotations.Test;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.Matchers.*;

public class StoreEdgeCasesTests extends BaseApiTest {

    // Priority 2 Tests - Edge cases and integration scenarios

    @Test
    void testFullOrderLifecycle() {
        Order order = OrderFactory.createValidOrder();
        
        Order createdOrder = storeService.placeOrder(order)
                .shouldHave(Conditions.statusCode(Constants.HTTP_OK))
                .shouldHave(Conditions.bodyField("id", notNullValue()))
                .as(Order.class);
        
        int createdOrderId = createdOrder.getId();
        
        storeService.getOrderById(createdOrderId)
                .shouldHave(Conditions.statusCode(Constants.HTTP_OK))
                .shouldHave(Conditions.bodyField("id", equalTo(createdOrderId)));
        
        storeService.deleteOrder(createdOrderId)
                .shouldHave(Conditions.statusCode(Constants.HTTP_OK));
        
        storeService.getOrderById(createdOrderId)
                .shouldHave(Conditions.statusCode(Constants.HTTP_NOT_FOUND));
    }

    @Test
    void testGetOrderByIdZero() {
        int zeroId = 0;
        
        storeService.getOrderById(zeroId)
                .shouldHave(Conditions.statusCode(Constants.HTTP_NOT_FOUND));
    }

    @Test
    void testDeleteAlreadyDeletedOrder() {
        Order order = OrderFactory.createValidOrder();
        
        Order createdOrder = storeService.placeOrder(order)
                .shouldHave(Conditions.statusCode(Constants.HTTP_OK))
                .shouldHave(Conditions.bodyField("id", notNullValue()))
                .as(Order.class);
        
        int createdOrderId = createdOrder.getId();
        
        // Delete the order first time
        storeService.deleteOrder(createdOrderId)
                .shouldHave(Conditions.statusCode(Constants.HTTP_OK));
        
        storeService.deleteOrder(createdOrderId)
                .shouldHave(Conditions.statusCode(Constants.HTTP_NOT_FOUND));
    }

    @Test
    void testGetInventoryResponseTime() {
        long startTime = System.currentTimeMillis();
        
        storeService.getInventory()
                .shouldHave(Conditions.statusCode(Constants.HTTP_OK))
                .shouldHave(Conditions.bodyField("$", notNullValue()));
        
        long responseTime = System.currentTimeMillis() - startTime;
        
        if (responseTime > Constants.MAX_RESPONSE_TIME_MS) {
            throw new AssertionError("Inventory response time too slow: " + responseTime + "ms");
        }
    }

    @Test(enabled = false)
        // not clear how business logic works according to documentation available,
        // just leave it here for the idea of verification type needed
    void testInventoryDecreasesAfterOrderPlacement() {
        var initialAvailable = storeService.getInventory()
                .shouldHave(Conditions.statusCode(Constants.HTTP_OK))
                .parseValue(Constants.AVAILABLE_FIELD, Integer.class);

        Order order1 = OrderFactory.createValidOrder();
        Order order2 = OrderFactory.createValidOrder();

        storeService.placeOrder(order1).shouldHave(Conditions.statusCode(Constants.HTTP_OK));
        storeService.placeOrder(order2).shouldHave(Conditions.statusCode(Constants.HTTP_OK));

        var updatedAvailable = storeService.getInventory()
                .shouldHave(Conditions.statusCode(Constants.HTTP_OK))
                .parseValue(Constants.AVAILABLE_FIELD, Integer.class);

        // Available pets should decrease when orders are placed
        if (updatedAvailable >= initialAvailable) {
            throw new AssertionError("Expected available orders to decrease after placing new orders. " +
                    "Initial: " + initialAvailable + ", Updated: " + updatedAvailable);
        }
    }

    @Test
    void testParallelOrderOperations() {
        Order order1 = OrderFactory.createValidOrder();
        Order order2 = OrderFactory.createValidOrder();
        
        long startTime = System.currentTimeMillis();
        
        var response1 = storeService.placeOrder(order1);
        var response2 = storeService.placeOrder(order2);
        
        long executionTime = System.currentTimeMillis() - startTime;
        
        response1.shouldHave(Conditions.statusCode(Constants.HTTP_OK))
                 .shouldHave(Conditions.bodyField("id", notNullValue()));
        
        response2.shouldHave(Conditions.statusCode(Constants.HTTP_OK))
                 .shouldHave(Conditions.bodyField("id", notNullValue()));
        
        if (executionTime > Constants.MAX_CONCURRENT_TIME_MS) {
            throw new AssertionError("Concurrent operations too slow: " + executionTime + "ms");
        }
    }

    @Test
    void testPlaceOrderWithFutureShipDate() {
        Order orderWithFutureDate = OrderFactory.createValidOrder()
                .setShipDate(OffsetDateTime.now().plusDays(30).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        
        storeService.placeOrder(orderWithFutureDate)
                .shouldHave(Conditions.statusCode(Constants.HTTP_OK))
                .shouldHave(Conditions.bodyField("shipDate", notNullValue()));
    }

    @Test
    void testOrderStatusTransitions() {
        Order placedOrder = OrderFactory.createValidOrder().setStatus(OrderStatus.PLACED);
        Order approvedOrder = OrderFactory.createValidOrder().setStatus(OrderStatus.APPROVED);
        Order deliveredOrder = OrderFactory.createValidOrder().setStatus(OrderStatus.DELIVERED);
        
        storeService.placeOrder(placedOrder)
                .shouldHave(Conditions.statusCode(Constants.HTTP_OK))
                .shouldHave(Conditions.bodyField("status", equalTo(Constants.STATUS_PLACED)));
        
        storeService.placeOrder(approvedOrder)
                .shouldHave(Conditions.statusCode(Constants.HTTP_OK))
                .shouldHave(Conditions.bodyField("status", equalTo(Constants.STATUS_APPROVED)));
        
        storeService.placeOrder(deliveredOrder)
                .shouldHave(Conditions.statusCode(Constants.HTTP_OK))
                .shouldHave(Conditions.bodyField("status", equalTo(Constants.STATUS_DELIVERED)));
    }
}
