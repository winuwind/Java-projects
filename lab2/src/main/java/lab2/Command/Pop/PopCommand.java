package lab2.Command.Pop;

import lab2.Command.AbstractCommand;
import lab2.Context.ContextClass;

public class PopCommand extends AbstractCommand {
    public PopCommand(ContextClass context) {
        super(context, "POP");
    }

    @Override
    public void foo(String[] args) throws Exception {
        logger.info("Before POP");
        printStack();
        if (context.getStack().empty()) {
            StringBuilder builder = new StringBuilder();
            for(String i: args){
                builder.append(i).append(" ");
            }
            logger.severe("command: " + builder.toString() + "\nStack is empty right now");
            throw new Exception("Stack is empty");
        }
        double x = context.getStack().pop();
        if (args.length != 1) {
            logger.warning("So many arguments for POP, but command completed");
        }
        StringBuilder builder = new StringBuilder();
        for(String i: args){
            builder.append(i).append(" ");
        }
        logger.info("command: \"" + builder.toString() + "\" is successfully completed");
        logger.info("After POP");
        printStack();
    }
}
