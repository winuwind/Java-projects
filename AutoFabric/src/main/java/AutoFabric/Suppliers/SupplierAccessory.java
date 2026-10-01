package AutoFabric.Suppliers;

import AutoFabric.Product.Accessory;
import AutoFabric.Product.Product;
import AutoFabric.Storage.Storage;

public class SupplierAccessory extends AbstractSupplier {
    public SupplierAccessory(int delay, Storage storage, String nameProduct) {
        super(delay, storage, nameProduct);
    }

    @Override
    public Product generateProduct() {
        return new Accessory(nameProduct);
    }
}
