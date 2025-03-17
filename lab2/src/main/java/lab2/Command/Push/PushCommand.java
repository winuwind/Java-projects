package lab2.Command.Push;

import lab2.Command.AbstractCommand;
import lab2.Context.ContextClass;

public class PushCommand extends AbstractCommand {
    public PushCommand(ContextClass context) {
        super(context, "POP");
    }

    @Override
    public void foo(String[] args) throws Exception {
        logger.info("Before PUSH");
        printStack();
        if (args.length != 2) {
            StringBuilder builder = new StringBuilder();
            for(String i: args){
                builder.append(i).append(" ");
            }
            logger.severe("command: " + builder.toString() + "\nPUSH must have 2 arguments");
            throw new Exception("Wrong number of arguments");
        }
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
        StringBuilder builder = new StringBuilder();
        for(String i: args){
            builder.append(i).append(" ");
        }
        logger.info("command: \"" + builder.toString() + "\" is successfully completed");
        logger.info("After PUSH");
        printStack();
    }
}
