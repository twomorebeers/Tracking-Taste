package ui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import models.FoodItem;
import models.NutritionTracker;
import database.dao.FoodDAO;
import java.util.List;

public class TrackingTasteUI extends Application {
    private NutritionTracker tracker;
    private FoodDAO foodDAO;

    @Override
    public void start(Stage primaryStage) {
        tracker = new NutritionTracker();
        foodDAO = new FoodDAO();

        // Create main layout
        BorderPane mainLayout = new BorderPane();
        
        // Create top menu
        MenuBar menuBar = createMenuBar();
        mainLayout.setTop(menuBar);

        // Create dashboard
        VBox dashboard = createDashboard();
        mainLayout.setCenter(dashboard);

        // Create scene
        Scene scene = new Scene(mainLayout, 1000, 700);
        scene.getStylesheets().add(getClass().getResource("/styles/style.css").toExternalForm());

        // Configure stage
        primaryStage.setTitle("Tracking Taste");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();
        
        Menu fileMenu = new Menu("File");
        MenuItem exportItem = new MenuItem("Export Data");
        MenuItem exitItem = new MenuItem("Exit");
        fileMenu.getItems().addAll(exportItem, exitItem);

        Menu helpMenu = new Menu("Help");
        MenuItem aboutItem = new MenuItem("About");
        helpMenu.getItems().add(aboutItem);

        menuBar.getMenus().addAll(fileMenu, helpMenu);
        return menuBar;
    }

    private VBox createDashboard() {
        VBox dashboard = new VBox(20);
        dashboard.setPadding(new Insets(20));
        dashboard.setAlignment(Pos.TOP_CENTER);

        // Nutrition Summary Section
        TitledPane nutritionSummary = createNutritionSummarySection();
        
        // Recent Foods Section
        TitledPane recentFoods = createRecentFoodsSection();
        
        // Quick Add Section
        TitledPane quickAdd = createQuickAddSection();

        dashboard.getChildren().addAll(nutritionSummary, recentFoods, quickAdd);
        return dashboard;
    }

    private TitledPane createNutritionSummarySection() {
        VBox content = new VBox(10);
        content.setPadding(new Insets(10));

        // Create nutrition chart
        PieChart nutritionChart = new PieChart();
        nutritionChart.setTitle("Today's Nutrition");
        nutritionChart.getData().addAll(
            new PieChart.Data("Protein", 30),
            new PieChart.Data("Carbs", 50),
            new PieChart.Data("Fat", 20)
        );

        content.getChildren().add(nutritionChart);
        
        TitledPane section = new TitledPane("Nutrition Summary", content);
        section.setExpanded(true);
        return section;
    }

    private TitledPane createRecentFoodsSection() {
        VBox content = new VBox(10);
        content.setPadding(new Insets(10));

        ListView<String> recentFoodsList = new ListView<>();
        List<FoodItem> recentFoods = foodDAO.getRecentFoods(5);
        recentFoods.forEach(food -> 
            recentFoodsList.getItems().add(food.getName() + " - " + food.getCalories() + " calories")
        );

        content.getChildren().add(recentFoodsList);
        
        TitledPane section = new TitledPane("Recent Foods", content);
        section.setExpanded(true);
        return section;
    }

    private TitledPane createQuickAddSection() {
        VBox content = new VBox(10);
        content.setPadding(new Insets(10));

        TextField foodNameField = new TextField();
        foodNameField.setPromptText("Food name");

        TextField caloriesField = new TextField();
        caloriesField.setPromptText("Calories");

        Button addButton = new Button("Add Food");
        addButton.getStyleClass().add("primary-button");

        content.getChildren().addAll(foodNameField, caloriesField, addButton);
        
        TitledPane section = new TitledPane("Quick Add", content);
        section.setExpanded(true);
        return section;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
