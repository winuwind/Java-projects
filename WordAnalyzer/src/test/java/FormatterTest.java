import lab1.formatter.Formatter;
import lab1.intClass.IntClass;

import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;

import static org.junit.jupiter.api.Assertions.*;

public class FormatterTest {

    @Test
    void testFormatterCsv() {
        // Подготовка тестовых данных
        LinkedHashMap<String, IntClass> map = new LinkedHashMap<>();
        map.put("apple", new IntClass().plus(5));
        map.put("banana", new IntClass().plus(3));
        map.put("cherry", new IntClass().plus(2));

        // Создание объекта Formatter для формата CSV
        Formatter formatter = new Formatter(map, "csv");

        // Проверка, что метод next() работает корректно
        assertEquals("apple,5,50.0\n", formatter.next(), "Первая строка должна быть для apple");
        assertEquals("banana,3,30.0\n", formatter.next(), "Вторая строка должна быть для banana");
        assertEquals("cherry,2,20.0\n", formatter.next(), "Третья строка должна быть для cherry");

        // Проверка, что next() возвращает null после окончания списка
        assertNull(formatter.next(), "После последнего элемента должно быть null");
    }

    @Test
    void testFormatterHtml() {
        // Подготовка тестовых данных
        LinkedHashMap<String, IntClass> map = new LinkedHashMap<>();
        map.put("apple", new IntClass().plus(5));
        map.put("banana", new IntClass().plus(3));
        map.put("cherry", new IntClass().plus(2));

        // Создание объекта Formatter для формата HTML
        Formatter formatter = new Formatter(map, "html");

        // Проверка, что метод next() работает корректно
        assertEquals("<tr>\n<th>apple</th>\n<td>5</td>\n<td>50.0</td>\n</tr>\n", formatter.next(), "Первая строка должна быть для apple");
        assertEquals("<tr>\n<th>banana</th>\n<td>3</td>\n<td>30.0</td>\n</tr>\n", formatter.next(), "Вторая строка должна быть для banana");
        assertEquals("<tr>\n<th>cherry</th>\n<td>2</td>\n<td>20.0</td>\n</tr>\n", formatter.next(), "Третья строка должна быть для cherry");

        // Проверка, что next() возвращает null после окончания списка
        assertNull(formatter.next(), "После последнего элемента должно быть null");
    }

    @Test
    void testFormatterEmptyMap() {
        // Подготовка пустой карты
        LinkedHashMap<String, IntClass> map = new LinkedHashMap<>();

        // Создание объекта Formatter для формата CSV
        Formatter formatter = new Formatter(map, "csv");

        // Проверка, что next() возвращает null для пустой карты
        assertNull(formatter.next(), "Для пустой карты next() должно вернуть null");
    }

    @Test
    void testFormatterInvalidFormat() {
        // Подготовка тестовых данных
        LinkedHashMap<String, IntClass> map = new LinkedHashMap<>();
        map.put("apple", new IntClass().plus(5));
        map.put("banana", new IntClass().plus(3));

        // Проверка, что строки формируются корректно, даже если формат неправильный
        assertThrows(IllegalArgumentException.class, () ->
                new Formatter(map, "invalid"),
                "При передаче неправильного типа файла должна выбрасываться ошибка");
    }
}