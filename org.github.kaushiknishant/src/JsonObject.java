import java.util.HashMap;
import java.util.Map;

public class JsonObject implements JsonElement{
    Map<String, JsonElement> properties;

    public JsonObject(Map<String, JsonElement> properties) {
        this.properties = properties;
    }

    @Override
    public Object getValue() {
        Map<String, Object> finalProperties = new HashMap<>();
        properties.forEach((key, value) -> {
            if (value != null) {
                finalProperties.put(key, value.getValue());
            } else {
                finalProperties.put(key, null);
            }
        });

        return finalProperties;
    }
}
