package main;

import ui.CardTaxFilingSystem;

import javax.swing.*;

public class RunGUI {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CardTaxFilingSystem().setVisible(true));
    }
}
