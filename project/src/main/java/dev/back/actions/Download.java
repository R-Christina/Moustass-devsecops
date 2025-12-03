package dev.back.actions;

import dev.back.crypto.FileHash;
import dev.back.crypto.RSAUtil;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.security.PublicKey;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Download {

    private Connection conn;

    public Download(Connection conn) {
        this.conn = conn;
    }

    public String downloadFileMessage(int idUser, String fileName, String destFolder) {
        String message;
        try {
            // Récupération signature, hash, etc. comme avant...
            String sql = "SELECT file_hash, signatur, public_key FROM signature WHERE file_name=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, fileName);
            ResultSet rs = ps.executeQuery();

            if (!rs.next())
                throw new Exception("Aucune signature trouvée.");

            String hashSaved = rs.getString("file_hash");
            String signature = rs.getString("signatur");
            String publicKeyPEM = rs.getString("public_key");

            PublicKey publicKey = RSAUtil.loadPublicKey(publicKeyPEM);

            boolean signatureOk = RSAUtil.verify(hashSaved, signature, publicKey);
            if (!signatureOk) {
                message = "SIGNATURE CORROMPUE EN BASE";
                logAction("DOWNLOAD", message, idUser, fileName);
                return message;
            }

            File currentFile = new File("storage/files_brut/" + fileName);
            if (!currentFile.exists())
                throw new Exception("Fichier physique introuvable dans storage.");

            String currentHash = FileHash.computeSHA256(currentFile);

            boolean fileIsIntact = hashSaved.equals(currentHash);
            if (!fileIsIntact) {
                message = "FICHIER MODIFIÉ DEPUIS SIGNATURE ! IMPOSSIBLE DE TELECHARGER";
                logAction("DOWNLOAD", message, idUser, fileName);
                return message;
            }

            // Tout est OK
            Files.copy(currentFile.toPath(), new File(destFolder + "/" + fileName).toPath(),
                    StandardCopyOption.REPLACE_EXISTING);
            message = "Téléchargement OK + signature valide.";
            logAction("DOWNLOAD", "SIGNATURE OK - Fichier intact", idUser, fileName);
            return message;

        } catch (Exception e) {
            try {
                message = "ERREUR: " + e.getMessage();
                logAction("DOWNLOAD", message, idUser, fileName);
            } catch (Exception logEx) {
                message = "Erreur de log : " + logEx.getMessage();
            }
            return message;
        }
    }

    private void logAction(String action, String result, int idUser, String fileName) throws Exception {
        String sql = "INSERT INTO logs (name_user, act, date_time, result) VALUES (?, ?, NOW(), ?)";

        PreparedStatement psUser = conn.prepareStatement("SELECT user_name FROM User WHERE id_user=?");
        psUser.setInt(1, idUser);
        ResultSet rs = psUser.executeQuery();
        rs.next();

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, rs.getString("user_name"));
        ps.setString(2, action + " fichier : " + fileName);
        ps.setString(3, result);
        ps.executeUpdate();
    }
}
