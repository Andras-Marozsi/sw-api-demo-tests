package tests;

import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;

public class ExampleTests {

    @Test(priority = 1)
    public void planets_returns_200() {
        given().when().get("http://swapi.co/api/planets").then().statusCode(200);
    }

    @Test(priority = 1)
    public void planet_returns_200() {
        given().when().get("http://swapi.co/api/planets/1").then().statusCode(200);
    }
}
