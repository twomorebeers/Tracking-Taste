package database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConfig {
    private static HikariDataSource dataSource;
    
    // Database configuration
    private static final String DB_HOST = "localhost";
    private static final String DB_PORT = "5432";
    private static final String DB_NAME = "tracking_taste";
    private static final String DB_USER = "postgres                         ";
    private static final String DB_PASSWORD = "aport2005"; // Replace with your actual password
    
    static {
        initializeDataSource();
    }
    
    private static void initializeDataSource() {
        Properties props = new Properties();
        props.setProperty("dataSourceClassName", "org.postgresql.ds.PGSimpleDataSource");
        props.setProperty("dataSource.serverName", DB_HOST);
        props.setProperty("dataSource.portNumber", DB_PORT);
        props.setProperty("dataSource.databaseName", DB_NAME);
        props.setProperty("dataSource.user", DB_USER);
        props.setProperty("dataSource.password", DB_PASSWORD);
        
        HikariConfig config = new HikariConfig(props);
        config.setMaximumPoolSize(10); // Maximum number of connections in the pool
        config.setMinimumIdle(5);      // Minimum number of idle connections
        config.setIdleTimeout(300000);  // Maximum idle time for connection (5 minutes)
        config.setConnectionTimeout(10000); // Maximum wait time for connection (10 seconds)
        
        dataSource = new HikariDataSource(config);
    }
    
    public static Connection getConnection() throws SQLException {
        if (dataSource == null) {
            initializeDataSource();
        }
        return dataSource.getConnection();
    }
    
    public static void closeDataSource() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }
    
    // Test database connection
    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
