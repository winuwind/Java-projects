package AutoFabric.Suppliers;

import AutoFabric.Product.Accessory;
import AutoFabric.Storage.Storage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SupplierAccessoryTest {

    @Test
    void testGenerateProduct() {
        Storage storage = new Storage("Accessory", 10);
        SupplierAccessory supplier = new SupplierAccessory(1, storage, "Wheels");

        Accessory accessory = (Accessory) supplier.generateProduct();
        assertEquals("Wheels", accessory.getName());
    }

    @Test
    void testNameAndDelay() {
        Storage storage = new Storage("Accessory", 10);
        SupplierAccessory supplier = new SupplierAccessory(2, storage, "Hi-Fi");

        assertEquals("Hi-Fi", supplier.getName());
        assertEquals(2, supplier.getDelay());

        supplier.changeDelay(5);
        assertEquals(5, supplier.getDelay());
    }
}
