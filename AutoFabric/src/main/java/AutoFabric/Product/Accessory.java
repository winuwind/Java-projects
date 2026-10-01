package AutoFabric.Product;

public class Accessory extends AbstractProduct {
    private static int number = 0;

    public Accessory(String name) {
        id = 10 * number++ + 3;
        this.name = name;
    }
}
