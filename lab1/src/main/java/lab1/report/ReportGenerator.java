package lab1.report;

import lab1.intClass.IntClass;

import java.util.*;

public class ReportGenerator {
    private final ArrayList<Map.Entry<String, IntClass>> list;

    public ReportGenerator(ArrayList<Map.Entry<String, IntClass>> entryList) {
        list = entryList;
        list.sort(Map.Entry.comparingByValue(Comparator.comparingInt(IntClass::getValue)));
    }

    public LinkedHashMap<String, IntClass> sort(String type) {
        LinkedHashMap<String, IntClass> sortedMap = new LinkedHashMap<>();
        if (type.equals("more")) {
            for (Map.Entry<String, IntClass> entry : list) {
                sortedMap.put(entry.getKey(), entry.getValue());
            }
        } else if (type.equals("less")) {
            for (Map.Entry<String, IntClass> entry : list.reversed()) {
                sortedMap.put(entry.getKey(), entry.getValue());
            }
        } else {
            throw new IllegalArgumentException("Unsupported type: " + type);
        }
        return sortedMap;
    }
}
