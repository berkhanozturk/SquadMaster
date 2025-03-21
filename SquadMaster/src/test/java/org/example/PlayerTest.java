import org.example.Model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {

    private Player player;

    @BeforeEach
    public void setUp() {
        player = new Player(10, "Harry Kane", "Forward", 30, "England", "https://photo.com/kane.jpg");
    }

    @Test
    public void testGetters() {
        assertEquals(10, player.getId());
        assertEquals("Harry Kane", player.getName());
        assertEquals("Forward", player.getPosition());
        assertEquals(30, player.getAge());
        assertEquals("England", player.getNationality());
        assertEquals("https://photo.com/kane.jpg", player.getPhoto());
    }

    @Test
    public void testSetters() {
        player.setId(9);
        player.setName("Bukayo Saka");
        player.setPosition("Midfielder");
        player.setAge(22);
        player.setNationality("England");
        player.setPhoto("https://photo.com/saka.jpg");

        assertEquals(9, player.getId());
        assertEquals("Bukayo Saka", player.getName());
        assertEquals("Midfielder", player.getPosition());
        assertEquals(22, player.getAge());
        assertEquals("England", player.getNationality());
        assertEquals("https://photo.com/saka.jpg", player.getPhoto());
    }

    @Test
    public void testToString() {
        String expected = "Harry Kane - Forward (England), Yaş: 30, Fotoğraf: https://photo.com/kane.jpg";
        assertEquals(expected, player.toString());
    }
}
