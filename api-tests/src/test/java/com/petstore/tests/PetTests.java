package com.petstore.tests;

import com.petstore.api.conditions.Conditions;
import com.petstore.api.model.User;
import common.Constants;
import factory.UserFactory;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.notNullValue;

public class PetTests extends BaseApiTest {

    @Test(enabled = false)
    void testCanCreateUserWishlist() {
        User user = UserFactory.createRandomUser();

        userApiService.registerUser(user)
                .shouldHave(Conditions.statusCode(Constants.HTTP_OK))
                .shouldHave(Conditions.bodyField("message", notNullValue()));

    }
}
