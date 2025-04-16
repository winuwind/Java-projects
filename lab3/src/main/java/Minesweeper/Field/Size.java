package Minesweeper.Field;

public class Size {
    private final int width;
    private final int height;

    public Size(){
        width = 9;
        height = 9;
    }

    public Size(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
