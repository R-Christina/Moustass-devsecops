package dev.ui;

import javax.swing.*;
import java.awt.*;

public class AddUserUi extends JPanel 
{

    public AddUserUi() 
    {
        setBackground(Color.WHITE);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // LABELS & INPUTS
        JLabel lblUser = new JLabel("User Name:");
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
        JButton saveBtn = new JButton("Enregistrer");
        saveBtn.setPreferredSize(new Dimension(150, 35));

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(saveBtn, gbc);
    }
}
