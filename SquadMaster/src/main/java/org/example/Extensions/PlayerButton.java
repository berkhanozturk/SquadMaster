package org.example.Extensions;

import org.example.Model.Player;
import org.example.Service.ApiService;
import org.example.View.PlayerSelectionDialogUI;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class PlayerButton extends JButton {
    private Player player;
    private JLabel positionLabel;
    private JLabel ageLabel;

    public PlayerButton(Player player) {
        this.player = player;
        setupButtonUI();
        addActionListener(e -> openPlayerSelection());
    }

    private void setupButtonUI() {
        // Önce butondaki her şeyi temizle, aksi halde üst üste ekleme yapar!
        removeAll();
        setLayout(new BorderLayout());

        setPreferredSize(new Dimension(120, 140)); // Butonu büyüttük
        setBackground(Color.WHITE);

        // Oyuncu adı ve pozisyon
        setText("<html><center>" + player.getName() + "<br>" + player.getPosition() + "</center></html>");
        setHorizontalTextPosition(SwingConstants.CENTER);
        setVerticalTextPosition(SwingConstants.BOTTOM);

        // **Sağ üstte mevki**
        positionLabel = new JLabel(player.getPosition());
        positionLabel.setFont(new Font("Arial", Font.BOLD, 10));
        positionLabel.setForeground(Color.BLUE);
        positionLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        add(positionLabel, BorderLayout.NORTH);

        // **Sağ altta yaş**
        ageLabel = new JLabel("Yaş: " + (player.getAge() > 0 ? player.getAge() : "?"));
        ageLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        ageLabel.setForeground(Color.RED);
        ageLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        add(ageLabel, BorderLayout.SOUTH);

        // **Fotoğrafı güncelle**
        updatePlayerImage(player.getPhoto());

        // UI'yi yenile
        revalidate();
        repaint();
    }

    private void openPlayerSelection() {
        ApiService apiService = new ApiService();
        PlayerSelectionDialogUI selectionUI = new PlayerSelectionDialogUI(null, apiService);
        selectionUI.setVisible(true);

        Player selectedPlayer = selectionUI.getSelectedPlayer();
        if (selectedPlayer != null) {
            this.player = selectedPlayer;
            setupButtonUI(); // **Seçim sonrası UI'yi güncelle**
        }
    }

    private void updatePlayerImage(String imageUrl) {
        try {
            if (imageUrl != null && !imageUrl.isBlank()) {
                URL url = new URL(imageUrl);
                ImageIcon playerIcon = new ImageIcon(url);
                Image img = playerIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                setIcon(new ImageIcon(img));
            } else {
                setIcon(null);
            }
        } catch (Exception ex) {
            setIcon(null);
        }
    }

}
