package AutoFabric.Suppliers;

import AutoFabric.Product.Engine;
import AutoFabric.Storage.Storage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SupplierEngineTest {

    @Test
    void testGenerateProduct() {
        Storage storage = new Storage("Engine", 10);
        SupplierEngine supplier = new SupplierEngine(1, storage, "Renault");

        Engine engine = (Engine) supplier.generateProduct();
        assertEquals("Renault", engine.getName());
    }

    @Test
    void testNameAndDelay() {
        Storage storage = new Storage("Engine", 10);
        SupplierEngine supplier = new SupplierEngine(4, storage, "Ford");

        assertEquals("Ford", supplier.getName());
        assertEquals(4, supplier.getDelay());

        supplier.changeDelay(8);
        assertEquals(8, supplier.getDelay());
    }
}
