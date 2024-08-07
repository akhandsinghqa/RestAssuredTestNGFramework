package com.spotify.oauth2.api;

import io.restassured.response.Response;

import java.util.Map;

import static com.spotify.oauth2.api.SpecBuilder.*;
import static io.restassured.RestAssured.given;

public class RestApi {

    public static Response postAccount(String path, Map<String, String> formParams) {
        return given(getAccountRequestSpec()).
                formParams(formParams).
                when().
                post(path).
                then().
                spec(SpecBuilder.getResponseSpec()).
                extract().
                response();
    }

    public static Response post(String token, String path, Object request) {
        return given(getRequestSpec()).
                auth().oauth2(token).
                body(request).
                when().
                post(path).
                then().
                spec(getResponseSpec()).
                extract().
                response();
    }

    public static Response get(String token, String path) {
        return given(getRequestSpec()).
                auth().oauth2(token).
                when().
                get(path).
                then().
                spec(getResponseSpec()).
                extract().
                response();
    }

    public static Response update(String token, String path, Object request) {
        return given(getRequestSpec()).
                auth().oauth2(token).
                body(request).
                when().
                put(path).
                then().
                spec(getResponseSpec()).
                extract().
                response();
    }
}
