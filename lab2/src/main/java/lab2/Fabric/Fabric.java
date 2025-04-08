package lab2.Fabric;

import lab2.Command.AbstractCommand;
import lab2.Context.ContextClass;
import lab2.Reader.ReaderClass;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Fabric {
    HashMap<String, String> map;

    public Fabric(String filename) {
        map = new HashMap<>();
        File file;
        Scanner scanner;
        try {
            file = new File(filename);
            scanner = new Scanner(file);
        } catch (FileNotFoundException exception) {
            throw new RuntimeException(exception);
        }
        ReaderClass reader = new ReaderClass(scanner);
        while (true) {
            try {
                String[] list = reader.readLine();
                if (list == null || list.length == 0) {
                    break;
                }
                if (list.length != 2) {
                    System.out.println("Wrong format in config file");
                    continue;
                }
                map.put(list[0], list[1]);
            } catch (NoSuchElementException | IOException exception) {
                if (exception.getMessage().equals("No line found")) {
                    break;
                } else {
                    throw new RuntimeException(exception);
                }
            }
        }
    }

    public AbstractCommand getCommand(String command, ContextClass context) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        String path = map.getOrDefault(command, "");
        if (path.isEmpty()) {
            return null;
        }
        Class<?> clazz = Class.forName(path);
        return (AbstractCommand) clazz.getDeclaredConstructor(ContextClass.class).newInstance(context);
    }

    public String get(String key) {
        return map.getOrDefault(key, "");
    }
}
