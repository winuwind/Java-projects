package Minesweeper.Controller;

import Minesweeper.UI.GUI.Window;
import Minesweeper.Game.Game;
import Minesweeper.Game.Score;
import Minesweeper.UI.TUI.Console;
import Minesweeper.UI.UserInterface;

import javax.swing.*;
import java.io.*;
import java.time.format.DateTimeFormatter;

public class Main {
    private Game game;
    private final UserInterface window;
    private final String filepath;
    private int minTime;

    public static void openOrCreateFile(String filepath) throws IOException {
        File file = new File(filepath);
        BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
        writer.close();
    }

    public static void main(String[] args) {
        Game game = new Game();
        UserInterface window = null;
        String filepath = "Records.msr";
        if (args.length > 0) {
            for(int i = 0; i < args.length; i++) {
                if(args[i].equals("-r") && args.length > i + 1) {
                    filepath = args[++i];
                }
                else if(args[i].equals("-t")) {
                    window = new Console();
                }
                else{
                    String info = """
                            You want to start a Minesweeper?
                            Please, don't use this key for to start.
                            You can use:
                                "-r <filepath>" - Select file with high score table. If this file have incorrect data, program will terminate with an error.
                                "-t" - Start program with Text-based User Interface.
                            If you start program without flag "-t" - Start program with Graphical User Interface.
                            For more information about game you may enter a command "About" when you start a Minesweeper.
                            """;
                    System.out.println(info);
                    return;
                }
            }
        }
        if(window == null) {
            window = new Window();
        }
        try{
            openOrCreateFile(filepath);
        }
        catch(IOException e){
            System.out.println("Could not open or create file " + filepath);
            System.exit(1);
        }


        Main controller = new Main(game, window, filepath);
        UserInterface finalWindow = window;
        Timer timer = new Timer(100, _ -> {
            if(controller.window.isNewCommand()){
                controller.doCommand(finalWindow.getCommand());
                finalWindow.updateField();
                finalWindow.doneCommand();
            }
        });
        timer.start();
    }

    public Main(Game game, UserInterface window, String filepath) {
        this.game = game;
        this.window = window;
        this.filepath = filepath;
        minTime = 10000;
    }

    private Commands getCommand(String command) {
        return switch (command) {
            case "Open" -> Commands.Open;
            case "Note" -> Commands.Note;
            case "Pause" -> Commands.Pause;
            case "Resume" -> Commands.Resume;
            case "About" -> Commands.About;
            case "Exit" -> Commands.Exit;
            case "New" -> Commands.NewGame;
            case "High" -> Commands.HighScores;
            case "Finish" -> Commands.Finish;
            case "Save" -> Commands.SaveScore;
            case "Press" -> Commands.Press;
            case "Release" -> Commands.Release;
            default -> null;
        };
    }

    private void getAbout(){
        window.aboutCommand();
    }

    private void saveScore() {
        if(game.isGameSaved())
        {
            return;
        }
        File file;
        BufferedWriter writer;
        try {
            file = new File(filepath);
            writer = new BufferedWriter(new FileWriter(file, true));
        }
        catch (IOException e) {
            e.printStackTrace();
            return;
        }
        Score score = game.getScore();
        String resultGame = "---";
        String gameState;
        if(game.isGameOver()){
            gameState = "Completed";
            if(game.isGameWin()){
                resultGame = "Win";
            }
            else{
                resultGame = "Loss";
            }
            game.setFlagSaved();
        }
        else{
            gameState = "NotCompleted";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String formattedDate = score.timeStart.format(formatter);
        try{
            writer.write(score.score + " " + score.time + " " +
                    formattedDate + " " + score.countMines + " " +
                    score.size.getWidth() + " " + score.size.getHeight() + " " +
                    gameState + " " + resultGame + " " + window.getType() + "\n");
            writer.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void exitGame(){
        window.exitCommand();
        System.exit(0);
    }

    private void newGame(String command) {
        String[] args = command.split(" ");
        int countMines = -1;
        int width = -1, height = -1;
        try {
            for (int i = 1; i < args.length; i++) {
                switch (args[i]) {
                    case "-w" -> width = Integer.parseInt(args[++i]);
                    case "-h" -> height = Integer.parseInt(args[++i]);
                    case "-m" -> countMines = Integer.parseInt(args[++i]);
                }
            }
        }
        catch (NumberFormatException e) {
            width = height = countMines = -1;
        }
        if(countMines == -1 && (width == -1 || height == -1)){
            game = new Game();
        }
        else if (countMines == -1){
            game = new Game(width, height);
        }
        else if (width == -1 || height == -1){
            game = new Game(countMines);
        }
        else{
            game = new Game(width, height, countMines);
        }
        window.newGameCommand(game);

        minTime = 10000;
        File file;
        BufferedReader reader;
        try {
            file = new File(filepath);
            reader = new BufferedReader(new FileReader(file));
        }
        catch (IOException e) {
            e.printStackTrace();
            return;
        }
        try {
            while (reader.ready()) {
                String str = reader.readLine();
                String[] line = str.split(" ");
                if(Integer.parseInt(line[1]) < minTime
                        && Integer.parseInt(line[4]) == game.getField().getCountMines()
                        && Integer.parseInt(line[5]) == game.getField().getSize().getWidth()
                        && Integer.parseInt(line[6]) == game.getField().getSize().getHeight()
                        && line[7].equals("Completed") && line[8].equals("Win") && line[9].equals(window.getType())){
                    minTime = Integer.parseInt(line[1]);
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        try {
            reader.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        game.setMinTime(minTime);
    }

    private void highScores(){
        File file = new File(filepath);
        window.printScoresCommand(file);
    }

    public void doCommand(String command) {
        Commands cmd = getCommand(command.split(" ")[0]);
        if (cmd == null) {
            System.out.println("Unknown command: " + command);
            return;
        }
        int x = -1, y = -1;
        if(cmd == Commands.Open || cmd == Commands.Note || cmd == Commands.Press || cmd == Commands.Release) {
            String[] args = command.split(" ");
            if(args.length != 3) {
                System.out.println("Invalid command: " + command);
                return;
            }
            try {
                x = Integer.parseInt(args[1]);
                y = Integer.parseInt(args[2]);
            }
            catch (NumberFormatException e) {
                System.out.println("Invalid command: " + command);
                return;
            }
        }
        if(!game.isGameOver()) {
            game.doCommand(cmd, x, y);
            if (game.isGameWin()) {
                saveScore();
            }
        }
        switch(cmd){
            case About -> getAbout();
            case NewGame -> newGame(command);
            case HighScores -> highScores();
            case Exit -> exitGame();
            case SaveScore -> saveScore();
        }
    }
}
