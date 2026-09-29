import lab1.argParse.ParserArguments;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParserArgumentsTest {

    @Test
    void testDefaultValues() {
        // Передаем пустой массив аргументов
        ParserArguments parser = new ParserArguments(new String[]{});

        // Проверяем значения по умолчанию
        assertEquals("csv", parser.getTypeFile(), "Тип файла должен быть csv по умолчанию");
        assertEquals("more", parser.getTypeSort(), "Тип сортировки должен быть more по умолчанию");
        assertEquals("in.txt", parser.getNameFile(), "Имя файла должно быть in.txt по умолчанию");
    }

    @Test
    void testWithFileArgument() {
        // Передаем аргумент для типа файла
        ParserArguments parser = new ParserArguments(new String[]{"-f", "json"});

        // Проверяем, что тип файла был изменен
        assertEquals("json", parser.getTypeFile(), "Тип файла должен быть json");
    }

    @Test
    void testWithSortArgument() {
        // Передаем аргумент для типа сортировки
        ParserArguments parser = new ParserArguments(new String[]{"-s", "less"});

        // Проверяем, что тип сортировки был изменен
        assertEquals("less", parser.getTypeSort(), "Тип сортировки должен быть less");
    }

    @Test
    void testWithFileAndSortArguments() {
        // Передаем оба аргумента
        ParserArguments parser = new ParserArguments(new String[]{"-f", "xml", "-s", "asc"});

        // Проверяем, что оба аргумента были правильно обработаны
        assertEquals("xml", parser.getTypeFile(), "Тип файла должен быть xml");
        assertEquals("asc", parser.getTypeSort(), "Тип сортировки должен быть asc");
    }

    @Test
    void testWithInputFileArgument() {
        // Передаем аргумент для имени файла
        ParserArguments parser = new ParserArguments(new String[]{"-i", "output.txt"});

        // Проверяем, что имя файла было изменено
        assertEquals("output.txt", parser.getNameFile(), "Имя файла должно быть output.txt");
    }

    @Test
    void testWithOutputFileArgument() {
        //Передаем аргумент для имени выходного файла
        ParserArguments parser = new ParserArguments(new String[]{"-o", "output.txt"});

        //Проверяем, что имя файла было изменено
        assertEquals("output.txt", parser.getNameOut(), "Имя выходного файла должно быть output.txt");
    }

    @Test
    void testWithUnknownArgument() {
        // Передаем неизвестный аргумент
        ParserArguments parser = new ParserArguments(new String[]{"-x", "unknown"});

        // Проверяем, что значения по умолчанию не изменились, так как аргумент неизвестен
        assertEquals("csv", parser.getTypeFile(), "Тип файла должен быть csv по умолчанию");
        assertEquals("more", parser.getTypeSort(), "Тип сортировки должен быть more по умолчанию");
        assertEquals("in.txt", parser.getNameFile(), "Имя файла должно быть in.txt по умолчанию");
    }

    @Test
    void testWithMultipleUnknownArguments() {
        // Передаем несколько неизвестных аргументов
        ParserArguments parser = new ParserArguments(new String[]{"-x", "unknown", "-y", "data"});

        // Проверяем, что значения по умолчанию не изменились, так как аргументы неизвестны
        assertEquals("csv", parser.getTypeFile(), "Тип файла должен быть csv по умолчанию");
        assertEquals("more", parser.getTypeSort(), "Тип сортировки должен быть more по умолчанию");
        assertEquals("in.txt", parser.getNameFile(), "Имя файла должно быть in.txt по умолчанию");
    }
}