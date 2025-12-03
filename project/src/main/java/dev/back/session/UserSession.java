package dev.back.session;

public class UserSession {

    private static int userId;
    private static String username;
    private static String email;
    private static boolean isAdmin;

    public static void setSession(int id, String name, String mail, boolean admin) {
        userId = id;
        username = name;
        email = mail;
        isAdmin = admin;
    }

    public static int getUserId() {
        return userId;
    }

    public static String getUsername() {
        return username;
    }

    public static boolean isAdmin() {
        return isAdmin;
    }

    public static String getEmail() {
        return email;
    }

    public static void clear() {
        userId = 0;
        username = null;
        email = null;
        isAdmin = false;
    }
}
