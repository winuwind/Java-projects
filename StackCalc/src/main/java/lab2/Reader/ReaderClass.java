package lab2.Reader;

import java.io.IOException;
import java.util.Scanner;

public class ReaderClass {
    private final Scanner scanner;

    public ReaderClass(Scanner scanner) {
        this.scanner = scanner;
    }

    public String[] readLine() throws IOException {
        return scanner.nextLine().split(" ");
    }
}
