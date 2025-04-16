package Minesweeper.UI;

import Minesweeper.Game.Game;

import javax.swing.*;

public abstract class UserInterface implements IUserInterface {
    protected Game game;
    protected String command;
    protected boolean flagCommand;
    protected Timer timer;
    protected boolean flagNewRecord;
    protected String type;

    protected UserInterface() {
        game = new Game();
        flagCommand = false;
        command = "";
        flagNewRecord = false;
    }

    @Override
    public final Game getGame(){
        return game;
    }

    @Override
    public void setFlagCommand(boolean flag) {
        flagCommand = flag;
    }

    @Override
    public void setCommand(String command) {
        this.command = command;
    }

    @Override
    public String getCommand(){
        return command;
    }

    @Override
    public boolean isNewCommand() {
        return flagCommand;
    }

    @Override
    public void doneCommand() {
        command = null;
        flagCommand = false;
    }

    @Override
    public String getType(){
        return type;
    }
}
