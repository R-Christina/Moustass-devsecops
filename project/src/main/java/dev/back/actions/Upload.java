package dev.back.actions;

import dev.back.crypto.FileHash;
import dev.back.crypto.RSAUtil;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.security.PrivateKey;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Upload {

    private final Connection conn;

    public Upload(Connection conn) {
        this.conn = conn;
    }

    public boolean uploadFile(int idUser, File chosenFile) {

        try {
            // === Vérifier / créer dossier files_brut ===
            File destFolder = new File("storage/files_brut/");
            if (!destFolder.exists()) destFolder.mkdirs();

            // === Copier fichier brut ===
            String destPath = "storage/files_brut/" + chosenFile.getName();
            Files.copy(chosenFile.toPath(), new File(destPath).toPath(), StandardCopyOption.REPLACE_EXISTING);

            // === Hash du fichier ===
            String hash = FileHash.sha256(destPath);

            // === Charger clé privée ===
            String privateKeyPath = "storage/private_key_user/" + idUser + "_key_private.pem";


            File keyFile = new File(privateKeyPath);
            if (!keyFile.exists()) {
                System.err.println("ERREUR : Clé privée introuvable : " + privateKeyPath);
                logAction("UPLOAD", "ERROR: private key missing", idUser, chosenFile.getName());
                return false;
            }

            PrivateKey privateKey = RSAUtil.loadPrivateKey(privateKeyPath);

            // === Signature RSA ===
            String signature = RSAUtil.sign(hash, privateKey);

            // === Clé publique depuis BDD ===
            String sqlKey = "SELECT public_key FROM User WHERE id_user=?";
            PreparedStatement psKey = conn.prepareStatement(sqlKey);
            psKey.setInt(1, idUser);
            ResultSet rs = psKey.executeQuery();

            if (!rs.next()) {
                System.err.println(" ERREUR : Aucun utilisateur trouvé !");
                logAction("UPLOAD", "ERROR: user not found", idUser, chosenFile.getName());
                return false;
            }

            String publicKeyPEM = rs.getString("public_key");

            // === Sauvegarder signature en base ===
            String sql = "INSERT INTO signature (id_user, file_name, file_hash, signatur, public_key, signed_at) "
                       + "VALUES (?, ?, ?, ?, ?, NOW())";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idUser);
            ps.setString(2, chosenFile.getName());
            ps.setString(3, hash);
            ps.setString(4, signature);
            ps.setString(5, publicKeyPEM);
            ps.executeUpdate();

            // === Log OK ===
            logAction("UPLOAD", "OK", idUser, chosenFile.getName());

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            logAction("UPLOAD", "ERROR: " + e.getMessage(), idUser, chosenFile.getName());
            return false;
        }
    }


    // ======================================================
    // LOG SANS throws Exception (aucune erreur bloquante)
    // ======================================================
    private void logAction(String action, String result, int idUser, String fileName) {
        try {
            // Récupérer nom du user
            PreparedStatement psUser = conn.prepareStatement("SELECT user_name FROM User WHERE id_user=?");
            psUser.setInt(1, idUser);
            ResultSet rs = psUser.executeQuery();

            String username = rs.next() ? rs.getString("user_name") : "Unknown";

            String sql = "INSERT INTO logs (name_user, act, date_time, result) VALUES (?, ?, NOW(), ?)";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, action + " fichier : " + fileName);
            ps.setString(3, result);

            ps.executeUpdate();

        } catch (Exception ex) {
            ex.printStackTrace(); // On ne stoppe jamais l'upload pour un log
        }
    }
}
