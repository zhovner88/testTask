package com.petstore.tests.store;

import com.petstore.api.services.StoreApiService;
import com.petstore.api.services.UserApiService;
import common.Constants;
import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;

public abstract class BaseStoreApiTest {

    // Can't keep one BaseApiTest class on package above. That leads to incorrect BASE_URL initialization.

    protected final StoreApiService storeService = new StoreApiService();
    protected final UserApiService userApiService = new UserApiService();
    
    @BeforeClass
    static void setup() {
        RestAssured.baseURI = Constants.BASE_URI;
    }
}