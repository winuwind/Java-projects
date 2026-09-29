import lab2.Calculator.Calculator;
import org.junit.jupiter.api.Test;

public class CalculatorTest {
    @Test
    public void test() {
        Calculator.main(new String[]{});
        String[] args = new String[]{"src/test/resources/config.txt", "src/test/resources/Commands.txt", "INFO"};
        Calculator.main(args);
        String[] args2 = new String[]{"src/test/resources/config.txt", "src/test/resources/Commands.txt"};
        Calculator.main(args2);
        String[] args3 = new String[]{"src/test/resources/config.txt", "src/test/resources/Commands.txt", "ERROR"};
        Calculator.main(args3);
        String[] args4 = new String[]{"src/test/resources/config.txt", "src/test/resources/Commands.txt", "WARNINGS"};
        Calculator.main(args4);
    }
}
