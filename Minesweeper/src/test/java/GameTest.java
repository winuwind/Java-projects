import Minesweeper.Game.Game;
import Minesweeper.Controller.Commands;
import Minesweeper.Field.*;
import com.sun.source.tree.AssertTree;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    private Game game;

    @BeforeEach
    void setUp() {
        game = new Game(5, 5, 5);
    }

    @Test
    void testGameInitialization() {
        assertNotNull(game.getField(), "Field should not be null");
        assertEquals(5, game.getField().getSize().getWidth(), "Width of the field should be 5");
        assertEquals(5, game.getField().getSize().getHeight(), "Height of the field should be 5");
        assertFalse(game.isGameOver(), "Game should not be over initially");
        assertFalse(game.isGamePaused(), "Game should not be paused initially");
        assertEquals(0, game.getScore().score, "Initial score should be 0");
    }

    @Test
    void testOpenCell() {
        game.doCommand(Commands.Open, 0, 0);
        assertEquals(StateOpen.Opened, game.getField().getCell(0, 0).getStateOpen(), "Cell (0, 0) should be opened");
    }

    @Test
    void testOpenCellWhichIsOpenedSuccess(){
        int x = 0, y = 0;
        for(int i = 0; i < game.getField().getSize().getWidth(); i++){
            for(int j = 0; j < game.getField().getSize().getHeight(); j++){
                Cell cell = game.getField().getCell(i, j);
                if(cell.getStateMine() != StateMine.Mine && cell.getStateMine() != StateMine.ZeroMineNearby){
                    x = i;
                    y = j;
                    break;
                }
            }
        }
        game.doCommand(Commands.Open, x, y);
        for(int i = -1; i < 2; i++){
            if(x + i < 0 || x + i >= game.getField().getSize().getWidth()){
                continue;
            }
            for(int j = -1; j < 2; j++){
                if(y + j < 0 || y + j >= game.getField().getSize().getHeight()){
                    continue;
                }
                if(game.getField().getCell(x + i, y + j).getStateMine() == StateMine.Mine){
                    game.doCommand(Commands.Note, x + i, y + j);
                }
            }
        }
        game.doCommand(Commands.Open, x, y);
        for(int i = -1; i < 2; i++){
            if(x + i < 0 || x + i >= game.getField().getSize().getWidth()){
                continue;
            }
            for(int j = -1; j < 2; j++){
                if(y + j < 0 || y + j >= game.getField().getSize().getHeight()){
                    continue;
                }
                if(game.getField().getCell(x + i, y + j).getStateMine() != StateMine.Mine){
                    assertEquals(StateOpen.Opened, game.getField().getCell(x + i, y + j).getStateOpen(), "Cell (x + i, y + j) should be opened");
                }
            }
        }
    }

    @Test
    void testOpenCellWhichIsOpenedFailure(){
        int x = 0, y = 0;
        for(int i = 0; i < game.getField().getSize().getWidth(); i++){
            for(int j = 0; j < game.getField().getSize().getHeight(); j++){
                if(game.getField().getCell(i, j).getStateMine() != StateMine.Mine && game.getField().getCell(i, j).getStateMine() != StateMine.ZeroMineNearby){
                    x = i;
                    y = j;
                    break;
                }
            }
        }
        game.doCommand(Commands.Open, x, y);
        boolean flagSkipMine = false;
        boolean flagNoteCellWithoutMine = false;
        for(int i = -1; i < 2; i++){
            if(x + i < 0 || x + i >= game.getField().getSize().getWidth()){
                continue;
            }
            for(int j = -1; j < 2; j++){
                if(y + j < 0 || y + j >= game.getField().getSize().getHeight()){
                    continue;
                }
                if(game.getField().getCell(x + i, y + j).getStateMine() == StateMine.Mine){
                    if(!flagSkipMine){
                        flagSkipMine = true;
                        continue;
                    }
                    game.doCommand(Commands.Note, x + i, y + j);
                }
                else if(game.getField().getCell(x + i, y + j).getStateOpen() == StateOpen.Closed && !flagNoteCellWithoutMine){
                    game.doCommand(Commands.Note, x + i, y + j);
                    flagNoteCellWithoutMine = true;
                }
            }
        }
        game.doCommand(Commands.Open, x, y);
        assertTrue(game.isGameOver(), "Game should not be over");
    }

    @Test
    void testOpenCellWithZeroMineInNeighborCells(){
        int x = 0, y = 0;
        for(int i = 0; i < game.getField().getSize().getWidth(); i++){
            for(int j = 0; j < game.getField().getSize().getHeight(); j++){
                if(game.getField().getCell(i, j).getStateMine() != StateMine.Mine){
                    x = i;
                    y = j;
                    break;
                }
            }
        }
    }

    @Test
    void testNoteCell() {
        game.doCommand(Commands.Note, 1, 1);
        assertEquals(StateOpen.Noted, game.getField().getCell(1, 1).getStateOpen(), "Cell (1, 1) should be noted");
    }

    @Test
    void testNoteCellWhichIsNoted() {
        game.doCommand(Commands.Note, 1, 1);
        game.doCommand(Commands.Note, 1, 1);
        assertEquals(StateOpen.Closed, game.getField().getCell(1, 1).getStateOpen(), "Cell (1, 1) should not be noted");
    }

    @Test
    void testPauseAndResume() {
        game.doCommand(Commands.Pause, 0, 0);
        assertTrue(game.isGamePaused(), "Game should be paused");

        game.doCommand(Commands.Resume, 0, 0);
        assertFalse(game.isGamePaused(), "Game should be resumed");
    }

    @Test
    void testGameOver() {
        for(int i = 0; i < game.getField().getSize().getWidth(); i++){
            for(int j = 0; j < game.getField().getSize().getHeight(); j++){
                if(game.getField().getCell(i, j).getStateMine() == StateMine.Mine){
                    game.doCommand(Commands.Open, i, j);
                    break;
                }
            }
        }
        assertTrue(game.isGameOver(), "Game should be over after hitting a mine");
    }

    @Test
    void testWinCondition() {
        for(int i = 0; i < game.getField().getSize().getWidth(); i++){
            for(int j = 0; j < game.getField().getSize().getHeight(); j++){
                if(game.getField().getCell(i, j).getStateMine() != StateMine.Mine){
                    game.doCommand(Commands.Open, i, j);
                }
            }
        }
        assertTrue(game.isGameWin(), "Game should be won if all non-mine cells are opened");
    }

    @Test
    void testScoreCalculation() {
        for(int i = 0; i < game.getField().getSize().getWidth(); i++){
            for(int j = 0; j < game.getField().getSize().getHeight(); j++){
                if(game.getField().getCell(i, j).getStateMine() != StateMine.Mine){
                    game.doCommand(Commands.Open, i, j);
                    break;
                }
            }
        }
        game.doCommand(Commands.Note, 1, 1);
        game.doCommand(Commands.Press, 2, 2);
        assertNotEquals(0, game.getScore().score, "Score should not be zero after some moves");
    }
}
