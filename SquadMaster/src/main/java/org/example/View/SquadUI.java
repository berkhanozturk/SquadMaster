package org.example.View;

import javax.swing.*;
import java.awt.*;

import org.example.Extensions.PlayerButton;
import org.example.Model.Player;
import org.example.Service.ApiService;

import java.util.ArrayList;
import java.util.List;

public class SquadUI extends JFrame {
    private JPanel fieldPanel;
    private JComboBox<String> formationComboBox;
    private static List<Player> allPlayers;
    private List<PlayerButton> playerButtons = new ArrayList<>();

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

        JButton saveButton = new JButton("💾 Kadroyu kaydet");
        saveButton.addActionListener(e -> saveSquad());
        topPanel.add(saveButton);

        JButton homeButton = new JButton("🏠 Anasayfa");
        homeButton.addActionListener(e -> {
           
            new MainMenuUI().setVisible(true);
            dispose();

        });
        topPanel.add(homeButton);

        // Orta Panel (Saha ve Oyuncular)
        fieldPanel = new JPanel(new BorderLayout());
        updateFormation("4-4-2");
        add(fieldPanel, BorderLayout.CENTER);

        // Alt Panel (Yedek Oyuncular)
        JPanel benchPanel = createBenchPanel();
        add(benchPanel, BorderLayout.SOUTH);
    }

    // Kadroyu kaydetme işlemi
    private void saveSquad() {
        String name = JOptionPane.showInputDialog(this, "Kadroya bir isim verin:", "Kadro Kaydet", JOptionPane.PLAIN_MESSAGE);
        if (name == null || name.isBlank()) return;
    
        List<Player> playersToSave = new ArrayList<>();
    
        // Sahadaki oyuncuları topla
        for (Component comp : fieldPanel.getComponents()) {
            if (comp instanceof JPanel rowPanel) {
                for (Component c : ((JPanel) rowPanel).getComponents()) {
                    if (c instanceof PlayerButton btn) {
                        Player p = btn.getPlayer();
                        if (p != null && !p.getName().equalsIgnoreCase("seç")) {
                            playersToSave.add(p);
                        }
                    }
                }
            }
        }
    
        // Konsola yaz – debug için
        System.out.println("Kayıt edilecek oyuncu sayısı: " + playersToSave.size());
    
        if (playersToSave.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Kadroda hiç oyuncu seçilmedi. Lütfen en az 1 oyuncu seçin.",
                    "Boş Kadro", JOptionPane.WARNING_MESSAGE);
            return;
        }
    
        // JSON'a yaz
        try {
            com.google.gson.Gson gson = new com.google.gson.GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(playersToSave);
            java.nio.file.Files.write(java.nio.file.Path.of(name + ".json"), json.getBytes());
            JOptionPane.showMessageDialog(this, "Kadro '" + name + "' başarıyla kaydedildi.");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Kadro kaydedilirken hata oluştu!", "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    
    
        
    

    private void updateFormation(String formation) {
        fieldPanel.removeAll();
        playerButtons.clear(); // her seferinde temizle
        JPanel formationPanel = FormationUI.createFormationPanel(formation, playerButtons);
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

    
}
