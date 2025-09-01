package com.petstore.tests;

import com.petstore.api.conditions.Conditions;
import factory.UserFactory;
import io.restassured.RestAssured;
import com.petstore.api.model.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import com.petstore.api.model.services.UserApiService;

import static org.hamcrest.Matchers.notNullValue;

public class PetTests {
    @BeforeAll
    static void setup() {
        RestAssured.baseURI = "https://petstore.swagger.io/v2";
    }

    private final UserApiService apiService = new UserApiService();

    @Test
    void testCanCreateUserWishlist() {
        // given
        User user = UserFactory.createRandomUser();

        // expect
        apiService.registerUser(user)
                .shouldHave(Conditions.statusCode(200))
                .shouldHave(Conditions.bodyField("message", notNullValue()));

    }
}
