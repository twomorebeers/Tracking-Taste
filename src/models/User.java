package models;

import enums.Gender;
import java.util.ArrayList;
import java.util.List;

public class User {
    private int userId;
    private String username;
    private String name;
    private double weight;
    private double height;
    private int age;
    private Gender gender;
    private UserRole role;
    private List<DietLog> dietHistory;

    public User(int userId, String username, String name, double weight, double height, int age, Gender gender, UserRole role) {
        this.userId = userId;
        this.username = username;
        this.name = name;
        this.weight = weight;
        this.height = height;
        this.age = age;
        this.gender = gender;
        this.role = role;
        this.dietHistory = new ArrayList<>();
    }

    // Getters and setters
    public int getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getName() { return name; }
    public double getWeight() { return weight; }
    public double getHeight() { return height; }
    public int getAge() { return age; }
    public Gender getGender() { return gender; }
    public UserRole getRole() { return role; }
    public List<DietLog> getDietHistory() { return new ArrayList<>(dietHistory); }

    public void setUserId(int userId) { this.userId = userId; }
    public void setUsername(String username) { this.username = username; }
    public void setName(String name) { this.name = name; }
    public void setWeight(double weight) { this.weight = weight; }
    public void setHeight(double height) { this.height = height; }
    public void setAge(int age) { this.age = age; }
    public void setGender(Gender gender) { this.gender = gender; }
    public void setRole(UserRole role) { this.role = role; }
    public void addDietLog(DietLog log) { this.dietHistory.add(log); }

    public boolean isAdmin() {
        return role == UserRole.ADMIN;
    }
}