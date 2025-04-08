package lab2.Command.Multiplication;

import lab2.Command.AbstractCommand;
import lab2.Context.ContextClass;

public class MultiplicationCommand extends AbstractCommand {
    public MultiplicationCommand(ContextClass context) {
        super(context, "*");
    }

    @Override
    public void exec(String[] args) throws Exception {
        logger.info("Before multiplication");
        printStack();
        double[] arguments = getArguments(2);
        context.getStack().push(arguments[0] * arguments[1]);
        writeLogs(args, 1);
    }
}
