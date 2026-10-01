package AutoFabric.Suppliers;

import AutoFabric.Product.Engine;
import AutoFabric.Product.Product;
import AutoFabric.Storage.Storage;

public class SupplierEngine extends AbstractSupplier{
    public SupplierEngine(int delay, Storage storage, String nameProduct) {
        super(delay, storage, nameProduct);
    }

    @Override
    public Product generateProduct() {
        return new Engine(nameProduct);
    }
}
