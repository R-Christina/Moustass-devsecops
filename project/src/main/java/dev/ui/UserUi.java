package dev.ui;

import javax.swing.*;
import java.awt.*;

public class UserUi {

    private JFrame frame;

    public UserUi() {
        // ===== FENÊTRE =====
        frame = new JFrame("Espace Utilisateur");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        // ===== HEADER =====
        JPanel header = new JPanel();
        header.setBackground(new Color(125, 41, 219));
        header.setPreferredSize(new Dimension(0, 60));

        JLabel title = new JLabel("Espace Utilisateur", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));

        header.add(title);
        frame.add(header, BorderLayout.NORTH);

        // ===== PANEL BAS AVEC BOUTONS =====
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 20));
        bottomPanel.setBackground(Color.WHITE);

        JButton uploadBtn = new JButton("Upload Files");
        uploadBtn.setPreferredSize(new Dimension(200, 45));
        uploadBtn.setBackground(new Color(46, 204, 113));
        uploadBtn.setForeground(Color.WHITE);
        uploadBtn.setFocusPainted(false);

        JButton downloadBtn = new JButton("Download Files");
        downloadBtn.setPreferredSize(new Dimension(200, 45));
        downloadBtn.setBackground(new Color(41, 128, 185));
        downloadBtn.setForeground(Color.WHITE);
        downloadBtn.setFocusPainted(false);

        bottomPanel.add(uploadBtn);
        bottomPanel.add(downloadBtn);

        frame.add(bottomPanel, BorderLayout.SOUTH);

        // ===== ESPACE CENTRAL BLANC =====
        JPanel center = new JPanel();
        center.setBackground(Color.WHITE);
        frame.add(center, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    // Facultatif : pour lancer directement
    public static void main(String[] args) {
        new UserUi();
    }
}
