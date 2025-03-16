package org.example.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.handler.codec.http.HttpHeaders;
import org.asynchttpclient.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import org.example.Model.Player;

public class ApiService {
    private static final String BASE_URL = "https://api-football-v1.p.rapidapi.com/v3/";
    private static final String API_KEY = "-------YourApiKey-------";
    private static final String API_HOST = "api-football-v1.p.rapidapi.com";
    private static final int PREMIER_LEAGUE_ID = 39;
    private static final int SEASON = 2023;

    /**
     * Premier Lig'deki takımları çeker ve takım ID'lerini döndürür.
     */
    public List<Integer> getPremierLeagueTeams() {
        List<Integer> teamIds = new ArrayList<>();
        String url = BASE_URL + "teams?league=" + PREMIER_LEAGUE_ID + "&season=" + SEASON;

        try (AsyncHttpClient client = new DefaultAsyncHttpClient()) {
            ListenableFuture<Response> responseFuture = client.prepareGet(url)
                    .setHeader("x-rapidapi-key", API_KEY)
                    .setHeader("x-rapidapi-host", API_HOST)
                    .execute();

            Response response = responseFuture.get();

            if (response.getStatusCode() != 200) {
                System.err.println("Takım verisi alınamadı! Hata kodu: " + response.getStatusCode());
                return teamIds;
            }

            String responseBody = response.getResponseBody();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(responseBody);
            JsonNode teamsArray = rootNode.path("response");

            for (JsonNode node : teamsArray) {
                JsonNode teamNode = node.path("team");
                int teamId = teamNode.path("id").asInt();
                teamIds.add(teamId);
            }
        } catch (Exception e) {
            System.err.println("Takım ID'leri alınırken hata oluştu: " + e.getMessage());
        }

        return teamIds;
    }

    /**
     * Belirtilen takım ID'sine ait oyuncuları çeker ve döndürür.
     */
    public List<Player> getPlayers(int teamId) {
        List<Player> players = new ArrayList<>();
        String url = BASE_URL + "players?team=" + teamId + "&season=" + SEASON;

        try (AsyncHttpClient client = new DefaultAsyncHttpClient()) {
            ListenableFuture<Response> responseFuture = client.prepareGet(url)
                    .setHeader("x-rapidapi-key", API_KEY)
                    .setHeader("x-rapidapi-host", API_HOST)
                    .execute();

            Response response = responseFuture.get();

            if (response.getStatusCode() != 200) {
                System.err.println("Oyuncu verisi alınamadı! Hata kodu: " + response.getStatusCode());
                return players;
            }

            String responseBody = response.getResponseBody();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(responseBody);
            JsonNode playersArray = rootNode.path("response");

            for (JsonNode node : playersArray) {
                JsonNode playerNode = node.path("player");

                int id = playerNode.path("id").asInt();
                String name = playerNode.path("name").asText("Bilinmiyor");
                String position = "Belirtilmemiş";
                int age = playerNode.path("age").asInt(0);
                String nationality = playerNode.path("nationality").asText("Bilinmiyor");
                String photo = playerNode.path("photo").asText("Bilinmiyor");

                // Pozisyon bilgisi için "statistics" kontrol ediliyor
                JsonNode statistics = node.path("statistics");
                if (statistics.isArray() && statistics.size() > 0) {
                    JsonNode firstStat = statistics.get(0);
                    JsonNode gamesNode = firstStat.path("games");
                    position = gamesNode.path("position").asText("Bilinmiyor");
                }

                // Yaş 0 ise -1 olarak işaretleniyor
                if (age == 0) {
                    age = -1;
                }

                players.add(new Player(id, name, position, age, nationality, photo));
            }
        } catch (Exception e) {
            System.err.println("Oyuncular alınırken hata oluştu: " + e.getMessage());
        }

        return players;
    }
}
