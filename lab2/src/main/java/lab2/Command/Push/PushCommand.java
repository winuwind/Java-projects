package lab2.Command.Push;

import lab2.Command.AbstractCommand;
import lab2.Context.ContextClass;

public class PushCommand extends AbstractCommand {
    public PushCommand(ContextClass context) {
        super(context, "POP");
    }

    @Override
    public void exec(String[] args) throws Exception {
        logger.info("Before PUSH");
        printStack();
        checkArgs(args, 2);
        try {
            if (context.get(args[1]) == Double.NEGATIVE_INFINITY) {
                context.getStack().push(Double.parseDouble(args[1]));
            } else {
                context.getStack().push(context.get(args[1]));
            }
        } catch (NumberFormatException exception) {
            logger.severe("command PUSH: " + args[1] + " is not a number");
            System.out.println(exception.getMessage());
            throw new NumberFormatException(exception.getMessage());
        }
        writeLogs(args, 2);
    }
}
