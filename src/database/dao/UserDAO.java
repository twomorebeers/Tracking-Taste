package database.dao;

import database.DatabaseConfig;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import models.User;
import models.UserRole;
import enums.Gender;
import java.sql.*;

public class UserDAO {
    public static boolean createUser(String username, String passwordHash, int roleId) {
        String sql = "INSERT INTO users (username, password_hash, role_id) VALUES (?, ?, ?)";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, passwordHash);
            pstmt.setInt(3, roleId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static List<String> getAllUsernames() {
        List<String> usernames = new ArrayList<>();
        String sql = "SELECT username FROM users";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                usernames.add(rs.getString("username"));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return usernames;
    }
    
    public static boolean userExists(String username) {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    public static User getUser(String username) {
        String sql = "SELECT u.*, ur.role_name FROM users u " +
                    "JOIN user_roles ur ON u.role_id = ur.role_id " +
                    "WHERE u.username = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new User(
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        "Default User", // You can add a name field to your users table if needed
                        70.0, // Default weight
                        170.0, // Default height
                        30, // Default age
                        Gender.OTHER, // Default gender
                        UserRole.valueOf(rs.getString("role_name"))
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting user: " + e.getMessage());
            e.printStackTrace();
        }
        
        // If no user found or error occurred, return a default user with USER role
        return new User(0, username, "Default User", 70.0, 170.0, 30, Gender.OTHER, UserRole.USER);
    }
    
    public static User authenticateUser(String username, String password) {
        String sql = "SELECT u.*, ur.role_name FROM users u " +
                    "JOIN user_roles ur ON u.role_id = ur.role_id " +
                    "WHERE u.username = ? AND u.password_hash = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            // In a real application, you should use proper password hashing
            // This is a simplified version for demonstration
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new User(
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        "Default User",
                        70.0,
                        170.0,
                        30,
                        Gender.OTHER,
                        UserRole.valueOf(rs.getString("role_name"))
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error authenticating user: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    public static boolean registerUser(String username, String password) {
        // Default role_id is 2 for regular users
        return createUser(username, password, 2);
    }
}
