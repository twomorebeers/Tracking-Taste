package models;

import java.time.LocalDate;

public class DietLog {
    private LocalDate date;
    private double calories;
    private double protein;
    private double carbs;
    private double fats;

    public DietLog(LocalDate date, double calories, double protein, double carbs, double fats) {
        this.date = date;
        this.calories = calories;
        this.protein = protein;
        this.carbs = carbs;
        this.fats = fats;
    }

    // Getters
    public LocalDate getDate() { return date; }
    public double getCalories() { return calories; }
    public double getProtein() { return protein; }
    public double getCarbs() { return carbs; }
    public double getFats() { return fats; }
} 