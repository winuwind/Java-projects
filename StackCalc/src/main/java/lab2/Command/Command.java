package lab2.Command;

import java.util.EmptyStackException;

public interface Command {
    void exec(String[] args) throws Exception;

    void printStack();

    double[] getArguments(int count) throws Exception;

    void writeLogs(String[] args, int count);

    void checkStack(String[] args) throws EmptyStackException;

    void checkArgs(String[] args, int count) throws IllegalArgumentException;
}
