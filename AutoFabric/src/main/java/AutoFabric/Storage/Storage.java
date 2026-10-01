package AutoFabric.Storage;

import AutoFabric.Meta.MyMap;
import AutoFabric.Product.Accessory;
import AutoFabric.Product.Car;
import AutoFabric.Product.Product;

import java.util.ArrayList;

public class Storage {
    private Controller controller = null;
    private final String typeProduction;
    private final int maxSize;
    ArrayList<Product> products;

    public Storage(String typeProduction, int maxSize) {
        this.typeProduction = typeProduction;
        this.maxSize = maxSize;
        this.products = new ArrayList<>();
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void clear(){
        products.clear();
        synchronized (this) {
            notifyAll();
        }
    }

    public String getTypeProduction() {
        return typeProduction;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public int getSize() {
        return products.size();
    }

    public final MyMap getInfoAboutStorage() {
        MyMap info = new MyMap();
        ArrayList<Product> copyProducts = new ArrayList<>(products);
        for (Product product : copyProducts) {
            info.add(product.getName());
        }
        return info;
    }

    private boolean checkProduct(String productName) {
        return productName.equals("BMW") || productName.equals("Range Rover") || productName.equals("Lada") ||
                productName.equals("BMW M5") || productName.equals("Ford") || productName.equals("Renault") ||
                productName.equals("BMW M5 C") || productName.equals("Land Rover") || productName.equals("Granta") ||
                productName.equals("Wheels") || productName.equals("Hi-Fi") || productName.equals("Panoramic roof");
    }

    public synchronized Product getProducts() {
        while (products.isEmpty() && !Thread.currentThread().isInterrupted()) {
            try {
                wait();
            }
            catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        Product product = null;
        if(!products.isEmpty()) {
            product = products.removeFirst();
        }
        if(controller != null) {
            synchronized (controller) {
                controller.notifyAll();
            }
        }
        notifyAll();
        return product;
    }

    public synchronized Product getProducts(String name) {
        if(!checkProduct(name)) {
            if(typeProduction.equals("Car")){
                name = "";
            }
            else {
                return getProducts();
            }
        }
        while (!Thread.currentThread().isInterrupted()) {
            for (Product product : products) {
                if (product.getName().equals(name)) {
                    products.remove(product);
                    if(controller != null) {
                        synchronized (controller) {
                            controller.notifyAll();
                        }
                    }
                    notifyAll();
                    return product;
                }
            }
            try {
                wait();
            }
            catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        return null;
    }

    public synchronized void add(Product product) {
        while(products.size() >= maxSize && !Thread.currentThread().isInterrupted()) {
            try {
                wait();
            }
            catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        if(product instanceof Car car) {
            if(car.getEngine() == null || car.getBody() == null || car.getAccessories() == null) {
                notifyAll();
                return;
            }
            for(Accessory accessory: car.getAccessories()){
                if(accessory == null) {
                    notifyAll();
                    return;
                }
            }
        }
        products.add(product);
        notifyAll();
    }
}
