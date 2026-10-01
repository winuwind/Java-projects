package AutoFabric.Product;

public abstract class AbstractProduct implements Product {
    protected int id;
    protected String name;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name){
        this.name = name;
    }
}
