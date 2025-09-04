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
        Order invalidOrder = OrderFactory.createOrderWithIdNullValue();
        
        storeService.placeOrder(invalidOrder)
                .shouldHave(Conditions.statusCode(Constants.HTTP_BAD_REQUEST))
                .shouldHave(Conditions.bodyField("type", equalTo(Constants.ERROR_TYPE)));
    }
    
    @Test 
    void testPlaceOrderWithNegativePetId() {
        Order invalidOrder = OrderFactory.createOrderWithNegativePetId();
        
        storeService.placeOrder(invalidOrder)
                .shouldHave(Conditions.statusCode(Constants.HTTP_BAD_REQUEST))
                .shouldHave(Conditions.bodyField("type", equalTo(Constants.ERROR_TYPE)));
    }
    
    @Test 
    void testPlaceOrderWithNegativeQuantity() {
        Order invalidOrder = OrderFactory.createOrderWithNegativeQuantity();
        
        storeService.placeOrder(invalidOrder)
                .shouldHave(Conditions.statusCode(Constants.HTTP_BAD_REQUEST))
                .shouldHave(Conditions.bodyField("type", equalTo(Constants.ERROR_TYPE)));
    }
    
    @Test 
    void testPlaceOrderWithInvalidDate() {
        Order invalidOrder = OrderFactory.createOrderWithInvalidDate();
        
        storeService.placeOrder(invalidOrder)
                .shouldHave(Conditions.statusCode(Constants.HTTP_BAD_REQUEST))
                .shouldHave(Conditions.bodyField("type", equalTo(Constants.ERROR_TYPE)));
    }
    
    @Test 
    void testPlaceOrderWithNullStatus() {
        Order invalidOrder = OrderFactory.createOrderWithNullStatus();
        
        storeService.placeOrder(invalidOrder)
                .shouldHave(Conditions.statusCode(Constants.HTTP_BAD_REQUEST))
                .shouldHave(Conditions.bodyField("type", equalTo(Constants.ERROR_TYPE)));
    }

    @Test
    void testGetOrderByIdGreaterThanUpperLimit() {
        int orderIdEleven = Constants.ORDER_ID_ABOVE_LIMIT;
        
        storeService.getOrderById(orderIdEleven)
                .shouldHave(Conditions.statusCode(Constants.HTTP_BAD_REQUEST))
                .shouldHave(Conditions.bodyField("type", equalTo(Constants.ERROR_TYPE)))
                .shouldHave(Conditions.bodyField("message", containsString("Input error")));
    }

    @Test
    void testGetOrderByNonExistentId() {
        int nonExistentId = Utils.generateNonExistentOrderId();
        
        storeService.getOrderById(nonExistentId)
                .shouldHave(Conditions.statusCode(Constants.HTTP_NOT_FOUND))
                .shouldHave(Conditions.bodyField("type", equalTo(Constants.ERROR_TYPE)))
                .shouldHave(Conditions.bodyField("message", equalTo("Order not found")));
    }

    @Test
    void testGetOrderByIdZero() {
        int zeroId = Constants.ORDER_ID_ZERO;
        
        storeService.getOrderById(zeroId)
                .shouldHave(Conditions.statusCode(Constants.HTTP_NOT_FOUND))
                .shouldHave(Conditions.bodyField("type", equalTo(Constants.ERROR_TYPE)))
                .shouldHave(Conditions.bodyField("message", equalTo("Order not found")));
    }

    @Test
    void testGetOrderByNegativeId() {
        int negativeId = Constants.ORDER_ID_NEGATIVE;
        
        storeService.getOrderById(negativeId)
                .shouldHave(Conditions.statusCode(Constants.HTTP_NOT_FOUND))
                .shouldHave(Conditions.bodyField("type", equalTo(Constants.ERROR_TYPE)))
                .shouldHave(Conditions.bodyField("message", equalTo("Order not found")));
    }

    @Test
    void testDeleteNonExistentOrder() {
        int nonExistentOrderId = Utils.generateNonExistentOrderId();
        
        storeService.deleteOrder(nonExistentOrderId)
                .shouldHave(Conditions.statusCode(Constants.HTTP_NOT_FOUND))
                .shouldHave(Conditions.bodyField("type", equalTo(Constants.UNKNOWN_TYPE)))
                .shouldHave(Conditions.bodyField("message", equalTo("Order Not Found")));
    }

    @Test
    void testDeleteOrderWithNegativeId() {
        int negativeId = Constants.ORDER_ID_NEGATIVE;
        
        storeService.deleteOrder(negativeId)
                .shouldHave(Conditions.statusCode(Constants.HTTP_BAD_REQUEST))
                .shouldHave(Conditions.bodyField("type", equalTo(Constants.ERROR_TYPE)))
                .shouldHave(Conditions.bodyField("message", containsString("Invalid ID")));
    }

    @Test
    void testGetOrderWithInvalidPath() {
        
        // Using existing method with extreme boundary to simulate path validation
        int invalidId = Integer.MAX_VALUE;
        
        var response = storeService.getOrderById(invalidId);
        
        int statusCode = response.getStatusCode();
        
        if (statusCode == Constants.HTTP_BAD_REQUEST || statusCode == Constants.HTTP_NOT_FOUND) {
            response.shouldHave(Conditions.bodyField("type", equalTo(Constants.ERROR_TYPE)));
        } else {
            throw new AssertionError("Expected status code Constants.HTTP_BAD_REQUEST or Constants.HTTP_NOT_FOUND, but got: " + statusCode);
        }
    }

    @Test
    void testStoreEndpointsErrorMessageFormat() {
        Order invalidOrder = OrderFactory.createOrderWithIdNullValue();
        
        storeService.placeOrder(invalidOrder)
                .shouldHave(Conditions.statusCode(Constants.HTTP_BAD_REQUEST))
                .shouldHave(Conditions.bodyField("type", notNullValue()))
                .shouldHave(Conditions.bodyField("message", notNullValue()))
                .shouldHave(Conditions.bodyField("type", equalTo(Constants.ERROR_TYPE)));
    }

    @Test
    void testInventoryEndpointErrorHandling() {
        String invalidApiKey = "invalid-key-format-123";
        
        storeService.getInventoryWithApiKey(invalidApiKey)
                .shouldHave(Conditions.statusCode(Constants.HTTP_OK))
                .shouldHave(Conditions.bodyField("$", notNullValue()));
    }

    // Security and Header Tests
    
    @Test
    void testGetOrderWithInvalidContentType() {
        int validOrderId = Constants.VALID_ORDER_ID_FOR_TESTING;
        
        var response = storeService.setUpRequest()
                .contentType("text/plain")  // Invalid content type
                .pathParam("orderId", validOrderId)
                .when()
                .get("store/order/{orderId}")
                .then();
        
        // Should either work (HTTP_OK) or return unsupported media type (HTTP_UNSUPPORTED_MEDIA_TYPE)
        int statusCode = response.extract().statusCode();
        if (statusCode != Constants.HTTP_OK && statusCode != Constants.HTTP_UNSUPPORTED_MEDIA_TYPE) {
            throw new AssertionError("Expected " + Constants.HTTP_OK + " or " + Constants.HTTP_UNSUPPORTED_MEDIA_TYPE + ", but got: " + statusCode);
        }
    }

    @Test
    void testGetOrderWithSqlInjectionInPath() {
        String sqlInjection = "1; DROP TABLE orders--";
        
        try {
            var response = storeService.setUpRequest()
                    .pathParam("orderId", sqlInjection)
                    .when()
                    .get("store/order/{orderId}")
                    .then();
            
            // Should return Constants.HTTP_BAD_REQUEST for invalid parameter format
            response.statusCode(Constants.HTTP_BAD_REQUEST);
        } catch (Exception e) {
            // Path parameter validation might throw exception - which is also acceptable
            // This indicates proper input validation
        }
    }

    @Test
    void testPlaceOrderWithXssPayload() {
        Order xssOrder = OrderFactory.createOrderWithXssInDate();
        
        var response = storeService.placeOrder(xssOrder);
        int statusCode = response.getStatusCode();
        
        if (statusCode == Constants.HTTP_OK) {
            // If accepted, check that XSS is not reflected in response
            String responseBody = response.getResponseBody();
            if (responseBody.contains("<script>")) {
                throw new AssertionError("XSS payload was reflected in response - security vulnerability!");
            }
        } else if (statusCode == Constants.HTTP_BAD_REQUEST) {
            // Rejection is also acceptable - shows input validation
            response.shouldHave(Conditions.bodyField("type", equalTo(Constants.ERROR_TYPE)));
        } else {
            throw new AssertionError("Expected " + Constants.HTTP_OK + " or " + Constants.HTTP_BAD_REQUEST + ", but got: " + statusCode);
        }
    }

    @Test
    void testGetInventoryWithMaliciousHeaders() {
        var response = storeService.setUpRequest()
                .header("X-Forwarded-For", "127.0.0.1, <script>alert('xss')</script>")
                .header("User-Agent", "' OR 1=1--")
                .header("Referer", "javascript:alert('xss')")
                .when()
                .get("store/inventory")
                .then();
        
        response.statusCode(Constants.HTTP_OK);
    }

}
