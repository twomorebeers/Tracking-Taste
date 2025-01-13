import org.junit.Test;
import static org.junit.Assert.*;
import models.FoodItem;
import enums.FoodCategory;

public class FoodItemTest {
    @Test
    public void testFoodItemConstructor() {
        FoodItem food = new FoodItem("Apple", 95, 0.5, 25, 0.3, FoodCategory.FRUITS);
        
        assertEquals("Apple", food.getName());
        assertEquals(95, food.getCalories(), 0.01);
        assertEquals(0.5, food.getProtein(), 0.01);
        assertEquals(25, food.getCarbs(), 0.01);
        assertEquals(0.3, food.getFats(), 0.01);
        assertEquals(FoodCategory.FRUITS, food.getCategory());
    }

    @Test
    public void testNutritionCalculations() {
        FoodItem food = new FoodItem("Chicken", 165, 31, 0, 3.6, FoodCategory.PROTEINS);
        
        // Test total calories calculation (4 * protein + 4 * carbs + 9 * fats)
        double expectedCalories = (31 * 4) + (0 * 4) + (3.6 * 9);
        assertEquals(expectedCalories, food.calculateTotalCalories(), 0.01);
    }

    @Test
    public void testInvalidValues() {
        try {
            FoodItem food = new FoodItem("Invalid", -100, -1, -5, -2, FoodCategory.PROTEINS);
            fail("Should throw IllegalArgumentException for negative values");
        } catch (IllegalArgumentException e) {
            // Test passed
        }
    }
}
