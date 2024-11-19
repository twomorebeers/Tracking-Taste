package utils;

import java.nio.file.Path;
import java.nio.file.Paths;

public class FilePathManager {
    private static final String DATA_DIR = "data";
    
    public static String getFoodsFilePath() {
        return Paths.get(DATA_DIR, "foods.json").toString();
    }
    
    public static String getUsersFilePath() {
        return Paths.get(DATA_DIR, "users.json").toString();
    }
    
    public static void ensureDataDirectoryExists() {
        Path dataPath = Paths.get(DATA_DIR);
        if (!dataPath.toFile().exists()) {
            dataPath.toFile().mkdir();
        }
    }
} 