package com.petstore.tests;

import com.petstore.api.conditions.Conditions;
import com.petstore.api.model.User;
import factory.UserFactory;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.notNullValue;

public class PetTests extends BaseApiTest {

    @Test
    @Disabled
    void testCanCreateUserWishlist() {
        // given
        User user = UserFactory.createRandomUser();

        // expect
        userApiService.registerUser(user)
                .shouldHave(Conditions.statusCode(200))
                .shouldHave(Conditions.bodyField("message", notNullValue()));

    }
}
