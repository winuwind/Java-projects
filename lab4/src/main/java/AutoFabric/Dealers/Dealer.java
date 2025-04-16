package AutoFabric.Dealers;

import AutoFabric.Product.Accessory;
import AutoFabric.Product.Car;
import AutoFabric.Storage.Storage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Dealer implements Runnable {
    private final String nameCar;
    private int delay;
    private final int id;
    private final Storage storage;
    private static final File file = new File("AutoFabric.log");
    private static BufferedWriter writer;
    private static boolean logging = false;
    private static boolean isUpdate = false;

    public static void reset(){
        isUpdate = false;
    }

    public static boolean update(){
        return isUpdate;
    }

    public static void setLogging(boolean flag) {
        logging = flag;
        if (logging) {
            try {
                writer = new BufferedWriter(new FileWriter(file));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void Exit() {
        if (logging) {
            try {
                writer.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public Dealer(String nameCar, int delay, Storage storage, int id) {
        this.nameCar = nameCar;
        this.delay = delay;
        this.storage = storage;
        this.id = id;
    }

    public String getName() {
        return nameCar;
    }

    public int getDelay() {
        return delay;
    }

    public void changeDelay(int delay) {
        this.delay = delay;
        isUpdate = true;
    }

    private void execute() {
        Car car = (Car) storage.getProducts(nameCar);
        if(car == null) {
            return;
        }
        car.setName(nameCar);
        if(logging) {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            String formattedDate = now.format(formatter);
            StringBuilder string = new StringBuilder("Time: " + formattedDate + ";\nDealer: " + id + ";\nCar:\n\tName Car: " + nameCar + "\n\tId: " + car.getId()
                    + "\n\t\tBody:\n\t\t\tName: " + car.getBody().getName() + "\n\t\t\tId: " + car.getBody().getId()
                    + "\n\t\tEngine:\n\t\t\tName: " + car.getEngine().getName() + "\n\t\t\tId: " + car.getEngine().getId()
                    + "\n\t\tAccessories:");
            for (Accessory accessory : car.getAccessories()) {
                string.append("\n\t\t\t- Name: ").append(accessory.getName()).append("\n\t\t\t  Id: ").append(accessory.getId());
            }
            string.append("\n");
            try {
                writer.write(string.toString());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            execute();
            try{
                Thread.sleep(delay * 1000L);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
