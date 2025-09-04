package com.petstore.tests;

import com.petstore.api.services.StoreApiService;
import com.petstore.api.services.UserApiService;
import common.Constants;
import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;

public abstract class BaseApiTest {

    protected final StoreApiService storeService = new StoreApiService();
    protected final UserApiService userApiService = new UserApiService();

    @BeforeClass
    static void setup() {
        RestAssured.baseURI = Constants.BASE_URI;
    }
}