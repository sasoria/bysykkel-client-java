package no.sasoria.løsning;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import java.nio.charset.StandardCharsets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class App {
    private static final String STATION_INFO_URL =
            "http://gbfs.urbansharing.com/oslobysykkel.no/station_information.json";
    private static final String STATION_STATUS_URL =
            "http://gbfs.urbansharing.com/oslobysykkel.no/station_status.json";
    private List<Station> stations;

    public static void main(String[] args) throws Exception {
        App app = new App();
        app.init();
        app.exec();
    }

    private void init() {
        stations = new ArrayList<Station>();
    }

    private void exec() throws Exception {
        HttpEntity infoEntity = getStationsEntity(STATION_INFO_URL);
        HttpEntity statusEntity = getStationsEntity(STATION_STATUS_URL);

        parseStationsInformation(infoEntity);
        parseStationsStatus(statusEntity);

        stations.forEach(System.out::println);
    }

    /**
     * Performs get requests to oslobysykkel.no, parses the response and adds {@code Station} instances to stations.
     * @param url
     * @return
     * @throws IOException
     */
    private HttpEntity getStationsEntity(String url) throws Exception, IOException {
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(url);

        HttpResponse response = client.execute(request);
        HttpEntity entity = response.getEntity();

        if(is200(response))
            return entity;

        else
            throw new Exception("Get request failed");
    }

    /**
     * Checks if a http request was successful.
     * @param response from http request
     * @return true if the request was successful.
     */
    private boolean is200(HttpResponse response) {
        return response.getStatusLine().getStatusCode() == 200;
    }

    /**
     * Parses an HttpEntity for StationInformation to a json object.
     * @param entity
     * @return
     */
    private void parseStationsInformation(HttpEntity entity) throws IOException {
        String json = EntityUtils.toString(entity, StandardCharsets.UTF_8);
        JSONObject jsonObject = new JSONObject(json);
        JSONArray stationsInfo = jsonObject.getJSONObject("data").getJSONArray("stations");

        for (int i = 0; i < stationsInfo.length(); i++) {
            JSONObject station = stationsInfo.getJSONObject(i);
            String staionName = station.getString("name");
            int stationId = station.getInt("station_id");

            stations.add(new Station(stationId, staionName));
        }
    }

    /**
     * Parses an HttpEntity for StationInformation to a json object.
     * @param entity
     * @return
     */
    private void parseStationsStatus(HttpEntity entity) throws IOException {
        String json = EntityUtils.toString(entity, StandardCharsets.UTF_8);
        JSONObject jsonObject = new JSONObject(json);
        JSONArray stationsStatusArray = jsonObject.getJSONObject("data").getJSONArray("stations");

        for (int i = 0; i < stationsStatusArray.length(); i++) {
            JSONObject stationJson = stationsStatusArray.getJSONObject(i);

            int bikesAvailable = stationJson.getInt("num_bikes_available");
            int locksAvailable = stationJson.getInt("num_docks_available");
            int stationId = stationJson.getInt("station_id");

            for(Station station : stations ){
                if(station.getId() == stationId) {
                    station.setBikesAvailable(bikesAvailable);
                    station.setLocksAvailable(locksAvailable);
                    break;
                }
            }
        }
    }
}
