package Minesweeper.Game;

import Minesweeper.Controller.Commands;
import Minesweeper.Field.*;

import javax.swing.*;
import java.time.LocalDateTime;

public class Game {
    private final Field field;
    private final Field emptyField;
    private int countOpenedCells;
    private int countNotedCells;
    private int countRightNotedCells;
    private int countCommand;
    private boolean gameOver;
    private boolean gamePaused;
    private boolean flagWin;
    private boolean flagSaved;
    private Score score;
    private GameTimer gameTimer;
    private Timer timer;
    private int minTime;

    public Game() {
        field = new Field();
        emptyField = new Field();
        initClass();
    }

    public Game(int countMines){
        field = new Field(countMines);
        emptyField = new Field(countMines);
        initClass();

    }

    public Game(int width, int height) {
        field = new Field(width, height);
        emptyField = new Field(width, height);
        initClass();

    }

    public Game(int width, int height, int countMines) {
        field = new Field(width, height, countMines);
        emptyField = new Field(width, height, countMines);
        initClass();
    }

    private void initClass() {
        gameTimer = new GameTimer();
        timer = new Timer(100, _ -> gameTimer.updateCurrentTime());
        GenerateField.generateField(field);
        countOpenedCells = 0;
        countNotedCells = 0;
        countRightNotedCells = 0;
        countCommand = 0;
        minTime = 10000;
        gameOver = false;
        gamePaused = false;
        flagWin = false;
        flagSaved = false;
        generateScore();
    }

    private void generateScore() {
        score = new Score();
        score.size = field.getSize();
        score.countMines = field.getCountMines();
        score.timeStart = LocalDateTime.now();
    }

    private void noteCell(int x, int y){
        ReleaseCell(x, y);
        if(field.getCell(x, y).getStateOpen() == StateOpen.Closed && countNotedCells < field.getCountMines()){
            countNotedCells++;
            field.getCell(x, y).noteCell();
            field.getCell(x, y).setUpdated(true);
        }
        else if(field.getCell(x, y).getStateOpen() == StateOpen.Noted){
            countNotedCells--;
            field.getCell(x, y).noteCell();
            field.getCell(x, y).setUpdated(true);
        }
    }

