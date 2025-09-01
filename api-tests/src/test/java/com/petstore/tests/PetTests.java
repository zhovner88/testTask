package com.petstore.tests;

import factory.UserFactory;
import io.restassured.RestAssured;
import model.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import services.UserApiService;

import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.not;

public class PetTests {
    @BeforeAll
    static void setup() {
        RestAssured.baseURI = "https://petstore.swagger.io/v2";
    }

    private UserApiService apiService = new UserApiService();

    @Test
    void testCanCreateUserWishlist() {
        // given
        User user = UserFactory.createRandomUser();

        // expect
        apiService.registerUser(user)
                .assertThat()
                .body("id", not(emptyString()));

    }
}
