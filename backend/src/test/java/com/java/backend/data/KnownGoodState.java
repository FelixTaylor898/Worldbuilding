package com.java.backend.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@ActiveProfiles("test")
@Service
public class KnownGoodState {

    @Autowired
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public KnownGoodState(JdbcTemplate jdbcTemplate, DataSource dataSource) {

        this.jdbcTemplate = jdbcTemplate;
        // Log the connected database for debugging
        try (Connection connection = dataSource.getConnection()) {
            System.out.println("Connected to database: " + connection.getCatalog());
        } catch (SQLException e) {
            System.err.println("Error getting database name: " + e.getMessage());
        }
    }

    public void reset() {
        try {
            // Call the stored procedure to reset the database state
            jdbcTemplate.execute("CALL reset_database();");
            System.out.println("Database reset successfully.");
        } catch (Exception e) {
            // Handle the exception if there's any issue in calling the procedure
            System.err.println("Error while resetting the database: " + e.getMessage());
        }
    }
}