package AutoFabric.Storage;

import AutoFabric.Meta.IntClass;
import AutoFabric.Meta.MyMap;
import AutoFabric.Product.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class StorageTest {

    private Storage storage;

    @BeforeEach
    void setUp() {
        storage = new Storage("Accessory", 3);
    }

    @Test
    void testGetTypeProduction() {
        assertEquals("Accessory", storage.getTypeProduction());
    }

    @Test
    void testGetMaxSize() {
        assertEquals(3, storage.getMaxSize());
    }

    @Test
    void testAddAndGetSize() {
        Product product = new Accessory("Hi-Fi");
        storage.add(product);
        assertEquals(1, storage.getSize());
    }

    @Test
    void testClear() {
        Product product = new Accessory("Hi-Fi");
        storage.add(product);
        assertEquals(1, storage.getSize());
        storage.clear();
        assertEquals(0, storage.getSize());
    }

    @Test
    void testGetInfoAboutStorage() {
        Product product1 = new Accessory("Hi-Fi");
        Product product2 = new Accessory("Wheels");
        storage.add(product1);
        storage.add(product2);
        MyMap info = storage.getInfoAboutStorage();
        HashMap<String, IntClass> dictionary = info.getDictionary();
        assertEquals(2, dictionary.size());
        assertNotNull(dictionary.getOrDefault("Hi-Fi", null));
        assertNotNull(dictionary.getOrDefault("Wheels", null));
    }

    @Test
    void testGetProductsWithoutName() {
        Product product = new Accessory("Wheels");
        storage.add(product);
        Product retrieved = storage.getProducts();
        assertNotNull(retrieved);
        assertEquals("Wheels", retrieved.getName());
        assertEquals(0, storage.getSize());
    }

    @Test
    void testGetProductsWithName() {
        Product product1 = new Accessory("Wheels");
        Product product2 = new Accessory("Hi-Fi");
        storage.add(product1);
        storage.add(product2);
        Product retrieved = storage.getProducts("Hi-Fi");
        assertNotNull(retrieved);
        assertEquals("Hi-Fi", retrieved.getName());
        assertEquals(1, storage.getSize());
    }

    @Test
    void testGetProductsWithInvalidNameFallback() {
        Product product = new Accessory("Wheels");
        storage.add(product);
        Product retrieved = storage.getProducts("InvalidAccessory");
        assertEquals("Wheels", retrieved.getName());
    }

    @Test
    void testAddCarWithNullDetailsNotAdded() {
        Storage carStorage = new Storage("Car", 2);
        Car invalidCar = new Car("BMW", new Engine(""), null, null);
        carStorage.add(invalidCar);
        assertEquals(0, carStorage.getSize());
    }

    @Test
    void testAddCarWithNullAccessoryNotAdded() {
        Storage carStorage = new Storage("Car", 2);
        Car invalidCar = new Car("BMW", new Engine(""), new Body("Body"), null);
        carStorage.add(invalidCar);
        assertEquals(0, carStorage.getSize());
    }

    @Test
    void testAddCarProperly() {
        Storage carStorage = new Storage("Car", 2);
        ArrayList<Accessory> accessories = new ArrayList<Accessory>();
        accessories.add(new Accessory("Hi-Fi"));
        Car validCar = new Car("BMW", new Engine(""), new Body("Body"), accessories);
        carStorage.add(validCar);
        assertEquals(1, carStorage.getSize());
    }
}
