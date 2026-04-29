package ui;

import dao.AdminDAO;
import dao.PaymentDAO;
import dao.TaxFilingDAO;
import dao.TaxPayerDAO;
import model.*;
import java.sql.ResultSet;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class CardTaxFilingSystem extends JFrame {

    private final CardLayout card = new CardLayout();
    private final JPanel root = new JPanel(card);

    private TaxPayer loggedUser = null;

    private final DefaultTableModel filingsModel =
            new DefaultTableModel(new String[]{"FilingID","Tax","Status","Date"}, 0) {
                @Override public boolean isCellEditable(int r, int c) { return false; }
            };

    private final DefaultTableModel paymentsModel =
            new DefaultTableModel(new String[]{"PaymentID","FilingID","Amount","Method","Verified"}, 0) {
                @Override public boolean isCellEditable(int r, int c) { return false; }
            };

    // Shared fonts
    private final Font TITLE = new Font("Segoe UI", Font.BOLD, 28);
    private final Font SUB   = new Font("Segoe UI", Font.PLAIN, 14);
    private final Font H2    = new Font("Segoe UI", Font.BOLD, 18);
    private final Font UI    = new Font("Segoe UI", Font.PLAIN, 14);

    // Accent color
    private final Color ACCENT = new Color(32, 110, 243);
    private final Color BG     = new Color(245, 246, 250);

    public CardTaxFilingSystem() {
        setTitle("Digital Tax Filing System");
        setSize(980, 580);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setContentPane(root);
        root.setBackground(BG);

        root.add(welcome(), "WELCOME");
        root.add(register(), "REGISTER");
        root.add(login(), "LOGIN");
        root.add(dashboard(), "DASH");
        root.add(adminPanel(), "ADMIN");

        card.show(root, "WELCOME");
    }

    // ---------- UI helpers ----------
    private JPanel page() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(BG);
        p.setBorder(new EmptyBorder(24, 24, 24, 24));
        return p;
    }

    private JPanel cardPanel() {
        JPanel c = new JPanel();
        c.setLayout(new BoxLayout(c, BoxLayout.Y_AXIS));
        c.setBackground(Color.WHITE);
        c.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 232, 240)),
                new EmptyBorder(18, 18, 18, 18)
        ));
        return c;
    }

    private JLabel label(String s) {
        JLabel l = new JLabel(s);
        l.setFont(UI);
        l.setForeground(new Color(50, 50, 50));
        return l;
    }

    private JTextField textField() {
        JTextField t = new JTextField();
        t.setFont(UI);
        t.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 222, 230)),
                new EmptyBorder(8, 10, 8, 10)
        ));
        return t;
    }

    private JPasswordField passwordField() {
        JPasswordField t = new JPasswordField();
        t.setFont(UI);
        t.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 222, 230)),
                new EmptyBorder(8, 10, 8, 10)
        ));
        return t;
    }

    private JButton primaryBtn(String text) {
        JButton b = new JButton(text);
        b.setFont(UI.deriveFont(Font.BOLD));
        b.setBackground(ACCENT);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorder(new EmptyBorder(10, 14, 10, 14));
        return b;
    }

    private JButton ghostBtn(String text) {
        JButton b = new JButton(text);
        b.setFont(UI);
        b.setBackground(Color.WHITE);
        b.setForeground(new Color(60, 60, 60));
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 222, 230)),
                new EmptyBorder(10, 14, 10, 14)
        ));
        return b;
    }

    private JTable niceTable(DefaultTableModel model) {
        JTable t = new JTable(model);
        t.setFont(UI);
        t.setRowHeight(30);
        t.getTableHeader().setFont(UI.deriveFont(Font.BOLD));
        t.getTableHeader().setReorderingAllowed(false);

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER);
        t.setDefaultRenderer(Object.class, center);

        return t;
    }

    // ---------- Screens ----------
    private JPanel welcome() {
        JPanel p = page();

        JPanel c = cardPanel();
        c.setPreferredSize(new Dimension(520, 340));

        JLabel title = new JLabel("Digital Tax Filing System");
        title.setFont(TITLE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel sub = new JLabel("Simple • Secure • Fast filing experience");
        sub.setFont(SUB);
        sub.setForeground(new Color(90, 90, 90));
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);

        c.add(title);
        c.add(Box.createVerticalStrut(8));
        c.add(sub);
        c.add(Box.createVerticalStrut(20));

        JButton b1 = primaryBtn("User Sign Up");
        JButton b2 = ghostBtn("User Login");
        JButton b3 = ghostBtn("Admin");

        b1.setAlignmentX(Component.CENTER_ALIGNMENT);
        b2.setAlignmentX(Component.CENTER_ALIGNMENT);
        b3.setAlignmentX(Component.CENTER_ALIGNMENT);

        b1.setMaximumSize(new Dimension(240, 42));
        b2.setMaximumSize(new Dimension(240, 42));
        b3.setMaximumSize(new Dimension(240, 42));

        b1.addActionListener(e -> card.show(root,"REGISTER"));
        b2.addActionListener(e -> card.show(root,"LOGIN"));
        b3.addActionListener(e -> card.show(root,"ADMIN"));

        c.add(b1);
        c.add(Box.createVerticalStrut(10));
        c.add(b2);
        c.add(Box.createVerticalStrut(10));
        c.add(b3);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0;
        p.add(c, gbc);

        return p;
    }

    private JPanel register() {
        JPanel p = page();
        JPanel c = cardPanel();
        c.setPreferredSize(new Dimension(640, 420));

        JLabel h = new JLabel("Create a Taxpayer Account");
        h.setFont(H2);
        h.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField id = textField();
        JTextField name = textField();
        JTextField income = textField();
        JComboBox<String> type = new JComboBox<>(new String[]{"Individual","Company"});
        type.setFont(UI);
        type.setBorder(BorderFactory.createLineBorder(new Color(220, 222, 230)));
        JPasswordField pass = passwordField();

        JButton sign = primaryBtn("Sign Up");
        JButton back = ghostBtn("Back");

        JPanel form = new JPanel(new GridLayout(5,2,12,12));
        form.setOpaque(false);
        form.setAlignmentX(Component.LEFT_ALIGNMENT);

        form.add(label("Taxpayer ID:")); form.add(id);
        form.add(label("Name:"));       form.add(name);
        form.add(label("Income:"));     form.add(income);
        form.add(label("Type:"));       form.add(type);
        form.add(label("Password:"));   form.add(pass);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actions.setOpaque(false);
        actions.setAlignmentX(Component.LEFT_ALIGNMENT);
        actions.add(back);
        actions.add(sign);

        sign.addActionListener(e -> {
            String tid = id.getText().trim();
            String nm  = name.getText().trim();
            String incTxt = income.getText().trim();
            String tp  = type.getSelectedItem().toString();
            String pw  = new String(pass.getPassword()).trim();

            if (tid.isEmpty() || nm.isEmpty() || incTxt.isEmpty() || pw.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Fill all fields!");
                return;
            }

            double inc;
            try {
                inc = Double.parseDouble(incTxt);
                if (inc < 0) throw new NumberFormatException();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Income must be a valid number!");
                return;
            }

            if (TaxPayerDAO.existsTaxpayer(tid)) {
                JOptionPane.showMessageDialog(this, "This ID already exists. Try 1002, 1003, ...");
                return;
            }

            TaxPayer taxpayer = tp.equalsIgnoreCase("Company")
                    ? new Company(tid, nm, inc)
                    : new Individual(tid, nm, inc);

            boolean ok = TaxPayerDAO.insertTaxPayer(taxpayer, pw);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Sign Up successful! Now login.");
                id.setText(""); name.setText(""); income.setText(""); pass.setText("");
                card.show(root,"LOGIN");
            } else {
                JOptionPane.showMessageDialog(this, "Sign Up failed (DB). Check console + table columns.");
            }
        });

        back.addActionListener(e -> card.show(root,"WELCOME"));

        c.add(h);
        c.add(Box.createVerticalStrut(14));
        c.add(form);
        c.add(Box.createVerticalStrut(18));
        c.add(actions);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0;
        p.add(c, gbc);

        return p;
    }

    private JPanel login() {
        JPanel p = page();
        JPanel c = cardPanel();
        c.setPreferredSize(new Dimension(520, 320));

        JLabel h = new JLabel("Login");
        h.setFont(H2);
        h.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField id = textField();
        JPasswordField pass = passwordField();

        JButton login = primaryBtn("Login");
        JButton back  = ghostBtn("Back");

        JPanel form = new JPanel(new GridLayout(2,2,12,12));
        form.setOpaque(false);
        form.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(label("Taxpayer ID:"));
        form.add(id);
        form.add(label("Password:"));
        form.add(pass);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actions.setOpaque(false);
        actions.add(back);
        actions.add(login);

        login.addActionListener(e -> {
            String tid = id.getText().trim();
            String pw = new String(pass.getPassword()).trim();

            if (tid.isEmpty() || pw.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter ID and Password!");
                return;
            }

            TaxPayer user = TaxPayerDAO.login(tid, pw);

            if (user == null) {
                JOptionPane.showMessageDialog(this, "Wrong ID or password!");
                return;
            }

            loggedUser = user;
            refreshHistory();
            card.show(root,"DASH");
        });

        back.addActionListener(e -> card.show(root,"WELCOME"));

        c.add(h);
        c.add(Box.createVerticalStrut(14));
        c.add(form);
        c.add(Box.createVerticalStrut(18));
        c.add(actions);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0;
        p.add(c, gbc);

        return p;
    }

    private JPanel dashboard() {
        JPanel p = new JPanel(new BorderLayout(12,12));
        p.setBackground(BG);
        p.setBorder(new EmptyBorder(14,14,14,14));

        // Top header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 232, 240)),
                new EmptyBorder(10, 12, 10, 12)
        ));

        JLabel title = new JLabel("Dashboard");
        title.setFont(H2);

        JButton logout = ghostBtn("Logout");
        logout.addActionListener(e -> {
            loggedUser = null;
            filingsModel.setRowCount(0);
            paymentsModel.setRowCount(0);
            card.show(root,"WELCOME");
        });

        header.add(title, BorderLayout.WEST);
        header.add(logout, BorderLayout.EAST);

        // Toolbar
        JPanel tools = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        tools.setBackground(Color.WHITE);
        tools.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 232, 240)),
                new EmptyBorder(8, 10, 8, 10)
        ));

        JTextField income = textField();
        income.setPreferredSize(new Dimension(140, 36));

        JButton file = primaryBtn("File Tax");
        JButton refresh = ghostBtn("Refresh History");

        tools.add(label("Income for filing:"));
        tools.add(income);
        tools.add(file);
        tools.add(refresh);

        // Tabs
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(UI);

        JTable filingsTable = niceTable(filingsModel);
        JTable paymentsTable = niceTable(paymentsModel);

        tabs.add("Filings", new JScrollPane(filingsTable));
        tabs.add("Payments", new JScrollPane(paymentsTable));

        // Actions
        file.addActionListener(e -> {
            if (loggedUser == null) return;

            double inc;
            try {
                inc = Double.parseDouble(income.getText().trim());
                if (inc < 0) throw new NumberFormatException();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid income!");
                return;
            }

            loggedUser.setIncome(inc);

            String filingId = "F" + System.currentTimeMillis();
            TaxFiling f = new TaxFiling(filingId, loggedUser);
            f.submitFiling();

            boolean saved = TaxFilingDAO.insertTaxFiling(f);
            if (!saved) {
                JOptionPane.showMessageDialog(this, "Filing save failed (DB). Check console.");
                return;
            }

            int choice = JOptionPane.showConfirmDialog(this,
                    "Tax filed!\nFiling ID: " + filingId + "\nTax: " + f.getTaxAmount() + "\n\nPay now?",
                    "Payment", JOptionPane.YES_NO_OPTION);

            if (choice == JOptionPane.YES_OPTION) {
                String method = JOptionPane.showInputDialog(this, "Method: Card/Bank/Cash");
                if (method != null && !method.trim().isEmpty()) {
                    Payment pay = new Payment("P" + System.currentTimeMillis(), f, method.trim());
                    pay.processPayment();
                    boolean paySaved = PaymentDAO.insertPayment(pay);
                    if (!paySaved) JOptionPane.showMessageDialog(this, "Payment save failed (DB).");
                }
            }

            refreshHistory();
        });

        refresh.addActionListener(e -> refreshHistory());

        JPanel center = new JPanel(new BorderLayout(10,10));
        center.setOpaque(false);
        center.add(tools, BorderLayout.NORTH);
        center.add(tabs, BorderLayout.CENTER);

        p.add(header, BorderLayout.NORTH);
        p.add(center, BorderLayout.CENTER);
        return p;
    }

  private void refreshHistory() {
    if (loggedUser == null) return;

    filingsModel.setRowCount(0);
    paymentsModel.setRowCount(0);

    try {
        // ----- FILINGS -----
        ResultSet frs = TaxFilingDAO.getFilingsByTaxpayer(
                loggedUser.getTaxpayerID()
        );

        if (frs != null) {
            while (frs.next()) {
                filingsModel.addRow(new Object[]{
                        frs.getString("filingID"),
                        frs.getDouble("taxAmount"),
                        frs.getString("status"),
                        frs.getDate("filingDate")
                });
            }
        }

        // ----- PAYMENTS -----
        ResultSet prs = PaymentDAO.getPaymentsByTaxpayer(
                loggedUser.getTaxpayerID()
        );

        if (prs != null) {
            while (prs.next()) {
                paymentsModel.addRow(new Object[]{
                        prs.getString("paymentID"),
                        prs.getString("filingID"),
                        prs.getDouble("amount"),
                        prs.getString("method"),
                        prs.getBoolean("verified")
                });
            }
        }

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(
                this,
                "Failed to load history. Check console."
        );
    }
}



    private JPanel adminPanel() {
        JPanel p = page();
        JPanel c = cardPanel();
        c.setPreferredSize(new Dimension(640, 360));

        JLabel h = new JLabel("Admin Approval");
        h.setFont(H2);
        h.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField user = textField();
        JPasswordField pass = passwordField();
        JTextField filingId = textField();

        JButton approve = primaryBtn("Login & Approve");
        JButton back = ghostBtn("Back");

        JPanel form = new JPanel(new GridLayout(3,2,12,12));
        form.setOpaque(false);
        form.setAlignmentX(Component.LEFT_ALIGNMENT);

        form.add(label("Admin username:")); form.add(user);
        form.add(label("Admin password:")); form.add(pass);
        form.add(label("Filing ID to approve:")); form.add(filingId);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actions.setOpaque(false);
        actions.add(back);
        actions.add(approve);

        approve.addActionListener(e -> {
            String u = user.getText().trim();
            String pw = new String(pass.getPassword()).trim();
            String fid = filingId.getText().trim();

            if (u.isEmpty() || pw.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter admin username & password!");
                return;
            }

            Admin a = AdminDAO.login(u, pw);
            if (a == null) {
                JOptionPane.showMessageDialog(this, "Admin login failed!");
                return;
            }

            if (fid.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter Filing ID!");
                return;
            }

            boolean ok = TaxFilingDAO.updateFilingStatus(fid, "Approved");
            JOptionPane.showMessageDialog(this, ok ? "Approved!" : "Filing not found!");
        });

        back.addActionListener(e -> card.show(root,"WELCOME"));

        c.add(h);
        c.add(Box.createVerticalStrut(14));
        c.add(form);
        c.add(Box.createVerticalStrut(18));
        c.add(actions);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0;
        p.add(c, gbc);

        return p;
    }
}
