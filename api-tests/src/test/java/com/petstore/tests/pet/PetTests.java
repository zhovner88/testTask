package com.petstore.tests.pet;

import com.petstore.api.conditions.Conditions;
import com.petstore.api.model.User;
import com.petstore.tests.store.BaseStoreApiTest;
import factory.UserFactory;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.notNullValue;

public class PetTests extends BasePetApiTest {

    @Test(enabled = false)
    void testCanCreateUserWishlist() {
        // given
        User user = UserFactory.createRandomUser();

        // expect
        userApiService.registerUser(user)
                .shouldHave(Conditions.statusCode(200))
                .shouldHave(Conditions.bodyField("message", notNullValue()));

    }
}
