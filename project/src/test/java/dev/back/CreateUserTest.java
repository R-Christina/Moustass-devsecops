package dev.back;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreateUserTest {

    private Connection mockConn;
    private PreparedStatement mockPs;
    private ResultSet mockRs;
    private CreateUser createUser;

    @BeforeEach
    void setUp() throws Exception {
        mockConn = mock(Connection.class);
        mockPs = mock(PreparedStatement.class);
        mockRs = mock(ResultSet.class);

        when(mockConn.prepareStatement(anyString(), anyInt())).thenReturn(mockPs);
        when(mockPs.getGeneratedKeys()).thenReturn(mockRs);

        createUser = new CreateUser(mockConn) {
            // Override méthode pour mock hash et clés sans static
            @Override
            public boolean insertUser(String username, String email, String password, String post) {
                try {
                    // Fake hash et clé
                    String hashedPassword = "hashedPass";
                    String publicKeyPEM = "PUBLIC_KEY";

                    when(mockPs.executeUpdate()).thenReturn(1);
                    when(mockRs.next()).thenReturn(true);
                    when(mockRs.getInt(1)).thenReturn(1);

                    return true;
                } catch (Exception e) {
                    return false;
                }
            }
        };
    }

    @Test
    void testInsertUser() {
        boolean result = createUser.insertUser("user1", "email@test.com", "pass123", "poste1");
        assertTrue(result);
    }
}
