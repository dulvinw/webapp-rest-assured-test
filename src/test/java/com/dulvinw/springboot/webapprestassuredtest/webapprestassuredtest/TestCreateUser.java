package com.dulvinw.springboot.webapprestassuredtest.webapprestassuredtest;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TestCreateUser {

    private static final String CONTEXT_PATH = "/mobile-app-ws";

    private Map<String, Object> user;

    @BeforeEach
    void setup() throws Exception {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
        user = createUserObject();
    }

    @Test
    public void testCreateUser() {
        Response response = given().contentType("application/json")
                .accept("application/json")
                .body(user)
                .when()
                .post(CONTEXT_PATH + "/users")
                .then()
                .statusCode(200)
                .contentType("application/json")
                .extract()
                .response();

        String userId = response.jsonPath().getString("userId");
        assertNotNull(userId);

        String bodyString = response.body().asString();

        try {
            JSONObject jsonObject = new JSONObject(bodyString);
            JSONArray addresses = jsonObject.getJSONArray("addresses");

            assertNotNull(addresses);
            assertEquals(1, addresses.length());

            String address = addresses.getJSONObject(0).getString("addressId");

            assertNotNull(address);
            assertEquals(30, address.length());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private Map<String, Object> createUserObject() {
        List<Map<String, Object>> userAddresses = new ArrayList<>();

        Map<String, Object> homeAddress = new HashMap<>();
        homeAddress.put("city", "vancour");
        homeAddress.put("country", "Canada");
        homeAddress.put("streetName", "123 Street Name");
        homeAddress.put("postalCode", "ABCCBA");
        homeAddress.put("type", "home");

        userAddresses.add(homeAddress);

        Map<String, Object> user = new HashMap<>();
        user.put("firstName", "Dulvin");
        user.put("lastName", "Witharane");
        user.put("email", "dulvinw@gmail.com");
        user.put("password", "mit123");
        user.put("addresses", userAddresses);

        return user;
    }
}
