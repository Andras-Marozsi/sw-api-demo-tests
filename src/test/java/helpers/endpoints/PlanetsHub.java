package helpers.endpoints;

import helpers.Constants;
import helpers.URLS;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

import java.util.ArrayList;
import java.util.HashMap;

import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

public class PlanetsHub extends BaseEndpoint {

    public PlanetsHub() {
        super();
        this.url = URLS.SWAPI_PLANETS;
        this.schema = new HashMap<String, Matcher>();
        this.schema.put(Constants.ATTR_COUNT, Matchers.any(Integer.class));
        this.schema.put(Constants.ATTR_NEXT, is(URLS.SWAPI_PLANETS + "/?page=2"));
        this.schema.put(Constants.ATTR_PREV, is(nullValue()));
        this.schema.put(Constants.ATTR_RESULTS, any(ArrayList.class));
    }
}
