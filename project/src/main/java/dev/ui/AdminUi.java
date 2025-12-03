package dev.ui;

import dev.ui.AddUserUi;
import dev.unit.Connexion;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;

public class AdminUi {

    private JFrame frame;
    private JPanel centerPanel;
    private final Connection conn;
    private final int adminId;

    // ===== CONSTRUCTEUR OFFICIEL =====
    public AdminUi(int adminId, Connection conn) {
        this.adminId = adminId;
        this.conn = conn;
        initUI();
    }

    // ===== INTERFACE =====
    private void initUI() {

        // ===== FENÊTRE =====
        frame = new JFrame("Administrateur");
        frame.setSize(900, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        // ===== HEADER =====
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(125, 41, 219));
        header.setPreferredSize(new Dimension(0, 60));

        JLabel title = new JLabel("Admin", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        header.add(title, BorderLayout.CENTER);

        frame.add(header, BorderLayout.NORTH);

        // ===== CENTER PANEL =====
        centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.WHITE);
        frame.add(centerPanel, BorderLayout.CENTER);

        // ===== SIDEBAR =====
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(200, 0));
        sidebar.setBackground(new Color(245, 245, 245));
        sidebar.setLayout(new GridLayout(10, 1, 0, 10));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        JButton btnListe = menuButton("Liste Fichiers");
        JButton btnCreateUser = menuButton("Créer Utilisateur");
        JButton btnLogs = menuButton("Logs");

        sidebar.add(btnListe);
        sidebar.add(btnCreateUser);
        sidebar.add(btnLogs);

        frame.add(sidebar, BorderLayout.WEST);

        // ===== ACTIONS =====
        btnCreateUser.addActionListener(e -> showPage(new AddUserUi(conn)));

        btnListe.addActionListener(e -> {
            JLabel page = new JLabel("Page : Liste des fichiers", SwingConstants.CENTER);
            showPage(page);
        });

        btnLogs.addActionListener(e -> {
            JLabel page = new JLabel("Page : Logs", SwingConstants.CENTER);
            showPage(page);
        });

        frame.setVisible(true);
    }

    // ===== CHANGER DE PAGE =====
    private void showPage(Component comp) {
        centerPanel.removeAll();
        centerPanel.add(comp, BorderLayout.CENTER);
        centerPanel.revalidate();
        centerPanel.repaint();
    }

    // ===== STYLE BOUTONS MENUS =====
    private static JButton menuButton(String text) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setBackground(new Color(245, 245, 245));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // ===== MAIN TEMPORAIRE POUR TEST =====
    public static void main(String[] args) {

        try {
            Connection conn = Connexion.getConnection();
            int fakeAdminId = 1; // ID d’admin pour test

            new AdminUi(fakeAdminId, conn);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    null,
                    "Erreur DB : " + ex.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
