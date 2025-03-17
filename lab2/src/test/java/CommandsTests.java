import lab2.Command.*;
import lab2.Command.Define.DefineCommand;
import lab2.Command.Division.DivisionCommand;
import lab2.Command.Minus.MinusCommand;
import lab2.Command.Multiplication.MultiplicationCommand;
import lab2.Command.Plus.PlusCommand;
import lab2.Command.Pop.PopCommand;
import lab2.Command.Print.PrintCommand;
import lab2.Command.Push.PushCommand;
import lab2.Command.Sqrt.SqrtCommand;
import lab2.Context.ContextClass;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class CommandsTests {
    private final ContextClass context = new ContextClass();

    @BeforeEach
    void setUp() throws Exception {
        while (!context.getStack().empty()) {
            context.getStack().pop();
        }
        context.add("a", 123.432);
        context.add("b", 456.789);
        context.add("zero", 0.0);
        context.add("c", -143.1960);
    }

    @Test
    void PushTest() throws Exception {
        Command cmd = new PushCommand(context);

        //Test with wrong number of arguments
        String[] push_args1 = new String[]{"PUSH", "11", "13"};
        assertThrows(Exception.class, () ->
                cmd.foo(push_args1),
                "При передачи неправильного количества аргументов должна быть ошибка");

        //Test with non-numeric argument
        String[] push_args2 = new String[]{"PUSH", "z"};
        assertThrows(NumberFormatException.class, () ->
                        cmd.foo(push_args2),
                "При передачи нечислового значения должна быть ошибка");

        //Test with non-numeric argument from context
        String[] push_args3 = new String[]{"PUSH", "a"};
        cmd.foo(push_args3);
        assertEquals(context.get("a"), context.getStack().peek());

        //Test with numeric argument
        String[] push_args4 = new String[]{"PUSH", "11.231"};
        cmd.foo(push_args4);
        assertEquals(11.231, context.getStack().peek());
    }

    @Test
    void PopTest() throws Exception {
        Command cmd = new PopCommand(context);

        //Test with empty stack
        String[] pop_args1 = new String[]{"POP"};
        assertThrows(Exception.class, () ->
                cmd.foo(pop_args1),
                "При вызове функции для пустого стека должна быть ошибка");

        context.getStack().push(11.2);
        context.getStack().push(-13.12);

        //Test with wrong number of arguments
        String[] pop_args2 = new String[]{"POP", "11", "13"};
        cmd.foo(pop_args2);
        assertEquals(11.2, context.getStack().peek());

        context.getStack().push(15.1);

        //Test with right arguments
        String[] pop_args3 = new String[]{"POP"};
        cmd.foo(pop_args3);
        assertEquals(11.2, context.getStack().peek());
    }

    @Test
    void PrintTest() throws Exception {
        Command cmd = new PrintCommand(context);

        //Test with empty stack
        String[] print_args1 = new String[]{"PRINT"};
        assertThrows(Exception.class, () ->
                        cmd.foo(print_args1),
                "При вызове функции для пустого стека должна быть ошибка");

        context.getStack().push(11.2);

        //Test with wrong number of arguments
        String[] print_args2 = new String[]{"PRINT", "11", "13"};
        cmd.foo(print_args2);
        assertEquals(11.2, context.getStack().peek());

        //Test with right arguments
        String[] print_args3 = new String[]{"POP"};
        cmd.foo(print_args3);
        assertEquals(11.2, context.getStack().peek());
    }

    @Test
    void DefineTest() throws Exception {
        Command cmd = new DefineCommand(context);

        //Test with wrong number of arguments
        String[] define_args1 = new String[]{"DEFINE", "11", "13", "21"};
        assertThrows(Exception.class, () ->
                        cmd.foo(define_args1),
                "При передачи неправильного количества аргументов должна быть ошибка");

        //Test with non-numeric argument
        String[] define_args2 = new String[]{"PUSH", "z", "q"};
        assertThrows(NumberFormatException.class, () ->
                        cmd.foo(define_args2),
                "При передачи нечислового значения должна быть ошибка");

        //Test with define numeric
        String[] define_args3 = new String[]{"DEFINE", "111", "13.2"};
        cmd.foo(define_args3);
        assertEquals(13.2, context.get("111"));

        //Test with define numeric
        String[] define_args4 = new String[]{"DEFINE", "d", "13.2"};
        cmd.foo(define_args4);
        assertEquals(13.2, context.get("d"));
    }

    @Test
    void MinusTest() throws Exception {
        Command cmd = new MinusCommand(context);

        //Test with empty stack
        String[] minus_args1 = new String[]{"-"};
        assertThrows(Exception.class, () ->
                        cmd.foo(minus_args1),
                "При вызове с пустым стеком должна быть ошибка");

        context.getStack().push(11.2);

        //Test with only one numeric on stack
        String[] minus_args2 = new String[]{"-"};
        assertThrows(Exception.class, () ->
                        cmd.foo(minus_args2),
                "При вызове с одним числом на стеке должна быть ошибка");
        //Stack must preserve its state
        assertEquals(11.2, context.getStack().peek());

        context.getStack().push(13.2);

        //Test with wrong number of arguments
        String[] minus_args3 = new String[]{"-", "x"};
        cmd.foo(minus_args3);
        assertEquals(2.0, context.getStack().peek());
        assertEquals(1, context.getStack().size());

        context.getStack().push(1.0);

        //Test with right arguments
        String[] minus_args4 = new String[]{"-"};
        cmd.foo(minus_args4);
        assertEquals(-1.0, context.getStack().peek());
        assertEquals(1, context.getStack().size());
    }

    @Test
    void PlusTest() throws Exception {
        Command cmd = new PlusCommand(context);

        //Test with empty stack
        String[] plus_args1 = new String[]{"+"};
        assertThrows(Exception.class, () ->
                        cmd.foo(plus_args1),
                "При вызове с пустым стеком должна быть ошибка");

        context.getStack().push(11.2);

        //Test with only one numeric on stack
        String[] plus_args2 = new String[]{"+"};
        assertThrows(Exception.class, () ->
                        cmd.foo(plus_args2),
                "При вызове с одним числом на стеке должна быть ошибка");
        //Stack must preserve its state
        assertEquals(11.2, context.getStack().peek());

        context.getStack().push(13.2);

        //Test with wrong number of arguments
        String[] plus_args3 = new String[]{"+", "x"};
        cmd.foo(plus_args3);
        assertEquals(24.4, context.getStack().peek());
        assertEquals(1, context.getStack().size());

        context.getStack().push(1.0);

        //Test with right arguments
        String[] plus_args4 = new String[]{"+"};
        cmd.foo(plus_args4);
        assertEquals(25.4, context.getStack().peek());
        assertEquals(1, context.getStack().size());
    }

    @Test
    void MultiplicationTest() throws Exception {
        Command cmd = new MultiplicationCommand(context);

        //Test with empty stack
        String[] multiplication_args1 = new String[]{"*"};
        assertThrows(Exception.class, () ->
                        cmd.foo(multiplication_args1),
                "При вызове с пустым стеком должна быть ошибка");

        context.getStack().push(11.2);

        //Test with only one numeric on stack
        String[] multiplication_args2 = new String[]{"*"};
        assertThrows(Exception.class, () ->
                        cmd.foo(multiplication_args2),
                "При вызове с одним числом на стеке должна быть ошибка");
        //Stack must preserve its state
        assertEquals(11.2, context.getStack().peek());

        context.getStack().push(13.2);

        //Test with wrong number of arguments
        String[] multiplication_args3 = new String[]{"*", "x"};
        cmd.foo(multiplication_args3);
        assertEquals(147.83999999999997, context.getStack().peek());
        assertEquals(1, context.getStack().size());

        context.getStack().push(1.5);

        //Test with right arguments
        String[] multiplication_args4 = new String[]{"*"};
        cmd.foo(multiplication_args4);
        assertEquals(221.75999999999996, context.getStack().peek());
        assertEquals(1, context.getStack().size());
    }

    @Test
    void DivisionTest() throws Exception {
        Command cmd = new DivisionCommand(context);

        //Test with empty stack
        String[] division_args1 = new String[]{"/"};
        assertThrows(Exception.class, () ->
                        cmd.foo(division_args1),
                "При вызове с пустым стеком должна быть ошибка");

        context.getStack().push(11.0);

        //Test with only one numeric on stack
        String[] division_args2 = new String[]{"/"};
        assertThrows(Exception.class, () ->
                        cmd.foo(division_args2),
                "При вызове с одним числом на стеке должна быть ошибка");
        //Stack must preserve its state
        assertEquals(11.0, context.getStack().peek());

        context.getStack().push(13.2);

        //Test with wrong number of arguments
        String[] division_args3 = new String[]{"/", "x"};
        cmd.foo(division_args3);
        assertEquals(1.2, context.getStack().peek());
        assertEquals(1, context.getStack().size());

        context.getStack().push(4.8);

        //Test with right arguments
        String[] division_args4 = new String[]{"/"};
        cmd.foo(division_args4);
        assertEquals(4.0, context.getStack().peek());
        assertEquals(1, context.getStack().size());

        context.getStack().push(0.0);
        context.getStack().push(1.0);

        //Test with division by zero
        String[] division_args5 = new String[]{"/"};
        assertThrows(Exception.class, () ->
                cmd.foo(division_args5),
                "При делении на 0 должна быть ошибка");
        //Stack must preserve its state
        assertEquals(3, context.getStack().size());
        assertEquals(1.0, context.getStack().pop());
        assertEquals(0.0, context.getStack().pop());
        assertEquals(4.0, context.getStack().pop());
    }

    @Test
    void SqrtTest() throws Exception {
        Command cmd = new SqrtCommand(context);


        //Test with empty stack
        String[] sqrt_args1 = new String[]{"sqrt"};
        assertThrows(Exception.class, () ->
                        cmd.foo(sqrt_args1),
                "При вызове с пустым стеком должна быть ошибка");

        context.getStack().push(9.0);

        //Test with wrong number of arguments
        String[] sqrt_args3 = new String[]{"sqrt", "x"};
        cmd.foo(sqrt_args3);
        assertEquals(3.0, context.getStack().peek());
        assertEquals(1, context.getStack().size());

        context.getStack().push(0.49);

        //Test with right arguments
        String[] sqrt_args4 = new String[]{"sqrt"};
        cmd.foo(sqrt_args4);
        assertEquals(0.7, context.getStack().peek());
        assertEquals(2, context.getStack().size());

        context.getStack().push(-1.0);

        //Test sqrt with negative number
        String[] sqrt_args5 = new String[]{"sqrt"};
        assertThrows(ArithmeticException.class, () ->
                        cmd.foo(sqrt_args5),
                "При вычислении корня из отрицательного числа должна быть ошибка");
        //Stack must preserve its state
        assertEquals(3, context.getStack().size());
        assertEquals(-1.0, context.getStack().pop());
        assertEquals(0.7, context.getStack().pop());
        assertEquals(3.0, context.getStack().pop());
    }
}