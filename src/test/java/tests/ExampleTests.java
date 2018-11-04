package tests;

import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import helpers.URLS;

import static io.restassured.RestAssured.*;

public class ExampleTests {

    @Test(priority = 1)
    public void planets_returns_200() {
        given().when().get(URLS.SWAPI_PLANETS).then().statusCode(200);
    }

    @Parameters({"planetID"})
    @Test(priority = 1)
    public void planets_with_id_returns_200(int planetID) {
        given().when().get(URLS.getPlanetURL(planetID)).then().statusCode(200);
    }
}
