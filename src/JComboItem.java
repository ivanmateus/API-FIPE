public class JComboItem {
    private String key;
    private long value;

    public JComboItem(String key, long value){
        this.key = key;
        this.value = value;
    }

    @Override
    public String toString(){
        return key;
    }

    public String getKey(){
        return key;
    }

    public long getValue() {
        return value;
    }
}
