package dev.ui;

import dev.back.CreateUser;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;

public class AddUserUi extends JPanel 
{

    // On passe la connexion SQL via le constructeur
    public AddUserUi(Connection conn) 
    {

        setBackground(Color.WHITE);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // LABELS & INPUTS
        JLabel lblUser = new JLabel("Nom utilisateur:");
        JTextField txtUser = new JTextField(15);

        JLabel lblEmail = new JLabel("Email:");
        JTextField txtEmail = new JTextField(15);

        JLabel lblPassword = new JLabel("Mot de Passe:");
        JPasswordField txtPass = new JPasswordField(15);

        JLabel lblPoste = new JLabel("Poste:");
        JTextField txtPoste = new JTextField(15);

        // POSITIONNEMENT
        gbc.gridx = 0; gbc.gridy = 0;
        add(lblUser, gbc);
        gbc.gridx = 1;
        add(txtUser, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        add(lblEmail, gbc);
        gbc.gridx = 1;
        add(txtEmail, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        add(lblPassword, gbc);
        gbc.gridx = 1;
        add(txtPass, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        add(lblPoste, gbc);
        gbc.gridx = 1;
        add(txtPoste, gbc);

        // Bouton Save
        JButton saveBtn = new JButton("Créer");
        saveBtn.setPreferredSize(new Dimension(150, 35));

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(saveBtn, gbc);

        // ---------------------------------------------
        // ACTION DU BOUTON
        // ---------------------------------------------

        saveBtn.addActionListener(e -> {

            String username = txtUser.getText().trim();
            String email = txtEmail.getText().trim();
            String password = new String(txtPass.getPassword()).trim();
            String post = txtPoste.getText().trim();

            // Vérifications simples
            if (username.isEmpty() || email.isEmpty() || password.isEmpty() || post.isEmpty()) 
            {
                JOptionPane.showMessageDialog(this,
                        "Veuillez remplir tous les champs.",
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Appel CreateUser
            CreateUser cu = new CreateUser(conn);
            boolean success = cu.insertUser(username, email, password, post);

            if (success) {
                JOptionPane.showMessageDialog(this,
                        "Utilisateur créé avec succès !",
                        "Succès",
                        JOptionPane.INFORMATION_MESSAGE);

                // Réinitialiser les champs
                txtUser.setText("");
                txtEmail.setText("");
                txtPass.setText("");
                txtPoste.setText("");

            } else {
                JOptionPane.showMessageDialog(this,
                        "Erreur lors de la création de l'utilisateur.\nVérifiez si l'email n'existe pas déjà.",
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
