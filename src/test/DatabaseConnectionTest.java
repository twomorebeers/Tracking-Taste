package test;

import database.DatabaseConfig;
import database.dao.UserDAO;
import database.dao.FoodDAO;
import models.FoodItem;
import enums.FoodCategory;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;

public class DatabaseConnectionTest {
    
    @Test
    public void testDatabaseConnection() {
        assertTrue("Database connection failed", DatabaseConfig.testConnection());
    }
    
    @Test
    public void testUserCreation() {
        // Test creating a new user
        String testUsername = "testuser_" + System.currentTimeMillis();
        String testPassword = "hashedpassword123"; // In real app, this would be properly hashed
        boolean created = UserDAO.createUser(testUsername, testPassword, 2); // 2 is the USER role
        
        assertTrue("User creation failed", created);
        assertTrue("Created user not found", UserDAO.userExists(testUsername));
    }
    
    @Test
    public void testFoodOperations() {
        // Test creating a new food item
        FoodItem testFood = new FoodItem(
            "Test Apple",
            95.0,
            0.5,
            25.0,
            0.3,
            FoodCategory.FRUITS
        );
        
        // Create food item (assuming user ID 1 exists)
        boolean created = FoodDAO.createFood(testFood, 1);
        assertTrue("Food creation failed", created);
        
        // Test retrieving all foods
        assertFalse("No foods found in database", FoodDAO.getAllFoods().isEmpty());
        
        // Test retrieving foods by category
        List<FoodItem> fruits = FoodDAO.getFoodsByCategory(FoodCategory.FRUITS);
        assertFalse("No fruits found in database", fruits.isEmpty());
        
        // Verify the retrieved food has correct properties
        FoodItem retrievedFood = fruits.stream()
            .filter(f -> f.getName().equals("Test Apple"))
            .findFirst()
            .orElse(null);
            
        assertNotNull("Test food not found in database", retrievedFood);
        assertEquals("Incorrect calories", 95.0, retrievedFood.getCalories(), 0.01);
        assertEquals("Incorrect protein", 0.5, retrievedFood.getProtein(), 0.01);
    }
}
