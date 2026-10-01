package AutoFabric.Suppliers;

import AutoFabric.Storage.Storage;

public abstract class AbstractSupplier implements Supplier, Runnable {
    private final Storage storage;
    protected final String nameProduct;
    protected int delay;

    public AbstractSupplier(int delay, Storage storage, String nameProduct) {
        this.storage = storage;
        this.nameProduct = nameProduct;
        this.delay = delay;

    }

    @Override
    public String getName(){
        return nameProduct;
    }

    @Override
    public int getDelay(){
        return delay;
    }

    @Override
    public void putProduct(){
        storage.add(generateProduct());
    }

    @Override
    public void changeDelay(int delay) {
        this.delay = delay;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            putProduct();
            try {
                Thread.sleep(delay * 1000L);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
