package org.example.View;

import javax.swing.*;
import java.awt.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Extensions.PlayerButton;
import org.example.Model.Player;
import org.example.Model.SavedSquad;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class SquadUI extends JFrame {
    private JPanel fieldPanel;
    private JComboBox<String> formationComboBox;
    private static List<Player> allPlayers;
    private String currentFormation = "4-4-2";

    public SquadUI(List<Player> players) {
        this.allPlayers = players;

        setTitle("Squad Master - Kadro Seçimi");
        setSize(1100, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        // Üst Panel (Formasyon Seçme)
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topPanel.setBackground(new Color(0, 100, 0));
        JLabel formationLabel = new JLabel("Formasyon Seç:");
        formationComboBox = new JComboBox<>(new String[]{"4-4-2", "4-3-3", "3-5-2","5-3-2","5-2-3","4-5-1"});
        formationComboBox.addActionListener(e -> updateFormation((String) formationComboBox.getSelectedItem()));
        topPanel.add(formationLabel);
        topPanel.add(formationComboBox);

        // 🔵 Kadroyu Kaydet Butonu
        JButton saveButton = new JButton("Kadroyu Kaydet");
        saveButton.addActionListener(e -> saveSquadToFile());

        JButton loadButton = new JButton("Kadroyu Yükle");
        loadButton.addActionListener(e -> loadSquadFromFile());

        topPanel.add(saveButton);
        topPanel.add(loadButton);

        add(topPanel, BorderLayout.NORTH);

        // Orta Panel (Saha ve Oyuncular)
        fieldPanel = new JPanel(new BorderLayout());
        updateFormation("4-4-2");
        add(fieldPanel, BorderLayout.CENTER);

        // Alt Panel (Yedek Oyuncular)
        JPanel benchPanel = createBenchPanel();
        benchPanel.setBackground(new Color(0, 100, 0));
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

    private void saveSquadToFile() {
        String squadName = JOptionPane.showInputDialog(this, "Kadro ismi giriniz:");
        if (squadName == null || squadName.trim().isEmpty()) return;

        List<Player> selectedPlayers = FormationUI.getCurrentPlayers();
        SavedSquad savedSquad = new SavedSquad(currentFormation, selectedPlayers);

        try {
            File dir = new File("saved_squads");
            if (!dir.exists()) dir.mkdirs();

            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(new File(dir, squadName + ".json"), savedSquad);
            JOptionPane.showMessageDialog(this, "Kadro başarıyla kaydedildi.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Kayıt başarısız: " + ex.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadSquadFromFile() {
        JFileChooser fileChooser = new JFileChooser(new File("saved_squads"));
        int result = fileChooser.showOpenDialog(this);
        if (result != JFileChooser.APPROVE_OPTION) return;

        File file = fileChooser.getSelectedFile();
        try {
            ObjectMapper mapper = new ObjectMapper();
            SavedSquad savedSquad = mapper.readValue(file, SavedSquad.class);

            currentFormation = savedSquad.getFormation();
            formationComboBox.setSelectedItem(currentFormation);
            JPanel formationPanel = FormationUI.createFormationPanel(currentFormation, savedSquad.getPlayers());
            fieldPanel.removeAll();
            fieldPanel.add(formationPanel, BorderLayout.CENTER);
            fieldPanel.revalidate();
            fieldPanel.repaint();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Kadro yüklenemedi: " + ex.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }



}
