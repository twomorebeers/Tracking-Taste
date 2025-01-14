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
import models.User;
import models.FoodLog;
import database.dao.FoodLogDAO;
import java.time.LocalDateTime;

public class TrackingTasteSwingUI {

    private NutritionTracker tracker;
    private FoodDAO foodDAO;
    private UserDAO userDAO;
    private FoodLogDAO foodLogDAO;
    private JComboBox<FoodItem> foodComboBox;
    private JPanel panel;
    private JTextField portionField;
    private User currentUser;
    private JButton editFoodButton;
    private JButton deleteFoodButton;
    private JList<FoodLog> foodLogList;
    private DefaultListModel<FoodLog> foodLogListModel;

    public TrackingTasteSwingUI(User user) {
        this.currentUser = user;
        tracker = new NutritionTracker();
        foodDAO = new FoodDAO();
        userDAO = new UserDAO();
        foodLogDAO = new FoodLogDAO();
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
        frame.setSize(800, 600);

        // Create split pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        
        // Left panel for food logging
        JPanel leftPanel = new JPanel(new BorderLayout());
        panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Food selection
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Select Food:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        foodComboBox = new JComboBox<>();
        panel.add(foodComboBox, gbc);
        
        // Serving size
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Serving Size:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        JSpinner servingSpinner = new JSpinner(new SpinnerNumberModel(1.0, 0.1, 10.0, 0.1));
        panel.add(servingSpinner, gbc);
        
        // Notes
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Notes:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 2;
        JTextField notesField = new JTextField(20);
        panel.add(notesField, gbc);
        
        // Log button
        gbc.gridx = 1;
        gbc.gridy = 3;
        JButton logButton = new JButton("Log Food");
        logButton.addActionListener(e -> {
            FoodItem selectedFood = (FoodItem) foodComboBox.getSelectedItem();
            if (selectedFood != null) {
                double servingSize = (Double) servingSpinner.getValue();
                String notes = notesField.getText().trim();
                
                FoodLog newLog = new FoodLog(
                    currentUser.getId(),
                    selectedFood.getId(),
                    servingSize,
                    LocalDateTime.now(),
                    notes
                );
                
                foodLogDAO.addFoodLogAsync(newLog)
                    .thenAcceptAsync(success -> {
                        if (success) {
                            refreshFoodLogs();
                            servingSpinner.setValue(1.0);
                            notesField.setText("");
                        } else {
                            JOptionPane.showMessageDialog(frame,
                                "Failed to log food item.",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                        }
                    });
            }
        });
        panel.add(logButton, gbc);
        
        // Admin buttons
        if (currentUser.isAdmin()) {
            gbc.gridx = 1;
            gbc.gridy = 4;
            JPanel adminPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            
            JButton editButton = new JButton("Edit Food");
            editButton.addActionListener(e -> editSelectedFood());
            adminPanel.add(editButton);
            
            JButton deleteButton = new JButton("Delete Food");
            deleteButton.addActionListener(e -> deleteSelectedFood());
            adminPanel.add(deleteButton);
            
            panel.add(adminPanel, gbc);
        }
        
        leftPanel.add(panel, BorderLayout.NORTH);
        
        // Right panel for food log history
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createTitledBorder("Food Log History"));
        
        foodLogListModel = new DefaultListModel<>();
        foodLogList = new JList<>(foodLogListModel);
        foodLogList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                        boolean isSelected, boolean cellHasFocus) {
                FoodLog log = (FoodLog) value;
                FoodItem food = log.getFoodItem();
                String text = String.format("%s - %.1f serving(s) - %.0f calories - %s",
                    food.getName(),
                    log.getServingSize(),
                    food.getCalories() * log.getServingSize(),
                    log.getLogDateTime().toString()
                );
                return super.getListCellRendererComponent(list, text, index, isSelected, cellHasFocus);
            }
        });
        
        JScrollPane logScrollPane = new JScrollPane(foodLogList);
        rightPanel.add(logScrollPane, BorderLayout.CENTER);
        
        // Delete log button
        JButton deleteLogButton = new JButton("Delete Selected Log");
        deleteLogButton.addActionListener(e -> {
            FoodLog selectedLog = foodLogList.getSelectedValue();
            if (selectedLog != null) {
                int confirm = JOptionPane.showConfirmDialog(frame,
                    "Are you sure you want to delete this log entry?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION);
                
                if (confirm == JOptionPane.YES_OPTION) {
                    foodLogDAO.deleteFoodLogAsync(selectedLog.getId())
                        .thenAcceptAsync(success -> {
                            if (success) {
                                refreshFoodLogs();
                            } else {
                                JOptionPane.showMessageDialog(frame,
                                    "Failed to delete log entry.",
                                    "Error",
                                    JOptionPane.ERROR_MESSAGE);
                            }
                        });
                }
            }
        });
        rightPanel.add(deleteLogButton, BorderLayout.SOUTH);
        
        // Set up split pane
        splitPane.setLeftComponent(leftPanel);
        splitPane.setRightComponent(rightPanel);
        splitPane.setDividerLocation(400);
        
        frame.add(splitPane);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        
        // Load initial data
        loadFoodItems();
        refreshFoodLogs();
    }
    
    private void refreshFoodLogs() {
        foodLogDAO.getUserFoodLogsAsync(currentUser.getId())
            .thenAcceptAsync(logs -> {
                SwingUtilities.invokeLater(() -> {
                    foodLogListModel.clear();
                    for (FoodLog log : logs) {
                        foodLogListModel.addElement(log);
                    }
                });
            });
    }

    private void loadFoodItems() {
        foodDAO.getAllFoodsAsync()
            .thenAcceptAsync(foods -> {
                SwingUtilities.invokeLater(() -> {
                    foodComboBox.removeAllItems();
                    for (FoodItem food : foods) {
                        foodComboBox.addItem(food);
                    }
                });
            });
    }

    private void editSelectedFood() {
        FoodItem selectedFood = (FoodItem) foodComboBox.getSelectedItem();
        if (selectedFood == null) {
            JOptionPane.showMessageDialog(panel, "Please select a food to edit.");
            return;
        }

        System.out.println("Editing food with ID: " + selectedFood.getId()); // Debug line

        JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(panel), "Edit Food", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(400, 300);
        
        JPanel inputPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        inputPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JTextField nameField = new JTextField(selectedFood.getName());
        JTextField caloriesField = new JTextField(String.valueOf(selectedFood.getCalories()));
        JTextField proteinField = new JTextField(String.valueOf(selectedFood.getProtein()));
        JTextField carbsField = new JTextField(String.valueOf(selectedFood.getCarbs()));
        JTextField fatsField = new JTextField(String.valueOf(selectedFood.getFats()));
        JComboBox<FoodCategory> categoryCombo = new JComboBox<>(FoodCategory.values());
        categoryCombo.setSelectedItem(selectedFood.getCategory());
        
        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Calories:"));
        inputPanel.add(caloriesField);
        inputPanel.add(new JLabel("Protein (g):"));
        inputPanel.add(proteinField);
        inputPanel.add(new JLabel("Carbs (g):"));
        inputPanel.add(carbsField);
        inputPanel.add(new JLabel("Fats (g):"));
        inputPanel.add(fatsField);
        inputPanel.add(new JLabel("Category:"));
        inputPanel.add(categoryCombo);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Save Changes");
        JButton cancelButton = new JButton("Cancel");
        
        saveButton.addActionListener(e -> {
            try {
                String name = nameField.getText().trim();
                double calories = Double.parseDouble(caloriesField.getText().trim());
                double protein = Double.parseDouble(proteinField.getText().trim());
                double carbs = Double.parseDouble(carbsField.getText().trim());
                double fats = Double.parseDouble(fatsField.getText().trim());
                FoodCategory category = (FoodCategory) categoryCombo.getSelectedItem();
                
                if (name.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Please enter a name for the food item.");
                    return;
                }
                
                FoodItem updatedFood = new FoodItem(
                    selectedFood.getId(), // Keep the same ID
                    name,
                    calories,
                    protein,
                    carbs,
                    fats,
                    category
                );
                foodDAO.updateFoodAsync(selectedFood.getId(), updatedFood)
                    .thenAcceptAsync(success -> {
                        SwingUtilities.invokeLater(() -> {
                            if (success) {
                                refreshFoodLogs();
                                dialog.dispose();
                                JOptionPane.showMessageDialog(panel, "Food item updated successfully!");
                            } else {
                                JOptionPane.showMessageDialog(dialog, "Failed to update food item.");
                            }
                        });
                    });
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Please enter valid numbers for nutritional values.");
            }
        });
        
        cancelButton.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        dialog.add(inputPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }
    
    private void deleteSelectedFood() {
        FoodItem selectedFood = (FoodItem) foodComboBox.getSelectedItem();
        if (selectedFood == null) {
            JOptionPane.showMessageDialog(panel, "Please select a food to delete.");
            return;
        }
        
        System.out.println("Attempting to delete food with ID: " + selectedFood.getId()); // Debug line
        
        int confirm = JOptionPane.showConfirmDialog(panel,
            "Are you sure you want to delete '" + selectedFood.getName() + "'?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
            
        if (confirm == JOptionPane.YES_OPTION) {
            foodDAO.deleteFoodAsync(selectedFood.getId())
                .thenAcceptAsync(success -> {
                    SwingUtilities.invokeLater(() -> {
                        if (success) {
                            refreshFoodLogs();
                            JOptionPane.showMessageDialog(panel, "Food item deleted successfully!");
                        } else {
                            JOptionPane.showMessageDialog(panel, "Failed to delete food item.");
                        }
                    });
                });
        }
    }

    public static void main(String[] args) {
        DatabaseConfig.testConnection();
        
        // Create default admin user if it doesn't exist
        if (!UserDAO.userExists("admin")) {
            UserDAO.createUser("admin", "admin123", 1); // 1 is ADMIN role
        }
        
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame();
            LoginDialog loginDialog = new LoginDialog(frame);
            loginDialog.setVisible(true);
            
            if (loginDialog.isLoginSuccessful()) {
                User user = loginDialog.getAuthenticatedUser();
                TrackingTasteSwingUI ui = new TrackingTasteSwingUI(user);
                ui.createAndShowGUI();
            } else {
                System.exit(0);
            }
        });
    }
}
