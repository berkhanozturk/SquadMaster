package org.example;

import java.util.ArrayList;
import java.util.List;

import org.example.Model.Player;
import org.example.Utils.FilterUtils;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FilterUtilsTest {
    private List<Player> players;

    @BeforeAll
    void setup() {
        players = new ArrayList<>();
    players.add(new Player(1, "Kylian Mbappe", "Forward", 24, "France", ""));
    }

    
    @Test
    public void testFilterByPosition() {
        List<Player> players = new ArrayList<>();
        players.add(new Player(1, "John", "Forward", 25, "England", ""));
        players.add(new Player(2, "Mike", "Midfielder", 28, "France", ""));
        
        List<Player> result = FilterUtils.filterByExactPosition(players, "Forvet");

        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getName());
    }

    @Test
    public void testFilterByAge() {
        List<Player> players = new ArrayList<>();
        players.add(new Player(1, "Ali", "Defender", 21, "Turkey", ""));
        players.add(new Player(2, "Veli", "Goalkeeper", 31, "Turkey", ""));
        
        List<Player> result = FilterUtils.filterByAge(players, 20, 30);
        assertEquals(1, result.size());
        assertEquals("Ali", result.get(0).getName());
    }

    @Test
    void testFilterByName() {
        List<Player> filtered = FilterUtils.filterByName(players, "Mbappe");
        assertEquals(1, filtered.size(), "Should return one player with name Mbappe");
    }
}
