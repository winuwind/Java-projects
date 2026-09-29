package lab1.controller;

import lab1.argParse.ParserArguments;
import lab1.formatter.Formatter;
import lab1.report.ReportGenerator;
import lab1.statistic.*;

import java.io.*;
import java.util.Objects;

public class Controller {
    private static WordStatistic parseStream(InputStreamReader reader) throws IOException {
        WordParser parser = new WordParser(reader);
        WordStatistic statistic = new WordStatistic();
        String word;
        do {
            word = parser.nextWord();
            if (word != null && !word.isEmpty()) {
                statistic.addWord(word);
            }
        } while (word != null);
        return statistic;
    }

    private static BufferedWriter makeCSVFile(String nameOut) throws IOException {
        File file = new File(nameOut + ".csv");
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writer.write("word,count,frequency(%)\n");
        return writer;
    }

    private static BufferedWriter makeHTMLFile(String nameOut) throws IOException {
        File file = new File(nameOut + ".html");
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writer.write("<table>\n<style>\ntable{\nborder: 3px solid red;\nborder-collapse: collapse;\nwidth: 20%;\n}\nth{\nborder: 2px solid black;\ncolor: green;\n}\ntd{\nborder: 1px solid grey;\n}\n</style>\n<tr>\n<th>word</th><th>count</th><th>frequency(%)</th>\n</tr>\n");
        return writer;
    }

    public static void main(String[] args) throws IOException {
        ParserArguments arguments = new ParserArguments(args);
        String typeFile = arguments.getTypeFile();
        String typeSort = arguments.getTypeSort();
        String nameFile = arguments.getNameFile();
        FileInputStream fis = new FileInputStream(nameFile);
        InputStreamReader isr = new InputStreamReader(fis);
        WordStatistic statistic = parseStream(isr);
        ReportGenerator gen = new ReportGenerator(statistic.getEntry());
        Formatter stream = new Formatter(gen.sort(typeSort), typeFile);
        BufferedWriter writer;
        if (Objects.equals(typeFile, "csv")) {
            writer = makeCSVFile(arguments.getNameOut());
        } else if (Objects.equals(typeFile, "html")) {
            writer = makeHTMLFile(arguments.getNameOut());
        } else {
            System.err.println("Unsupported type of file: " + typeFile);
            return;
        }
        String data = stream.next();
        while (data != null) {
            writer.write(data);
            data = stream.next();
        }
        if (Objects.equals(typeFile, "html")) {
            writer.write("</table>");
        }
        writer.close();

        System.out.println("test");
    }
}
