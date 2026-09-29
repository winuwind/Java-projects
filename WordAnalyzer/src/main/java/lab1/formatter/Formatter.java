package lab1.formatter;

import lab1.intClass.IntClass;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Objects;

public class Formatter {
    private final ArrayList<String> list;
    private int index;

    public Formatter(LinkedHashMap<String, IntClass> map, String format) {
        list = new ArrayList<>(0);
        index = 0;

        if (!Objects.equals(format, "csv") && !Objects.equals(format, "html")) {
            throw new IllegalArgumentException("format must be either csv or html");
        }

        int count = 0;
        for (String key : map.keySet()) {
            count += map.get(key).getValue();
        }

        for (String s : map.keySet()) {
            float x = map.get(s).getValue();
            x = x * 100 / count;
            if (Objects.equals(format, "csv")) {
                list.add(s + "," + map.get(s).getValue() + "," + x + "\n");
            } else if (Objects.equals(format, "html")) {
                list.add("<tr>\n<th>" + s + "</th>\n<td>" + map.get(s).getValue() + "</td>\n<td>" + x + "</td>\n</tr>\n");
            }
        }
    }

    public String next() {
        if (index >= list.size()) {
            return null;
        }
        return list.get(index++);
    }
}
