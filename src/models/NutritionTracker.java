package models;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import enums.FoodCategory;

public class NutritionTracker {
    private List<FoodItem> foodItems;

    public NutritionTracker() {
        this.foodItems = new ArrayList<>();
    }

    public void addFoodItem(FoodItem item) {
        foodItems.add(item);
    }

    public List<FoodItem> getFoodItemsByCategory(FoodCategory category) {
        return foodItems.stream()
                .filter(item -> item.getCategory() == category)
                .collect(Collectors.toList());
    }

    public double getTotalCalories() {
        return foodItems.stream().mapToDouble(FoodItem::getCalories).sum();
    }

    public double getTotalProtein() {
        return foodItems.stream().mapToDouble(FoodItem::getProtein).sum();
    }

    public double getTotalCarbs() {
        return foodItems.stream().mapToDouble(FoodItem::getCarbs).sum();
    }

    public double getTotalFats() {
        return foodItems.stream().mapToDouble(FoodItem::getFats).sum();
    }

    public List<FoodItem> getAllFoodItems() {
        return new ArrayList<>(foodItems);
    }
} 