package dev.back.actions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.File;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class UploadTest {

    private Connection conn;
    private PreparedStatement ps;
    private ResultSet rs;
    private Upload upload;

    @BeforeEach
    void setUp() throws Exception {
        conn = mock(Connection.class);
        ps = mock(PreparedStatement.class);
        rs = mock(ResultSet.class);
        upload = new Upload(conn);

        // mock PreparedStatement creation
        when(conn.prepareStatement(anyString())).thenReturn(ps);
        // mock executeQuery for user public key
        when(ps.executeQuery()).thenReturn(rs);
    }

    @Test
    void uploadFile_shouldReturnFalse_whenPrivateKeyMissing() throws Exception {
        Connection conn = mock(Connection.class);
        Upload upload = new Upload(conn);

        File file = new File("dummy.txt");
        int idUser = 999; // Utilisateur fictif pour test

        // Mock statique LoggerUtil
        try (MockedStatic<LoggerUtil> mockedLogger = mockStatic(LoggerUtil.class)) {
            // Quand LoggerUtil.log est appelé, ne rien faire
            mockedLogger.when(() -> LoggerUtil.log(any(), any(), any(), anyInt(), any())).thenAnswer(invocation -> null);

            // Appel de la méthode à tester
            boolean result = upload.uploadFile(idUser, file);

            // Vérification
            assertFalse(result);
        }
    }

    @Test
    void uploadFile_shouldReturnFalse_whenUserNotFound() throws Exception {
        File tempFile = File.createTempFile("testFile2", ".txt");
        tempFile.deleteOnExit();

        // Simuler dossier de clé privée existant
        File keyFolder = new File("storage/private_key_user/");
        keyFolder.mkdirs();
        File keyFile = new File(keyFolder, "1_key_private.pem");
        keyFile.createNewFile();
        keyFile.deleteOnExit();

        // Simuler résultat vide pour user
        when(rs.next()).thenReturn(false);

        boolean result = upload.uploadFile(1, tempFile);
        assertFalse(result);
    }
}
