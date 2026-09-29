package lab1.statistic;

import java.io.IOException;
import java.io.InputStreamReader;

public class WordParser {
    private final InputStreamReader reader;
    private String lastWord;

    public WordParser(InputStreamReader reader) {
        this.reader = reader;
    }

    public String nextWord() throws IOException {
        readWord();
        return lastWord;
    }

    private boolean isLetter(char c) {
        return (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || c == '\'';
    }

    private void readWord() throws IOException {
        char symbol;
        if (!reader.ready()) {
            lastWord = null;
            return;
        }
        StringBuilder word = new StringBuilder();
        while (reader.ready()) {
            symbol = (char) reader.read();
            if (isLetter(symbol)) {
                word.append(symbol);
            } else {
                if (!word.isEmpty()) {
                    break;
                }
            }
        }
        if (word.isEmpty()) {
            lastWord = null;
        } else {
            lastWord = word.toString();
        }
    }
}
