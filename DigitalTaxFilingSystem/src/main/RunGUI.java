package main;

import db.DBSetup;
import ui.CardTaxFilingSystem;

import javax.swing.*;

public class RunGUI {
    public static void main(String[] args) {
        DBSetup.bootstrap();
        SwingUtilities.invokeLater(() -> new CardTaxFilingSystem().setVisible(true));
    }
}