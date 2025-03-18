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
    private final Fabric fabric;
    private final ReaderClass reader;
    public static Logger logger = Logger.getLogger(Calculator.class.getName());

    public Calculator(String arg, Scanner scanner) {
        context = new ContextClass();
        fabric = new Fabric(arg);
        reader = new ReaderClass(scanner);
    }

    public ContextClass getContext() {
        return context;
    }

    public void calculate() {
        while (true) {
            try {
                String[] list = reader.readLine();
                StringBuilder builder = new StringBuilder();
                for (String i : list) {
                    builder.append(i).append(" ");
                }
                logger.info("Command: \"" + builder + "\"");
                if (list.length == 0 || list[0].equals("exit")) {
                    break;
                }
                String command = list[0];
                if (command.charAt(0) == '#') {
                    continue;
                }
                try {
                    AbstractCommand commandObject = fabric.getCommand(command, context);
                    if (commandObject == null) {
                        logger.warning("Command \"" + command + "\" not found");
                        System.out.println("Unknown command: " + command);
                    } else {
                        try {
                            commandObject.exec(list);
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
    }

    private static Handler getHandlerCommand() {
        try {
            FileHandler fileHandler = new FileHandler("src/main/resources/commands.log");
            fileHandler.setFormatter(new SimpleFormatter());
            return fileHandler;
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setFormatter(new SimpleFormatter());
            return consoleHandler;
        }
    }

    private static Handler getHandler() {
        try {
            FileHandler fileHandler = new FileHandler("src/main/resources/calculator.log");
            fileHandler.setFormatter(new SimpleFormatter());
            return fileHandler;
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setFormatter(new SimpleFormatter());
            return consoleHandler;
        }
    }

    public static void main(String[] args) {
        File file;
        Scanner scanner = new Scanner(System.in);
        Level level = Level.WARNING;
        if (args.length == 0 || args.length > 3) {
            System.out.println("Usage: java Calculator <configFile> <inFile = stdin> <level = WARNING>");
            return;
        } else if (args.length == 2) {
            if (!args[1].equals("stdin")) {
                try {
                    file = new File(args[1]);
                    scanner = new Scanner(file);
                } catch (Exception exception) {
                    System.out.println(exception.getMessage());
                    return;
                }
            }
        } else if (args.length == 3) {
            if (!args[1].equals("stdin")) {
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

        AbstractCommand.logger.setLevel(level);
        logger.setLevel(level);
        Handler handlerCommands = getHandlerCommand();
        AbstractCommand.logger.addHandler(handlerCommands);
        Handler handler = getHandler();
        logger.addHandler(handler);

        Calculator calculator;
        try {
            calculator = new Calculator(args[0], scanner);
        } catch (RuntimeException exception) {
            System.out.println(exception.getMessage());
            return;
        }

        calculator.calculate();

        handlerCommands.close();
        handler.close();
    }
}
