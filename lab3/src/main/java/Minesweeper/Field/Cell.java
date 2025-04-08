package Minesweeper.Field;

public class Cell {
    private StateOpen stateOpen;
    private StateMine stateMine;
    private boolean flagUpdated;

    public Cell(StateMine stateMine) {
        this.stateOpen = StateOpen.Closed;
        this.stateMine = stateMine;
        flagUpdated = false;
    }

    public StateOpen getStateOpen() {
        return stateOpen;
    }

    public StateMine getStateMine() {
        return stateMine;
    }

    public void setUpdated(boolean flagUpdated) {
        this.flagUpdated = flagUpdated;
    }

    public boolean isUpdated() {
        return flagUpdated;
    }

    public void openCell() {
        stateOpen = StateOpen.Opened;
    }

    public void noteCell() {
        if(stateOpen == StateOpen.Noted){
            stateOpen = StateOpen.Closed;
        }
        else if(stateOpen == StateOpen.Closed) {
            stateOpen = StateOpen.Noted;
        }
    }

    public void pressCell(){
        if(stateOpen == StateOpen.Closed) {
            stateOpen = StateOpen.Pressed;
        }
    }

    public void ReleaseCell(){
        if(stateOpen == StateOpen.Pressed) {
            stateOpen = StateOpen.Closed;
        }
    }

    public void setStateMine(StateMine stateMine) {
        this.stateMine = stateMine;
    }
}
