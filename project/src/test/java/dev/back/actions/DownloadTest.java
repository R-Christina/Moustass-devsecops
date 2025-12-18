package dev.back.actions;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.junit.jupiter.api.Test;

class DownloadTest {

    @Test
    void downloadFileMessage_shouldReturnError_whenNoSignatureFound() throws Exception {
        Connection conn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(conn.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(false); // aucune signature

        Download download = new Download(conn);
        String result = download.downloadFileMessage(1, "test.txt", "dest");

        assertTrue(result.startsWith("ERREUR"));
    }

    @Test
    void downloadFileMessage_shouldHandleSqlException() throws Exception {
        Connection conn = mock(Connection.class);

        when(conn.prepareStatement(anyString()))
                .thenThrow(new RuntimeException("DB DOWN"));

        Download download = new Download(conn);
        String result = download.downloadFileMessage(1, "file.txt", "dest");

        assertTrue(result.contains("ERREUR"));
    }
}