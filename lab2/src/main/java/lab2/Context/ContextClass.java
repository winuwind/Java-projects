package lab2.Context;

import java.util.HashMap;
import java.util.Stack;

public class ContextClass {
    private final Stack<Double> stack;
    private final HashMap<String, Double> map;

    public ContextClass() {
        stack = new Stack<>();
        map = new HashMap<>();
    }

    public Double get(String key) {
        return map.getOrDefault(key, Double.NEGATIVE_INFINITY);
    }

    public Stack<Double> getStack() {
        return stack;
    }

    public void add(String key, Double value) {
        map.put(key, value);
    }
}
