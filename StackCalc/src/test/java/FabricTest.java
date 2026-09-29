import lab2.Command.Define.DefineCommand;
import lab2.Command.Division.DivisionCommand;
import lab2.Command.Minus.MinusCommand;
import lab2.Command.Multiplication.MultiplicationCommand;
import lab2.Command.Plus.PlusCommand;
import lab2.Command.Pop.PopCommand;
import lab2.Command.Print.PrintCommand;
import lab2.Command.Push.PushCommand;
import lab2.Command.Sqrt.SqrtCommand;
import lab2.Fabric.Fabric;
import lab2.Context.ContextClass;

import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FabricTest {
    @Test
    public void test() throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Fabric fabric = new Fabric("src/test/resources/config.txt");

        assertEquals("lab2.Command.Plus.PlusCommand", fabric.get("+"));
        assertEquals("lab2.Command.Minus.MinusCommand", fabric.get("-"));
        assertEquals("lab2.Command.Multiplication.MultiplicationCommand", fabric.get("*"));
        assertEquals("lab2.Command.Division.DivisionCommand", fabric.get("/"));
        assertEquals("lab2.Command.Sqrt.SqrtCommand", fabric.get("SQRT"));
        assertEquals("lab2.Command.Push.PushCommand", fabric.get("PUSH"));
        assertEquals("lab2.Command.Pop.PopCommand", fabric.get("POP"));
        assertEquals("lab2.Command.Define.DefineCommand", fabric.get("DEFINE"));
        assertEquals("lab2.Command.Print.PrintCommand", fabric.get("PRINT"));

        ContextClass context = new ContextClass();
        context.add("21", 123.432);

        assertTrue((new PlusCommand(context)).equals(fabric.getCommand("+", context)));
        assertTrue((new MinusCommand(context)).equals(fabric.getCommand("-", context)));
        assertTrue((new MultiplicationCommand(context)).equals(fabric.getCommand("*", context)));
        assertTrue((new DivisionCommand(context)).equals(fabric.getCommand("/", context)));
        assertTrue((new SqrtCommand(context)).equals(fabric.getCommand("SQRT", context)));
        assertTrue((new PushCommand(context)).equals(fabric.getCommand("PUSH", context)));
        assertTrue((new PopCommand(context)).equals(fabric.getCommand("POP", context)));
        assertTrue((new DefineCommand(context)).equals(fabric.getCommand("DEFINE", context)));
        assertTrue((new PrintCommand(context)).equals(fabric.getCommand("PRINT", context)));
    }
}
