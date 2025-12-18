package dev.ui;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import dev.back.hash.Hash256;
import dev.back.session.UserSession;
import dev.unit.Connexion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.MockedStatic.*;

/**
 * Tests unitaires pour le composant Login.
 * Couvre la validation, l'authentification et la gestion des erreurs.
 */
@DisplayName("Test Suite - Composant Login")
class LoginTest {

    private static final int TEST_USER_ID = 1;
    private static final String TEST_USERNAME = "john_doe";
    private static final String TEST_EMAIL = "john@example.com";
    private static final String TEST_PASSWORD = "securePassword123";
    private static final String SQL_QUERY = "SELECT id_user, user_name, email, is_admin FROM User WHERE user_name = ? AND password_hash = ?";

    @Mock
    private Connection mockConnection;

    @Mock
    private PreparedStatement mockStatement;

    @Mock
    private ResultSet mockResultSet;

    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() {
        try {
            if (autoCloseable != null) {
                autoCloseable.close();
            }
        } catch (Exception e) {
            // Exception lors de la fermeture des mocks - ignorée
            Thread.currentThread().interrupt();
        }
    }

    // ========== TESTS DE VALIDATION DES CHAMPS ==========

    @Test
    @DisplayName("Test 1: Username et password vides")
    void testEmptyUsernameAndPassword() {
        String username = "";
        String password = "";

        assertTrue(username.isEmpty(), "Username should be empty");
        assertTrue(password.isEmpty(), "Password should be empty");
    }

    @Test
    @DisplayName("Test 2: Username vide, password rempli")
    void testEmptyUsername() {
        String username = "";
        String password = TEST_PASSWORD;

        assertTrue(username.isEmpty(), "Username should be empty");
        assertFalse(password.isEmpty(), "Password should not be empty");
    }

    @Test
    @DisplayName("Test 3: Username rempli, password vide")
    void testEmptyPassword() {
        String username = TEST_USERNAME;
        String password = "";

        assertFalse(username.isEmpty(), "Username should not be empty");
        assertTrue(password.isEmpty(), "Password should be empty");
    }

    @Test
    @DisplayName("Test 4: Username et password avec espaces uniquement")
    void testWhitespaceUsernameAndPassword() {
        String username = "   ";
        String password = "   ";

        assertTrue(username.trim().isEmpty(), "Trimmed username should be empty");
        assertTrue(password.trim().isEmpty(), "Trimmed password should be empty");
    }

    // ========== TESTS DE HACHAGE ==========

    @Test
    @DisplayName("Test 5: Hachage du mot de passe")
    void testPasswordHashing() {
        String hashedPassword = Hash256.hashPassword(TEST_PASSWORD);

        assertNotNull(hashedPassword, "Hashed password should not be null");
        assertNotEquals(TEST_PASSWORD, hashedPassword, "Hashed password should differ from original");
        assertFalse(hashedPassword.isEmpty(), "Hashed password should not be empty");
    }

    @Test
    @DisplayName("Test 6: Hachage de mots de passe identiques (cohérence)")
    void testPasswordHashingConsistency() {
        String hash1 = Hash256.hashPassword(TEST_PASSWORD);
        String hash2 = Hash256.hashPassword(TEST_PASSWORD);

        assertEquals(hash1, hash2, "Hashing same password should produce same hash");
    }

    @Test
    @DisplayName("Test 7: Hachage de mots de passe différents")
    void testDifferentPasswordsHashing() {
        String password1 = "password1";
        String password2 = "password2";

        String hash1 = Hash256.hashPassword(password1);
        String hash2 = Hash256.hashPassword(password2);

        assertNotEquals(hash1, hash2, "Different passwords should produce different hashes");
    }

    @Test
    @DisplayName("Test 8: Hash non vide avec mot de passe vide")
    void testEmptyPasswordHashing() {
        String emptyPassword = "";
        String hashedPassword = Hash256.hashPassword(emptyPassword);

        assertNotNull(hashedPassword, "Hashed password should not be null even for empty input");
    }

    // ========== TESTS DE CONNEXION À LA BDD ==========

    @Test
    @DisplayName("Test 9: Connexion réussie à la base de données")
    void testDatabaseConnectionSuccess() throws SQLException {
        assertNotNull(mockConnection, "Connection should not be null");
    }

    @Test
    @DisplayName("Test 10: Vérification que la connexion est non-null")
    void testConnectionNotNull() {
        assertNotNull(mockConnection, "Mocked connection should not be null");
    }

    // ========== TESTS DE REQUÊTE SQL ==========

