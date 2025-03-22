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
    private List<Player> allPlayers;
    private Player selectedPlayer;


    public PlayerSelectionDialogUI(Frame parent, ApiService apiService) {
        super(parent, "Oyuncu Seçimi", true);
        setSize(400, 550);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Başlangıç oyuncu verisi
        allPlayers = ApiService.getCachedPlayers();

        // Oyuncu listesi
        listModel = new DefaultListModel<>();
        playerList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(playerList);

        // Mevki filtreleme
        positionFilter = new JComboBox<>(new String[]{"Tümü", "Forvet", "Orta Saha", "Defans", "Kaleci"});
        positionFilter.addActionListener(e -> filterPlayers());

        // İsim filtreleme
        nameFilterField = new JTextField(15);
        nameFilterField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filterPlayers();
            }
        });

        // Seçim butonu
        JButton selectButton = new JButton("Oyuncuyu Seç");
        selectButton.addActionListener(e -> {
            int selectedIndex = playerList.getSelectedIndex();
            if (selectedIndex != -1 && selectedIndex < listModel.size()) {
                String selectedValue = listModel.getElementAt(selectedIndex);
                String selectedName = selectedValue.split(" - ")[0];

                selectedPlayer = allPlayers.stream()
                        .filter(p -> p.getName().equals(selectedName))
                        .findFirst()
                        .orElse(null);
                dispose();
            }
        });

        // 🚀 **Silme butonu ekleme**
        JButton removeButton = new JButton("Oyuncuyu Sil");
        removeButton.addActionListener(e -> {
            selectedPlayer = new Player(0, "Seç", "", 0, "", "");
            dispose();
        });

        // Üst panel (filtreler)
        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Mevki:"));
        topPanel.add(positionFilter);
        topPanel.add(new JLabel("İsim:"));
        topPanel.add(nameFilterField);

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(selectButton);
        bottomPanel.add(removeButton);

        // Ekrana ekle
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // Başlangıçta listele
        updatePlayerList(allPlayers);
    }

    private void updatePlayerList(List<Player> players) {
        listModel.clear();

        for (Player player : players) {
            listModel.addElement(player.getName() + " - " + player.getPosition());
        }
    }

    private void filterPlayers() {
        String selectedPosition = (String) positionFilter.getSelectedItem();
        String nameFilter = nameFilterField.getText();

        List<Player> filtered = allPlayers;

        // Pozisyon filtresi "Tümü" değilse uygula
        if (!selectedPosition.equalsIgnoreCase("Tümü")) {
            filtered = FilterUtils.filterByExactPosition(filtered, selectedPosition);
        }

        // İsim filtresi varsa uygula
        if (!nameFilter.isBlank()) {
            filtered = FilterUtils.filterByName(filtered, nameFilter);
        }

        updatePlayerList(filtered);
    }

    public Player getSelectedPlayer() {
        return selectedPlayer;
    }
}
