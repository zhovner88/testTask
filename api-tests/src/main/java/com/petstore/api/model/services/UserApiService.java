package com.petstore.api.model.services;

import com.petstore.api.AssertableResponse;
import com.petstore.api.model.User;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.XSlf4j;

@XSlf4j
public class UserApiService {

    public RequestSpecification setUpReqeust() {
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .filters(
                        new RequestLoggingFilter(),
                        new ResponseLoggingFilter(),
                        new AllureRestAssured()
                );
    }

    // any custom headers might be defined here if needed
    public AssertableResponse registerUser(User user) {
        log.info("Register user {}", user);

        return new AssertableResponse(setUpReqeust()
                .body(user)
                .when()
                .post("user")
                .then()) {
        };
    }

}

