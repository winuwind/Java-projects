package AutoFabric.Storage;

import AutoFabric.Dealers.Dealer;
import AutoFabric.Factory.Factory;
import AutoFabric.Meta.IntClass;
import AutoFabric.Meta.MyMap;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Controller implements Runnable {
    private final Factory factory;
    private final Storage storage;
    private final Dealer[] dealers;
    private final Timer timer;

    public Controller(Factory factory, Storage storage, Dealer[] dealers) {
        this.factory = factory;
        this.storage = storage;
        this.dealers = dealers;
        timer = new Timer(5000, _ ->{
            if(storage.getSize() == 0 && factory.getCountTasks() == 0){
                synchronized (this) {
                    notifyAll();
                }
            }
        });
        timer.start();
    }

    private String getNextTask(){
        HashMap<String, Double> doubleHashMap = new HashMap<>();
        doubleHashMap.put("BMW", 0.0);
        doubleHashMap.put("Range Rover", 0.0);
        doubleHashMap.put("Lada", 0.0);
        doubleHashMap.put("", 0.0);
        for(Dealer dealer : dealers){
            String name = dealer.getName();
            if(!name.equals("BMW") && !name.equals("Range Rover") && !name.equals("Lada")){
                name = "";
            }
            doubleHashMap.put(name, 1.0 / (double) dealer.getDelay());
        }
        ArrayList<Factory.Task> tasks = new ArrayList<>(factory.getTasks());
        MyMap info = storage.getInfoAboutStorage();
        for(Factory.Task task : tasks){
            if(task == null){
                continue;
            }
            info.add(task.getNameCar());
        }
        HashMap<String, IntClass> dict = info.getDictionary();
        if(!dict.containsKey("BMW")){
            dict.put("BMW", new IntClass());
        }
        if(!dict.containsKey("Range Rover")){
            dict.put("Range Rover", new IntClass());
        }
        if(!dict.containsKey("Lada")){
            dict.put("Lada", new IntClass());
        }

        int size = 0;
        for(String name : dict.keySet()){
            size += dict.get(name).getValue();
        }
        if(size == 0){
            return "";
        }
        double x = 0;
        for(String name : doubleHashMap.keySet()){
            x += doubleHashMap.get(name);
        }
        for(String name : doubleHashMap.keySet()){
            double y = doubleHashMap.get(name);
            doubleHashMap.put(name, y / x);
        }
        double max = 0;
        String maxKey = "";
        for(String key : dict.keySet()){
            if(doubleHashMap.containsKey(key)) {
                double value = doubleHashMap.get(key) - (double) dict.get(key).getValue() / (double) size;
                if(value > max){
                    max = value;
                    maxKey = key;
                }
            }
        }
        return maxKey;
    }

    @Override
    public synchronized void run() {
        while (!Thread.currentThread().isInterrupted()) {
            if(Dealer.update()){
                factory.deleteAllTasks();
            }
            while (storage.getMaxSize() - storage.getSize() - factory.getCountTasks() > 0) {
                factory.newTask(getNextTask());
            }
            try{
                wait();
            }
            catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        timer.stop();
        synchronized (storage) {
            notifyAll();
        }
    }
}
