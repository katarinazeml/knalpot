package server;

//Basic getters and setters.

public class RequestData {
    private int intValue;
    private String stringValue;

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
