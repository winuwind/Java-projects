package Minesweeper.UI;

import Minesweeper.Game.Game;

import java.io.File;

public interface IUserInterface {
    Game getGame();
    void updateField();
    void newGameCommand(Game game);
    void setFlagCommand(boolean flag);
    void setCommand(String command);
    String getCommand();
    boolean isNewCommand();
    void doneCommand();
    void printScoresCommand(File file);
    void aboutCommand();
    void exitCommand();
    String getType();
}
