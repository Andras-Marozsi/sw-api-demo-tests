package helpers.endpoints;

import io.restassured.response.Response;
import org.hamcrest.Matcher;

import java.util.*;
import java.util.logging.Logger;

public abstract class BaseEndpoint {
    private static final Logger LOGGER = Logger.getLogger(BaseEndpoint.class.getName());
    public String url;
    protected HashMap<String, Matcher> schema;

    public void verifyResponseSchema(Response apiResp) {
        LOGGER.info("Checking schema for response");
        for (String key : this.schema.keySet()) {
            LOGGER.info("Checking '" + key + "' attribute");
            apiResp.then().body(key, this.schema.get(key));
        }
        LOGGER.info("Checked all schema attributes for response");
    }
}
