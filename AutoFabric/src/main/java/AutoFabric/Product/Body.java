package AutoFabric.Product;

public class Body extends AbstractProduct {
    private static int number = 0;

    public Body(String name) {
        id = 10 * number++ + 2;
        this.name = name;
    }
}
