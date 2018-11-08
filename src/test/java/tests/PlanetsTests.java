package tests;

import helpers.endpoints.Planets;
import io.restassured.response.Response;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import helpers.URLS;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class PlanetsTests {

    private Planets planet;

    @Parameters({"planetIDSmoke"})
    @Test(priority = 1)
    public void verify_planets_endpoint_with_id_smoke(int planetID) {
        planet = new Planets(planetID);
        Response apiResp = given().when().get(planet.url);
        apiResp.then().statusCode(200);
        planet.verifyResponseSchema(apiResp);
    }

    @Test(priority = 1)
    public void verify_planets_with_invalid_id_returns_404() {
        given().when().get(URLS.getPlanetURL("invalid")).then()
                .statusCode(404);
    }

    @Test(priority = 1)
    public void verify_planets_with_negative_id_returns_404() {
        given().when().get(URLS.getPlanetURL(-1)).then()
                .statusCode(404);
    }

    @Test(priority = 2)
    public void verify_planets_endpoint_ignores_starting_zeroes_in_id() {
        String idWithZeros = "00001";
        int normalizedID = 1;
        given().when().get(URLS.getPlanetURL(idWithZeros)).then()
                .statusCode(200)
                .body("url", is(URLS.getPlanetURL(normalizedID) + "/"));
    }

}
