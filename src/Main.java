import models.FoodItem;
import models.NutritionTracker;
import models.User;
import enums.FoodCategory;
import implementations.JsonDataPersistence;
import utils.FilePathManager;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            FilePathManager.ensureDataDirectoryExists();
            JsonDataPersistence dataPersistence = new JsonDataPersistence();
            NutritionTracker tracker = new NutritionTracker();
            ObjectMapper mapper = new ObjectMapper();

            // Load foods from JSON
            File foodsFile = new File(FilePathManager.getFoodsFilePath());
            if (foodsFile.exists()) {
                List<FoodItem> foods = mapper.readValue(foodsFile, 
                    new TypeReference<List<FoodItem>>() {});
                foods.forEach(tracker::addFoodItem);
            } else {
                // Add default foods if file doesn't exist
                FoodItem banana = new FoodItem("Banana", 105, 1.3, 27, 0.3, FoodCategory.FRUITS);
                FoodItem chicken = new FoodItem("Chicken Breast", 165, 31, 0, 3.6, FoodCategory.PROTEINS);
                FoodItem rice = new FoodItem("Brown Rice", 216, 5, 45, 1.8, FoodCategory.GRAINS);

                tracker.addFoodItem(banana);
                tracker.addFoodItem(chicken);
                tracker.addFoodItem(rice);

                // Save to JSON
                mapper.writeValue(foodsFile, tracker.getAllFoodItems());
            }

            // Print daily totals
            System.out.println("Daily Nutrition Summary:");
            System.out.println("Total Calories: " + tracker.getTotalCalories());
            System.out.println("Total Protein: " + tracker.getTotalProtein() + "g");
            System.out.println("Total Carbs: " + tracker.getTotalCarbs() + "g");
            System.out.println("Total Fats: " + tracker.getTotalFats() + "g");

        } catch (IOException e) {
            System.err.println("Error handling data files: " + e.getMessage());
        }
    }
}
