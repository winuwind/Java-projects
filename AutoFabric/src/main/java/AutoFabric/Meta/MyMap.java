package AutoFabric.Meta;

import java.util.HashMap;

public class MyMap {
    private final HashMap<String, IntClass> dictionary;

    public MyMap() {
        dictionary = new HashMap<>();
    }

    public void add(String word) {
        IntClass defaultValue = new IntClass();
        dictionary.put(word, dictionary.getOrDefault(word, defaultValue).plus(1));
    }

    public HashMap<String, IntClass> getDictionary() {
        return dictionary;
    }
}
