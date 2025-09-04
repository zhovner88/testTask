package com.petstore.tests;

import com.petstore.api.conditions.Conditions;
import com.petstore.api.model.Order;
import common.Constants;
import factory.OrderFactory;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.*;

public class StoreTests extends BaseApiTest {

    // Priority 1 Tests: Critical functionality

    @Test
    void testGetInventoryWithValidApiKey() {
        storeService.getInventoryWithApiKey(Constants.SPECIAL_API_KEY)
                .shouldHave(Conditions.statusCode(Constants.HTTP_OK))
                .shouldHave(Conditions.bodyField("$", notNullValue()));
    }

    @Test
    void testGetInventoryWithoutApiKey() {
        storeService.getInventory()
                .shouldHave(Conditions.statusCode(Constants.HTTP_OK))
                .shouldHave(Conditions.bodyField("$", notNullValue()));
    }

    @Test
    void testGetInventoryResponseStructure() {
        storeService.getInventoryWithApiKey(Constants.SPECIAL_API_KEY)
                .shouldHave(Conditions.statusCode(Constants.HTTP_OK))
                .shouldHave(Conditions.bodyField("$", instanceOf(Object.class)));
    }

    @Test
    void testPlaceOrderWithValidData() {
        Order order = OrderFactory.createValidOrder();
        
        storeService.placeOrder(order)
                .shouldHave(Conditions.statusCode(Constants.HTTP_OK))
                .shouldHave(Conditions.bodyField("id", notNullValue()))
                .shouldHave(Conditions.bodyField("petId", notNullValue()))
                .shouldHave(Conditions.bodyField("status", notNullValue()))
                .as(Order.class);
    }

    @Test
    void testGetOrderByValidId() {
        int validOrderId = Constants.VALID_ORDER_ID_FOR_TESTING;
        
        storeService.getOrderById(validOrderId)
                .shouldHave(Conditions.statusCode(Constants.HTTP_OK));
    }

    @Test
    void testGetOrderByUpperBoundaryId() {
        int upperBoundaryId = Constants.ORDER_ID_UPPER_LIMIT;
        
        storeService.getOrderById(upperBoundaryId)
                .shouldHave(Conditions.statusCode(Constants.HTTP_OK));
    }

    @Test
    void testDeleteExistingOrder() {
        Order order = OrderFactory.createValidOrder();
        
        Order createdOrder = storeService.placeOrder(order)
                .shouldHave(Conditions.statusCode(Constants.HTTP_OK))
                .shouldHave(Conditions.bodyField("id", notNullValue()))
                .as(Order.class);
        
        int createdOrderId = createdOrder.getId();
        
        storeService.deleteOrder(createdOrderId)
                .shouldHave(Conditions.statusCode(Constants.HTTP_OK));
    }

}
