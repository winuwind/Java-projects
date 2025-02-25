import lab1.statistic.WordStatistic;
import lab1.intClass.IntClass;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class WordStatisticTest {

    @Test
    void testAddWordNew() {
        // Создаем объект статистики
        WordStatistic wordStatistic = new WordStatistic();

        // Добавляем новое слово
        wordStatistic.addWord("hello");

        // Получаем список всех слов и их статистики
        ArrayList<Map.Entry<String, IntClass>> entries = wordStatistic.getEntry();

        // Проверяем, что слово "hello" было добавлено с количеством 1
        assertEquals(1, entries.size(), "Должен быть один элемент в списке");
        assertEquals("hello", entries.getFirst().getKey(), "Ключ должен быть 'hello'");
        assertEquals(1, entries.getFirst().getValue().getValue(), "Количество слова 'hello' должно быть 1");
    }

    @Test
    void testAddWordMultipleTimes() {
        // Создаем объект статистики
        WordStatistic wordStatistic = new WordStatistic();

        // Добавляем слово несколько раз
        wordStatistic.addWord("hello");
        wordStatistic.addWord("hello");
        wordStatistic.addWord("hello");

        // Получаем список всех слов и их статистики
        ArrayList<Map.Entry<String, IntClass>> entries = wordStatistic.getEntry();

        // Проверяем, что слово "hello" добавлено с количеством 3
        assertEquals(1, entries.size(), "Должен быть один элемент в списке");
        assertEquals("hello", entries.getFirst().getKey(), "Ключ должен быть 'hello'");
        assertEquals(3, entries.getFirst().getValue().getValue(), "Количество слова 'hello' должно быть 3");
    }

    @Test
    void testAddMultipleWords() {
        // Создаем объект статистики
        WordStatistic wordStatistic = new WordStatistic();

        // Добавляем несколько различных слов
        wordStatistic.addWord("hello");
        wordStatistic.addWord("world");

        // Получаем список всех слов и их статистики
        ArrayList<Map.Entry<String, IntClass>> entries = wordStatistic.getEntry();

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
        WordStatistic wordStatistic = new WordStatistic();

        // Добавляем несколько различных слов с разным количеством повторений
        wordStatistic.addWord("apple");
        wordStatistic.addWord("apple");
        wordStatistic.addWord("banana");

        // Получаем список всех слов и их статистики
        ArrayList<Map.Entry<String, IntClass>> entries = wordStatistic.getEntry();

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