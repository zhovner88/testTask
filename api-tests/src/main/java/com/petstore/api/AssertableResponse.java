package com.petstore.api;

import com.petstore.api.conditions.Condition;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class AssertableResponse {

    private final ValidatableResponse response;

    @Step("API response should have {condition}") // that allows logging in Allure repot for each verification step
    public AssertableResponse shouldHave(Condition condition) {
        long start = System.currentTimeMillis();
        try {
            condition.check(response);
            log.info("PASSED: {} ({}ms)", condition, System.currentTimeMillis() - start);
        } catch (AssertionError e) {
            log.error("FAILED: {} ({}ms)", condition, System.currentTimeMillis() - start);
            throw e;
        }
        return this;
    }
    
    // Parse value with type
    public <T> T parseValue(String jsonPath, Class<T> type) {
        log.info("Extracting {} value from path: {}", type.getSimpleName(), jsonPath);
        return response.extract().path(jsonPath);
    }
    
    // Get status code
    public int getStatusCode() {
        return response.extract().statusCode();
    }
    
    // Get response body as string
    public String getResponseBody() {
        return response.extract().asString();
    }
    
    // Map response to DTO object
    public <T> T as(Class<T> dtoClass) {
        log.info("Mapping response to {} DTO", dtoClass.getSimpleName());
        return response.extract().as(dtoClass);
    }
}
