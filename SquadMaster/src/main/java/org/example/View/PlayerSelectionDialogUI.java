package org.example.View;

import org.example.Model.Player;
import org.example.Service.ApiService;
import org.example.Utils.FilterUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.stream.Collectors;

public class PlayerSelectionDialogUI extends JDialog {
    private JList<String> playerList;
    private DefaultListModel<String> listModel;
    private JComboBox<String> positionFilter;
    private JTextField nameFilterField;
    private JComboBox<String> minAgeCombo;
    private JComboBox<String> maxAgeCombo;

    private List<Player> allPlayers;
    private Player selectedPlayer;

    public PlayerSelectionDialogUI(Frame parent, ApiService apiService) {
        super(parent, "Oyuncu Seçimi", true);
        setSize(750, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        allPlayers = ApiService.getCachedPlayers();

        // Oyuncu listesi
        listModel = new DefaultListModel<>();
        playerList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(playerList);

        // Mevki filtreleme
        positionFilter = new JComboBox<>(new String[]{"Tümü", "Attacker", "Midfielder", "Defender", "Goalkeeper"});
        positionFilter.addActionListener(e -> filterPlayers());

        // İsim filtreleme (JTextField)
        nameFilterField = new JTextField(15);
        nameFilterField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filterPlayers();
            }
        });

        // Yaş filtreleme
        minAgeCombo = new JComboBox<>();
        maxAgeCombo = new JComboBox<>();
        minAgeCombo.addItem("Min");
        maxAgeCombo.addItem("Max");
        for (int i = 16; i <= 45; i++) {
            minAgeCombo.addItem(String.valueOf(i));
            maxAgeCombo.addItem(String.valueOf(i));
        }
        minAgeCombo.addActionListener(e -> filterPlayers());
        maxAgeCombo.addActionListener(e -> filterPlayers());

        // Oyuncu Seç Butonu
        JButton selectButton = new JButton("Oyuncuyu Seç");
        selectButton.addActionListener(e -> {
            int selectedIndex = playerList.getSelectedIndex();
            if (selectedIndex != -1 && selectedIndex < listModel.size()) {
                String selectedValue = listModel.getElementAt(selectedIndex);
                String selectedName = selectedValue.split(" - ")[0];

                selectedPlayer = allPlayers.stream()
                        .filter(p -> p.getName().equalsIgnoreCase(selectedName))
                        .findFirst()
                        .orElse(null);
                dispose();
            }
        });

        // Oyuncu Sil Butonu
        JButton removeButton = new JButton("Oyuncuyu Sil");
        removeButton.addActionListener(e -> {
            selectedPlayer = new Player(0, "Seç", "", 0, "", "");
            dispose();
        });

        // Üst Panel - Filtreler
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.add(new JLabel("Mevki:"));
        filterPanel.add(positionFilter);
        filterPanel.add(new JLabel("Yaş Min:"));
        filterPanel.add(minAgeCombo);
        filterPanel.add(new JLabel("Yaş Max:"));
        filterPanel.add(maxAgeCombo);
        filterPanel.add(new JLabel("İsim:"));
        filterPanel.add(nameFilterField);

        // Alt Panel - Seç / Sil
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(selectButton);
        bottomPanel.add(removeButton);

        // Arayüzü birleştir
        add(filterPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        updatePlayerList(allPlayers);
    }

    private void filterPlayers() {
        String selectedPosition = (String) positionFilter.getSelectedItem();
        String nameFilter = nameFilterField.getText().trim().toLowerCase();
        String minAgeStr = (String) minAgeCombo.getSelectedItem();
        String maxAgeStr = (String) maxAgeCombo.getSelectedItem();

        int minAge = minAgeStr.equals("Min") ? -1 : Integer.parseInt(minAgeStr);
        int maxAge = maxAgeStr.equals("Max") ? -1 : Integer.parseInt(maxAgeStr);

        List<Player> filtered = allPlayers;

        if (!selectedPosition.equalsIgnoreCase("Tümü")) {
            filtered = FilterUtils.filterByExactPosition(filtered, selectedPosition);
        }

        if (!nameFilter.isBlank()) {
            filtered = FilterUtils.filterByName(filtered, nameFilter);
        }

        filtered = FilterUtils.filterByAge(filtered, minAge, maxAge);

        updatePlayerList(filtered);
    }

    private void updatePlayerList(List<Player> players) {
        listModel.clear();
        if (players.isEmpty()) {
            listModel.addElement("Oyuncu bulunamadı!");
        } else {
            for (Player p : players) {
                listModel.addElement(p.getName() + " - " + p.getPosition());
            }
        }
    }

    public Player getSelectedPlayer() {
        return selectedPlayer;
    }
}
