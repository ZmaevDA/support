package ru.zmaev.container;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;


class UserControllerManualTest extends KeycloakTestContainers {

    @Test
    void givenAuthenticatedUser_whenGetMe_shouldReturnMyInfo() {

        given().header("Authorization", getJaneDoeBearer())
                .when()
                .get("api/user/me/test")
                .then()
                .body("username", equalTo("janedoe"))
                .body("lastname", equalTo("Doe"))
                .body("firstname", equalTo("Jane"))
                .body("email", equalTo("jane.doe@baeldung.com"));
    }
}
