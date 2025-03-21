package org.example.Test;

import org.example.Model.Player;
import org.example.Utils.FilterUtils;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FilterUtilsTest {
    private List<Player> players;

    @BeforeAll
    void setup() {
        players = new ArrayList<>();
        players.add(new Player(1, "Lionel Messi", "Forward", 36, "photo_url"));
        players.add(new Player(2, "Cristiano Ronaldo", "Forward", 39, "photo_url"));
        players.add(new Player(3, "Kylian Mbappe", "Forward", 25, "photo_url"));
        players.add(new Player(4, "Kevin De Bruyne", "Midfielder", 32, "photo_url"));
    }

    @Test
    @Test
    public void testFilterByPosition() {
        List<Player> players = new ArrayList<>();
        players.add(new Player(1, "John", "Forward", 25, "England", ""));
        players.add(new Player(2, "Mike", "Midfielder", 28, "France", ""));
        
        List<Player> result = FilterUtils.filterByPosition(players, "Forward");
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
