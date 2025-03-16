package org.example.View;

import javax.swing.*;
import java.awt.*;

public class SquadUI extends JFrame {
    private JComboBox<String> formationComboBox;
    private JPanel fieldPanel;
    private JPanel benchPanel;

    public SquadUI() {
        setTitle("Squad Master - Kadro Seçimi");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Üst Panel: Formasyon Seçimi
        JPanel topPanel = new JPanel();
        formationComboBox = new JComboBox<>(new String[]{"4-4-2", "4-3-3", "3-5-2"});
        formationComboBox.addActionListener(e -> updateFormation());

        topPanel.add(new JLabel("Formasyon Seç:"));
        topPanel.add(formationComboBox);
        add(topPanel, BorderLayout.NORTH);

        // Orta Panel: Futbol Sahası
        fieldPanel = FormationUI.createFormationPanel("4-4-2");
        add(fieldPanel, BorderLayout.CENTER);

        // Alt Panel: Yedekler ve Rezerve Oyuncular
        benchPanel = new JPanel();
        benchPanel.setLayout(new FlowLayout());

        JLabel benchLabel = new JLabel("Yedekler:");
        benchPanel.add(benchLabel);

        for (int i = 0; i < 5; i++) { // 5 Yedek Oyuncu
            JButton subButton = new JButton("Yedek " + (i + 1));
            subButton.setPreferredSize(new Dimension(80, 80));
            benchPanel.add(subButton);
        }

        JLabel reserveLabel = new JLabel("Rezerve:");
        benchPanel.add(reserveLabel);

        for (int i = 0; i < 3; i++) { // 3 Rezerve Oyuncu
            JButton resButton = new JButton("Rezerve " + (i + 1));
            resButton.setPreferredSize(new Dimension(80, 80));
            benchPanel.add(resButton);
        }

        add(benchPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void updateFormation() {
        remove(fieldPanel);
        fieldPanel = FormationUI.createFormationPanel((String) formationComboBox.getSelectedItem());
        add(fieldPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SquadUI::new);
    }
}
