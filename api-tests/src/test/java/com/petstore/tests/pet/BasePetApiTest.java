package com.petstore.tests.pet;

import com.petstore.api.services.StoreApiService;
import com.petstore.api.services.UserApiService;
import common.Constants;
import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;

public abstract class BasePetApiTest {

    protected final StoreApiService storeService = new StoreApiService();
    protected final UserApiService userApiService = new UserApiService();

    // Can't keep one BaseApiTest class on package above. That leads to incorrect BASE_URL initialization.

    @BeforeClass
    static void setup() {
        RestAssured.baseURI = Constants.BASE_URI;
    }
}