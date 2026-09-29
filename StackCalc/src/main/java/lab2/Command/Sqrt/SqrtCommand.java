package lab2.Command.Sqrt;

import lab2.Command.AbstractCommand;
import lab2.Context.ContextClass;

import static java.lang.Math.sqrt;

public class SqrtCommand extends AbstractCommand {
    public SqrtCommand(ContextClass context) {
        super(context, "SQRT");
    }

    @Override
    public void exec(String[] args) throws Exception {
        logger.info("Before SQRT");
        printStack();
        double[] arguments = getArguments(1);
        if (arguments[0] < 0.0) {
            context.getStack().push(arguments[0]);
            logger.severe("SQRT " + arguments[0] + ", impossible operation");
            throw new ArithmeticException("negative number");
        }
        context.getStack().push(sqrt(arguments[0]));
        writeLogs(args, 1);
    }
}
