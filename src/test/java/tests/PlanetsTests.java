package tests;

import helpers.endpoints.Planets;
import helpers.utils.Helper;
import io.restassured.response.Response;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import helpers.URLS;

import static org.hamcrest.Matchers.*;

public class PlanetsTests {

    private Planets planet;

    @Parameters({"planetIDSmoke"})
    @Test(priority = 1)
    public void verify_planets_endpoint_with_id_smoke(int planetID) {
        planet = new Planets(planetID);
        Response apiResp = Helper.getResponse(planet.url);
        apiResp.then().statusCode(200);
        planet.verifyResponseSchema(apiResp);
    }

    @Test(priority = 1)
    public void verify_planets_with_invalid_string_id_returns_404() {
        Helper.getResponse(URLS.getPlanetURL("invalid")).then()
                .statusCode(404);
    }

    @Test(priority = 1)
    public void verify_planets_with_invalid_negative_id_returns_404() {
        Helper.getResponse(URLS.getPlanetURL(-1)).then()
                .statusCode(404);
    }

    @Test(priority = 1)
    public void verify_planets_with_invalid_int_id_returns_404() {
        Helper.getResponse(URLS.getPlanetURL(99)).then()
                .statusCode(404);
    }

    @Test(priority = 2)
    public void verify_planets_endpoint_ignores_starting_zeroes_in_id() {
        String idWithZeros = "00001";
        int normalizedID = 1;
        Helper.getResponse(URLS.getPlanetURL(idWithZeros)).then()
                .statusCode(200)
                .body("url", is(URLS.getPlanetURL(normalizedID) + "/"));
    }

}
