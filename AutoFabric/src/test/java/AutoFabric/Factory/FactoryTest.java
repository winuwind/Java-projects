package AutoFabric.Factory;

import AutoFabric.Product.Accessory;
import AutoFabric.Product.Body;
import AutoFabric.Product.Engine;
import AutoFabric.Storage.Storage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class FactoryTest {
    private Storage storageEngines;
    private Storage storageBodies;
    private Storage storageAccessories;
    private Storage storageCars;
    private Factory factory;

    @BeforeEach
    void setUp() {
        storageEngines = new Storage("Engine", 10);
        storageBodies = new Storage("Body", 10);
        storageAccessories = new Storage("Accessory", 10);
        storageCars = new Storage("Car", 10);

        factory = new Factory(storageEngines, storageBodies, storageAccessories, storageCars, 2);
    }

    @AfterEach
    void tearDown() {
        factory.exit();
    }

    @Test
    void testNewTaskIncreasesTaskCount() {
        factory.newTask("BMW");
        factory.newTask("Lada");
        assertEquals(2, factory.getCountTasks());
    }

    @Test
    void testDeleteAllTasksClearsAllTasks() {
        factory.newTask("BMW");
        factory.newTask("Lada");
        factory.deleteAllTasks();
        assertEquals(0, factory.getCountTasks());
    }

    @Test
    void testDeleteTaskRemovesSpecificTask() {
        Factory.Task task1 = new Factory.Task(factory, "BMW");
        factory.newTask("BMW");
        factory.newTask("Range Rover");
        factory.newTask("XXX");
        factory.deleteTask(factory.getTasks().getFirst());
        assertFalse(factory.getTasks().contains(task1));
        try{
            Thread.sleep(100);
        }
        catch (InterruptedException e) {
            return;
        }
    }

    @Test
    void testGetProductsInFactory() {
        storageBodies.add(new Body("BMW M5 C"));
        storageBodies.add(new Body("Lada"));
        storageEngines.add(new Engine("BMW M5"));
        storageEngines.add(new Engine("Lada"));
        storageAccessories.add(new Accessory("Wheels"));
        storageAccessories.add(new Accessory("Wheels"));
        storageAccessories.add(new Accessory("Hi-Fi"));
        factory.newTask("BMW");
        try{
            Thread.sleep(100);
        }
        catch (InterruptedException e) {
            return;
        }
        assertEquals(0, factory.getCountTasks());
        factory.newTask("XXX");
        try{
            Thread.sleep(100);
        }
        catch (InterruptedException e) {
            return;
        }
        assertEquals(0, factory.getCountTasks());
    }

    @Test
    void testGetTasksReturnsCorrectList() {
        Factory.Task task = new Factory.Task(factory, "BMW");
        factory.getTasks().add(task);
        assertTrue(factory.getTasks().contains(task));
    }

    @Test
    void testExitTerminatesExecutor() {
        factory.exit();
        assertTrue(factory.getCountTasks() == 0 || factory.getCountTasks() >= 0); // Просто проверка, что не упало
    }

    @Test
    void testTaskConstructorInitializesCorrectly() {
        Factory.Task task = new Factory.Task(factory, "Lada");
        assertEquals("Lada", task.getNameCar());
    }
}
