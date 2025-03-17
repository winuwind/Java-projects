package lab2.Command.Define;

import lab2.Command.AbstractCommand;
import lab2.Context.ContextClass;

public class DefineCommand extends AbstractCommand {
    public DefineCommand(ContextClass context) {
        super(context, "DEFINE");
    }

    @Override
    public void foo(String[] args) throws Exception {
        logger.info("Before DEFINE");
        printStack();
        if (args.length != 3) {
            StringBuilder builder = new StringBuilder();
            for (String i : args) {
                builder.append(i).append(" ");
            }
            logger.severe("command: " + builder.toString() + "\nDEFINE must have 3 arguments");
            throw new Exception("Wrong number of arguments");
        }
        try {
            context.add(args[1], Double.parseDouble(args[2]));
        } catch (NumberFormatException exception) {
            logger.severe("command: " + args[2] + " is not a number");
            throw new NumberFormatException(exception.getMessage());
        }
        StringBuilder builder = new StringBuilder();
        for(String i: args){
            builder.append(i).append(" ");
        }
        logger.info("command: \"" + builder.toString() + "\" is successfully completed");
        logger.info("after DEFINE");
        printStack();
    }
}
