
package com.clockinpro.database;

import com.clockinpro.models.Employee;
import com.clockinpro.models.Employee.TimeRecord;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.io.IOException;
import java.io.InputStream;

public class DBUtil {

    private static final String PROPERTIES_FILE = "/config.properties";
    private static String URL;
    private static String USER;
    private static String PASSWORD;

    static {
        Properties props = new Properties();
        try (InputStream input = DBUtil.class.getResourceAsStream(PROPERTIES_FILE)) {
            if (input == null) {
                throw new IOException("Unable to find " + PROPERTIES_FILE);
            }
            props.load(input);
            URL = props.getProperty("db.url");
            USER = props.getProperty("db.user");
            PASSWORD = props.getProperty("db.password");

            if (URL == null || USER == null || PASSWORD == null) {
                throw new IllegalStateException("Missing required database properties in " + PROPERTIES_FILE);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load database configuration: " + e.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static Employee validateLogin(String username, String password) {
        String query = "SELECT * FROM employees WHERE username = ? AND password = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Employee(rs.getInt("id"), rs.getString("username"), rs.getString("password"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void saveTimeRecord(TimeRecord record) {
        String query = "INSERT INTO time_records (employee_id, clock_in, clock_out, hours_worked) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, record.getEmployeeId());
            stmt.setTimestamp(2, Timestamp.valueOf(record.getClockIn()));
            stmt.setTimestamp(3, Timestamp.valueOf(record.getClockOut()));
            stmt.setDouble(4, record.getHoursWorked());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<TimeRecord> getTimeRecords(int employeeId) {
        List<TimeRecord> records = new ArrayList<>();
        String query = "SELECT * FROM time_records WHERE employee_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, employeeId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                TimeRecord record = new TimeRecord(
                        rs.getInt("employee_id"),
                        rs.getTimestamp("clock_in").toLocalDateTime(),
                        rs.getTimestamp("clock_out").toLocalDateTime()
                );
                records.add(record);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return records;
    }
}
