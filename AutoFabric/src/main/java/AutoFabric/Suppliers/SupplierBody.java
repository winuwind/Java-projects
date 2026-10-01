package AutoFabric.Suppliers;

import AutoFabric.Product.Body;
import AutoFabric.Product.Product;
import AutoFabric.Storage.Storage;

public class SupplierBody extends AbstractSupplier {
    public SupplierBody(int delay, Storage storage, String nameProduct) {
        super(delay, storage, nameProduct);
    }

    @Override
    public Product generateProduct() {
        return new Body(nameProduct);
    }
}
