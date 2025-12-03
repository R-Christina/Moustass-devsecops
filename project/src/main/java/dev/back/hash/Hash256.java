package dev.back.hash;

import java.io.FileInputStream;
import java.security.MessageDigest;
import java.io.File;

public class Hash256 
{

    // Hash SHA-256 mdp en hexadécimal
    public static String hashPassword(String password) 
    {
        try 
        {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            byte[] hashBytes = digest.digest(password.getBytes("UTF-8"));

            StringBuilder hex = new StringBuilder();
            for (byte b : hashBytes) {
                String hexPart = Integer.toHexString(0xff & b);
                if (hexPart.length() == 1) hex.append('0');
                hex.append(hexPart);
            }

            return hex.toString();

        } catch (Exception e) 
        {
            throw new RuntimeException("Erreur hash SHA-256 : " + e.getMessage());
        }
    }

}
