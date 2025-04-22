package AutoFabric.Main;

import AutoFabric.Dealers.Dealer;
import AutoFabric.Factory.Factory;
import AutoFabric.GUI.Graphics;
import AutoFabric.Storage.Controller;
import AutoFabric.Storage.Storage;
import AutoFabric.Suppliers.AbstractSupplier;
import AutoFabric.Suppliers.SupplierAccessory;
import AutoFabric.Suppliers.SupplierBody;
import AutoFabric.Suppliers.SupplierEngine;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

public class Main {
    private final Storage storageCars;
    private final Storage storageAccessories;
    private final Storage storageBodies;
    private final Storage storageEngines;
    private final int countWorkers;
    private final Factory factory;
    private final Controller controller;
    private final AbstractSupplier[] suppliers;
    private final Dealer[] dealers;
    private final Thread controllerThread;
    private final Thread[] supplierThreads;
    private final Thread[] dealerThreads;
    private final Graphics graphics;
    Timer timer;

    static class Settings{
        public int engineStorageSize = 100;
        public int bodyStorageSize = 100;
        public int accessoryStorageSize = 100;
        public int carStorageSize = 100;
        public int countWorkers = 8;
        public boolean logging = true;
        public HashMap<String, Integer> dealers = new HashMap<>();
        public HashMap<String, Integer> engineSuppliers = new HashMap<>();
        public HashMap<String, Integer> bodySuppliers = new HashMap<>();
        public HashMap<String, Integer> accessorySuppliers = new HashMap<>();

        public Settings(){
            dealers.put("BMW", 1);
            dealers.put("Lada", 1);
            dealers.put("Range Rover", 1);
            dealers.put("Porsche", 1);
            engineSuppliers.put("BMW M5", 1);
            engineSuppliers.put("Renault", 1);
            engineSuppliers.put("Ford", 1);
            bodySuppliers.put("BMW M5 C", 1);
            bodySuppliers.put("Granta", 1);
            bodySuppliers.put("Land Rover", 1);
            accessorySuppliers.put("Wheels", 1);
            accessorySuppliers.put("Hi-Fi", 1);
            accessorySuppliers.put("Panoramic roof", 1);
        }
    }

    public static Settings getSettings(String fileName){
        if(fileName == null){
            return new Settings();
        }
        Settings settings = new Settings();
        try{
            File file = new File(fileName);
            BufferedReader reader = new BufferedReader(new FileReader(file));
            while(reader.ready()){
                String[] line = reader.readLine().split("=");
                if(line.length != 2){
                    reader.close();
                    return new Settings();
                }
                switch(line[0]){
                    case "engineStorageSize":
                        settings.engineStorageSize = Integer.parseInt(line[1]);
                        break;
                    case "bodyStorageSize":
                        settings.bodyStorageSize = Integer.parseInt(line[1]);
                        break;
                    case "accessoryStorageSize":
                        settings.accessoryStorageSize = Integer.parseInt(line[1]);
                        break;
                    case "carStorageSize":
                        settings.carStorageSize = Integer.parseInt(line[1]);
                        break;
                    case "countWorkers":
                        settings.countWorkers = Integer.parseInt(line[1]);
                        break;
                    case "logging":
                        settings.logging = line[1].equals("true");
                        break;
                    case "dealerBMW":
                        settings.dealers.put("BMW", Integer.parseInt(line[1]));
                        break;
                    case "dealerLada":
                        settings.dealers.put("Lada", Integer.parseInt(line[1]));
                        break;
                    case "dealerRangeRover":
                        settings.dealers.put("Range Rover", Integer.parseInt(line[1]));
                        break;
                    case "supplierBMWm5":
                        settings.engineSuppliers.put("BMW M5", Integer.parseInt(line[1]));
                        break;
                    case "supplierRenault":
                        settings.engineSuppliers.put("Renault", Integer.parseInt(line[1]));
                        break;
                    case "supplierRangeRover":
                        settings.engineSuppliers.put("Ford", Integer.parseInt(line[1]));
                        break;
                    case "supplierBMWm5C":
                        settings.bodySuppliers.put("BMW M5 C", Integer.parseInt(line[1]));
                        break;
                    case "supplierGranta":
                        settings.accessorySuppliers.put("Granta", Integer.parseInt(line[1]));
                        break;
                    case "supplierLandRover":
                        settings.bodySuppliers.put("Land Rover", Integer.parseInt(line[1]));
                        break;
                    case "supplierWheels":
                        settings.accessorySuppliers.put("Wheels", Integer.parseInt(line[1]));
                        break;
                    case "supplierHiFi":
                        settings.bodySuppliers.put("Hi-Fi", Integer.parseInt(line[1]));
                        break;
                    case "supplierPanoramicRoof":
                        settings.bodySuppliers.put("Panoramic roof", Integer.parseInt(line[1]));
                        break;
                    default:
                        if(line[0].startsWith("dealer")){
                            String nameCar = line[0].substring("dealer".length());
                            settings.dealers.put(nameCar, Integer.parseInt(line[1]));
                        }
                        break;
                }
            }
            reader.close();
        }
        catch(Exception e){
            return new Settings();
        }
        return settings;
    }

