public class JsonNumber implements JsonElement{
    private final Number value;
    public JsonNumber(Number value){
        this.value = value;
    }
    @Override
    public Object getValue() {
        return value;
    }
}
