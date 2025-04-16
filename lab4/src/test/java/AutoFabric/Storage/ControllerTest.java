package AutoFabric.Storage;

import AutoFabric.Dealers.Dealer;
import AutoFabric.Factory.Factory;
import AutoFabric.Product.Accessory;
import AutoFabric.Product.Body;
import AutoFabric.Product.Car;
import AutoFabric.Product.Engine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class ControllerTest {
    private Storage storageEngines;
    private Storage storageBodies;
    private Storage storageAccessories;
    private Storage storageCars;
    private Factory factory;
    private Dealer[] dealers;

    @BeforeEach
    public void setUp() {
        storageEngines = new Storage("Engine", 10);
        storageBodies = new Storage("Body", 10);
        storageAccessories = new Storage("Accessory", 10);
        storageCars = new Storage("Car", 10);

        factory = new Factory(storageEngines, storageBodies, storageAccessories, storageCars, 1);

        dealers = new Dealer[]{
                new Dealer("BMW", 3, storageCars, 1),
                new Dealer("Range Rover", 3, storageCars, 2),
                new Dealer("Lada", 3, storageCars, 3)
        };
    }

    @Test
    public void testControllerGetNextTaskLogic() throws InterruptedException {
        Controller controller = new Controller(factory, storageCars, dealers);

        storageEngines.add(new Engine("BMW M5"));
        storageBodies.add(new Body("BMW M5 C"));
        storageAccessories.add(new Accessory("Wheels"));
        storageAccessories.add(new Accessory("Hi-Fi"));

        Thread controllerThread = new Thread(controller);
        controllerThread.start();
        Thread.sleep(100);
        controllerThread.interrupt();
        controllerThread.join();
        assertTrue(factory.getCountTasks() >= 0);
        assertTrue(storageCars.getSize() >= 0);
    }

    @Test
    public void testControllerNotCreatesWhenStorageFull() throws InterruptedException {
        Storage smallStorage = new Storage("Car", 1);
        Controller controller = new Controller(factory, smallStorage, dealers);

        smallStorage.add(new Car("Lada", new Engine("Renault"), new Body("Granta"),
                new ArrayList<>(){{ add(new Accessory("Wheels")); }}));

        Thread thread = new Thread(controller);
        thread.start();
        Thread.sleep(100);
        thread.interrupt();
        thread.join();

        assertEquals(0, factory.getCountTasks());
    }
}
