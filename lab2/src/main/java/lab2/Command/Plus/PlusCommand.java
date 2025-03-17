package lab2.Command.Plus;

import lab2.Command.AbstractCommand;
import lab2.Context.ContextClass;

public class PlusCommand extends AbstractCommand {
    public PlusCommand(ContextClass context) {
        super(context, "+");
    }

    @Override
    public void foo(String[] args) throws Exception {
        logger.info("Before plus");
        printStack();
        double a, b;
        try {
            a = context.getStack().pop();
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            throw new Exception(exception.getMessage());
        }
        try {
            b = context.getStack().pop();
        } catch (Exception exception) {
            context.getStack().push(a);
            System.out.println(exception.getMessage());
            throw new Exception(exception.getMessage());
        }
        context.getStack().push(a + b);
        if (args.length != 1) {
            logger.warning("So many arguments for plus, but command completed");
        }
        StringBuilder builder = new StringBuilder();
        for(String i: args){
            builder.append(i).append(" ");
        }
        logger.info("command: \"" + builder.toString() + "\" is successfully completed");
        logger.info("After plus");
        printStack();
    }
}
