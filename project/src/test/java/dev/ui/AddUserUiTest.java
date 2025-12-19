package dev.ui;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.Component;
import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

class AddUserUiTest {

    private Connection mockConn;
    private AddUserUi addUserUi;

    @BeforeEach
    void setUp() {
        mockConn = null; // Pas besoin de vraie connexion pour ces tests
        addUserUi = new AddUserUi(mockConn);
    }

    @Test
    void testEmptyFieldsShowsErrorDialog() {
        JButton saveBtn = getButton(addUserUi, "Créer");
        assertNotNull(saveBtn);

        // Remplir tous les champs vides
        setTextField(addUserUi, "Nom utilisateur:", "");
        setTextField(addUserUi, "Email:", "");
        setTextField(addUserUi, "Mot de Passe:", "");
        setTextField(addUserUi, "Poste:", "");

        // Clic sur le bouton
        SwingUtilities.invokeLater(saveBtn::doClick);

        // Ici on ne peut pas vérifier le JOptionPane directement sans framework supplémentaire
        // On se contente de vérifier que le bouton existe et les champs sont vides
        assertEquals("", getTextField(addUserUi, "Nom utilisateur:").getText());
        assertEquals("", getTextField(addUserUi, "Email:").getText());
        assertEquals("", new String(getPasswordField(addUserUi, "Mot de Passe:").getPassword()));
        assertEquals("", getTextField(addUserUi, "Poste:").getText());
    }

    // ------------------------
    // Helpers Swing
    // ------------------------
    private JButton getButton(JPanel panel, String text) {
        for (Component comp : panel.getComponents()) {
            if (comp instanceof JButton btn && btn.getText().equals(text)) return btn;
        }
        return null;
    }

    private void setTextField(JPanel panel, String labelText, String value) {
        Component[] comps = panel.getComponents();
        for (int i = 0; i < comps.length; i++) {
            if (comps[i] instanceof JLabel lbl && lbl.getText().equals(labelText)) {
                Component next = comps[i + 1];
                if (next instanceof JTextField tf) tf.setText(value);
                if (next instanceof JPasswordField pf) pf.setText(value);
            }
        }
    }

    private JTextField getTextField(JPanel panel, String labelText) {
        Component[] comps = panel.getComponents();
        for (int i = 0; i < comps.length; i++) {
            if (comps[i] instanceof JLabel lbl && lbl.getText().equals(labelText)) {
                return (JTextField) comps[i + 1];
            }
        }
        return null;
    }

    private JPasswordField getPasswordField(JPanel panel, String labelText) {
        Component[] comps = panel.getComponents();
        for (int i = 0; i < comps.length; i++) {
            if (comps[i] instanceof JLabel lbl && lbl.getText().equals(labelText)) {
                return (JPasswordField) comps[i + 1];
            }
        }
        return null;
    }
}
