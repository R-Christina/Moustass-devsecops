package dev.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LogsUi extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;

    public LogsUi(Connection conn) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Colonnes du tableau
        String[] columns = {"Utilisateur", "Action", "Date/Heure", "Résultat"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Empêche l'édition directe
            }
        };
        table = new JTable(tableModel);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Charger les logs depuis la base
        loadLogs(conn);
    }

    private void loadLogs(Connection conn) {
        String query = "SELECT name_user, act, date_time, result FROM logs ORDER BY date_time DESC";
        try (PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            tableModel.setRowCount(0); // vider le tableau avant ajout
            while (rs.next()) {
                Object[] row = new Object[]{
                        rs.getString("name_user"),
                        rs.getString("act"),
                        rs.getTimestamp("date_time"),
                        rs.getString("result")
                };
                tableModel.addRow(row);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erreur lors du chargement des logs : " + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}