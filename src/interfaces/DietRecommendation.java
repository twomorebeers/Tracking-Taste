package interfaces;

import models.User;
import models.DietPlan;
import exceptions.InvalidInputException;
import java.util.Map;

public interface DietRecommendation {
    DietPlan generateRecommendations(User user, double targetCalories) throws InvalidInputException;
    Map<String, Double> calculateMacroRatio(DietPlan dietPlan);
} 