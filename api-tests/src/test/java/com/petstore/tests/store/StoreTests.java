package com.petstore.tests.store;

import com.petstore.api.conditions.Conditions;
import com.petstore.api.model.Order;
import common.Constants;
import factory.OrderFactory;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.*;

public class StoreTests extends BaseStoreApiTest {

    // Priority 1 Tests: Critical functionality

    @Test
    void testGetInventoryWithValidApiKey() {
        // Given, When, Then
        storeService.getInventoryWithApiKey(Constants.SPECIAL_API_KEY)
                .shouldHave(Conditions.statusCode(200))
                .shouldHave(Conditions.bodyField("$", notNullValue()));
    }

    @Test
    void testGetInventoryWithoutApiKey() {
        // Given, When, Then
        storeService.getInventory()
                .shouldHave(Conditions.statusCode(200))
                .shouldHave(Conditions.bodyField("$", notNullValue()));
    }

    @Test
    void testGetInventoryResponseStructure() {
        // Given, When, Then
        storeService.getInventoryWithApiKey(Constants.SPECIAL_API_KEY)
                .shouldHave(Conditions.statusCode(200))
                .shouldHave(Conditions.bodyField("$", instanceOf(Object.class)));
        // Note: inventory returns object with dynamic keys, so we check it's an object
    }

    @Test
    void testPlaceOrderWithValidData() {
        // Given
        Order order = OrderFactory.createValidOrder();
        
        // When, Then
        storeService.placeOrder(order)
                .shouldHave(Conditions.statusCode(200))
                .shouldHave(Conditions.bodyField("id", notNullValue()))
                .shouldHave(Conditions.bodyField("petId", notNullValue()))
                .shouldHave(Conditions.bodyField("status", notNullValue()));
    }

    @Test
    void testGetOrderByValidId() {
        // Given - using ID in valid ID range (1-10)
        int validOrderId = Constants.VALID_ORDER_ID_FOR_TESTING;
        
        // When, Then
        storeService.getOrderById(validOrderId)
                .shouldHave(Conditions.statusCode(200));
        // Note: might return 404 if order doesn't exist, which is also valid
    }

    @Test
    void testGetOrderByUpperBoundaryId() {
        // Given - using upper boundary ID (according to Swagger spec: valid range 1-10)
        int upperBoundaryId = Constants.ORDER_ID_UPPER_LIMIT;
        
        // When, Then
        storeService.getOrderById(upperBoundaryId)
                .shouldHave(Conditions.statusCode(200));
        // Note: ID=10 is within valid range, might return 404 if order doesn't exist
    }

    @Test
    void testDeleteExistingOrder() {
        // Given - create an order
        Order order = OrderFactory.createValidOrder();
        
        // When, Then
        int createdOrderId = storeService.placeOrder(order)
                .shouldHave(Conditions.statusCode(200))
                .shouldHave(Conditions.bodyField("id", notNullValue()))
                .parseValue("id", Integer.class);
        
        // When, Then - delete the created order
        storeService.deleteOrder(createdOrderId)
                .shouldHave(Conditions.statusCode(200));
    }

}
