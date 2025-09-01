package com.petstore.api.conditions;

import io.restassured.response.ValidatableResponse;
import org.hamcrest.Matcher;

public record BodyFieldCondition(String fieldPath, Matcher<?> matcher) implements Condition {

    @Override
    public void check(ValidatableResponse response) {
        response.assertThat().body(fieldPath, matcher);
    }

    @Override
    public String toString() {
        return String.format("Body field '%s' should match %s", fieldPath, matcher);
    }
}