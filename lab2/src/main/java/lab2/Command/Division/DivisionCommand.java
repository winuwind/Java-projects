package lab2.Command.Division;

import lab2.Command.AbstractCommand;
import lab2.Context.ContextClass;

public class DivisionCommand extends AbstractCommand {
    public DivisionCommand(ContextClass context) {
        super(context, "/");
    }

    @Override
    public void exec(String[] args) throws Exception {
        logger.info("Before division");
        printStack();
        double[] arguments = getArguments(2);
        if (arguments[1] == 0.0) {
            context.getStack().push(arguments[1]);
            context.getStack().push(arguments[0]);
            logger.severe("div " + arguments[0] + " " + arguments[1] + ", impossible operation");
            throw new ArithmeticException("Division by zero");
        }
        context.getStack().push(arguments[0] / arguments[1]);
        writeLogs(args, 1);
    }
}
