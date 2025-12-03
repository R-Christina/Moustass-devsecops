package dev.ui;

import javax.swing.*;

import dev.back.hash.Hash256;
import dev.unit.Connexion;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Login extends JFrame {

    public Login() {
        super("Authentification");

        // --------- GLOBAL ---------
        setSize(500, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        // Police moderne
        UIManager.put("Label.font", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("Button.font", new Font("Segoe UI", Font.BOLD, 14));

        // --------- HEADER ---------
        JPanel header = new JPanel();
        header.setBackground(new Color(125, 41, 219));
        header.setPreferredSize(new Dimension(500, 70));

        JLabel title = new JLabel("Connexion");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        header.add(title);

        // --------- CARD PANEL (Centre) ---------
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Champ NOM
        JLabel labelNom = new JLabel("Nom d'utilisateur :");
        JTextField inputNom = new JTextField(15);
        styleTextField(inputNom);

        gbc.gridx = 0;
        gbc.gridy = 0;
        card.add(labelNom, gbc);
        gbc.gridx = 1;
        card.add(inputNom, gbc);

        // Champ Mot de passe
        JLabel labelPass = new JLabel("Mot de passe :");
        JPasswordField inputPass = new JPasswordField(15);
        styleTextField(inputPass);

        gbc.gridx = 0;
        gbc.gridy = 1;
        card.add(labelPass, gbc);
        gbc.gridx = 1;
        card.add(inputPass, gbc);

        // --------- BOUTON MODERNE ---------
        JButton loginButton = new JButton("Se connecter");
        loginButton.setBackground(new Color(125, 41, 219));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.setPreferredSize(new Dimension(200, 40));

        // Effet hover
        loginButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                loginButton.setBackground(new Color(30, 105, 200));
            }

            public void mouseExited(MouseEvent e) {
                loginButton.setBackground(new Color(40, 120, 220));
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        card.add(loginButton, gbc);

        loginButton.addActionListener(e -> {
            String username = inputNom.getText();
            String password = new String(inputPass.getPassword());
            String hashed = Hash256.hashPassword(password);
            try (Connection conn = Connexion.getConnection()) {
                String sql = "SELECT is_admin FROM User WHERE user_name = ? AND password_hash = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, username);
                stmt.setString(2, hashed);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    int isAdmin = rs.getInt("is_admin");
                    if (isAdmin == 0) {
                        new AdminUi();
                    } else {
                        new UserUi(); // à créer
                    }
                    dispose(); // ferme la fenêtre login
                } else {
                    JOptionPane.showMessageDialog(this, "Nom ou mot de passe incorrect.", "Erreur",
                            JOptionPane.ERROR_MESSAGE);
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }

        });

        // --------- CONTENEUR CENTRAL (pour effet shadow) ---------
        JPanel centerWrapper = new JPanel();
        centerWrapper.setBackground(new Color(230, 230, 230));
        centerWrapper.setLayout(new GridBagLayout());

        JPanel shadow = createShadowPanel(card);
        centerWrapper.add(shadow);

        // --------- ADD TO FRAME ---------
        setLayout(new BorderLayout());
        add(header, BorderLayout.NORTH);
        add(centerWrapper, BorderLayout.CENTER);

        setVisible(true);
    }

    // STYLE TEXTFIELD (arrondi)
    private void styleTextField(JTextField field) {
        field.setPreferredSize(new Dimension(200, 30));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        field.setBackground(new Color(250, 250, 250));
    }

    // FAKE SHADOW PANEL
    private JPanel createShadowPanel(JPanel content) {
        JPanel shadow = new JPanel();
        shadow.setLayout(new BorderLayout());
        shadow.setBackground(new Color(0, 0, 0, 30)); // Ombre légère
        shadow.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        shadow.add(content, BorderLayout.CENTER);
        return shadow;
    }

    public static void main(String[] args) {
        new Login();
    }
}
