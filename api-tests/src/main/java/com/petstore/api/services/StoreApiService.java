package com.petstore.api.services;

import com.petstore.api.AssertableResponse;
import com.petstore.api.model.Order;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.XSlf4j;

@XSlf4j
public class StoreApiService {

    public RequestSpecification setUpRequest() {
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .filters(
                        new RequestLoggingFilter(),
                        new ResponseLoggingFilter(),
                        new AllureRestAssured()
                );
    }
    
    public RequestSpecification setUpRequestWithApiKey(String apiKey) {
        return setUpRequest()
                .header("api_key", apiKey);
    }

    // GET /store/inventory
    public AssertableResponse getInventory() {
        log.info("Get store inventory");
        
        return new AssertableResponse(setUpRequest()
                .when()
                .get("store/inventory")
                .then());
    }
    
    public AssertableResponse getInventoryWithApiKey(String apiKey) {
        log.info("Get store inventory with API key: {}", apiKey);
        
        return new AssertableResponse(setUpRequestWithApiKey(apiKey)
                .when()
                .get("store/inventory")
                .then());
    }

    // POST /store/order
    public AssertableResponse placeOrder(Order order) {
        log.info("Place order: {}", order);

        return new AssertableResponse(setUpRequest()
                .body(order)
                .when()
                .post("store/order")
                .then());
    }

    // GET /store/order/{orderId}
    public AssertableResponse getOrderById(int orderId) {
        log.info("Get order by ID: {}", orderId);

        return new AssertableResponse(setUpRequest()
                .pathParam("orderId", orderId)
                .when()
                .get("store/order/{orderId}")
                .then());
    }

    // DELETE /store/order/{orderId}
    public AssertableResponse deleteOrder(int orderId) {
        log.info("Delete order by ID: {}", orderId);

        return new AssertableResponse(setUpRequest()
                .pathParam("orderId", orderId)
                .when()
                .delete("store/order/{orderId}")
                .then());
    }
}