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

    public Download(Connection conn) 
    {
        this.conn = conn;
    }

    public boolean downloadFile(int idUser, String fileName, String destFolder) {

        try {
            //Récupérer la signature du fichier
            String sql = "SELECT file_hash, signatur, public_key FROM signature WHERE file_name=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, fileName);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) throw new Exception("Aucune signature trouvée.");

            String hashSaved = rs.getString("file_hash");
            String signature = rs.getString("signatur");
            String publicKeyPEM = rs.getString("public_key");

            //Vérifier signature RSA
            PublicKey publicKey = RSAUtil.loadPublicKey(publicKeyPEM);

            boolean valid = RSAUtil.verify(hashSaved, signature, publicKey);

            //Copier le fichier
            String srcPath = "storage/files_brut/" + fileName;
            String destPath = destFolder + "/" + fileName;

            Files.copy(new File(srcPath).toPath(),
                       new File(destPath).toPath(),
                       StandardCopyOption.REPLACE_EXISTING);

            //Log
            logAction("DOWNLOAD", valid ? "SIGNATURE OK" : "SIGNATURE INVALID", idUser, fileName);

            return valid;

        } catch (Exception e) 
        {
            try {
                logAction("DOWNLOAD", "ERROR: " + e.getMessage(), idUser, fileName);
            } catch (Exception logEx) {
                System.err.println("Erreur logAction : " + logEx.getMessage());
            }
            return false;
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
