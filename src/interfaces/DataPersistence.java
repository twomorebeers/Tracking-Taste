package interfaces;

import java.io.IOException;
import models.User;
import models.DietPlan;

public interface DataPersistence {
    void saveToFile(String filename, Object data) throws IOException;
    Object loadFromFile(String filename) throws IOException, ClassNotFoundException;
} 