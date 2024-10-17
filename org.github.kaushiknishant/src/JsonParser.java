import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonParser {
    private int index;
    private String json;
    private static final char OPEN_CURLY_BRACE = '{';
    private static final char CLOSE_CURLY_BRACE = '}';
    private static final char OPEN_SQUARE_BRACKET = '[';
    private static final char CLOSE_SQUARE_BRACKET = ']';
    private static final char DOUBLE_QUOTE = '"';
    private static final char COLON = ':';
    private static final char COMMA = ',';

    public JsonElement parse(String jsonString) {
        this.index = 0;
        this.json = jsonString;
        skipWhitespace();
        return parseValue();
    }

    private JsonElement parseValue() {
        char currentChar = json.charAt(index);
        if (currentChar == OPEN_CURLY_BRACE) {
            return parseObject();
        } else if (currentChar == OPEN_SQUARE_BRACKET) {
            return parseArray();
        } else if (currentChar == DOUBLE_QUOTE) {
            return parseString();
        } else if (Character.isDigit(currentChar)) {
            return parseNumber();
        } else if (currentChar == 't' || currentChar == 'f') {
            return parseBoolean();
        } else if (currentChar == 'n') {
            return parseNull();
        }
        throw new RuntimeException("Invalid JSON");
    }

    private JsonObject parseObject() {
        Map<String, JsonElement> properties = new HashMap<>();
        consume(OPEN_CURLY_BRACE);
                  skipWhitespace();
            while (json.charAt(index) != CLOSE_CURLY_BRACE) {
                String propertyName = parseString().getValue().toString();
                skipWhitespace();
            consume(COLON);
            skipWhitespace();
            JsonElement propertyValue = parseValue();
            properties.put(propertyName, propertyValue);

            skipWhitespace();

            if (json.charAt(index) == COMMA && (json.charAt(index+1) != CLOSE_CURLY_BRACE)) {
                consume(COMMA);
                skipWhitespace();
            }
        }

        consume(CLOSE_CURLY_BRACE);

        return new JsonObject(properties);
    }


    private JsonArray parseArray() {
        List<JsonElement> elements = new ArrayList<>();
        consume(OPEN_SQUARE_BRACKET);
        skipWhitespace();
        while (json.charAt(index) != CLOSE_SQUARE_BRACKET) {
            JsonElement jsonElement = parseValue();
            elements.add(jsonElement);
            skipWhitespace();
            if (json.charAt(index) == COMMA) {
                consume(COMMA);
                skipWhitespace();
            }
        }
        consume(CLOSE_SQUARE_BRACKET);
        return new JsonArray(elements);
    }

    private JsonString parseString() {
        consume(DOUBLE_QUOTE);
        StringBuilder stringBuilder = new StringBuilder();
        while (json.charAt(index) != DOUBLE_QUOTE) {
            stringBuilder.append(json.charAt(index));
            index++;
        }
        consume(DOUBLE_QUOTE);
        return new JsonString(stringBuilder.toString());
    }

    private JsonNumber parseNumber() {
        int startIndex = index;

        while (index < json.length() && (Character.isDigit(json.charAt(index))
                || json.charAt(index) == '.')) {
            index++;
        }

        String numberStr = json.substring(startIndex, index);
        return new JsonNumber(numberStr.contains(".") ?
                Double.parseDouble(numberStr) : Long.parseLong(numberStr));
    }

    private JsonBoolean parseBoolean() {
        String str = consumeWord();
        if (str.equals("true")) {
            return new JsonBoolean(true);
        } else if (str.equals("false")) {
            return new JsonBoolean(false);
        }
        throw new RuntimeException("Invalid boolean value");
    }

    private JsonElement parseNull() {
        consumeWord();
        return null;
    }

    private String consumeWord() {
        StringBuilder stringBuilder = new StringBuilder();
        while (Character.isLetter(json.charAt(index))) {
            stringBuilder.append(json.charAt(index));
            index++;
        }
        return stringBuilder.toString();
    }

    private void consume(char expected) {
        if (json.charAt(index) == expected) {
            index++;
        } else {
            throw new RuntimeException("Expected: " + expected);
        }
    }

    private void skipWhitespace() {
        if(json.length() == 0){
            throw new RuntimeException("Invalid Json");
        }
        while (Character.isWhitespace(json.charAt(index))) {
            index++;
        }

    }

}
