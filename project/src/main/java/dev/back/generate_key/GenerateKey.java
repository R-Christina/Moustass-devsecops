package dev.back.generate_key;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;
import java.io.File;
import java.io.FileOutputStream;

public class GenerateKey 
{

    private static final String STORAGE_BASE_PATH = "storage/private_key_user/"; 

    // Générer une paire RSA 2048 bits
    public static KeyPair generateRSAKeyPair() 
    {
        try 
        {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(2048, new SecureRandom());
            return generator.generateKeyPair();
        } catch (Exception e) 
        {
            throw new RuntimeException("Erreur génération clés RSA : " + e.getMessage());
        }
    }

    // Clé publique en PEM (pour stocker dans la base)
    public static String publicKeyToPEM(PublicKey key) 
    {
        String base64 = Base64.getEncoder().encodeToString(key.getEncoded());
        return "-----BEGIN PUBLIC KEY-----\n" +
                base64 +
                "\n-----END PUBLIC KEY-----";
    }

    // Clé privée en PEM (pour le fichier)
    public static String privateKeyToPEM(PrivateKey key) 
    {
        String base64 = Base64.getEncoder().encodeToString(key.getEncoded());
        return "-----BEGIN PRIVATE KEY-----\n" +
                base64 +
                "\n-----END PRIVATE KEY-----";
    }

    // Sauvegarder la clé privée dans src/storage/private_key_user/
    public static void savePrivateKeyToFile(PrivateKey privateKey, int userId) 
    {
        try {

            // Préparation du chemin correct
            String directoryPath = STORAGE_BASE_PATH;
            String filePath = directoryPath + userId + "_key_private.pem";

            // Création auto du dossier s'il n'existe pas
            File directory = new File(directoryPath);
            if (!directory.exists()) {
                directory.mkdirs();   // Crée tous les dossiers nécessaires
            }

            // Conversion en PEM
            String pem = privateKeyToPEM(privateKey);

            // écriture dans le fichier .pem
            FileOutputStream fos = new FileOutputStream(filePath);
            fos.write(pem.getBytes(StandardCharsets.UTF_8));
            fos.close();

            System.out.println(" Clé privée sauvegardée : " + filePath);

        } catch (Exception e) {
            throw new RuntimeException("Erreur sauvegarde clé privée : " + e.getMessage());
        }
    }
}
