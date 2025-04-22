package AutoFabric.Product;

public class Engine extends AbstractProduct {
    private static int number = 0;

    public Engine(String name) {
        id = 10 * number++ + 1;
        this.name = name;
    }
}
