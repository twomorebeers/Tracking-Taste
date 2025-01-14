package database.dao;

import database.DatabaseConfig;
import models.FoodItem;
import enums.FoodCategory;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import util.DatabaseThreadPool;

public class FoodDAO {
    
    public static boolean createFood(FoodItem food, int userId) {
        String sql = "INSERT INTO foods (name, calories, protein, carbs, fats, category, created_by) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
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
        String sql = "SELECT * FROM foods";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            System.out.println("Executing getAllFoods query...");
            int count = 0;
            while (rs.next()) {
                count++;
                try {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    double calories = rs.getDouble("calories");
                    double protein = rs.getDouble("protein");
                    double carbs = rs.getDouble("carbs");
                    double fats = rs.getDouble("fats");
                    String categoryName = rs.getString("category");
                    System.out.println("Found food: " + name + " (Category: " + categoryName + ")");
                    
                    FoodItem food = new FoodItem(
                        id,
                        name,
                        calories,
                        protein,
                        carbs,
                        fats,
                        FoodCategory.valueOf(categoryName)
                    );
                    foods.add(food);
                } catch (IllegalArgumentException e) {
                    System.err.println("Error processing food item: Invalid category name: " + e.getMessage());
                } catch (SQLException e) {
                    System.err.println("Error reading food item data: " + e.getMessage());
                }
            }
            System.out.println("Total foods found: " + count);
            
        } catch (SQLException e) {
            System.err.println("Database error in getAllFoods: " + e.getMessage());
            e.printStackTrace();
        }
        
        return foods;
    }
    
    public static CompletableFuture<List<FoodItem>> getAllFoodsAsync() {
        return DatabaseThreadPool.submitTask(() -> getAllFoods());
    }
    
    public static FoodItem getFoodById(int foodId) {
        String sql = "SELECT * FROM foods WHERE id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, foodId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new FoodItem(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("calories"),
                        rs.getDouble("protein"),
                        rs.getDouble("carbs"),
                        rs.getDouble("fats"),
                        FoodCategory.valueOf(rs.getString("category"))
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
        String sql = "SELECT * FROM foods WHERE category = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, category.toString());
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    FoodItem food = new FoodItem(
                        rs.getInt("id"),
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
    
    public static CompletableFuture<List<FoodItem>> getFoodsByCategoryAsync(FoodCategory category) {
        return DatabaseThreadPool.submitTask(() -> getFoodsByCategory(category));
    }
    
    public static boolean updateFood(int foodId, FoodItem food) {
        String sql = "UPDATE foods SET name = ?, calories = ?, protein = ?, " +
                    "carbs = ?, fats = ?, category = ? " +
                    "WHERE id = ?";
        
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
    
    public static CompletableFuture<Boolean> updateFoodAsync(int foodId, FoodItem food) {
        return DatabaseThreadPool.submitTask(() -> {
            String sql = "UPDATE foods SET name = ?, calories = ?, protein = ?, " +
                        "carbs = ?, fats = ?, category = ? WHERE id = ?";
            
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
                System.err.println("Error updating food: " + e.getMessage());
                e.printStackTrace();
                return false;
            }
        });
    }
    
    public static List<FoodItem> getRecentFoods(int limit) {
        List<FoodItem> foods = new ArrayList<>();
        String sql = "SELECT * FROM foods ORDER BY created_at DESC LIMIT ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, limit);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    FoodItem food = new FoodItem(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("calories"),
                        rs.getDouble("protein"),
                        rs.getDouble("carbs"),
                        rs.getDouble("fats"),
                        FoodCategory.valueOf(rs.getString("category"))
                    );
                    foods.add(food);
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return foods;
    }
    
    public static boolean addFood(FoodItem food) {
        String sql = "INSERT INTO foods (name, calories, protein, carbs, fats, category) " +
                    "VALUES (?, ?, ?, ?, ?, ?) RETURNING id";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, food.getName());
            pstmt.setDouble(2, food.getCalories());
            pstmt.setDouble(3, food.getProtein());
            pstmt.setDouble(4, food.getCarbs());
            pstmt.setDouble(5, food.getFats());
            pstmt.setString(6, food.getCategory().toString());
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    food.setId(rs.getInt(1));
                    return true;
                }
            }
            return false;
            
        } catch (SQLException e) {
            System.err.println("Error adding food: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public static CompletableFuture<Boolean> addFoodAsync(FoodItem food) {
        return DatabaseThreadPool.submitTask(() -> addFood(food));
    }
    
    public static CompletableFuture<Boolean> deleteFoodAsync(int foodId) {
        return DatabaseThreadPool.submitTask(() -> {
            String sql = "DELETE FROM foods WHERE id = ?";
            
            try (Connection conn = DatabaseConfig.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                
                pstmt.setInt(1, foodId);
                int rowsAffected = pstmt.executeUpdate();
                return rowsAffected > 0;
                
            } catch (SQLException e) {
                System.err.println("Error deleting food: " + e.getMessage());
                e.printStackTrace();
                return false;
            }
        });
    }
}
