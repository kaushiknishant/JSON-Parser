public class JsonBoolean implements JsonElement{
    private final Boolean value;

    public JsonBoolean(Boolean value){
        this.value = value;
    }

    @Override
    public Object getValue() {
        return value;
    }
}
