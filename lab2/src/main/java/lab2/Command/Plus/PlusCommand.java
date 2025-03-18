package lab2.Command.Plus;

import lab2.Command.AbstractCommand;
import lab2.Context.ContextClass;

public class PlusCommand extends AbstractCommand {
    public PlusCommand(ContextClass context) {
        super(context, "+");
    }

    @Override
    public void exec(String[] args) throws Exception {
        logger.info("Before plus");
        printStack();
        double[] arguments = getArguments(2);
        context.getStack().push(arguments[0] + arguments[1]);
        writeLogs(args, 1);
    }
}
