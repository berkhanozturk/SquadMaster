package org.example.View;

import org.example.Model.Player;
import org.example.Service.ApiService;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

public class MainMenuUI extends JFrame {

    public MainMenuUI() {
        setTitle("Kadro Kurma Oyunu - Squad Master");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Arka plan resmi paneli
        BackgroundPanel backgroundPanel = new BackgroundPanel();
        backgroundPanel.setLayout(new BorderLayout());
        setContentPane(backgroundPanel);

        //  Beyaz kutu paneli (başlık için)
        JPanel titleBox = new JPanel();
        titleBox.setBackground(Color.WHITE);
        titleBox.setOpaque(true);
        titleBox.setLayout(new BorderLayout());
        titleBox.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 2),             // Dış gri çerçeve
                BorderFactory.createEmptyBorder(10, 20, 10, 20)            // İç padding
        ));
        titleBox.setPreferredSize(new Dimension(340, 60));

        // 🏷️ Başlık etiketi
        JLabel titleLabel = new JLabel("🏟️ SquadMaster", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(Color.BLACK);
        titleBox.add(titleLabel, BorderLayout.CENTER);

        JPanel titleWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titleWrapper.setOpaque(false);
        titleWrapper.add(titleBox);
        backgroundPanel.add(titleWrapper, BorderLayout.NORTH);

        // Başlat butonu
        JButton startButton = new JButton("🎮 Oyuna Başla");
        startButton.setFont(new Font("Arial", Font.BOLD, 20));
        startButton.setBackground(new Color(0, 150, 0));
        startButton.setForeground(Color.BLACK);
        startButton.setFocusPainted(false);
        startButton.setPreferredSize(new Dimension(200, 50));
        startButton.addActionListener(this::startGame);


        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.add(startButton);

        backgroundPanel.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void startGame(ActionEvent e) {
        JDialog loadingDialog = new JDialog(this, "Yükleniyor...", true);
        loadingDialog.setSize(300, 150);
        loadingDialog.setLayout(new BorderLayout());
        loadingDialog.add(new JLabel("Oyuncular yükleniyor, lütfen bekleyin...", SwingConstants.CENTER), BorderLayout.CENTER);
        loadingDialog.setLocationRelativeTo(this);
        loadingDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        SwingWorker<List<Player>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Player> doInBackground() {
                ApiService.initializePlayers();
                return ApiService.getCachedPlayers();
            }

            @Override
            protected void done() {
                try {
                    List<Player> players = get();
                    loadingDialog.dispose();
                    SquadUI squadUI = new SquadUI(players);
                    squadUI.setVisible(true);
                    dispose();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Oyuncular yüklenirken hata oluştu!", "Hata", JOptionPane.ERROR_MESSAGE);
                    loadingDialog.dispose();
                }
            }
        };

        worker.execute();
        loadingDialog.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainMenuUI menu = new MainMenuUI();
            menu.setVisible(true);
        });
    }

    // Arka plan resmi paneli
    class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel() {
            try {
                // Proje içinden, classpath üzerinden yüklenir
                backgroundImage = new ImageIcon(getClass().getClassLoader().getResource("menu5.png")).getImage();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this); // Ekran boyutuna yay
            }
        }
    }

}
