package lab2.Command.Define;

import lab2.Command.AbstractCommand;
import lab2.Context.ContextClass;

public class DefineCommand extends AbstractCommand {
    public DefineCommand(ContextClass context) {
        super(context, "DEFINE");
    }

    @Override
    public void exec(String[] args) throws Exception {
        logger.info("Before DEFINE");
        printStack();
        checkArgs(args, 3);
        try {
            context.add(args[1], Double.parseDouble(args[2]));
        } catch (NumberFormatException exception) {
            logger.severe("command: " + args[2] + " is not a number");
            throw new NumberFormatException(exception.getMessage());
        }
        writeLogs(args, 3);
    }
}
