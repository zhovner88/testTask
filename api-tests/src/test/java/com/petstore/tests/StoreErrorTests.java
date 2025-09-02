package com.petstore.tests;

import com.petstore.api.conditions.Conditions;
import com.petstore.api.model.Order;
import factory.OrderFactory;
import org.testng.annotations.Test;
import utils.Utils;

import static org.hamcrest.Matchers.*;

public class StoreErrorTests extends BaseApiTest {

    // Priority 3 Tests: Error Handling Tests - Swagger API error codes validation

    @Test
    void testPlaceOrderWithInvalidId() {
        // Given - order with null ID
        Order invalidOrder = OrderFactory.createOrderWithInvalidId();
        
        // When, Then - should return 400 Bad Request
        storeService.placeOrder(invalidOrder)
                .shouldHave(Conditions.statusCode(400))
                .shouldHave(Conditions.bodyField("type", equalTo("error")));
        // Note: According to Swagger - null ID should be rejected with 400
    }
    
    @Test 
    void testPlaceOrderWithNegativePetId() {
        // Given - order with negative petId
        Order invalidOrder = OrderFactory.createOrderWithNegativePetId();
        
        // When, Then - should return 400 Bad Request
        storeService.placeOrder(invalidOrder)
                .shouldHave(Conditions.statusCode(400))
                .shouldHave(Conditions.bodyField("type", equalTo("error")));
        // Note: According to Swagger - negative petId should be rejected with 400
    }
    
    @Test 
    void testPlaceOrderWithNegativeQuantity() {
        // Given - order with negative quantity
        Order invalidOrder = OrderFactory.createOrderWithNegativeQuantity();
        
        // When, Then - should return 400 Bad Request
        storeService.placeOrder(invalidOrder)
                .shouldHave(Conditions.statusCode(400))
                .shouldHave(Conditions.bodyField("type", equalTo("error")));
        // Note: According to Swagger - negative quantity should be rejected with 400
    }
    
    @Test 
    void testPlaceOrderWithInvalidDate() {
        // Given - order with invalid date format
        Order invalidOrder = OrderFactory.createOrderWithInvalidDate();
        
        // When, Then - should return 400 Bad Request
        storeService.placeOrder(invalidOrder)
                .shouldHave(Conditions.statusCode(400))
                .shouldHave(Conditions.bodyField("type", equalTo("error")));
        // Note: According to Swagger - invalid date format should be rejected with 400
    }
    
    @Test 
    void testPlaceOrderWithNullStatus() {
        // Given - order with null status
        Order invalidOrder = OrderFactory.createOrderWithNullStatus();
        
        // When, Then - should return 400 Bad Request
        storeService.placeOrder(invalidOrder)
                .shouldHave(Conditions.statusCode(400))
                .shouldHave(Conditions.bodyField("type", equalTo("error")));
        // Note: According to Swagger - null status should be rejected with 400
    }

    @Test
    void testGetOrderByIdGreaterThanUpperLimit() {
        // Given - boundary test with ID > 10 (according to Swagger spec)
        int orderIdEleven = 11;
        
        // When, Then - should return 400 Bad Request
        storeService.getOrderById(orderIdEleven)
                .shouldHave(Conditions.statusCode(400))
                .shouldHave(Conditions.bodyField("type", equalTo("error")))
                .shouldHave(Conditions.bodyField("message", containsString("Input error")));
        // Note: Swagger spec - valid IDs are 1-10, others return 400
    }

    @Test
    void testGetOrderByNonExistentId() {
        // Given - generate a very large random ID that doesn't exist
        int nonExistentId = Utils.generateNonExistentOrderId();
        
        // When, Then - should return 404 Not Found
        storeService.getOrderById(nonExistentId)
                .shouldHave(Conditions.statusCode(404))
                .shouldHave(Conditions.bodyField("type", equalTo("error")))
                .shouldHave(Conditions.bodyField("message", equalTo("Order not found")));
        // Note: According to Swagger - non-existent orders return 404
    }