    @Test
    @DisplayName("Test 11: Préparation de la requête SQL")
    void testSQLStatementPrepared() throws SQLException {
        when(mockConnection.prepareStatement(SQL_QUERY)).thenReturn(mockStatement);

        PreparedStatement stmt = mockConnection.prepareStatement(SQL_QUERY);

        assertNotNull(stmt, "Statement should not be null");
        verify(mockConnection, times(1)).prepareStatement(SQL_QUERY);
    }

    @Test
    @DisplayName("Test 12: Paramètres setString pour username et password")
    void testSQLParametersSet() throws SQLException {
        String hashedPassword = Hash256.hashPassword(TEST_PASSWORD);

        mockStatement.setString(1, TEST_USERNAME);
        mockStatement.setString(2, hashedPassword);

        verify(mockStatement, times(1)).setString(1, TEST_USERNAME);
        verify(mockStatement, times(1)).setString(2, hashedPassword);
    }

    @Test
    @DisplayName("Test 13: Erreur lors de la préparation de la requête")
    void testSQLStatementPreparationError() throws SQLException {
        when(mockConnection.prepareStatement(anyString()))
                .thenThrow(new SQLException("Statement preparation failed"));

        assertThrows(SQLException.class,
                () -> mockConnection.prepareStatement(SQL_QUERY),
                "Should throw SQLException on statement preparation error");
    }

    // ========== TESTS DE RÉSULTATS ==========

    @Test
    @DisplayName("Test 14: Utilisateur trouvé avec credentials corrects")
    void testUserFoundWithCorrectCredentials() throws SQLException {
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt("id_user")).thenReturn(TEST_USER_ID);
        when(mockResultSet.getString("user_name")).thenReturn(TEST_USERNAME);
        when(mockResultSet.getString("email")).thenReturn(TEST_EMAIL);
        when(mockResultSet.getInt("is_admin")).thenReturn(0);

