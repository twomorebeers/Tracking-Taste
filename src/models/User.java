package models;

import enums.Gender;
import java.util.ArrayList;
import java.util.List;

public class User {
    private String name;
    private double weight;
    private double height;
    private int age;
    private Gender gender;
    private List<DietLog> dietHistory;

    public User(String name, double weight, double height, int age, Gender gender) {
        this.name = name;
        this.weight = weight;
        this.height = height;
        this.age = age;
        this.gender = gender;
        this.dietHistory = new ArrayList<>();
    }

    // Getters and setters
    public String getName() { return name; }
    public double getWeight() { return weight; }
    public double getHeight() { return height; }
    public int getAge() { return age; }
    public Gender getGender() { return gender; }
    public List<DietLog> getDietHistory() { return new ArrayList<>(dietHistory); }

    public void setName(String name) { this.name = name; }
    public void setWeight(double weight) { this.weight = weight; }
    public void setHeight(double height) { this.height = height; }
    public void setAge(int age) { this.age = age; }
    public void setGender(Gender gender) { this.gender = gender; }
    public void addDietLog(DietLog log) { this.dietHistory.add(log); }
} 