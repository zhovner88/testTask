package com.petstore.api.conditions;

import org.hamcrest.Matcher;

public class Conditions {

    public static StatusCodeCondition statusCode(int code) {
        return new StatusCodeCondition(code);
    }

    public static BodyFieldCondition bodyField(String fieldPath, Matcher<?> matcher) {
        return new BodyFieldCondition(fieldPath, matcher);
    }

}
