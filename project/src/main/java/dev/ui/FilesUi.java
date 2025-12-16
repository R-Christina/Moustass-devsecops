package dev.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class FilesUi extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;

    public FilesUi(Connection conn) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Colonnes simplifiées
        String[] columns = {"ID Utilisateur", "Nom du fichier", "Date/Heure"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // tableau non éditable
            }
        };

        table = new JTable(tableModel);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(600, 400));

        // Panel centré
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerPanel.setBackground(Color.WHITE);
        centerPanel.add(scrollPane);

        add(centerPanel, BorderLayout.CENTER);

        // Charger les fichiers depuis la base
        loadFiles(conn);
    }

    private void loadFiles(Connection conn) {
        String query = "SELECT user_name, file_name, signed_at " +
               "FROM signature " +
               "LEFT JOIN user ON user.id_user = signature.id_user " +
               "ORDER BY signed_at DESC";


        try (PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            tableModel.setRowCount(0); // vider le tableau
            while (rs.next()) {
                Object[] row = new Object[]{
                        rs.getString("user_name"),
                        rs.getString("file_name"),
                        rs.getTimestamp("signed_at")
                };
                tableModel.addRow(row);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erreur lors du chargement des fichiers : " + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
