package tests;

import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import helpers.URLS;

import java.util.*;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class PlanetsTests {

    @Parameters({"planetIDSmoke"})
    @Test(priority = 1)
    public void verify_planets_endpoint_with_id_smoke(int planetID) {
        given().when().get(URLS.getPlanetURL(planetID)).then()
                .statusCode(200)
                .body("name", any(String.class),
                        "orbital_period", any(String.class),
                        "diameter", any(String.class),
                        "climate", any(String.class),
                        "gravity", any(String.class),
                        "terrain", any(String.class),
                        "surface_water", any(String.class),
                        "population", any(String.class),
                        "residents", any(ArrayList.class),
                        "films", any(ArrayList.class),
                        "created", any(String.class),
                        "edited", any(String.class),
                        "url", is(URLS.getPlanetURL(planetID) + "/"));
    }

    @Test(priority = 1)
    public void verify_planets_with_invalid_id_returns_404() {
        given().when().get(URLS.getPlanetURL("invalid")).then()
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
