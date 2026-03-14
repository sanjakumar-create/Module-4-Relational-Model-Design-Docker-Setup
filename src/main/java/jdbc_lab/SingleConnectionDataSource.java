package jdbc_lab;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

public class SingleConnectionDataSource implements DataSource {
    private final String url;
    private final String user;
    private final String password;
    private Connection connection; // Cached connection

    public SingleConnectionDataSource(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    @Override
    public Connection getConnection() throws SQLException {
        // Only create the connection if it doesn't exist or is closed
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(url, user, password);
        }
        return connection;
    }

    // All other methods left unimplemented as per instructions
    @Override public Connection getConnection(String u, String p) { return null; }
    @Override public <T> T unwrap(Class<T> i) { return null; }
    @Override public boolean isWrapperFor(Class<?> i) { return false; }
    @Override public PrintWriter getLogWriter() { return null; }
    @Override public void setLogWriter(PrintWriter o) {}
    @Override public void setLoginTimeout(int s) {}
    @Override public int getLoginTimeout() { return 0; }
    @Override public Logger getParentLogger() { return null; }
}