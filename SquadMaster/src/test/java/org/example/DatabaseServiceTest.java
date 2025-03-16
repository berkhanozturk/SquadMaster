package org.example.Test;

import org.example.Model.Player;
import org.example.Service.DatabaseService;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DatabaseServiceTest {
    private DatabaseService databaseService;

    @BeforeAll
    void setup() {
        databaseService = new DatabaseService();
    }

    @Test
    void testInsertAndRetrievePlayer() {
        Player player = new Player(1, "Lionel Messi", "Forward", 36, "photo_url");
        databaseService.insertPlayer(player);

        List<Player> players = databaseService.getAllPlayers();
        assertFalse(players.isEmpty());
        assertEquals("Lionel Messi", players.get(0).getName());
    }

    @Test
    void testPreventDuplicateInsertion() {
        Player player = new Player(2, "Cristiano Ronaldo", "Forward", 39, "photo_url");
        databaseService.insertPlayer(player);
        databaseService.insertPlayer(player);  // Trying to insert duplicate

        List<Player> players = databaseService.getAllPlayers();
        long count = players.stream().filter(p -> p.getName().equals("Cristiano Ronaldo")).count();
        assertEquals(1, count, "Duplicate player inserted!");
    }
}
