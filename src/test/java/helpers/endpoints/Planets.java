package helpers.endpoints;

import helpers.Constants;
import helpers.URLS;
import org.hamcrest.Matcher;

import java.util.ArrayList;
import java.util.HashMap;

import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.is;

public class Planets extends BaseEndpoint {
    public Planets(int planetID) {
        super();
        this.url = URLS.getPlanetURL(planetID);
        this.schema = new HashMap<String, Matcher>();

        this.schema.put(Constants.ATTR_NAME, any(String.class));
        this.schema.put(Constants.ATTR_ORBITAL_PERIOD, any(String.class));
        this.schema.put(Constants.ATTR_DIAMETER, any(String.class));
        this.schema.put(Constants.ATTR_CLIMATY, any(String.class));
        this.schema.put(Constants.ATTR_GRAVITY, any(String.class));
        this.schema.put(Constants.ATTR_TERRAIN, any(String.class));
        this.schema.put(Constants.ATTR_SURFACE_WATER, any(String.class));
        this.schema.put(Constants.ATTR_POPULATION, any(String.class));
        this.schema.put(Constants.ATTR_RESIDENTS, any(ArrayList.class));
        this.schema.put(Constants.ATTR_FILMS, any(ArrayList.class));
        this.schema.put(Constants.ATTR_CREATED, any(String.class));
        this.schema.put(Constants.ATTR_EDITED, any(String.class));
        this.schema.put(Constants.ATTR_URL, is(URLS.getPlanetURL(planetID) + "/"));
    }
}
