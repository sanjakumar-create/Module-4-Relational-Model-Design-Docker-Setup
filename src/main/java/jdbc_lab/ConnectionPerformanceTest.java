package jdbc_lab;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ConnectionPerformanceTest {
    private static final String URL = "jdbc:postgresql://localhost:5433/sijay";
    private static final String USER = "sanjay";
    private static final String PASS = "1234";

    public static void main(String[] args) throws InterruptedException {
        // 1. Run with Custom Single Connection DataSource
        DataSource singleDS = new SingleConnectionDataSource(URL, USER, PASS);
        System.out.println("Starting Test A: Single Connection...");
        runTest(singleDS);

        // 2. Run with HikariCP Connection Pool
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(URL);
        config.setUsername(USER);
        config.setPassword(PASS);
        config.setMaximumPoolSize(10); // 10 connections for 10 threads
        HikariDataSource hikariDS = new HikariDataSource(config);

        System.out.println("\nStarting Test B: HikariCP Pool (Size 10)...");
        runTest(hikariDS);
        hikariDS.close();
    }

    private static void runTest(DataSource ds) throws InterruptedException {
        int threads = 10;
        ExecutorService executor = Executors.newFixedThreadPool(threads);
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < threads; i++) {
            executor.submit(() -> {

                    try (Connection conn = ds.getConnection();
                     Statement stmt = conn.createStatement()) {

                    var rs = stmt.executeQuery(
                            "SELECT * FROM students, (SELECT pg_sleep(1)) AS delay"
                    );

                    while (rs.next()) {
                        System.out.println(rs.getInt("id") + " " + rs.getString("name"));
                    }
                } catch (Exception e) {
                    System.err.println("Error: " + e.getMessage());
                }
            });
        }

        executor.shutdown();
        executor.awaitTermination(30, TimeUnit.SECONDS);
        long duration = System.currentTimeMillis() - startTime;
        System.out.println("Total execution time: " + duration + " ms");
    }
}