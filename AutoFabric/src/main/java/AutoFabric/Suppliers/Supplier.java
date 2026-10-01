package AutoFabric.Suppliers;

import AutoFabric.Product.Product;

public interface Supplier {
    void putProduct();
    Product generateProduct();
    void changeDelay(int delay);
    String getName();
    int getDelay();
}
