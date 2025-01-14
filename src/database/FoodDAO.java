package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import models.FoodItem;
import enums.FoodCategory;
import database.DatabaseConfig;

public class FoodDAO {
    public List<FoodItem> getAllFoods() {
        List<FoodItem> foods = new ArrayList<>();
        String query = "SELECT Id, name, calories, protein, carbs, fats, category FROM foods";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("Id");
                String name = rs.getString("name");
                int calories = rs.getInt("calories");
                double protein = rs.getDouble("protein");
                double carbs = rs.getDouble("carbs");
                double fats = rs.getDouble("fats");
                FoodCategory category = FoodCategory.valueOf(rs.getString("category"));
                foods.add(new FoodItem(id, name, calories, protein, carbs, fats, category));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return foods;
    }
}
