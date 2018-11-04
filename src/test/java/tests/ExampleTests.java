package tests;

import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import helpers.Constants;
import helpers.URLS;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.*;
import static io.restassured.path.json.JsonPath.from;
import static org.hamcrest.Matchers.*;

public class ExampleTests {

    @Test(priority = 1)
    public void planets_endpoint_smoke() {
        given().when().get(URLS.SWAPI_PLANETS).then()
                .statusCode(200)
                .body(Constants.ATTR_COUNT, any(Integer.class),
                        Constants.ATTR_NEXT, is(URLS.SWAPI_PLANETS + "/?page=2"),
                        Constants.ATTR_PREV, is(nullValue()),
                        Constants.ATTR_RESULTS, any(ArrayList.class));
    }

    @Parameters({"planetID"})
    @Test(priority = 1)
    public void planets_endpoint_with_id_smoke(int planetID) {
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
    public void planets_with_invalid_id_returns_404() {
        given().when().get(URLS.getPlanetURL("invalid")).then()
                .statusCode(404);
    }

    @Test(priority = 2)
    public void planets_endpoint_ignores_starting_zeroes_in_id() {
        String idWithZeros = "00001";
        int normalizedID = 1;
        given().when().get(URLS.getPlanetURL(idWithZeros)).then()
                .statusCode(200)
                .body("url", is(URLS.getPlanetURL(normalizedID) + "/"));
    }

    @Test(dependsOnMethods = {"planets_endpoint_smoke"}, priority = 2)
    public void planets_endpoint_pagination() {
        int maxIterations = 10;
        int actualIteration = 0;
        int countFromResponse;
        String response;
        String prev;
        String next;
        List resultList;
        List collectedResults = new ArrayList();

        response = get(URLS.SWAPI_PLANETS).asString();
        countFromResponse = from(response).getInt(Constants.ATTR_COUNT);
        resultList = from(response).getList(Constants.ATTR_RESULTS);
        prev = from(response).getString(Constants.ATTR_PREV);
        next = from(response).getString(Constants.ATTR_NEXT);

        collectedResults.addAll(resultList);

        Assert.assertNull(prev, "Incorrect 'prev' value.");

        while (next != null && actualIteration < maxIterations) {
            response = get(next).asString();
            resultList = from(response).getList(Constants.ATTR_RESULTS);
            next = from(response).getString(Constants.ATTR_NEXT);
            prev = from(response).getString(Constants.ATTR_PREV);
            collectedResults.addAll(resultList);
            actualIteration++;
        }
        Assert.assertNotEquals(actualIteration, maxIterations, "Exceeded maximum retries");
        Assert.assertEquals(countFromResponse, collectedResults.size(), "'count' is incorrect in API");
        Assert.assertNotNull(prev, "Incorrect 'prev' value.");
    }
}