    private int countNoted(int x, int y){
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
                if(field.getCell(x + i, y + j).getStateOpen() == StateOpen.Noted) {
                    count++;
                }
            }
        }
        return count;
    }

    private int countMines(int x, int y){
        return switch (field.getCell(x, y).getStateMine()) {
            case ZeroMineNearby -> 0;
            case OneMineNearby -> 1;
            case TwoMineNearby -> 2;
            case ThreeMineNearby -> 3;
            case FourMineNearby -> 4;
            case FiveMineNearby -> 5;
            case SixMineNearby -> 6;
            case SevenMineNearby -> 7;
            case EightMineNearby -> 8;
            default -> 9;
        };
    }

    private void openNeighbor(int x, int y){
        for(int i = -1; i <= 1; i++) {
            if(x + i < 0 || x + i >= field.getSize().getWidth()) {
                continue;
            }
            for(int j = -1; j <= 1; j++) {
                if(y + j < 0 || y + j >= field.getSize().getHeight()) {
                    continue;
                }
                Cell cell = field.getCell(x + i, y + j);
                if(cell.getStateOpen() == StateOpen.Closed) {
                    cell.openCell();
                    field.getCell(x + i, y + j).setUpdated(true);
                    countOpenedCells++;
                    if(cell.getStateMine() == StateMine.Mine){
                        countOpenedCells--;
                        gameEnd(cell);
                        return;
                    }
                    else if(cell.getStateMine() == StateMine.ZeroMineNearby){
                        openNeighborZero(x + i, y + j);
                    }
                }
            }
        }
    }

    private void openNeighborZero(int x, int y){
        for(int i = -1; i <= 1; i++) {
            if(x + i < 0 || x + i >= field.getSize().getWidth()) {
                continue;
            }
            for(int j = -1; j <= 1; j++) {
                if(y + j < 0 || y + j >= field.getSize().getHeight()) {
                    continue;
                }
                Cell cell = field.getCell(x + i, y + j);
                if(cell.getStateOpen() == StateOpen.Closed) {
                    cell.openCell();
                    field.getCell(x + i, y + j).setUpdated(true);
                    countOpenedCells++;
                    if(cell.getStateMine() == StateMine.ZeroMineNearby){
                        openNeighborZero(x + i, y + j);
                    }
                }
            }
        }
    }

    private void openCell(int x, int y){
        if(field.getCell(x, y).getStateOpen() == StateOpen.Noted) {
            return;
        }
        if(countOpenedCells == 0){
            gameTimer.start();
            timer.start();
        }
        ReleaseCell(x, y);
        if(field.getCell(x, y).getStateMine() == StateMine.Mine){
            gameEnd(field.getCell(x, y));
        }
        else if(field.getCell(x, y).getStateOpen() == StateOpen.Opened){
            if(countNoted(x, y) == countMines(x, y)){
                openNeighbor(x, y);
            }
        }
        else{
            field.getCell(x, y).openCell();
            field.getCell(x, y).setUpdated(true);
            countOpenedCells++;
            if(field.getCell(x, y).getStateMine() == StateMine.ZeroMineNearby){
                openNeighborZero(x, y);
            }
        }
        if(countOpenedCells == field.getSize().getWidth() * field.getSize().getHeight() - field.getCountMines()){
            flagWin = true;
            gameEnd(new Cell(StateMine.Mine));
        }
    }

    void pressCell(int x, int y){
        if(field.getCell(x, y).getStateOpen() == StateOpen.Opened){
            for(int i = -1; i <= 1; i++) {
                if(x + i < 0 || x + i >= field.getSize().getWidth()) {
                 continue;
                }
                for (int j = -1; j <= 1; j++) {
                    if(y + j < 0 || y + j >= field.getSize().getHeight()) {
                        continue;
                    }
                    if(field.getCell(x + i, y + j).getStateOpen() == StateOpen.Closed){
                        field.getCell(x + i, y + j).pressCell();
                        field.getCell(x + i, y + j).setUpdated(true);
                    }
                }
            }
        }
        else{
            field.getCell(x, y).pressCell();
            field.getCell(x, y).setUpdated(true);
        }
    }

    void ReleaseCell(int x, int y){
        for(int i = -1; i <= 1; i++) {
            if(x + i < 0 || x + i >= field.getSize().getWidth()) {
                continue;
            }
            for (int j = -1; j <= 1; j++) {
                if(y + j < 0 || y + j >= field.getSize().getHeight()) {
                    continue;
                }
                if(field.getCell(x + i, y + j).getStateOpen() == StateOpen.Pressed){
                    field.getCell(x + i, y + j).ReleaseCell();
                }
                field.getCell(x + i, y + j).setUpdated(true);
            }
        }
    }

    private void pullTrue(){
        for(int y = 0; y < field.getSize().getHeight(); y++){
            for(int x = 0; x < field.getSize().getWidth(); x++){
                if(field.getCell(x, y).getStateOpen() != StateOpen.Closed) {
                    field.getCell(x, y).setUpdated(true);
                    emptyField.getCell(x, y).setUpdated(true);
                }
            }
        }
    }

    public void doCommand(Commands command, int x, int y){
        if (command == Commands.Open && !gamePaused) {
            countCommand++;
            openCell(x, y);
        } else if (command == Commands.Note && !gamePaused) {
            countCommand++;
            noteCell(x, y);
        } else if(command == Commands.Press && !gamePaused) {
            pressCell(x, y);
        } else if (command == Commands.Release && !gamePaused) {
            ReleaseCell(x, y);
        } else if (command == Commands.Pause) {
            gamePaused = true;
            gameTimer.pauseTimer();
            pullTrue();
        } else if (command == Commands.Resume) {
            gamePaused = false;
            gameTimer.resumeTimer();
        } else if (command == Commands.Exit || command == Commands.Finish || command == Commands.NewGame) {
            gamePaused = false;
            gameEnd(new Cell(StateMine.Mine));
        }
    }

    private void countScore() {
        score.score = countOpenedCells * 15 - countNotedCells * 3 - countCommand * 5;
    }

    private void gameEnd(Cell cell){
        gameOver = true;
        gameTimer.stopTimer();
        countScore();
        score.foundedMines = countRightNotedCells;
        score.time = (int) gameTimer.getCurrentTime();
        for(int x = 0; x < field.getSize().getWidth(); x++){
            for(int y = 0; y < field.getSize().getHeight(); y++){
                if(field.getCell(x, y).getStateOpen() == StateOpen.Noted){
                    if(field.getCell(x, y).getStateMine() == StateMine.Mine) {
                        countRightNotedCells++;
                    }
                    else{
                        field.getCell(x, y).setStateMine(StateMine.WrongNoted);
                    }
                }
                if(field.getCell(x, y).getStateOpen() != StateOpen.Opened){
                    field.getCell(x, y).setUpdated(true);
                }
                field.getCell(x, y).openCell();
            }
        }
        cell.setStateMine(StateMine.OpenedMine);
    }

    public boolean isGameWin() {
        return flagWin;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public boolean isGameSaved() {
        return flagSaved;
    }

    public boolean isGamePaused() {
        return gamePaused;
    }

    public void setFlagSaved() {
        flagSaved = true;
    }

    public Field getField() {
        if(gamePaused) {
            return emptyField;
        }
        return field;
    }

    public Score getScore() {
        if(gamePaused || gameOver) {
            return score;
        }
        countScore();
        score.foundedMines = countNotedCells;
        score.time = (int) gameTimer.getCurrentTime();
        return score;
    }

    public void setMinTime(int minTime) {
        this.minTime = minTime;
    }

    public int getMinTime() {
        return minTime;
    }
}
