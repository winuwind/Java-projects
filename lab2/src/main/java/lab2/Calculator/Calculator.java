package lab2.Calculator;

import lab2.Command.AbstractCommand;
import lab2.Context.ContextClass;
import lab2.Fabric.Fabric;
import lab2.Reader.ReaderClass;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.Scanner;
import java.util.logging.*;

public class Calculator {
    private final ContextClass context;
    public static Logger logger = Logger.getLogger(Calculator.class.getName());

    public Calculator() {
        context = new ContextClass();
    }

    public ContextClass getContext() {
        return context;
    }

    public static void main(String[] args) {
        Calculator calculator = new Calculator();
        File file;
        Scanner scanner = new Scanner(System.in);
        Level level = Level.WARNING;
        if (args.length == 0 || args.length > 3) {
            System.out.println("Usage: java Calculator <configFile> <inFile = stdin> <level = WARNING>");
            return;
        }
       else if (args.length == 2) {
            if(!args[1].equals("stdin")) {
                try {
                    file = new File(args[1]);
                    scanner = new Scanner(file);
                } catch (Exception exception) {
                    System.out.println(exception.getMessage());
                    return;
                }
            }
        }
        else if (args.length == 3) {
            if(!args[1].equals("stdin")) {
                try {
                    file = new File(args[1]);
                    scanner = new Scanner(file);
                } catch (Exception exception) {
                    System.out.println(exception.getMessage());
                    return;
                }
            }
            level = switch (args[2]) {
                case "INFO" -> Level.INFO;
                case "ERROR" -> Level.SEVERE;
                default -> Level.WARNING;
            };
        }
        Fabric fabric;
        try {
            fabric = new Fabric(args[0]);
        } catch (RuntimeException exception) {
            System.out.println(exception.getMessage());
            return;
        }
        ReaderClass reader = new ReaderClass(scanner);
        AbstractCommand.logger.setLevel(level);
        logger.setLevel(level);
        Handler handlerCommands;
        try {
            FileHandler fileHandler = new FileHandler("src/main/resources/commands.log");
            fileHandler.setFormatter(new SimpleFormatter());
            handlerCommands = fileHandler;
        }
        catch (IOException exception) {
            System.out.println(exception.getMessage());
            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setFormatter(new SimpleFormatter());
            handlerCommands = consoleHandler;
        }
        AbstractCommand.logger.addHandler(handlerCommands);
        Handler handler;
        try {
            FileHandler fileHandler = new FileHandler("src/main/resources/calculator.log");
            fileHandler.setFormatter(new SimpleFormatter());
            handler = fileHandler;
        }
        catch (IOException exception) {
            System.out.println(exception.getMessage());
            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setFormatter(new SimpleFormatter());
            handler = consoleHandler;
        }
        logger.addHandler(handler);
        while (true) {
            try {
                String[] list = reader.readLine();
                StringBuilder builder = new StringBuilder();
                for(String i: list){
                    builder.append(i).append(" ");
                }
                logger.info("Command: \"" + builder.toString());
                if (list.length == 0 || list[0].equals("exit")) {
                    break;
                }
                String command = list[0];
                if (command.charAt(0) == '#') {
                    continue;
                }
                try {
                    AbstractCommand commandObject = fabric.getCommand(command, calculator.getContext());
                    if (commandObject == null) {
                        logger.warning("Command \"" + command + "\" not found");
                        System.out.println("Unknown command: " + command);
                    } else {
                        try {
                            commandObject.foo(list);
                            logger.info("Command \"" + command + "\" successfully completed");
                        } catch (Exception exception) {
                            logger.warning("Error when performing the last command");
                            System.out.println(exception.getMessage());
                            System.out.println("The stack saved the state before the command" + list[0] + "was entered");
                        }
                    }
                } catch (ClassNotFoundException exception) {
                    logger.severe("File for \"" + command + "\" not found");
                    System.out.println(exception.getMessage());
                    System.out.println("File for " + command + " not found");
                    return;
                } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                         IllegalAccessException exception) {
                    logger.severe("It is impossible to get a constructor for \"" + command + "\"");
                    System.out.println(exception.getMessage());
                    System.out.println("It is impossible to get a constructor");
                    return;
                }
            } catch (IOException exception) {
                if (exception.getMessage().equals("end of stream")) {
                    logger.info("End of stream");
                    break;
                } else {
                    logger.severe("Error when reading file");
                    logger.severe(exception.getMessage());
                    System.out.println(exception.getMessage());
                    return;
                }
            }
        }
        handlerCommands.close();
        handler.close();
    }
}
