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
    public static final String DOWNLOAD = "DOWNLOAD";

    public Download(Connection conn) {
        this.conn = conn;
    }

    public String downloadFileMessage(int idUser, String fileName, String destFolder) {
        String message;
        try {
            String sql = "SELECT file_hash, signatur, public_key FROM signature WHERE file_name=?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, fileName);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next())
                        throw new Exception("Aucune signature trouvée.");

                    String hashSaved = rs.getString("file_hash");
                    String signature = rs.getString("signatur");
                    String publicKeyPEM = rs.getString("public_key");

                    PublicKey publicKey = RSAUtil.loadPublicKey(publicKeyPEM);

                    boolean signatureOk = RSAUtil.verify(hashSaved, signature, publicKey);
                    if (!signatureOk) {
                        message = "SIGNATURE CORROMPUE EN BASE";
                        LoggerUtil.log(conn, DOWNLOAD, message, idUser, fileName);
                        return message;
                    }

                    File currentFile = new File("storage/files_brut/" + fileName);
                    if (!currentFile.exists())
                        throw new Exception("Fichier physique introuvable dans storage.");

                    String currentHash = FileHash.computeSHA256(currentFile);
                    if (!hashSaved.equals(currentHash)) {
                        message = "FICHIER MODIFIÉ DEPUIS SIGNATURE ! IMPOSSIBLE DE TELECHARGER";
                        LoggerUtil.log(conn, DOWNLOAD, message, idUser, fileName);
                        return message;
                    }

                    // Tout est OK
                    Files.copy(currentFile.toPath(), new File(destFolder + "/" + fileName).toPath(),
                            StandardCopyOption.REPLACE_EXISTING);
                    message = "Téléchargement OK + signature valide.";
                    LoggerUtil.log(conn, DOWNLOAD, "SIGNATURE OK - Fichier intact", idUser, fileName);
                    return message;
                }
            }

        } catch (Exception e) {
            message = "ERREUR: " + e.getMessage();
            LoggerUtil.log(conn, DOWNLOAD, message, idUser, fileName);
            return message;
        }
    }
}
