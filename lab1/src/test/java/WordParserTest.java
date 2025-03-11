import lab1.statistic.WordParser;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import static org.junit.jupiter.api.Assertions.*;

public class WordParserTest {

    @Test
    void testNextWord() throws IOException {
        String input = "Hello world!";
        InputStreamReader reader = new InputStreamReader(new ByteArrayInputStream(input.getBytes()));
        WordParser wordParser = new WordParser(reader);

        // Проверяем, что первый вызов nextWord() вернет "Hello"
        assertEquals("Hello", wordParser.nextWord(), "Первое слово должно быть 'Hello'");

        // Проверяем, что второй вызов nextWord() вернет "world"
        assertEquals("world", wordParser.nextWord(), "Второе слово должно быть 'world'");

        // Проверяем, что третий вызов nextWord() вернет null, так как больше слов нет
        assertNull(wordParser.nextWord(), "Третье слово должно быть null");
    }

    @Test
    void testEmptyStream() throws IOException {
        String input = "";
        InputStreamReader reader = new InputStreamReader(new ByteArrayInputStream(input.getBytes()));
        WordParser wordParser = new WordParser(reader);

        // Проверяем, что nextWord() возвращает null для пустого потока
        assertNull(wordParser.nextWord(), "Для пустого потока nextWord() должно вернуть null");
    }

    @Test
    void testWordWithApostrophe() throws IOException {
        String input = "it's a test";
        InputStreamReader reader = new InputStreamReader(new ByteArrayInputStream(input.getBytes()));
        WordParser wordParser = new WordParser(reader);

        // Проверяем, что слово "it's" будет правильно распознано, несмотря на апостроф
        assertEquals("it's", wordParser.nextWord(), "Первое слово должно быть 'it's'");

        // Проверяем, что слово "a" будет правильно распознано
        assertEquals("a", wordParser.nextWord(), "Второе слово должно быть 'a'");

        // Проверяем, что слово "test" будет правильно распознано
        assertEquals("test", wordParser.nextWord(), "Третье слово должно быть 'test'");

        // Проверяем, что возвращается null, когда больше слов нет
        assertNull(wordParser.nextWord(), "После последнего слова должно быть null");
    }

    @Test
    void testSkipNonLetterCharacters() throws IOException {
        String input = "Hello, world! 123";
        InputStreamReader reader = new InputStreamReader(new ByteArrayInputStream(input.getBytes()));
        WordParser wordParser = new WordParser(reader);

        // Проверяем, что символы, не являющиеся буквами (запятые, восклицательные знаки и цифры), пропускаются
        assertEquals("Hello", wordParser.nextWord(), "Первое слово должно быть 'Hello'");
        assertEquals("world", wordParser.nextWord(), "Второе слово должно быть 'world'");

        // Проверяем, что nextWord() вернет null после слов
        assertNull(wordParser.nextWord(), "После слов должно быть null");
    }

    @Test
    void testNoWords() throws IOException {
        String input = "12345 67890";
        InputStreamReader reader = new InputStreamReader(new ByteArrayInputStream(input.getBytes()));
        WordParser wordParser = new WordParser(reader);

        // Проверяем, что nextWord() вернет null, так как все символы ? не буквы
        assertNull(wordParser.nextWord(), "Если нет слов, nextWord() должно вернуть null");
    }
}