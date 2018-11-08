package helpers.utils;
import io.restassured.response.Response;

import java.util.logging.Logger;

import static io.restassured.RestAssured.given;

public class Helper {
    private static final Logger LOGGER = Logger.getLogger(Helper.class.getName());

    public static Response getResponse (String url) {
        LOGGER.info("Loading url: " + url);
        return given().when().get(url);
    }
}
