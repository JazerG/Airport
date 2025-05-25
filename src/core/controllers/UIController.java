/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.controllers;

import javax.swing.JComboBox;
import javax.swing.JTabbedPane;

/**
 *
 * @author jazer
 */
public class UIController {
    public void blockPanels(JTabbedPane tabbedPane) {
        for (int i = 1; i < tabbedPane.getTabCount(); i++) {
            if (i != 9 && i != 11) {
                tabbedPane.setEnabledAt(i, false);
            }
        }
    }

    public void generateMonths(JComboBox... comboBoxes) {
        for (int i = 1; i < 13; i++) {
            for (JComboBox box : comboBoxes) {
                box.addItem("" + i);
            }
        }
    }

    public void generateDays(JComboBox... comboBoxes) {
        for (int i = 1; i < 32; i++) {
            for (JComboBox box : comboBoxes) {
                box.addItem("" + i);
            }
        }
    }

    public void generateHours(JComboBox... comboBoxes) {
        for (int i = 0; i < 24; i++) {
            for (JComboBox box : comboBoxes) {
                box.addItem("" + i);
            }
        }
    }

    public void generateMinutes(JComboBox... comboBoxes) {
        for (int i = 0; i < 60; i++) {
            for (JComboBox box : comboBoxes) {
                box.addItem("" + i);
            }
        }
    }
}
