package services;

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.XSlf4j;
import model.User;

@XSlf4j
public class UserApiService {

    public RequestSpecification setUpReqeust() {
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }

    // any custom headers might be defined here if needed
    public ValidatableResponse registerUser(User user) {
        log.info("Register user {}", user);

        return setUpReqeust()
                .body(user)
                .when()
                .post("user")
                .then();
    }

}

