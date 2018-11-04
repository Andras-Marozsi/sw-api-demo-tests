package helpers;

public class URLS {
    public static String SWAPI_BASE = "https://swapi.co/api";
    public static String SWAPI_PLANETS = SWAPI_BASE + "/planets";

    public static String getPlanetURL(int planetID) {
        return SWAPI_PLANETS + "/" + planetID;
    }

    public static String getPlanetURL(String planetID) {
        return SWAPI_PLANETS + "/" + planetID;
    }
}
