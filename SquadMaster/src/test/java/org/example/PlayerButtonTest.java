package org.example;
import org.example.Extensions.PlayerButton;
import org.example.Model.Player;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PlayerButtonTest {

    private Player player;
    private PlayerButton button;

    @BeforeEach
    void setUp() {
        player = new Player(9, "Karim Benzema", "Forward", 36, "France", "");
        button = new PlayerButton(player);
    }

    @Test
    void testTextContainsPlayerInfo() {
        String text = button.getText();
        assertTrue(text.contains("Karim"), "Buton ismi oyuncu adını içermeli.");
        assertTrue(text.contains("Forward"), "Buton metni pozisyon içermeli.");
    }

    @Test
    void testPreferredSize() {
        assertEquals(120, button.getPreferredSize().width);
        assertEquals(140, button.getPreferredSize().height);
    }

    @Test
    void testIconSafety() {
        assertDoesNotThrow(() -> button.setIcon(null), "Icon null olarak ayarlanmalı ve hata vermemeli.");
    }

    @Test
    void testPlayerUpdate() {
        Player newPlayer = new Player(10, "Modric", "Midfielder", 38, "Croatia", "");
        PlayerButton newButton = new PlayerButton(newPlayer);

        assertTrue(newButton.getText().contains("Modric"));
        assertTrue(newButton.getText().contains("Midfielder"));
    }
}
