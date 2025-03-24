package org.example;
import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.example.View.FormationUI;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class FormationUITest {

    @Test
    void testCreateFormationPanel_ValidFormation() {
        JPanel panel = FormationUI.createFormationPanel("4-4-2");

        assertNotNull(panel, "Panel null olmamalı.");
        assertEquals(4, panel.getComponentCount(), "4-4-2 formasyonu 4 satır içermeli.");
    }

    @Test
    void testCreateFormationPanel_InvalidFormation() {
        JPanel panel = FormationUI.createFormationPanel("10-0-0");

        assertNotNull(panel, "Geçersiz formasyonlarda bile panel null olmamalı.");
        assertEquals(0, panel.getComponentCount(), "Geçersiz formasyon boş panel dönmeli.");
    }

    @Test
    void testEachRowIsPanelAndContainsButtons() {
        JPanel panel = FormationUI.createFormationPanel("4-4-2");

        for (Component comp : panel.getComponents()) {
            assertInstanceOf(JPanel.class, comp, "Satırlar JPanel olmalı.");

            JPanel row = (JPanel) comp;
            assertTrue(row.getComponentCount() > 0, "Her satır en az bir buton içermeli.");
            assertInstanceOf(JButton.class, row.getComponent(0), "İlk buton JButton olmalı.");
        }
    }
}
