package lab2.Command.Pop;

import lab2.Command.AbstractCommand;
import lab2.Context.ContextClass;

public class PopCommand extends AbstractCommand {
    public PopCommand(ContextClass context) {
        super(context, "POP");
    }

    @Override
    public void exec(String[] args) throws Exception {
        logger.info("Before POP");
        printStack();
        checkStack(args);
        double x = context.getStack().pop();
        writeLogs(args, 1);
    }
}
