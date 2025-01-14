package database.dao;

import models.FoodLog;
import models.FoodItem;
import database.DatabaseConfig;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import util.DatabaseThreadPool;

public class FoodLogDAO {
    
    public static CompletableFuture<Boolean> addFoodLogAsync(FoodLog foodLog) {
        return CompletableFuture.supplyAsync(() -> {
            String sql = "INSERT INTO user_food_logs (user_id, food_id, serving_size, log_datetime, notes) " +
                        "VALUES (?, ?, ?, ?, ?) RETURNING id";
            
            try (Connection conn = DatabaseConfig.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                
                pstmt.setInt(1, foodLog.getUserId());
                pstmt.setInt(2, foodLog.getFoodId());
                pstmt.setDouble(3, foodLog.getServingSize());
                pstmt.setTimestamp(4, Timestamp.valueOf(foodLog.getLogDateTime()));
                pstmt.setString(5, foodLog.getNotes());
                
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        foodLog.setId(rs.getInt(1));
                        return true;
                    }
                }
                return false;
                
            } catch (SQLException e) {
                System.err.println("Error adding food log: " + e.getMessage());
                e.printStackTrace();
                return false;
            }
        }, DatabaseThreadPool.getExecutor());
    }

    public static CompletableFuture<List<FoodLog>> getUserFoodLogsAsync(int userId) {
        return CompletableFuture.supplyAsync(() -> {
            List<FoodLog> logs = new ArrayList<>();
            String sql = "SELECT l.*, f.* FROM user_food_logs l " +
                        "JOIN foods f ON l.food_id = f.id " +
                        "WHERE l.user_id = ? " +
                        "ORDER BY l.log_datetime DESC";
            
            try (Connection conn = DatabaseConfig.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                
                pstmt.setInt(1, userId);
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        // Create FoodLog
                        FoodLog log = new FoodLog(
                            rs.getInt("id"),
                            rs.getInt("user_id"),
                            rs.getInt("food_id"),
                            rs.getDouble("serving_size"),
                            rs.getTimestamp("log_datetime").toLocalDateTime(),
                            rs.getString("notes")
                        );
                        
                        // Create associated FoodItem
                        FoodItem food = new FoodItem(
                            rs.getInt("food_id"),
                            rs.getString("name"),
                            rs.getDouble("calories"),
                            rs.getDouble("protein"),
                            rs.getDouble("carbs"),
                            rs.getDouble("fats"),
                            FoodCategory.valueOf(rs.getString("category"))
                        );
                        
                        log.setFoodItem(food);
                        logs.add(log);
                    }
                }
            } catch (SQLException e) {
                System.err.println("Error getting user food logs: " + e.getMessage());
                e.printStackTrace();
            }
            
            return logs;
        }, DatabaseThreadPool.getExecutor());
    }

    public static CompletableFuture<Boolean> deleteFoodLogAsync(int logId) {
        return CompletableFuture.supplyAsync(() -> {
            String sql = "DELETE FROM user_food_logs WHERE id = ?";
            
            try (Connection conn = DatabaseConfig.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                
                pstmt.setInt(1, logId);
                int rowsAffected = pstmt.executeUpdate();
                return rowsAffected > 0;
                
            } catch (SQLException e) {
                System.err.println("Error deleting food log: " + e.getMessage());
                e.printStackTrace();
                return false;
            }
        }, DatabaseThreadPool.getExecutor());
    }
}
