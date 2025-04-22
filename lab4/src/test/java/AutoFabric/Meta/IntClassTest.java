package AutoFabric.Meta;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class IntClassTest {

    @Test
    void testInitialValue() {
        IntClass intClass = new IntClass();
        // Проверяем, что начальное значение value равно 0
        assertEquals(0, intClass.getValue(), "Начальное значение должно быть 0");
    }

    @Test
    void testPlusMethod() {
        IntClass intClass = new IntClass();
        intClass.plus(5);
        assertEquals(5, intClass.getValue(), "Значение должно быть 5 после добавления 5");
    }

    @Test
    void testPlusMultipleTimes() {
        IntClass intClass = new IntClass();
        intClass.plus(3).plus(2);
        assertEquals(5, intClass.getValue(), "Значение должно быть 5 после добавления 3 и 2");
    }

    @Test
    void testNegativeValues() {
        IntClass intClass = new IntClass();
        intClass.plus(-5);
        assertEquals(-5, intClass.getValue(), "Значение должно быть -5 после добавления -5");
    }

    @Test
    void testChainingPlus() {
        IntClass intClass = new IntClass();
        intClass.plus(5).plus(10).plus(3);
        assertEquals(18, intClass.getValue(), "Значение должно быть 18 после цепочки добавлений");
    }
}