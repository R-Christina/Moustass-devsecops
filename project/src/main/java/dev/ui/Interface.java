package dev.ui;
import javax.swing.*;
import java.awt.*;

public class Interface extends JFrame {

    public Interface() {
        super("Authentification");

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 20, 5, 10);  // marges : top, left, bottom, right

        JLabel labelNom = new JLabel("NOM :");
        JTextField inputNom = new JTextField(12);

        //labelNom.setForeground(Color.BLUE); // CSS

        JLabel labelPass = new JLabel("MOT DE PASSE :");
        JPasswordField inputPass = new JPasswordField(12);

        //labelPass.setForeground(Color.YELLOW); // C'est comme un css

        // Ligne 1 : Label Nom
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.LINE_END; // pousse un peu vers la droite
        formPanel.add(labelNom, gbc);

        // Ligne 1 : Input Nom
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        formPanel.add(inputNom, gbc);

        // Ligne 2 : Label Mot de passe
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        formPanel.add(labelPass, gbc);

        // Ligne 2 : Input Mot de passe
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        formPanel.add(inputPass, gbc);

        // ----- PANEL DU BAS -----
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton loginButton = new JButton("Login");
        bottomPanel.add(loginButton);

        // ----- FRAME -----
        setLayout(new BorderLayout());
        add(formPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        setSize(500, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }

    public static void main(String[] args) {
        new Interface();
    }
}
