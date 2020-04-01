package com.dulvinw.springboot.webapprestassuredtest.webapprestassuredtest;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EndpointTest {

    private static final String CONTEXT_PATH = "/mobile-app-ws";

    private static String authorizationHeader;

    private static String userId;

    private final String APPLICATION_JSON = "application/json";

    private final String LOGIN_URL = "/users/login";

    @BeforeEach
    void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
    }

    @Test
    @Order(1)
    public void testUserLogin() {
        Map<String, Object> userDetails = new HashMap<>();
        userDetails.put("email", "dulvinw@gmail.com");
        userDetails.put("password", "mit123");

        Response response = given().contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .when()
                .body(userDetails)
                .post(CONTEXT_PATH + LOGIN_URL)
                .then()
                .statusCode(200)
                .extract()
                .response();

        authorizationHeader = response.header("Authorization");
        userId = response.header("userId");

        assertNotNull(authorizationHeader);
        assertNotNull(userId);
    }

    @Test
    @Order(2)
    public void testGetUser() {
        Response response = given().contentType(APPLICATION_JSON)
                .pathParam("id", userId)
                .accept(APPLICATION_JSON)
                .header("Authorization", authorizationHeader)
                .get(CONTEXT_PATH + "/users/{id}")
                .then()
                .contentType(APPLICATION_JSON)
                .statusCode(200)
                .extract()
                .response();

        String body = response.body().asString();

        try {
            JSONObject jsonObject = new JSONObject(body);
            String userIdFromObject = jsonObject.getString("userId");
            String email = jsonObject.getString("email");
            JSONArray addresses = jsonObject.getJSONArray("addresses");

            assertNotNull(userIdFromObject);
            assertNotNull(email);
            assertNotNull(addresses);
            assertEquals(1, addresses.length());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
