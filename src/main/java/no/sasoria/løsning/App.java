package no.sasoria.løsning;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class App {
    private static final String STATION_INFO_URL =
            "http://gbfs.urbansharing.com/oslobysykkel.no/station_information.json";
    private static final String STATION_STATUS_URL =
            "http://gbfs.urbansharing.com/oslobysykkel.no/station_status.json";
    private List<Station> stations = new ArrayList<>();

    public static void main(String[] args) {
        App app = new App();

        try {
            app.exec();
        } catch (ClientProtocolException exception) {
            System.out.println(exception.getMessage());
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
    }

    private void exec() throws IOException {
        JSONArray stationsInfoArray = getStationsArray(STATION_INFO_URL);
        JSONArray stationsStatusArray = getStationsArray(STATION_STATUS_URL);

        createStations(stationsInfoArray, stationsStatusArray);
        stations.forEach(System.out::println);
    }

    /**
     * Performs get requests to oslobysykkel.no, parses the response and adds {@code Station} instances to stations.
     * @param url
     * @return
     * @throws IOException
     */
    private JSONArray getStationsArray(String url) throws IOException {
        HttpClient client = HttpClientBuilder.create().build();

        HttpGet request = new HttpGet(url);
        request.addHeader("client-name", "sasoria-origosolution");

        HttpResponse response = client.execute(request);
        HttpEntity entity = response.getEntity();

        if(is200(response))
            return JsonParser.parseStations(entity);

        else
            throw new ClientProtocolException("GET failed, statusCode=" + response.getStatusLine().getStatusCode());
    }

    /**
     * Checks if a http request was successful.
     * @param response from http request
     * @return true if the request was successful.
     */
    private boolean is200(HttpResponse response) {
        return response.getStatusLine().getStatusCode() == 200;
    }

    private Station createStation(JSONObject stationInfo, JSONObject stationStatus) {
        String stationName = stationInfo.getString("name");
        int bikesAvailable = stationStatus.getInt("num_bikes_available");
        int locksAvailable = stationStatus.getInt("num_docks_available");

        return new Station(stationName, bikesAvailable, locksAvailable);

    }

    private void createStations(JSONArray stationsInfoArray, JSONArray stationsStatusArray) {
        // Stations in station_info.json
        for (int i = 0; i < stationsInfoArray.length(); i++) {
            JSONObject stationInfoJson = stationsInfoArray.getJSONObject(i);
            int stationId = stationInfoJson.getInt("station_id");

            // Stations in station_status.json
            for (int k = 0; k < stationsStatusArray.length(); k++) {
                JSONObject stationStatusJson = stationsStatusArray.getJSONObject(k);
                if (stationId == stationStatusJson.getInt("station_id")) {
                    stations.add(createStation(stationInfoJson, stationStatusJson));
                    break;
                }
            }
        }
    }
}
