package ui;

import dao.AdminDAO;
import dao.PaymentDAO;
import dao.TaxFilingDAO;
import dao.TaxPayerDAO;
import model.*;
import java.sql.ResultSet;
import java.net.URL;
import java.io.File;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CardTaxFilingSystem extends JFrame {

    private final CardLayout card = new CardLayout();
    private final JPanel root = new JPanel(card);

    private TaxPayer loggedUser = null;

    private final DefaultTableModel filingsModel =
            new DefaultTableModel(new String[]{"FilingID","Tax","Status","Date"}, 0) {
                @Override public boolean isCellEditable(int r, int c) { return false; }
            };

    private final DefaultTableModel paymentsModel =
            new DefaultTableModel(new String[]{"PaymentID","FilingID","Amount","Method"}, 0) {
                @Override public boolean isCellEditable(int r, int c) { return false; }
            };

    private final DefaultTableModel adminModel =
            new DefaultTableModel(new String[]{"FilingID", "Name", "Email", "Amount", "Status", "Date"}, 0) {
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

    // Background image for the welcome screen
    private final ImageIcon WELCOME_BG = loadWelcomeBg();

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
        root.add(adminLogin(), "ADMIN_LOGIN");
        root.add(adminDashboard(), "ADMIN_DASH");

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
        JPanel p = new JPanel(new GridBagLayout()) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (WELCOME_BG != null) {
                    Image img = WELCOME_BG.getImage();
                    int iw = img.getWidth(this);
                    int ih = img.getHeight(this);
                    if (iw > 0 && ih > 0) {
                        int w = getWidth();
                        int h = (int) (ih * (w / (double) iw));
                        int y = (getHeight() - h) / 2; // center vertically
                        g.drawImage(img, 0, y, w, h, this);
                    }
                }
            }
        };
        p.setBackground(BG);
        p.setBorder(new EmptyBorder(24, 24, 24, 24));

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
        b3.addActionListener(e -> card.show(root,"ADMIN_LOGIN"));

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

    private ImageIcon loadWelcomeBg() {
        try {
            URL url = getClass().getResource("/img/bg.jpeg");
            if (url != null) return new ImageIcon(url);
        } catch (Exception ignored) { }
        // Fallback to relative file when running from IDE without resources copied
        File fallback = new File("img/bg.jpeg");
        if (fallback.exists()) return new ImageIcon(fallback.getAbsolutePath());
        return null;
    }

    private JPanel register() {
        JPanel p = page();
        JPanel c = cardPanel();
        c.setPreferredSize(new Dimension(640, 420));

        JLabel h = new JLabel("Create a Taxpayer Account");
        h.setFont(H2);
        h.setAlignmentX(Component.LEFT_ALIGNMENT);

        JComboBox<String> type = new JComboBox<>(new String[]{"Individual","Company"});
        type.setFont(UI);
        type.setBorder(BorderFactory.createLineBorder(new Color(220, 222, 230)));

        JTextField id = textField();
        JLabel idLabel = label("Taxpayer ID:");
        JTextField name = textField();
        JTextField email = textField();
        JPasswordField pass = passwordField();

        JButton sign = primaryBtn("Sign Up");
        JButton back = ghostBtn("Back");

        JPanel form = new JPanel(new GridLayout(5,2,12,12));
        form.setOpaque(false);
        form.setAlignmentX(Component.LEFT_ALIGNMENT);

        form.add(label("Type:"));         form.add(type);
        form.add(idLabel);                 form.add(id);
        form.add(label("Name:"));         form.add(name);
        form.add(label("Email:"));        form.add(email);
        form.add(label("Password:"));     form.add(pass);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actions.setOpaque(false);
        actions.setAlignmentX(Component.LEFT_ALIGNMENT);
        actions.add(back);
        actions.add(sign);

        sign.addActionListener(e -> {
            String tid = id.getText().trim();
            String nm  = name.getText().trim();
            String emailTxt = email.getText().trim();
            String tp  = type.getSelectedItem().toString();
            String pw  = new String(pass.getPassword()).trim();

            if (tid.isEmpty() || nm.isEmpty() || emailTxt.isEmpty() || pw.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Fill all fields!");
                return;
            }

            if (!emailTxt.contains("@") || !emailTxt.contains(".")) {
                JOptionPane.showMessageDialog(this, "Enter a valid email address!");
                return;
            }

            if (TaxPayerDAO.existsTaxpayer(tid)) {
                JOptionPane.showMessageDialog(this, "This ID/registration number already exists. Try 1002, 1003, ...");
                return;
            }

                TaxPayer taxpayer = tp.equalsIgnoreCase("Company")
                    ? new Company(tid, nm, emailTxt, 0)
                    : new Individual(tid, nm, emailTxt, 0);

            boolean ok = TaxPayerDAO.insertTaxPayer(taxpayer, pw);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Sign Up successful! Now login.");
                id.setText(""); name.setText(""); email.setText(""); pass.setText("");
                card.show(root,"LOGIN");
            } else {
                JOptionPane.showMessageDialog(this, "Sign Up failed (DB). Check console + table columns.");
            }
        });

        back.addActionListener(e -> card.show(root,"WELCOME"));

        type.addActionListener(e -> {
            boolean isCompany = type.getSelectedItem().toString().equalsIgnoreCase("Company");
            idLabel.setText(isCompany ? "Registration Number:" : "Taxpayer ID:");
        });
        // Initialize correct label on load
        idLabel.setText(type.getSelectedItem().toString().equalsIgnoreCase("Company")
            ? "Registration Number:" : "Taxpayer ID:");

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

            // Temporary offline bypass when DB is unavailable
            if (tid.equalsIgnoreCase("demo") && pw.equals("demo")) {
                loggedUser = new Individual("DEMO", "Demo User", "demo@example.com", 50000);
                JOptionPane.showMessageDialog(this, "Logged in with demo account (no DB).");
                refreshHistory();
                card.show(root,"DASH");
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

        filingsTable.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                int row = filingsTable.rowAtPoint(e.getPoint());
                int col = filingsTable.columnAtPoint(e.getPoint());
                if (row >= 0 && col == 0) {
                    Object val = filingsTable.getValueAt(row, 0);
                    if (val != null) {
                        String filingId = val.toString();
                        Toolkit.getDefaultToolkit().getSystemClipboard()
                                .setContents(new StringSelection(filingId), null);
                        JOptionPane.showMessageDialog(CardTaxFilingSystem.this,
                                "Copied Filing ID: " + filingId);
                    }
                }
            }
        });

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

                JPanel infoPanel = new JPanel(new BorderLayout(8,8));
                infoPanel.add(new JLabel("Filing created. ID:"), BorderLayout.NORTH);

                JPanel idRow = new JPanel(new BorderLayout(6,0));
                JTextField idField = new JTextField(filingId);
                idField.setEditable(false);
                JButton copyBtn = new JButton("Copy ID");
                copyBtn.addActionListener(ev -> {
                Toolkit.getDefaultToolkit().getSystemClipboard()
                    .setContents(new StringSelection(filingId), null);
                copyBtn.setText("Copied");
                copyBtn.setEnabled(false);
                });
                idRow.add(idField, BorderLayout.CENTER);
                idRow.add(copyBtn, BorderLayout.EAST);

                infoPanel.add(idRow, BorderLayout.CENTER);
                infoPanel.add(new JLabel("Tax: " + f.getTaxAmount()), BorderLayout.SOUTH);

                int choice = JOptionPane.showConfirmDialog(
                    this,
                    infoPanel,
                    "Payment",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.INFORMATION_MESSAGE
                );

            if (choice == JOptionPane.YES_OPTION) {
                String[] methods = {"Card","Bank","Cash"};
                JComboBox<String> methodBox = new JComboBox<>(methods);
                int opt = JOptionPane.showConfirmDialog(
                        this,
                        methodBox,
                        "Select payment method",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );

                if (opt == JOptionPane.OK_OPTION && methodBox.getSelectedItem() != null) {
                    String method = methodBox.getSelectedItem().toString();
                    Payment pay = new Payment("P" + System.currentTimeMillis(), f, method);
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
                        prs.getString("method")
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



    private JPanel adminLogin() {
        JPanel p = page();
        JPanel c = cardPanel();
        c.setPreferredSize(new Dimension(640, 280));

        JLabel h = new JLabel("Admin Login");
        h.setFont(H2);
        h.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField user = textField();
        JPasswordField pass = passwordField();

        JButton login = primaryBtn("Login");
        JButton back = ghostBtn("Back");

        JPanel form = new JPanel(new GridLayout(2,2,12,12));
        form.setOpaque(false);
        form.setAlignmentX(Component.LEFT_ALIGNMENT);

        form.add(label("Admin Username:")); form.add(user);
        form.add(label("Admin Password:")); form.add(pass);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actions.setOpaque(false);
        actions.add(back);
        actions.add(login);

        login.addActionListener(e -> {
            String u = user.getText().trim();
            String pw = new String(pass.getPassword()).trim();

            if (u.isEmpty() || pw.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter admin username & password!");
                return;
            }

            Admin a = AdminDAO.login(u, pw);
            if (a == null) {
                JOptionPane.showMessageDialog(this, "Admin login failed!");
                return;
            }

            user.setText(""); pass.setText("");
            refreshAdminData();
            card.show(root, "ADMIN_DASH");
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

    private JPanel adminDashboard() {
        JPanel p = new JPanel(new BorderLayout(12, 12));
        p.setBackground(BG);
        p.setBorder(new EmptyBorder(14, 14, 14, 14));

        // Ref Helper
        Runnable updateStatus = () -> {
            int row = -1;
            JTable t = null;
            // Find the table in the viewport
            for (Component c : p.getComponents()) {
                if (c instanceof JPanel) { // Center panel
                    for (Component cc : ((JPanel) c).getComponents()) {
                        if (cc instanceof JScrollPane) {
                            JViewport vp = ((JScrollPane) cc).getViewport();
                             if (vp.getView() instanceof JTable) {
                                 t = (JTable) vp.getView();
                                 row = t.getSelectedRow();
                                 break;
                             }
                        }
                    }
                }
            }
        };

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 232, 240)),
                new EmptyBorder(10, 12, 10, 12)
        ));

        JLabel title = new JLabel("Admin Dashboard");
        title.setFont(H2);
        JButton logout = ghostBtn("Logout");
        logout.addActionListener(e -> card.show(root, "WELCOME"));
        header.add(title, BorderLayout.WEST);
        header.add(logout, BorderLayout.EAST);

        // Table
        JTable table = niceTable(adminModel);
        JScrollPane scroll = new JScrollPane(table);

        // Actions Bottom
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.setBackground(BG);

        JButton approve = primaryBtn("Approve Selected");
        JButton decline = ghostBtn("Decline / Flag");
        // Styling helper for decline to look red-ish if desired, but stick to theme for now
        
        approve.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Select a filing first!");
                return;
            }
            String fid = (String) adminModel.getValueAt(row, 0);
            String currentStatus = (String) adminModel.getValueAt(row, 4);

            if ("Approved".equalsIgnoreCase(currentStatus)) {
                JOptionPane.showMessageDialog(this, "Already approved.");
                return;
            }

            boolean ok = TaxFilingDAO.updateFilingStatus(fid, "Approved");
            if (ok) {
                JOptionPane.showMessageDialog(this, "Marked as Approved.");
                refreshAdminData();
            } else {
                JOptionPane.showMessageDialog(this, "Database update failed.");
            }
        });

        decline.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Select a filing first!");
                return;
            }
            String fid = (String) adminModel.getValueAt(row, 0);
            boolean ok = TaxFilingDAO.updateFilingStatus(fid, "Declined");
            if (ok) {
                JOptionPane.showMessageDialog(this, "Marked as 'Declined'.");
                refreshAdminData();
            }
        });

        footer.add(decline);
        footer.add(approve);

        p.add(header, BorderLayout.NORTH);
        p.add(scroll, BorderLayout.CENTER);
        p.add(footer, BorderLayout.SOUTH);

        return p;
    }

    private void refreshAdminData() {
        adminModel.setRowCount(0);
        try (ResultSet rs = TaxFilingDAO.getAllFilingsWithDetails()) {
             if (rs != null) {
                 while (rs.next()) {
                     adminModel.addRow(new Object[]{
                         rs.getString("filingID"),
                         rs.getString("name"),
                         rs.getString("email"),
                         rs.getDouble("taxAmount"),
                         rs.getString("status"),
                         rs.getDate("filingDate")
                     });
                 }
             }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
