package dev.ui;

import dev.ui.AddUserUi;
import dev.unit.Connexion;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;

public class AdminUi 
{

    public static void main(String[] args) 
    {

        // ===== CONNEXION À LA BASE =====
        final Connection conn; // ⬅ final = corrige le warning

        try 
        {
            conn = Connexion.getConnection();
        } catch (Exception ex) 
        {
            JOptionPane.showMessageDialog(
                    null,
                    "Impossible de se connecter à la base de données.\n" + ex.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE
            );
            return; // stop l'exécution
        }

        // ===== FENÊTRE =====
        JFrame frame = new JFrame("Administrateur");
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

        // Upload / Download
        JPanel topRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 15));
        topRight.setOpaque(false);

        JLabel upload = new JLabel("Upload");
        upload.setForeground(Color.WHITE);

        JLabel download = new JLabel("Download");
        download.setForeground(Color.WHITE);

        topRight.add(upload);
        topRight.add(download);
        header.add(topRight, BorderLayout.EAST);

        frame.add(header, BorderLayout.NORTH);

        // ===== CENTER PANEL =====
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.WHITE);
        frame.add(centerPanel, BorderLayout.CENTER);

        // ===== SIDEBAR =====
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(200, 0));
        sidebar.setBackground(new Color(245, 245, 245));
        sidebar.setLayout(new GridLayout(10, 1, 0, 8));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        // ===== MENU BOUTONS =====
        JButton btnListe = menuButton("Liste Fichiers");
        JButton btnCreateUser = menuButton("Créer Utilisateur");
        JButton btnLogs = menuButton("Logs");

        sidebar.add(btnListe);
        sidebar.add(btnCreateUser);
        sidebar.add(btnLogs);

        frame.add(sidebar, BorderLayout.WEST);

        // ===== ACTIONS =====

        // --- Page : Créer un utilisateur
        btnCreateUser.addActionListener(e -> {
            centerPanel.removeAll();
            centerPanel.add(new AddUserUi(conn), BorderLayout.CENTER);  // ✔ OK
            centerPanel.revalidate();
            centerPanel.repaint();
        });

        // --- Page : Liste des fichiers
        btnListe.addActionListener(e -> {
            centerPanel.removeAll();
            centerPanel.add(new JLabel("Page : Liste des fichiers"), BorderLayout.CENTER);
            centerPanel.revalidate();
            centerPanel.repaint();
        });

        // --- Page : Logs
        btnLogs.addActionListener(e -> {
            centerPanel.removeAll();
            centerPanel.add(new JLabel("Page : Logs"), BorderLayout.CENTER);
            centerPanel.revalidate();
            centerPanel.repaint();
        });

        frame.setVisible(true);
    }

    // ----- STYLE DU MENU -----
    private static JButton menuButton(String text) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setBackground(new Color(245, 245, 245));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }
}
