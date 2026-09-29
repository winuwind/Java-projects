package lab2.Command.Minus;

import lab2.Command.AbstractCommand;
import lab2.Context.ContextClass;

public class MinusCommand extends AbstractCommand {
    public MinusCommand(ContextClass context) {
        super(context, "-");
    }

    @Override
    public void exec(String[] args) throws Exception {
        logger.info("Before minus");
        printStack();
        double[] arguments = getArguments(2);
        context.getStack().push(arguments[0] - arguments[1]);
        writeLogs(args, 1);
    }
}
