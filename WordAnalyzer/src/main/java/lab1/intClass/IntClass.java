package lab1.intClass;

public class IntClass {
    private int value;

    public IntClass() {
        value = 0;
    }

    public IntClass plus(int a) {
        value += a;
        return this;
    }

    public int getValue() {
        return value;
    }
}
