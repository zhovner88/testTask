package com.petstore.tests;

import com.petstore.api.conditions.Conditions;
import com.petstore.api.model.Order;
import common.Constants;
import factory.OrderFactory;
import org.junit.jupiter.api.Test;
import utils.Utils;

import static org.hamcrest.Matchers.*;

public class StoreTests extends BaseApiTest {

    // Priority 1 Tests - Critical functionality

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
    void testPlaceOrderWithInvalidData() {
        // Given - order with invalid data
        Order invalidOrder = OrderFactory.createOrderWithInvalidData();
        
        // When, Then
        storeService.placeOrder(invalidOrder)
                .shouldHave(Conditions.statusCode(400));
    }

    @Test
    void testPlaceOrderWithInvalidPetId() {
        // Given
        Order order = OrderFactory.createOrderWithPetId(-1);
        
        // When, Then
        storeService.placeOrder(order)
                .shouldHave(Conditions.statusCode(400));
        // Note: Simplified - expecting 400 for invalid petId
    }

    @Test
    void testGetOrderByValidId() {
        // Given - using ID in valid ID range (1-10)
        int validOrderId = 5;
        
        // When, Then
        storeService.getOrderById(validOrderId)
                .shouldHave(Conditions.statusCode(200));
        // Note: might return 404 if order doesn't exist, which is also valid
    }

    @Test
    void testGetOrderByIdGreaterThanUpperLimit() {
        // Given - boundary test with ID > 10 (equivalent partitioning)
        int orderIdEleven = 11;
        
        // When, Then
        storeService.getOrderById(orderIdEleven)
                .shouldHave(Conditions.statusCode(400));

        // Note: For valid response try integer IDs with value >= 1 and <= 10.
        // Other values will generate exceptions
    }

    @Test
    void testGetOrderByNonExistentId() {
        // Given - generate a very large random ID that most likely doesn't exist
        int nonExistentId = Utils.generateNonExistentOrderId();
        
        // When, Then
        storeService.getOrderById(nonExistentId)
                .shouldHave(Conditions.statusCode(404));
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

    @Test
    void testDeleteNonExistentOrder() {
        // Given - generate a very large random ID that most likely doesn't exist
        int nonExistentOrderId = Utils.generateNonExistentOrderId();
        
        // When, Then
        storeService.deleteOrder(nonExistentOrderId)
                .shouldHave(Conditions.statusCode(404));
    }

    @Test
    void testDeleteOrderWithNegativeId() {
        // Given
        int negativeId = -5;
        
        // When, Then
        storeService.deleteOrder(negativeId)
                .shouldHave(Conditions.statusCode(400));
        // Note: according to swagger documentation, negative values should generate API errors
    }
}
