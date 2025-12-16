package dev.unit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connexion {

    private static final String URL = "jdbc:mysql://localhost:3306/moustass";
    private static final String USER = "root";
    private static final String PASSWORD = System.getenv("DB_PASSWORD");

    public static Connection getConnection() throws Exception 
    {
        try 
        {
            System.out.println("DB_PASSWORD = " + System.getenv("DB_PASSWORD"));
            return DriverManager.getConnection(URL, USER, PASSWORD);

        } catch (SQLException e) 
        {
            throw new Exception(
                "No connexion Moustass : " + e.getMessage()
            );
        }
    }
}