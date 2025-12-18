package dev.back.actions;

import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.junit.jupiter.api.Test;

class LoggerUtilTest {

    @Test
    void log_shouldNotThrow_andExecuteStatements() throws Exception {
        Connection conn = mock(Connection.class);
        PreparedStatement psUser = mock(PreparedStatement.class);
        PreparedStatement psLog = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(conn.prepareStatement(startsWith("SELECT"))).thenReturn(psUser);
        when(conn.prepareStatement(startsWith("INSERT"))).thenReturn(psLog);

        when(psUser.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true);
        when(rs.getString("user_name")).thenReturn("admin");

        LoggerUtil.log(conn, "UPLOAD", "OK", 1, "file.txt");

        verify(psUser).executeQuery();
        verify(psLog).executeUpdate();
    }
}