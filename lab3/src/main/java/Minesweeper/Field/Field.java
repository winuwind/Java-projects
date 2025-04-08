package Minesweeper.Field;

public class Field {
    private final Size size;
    private final int countMines;
    private final Cell[][] field;

    private void pullField(){
        for (int x = 0; x < size.getWidth(); x++){
            for (int y = 0; y < size.getHeight(); y++){
                field[y][x] = new Cell(StateMine.ZeroMineNearby);
            }
        }
    }

    public Field() {
        this.size = new Size();
        this.countMines = 10;
        field = new Cell[size.getHeight()][size.getWidth()];
        pullField();
    }

    public Field(int countMines) {
        this.countMines = countMines;
        this.size = new Size((int) Math.sqrt(countMines * 10), (int) Math.sqrt(countMines * 10));
        field = new Cell[size.getHeight()][size.getWidth()];
        pullField();
    }

    public Field(int width, int height) {
        this.size = new Size(width, height);
        this.countMines = (int) ((double) size.getWidth() * (double) size.getHeight() * 0.1);
        field = new Cell[size.getHeight()][size.getWidth()];
        pullField();
    }

    public Field(int width, int height, int countMines) {
        this.size = new Size(width, height);
        this.countMines = Math.min(countMines, size.getHeight() * size.getWidth());
        field = new Cell[size.getHeight()][size.getWidth()];
        pullField();
    }

    public Size getSize() {
        return size;
    }

    public int getCountMines() {
        return countMines;
    }
    
    public Cell getCell(int x, int y) {
        return field[y][x];
    }
}
