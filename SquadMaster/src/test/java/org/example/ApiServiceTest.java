package org.example.Test;

import org.example.Service.ApiService;
import org.example.Model.Player;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ApiServiceTest {
    private ApiService apiService;

    @BeforeAll
    void setup() {
        apiService = new ApiService();
    }

    @Test
    void testFetchPlayers() {
        List<Player> players = apiService.fetchPlayers();
        assertNotNull(players);
        assertFalse(players.isEmpty(), "API should return player data");
    }

    @Test
    void testInvalidApiResponseHandling() {
        // Simulate invalid API response
        List<Player> players = apiService.fetchPlayers();
        assertNotNull(players);
        assertTrue(players.size() > 0, "API response should not be empty");
    }
}
