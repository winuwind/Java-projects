package lab2.Command;

import lab2.Context.ContextClass;

import java.util.ArrayList;
import java.util.EmptyStackException;
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
    public double[] getArguments(int count) throws Exception {
        double[] args = new double[count];
        for (int i = 0; i < count; i++) {
            try {
                args[i] = context.getStack().pop();
            } catch (Exception exception) {
                for (int j = i - 1; j >= 0; j--) {
                    context.getStack().push(args[j]);
                }
                throw new Exception(exception.getMessage());
            }
        }
        return args;
    }

    @Override
    public void checkArgs(String[] args, int count) throws IllegalArgumentException {
        if (args.length != count) {
            StringBuilder builder = new StringBuilder();
            for (String i : args) {
                builder.append(i).append(" ");
            }
            logger.severe("command: " + builder + "\n" + command + " must have " + count + " arguments");
            throw new IllegalArgumentException("Wrong number of arguments");
        }
    }

    @Override
    public void writeLogs(String[] args, int count) {
        if (args.length != count) {
            logger.warning("So many arguments for " + command + ", but command completed");
        }
        StringBuilder builder = new StringBuilder();
        for (String i : args) {
            builder.append(i).append(" ");
        }
        logger.info("command: \"" + builder + "\" is successfully completed");
        logger.info("after " + command);
        printStack();
    }

    @Override
    public void checkStack(String[] args) throws EmptyStackException {
        if (context.getStack().empty()) {
            StringBuilder builder = new StringBuilder();
            for (String i : args) {
                builder.append(i).append(" ");
            }
            logger.severe("command: " + builder + "\nStack is empty right now");
            throw new EmptyStackException();
        }
    }

    @Override
    public void printStack() {
        logger.info("Stack:");
        Stack<Double> stack = context.getStack();
        ArrayList<Double> list = new ArrayList<>();
        while (!stack.isEmpty()) {
            list.add(stack.pop());
        }
        for (Double d : list.reversed()) {
            logger.info(d.toString());
            stack.push(d);
        }
        logger.info("End stack");
    }

    public boolean equals(AbstractCommand object) {
        return context.equals(object.context) && command.equals(object.command);
    }
}
