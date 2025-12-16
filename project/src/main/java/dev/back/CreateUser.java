package dev.back;

import dev.back.hash.Hash256;
import dev.back.generate_key.GenerateKey;

import java.security.KeyPair;
import java.sql.*;

//class pour
// Créer un utilisateur + generate key private & key public

public class CreateUser 
{

    private Connection conn;

    public CreateUser(Connection connection) 
    {
        this.conn = connection;
    }

    public boolean insertUser(String username, String email, String password, String post) {

        try 
        {
            //Hash du mot de passe
            String hashedPassword = Hash256.hashPassword(password);
//--------------------------------------------------------------------------
            // Génération du couple RSA
            KeyPair keyPair = GenerateKey.generateRSAKeyPair();

            String publicKeyPEM = GenerateKey.publicKeyToPEM(keyPair.getPublic());
//--------------------------------------------------------------------------

            // requete sql
            String sql = "INSERT INTO User (user_name, email, password_hash, post, public_key, created_at) "
                       + "VALUES (?, ?, ?, ?, ?, NOW())";

            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, username);
            ps.setString(2, email);
            ps.setString(3, hashedPassword);
            ps.setString(4, post);
            ps.setString(5, publicKeyPEM);

            int rowsInserted = ps.executeUpdate();
            if (rowsInserted == 0) {
                System.err.println("Échec insertion SQL.");
                return false;
            }

            // Récupération de l'id_user généré automatiquement
            ResultSet rs = ps.getGeneratedKeys();
            int idUser = -1;

            if (rs.next()) {
                idUser = rs.getInt(1);
            }

            if (idUser == -1) {
                System.err.println("Impossible de récupérer id_user.");
                return false;
            }

            // Sauvegarde de la clé privée dans /storage/private_key_user/
            GenerateKey.savePrivateKeyToFile(keyPair.getPrivate(), idUser);

            System.out.println("Utilisateur créé avec succès: id=" + idUser);
            return true;

        } catch (SQLIntegrityConstraintViolationException e) 
        {
            // email UNIQUE
            System.err.println("Email déjà utilisé : " + email);
            return false;

        } catch (Exception e) 
        {
            System.err.println("Erreur insertUser : " + e.getMessage());
            return false;
        }
    }
}
