package Minesweeper.UI.TUI;

import Minesweeper.Field.Cell;
import Minesweeper.Field.StateOpen;
import Minesweeper.Game.Game;
import Minesweeper.UI.UserInterface;

import java.io.*;
import java.util.Objects;
import java.util.Scanner;

public class Console extends UserInterface {
    Scanner scanner;
    StringBuilder[] field;
    private final Thread inputThread;

    public Console() {
        super();
        type = "TUI";
        scanner = new Scanner(System.in);
        command = "New Game";
        System.out.print("Enter width: ");
        String widthString;
        while (true){
            widthString = scanner.nextLine();
            if(!widthString.matches("[-+]?\\d+")){
                System.out.print("Enter integer width: ");
            }
            else{
                if(Integer.parseInt(widthString) <= 0){
                    System.out.print("Enter width more then zero: ");
                }
                else{
                    break;
                }
            }
        }
        command += " -w " + widthString;
        System.out.print("Enter height: ");
        String heightString;
        while (true){
            heightString = scanner.nextLine();
            if(!heightString.matches("[-+]?\\d+")){
                System.out.print("Enter integer height: ");
            }
            else{
                if(Integer.parseInt(heightString) <= 0){
                    System.out.print("Enter height more then zero: ");
                }
                else{
                    break;
                }
            }
        }
        command += " -h " + heightString;
        int maxMines = Integer.parseInt(widthString) * Integer.parseInt(heightString);
        System.out.print("Enter count mines: ");
        String countMinesString;
        while (true){
            countMinesString = scanner.nextLine();
            if(!countMinesString.matches("[-+]?\\d+")){
                System.out.print("Enter integer count mines: ");
            }
            else{
                if(Integer.parseInt(countMinesString) <= 0 || Integer.parseInt(countMinesString) > maxMines){
                    System.out.printf("Enter count mines in the interval from 0 to %d: ", maxMines);
                }
                else{
                    break;
                }
            }
        }
        command += " -m " + countMinesString;
        flagCommand = true;

        inputThread = new Thread(() -> {
            while (true) {
                readCommand();
            }
        });
        inputThread.start();

    }

    private void readCommand() {
        command = scanner.nextLine();
        switch (command) {
            case "New Game" -> {
                System.out.print("Enter width: ");
                String widthString;
                while (true){
                    widthString = scanner.nextLine();
                    if(!widthString.matches("[-+]?\\d+")){
                        System.out.print("Enter integer width: ");
                    }
                    else{
                        if(Integer.parseInt(widthString) <= 0){
                            System.out.print("Enter width more then zero: ");
                        }
                        else{
                            break;
                        }
                    }
                }
                command += " -w " + widthString;
                System.out.print("Enter height: ");
                String heightString;
                while (true){
                    heightString = scanner.nextLine();
                    if(!heightString.matches("[-+]?\\d+")){
                        System.out.print("Enter integer height: ");
                    }
                    else{
                        if(Integer.parseInt(heightString) <= 0){
                            System.out.print("Enter height more then zero: ");
                        }
                        else{
                            break;
                        }
                    }
                }
                command += " -h " + heightString;
                int maxMines = Integer.parseInt(widthString) * Integer.parseInt(heightString);
                System.out.print("Enter count mines: ");
                String countMinesString;
                while (true){
                    countMinesString = scanner.nextLine();
                    if(!countMinesString.matches("[-+]?\\d+")){
                        System.out.print("Enter integer count mines: ");
                    }
                    else{
                        if(Integer.parseInt(countMinesString) <= 0 || Integer.parseInt(countMinesString) > maxMines){
                            System.out.printf("Enter count mines in the interval from 0 to %d: ", maxMines);
                        }
                        else{
                            break;
                        }
                    }
                }
                command += " -m " + countMinesString;
            }
            case "Open", "Note" -> {
                System.out.print("Enter number of columns: ");
                command += " " + scanner.nextLine();
                System.out.print("Enter number of rows: ");
                command += " " + scanner.nextLine();
            }
            case "Restart" ->
                    command = "New Game -w " + game.getField().getSize().getWidth() + " -h " + game.getField().getSize().getHeight() + " -m " + game.getField().getCountMines();
        }
        flagCommand = true;
    }

    private char getCell(Cell cell) {
        if(cell == null) {
            return 0;
        }
        char result = 0;
        if(cell.getStateOpen() == StateOpen.Opened){
            result = switch (cell.getStateMine()){
                case Mine -> '*';
                case OpenedMine -> 'X';
                case WrongNoted -> 'E';
                case ZeroMineNearby -> '0';
                case OneMineNearby -> '1';
                case TwoMineNearby -> '2';
                case ThreeMineNearby -> '3';
                case FourMineNearby -> '4';
                case FiveMineNearby -> '5';
                case SixMineNearby -> '6';
                case SevenMineNearby -> '7';
                case EightMineNearby -> '8';
            };
        }
        else if(cell.getStateOpen() == StateOpen.Noted){
            result = 'F';
        }
        else if(cell.getStateOpen() == StateOpen.Closed){
            result = '#';
        }
        return result;
    }

