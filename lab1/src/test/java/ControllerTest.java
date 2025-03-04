import lab1.controller.Controller;

import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class ControllerTest {
    @Test
    void testWithCSVFile() throws IOException {
        String[] argv = "-i src/test/resources/input.txt -f csv -s less -o src/test/resources/out".split(" ");
        Controller.main(argv);
        FileInputStream fis_c = new FileInputStream("src/test/resources/out.csv");
        InputStreamReader isr_c = new InputStreamReader(fis_c);
        FileInputStream fis_r = new FileInputStream("src/test/resources/right.csv");
        InputStreamReader isr_r = new InputStreamReader(fis_r);
        while (isr_r.ready() && isr_c.ready()) {
            char symbol_r = (char) isr_r.read();
            char symbol_c = (char) isr_c.read();
            assertEquals(symbol_r, symbol_c);
        }
    }

    @Test
    void testWithHTMLFile() throws IOException {
        String[] argv = "-i src/test/resources/input.txt -f html -s less -o src/test/resources/out".split(" ");
        Controller.main(argv);
        FileInputStream fis_c = new FileInputStream("src/test/resources/out.html");
        InputStreamReader isr_c = new InputStreamReader(fis_c);
        FileInputStream fis_r = new FileInputStream("src/test/resources/right.html");
        InputStreamReader isr_r = new InputStreamReader(fis_r);
        while (isr_r.ready() && isr_c.ready()) {
            char symbol_r = (char) isr_r.read();
            char symbol_c = (char) isr_c.read();
            assertEquals(symbol_r, symbol_c);
        }
    }
}
