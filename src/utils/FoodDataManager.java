package utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.FoodItem;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class FoodDataManager {
    private static final String FOOD_DATABASE_FILE = "data/foods.json";
    private final ObjectMapper objectMapper;

    public FoodDataManager() {
        this.objectMapper = new ObjectMapper();
        createDataDirectoryIfNotExists();
    }

    public void saveFoodItems(List<FoodItem> foodItems) throws IOException {
        objectMapper.writeValue(new File(FOOD_DATABASE_FILE), foodItems);
    }

    public List<FoodItem> loadFoodItems() throws IOException {
        File file = new File(FOOD_DATABASE_FILE);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        return objectMapper.readValue(file, new TypeReference<List<FoodItem>>() {});
    }

    private void createDataDirectoryIfNotExists() {
        File dataDir = new File("data");
        if (!dataDir.exists()) {
            dataDir.mkdir();
        }
    }
} 