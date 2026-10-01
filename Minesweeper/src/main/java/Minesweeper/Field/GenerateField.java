package Minesweeper.Field;

import java.util.Random;

public class GenerateField {
    private static void generateMines(Field field) {
        Random rand = new Random();
        int countMines = field.getCountMines();
        Size size = field.getSize();
        int maxRandom = size.getWidth() * size.getHeight();
        while (countMines > 0) {
            int coordinate = rand.nextInt(maxRandom);
            int x = coordinate % size.getWidth();
            int y = coordinate / size.getWidth();
            if(field.getCell(x, y).getStateMine() == StateMine.Mine){
                continue;
            }
            field.getCell(x, y).setStateMine(StateMine.Mine);
            countMines--;
        }
    }

    private static int countMines(Field field, int x, int y) {
        int count = 0;
        for(int i = -1; i <= 1; i++) {
            if(x + i < 0 || x + i >= field.getSize().getWidth()) {
                continue;
            }
            for(int j = -1; j <= 1; j++) {
                if((i == 0 && j == 0) ||
                        y + j < 0 || y + j >= field.getSize().getHeight()) {
                    continue;
                }
                if(field.getCell(x + i, y + j).getStateMine() == StateMine.Mine) {
                    count++;
                }
            }
        }
        return count;
    }

    private static void setCell(Cell cell, int countMines) {
        switch(countMines){
            case 0: cell.setStateMine(StateMine.ZeroMineNearby); break;
            case 1: cell.setStateMine(StateMine.OneMineNearby); break;
            case 2: cell.setStateMine(StateMine.TwoMineNearby); break;
            case 3: cell.setStateMine(StateMine.ThreeMineNearby); break;
            case 4: cell.setStateMine(StateMine.FourMineNearby); break;
            case 5: cell.setStateMine(StateMine.FiveMineNearby); break;
            case 6: cell.setStateMine(StateMine.SixMineNearby); break;
            case 7: cell.setStateMine(StateMine.SevenMineNearby); break;
            default: cell.setStateMine(StateMine.EightMineNearby); break;
        }
    }

    private static void pullFreeCells(Field field) {
        for(int x = 0; x < field.getSize().getWidth(); x++) {
            for(int y = 0; y < field.getSize().getHeight(); y++) {
                if(field.getCell(x, y).getStateMine() == StateMine.Mine){
                    continue;
                }
                setCell(field.getCell(x, y), countMines(field, x, y));
            }
        }
    }

    public static void generateField(Field field) {
        generateMines(field);
        pullFreeCells(field);
    }
}
