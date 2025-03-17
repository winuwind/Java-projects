package lab2.Command.Sqrt;

import lab2.Command.AbstractCommand;
import lab2.Context.ContextClass;

import static java.lang.Math.sqrt;

public class SqrtCommand extends AbstractCommand {
    public SqrtCommand(ContextClass context) {
        super(context, "SQRT");
    }

    @Override
    public void foo(String[] args) throws Exception {
        logger.info("Before SQRT");
        printStack();
        double a;
        try {
            a = context.getStack().pop();
        }
        catch (Exception exception){
            System.out.println(exception.getMessage());
            throw new Exception(exception.getMessage());
        }
        if (a < 0.0) {
            context.getStack().push(a);
            logger.severe("SQRT " + a + ", impossible operation");
            throw new ArithmeticException("negative number");
        }
        context.getStack().push(sqrt(a));
        if (args.length != 1) {
            logger.warning("So many arguments for SQRT, but command completed");
        }
        StringBuilder builder = new StringBuilder();
        for(String i: args){
            builder.append(i).append(" ");
        }
        logger.info("command: \"" + builder.toString() + "\" is successfully completed");
        logger.info("After SQRT");
        printStack();
    }
}
