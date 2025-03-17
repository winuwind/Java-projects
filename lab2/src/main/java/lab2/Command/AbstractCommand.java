package lab2.Command;

import lab2.Context.ContextClass;

import java.util.ArrayList;
import java.util.Stack;
import java.util.logging.Logger;

public abstract class AbstractCommand implements Command {
    public ContextClass context;
    public String command;
    public static final Logger logger = Logger.getLogger(AbstractCommand.class.getName());

    public AbstractCommand(ContextClass context, String command) {
        this.context = context;
        this.command = command;
    }

    @Override
    public void printStack(){
        logger.info("Stack:");
        Stack<Double> stack = context.getStack();
        ArrayList<Double> list = new ArrayList<>();
        while(!stack.isEmpty()){
            list.add(stack.pop());
        }
        for(Double d : list.reversed()){
            logger.info(d.toString());
            stack.push(d);
        }
        logger.info("End stack");
    }

    public boolean equals(AbstractCommand object){
        return context.equals(object.context) && command.equals(object.command);
    }
}
