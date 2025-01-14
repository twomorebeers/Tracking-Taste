package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.border.EmptyBorder;

import models.NutritionTracker;
import models.FoodItem;
import enums.FoodCategory;
import database.dao.FoodDAO;
import database.dao.UserDAO;
import java.util.List;
import database.DatabaseConfig;

public class TrackingTasteSwingUI {

    private NutritionTracker tracker;
    private FoodDAO foodDAO;
    private UserDAO userDAO;
    private JComboBox<FoodItem> foodComboBox;

    public TrackingTasteSwingUI() {
        tracker = new NutritionTracker();
        foodDAO = new FoodDAO();
        userDAO = new UserDAO();
        List<FoodItem> foods = FoodDAO.getAllFoods();
        if (foods.isEmpty()) {
            System.out.println("No foods found in the database.");
        } else {
            System.out.println("Foods loaded: " + foods.size());
        }
        foodComboBox = new JComboBox<>(foods.toArray(new FoodItem[0]));
        // initializeDefaultFoods();
    }

    // private void initializeDefaultFoods() {
    //     tracker.addFoodItem(new FoodItem("Banana", 105, 1.3, 27, 0.3, FoodCategory.FRUITS));
    //     tracker.addFoodItem(new FoodItem("Chicken Breast", 165, 31, 0, 3.6, FoodCategory.PROTEINS));
    //     tracker.addFoodItem(new FoodItem("Brown Rice", 216, 5, 45, 1.8, FoodCategory.GRAINS));
    // }

    public void createAndShowGUI() {
        JFrame frame = new JFrame("Tracking Taste");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 600);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel label = new JLabel("Calorie Tracker");
        label.setFont(new Font("Arial", Font.BOLD, 24));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(label);

        JTextArea textArea = new JTextArea(10, 30);
        textArea.setEditable(false);
        textArea.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(new JScrollPane(textArea));

        List<FoodItem> foods = FoodDAO.getAllFoods();
        foodComboBox = new JComboBox<>(foods.toArray(new FoodItem[0]));
        foodComboBox.setMaximumSize(new Dimension(300, 25));
        panel.add(foodComboBox);

        JButton addButton = new JButton("Add Food");
        addButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        addButton.setBackground(new Color(70, 130, 180));
        addButton.setForeground(Color.WHITE);
        addButton.setFont(new Font("Arial", Font.BOLD, 16));
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FoodItem selectedFood = (FoodItem) foodComboBox.getSelectedItem();
                if (selectedFood != null) {
                    tracker.addFoodItem(selectedFood);
                    textArea.setText(getNutritionSummary());
                }
            }
        });
        panel.add(addButton);

        JButton button = new JButton("Show Summary");
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setBackground(new Color(60, 179, 113));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String summary = getNutritionSummary();
                textArea.setText(summary);
            }
        });
        panel.add(button);

        frame.add(panel);
        frame.setVisible(true);
    }

    private String getNutritionSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append("Daily Nutrition Summary:\n");
        summary.append("Total Calories: " + tracker.getTotalCalories() + "\n");
        summary.append("Total Protein: " + tracker.getTotalProtein() + "g\n");
        summary.append("Total Carbs: " + tracker.getTotalCarbs() + "g\n");
        summary.append("Total Fats: " + tracker.getTotalFats() + "g\n");
        return summary.toString();
    }

    public static void main(String[] args) {
        DatabaseConfig.testConnection();
        SwingUtilities.invokeLater(() -> {
            TrackingTasteSwingUI ui = new TrackingTasteSwingUI();
            ui.createAndShowGUI();
        });
    }
}