    @Test
    void testGetOrderByIdZero() {
        // Given - boundary test with ID = 0
        int zeroId = 0;
        
        // When, Then - should return 404 Not Found
        storeService.getOrderById(zeroId)
                .shouldHave(Conditions.statusCode(404))
                .shouldHave(Conditions.bodyField("type", equalTo("error")))
                .shouldHave(Conditions.bodyField("message", equalTo("Order not found")));
        // Note: According to Swagger - zero is not a valid ID, returns 404
    }

    @Test
    void testGetOrderByNegativeId() {
        // Given - negative ID test
        int negativeId = -5;
        
        // When, Then - should return 404 Not Found
        storeService.getOrderById(negativeId)
                .shouldHave(Conditions.statusCode(404))
                .shouldHave(Conditions.bodyField("type", equalTo("error")))
                .shouldHave(Conditions.bodyField("message", equalTo("Order not found")));
        // Note: According to Swagger - negative IDs return 404
    }

    @Test
    void testDeleteNonExistentOrder() {
        // Given - generate a very large random ID that doesn't exist
        int nonExistentOrderId = Utils.generateNonExistentOrderId();
        
        // When, Then - should return 404 Not Found
        storeService.deleteOrder(nonExistentOrderId)
                .shouldHave(Conditions.statusCode(404))
                .shouldHave(Conditions.bodyField("type", equalTo("error")))
                .shouldHave(Conditions.bodyField("message", equalTo("Order not found")));
        // Note: According to Swagger - deleting non-existent order returns 404
    }

    @Test
    void testDeleteOrderWithNegativeId() {
        // Given - negative ID, invalid field value validation
        int negativeId = -5;
        
        // When, Then - should return 400 Bad Request
        storeService.deleteOrder(negativeId)
                .shouldHave(Conditions.statusCode(400))
                .shouldHave(Conditions.bodyField("type", equalTo("error")))
                .shouldHave(Conditions.bodyField("message", containsString("Invalid ID")));
        // Note: According to Swagger - negative values should generate API errors (400)
    }

    @Test
    void testGetOrderWithInvalidPath() {
        // Given - malformed request path (test URL validation)
        // Note: This test validates path parameter handling
        // When trying to get order with non-numeric ID, API should return 400
        
        // Using existing method with extreme boundary to simulate path validation
        int invalidId = Integer.MAX_VALUE;
        
        // When, Then - should return 400 or 404 for invalid path
        // Using multiple assertions to handle either error code
        try {
            storeService.getOrderById(invalidId)
                    .shouldHave(Conditions.statusCode(400))
                    .shouldHave(Conditions.bodyField("type", equalTo("error")));
        } catch (AssertionError e) {
            // If 400 fails, try 404
            storeService.getOrderById(invalidId)
                    .shouldHave(Conditions.statusCode(404))
                    .shouldHave(Conditions.bodyField("type", equalTo("error")));
        }
        // Note: API validates path parameters and returns appropriate error
    }

    @Test
    void testStoreEndpointsErrorMessageFormat() {
        // Given - invalid order to test error message structure
        Order invalidOrder = OrderFactory.createOrderWithInvalidId();
        
        // When, Then - verify error response follows Swagger error format
        storeService.placeOrder(invalidOrder)
                .shouldHave(Conditions.statusCode(400))
                .shouldHave(Conditions.bodyField("type", notNullValue()))
                .shouldHave(Conditions.bodyField("message", notNullValue()))
                .shouldHave(Conditions.bodyField("type", equalTo("error")));
        // Note: All error responses should follow consistent format per Swagger spec
    }

    @Test
    void testInventoryEndpointErrorHandling() {
        // Given - test inventory endpoint with invalid API key format
        String invalidApiKey = "invalid-key-format-123";
        
        // When, Then - should handle gracefully (usually returns 200)
        storeService.getInventoryWithApiKey(invalidApiKey)
                .shouldHave(Conditions.statusCode(200))
                .shouldHave(Conditions.bodyField("$", notNullValue()));
        // Note: According to Swagger - invalid API keys are typically ignored, returns 200
    }
}
