package AutoFabric.Factory;

import AutoFabric.Product.Accessory;
import AutoFabric.Product.Body;
import AutoFabric.Product.Car;
import AutoFabric.Product.Engine;
import AutoFabric.Storage.Storage;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class Factory {
    private final Storage storageEngines;
    private final Storage storageBodies;
    private final Storage storageAccessories;
    private final Storage storageCars;
    private final ExecutorService executor;
    private final ArrayList<Task> tasks;
    private final ArrayList<Future<?>> futures;

    public Factory(Storage storageEngines, Storage storageBodies, Storage storageAccessories, Storage storageCars, int countWorkers) {
        this.storageEngines = storageEngines;
        this.storageBodies = storageBodies;
        this.storageAccessories = storageAccessories;
        this.storageCars = storageCars;
        this.executor = Executors.newFixedThreadPool(countWorkers);
        this.tasks = new ArrayList<Task>();
        this.futures = new ArrayList<Future<?>>();
    }

    public void newTask(String name) {
        Task task = new Task(this, name);
        tasks.add(task);
        futures.add(executor.submit(task));
    }

    public void deleteAllTasks() {
        tasks.clear();
        for (Future<?> future : futures) {
            if(future == null) {
                continue;
            }
            future.cancel(true);
        }
        futures.clear();
    }

    public void deleteTask(Task task) {
        int index = tasks.indexOf(task);
        if(index == -1){
            return;
        }
        tasks.remove(index);

        futures.get(index).cancel(true);
        futures.remove(index);
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public int getCountTasks() {
        return tasks.size();
    }

    public void exit(){
        executor.shutdown();
        try {
            if (!executor.awaitTermination(100, TimeUnit.MILLISECONDS)) {
                executor.shutdownNow(); // принудительно завершаем
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    static public class Task implements Runnable {
        private final Factory factory;
        private final String nameCar;
        private String nameEngine;
        private String nameBody;
        private ArrayList<String> nameAccessories;
        private Engine engine;
        private Body body;
        private ArrayList<Accessory> accessories;

        private void getNamePartsOfCar(){
            switch (nameCar){
                case "BMW":
                    nameEngine = "BMW M5";
                    nameBody = "BMW M5 C";
                    nameAccessories = new ArrayList<>();
                    nameAccessories.add("Hi-Fi");
                    nameAccessories.add("Wheels");
                    break;
                case "Lada":
                    nameEngine = "Renault";
                    nameBody = "Granta";
                    nameAccessories = new ArrayList<>();
                    nameAccessories.add("Wheels");
                    break;
                case "Range Rover":
                    nameEngine = "Ford";
                    nameBody = "Land Rover";
                    nameAccessories = new ArrayList<>();
                    nameAccessories.add("Hi-Fi");
                    nameAccessories.add("Wheels");
                    nameAccessories.add("Panoramic roof");
                    break;
                default:
                    nameEngine = "";
                    nameBody = "";
                    nameAccessories = new ArrayList<>();
                    nameAccessories.add("Wheels");
            }
        }

        public Task(Factory factory, String nameCar) {
            this.factory = factory;
            this.nameCar = nameCar;
            getNamePartsOfCar();
        }

        public String getNameCar() {
            return nameCar;
        }

        private void getEngine(){
            if (nameEngine.isEmpty()){
                engine = (Engine) factory.storageEngines.getProducts();
            }
            else{
                engine = (Engine) factory.storageEngines.getProducts(nameEngine);
            }
        }

        private void getBody(){
            if (nameBody.isEmpty()){
                body = (Body) factory.storageBodies.getProducts();
            }
            else{
                body = (Body) factory.storageBodies.getProducts(nameBody);
            }
        }

        private void getAccessories(){
            accessories = new ArrayList<>();
            if(nameAccessories.getFirst().isEmpty()){
                accessories.add((Accessory) factory.storageAccessories.getProducts());
            }
            else{
                for(String name : nameAccessories){
                    accessories.add((Accessory) factory.storageAccessories.getProducts(name));
                }
            }
        }

        private void getPartsOfCar(){
            getEngine();
            if(Thread.currentThread().isInterrupted()){
                return;
            }
            getBody();
            if(Thread.currentThread().isInterrupted()){
                return;
            }
            getAccessories();
        }

        @Override
        public void run() {
            getPartsOfCar();
            if(Thread.currentThread().isInterrupted()){
                return;
            }
            Car car = new Car(nameCar, engine, body, accessories);
            factory.storageCars.add(car);
            int index = factory.tasks.indexOf(this);
            factory.tasks.remove(index);
            factory.futures.remove(index);
        }
    }
}
