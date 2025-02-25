package lab1.statistic;

import lab1.intClass.IntClass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WordStatistic {
    private final HashMap<String, IntClass> dictionary;

    public WordStatistic() {
        dictionary = new HashMap<>();
    }

    public void addWord(String word) {
        IntClass defaultValue = new IntClass();
        dictionary.put(word, dictionary.getOrDefault(word, defaultValue).plus(1));
    }

    public ArrayList<Map.Entry<String, IntClass>> getEntry() {
        return new ArrayList<>(dictionary.entrySet());
    }
}
