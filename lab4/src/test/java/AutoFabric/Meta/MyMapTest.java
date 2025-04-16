package AutoFabric.Meta;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class MyMapTest {

    @Test
    void testAddNew() {
        MyMap MyMap = new MyMap();

        MyMap.add("hello");
        HashMap<String, IntClass> map = MyMap.getDictionary();
        assertEquals(1, map.size());
        assertEquals(1, map.getOrDefault("hello", new IntClass()).getValue());
    }

    @Test
    void testAddMultipleTimes() {
        // Создаем объект статистики
        MyMap MyMap = new MyMap();

        // Добавляем слово несколько раз
        MyMap.add("Hello");
        MyMap.add("Hello");
        MyMap.add("Hello");
        HashMap<String, IntClass> map = MyMap.getDictionary();

        assertEquals(3, map.getOrDefault("Hello", new IntClass()).getValue());
    }

    @Test
    void testAddMultipleWords() {
        MyMap MyMap = new MyMap();

        MyMap.add("hello");
        MyMap.add("world");

        // Получаем список всех слов и их статистики
        ArrayList<Map.Entry<String, IntClass>> entries = new ArrayList<>(MyMap.getDictionary().entrySet());

        // Проверяем, что оба слова добавлены и имеют корректные значения
        assertEquals(2, entries.size(), "Должно быть два элемента в списке");

        // Проверяем, что слово "hello" присутствует и его количество равно 1
        assertTrue(entries.stream().anyMatch(entry -> entry.getKey().equals("hello") && entry.getValue().getValue() == 1),
                "Слово 'hello' должно быть добавлено с количеством 1");

        // Проверяем, что слово "world" присутствует и его количество равно 1
        assertTrue(entries.stream().anyMatch(entry -> entry.getKey().equals("world") && entry.getValue().getValue() == 1),
                "Слово 'world' должно быть добавлено с количеством 1");
    }

    @Test
    void testAddDifferentWords() {
        // Создаем объект статистики
        MyMap MyMap = new MyMap();

        // Добавляем несколько различных слов с разным количеством повторений
        MyMap.add("apple");
        MyMap.add("apple");
        MyMap.add("banana");

        // Получаем список всех слов и их статистики
        ArrayList<Map.Entry<String, IntClass>> entries = new ArrayList<>(MyMap.getDictionary().entrySet());

        // Проверяем, что список содержит два уникальных слова
        assertEquals(2, entries.size(), "Должно быть два элемента в списке");

        // Проверяем, что слово "apple" присутствует и его количество равно 2
        assertTrue(entries.stream().anyMatch(entry -> entry.getKey().equals("apple") && entry.getValue().getValue() == 2),
                "Слово 'apple' должно быть добавлено с количеством 2");

        // Проверяем, что слово "banana" присутствует и его количество равно 1
        assertTrue(entries.stream().anyMatch(entry -> entry.getKey().equals("banana") && entry.getValue().getValue() == 1),
                "Слово 'banana' должно быть добавлено с количеством 1");
    }
}