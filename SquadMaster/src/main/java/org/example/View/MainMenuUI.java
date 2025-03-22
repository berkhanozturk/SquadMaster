package org.example.View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MainMenuUI extends JFrame {

    public MainMenuUI() {
        setTitle("Squad Master - Ana Menü");
        setSize(500, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(0, 100, 0));

        JLabel titleLabel = new JLabel("⚽ Squad Master", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        add(titleLabel, BorderLayout.NORTH);

        JButton startButton = new JButton("🎮 Oyuna Başla");
        startButton.setFont(new Font("Arial", Font.BOLD, 20));
        startButton.addActionListener(this::startGame);

        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(new Color(0, 100, 0));
        centerPanel.add(startButton);

        add(centerPanel, BorderLayout.CENTER);
    }

    private void startGame(ActionEvent e) {
        // Oyuncular arka planda yüklensin
        JDialog loadingDialog = new JDialog(this, "Yükleniyor...", true);
        loadingDialog.setSize(300, 150);
        loadingDialog.setLayout(new BorderLayout());
        loadingDialog.add(new JLabel("Oyuncular yükleniyor, lütfen bekleyin...", SwingConstants.CENTER), BorderLayout.CENTER);
        loadingDialog.setLocationRelativeTo(this);
        loadingDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                org.example.Service.ApiService.initializePlayers();
                return null;
            }

            @Override
            protected void done() {
                loadingDialog.dispose();
                SquadUI squadUI = new SquadUI(org.example.Service.ApiService.getCachedPlayers());
                squadUI.setVisible(true);
                dispose(); // Ana menüyü kapat
            }
        };

        worker.execute();
        loadingDialog.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainMenuUI mainMenu = new MainMenuUI();
            mainMenu.setVisible(true);
        });
    }
}
