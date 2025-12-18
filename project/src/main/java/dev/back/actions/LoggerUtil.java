package dev.back.actions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;


public class LoggerUtil {
    
    private LoggerUtil() {
        // Empêche l'instanciation
    }

    private static final Logger logger = Logger.getLogger(LoggerUtil.class.getName());

    public static void log(Connection conn, String action, String result, int idUser, String fileName) {
        String username = "Unknown";

        // Récupérer nom du user
        String sqlUser = "SELECT user_name FROM User WHERE id_user=?";
        try (PreparedStatement psUser = conn.prepareStatement(sqlUser)) {
            psUser.setInt(1, idUser);
            try (ResultSet rs = psUser.executeQuery()) {
                if (rs.next()) username = rs.getString("user_name");
            }
        } catch (Exception ex) {
            logger.log(Level.WARNING, "Impossible de récupérer le nom de l'utilisateur id=" + idUser, ex);
        }

        // Insérer dans les logs
        String sqlLog = "INSERT INTO logs (name_user, act, date_time, result) VALUES (?, ?, NOW(), ?)";
        try (PreparedStatement psLog = conn.prepareStatement(sqlLog)) {
            psLog.setString(1, username);
            psLog.setString(2, action + " fichier : " + fileName);
            psLog.setString(3, result);
            psLog.executeUpdate();
        } catch (Exception ex) {
            logger.log(Level.WARNING, "Impossible d'écrire le log pour l'action " + action, ex);
        }
    }
}
