package server;


public class RequestData {
    private int intValue;
    private String stringValue;
    private float floatValue;

    public void setFloatValue(float floatValue) {
        this.floatValue = floatValue;
    }

    public float getFloatValue() {
        return floatValue;
    }

    public void setIntValue(int readInt) {
        this.intValue = readInt;
    }

    public int getIntValue() {
        return intValue;
    }

    public void setStringValue(String readStr) {
        this.stringValue = readStr;
    }

    public String getStringValue() {
        return stringValue;
    }
}
