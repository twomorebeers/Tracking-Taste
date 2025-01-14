package models;

import enums.FoodCategory;

public class FoodItem {
    private int id;
    private String name;
    private double calories;
    private double protein;
    private double carbs;
    private double fats;
    private FoodCategory category;

    public FoodItem() {

    }

    public FoodItem(String name, double calories, double protein, double carbs, double fats, FoodCategory category) {
        this.name = name;
        this.calories = calories;
        this.protein = protein;
        this.carbs = carbs;
        this.fats = fats;
        this.category = category;
    }

    public FoodItem(int id, String name, int calories, double protein, double carbs, double fats, FoodCategory category) {
        this.id = id;
        this.name = name;
        this.calories = calories;
        this.protein = protein;
        this.carbs = carbs;
        this.fats = fats;
        this.category = category;
    }

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public double getCalories() { return calories; }
    public double getProtein() { return protein; }
    public double getCarbs() { return carbs; }
    public double getFats() { return fats; }
    public FoodCategory getCategory() { return category; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setCalories(double calories) { this.calories = calories; }
    public void setProtein(double protein) { this.protein = protein; }
    public void setCarbs(double carbs) { this.carbs = carbs; }
    public void setFats(double fats) { this.fats = fats; }
    public void setCategory(FoodCategory category) { this.category = category; }

    @Override
    public String toString() {
        return String.format("%s - Calories: %.1f, Protein: %.1fg, Carbs: %.1fg, Fats: %.1fg, Category: %s",
                name, calories, protein, carbs, fats, category);
    }

    /**
     * Calculates total calories based on macronutrients using the formula:
     * (protein * 4) + (carbs * 4) + (fats * 9)
     * @return calculated total calories
     */
    public double calculateTotalCalories() {
        return (protein * 4) + (carbs * 4) + (fats * 9);
    }
} 