package org.example.View;

import org.example.Model.Player;
import org.example.Service.ApiService;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
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

        // 🔲 Beyaz kutu paneli (başlık için)
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
        titleWrapper.setOpaque(false); // Arka planı şeffaf
        titleWrapper.add(titleBox);
        backgroundPanel.add(titleWrapper, BorderLayout.NORTH);
        
        // Butonlar paneli (alt kısım)
JPanel buttonPanel = new JPanel();
buttonPanel.setOpaque(false);
buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));

// ➕ Yeni Kadro Butonu
JButton newSquadButton = new JButton("➕ Yeni Kadro Oluştur");
newSquadButton.setFont(new Font("Arial", Font.BOLD, 18));
newSquadButton.setBackground(new Color(0, 150, 0));
newSquadButton.setForeground(Color.BLACK);
newSquadButton.setFocusPainted(false);
newSquadButton.setPreferredSize(new Dimension(250, 50));
newSquadButton.addActionListener(this::startNewSquad);

// 📂 Kadro Yükle Butonu
JButton loadSquadButton = new JButton("📂 Kadroyu Yükle");
loadSquadButton.setFont(new Font("Arial", Font.BOLD, 18));
loadSquadButton.setBackground(new Color(0, 120, 200));
loadSquadButton.setForeground(Color.BLACK);
loadSquadButton.setFocusPainted(false);
loadSquadButton.setPreferredSize(new Dimension(250, 50));
loadSquadButton.addActionListener(this::loadSquadFromFile);

// Butonları ekle
buttonPanel.add(newSquadButton);
buttonPanel.add(loadSquadButton);
backgroundPanel.add(buttonPanel, BorderLayout.SOUTH);

    }

    private void startNewSquad(ActionEvent e) {
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
    
    private void loadSquadFromFile(ActionEvent e) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Bir kadro dosyası seçin");
        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                java.nio.file.Path path = chooser.getSelectedFile().toPath();
                String json = java.nio.file.Files.readString(path);
                com.google.gson.Gson gson = new com.google.gson.Gson();
                java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<java.util.List<Player>>() {}.getType();
                List<Player> loadedPlayers = gson.fromJson(json, type);
    
                SquadUI squadUI = new SquadUI(loadedPlayers);
                squadUI.setVisible(true);
                dispose();
    
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Kadro yüklenemedi!", "Hata", JOptionPane.ERROR_MESSAGE);
            }
        }
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
                // 📦 Proje içinden, classpath üzerinden yüklenir
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