    public static void main(String[] args) throws InterruptedException {
        String filepath = null;
        for(int i = 0; i < args.length; i++){
            if(args[i].equals("-f") && args.length > i + 1){
                filepath = args[++i];
            }
        }
        Settings settings = getSettings(filepath);

        Main main = new Main(settings);
    }

    public Main(Settings settings){
        Dealer.setLogging(settings.logging);
        storageCars = new Storage("Car", settings.carStorageSize);
        storageEngines  = new Storage("Engine", settings.engineStorageSize);
        storageBodies = new Storage("Bodies", settings.bodyStorageSize);
        storageAccessories = new Storage("Accessories", settings.accessoryStorageSize);
        countWorkers = settings.countWorkers;
        factory = new Factory(storageEngines, storageBodies, storageAccessories, storageCars, countWorkers);
        int countSuppliers = 0;
        for(String key: settings.engineSuppliers.keySet()){
            countSuppliers += settings.engineSuppliers.get(key);
        }
        for(String key: settings.engineSuppliers.keySet()){
            countSuppliers += settings.engineSuppliers.get(key);
        }
        for(String key: settings.engineSuppliers.keySet()){
            countSuppliers += settings.engineSuppliers.get(key);
        }
        suppliers = new AbstractSupplier[countSuppliers];
        supplierThreads = new Thread[countSuppliers];
        int index = 0;
        for(String key: settings.engineSuppliers.keySet()){
            for(int i = 0; i < settings.engineSuppliers.get(key); i++){
                suppliers[index] = new SupplierEngine(3, storageEngines, key);
                supplierThreads[index] = new Thread(suppliers[index]);
                index++;
            }
        }
        for(String key: settings.bodySuppliers.keySet()){
            for(int i = 0; i < settings.bodySuppliers.get(key); i++){
                suppliers[index] = new SupplierBody(3, storageBodies, key);
                supplierThreads[index] = new Thread(suppliers[index]);
                index++;
            }
        }
        for(String key: settings.accessorySuppliers.keySet()){
            for(int i = 0; i < settings.accessorySuppliers.get(key); i++){
                suppliers[index] = new SupplierAccessory(3, storageAccessories, key);
                supplierThreads[index] = new Thread(suppliers[index]);
                index++;
            }
        }
        int countDealers = 0;
        for(String key: settings.dealers.keySet()){
            countDealers += settings.dealers.get(key);
        }
        dealers = new Dealer[countDealers];
        dealerThreads = new Thread[countDealers];
        int countTypeCar = settings.dealers.size();
        index = 0;
        for(String key: settings.dealers.keySet()){
            for(int i = 0; i < settings.dealers.get(key); i++){
                dealers[index] = new Dealer(key, 3, storageCars, index);
                dealerThreads[index] = new Thread(dealers[index]);
                index++;
            }
        }
        controller = new Controller(factory, storageCars, dealers);
        controllerThread = new Thread(controller);

        for(Thread thread : supplierThreads){
            thread.start();
        }
        for(Thread thread : dealerThreads){
            thread.start();
        }
        storageCars.setController(controller);
        controllerThread.start();

        graphics = new Graphics(suppliers, dealers, storageCars, storageEngines, storageBodies, storageAccessories, countTypeCar, factory);
        timer = new Timer(100, _ ->{
            if(!graphics.isRunning()){
                exit();
            }
        });
        timer.start();
    }

    public void exit(){
        timer.stop();
        for(Thread thread : supplierThreads){
            thread.interrupt();
        }
        for(Thread thread : dealerThreads){
            thread.interrupt();
        }
        Dealer.Exit();
        controllerThread.interrupt();
        factory.exit();
    }
}
