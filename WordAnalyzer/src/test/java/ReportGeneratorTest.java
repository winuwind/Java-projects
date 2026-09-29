import lab1.report.ReportGenerator;
import lab1.intClass.IntClass;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class ReportGeneratorTest {

    @Test
    void testSortMore() {
        // Подготавливаем тестовые данные
        List<Map.Entry<String, IntClass>> entryList = new ArrayList<>();
        entryList.add(new AbstractMap.SimpleEntry<>("apple", new IntClass().plus(2)));
        entryList.add(new AbstractMap.SimpleEntry<>("banana", new IntClass().plus(1)));
        entryList.add(new AbstractMap.SimpleEntry<>("cherry", new IntClass().plus(3)));

        // Создаем объект ReportGenerator и сортируем по возрастанию
        ReportGenerator reportGenerator = new ReportGenerator(new ArrayList<>(entryList));
        LinkedHashMap<String, IntClass> sortedMap = reportGenerator.sort("more");

        // Проверяем, что сортировка по возрастанию работает
        List<String> keys = new ArrayList<>(sortedMap.keySet());
        assertEquals("banana", keys.get(0), "Первым должно быть 'banana'");
        assertEquals("apple", keys.get(1), "Вторым должно быть 'apple'");
        assertEquals("cherry", keys.get(2), "Третьим должно быть 'cherry'");
    }

    @Test
    void testSortLess() {
        // Подготавливаем тестовые данные
        List<Map.Entry<String, IntClass>> entryList = new ArrayList<>();
        entryList.add(new AbstractMap.SimpleEntry<>("apple", new IntClass().plus(2)));
        entryList.add(new AbstractMap.SimpleEntry<>("banana", new IntClass().plus(1)));
        entryList.add(new AbstractMap.SimpleEntry<>("cherry", new IntClass().plus(3)));

        // Создаем объект ReportGenerator и сортируем по убыванию
        ReportGenerator reportGenerator = new ReportGenerator(new ArrayList<>(entryList));
        LinkedHashMap<String, IntClass> sortedMap = reportGenerator.sort("less");

        // Проверяем, что сортировка по убыванию работает
        List<String> keys = new ArrayList<>(sortedMap.keySet());
        assertEquals("cherry", keys.getFirst(), "Первым должно быть 'cherry'");
        assertEquals("apple", keys.get(1), "Вторым должно быть 'apple'");
        assertEquals("banana", keys.get(2), "Третьим должно быть 'banana'");
    }

    @Test
    void testSortEmptyList() {
        // Создаем пустой список
        List<Map.Entry<String, IntClass>> entryList = new ArrayList<>();

        // Создаем объект ReportGenerator
        ReportGenerator reportGenerator = new ReportGenerator(new ArrayList<>(entryList));

        // Проверяем, что результат сортировки для пустого списка тоже пуст
        LinkedHashMap<String, IntClass> sortedMap = reportGenerator.sort("more");
        assertTrue(sortedMap.isEmpty(), "Для пустого списка результат должен быть пустым");
    }

    @Test
    void testSortSingleElement() {
        // Подготавливаем список с одним элементом
        List<Map.Entry<String, IntClass>> entryList = new ArrayList<>();
        entryList.add(new AbstractMap.SimpleEntry<>("apple", new IntClass().plus(1)));

        // Создаем объект ReportGenerator
        ReportGenerator reportGenerator = new ReportGenerator(new ArrayList<>(entryList));

        // Проверяем, что сортировка по возрастанию с одним элементом работает корректно
        LinkedHashMap<String, IntClass> sortedMap = reportGenerator.sort("more");
        List<String> keys = new ArrayList<>(sortedMap.keySet());
        assertEquals("apple", keys.getFirst(), "Единственным элементом должен быть 'apple'");

        // Проверяем, что сортировка по убыванию с одним элементом также корректна
        sortedMap = reportGenerator.sort("less");
        keys = new ArrayList<>(sortedMap.keySet());
        assertEquals("apple", keys.getFirst(), "Единственным элементом должен быть 'apple'");
    }

    @Test
    void testSortInvalidType() {
        // Подготавливаем тестовые данные
        List<Map.Entry<String, IntClass>> entryList = new ArrayList<>();
        entryList.add(new AbstractMap.SimpleEntry<>("apple", new IntClass().plus(2)));
        entryList.add(new AbstractMap.SimpleEntry<>("banana", new IntClass().plus(1)));

        // Создаем объект ReportGenerator
        ReportGenerator reportGenerator = new ReportGenerator(new ArrayList<>(entryList));

        // Проверяем поведение при передаче недопустимого типа сортировки
        assertThrows(IllegalArgumentException.class, () -> {
            reportGenerator.sort("invalid");
        }, "При передаче неправильного типа сортировки должна выбрасываться ошибка");
    }
}