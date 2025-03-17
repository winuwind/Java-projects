package lab2.Command.Print;

import lab2.Command.AbstractCommand;
import lab2.Context.ContextClass;

public class PrintCommand extends AbstractCommand {
    public PrintCommand(ContextClass context) {
        super(context, "PRINT");
    }

    @Override
    public void foo(String[] args) throws Exception {
        logger.info("Before PRINT");
        printStack();
        if (context.getStack().empty()) {
            StringBuilder builder = new StringBuilder();
            for(String i: args){
                builder.append(i).append(" ");
            }
            logger.severe("command: " + builder.toString() + "\nStack is empty right now");
            throw new Exception("Stack is empty");
        }
        double x = context.getStack().peek();
        System.out.println(x);
        if (args.length != 1) {
            logger.warning("So many arguments for PRINT, but command completed");;
        }
        StringBuilder builder = new StringBuilder();
        for(String i: args){
            builder.append(i).append(" ");
        }
        logger.info("command: \"" + builder.toString() + "\" is successfully completed");
        logger.info("After PRINT");
        printStack();
    }
}
