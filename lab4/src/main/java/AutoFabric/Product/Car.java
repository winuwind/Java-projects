package AutoFabric.Product;

import java.util.ArrayList;

public class Car extends AbstractProduct {
    private static int number = 0;
    private final Engine engine;
    private final Body body;
    private final ArrayList<Accessory> accessories;

    public Car(String name, Engine engine, Body body, ArrayList<Accessory> accessories) {
        id = 10 * number++;
        this.name = name;
        this.engine = engine;
        this.body = body;
        this.accessories = accessories;
    }

    public Engine getEngine() {
        return engine;
    }

    public Body getBody() {
        return body;
    }

    public ArrayList<Accessory> getAccessories() {
        return accessories;
    }
}