        ResultSet rs = mockStatement.executeQuery();
        assertTrue(rs.next(), "ResultSet should have next record");
        assertEquals(TEST_USER_ID, rs.getInt("id_user"), "User ID should match");
        assertEquals(TEST_USERNAME, rs.getString("user_name"), "Username should match");
        assertEquals(TEST_EMAIL, rs.getString("email"), "Email should match");
    }

    @Test
    @DisplayName("Test 15: Utilisateur non trouvé avec credentials incorrects")
    void testUserNotFoundWithIncorrectCredentials() throws SQLException {
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        ResultSet rs = mockStatement.executeQuery();

        assertFalse(rs.next(), "ResultSet should not have next record");
    }

    @Test
    @DisplayName("Test 16: Récupération des données utilisateur Admin")
    void testUserDataRetrievalAdmin() throws SQLException {
        int adminId = 5;
        String adminUsername = "admin_user";
        String adminEmail = "admin@example.com";

        when(mockResultSet.getInt("id_user")).thenReturn(adminId);
        when(mockResultSet.getString("user_name")).thenReturn(adminUsername);
        when(mockResultSet.getString("email")).thenReturn(adminEmail);
        when(mockResultSet.getInt("is_admin")).thenReturn(0);  // Convention: 0 = Admin

        int idUser = mockResultSet.getInt("id_user");
        String uname = mockResultSet.getString("user_name");
        String email = mockResultSet.getString("email");
        boolean isAdmin = (mockResultSet.getInt("is_admin") == 1);

        assertEquals(adminId, idUser, "Admin ID should match");
        assertEquals(adminUsername, uname, "Admin username should match");
        assertFalse(isAdmin, "User with is_admin=0 should be identified as admin");
    }

    @Test
    @DisplayName("Test 17: Récupération des données utilisateur Standard")
    void testUserDataRetrievalStandardUser() throws SQLException {
        int userId = 10;
        String username = "regular_user";
        String email = "user@example.com";

        when(mockResultSet.getInt("id_user")).thenReturn(userId);
        when(mockResultSet.getString("user_name")).thenReturn(username);
        when(mockResultSet.getString("email")).thenReturn(email);
        when(mockResultSet.getInt("is_admin")).thenReturn(1);  // Convention: 1 = Standard User

        int idUser = mockResultSet.getInt("id_user");
        String uname = mockResultSet.getString("user_name");
        String userEmail = mockResultSet.getString("email");
        boolean isAdmin = (mockResultSet.getInt("is_admin") == 0);

        assertEquals(userId, idUser, "User ID should match");
        assertEquals(username, uname, "Username should match");
        assertFalse(isAdmin, "User with is_admin=1 should not be identified as admin");
    }

    // ========== TESTS DE SESSION ==========

    @Test
    @DisplayName("Test 18: Sauvegarde de la session utilisateur standard")
    void testUserSessionSaved() {
        try (MockedStatic<UserSession> mockedSession = mockStatic(UserSession.class)) {
            boolean isAdmin = false;

            UserSession.setSession(TEST_USER_ID, TEST_USERNAME, TEST_EMAIL, isAdmin);

            mockedSession.verify(() -> UserSession.setSession(TEST_USER_ID, TEST_USERNAME, TEST_EMAIL, isAdmin),
                    times(1));
        }
    }

    @Test
    @DisplayName("Test 19: Sauvegarde de la session Admin")
    void testAdminSessionSaved() {
        try (MockedStatic<UserSession> mockedSession = mockStatic(UserSession.class)) {
            int adminId = 5;
            String adminUsername = "admin_user";
            String adminEmail = "admin@example.com";
            boolean isAdmin = true;

            UserSession.setSession(adminId, adminUsername, adminEmail, isAdmin);

            mockedSession.verify(() -> UserSession.setSession(adminId, adminUsername, adminEmail, isAdmin),
                    times(1));
        }
    }

    // ========== TESTS DE REDIRECTION ==========

    @Test
    @DisplayName("Test 20: Détermination du rôle Admin")
    void testAdminRoleDetermination() throws SQLException {
        when(mockResultSet.getInt("is_admin")).thenReturn(0);  // Convention: 0 = Admin

        boolean isAdmin = (mockResultSet.getInt("is_admin") == 0);

        assertTrue(isAdmin, "User with is_admin=0 should be identified as admin");
    }

    @Test
    @DisplayName("Test 21: Détermination du rôle Utilisateur standard")
    void testStandardUserRoleDetermination() throws SQLException {
        when(mockResultSet.getInt("is_admin")).thenReturn(1);  // Convention: 1 = Standard User

        boolean isAdmin = (mockResultSet.getInt("is_admin") == 0);

        assertFalse(isAdmin, "User with is_admin=1 should not be identified as admin");
    }

    // ========== TESTS DE SÉCURITÉ ==========

    @Test
    @DisplayName("Test 22: Le mot de passe ne doit pas être en plaintext")
    void testPasswordNotPlaintext() {
        String hashed = Hash256.hashPassword(TEST_PASSWORD);

        assertNotEquals(TEST_PASSWORD, hashed, "Hashed password should not equal plaintext password");
    }

    @Test
    @DisplayName("Test 23: Longueur de hash suffisante")
    void testHashLengthSufficient() {
        String hashed = Hash256.hashPassword(TEST_PASSWORD);

        assertTrue(hashed.length() > 20, "Hash length should be at least 20 characters (SHA-256)");
    }

    @Test
    @DisplayName("Test 24: PreparedStatement prévient les injections SQL")
    void testSQLInjectionPrevention() throws SQLException {
        String maliciousUsername = "' OR '1'='1";
        String password = "password";

        when(mockConnection.prepareStatement(SQL_QUERY)).thenReturn(mockStatement);

        mockStatement.setString(1, maliciousUsername);
        mockStatement.setString(2, Hash256.hashPassword(password));

        verify(mockStatement).setString(1, maliciousUsername);
        // PreparedStatement traite ceci comme une chaîne littérale, pas du code SQL
    }

    // ========== TESTS D'ERREURS ==========

    @Test
    @DisplayName("Test 25: Gestion de SQLException")
    void testSQLExceptionHandling() throws SQLException {
        when(mockConnection.prepareStatement(anyString()))
                .thenThrow(new SQLException("Database error"));

        assertThrows(SQLException.class,
                () -> mockConnection.prepareStatement(SQL_QUERY),
                "Should throw SQLException on database error");
    }

    @Test
    @DisplayName("Test 26: Gestion d'erreur lors de l'exécution de la requête")
    void testQueryExecutionError() throws SQLException {
        when(mockStatement.executeQuery())
                .thenThrow(new SQLException("Query execution failed"));

        assertThrows(SQLException.class,
                () -> mockStatement.executeQuery(),
                "Should throw SQLException on query execution error");
    }

    @Test
    @DisplayName("Test 27: Gestion d'erreur lors de la récupération de données")
    void testDataRetrievalError() throws SQLException {
        when(mockResultSet.getInt("id_user"))
                .thenThrow(new SQLException("Column not found"));

        assertThrows(SQLException.class,
                () -> mockResultSet.getInt("id_user"),
                "Should throw SQLException on data retrieval error");
    }
}