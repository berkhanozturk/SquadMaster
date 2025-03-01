package org.example.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.handler.codec.http.HttpHeaders;
import org.asynchttpclient.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.example.Model.Player;

public class ApiService {
    private static final String API_URL = "https://api-football-v1.p.rapidapi.com/v3/players/profiles";
    private static final String API_KEY = "0338def286msh23678340aad1fd0p1d252fjsnbdca1280ab59";
    private static final String API_HOST = "api-football-v1.p.rapidapi.com";

    public List<Player> fetchPlayers() {
        List<Player> players = new ArrayList<>();

        try (AsyncHttpClient client = new DefaultAsyncHttpClient()) {
            ListenableFuture<Response> responseFuture = client.prepareGet(API_URL)
                    .setHeader("x-rapidapi-key", API_KEY)
                    .setHeader("x-rapidapi-host", API_HOST)
                    .execute();

            Response response = responseFuture.get();
            String responseBody = response.getResponseBody();

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(responseBody);
            JsonNode playersArray = rootNode.path("response");

            for (JsonNode node : playersArray) {
                int id = node.path("player").path("id").asInt();
                String name = node.path("player").path("name").asText();
                String position = node.path("player").path("position").asText();
                int age = node.path("player").path("age").asInt();
                String photo = node.path("player").path("photo").asText();

                players.add(new Player(id, name, position, age, photo));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return players;
    }
}
