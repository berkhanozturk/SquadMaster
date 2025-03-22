package org.example.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.asynchttpclient.*;
import org.example.Model.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ApiService {
    private static final String BASE_URL = "https://api-football-v1.p.rapidapi.com/v3/";
    private static final String API_KEY = "----------YourApiKey----------";
    private static final String API_HOST = "api-football-v1.p.rapidapi.com";
    private static final int PREMIER_LEAGUE_ID = 39;
    private static final int SEASON = 2023;

    //  Oyuncuların bellekte tutulacağı liste
    private static List<Player> cachedPlayers = new ArrayList<>();
    private static boolean playersLoaded = false; // Oyuncuların yüklenip yüklenmediğini kontrol eder

    /**
     * API'den tüm oyuncuları bir kez çekip bellekte saklar.
     */
    public static void initializePlayers() {
        if (playersLoaded) return; // Daha önce yüklendiyse tekrar yükleme!

        List<Player> allPlayers = new ArrayList<>();
        try (AsyncHttpClient client = new DefaultAsyncHttpClient()) {
            List<Integer> teamIds = getPremierLeagueTeams(); // Takım ID'lerini al
            for (int teamId : teamIds) {
                allPlayers.addAll(getPlayers(teamId)); // Takımın oyuncularını çek
            }
        } catch (Exception e) {
            System.err.println("Oyuncular yüklenirken hata oluştu: " + e.getMessage());
        }

        cachedPlayers = allPlayers;
        playersLoaded = true;
    }

    /**
     * Bellekteki oyuncuları döndürür.
     */
    public static List<Player> getCachedPlayers() {
        return cachedPlayers;
    }

    /**
     * Premier Lig'deki takımların ID'lerini çeker.
     */
    public static List<Integer> getPremierLeagueTeams() {
        List<Integer> teamIds = new ArrayList<>();
        String url = BASE_URL + "teams?league=" + PREMIER_LEAGUE_ID + "&season=" + SEASON;

        try (AsyncHttpClient client = new DefaultAsyncHttpClient()) {
            Response response = client.prepareGet(url)
                    .setHeader("x-rapidapi-key", API_KEY)
                    .setHeader("x-rapidapi-host", API_HOST)
                    .execute()
                    .get();

            if (response.getStatusCode() != 200) {
                System.err.println("Takım verisi alınamadı! Hata kodu: " + response.getStatusCode());
                return teamIds;
            }

            String responseBody = response.getResponseBody();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(responseBody);
            JsonNode teamsArray = rootNode.path("response");

            for (JsonNode node : teamsArray) {
                int teamId = node.path("team").path("id").asInt();
                teamIds.add(teamId);
            }
        } catch (Exception e) {
            System.err.println("Takım ID'leri alınırken hata oluştu: " + e.getMessage());
        }

        return teamIds;
    }

    /**
     * Belirtilen takım ID'sine ait oyuncuları çeker.
     */
    public static List<Player> getPlayers(int teamId) {
        List<Player> players = new ArrayList<>();
        String url = BASE_URL + "players?team=" + teamId + "&season=" + SEASON;

        try (AsyncHttpClient client = new DefaultAsyncHttpClient()) {
            Response response = client.prepareGet(url)
                    .setHeader("x-rapidapi-key", API_KEY)
                    .setHeader("x-rapidapi-host", API_HOST)
                    .execute()
                    .get();

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
                String position = "Bilinmiyor";
                int age = playerNode.path("age").asInt(0);
                String nationality = playerNode.path("nationality").asText("Bilinmiyor");
                String photo = playerNode.path("photo").asText("Bilinmiyor");

                // Pozisyon bilgisi için "statistics" düğümünü kontrol et
                JsonNode statistics = node.path("statistics");
                if (statistics.isArray() && statistics.size() > 0) {
                    JsonNode firstStat = statistics.get(0);
                    position = firstStat.path("games").path("position").asText("Bilinmiyor");
                }

                // Yaş bilgisini kontrol et
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
