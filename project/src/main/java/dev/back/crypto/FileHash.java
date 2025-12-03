package dev.back.crypto;

import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;

public class FileHash 
{
    // hashage du files pour envoie dans la base
    public static String sha256(String filePath) throws Exception 
    {
        byte[] fileBytes = Files.readAllBytes(Path.of(filePath));

        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(fileBytes);

        StringBuilder hex = new StringBuilder();
        for (byte b : hashBytes) {
            String h = Integer.toHexString(0xff & b);
            if (h.length() == 1) hex.append('0');
            hex.append(h);
        }

        return hex.toString();
    }
}