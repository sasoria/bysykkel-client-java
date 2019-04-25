package no.sasoria.løsning;

import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Util class for JSON parsing.
 */
public class JsonParser {
    /**
     * Static method that parses a HttpEntity from a get request to a JSONArray.
     * @param entity entity from http GET request.
     * @return stations as a JSONArray
     * @throws IOException
     */
    public static JSONArray parseStations(HttpEntity entity) throws IOException {
        String json = EntityUtils.toString(entity, StandardCharsets.UTF_8);
        JSONObject jsonObject = new JSONObject(json);
        return jsonObject.getJSONObject("data").getJSONArray("stations");
    }
}
