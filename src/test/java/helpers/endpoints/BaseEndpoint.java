package helpers.endpoints;

import io.restassured.response.Response;
import org.hamcrest.Matcher;

import java.util.*;

public class BaseEndpoint {
    public String url;
    protected HashMap<String, Matcher> schema;

    public void verifyResponseSchema(Response apiResp) {
        for (String key : this.schema.keySet()) {
            apiResp.then().body(key, this.schema.get(key));
        }
    }
}
