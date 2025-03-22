package org.example.View;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import org.example.Extensions.PlayerButton;
import org.example.Model.Player;

public class FormationUI {
    private static final Map<String, int[][]> FORMATIONS = new HashMap<>();

    static {
        FORMATIONS.put("4-4-2", new int[][]{
                {1, 3},       // 2 Forvet
                {1, 2, 3, 4}, // 4 Orta Saha
                {1, 2, 3, 4}, // 4 Defans
                {2}           // Kaleci
        });

        FORMATIONS.put("4-3-3", new int[][]{
                {1, 2, 3},    // 3 Forvet
                {1, 2, 3},    // 3 Orta Saha
                {1, 2, 3, 4}, // 4 Defans
                {2}           // Kaleci
        });

        FORMATIONS.put("3-5-2", new int[][]{
                {1, 3},       // 2 Forvet
                {1, 2, 3, 4, 5}, // 5 Orta Saha
                {1, 2, 3},    // 3 Defans
                {2}           // Kaleci
        });

        // 🔹 **Yeni Eklenen Formasyonlar**
        FORMATIONS.put("5-3-2", new int[][]{
                {1, 3},       // 2 Forvet
                {1, 2, 3},    // 3 Orta Saha
                {1, 2, 3, 4, 5}, // 5 Defans
                {2}           // Kaleci
        });

        FORMATIONS.put("5-2-3", new int[][]{
                {1, 2, 3},    // 3 Forvet
                {1, 2},       // 2 Orta Saha
                {1, 2, 3, 4, 5}, // 5 Defans
                {2}           // Kaleci
        });

        FORMATIONS.put("4-5-1", new int[][]{
                {2},          // 1 Forvet
                {1, 2, 3, 4, 5}, // 5 Orta Saha
                {1, 2, 3, 4}, // 4 Defans
                {2}           // Kaleci
        });
    }

    public static JPanel createFormationPanel(String formation) {
        JPanel fieldPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon fieldImage = new ImageIcon("src/main/resources/footballField.png");
                g.drawImage(fieldImage.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };

        fieldPanel.setLayout(new GridLayout(4, 1, 10, 10)); // Satır yapısı
        fieldPanel.setPreferredSize(new Dimension(900, 700));
        fieldPanel.setOpaque(false);

        if (!FORMATIONS.containsKey(formation)) return fieldPanel;

        int[][] positions = FORMATIONS.get(formation);

        for (int i = 0; i < positions.length; i++) {
            JPanel rowPanel = new JPanel();
            rowPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 80, 10)); // Butonların arası açıldı
            rowPanel.setOpaque(false);

            for (int j = 0; j < positions[i].length; j++) {
                PlayerButton playerButton = new PlayerButton(new Player(0, "seç","", 0, "", ""));
                rowPanel.add(playerButton);
            }

            fieldPanel.add(rowPanel);
        }

        return fieldPanel;
    }
}
