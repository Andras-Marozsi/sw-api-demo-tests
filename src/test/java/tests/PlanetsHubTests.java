package tests;

import helpers.Constants;
import helpers.URLS;
import helpers.endpoints.PlanetsHub;
import helpers.utils.Helper;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.*;
import java.util.logging.Logger;

import static io.restassured.path.json.JsonPath.from;


public class PlanetsHubTests {
    private static final Logger LOGGER = Logger.getLogger(PlanetsHubTests.class.getName());
    private List collectedPlanetResults = new ArrayList();
    private PlanetsHub planetsHub = new PlanetsHub();


    @Test(priority = 1)
    public void verify_planets_hub_endpoint_smoke() {
        Response apiResp = Helper.getResponse(planetsHub.url);
        apiResp.then().statusCode(200);
        planetsHub.verifyResponseSchema(apiResp);
    }

    @Test(dependsOnMethods = {"verify_planets_hub_endpoint_smoke"}, priority = 2)
    public void verify_planets_hub_endpoint_pagination() {
        int maxIterations = 10;
        int actualIteration = 1;
        int countFromResponse;
        String response;
        String prev;
        String next;
        List resultList;

        response = Helper.getResponse(URLS.SWAPI_PLANETS).asString();
        countFromResponse = from(response).getInt(Constants.ATTR_COUNT);
        resultList = from(response).getList(Constants.ATTR_RESULTS);
        prev = from(response).getString(Constants.ATTR_PREV);
        next = from(response).getString(Constants.ATTR_NEXT);

        collectedPlanetResults.addAll(resultList);

        Assert.assertNull(prev, "Incorrect 'prev' value.");

        while (next != null && actualIteration < maxIterations) {
            LOGGER.info("Iteration: '" + actualIteration + "'");
            response = Helper.getResponse(next).asString();
            resultList = from(response).getList(Constants.ATTR_RESULTS);
            next = from(response).getString(Constants.ATTR_NEXT);
            prev = from(response).getString(Constants.ATTR_PREV);
            collectedPlanetResults.addAll(resultList);
            LOGGER.info("Collected results so far: '" + collectedPlanetResults.size() + "'");
            actualIteration++;
        }
        LOGGER.info("Finished collection, final result count: '" + collectedPlanetResults.size() + "'");
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

    @Test(priority = 2)
    public void verify_planets_hub_endpoint_pagination_invalid_page() {
        Response apiResp = Helper.getResponse(planetsHub.url + "?page=99");
        apiResp.then().statusCode(404);
    }

    @Test(priority = 2)
    public void verify_planets_hub_endpoint_pagination_invalid_negative_page() {
        Response apiResp = Helper.getResponse(planetsHub.url + "?page=-1");
        apiResp.then().statusCode(404);
    }

    @Test(priority = 2)
    public void verify_planets_hub_endpoint_pagination_invalid_string_page() {
        Response apiResp = Helper.getResponse(planetsHub.url + "?page=abc");
        apiResp.then().statusCode(404);
    }
}
