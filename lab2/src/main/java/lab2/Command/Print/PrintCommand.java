package lab2.Command.Print;

import lab2.Command.AbstractCommand;
import lab2.Context.ContextClass;

public class PrintCommand extends AbstractCommand {
    public PrintCommand(ContextClass context) {
        super(context, "PRINT");
    }

    @Override
    public void exec(String[] args) throws Exception {
        logger.info("Before PRINT");
        printStack();
        checkStack(args);
        System.out.println(context.getStack().peek());
        writeLogs(args, 1);
    }
}
