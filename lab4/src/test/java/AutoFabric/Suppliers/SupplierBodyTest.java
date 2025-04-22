package AutoFabric.Suppliers;

import AutoFabric.Product.Body;
import AutoFabric.Storage.Storage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SupplierBodyTest {

    @Test
    void testGenerateProduct() {
        Storage storage = new Storage("Body", 10);
        SupplierBody supplier = new SupplierBody(1, storage, "Granta");

        Body body = (Body) supplier.generateProduct();
        assertEquals("Granta", body.getName());
    }

    @Test
    void testNameAndDelay() {
        Storage storage = new Storage("Body", 10);
        SupplierBody supplier = new SupplierBody(3, storage, "Land Rover");

        assertEquals("Land Rover", supplier.getName());
        assertEquals(3, supplier.getDelay());

        supplier.changeDelay(6);
        assertEquals(6, supplier.getDelay());
    }
}
