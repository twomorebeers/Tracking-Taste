package models;

import java.time.LocalDateTime;

public class FoodLog {
    private int id;
    private int userId;
    private int foodId;
    private double servingSize;
    private LocalDateTime logDateTime;
    private String notes;
    private FoodItem foodItem; // For storing the associated food item details

    public FoodLog(int userId, int foodId, double servingSize, LocalDateTime logDateTime, String notes) {
        this.userId = userId;
        this.foodId = foodId;
        this.servingSize = servingSize;
        this.logDateTime = logDateTime;
        this.notes = notes;
    }

    public FoodLog(int id, int userId, int foodId, double servingSize, LocalDateTime logDateTime, String notes) {
        this(userId, foodId, servingSize, logDateTime, notes);
        this.id = id;
    }

    // Getters
    public int getId() { return id; }
    public int getUserId() { return userId; }
    public int getFoodId() { return foodId; }
    public double getServingSize() { return servingSize; }
    public LocalDateTime getLogDateTime() { return logDateTime; }
    public String getNotes() { return notes; }
    public FoodItem getFoodItem() { return foodItem; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setUserId(int userId) { this.userId = userId; }
    public void setFoodId(int foodId) { this.foodId = foodId; }
    public void setServingSize(double servingSize) { this.servingSize = servingSize; }
    public void setLogDateTime(LocalDateTime logDateTime) { this.logDateTime = logDateTime; }
    public void setNotes(String notes) { this.notes = notes; }
    public void setFoodItem(FoodItem foodItem) { this.foodItem = foodItem; }

    @Override
    public String toString() {
        String foodName = (foodItem != null) ? foodItem.getName() : "Unknown Food";
        return String.format("%s - %.1f serving(s) at %s", 
            foodName, servingSize, logDateTime.toString());
    }
}
