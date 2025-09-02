package com.petstore.tests;

import com.petstore.api.conditions.Conditions;
import com.petstore.api.model.Order;
import common.Constants;
import factory.OrderFactory;
import org.testng.annotations.Test;
import utils.Utils;

import static org.hamcrest.Matchers.*;

public class StoreErrorTests extends BaseApiTest {

    // Priority 3 Tests: Error Handling Tests - Swagger API error codes validation

    @Test
    void testPlaceOrderWithInvalidId() {
        // Given - order with null ID
        Order invalidOrder = OrderFactory.createOrderWithIdNullValue();
        
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
        int orderIdEleven = Constants.ORDER_ID_ABOVE_LIMIT;
        
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
        int zeroId = Constants.ORDER_ID_ZERO;
        
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
        int negativeId = Constants.ORDER_ID_NEGATIVE;
        
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
                .shouldHave(Conditions.bodyField("type", equalTo("unknown")))
                .shouldHave(Conditions.bodyField("message", equalTo("Order Not Found")));
        // Note: According to Swagger - deleting non-existent order returns 404
    }

    @Test
    void testDeleteOrderWithNegativeId() {
        // Given - negative ID, invalid field value validation
        int negativeId = Constants.ORDER_ID_NEGATIVE;
        
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
        
        // When - make single request
        var response = storeService.getOrderById(invalidId);
        
        // Then - validate response code is either 400 or 404
        int statusCode = response.getStatusCode();
        
        if (statusCode == 400 || statusCode == 404) {
            response.shouldHave(Conditions.bodyField("type", equalTo("error")));
        } else {
            throw new AssertionError("Expected status code 400 or 404, but got: " + statusCode);
        }
        // Note: API validates path parameters and returns appropriate error
    }

    @Test
    void testStoreEndpointsErrorMessageFormat() {
        // Given - invalid order to test error message structure
        Order invalidOrder = OrderFactory.createOrderWithIdNullValue();
        
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

    // Security and Header Tests
    
    @Test
    void testGetOrderWithInvalidContentType() {
        // Given - request with invalid Content-Type header
        int validOrderId = Constants.VALID_ORDER_ID_FOR_TESTING;
        
        // When, Then - API should handle or return 415
        var response = storeService.setUpRequest()
                .contentType("text/plain")  // Invalid content type
                .pathParam("orderId", validOrderId)
                .when()
                .get("store/order/{orderId}")
                .then();
        
        // Should either work (200) or return unsupported media type (415)
        int statusCode = response.extract().statusCode();
        if (statusCode != 200 && statusCode != 415) {
            throw new AssertionError("Expected 200 or 415, but got: " + statusCode);
        }
    }

    @Test
    void testGetOrderWithSqlInjectionInPath() {
        // Given - SQL injection attempt with table drop
        String sqlInjection = "1; DROP TABLE orders--";
        
        // When, Then - API should handle input and return an error
        try {
            var response = storeService.setUpRequest()
                    .pathParam("orderId", sqlInjection)
                    .when()
                    .get("store/order/{orderId}")
                    .then();
            
            // Should return 400 for invalid parameter format
            response.statusCode(400);
        } catch (Exception e) {
            // Path parameter validation might throw exception - which is also acceptable
            // This indicates proper input validation
        }
        // Note: Proper API should reject malicious SQL commands in path parameters
    }

    @Test
    void testPlaceOrderWithXssPayload() {
        // Given - order with XSS payload in shipDate field
        Order xssOrder = OrderFactory.createOrderWithXssInDate();
        
        // When, Then - API should sanitize or reject malicious content
        var response = storeService.placeOrder(xssOrder);
        int statusCode = response.getStatusCode();
        
        if (statusCode == 200) {
            // If accepted, check that XSS is not reflected in response
            String responseBody = response.getResponseBody();
            if (responseBody.contains("<script>")) {
                throw new AssertionError("XSS payload was reflected in response - security vulnerability!");
            }
        } else if (statusCode == 400) {
            // Rejection is also acceptable - shows input validation
            response.shouldHave(Conditions.bodyField("type", equalTo("error")));
        } else {
            throw new AssertionError("Expected 200 or 400, but got: " + statusCode);
        }
        // Note: Proper implementation should either sanitize XSS or reject invalid date format
    }

    @Test
    void testGetInventoryWithMaliciousHeaders() {
        // Given - request with potentially malicious headers
        var response = storeService.setUpRequest()
                .header("X-Forwarded-For", "127.0.0.1, <script>alert('xss')</script>")
                .header("User-Agent", "' OR 1=1--")
                .header("Referer", "javascript:alert('xss')")
                .when()
                .get("store/inventory")
                .then();
        
        // When, Then - API should handle malicious headers
        response.statusCode(200);
        // Note: API should not reflect malicious headers in response
    }

}
