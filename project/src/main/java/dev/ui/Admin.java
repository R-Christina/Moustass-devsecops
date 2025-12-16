
package dev.ui;

import javax.swing.*;
import java.awt.*;
import javax.swing.JFrame;
// import javax.swing.JScrollPane;
// import javax.swing.JTable;

public class Admin {
    

    public static void main(String[] args) {

        // ===== FENÊTRE PRINCIPALE =====
        JFrame frame = new JFrame("Administrateur");
        frame.setSize(600, 480);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setBackground(new Color(245, 245, 245)); // fond doux
        frame.setLayout(new BorderLayout());

        // ===== TITRE =====
        JLabel title = new JLabel("Espace Administrateur", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        frame.add(title, BorderLayout.NORTH);

        // ===== PANNEAU FORMULAIRE =====
        JPanel formPanel = new JPanel();
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        formPanel.setLayout(new GridLayout(5, 2, 15, 15));

        // Encadrer le formulaire dans un panneau arrondi
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(new Color(255, 255, 255));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        card.add(formPanel);

        frame.add(card, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(new Color(245, 245, 245));

        JButton uploadBtn = new JButton("Upload");
        JButton downloadBtn = new JButton("Download");
        JButton saveBtn = new JButton("Login");



        // Style boutons
        JButton[] buttons = {uploadBtn, downloadBtn, saveBtn};
        for (JButton btn : buttons) {
            btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
            btn.setPreferredSize(new Dimension(130, 40));
            btn.setBackground(new Color(66, 133, 244));
            btn.setFocusPainted(false);
            btn.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        }
        
        buttonPanel.add(uploadBtn);
        buttonPanel.add(downloadBtn);
        buttonPanel.add(saveBtn);

        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }
}