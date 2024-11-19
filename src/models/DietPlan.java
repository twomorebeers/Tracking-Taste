package models;

import enums.DietType;

public class DietPlan {
    private String name;
    private double targetCalories;
    private double targetProtein;
    private double targetCarbs;
    private double targetFats;
    private DietType dietType;

    public DietPlan(String name, double targetCalories, double targetProtein, double targetCarbs, double targetFats, DietType dietType) {
        this.name = name;
        this.targetCalories = targetCalories;
        this.targetProtein = targetProtein;
        this.targetCarbs = targetCarbs;
        this.targetFats = targetFats;
        this.dietType = dietType;
    }

    // Getters
    public String getName() { return name; }
    public double getTargetCalories() { return targetCalories; }
    public double getTargetProtein() { return targetProtein; }
    public double getTargetCarbs() { return targetCarbs; }
    public double getTargetFats() { return targetFats; }
    public DietType getDietType() { return dietType; }
} 