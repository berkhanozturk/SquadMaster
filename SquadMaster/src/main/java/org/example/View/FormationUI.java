package org.example.View;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.example.Extensions.PlayerButton;
import org.example.Model.Player;

public class FormationUI {
    private static final Map<String, int[][]> FORMATIONS = new HashMap<>();
    private static final List<PlayerButton> currentButtons = new ArrayList<>();

    static {
        FORMATIONS.put("4-4-2", new int[][]{{1, 3}, {1, 2, 3, 4}, {1, 2, 3, 4}, {2}});
        FORMATIONS.put("4-3-3", new int[][]{{1, 2, 3}, {1, 2, 3}, {1, 2, 3, 4}, {2}});
        FORMATIONS.put("3-5-2", new int[][]{{1, 3}, {1, 2, 3, 4, 5}, {1, 2, 3}, {2}});
        FORMATIONS.put("5-3-2", new int[][]{{1, 3}, {1, 2, 3}, {1, 2, 3, 4, 5}, {2}});
        FORMATIONS.put("5-2-3", new int[][]{{1, 2, 3}, {1, 2}, {1, 2, 3, 4, 5}, {2}});
        FORMATIONS.put("4-5-1", new int[][]{{2}, {1, 2, 3, 4, 5}, {1, 2, 3, 4}, {2}});
    }

    public static JPanel createFormationPanel(String formation) {
        return createFormationPanel(formation, null);
    }

    public static JPanel createFormationPanel(String formation, List<Player> prefillPlayers) {
        currentButtons.clear();
        int playerIndex = 0;

        JPanel fieldPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon fieldImage = new ImageIcon("src/main/resources/footballField.png");
                g.drawImage(fieldImage.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };

        fieldPanel.setLayout(new GridLayout(4, 1, 10, 10));
        fieldPanel.setPreferredSize(new Dimension(900, 700));
        fieldPanel.setOpaque(false);

        if (!FORMATIONS.containsKey(formation)) return fieldPanel;

        int[][] positions = FORMATIONS.get(formation);

        for (int i = 0; i < positions.length; i++) {
            JPanel rowPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 80, 10));
            rowPanel.setOpaque(false);

            for (int j = 0; j < positions[i].length; j++) {
                Player player = (prefillPlayers != null && playerIndex < prefillPlayers.size())
                        ? prefillPlayers.get(playerIndex++)
                        : new Player(0, "seç", "", 0, "", "");
                PlayerButton button = new PlayerButton(player);
                currentButtons.add(button);
                rowPanel.add(button);
            }

            fieldPanel.add(rowPanel);
        }

        return fieldPanel;
    }

    public static List<Player> getCurrentPlayers() {
        List<Player> players = new ArrayList<>();
        for (PlayerButton btn : currentButtons) {
            players.add(btn.getPlayer());
        }
        return players;
    }




}
