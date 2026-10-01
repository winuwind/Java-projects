package AutoFabric.Dealers;

import AutoFabric.Product.Accessory;
import AutoFabric.Product.Body;
import AutoFabric.Product.Car;
import AutoFabric.Product.Engine;
import AutoFabric.Storage.Storage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class DealerTest {

    private Dealer dealer;

    @BeforeEach
    void setUp() {
        dealer = new Dealer("TestCar", 3, null, 42);
    }

    @Test
    void testGetName() {
        assertEquals("TestCar", dealer.getName(), "Имя автомобиля должно совпадать");
    }

    @Test
    void testGetDelay() {
        assertEquals(3, dealer.getDelay(), "Задержка должна быть равна 3");
    }

    @Test
    void testChangeDelay() {
        dealer.changeDelay(7);
        assertEquals(7, dealer.getDelay(), "Задержка должна обновиться до 7");
    }

    @Test
    void testConstructorSetsFieldsCorrectly() {
        Dealer testDealer = new Dealer("BMW", 5, null, 101);
        assertEquals("BMW", testDealer.getName());
        assertEquals(5, testDealer.getDelay());
    }

    @Test
    void testFlagUpdate() {
        Dealer.reset();
        assertFalse(Dealer.update());
        dealer.changeDelay(7);
        assertTrue(Dealer.update());
        Dealer.reset();
        assertFalse(Dealer.update());
    }

    private static Thread runDealer() {
        Storage storage = new Storage("Car", 100);
        ArrayList<Accessory> accessories = new ArrayList<Accessory>();
        accessories.add(new Accessory("Hi-Fi"));
        Car validCar = new Car("BMW", new Engine(""), new Body("Body"), accessories);
        storage.add(validCar);
        Dealer dealer_ = new Dealer("BMW", 3, storage, 42);
        Thread thread = new Thread(dealer_);
        thread.start();
        return thread;
    }

    @Test
    void testRun(){
        Thread thread = runDealer();
        assertTrue(thread.isAlive());
        thread.interrupt();
    }

    @Test
    void testFlagLogging() {
        Dealer.setLogging(true);
        Thread thread = runDealer();
        try {
            Thread.sleep(100);
        }
        catch (InterruptedException e) {
            thread.interrupt();
            return;
        }
        thread.interrupt();
        Dealer.Exit();
        File file = new File("AutoFabric.log");
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(file));
        }
        catch (Exception e) {
            return;
        }
        try {
            assertTrue(reader.ready());
            reader.close();
        }
        catch (Exception e) {
            return;
        }
    }
}
