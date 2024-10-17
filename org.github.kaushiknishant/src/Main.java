
public class Main {
    public static void main(String[] args) {
        String jsonString = "{\"key\": \"value\"}";
        JsonParser jsonParser = new JsonParser();
        JsonElement jsonElement = jsonParser.parse(jsonString);

        System.out.println(jsonElement.getValue());
    }
}