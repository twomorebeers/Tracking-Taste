package implementations;

import com.fasterxml.jackson.databind.ObjectMapper;
import interfaces.DataPersistence;
import java.io.File;
import java.io.IOException;

public class JsonDataPersistence implements DataPersistence {
    private final ObjectMapper objectMapper;

    public JsonDataPersistence() {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void saveToFile(String filename, Object data) throws IOException {
        objectMapper.writeValue(new File(filename), data);
    }

    @Override
    public Object loadFromFile(String filename) throws IOException, ClassNotFoundException {
        return objectMapper.readValue(new File(filename), Object.class);
    }
} 