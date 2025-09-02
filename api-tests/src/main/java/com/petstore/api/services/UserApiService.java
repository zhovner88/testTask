package com.petstore.api.services;

import com.petstore.api.AssertableResponse;
import com.petstore.api.model.User;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserApiService extends BaseApiService {

    // any custom headers might be defined here if needed
    // POST /user
    public AssertableResponse registerUser(User user) {
        log.info("Register user {}", user);

        return new AssertableResponse(setUpRequest()
                .body(user)
                .when()
                .post("user")
                .then()) {
        };
    }

}

