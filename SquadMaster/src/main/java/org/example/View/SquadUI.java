package org.example.View;

import javax.swing.*;
import java.awt.*;

import org.example.Extensions.PlayerButton;
import org.example.Model.Player;
import org.example.Service.ApiService;
import java.util.List;

public class SquadUI extends JFrame {
    private JPanel fieldPanel;
    private JComboBox<String> formationComboBox;
    private static List<Player> allPlayers;

    public SquadUI(List<Player> players) {
        this.allPlayers = players;

        setTitle("Squad Master - Kadro Seçimi");
        setSize(1100, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(0, 100, 0));

        // Üst Panel (Formasyon Seçme)
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel formationLabel = new JLabel("Formasyon Seç:");
        formationComboBox = new JComboBox<>(new String[]{"4-4-2", "4-3-3", "3-5-2","5-3-2","5-2-3","4-5-1"});
        formationComboBox.addActionListener(e -> updateFormation((String) formationComboBox.getSelectedItem()));
        topPanel.add(formationLabel);
        topPanel.add(formationComboBox);
        add(topPanel, BorderLayout.NORTH);

        // Orta Panel (Saha ve Oyuncular)
        fieldPanel = new JPanel(new BorderLayout());
        updateFormation("4-4-2");
        add(fieldPanel, BorderLayout.CENTER);

        // Alt Panel (Yedek Oyuncular)
        JPanel benchPanel = createBenchPanel();
        add(benchPanel, BorderLayout.SOUTH);
    }

    private void updateFormation(String formation) {
        fieldPanel.removeAll();
        JPanel formationPanel = FormationUI.createFormationPanel(formation);
        fieldPanel.add(formationPanel, BorderLayout.CENTER);
        fieldPanel.revalidate();
        fieldPanel.repaint();
    }

    private JPanel createBenchPanel() {
        JPanel benchPanel = new JPanel(new GridLayout(1, 10, 10, 10));
        benchPanel.setPreferredSize(new Dimension(900, 120));

        // Yedek oyuncular
        JLabel yedekLabel = new JLabel("Yedekler:");
        benchPanel.add(yedekLabel);
        for (int i = 0; i < 5; i++) {
            PlayerButton benchButton = new PlayerButton(new Player(0, "seç","", 0, "", ""));
            benchPanel.add(benchButton);
        }

        // Rezerve oyuncular
        JLabel reserveLabel = new JLabel("Rezerve:");
        benchPanel.add(reserveLabel);
        for (int i = 0; i < 3; i++) {
            PlayerButton reserveButton = new PlayerButton(new Player(0, "seç","", 0, "", ""));
            benchPanel.add(reserveButton);
        }

        return benchPanel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // 1️⃣ "Yükleniyor..." ekranını oluştur
            JDialog loadingDialog = new JDialog((Frame) null, "Yükleniyor...", true);
            loadingDialog.setSize(300, 150);
            loadingDialog.setLayout(new BorderLayout());
            loadingDialog.add(new JLabel("Oyuncular yükleniyor, lütfen bekleyin...", SwingConstants.CENTER), BorderLayout.CENTER);
            loadingDialog.setLocationRelativeTo(null);
            loadingDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

            // 2️⃣ Oyuncuları arka planda yükle
            SwingWorker<List<Player>, Void> worker = new SwingWorker<>() {
                @Override
                protected List<Player> doInBackground() {
                    ApiService.initializePlayers(); // API'den oyuncuları bir kere çekiyoruz
                    return ApiService.getCachedPlayers(); // Daha sonra buradan okuyoruz
                }

                @Override
                protected void done() {
                    try {
                        List<Player> players = get();
                        loadingDialog.dispose(); // 3️⃣ Yükleniyor ekranı kapatılır
                        SquadUI frame = new SquadUI(players); // Ana ekran açılır
                        frame.setVisible(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Oyuncular yüklenirken hata oluştu!", "Hata", JOptionPane.ERROR_MESSAGE);
                        loadingDialog.dispose();
                    }
                }
            };

            // 4️⃣ SwingWorker çalışmaya başlar ve ekran görünür
            worker.execute();
            loadingDialog.setVisible(true);
        });
    }
}
