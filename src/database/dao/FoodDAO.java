package database.dao;

import database.DatabaseConfig;
import models.FoodItem;
import enums.FoodCategory;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FoodDAO {
    
    public static boolean createFood(FoodItem food, int userId) {
        String sql = "INSERT INTO food_items (name, calories, protein, carbs, fats, category_id, created_by) " +
                    "VALUES (?, ?, ?, ?, ?, (SELECT category_id FROM food_categories WHERE category_name = ?), ?)";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, food.getName());
            pstmt.setDouble(2, food.getCalories());
            pstmt.setDouble(3, food.getProtein());
            pstmt.setDouble(4, food.getCarbs());
            pstmt.setDouble(5, food.getFats());
            pstmt.setString(6, food.getCategory().toString());
            pstmt.setInt(7, userId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static List<FoodItem> getAllFoods() {
        List<FoodItem> foods = new ArrayList<>();
        String sql = "SELECT f.*, c.category_name FROM food_items f " +
                    "JOIN food_categories c ON f.category_id = c.category_id";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                FoodItem food = new FoodItem(
                    rs.getString("name"),
                    rs.getDouble("calories"),
                    rs.getDouble("protein"),
                    rs.getDouble("carbs"),
                    rs.getDouble("fats"),
                    FoodCategory.valueOf(rs.getString("category_name"))
                );
                foods.add(food);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return foods;
    }
    
    public static FoodItem getFoodById(int foodId) {
        String sql = "SELECT f.*, c.category_name FROM food_items f " +
                    "JOIN food_categories c ON f.category_id = c.category_id " +
                    "WHERE f.food_id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, foodId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new FoodItem(
                        rs.getString("name"),
                        rs.getDouble("calories"),
                        rs.getDouble("protein"),
                        rs.getDouble("carbs"),
                        rs.getDouble("fats"),
                        FoodCategory.valueOf(rs.getString("category_name"))
                    );
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    public static List<FoodItem> getFoodsByCategory(FoodCategory category) {
        List<FoodItem> foods = new ArrayList<>();
        String sql = "SELECT f.* FROM food_items f " +
                    "JOIN food_categories c ON f.category_id = c.category_id " +
                    "WHERE c.category_name = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, category.toString());
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    FoodItem food = new FoodItem(
                        rs.getString("name"),
                        rs.getDouble("calories"),
                        rs.getDouble("protein"),
                        rs.getDouble("carbs"),
                        rs.getDouble("fats"),
                        category
                    );
                    foods.add(food);
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return foods;
    }
    
    public static boolean updateFood(int foodId, FoodItem food) {
        String sql = "UPDATE food_items SET name = ?, calories = ?, protein = ?, " +
                    "carbs = ?, fats = ?, category_id = (SELECT category_id FROM food_categories WHERE category_name = ?) " +
                    "WHERE food_id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, food.getName());
            pstmt.setDouble(2, food.getCalories());
            pstmt.setDouble(3, food.getProtein());
            pstmt.setDouble(4, food.getCarbs());
            pstmt.setDouble(5, food.getFats());
            pstmt.setString(6, food.getCategory().toString());
            pstmt.setInt(7, foodId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static List<FoodItem> getRecentFoods(int limit) {
        List<FoodItem> foods = new ArrayList<>();
        String sql = "SELECT f.*, c.category_name FROM food_items f " +
                    "JOIN food_categories c ON f.category_id = c.category_id " +
                    "ORDER BY f.created_at DESC LIMIT ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, limit);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    FoodItem food = new FoodItem(
                        rs.getString("name"),
                        rs.getDouble("calories"),
                        rs.getDouble("protein"),
                        rs.getDouble("carbs"),
                        rs.getDouble("fats"),
                        FoodCategory.valueOf(rs.getString("category_name"))
                    );
                    foods.add(food);
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return foods;
    }
}
