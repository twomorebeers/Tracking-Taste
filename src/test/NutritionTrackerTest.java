package test;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import models.NutritionTracker;
import models.FoodItem;
import enums.FoodCategory;

public class NutritionTrackerTest {
    private NutritionTracker tracker;
    private FoodItem testFood;

    @Before
    public void setUp() {
        tracker = new NutritionTracker();
        testFood = new FoodItem("Test Food", 100, 10, 20, 5, FoodCategory.OTHER);
    }

    @Test
    public void testAddFoodItem() {
        tracker.addFoodItem(testFood);
        assertTrue(tracker.getAllFoodItems().contains(testFood));
    }

    @Test
    public void testNutritionTotals() {
        tracker.addFoodItem(testFood);
        assertEquals(100, tracker.getTotalCalories(), 0.01);
        assertEquals(10, tracker.getTotalProtein(), 0.01);
        assertEquals(20, tracker.getTotalCarbs(), 0.01);
        assertEquals(5, tracker.getTotalFats(), 0.01);
    }

    @Test
    public void testMultipleFoodItems() {
        FoodItem food1 = new FoodItem("Food1", 100, 10, 20, 5, FoodCategory.OTHER);
        FoodItem food2 = new FoodItem("Food2", 150, 15, 25, 7, FoodCategory.OTHER);
        
        tracker.addFoodItem(food1);
        tracker.addFoodItem(food2);
        
        assertEquals(250, tracker.getTotalCalories(), 0.01);
        assertEquals(25, tracker.getTotalProtein(), 0.01);
    }
}
