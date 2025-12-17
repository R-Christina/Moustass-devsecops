package dev.ui;

import dev.back.actions.Upload;
import dev.back.actions.Download;
import dev.back.session.UserSession;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserUi {

    private final JFrame frame;
    private final Connection conn;
    private final int userId;
    private final String username;

    private JTable table;
    private DefaultTableModel model;

    public UserUi(int userId, Connection conn) {
        this.conn = conn;
        this.userId = userId;
        this.username = UserSession.getUsername();

        frame = new JFrame("Espace Utilisateur - " + username);

        initUI();
        loadFiles();
    }

    // ===== INITIALISATION DE L'INTERFACE =====
    private void initUI() {
        frame.setSize(750, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        // ===== HEADER =====
        JPanel header = new JPanel();
        header.setBackground(new Color(125, 41, 219));
        header.setPreferredSize(new Dimension(0, 60));

        JLabel title = new JLabel("Bienvenue : " + username, SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        header.add(title);
        frame.add(header, BorderLayout.NORTH);

        // ===== BOTTOM PANEL =====
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 20));
        bottomPanel.setBackground(Color.WHITE);

        JButton uploadBtn = new JButton("Upload Files");
        uploadBtn.setPreferredSize(new Dimension(200, 45));
        uploadBtn.setBackground(new Color(46, 204, 113));
        uploadBtn.setForeground(Color.WHITE);
        bottomPanel.add(uploadBtn);

        frame.add(bottomPanel, BorderLayout.SOUTH);

        // ===== TABLE =====
        JPanel center = new JPanel(new BorderLayout());

        String[] cols = { "ID User", "Nom du fichier", "Date de signature", "Télécharger" };
        model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3; // seule la colonne bouton est éditable
            }
        };

        table = new JTable(model);
        table.getColumn("Télécharger").setCellRenderer(new ButtonRenderer());
        table.getColumn("Télécharger").setCellEditor(new ButtonEditor());

        center.add(new JScrollPane(table), BorderLayout.CENTER);
        frame.add(center, BorderLayout.CENTER);

        // ===== ACTIONS =====
        uploadBtn.addActionListener(e -> doUpload());

        frame.setVisible(true);
    }

    // ============================
    // CHARGER LES FICHIERS DE LA BDD
    // ============================
    private void loadFiles() {
        model.setRowCount(0);

        String sql = "SELECT id_user, file_name, signed_at FROM signature ORDER BY signed_at DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int uid = rs.getInt("id_user");
                String name = rs.getString("file_name");
                String date = rs.getString("signed_at");

                model.addRow(new Object[] { uid, name, date, "Download" });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ============================
    // UPLOAD
    // ============================
    private void doUpload() {
        JFileChooser fc = new JFileChooser();
        if (fc.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            Upload up = new Upload(conn);

            boolean ok = up.uploadFile(userId, file);

            JOptionPane.showMessageDialog(frame,
                    ok ? "Upload + signature OK." : "Échec upload",
                    ok ? "Succès" : "Erreur",
                    ok ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);

            if (ok) {
                loadFiles();
            }
        }
    }

    // ============================
    // CLASSES POUR LE BOUTON TÉLÉCHARGER
    // ============================
    class ButtonRenderer extends JButton implements TableCellRenderer {

        public ButtonRenderer() {
            setOpaque(true);
            setText("Download");
            setBackground(new Color(41, 128, 185));
            setForeground(Color.WHITE);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus,
                int row, int col) {
            return this;
        }
    }

    class ButtonEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {

        private final JButton button;
        private String fileName;
        private int fileOwnerId;

        public ButtonEditor() {
            button = new JButton("Download");
            button.setBackground(new Color(41, 128, 185));
            button.setForeground(Color.WHITE);
            button.addActionListener(this);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int col) {
            fileOwnerId = Integer.parseInt(table.getValueAt(row, 0).toString());
            fileName = table.getValueAt(row, 1).toString();
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return fileName;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fc = new JFileChooser();
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

            if (fc.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
                String folder = fc.getSelectedFile().getAbsolutePath();
                Download dl = new Download(conn);

                String message = dl.downloadFileMessage(fileOwnerId, fileName, folder);
                boolean ok = message.contains("Téléchargement OK");

                JOptionPane.showMessageDialog(frame,
                        message,
                        ok ? "Succès" : "Alerte",
                        ok ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.WARNING_MESSAGE);
            }

            fireEditingStopped();
        }
    }
}
