package dev.back.crypto;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.*;
import java.util.Base64;
import java.nio.file.Files;
import java.nio.file.Path;

// Lire une clé privée depuis un fichier PEM
// Lire une clé publique depuis la base
// Signer un hash
// Vérifier une signature

public class RSAUtil 
{

    // Lire la clé privée d’un fichier PEM
    public static PrivateKey loadPrivateKey(String pemPath) throws Exception {
        String keyPem = Files.readString(Path.of(pemPath));

        String cleanPem = keyPem
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replace("\n", "")
                .trim();

        byte[] decoded = Base64.getDecoder().decode(cleanPem);

        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decoded);
        KeyFactory factory = KeyFactory.getInstance("RSA");

        return factory.generatePrivate(keySpec);
    }

    // Charger la clé publique en PEM depuis la BDD
    public static PublicKey loadPublicKey(String pem) throws Exception 
    {
        String cleanPem = pem
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replace("\n", "")
                .trim();

        byte[] decoded = Base64.getDecoder().decode(cleanPem);

        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decoded);
        KeyFactory factory = KeyFactory.getInstance("RSA");

        return factory.generatePublic(keySpec);
    }

    // Signer un hash SHA-256
    public static String sign(String hash, PrivateKey privateKey) throws Exception {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(hash.getBytes(StandardCharsets.UTF_8));

        return Base64.getEncoder().encodeToString(signature.sign());
    }

    // Vérifier une signature
    public static boolean verify(String hash, String signatureBase64, PublicKey publicKey) throws Exception {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initVerify(publicKey);
        signature.update(hash.getBytes(StandardCharsets.UTF_8));

        return signature.verify(Base64.getDecoder().decode(signatureBase64));
    }
}
