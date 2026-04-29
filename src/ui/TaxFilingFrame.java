package ui;

import javax.swing.*;

public class TaxFilingFrame extends JFrame {
    public TaxFilingFrame() {
        setTitle("Tax Filing Frame (Optional)");
        setSize(400, 200);
        setLocationRelativeTo(null);
        add(new JLabel("Use CardTaxFilingSystem for the main GUI.", SwingConstants.CENTER));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
}
