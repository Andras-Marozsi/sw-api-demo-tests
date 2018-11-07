package tests;

import helpers.Constants;
import helpers.URLS;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.*;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static io.restassured.path.json.JsonPath.from;
import static org.hamcrest.Matchers.*;

public class PlanetsHubTests {
    private List collectedPlanetResults = new ArrayList();

    @Test(priority = 1)
    public void verify_planets_hub_endpoint_smoke() {
        given().when().get(URLS.SWAPI_PLANETS).then()
                .statusCode(200)
                .body(Constants.ATTR_COUNT, any(Integer.class),
                        Constants.ATTR_NEXT, is(URLS.SWAPI_PLANETS + "/?page=2"),
                        Constants.ATTR_PREV, is(nullValue()),
                        Constants.ATTR_RESULTS, any(ArrayList.class));
    }

    @Test(dependsOnMethods = {"verify_planets_hub_endpoint_smoke"}, priority = 2)
    public void verify_planets_hub_endpoint_pagination() {
        int maxIterations = 10;
        int actualIteration = 0;
        int countFromResponse;
        String response;
        String prev;
        String next;
        List resultList;

        response = get(URLS.SWAPI_PLANETS).asString();
        countFromResponse = from(response).getInt(Constants.ATTR_COUNT);
        resultList = from(response).getList(Constants.ATTR_RESULTS);
        prev = from(response).getString(Constants.ATTR_PREV);
        next = from(response).getString(Constants.ATTR_NEXT);

        collectedPlanetResults.addAll(resultList);

        Assert.assertNull(prev, "Incorrect 'prev' value.");

        while (next != null && actualIteration < maxIterations) {
            response = get(next).asString();
            resultList = from(response).getList(Constants.ATTR_RESULTS);
            next = from(response).getString(Constants.ATTR_NEXT);
            prev = from(response).getString(Constants.ATTR_PREV);
            collectedPlanetResults.addAll(resultList);
            actualIteration++;
        }
        Assert.assertNotEquals(actualIteration, maxIterations, "Exceeded maximum retries");
        Assert.assertEquals(countFromResponse, collectedPlanetResults.size(), "'count' is incorrect in API");
        Assert.assertNotNull(prev, "Incorrect 'prev' value.");
    }

    @Test(dependsOnMethods = {"verify_planets_hub_endpoint_pagination"}, priority = 2)
    public void verify_planets_endpoint_unique_results() {
        Set<String> uniqueUrls = new HashSet<String>();
        for (Object planet : collectedPlanetResults) {
            uniqueUrls.add(((HashMap) planet).get("url").toString());
        }

        Assert.assertEquals(uniqueUrls.size(), collectedPlanetResults.size(), "Not all result urls are unique");
    }
}