    @Override
    public void updateField() {
        for(int y = 0; y < game.getField().getSize().getHeight(); y++) {
            for (int x = 0; x < game.getField().getSize().getWidth(); x++) {
                Cell cell = game.getField().getCell(x, y);
                field[y % 10 + 2 + y / 10 * 11].setCharAt(2 * (x + 1), getCell(cell));
            }
        }
        for (StringBuilder stringBuilder : field) {
            System.out.println(stringBuilder.toString());
        }
        if (game.isGameOver()) {
            if(game.isGameWin()){
                System.out.println("You win!");
                if(game.getScore().time < game.getMinTime() && !flagNewRecord) {
                    System.out.println("New Record!");
                    flagNewRecord = true;
                }
            }
            else if(!flagNewRecord){
                if(!Objects.equals(command, "Finish")) {
                    System.out.println("You lose!");
                }
            }
        }
    }

    @Override
    public void newGameCommand(Game game) {
        this.game = game;
        flagNewRecord = false;
        field = new StringBuilder[game.getField().getSize().getHeight() / 10 * 11 + 2 + game.getField().getSize().getHeight() % 10];
        field[0] = new StringBuilder(game.getField().getSize().getWidth() * 2 + 1);
        field[0].append(' ');
        for(int j = 1; j < game.getField().getSize().getWidth() * 2 + 1; j++){
            if((j - 1) % 20 == 0){
                field[0].append('|');
            } else if (j % 2 == 0) {
                field[0].append(((j - 1) / 2) % 10);
            } else {
                field[0].append(' ');
            }
        }
        int countSkipped = 1;
        for (int i = 1; i < field.length; i++) {
            field[i] = new StringBuilder(game.getField().getSize().getWidth() * 2 + 1);
            if((i - 1) % 11 == 0){
                for (int j = 0; j < game.getField().getSize().getWidth() * 2 + 1; j++) {
                    field[i].append('-');
                }
                countSkipped++;
                continue;
            }
            field[i].append((i - countSkipped) % 10);
            for (int j = 1; j < game.getField().getSize().getWidth() * 2 + 1; j++) {
                if((j - 1) % 20 == 0){
                    field[i].append('|');
                }
                else {
                    field[i].append(' ');
                }
            }
        }

    }

    @Override
    public void printScoresCommand(File file){
        System.out.printf("%-10s%-10s%-30s%-15s%-10s%-10s%-15s%-15s%-10s%n",
                "Score", "Time", "Date", "Count mines", "Width", "Height", "Game state", "Game result", "Interface");
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] words = line.split(" ");
                System.out.printf("%-10s%-10s%-30s%-15s%-10s%-10s%-15s%-15s%-10s%n",
                        words[0], words[1], words[2] + " " + words[3], words[4], words[5], words[6], words[7], words[8], words[9]);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void aboutCommand() {
        String info = """
                Minesweeper Game - Help
               \s
                Objective:
                The goal of Minesweeper is to uncover all the empty spaces on the grid without triggering any mines.
                If you reveal a cell that contains a mine, the game is over.
               \s
                How to Play:
                - Enter command to console:
                  - "Open <x> <y>" to open a cell.
                    - If the cell is empty, it will reveal the number of neighboring mines.
                    - If the cell contains a mine, the game ends.
                  - "Note <x> <y>" to mark a cell.
                  - "Pause" to pause. Field will be hidden.
                  - "Resume" to resume. Field will be displayed.
                  - "Restart" to start new game with same settings.
                  - "Finish" to finish game. All cells will be opened.
                  - "About" to show information about game.
                  - "New Game" to start new game with your settings.
                  - "Exit" to exit game.
                  - "Save" to save the game to a table of records. If you win this game, it's will be saved automatically.
                  - "High Score" to show table of records.
                - The command "Open" for the opened cell:
                  - If the number of neighboring flagged cells equals the number of neighboring mines, all cells without flags will be opened.
                - Numbers: Each revealed cell may display a number. This number indicates how many mines are in the eight adjacent cells around that particular cell.
                - Uncovering Cells: If you uncover a cell with zero number, all adjacent cells will automatically be uncovered.
               \s
                Symbol as cell:
                - #: closed cell.
                - *: cell with mine.
                - X: cell with mine which you opened.
                - n (n = 0, ..., 8): cell without mine, the neighbors of this cell contain n mines.
                - F: noted cell.
                - E: wrong noted cell.
               \s
                Winning the Game:
                You win when all safe cells are uncovered.
               \s
                Game Features:
                - Timer: The game keeps track of how much time you take to solve the puzzle.
               \s
                Tips:
                - Start by open a cell in the center of the grid.
                - Always flag the suspected mines as you progress.
                - If you are stuck, consider using the numbers to logically deduce which cells are safe to open.
               \s
                Troubleshooting:
                - Game doesn’t start: Make sure your computer meets the game’s system requirements.
                - Game freezes: Try restarting the game.""";
        System.out.println(info);
    }

    @Override
    public void exitCommand() {
        if (inputThread != null && inputThread.isAlive()) {
            inputThread.interrupt();
        }
        System.exit(0);
    }
}
