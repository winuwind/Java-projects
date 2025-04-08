package Minesweeper.Game;

public class GameTimer {
    private boolean isTimed;
    private boolean isPaused;
    private long currentTime;
    private long pauseTime;
    private long startTime;
    private long startPauseTime;

    GameTimer() {
        isTimed = false;
        isPaused = false;
        currentTime = 0;
        pauseTime = 0;
        startTime = 0;
        startPauseTime = 0;
    }

    public void updateCurrentTime() {
        if(isTimed && !isPaused) {
            currentTime = System.currentTimeMillis() - startTime - pauseTime;
        }
    }

    public void start() {
        startTime = System.currentTimeMillis();
        currentTime = 0;
        pauseTime = 0;
        startPauseTime = 0;
        isTimed = true;
    }

    public synchronized void pauseTimer() {
        isPaused = true;
        startPauseTime = System.currentTimeMillis();
    }

    public synchronized void resumeTimer() {
        if (isPaused) {
            pauseTime += (System.currentTimeMillis() - startPauseTime);
            isPaused = false;
        }
    }

    public synchronized void stopTimer() {
        isTimed = false;
    }

    public synchronized long getCurrentTime() {
        return currentTime / 1000;
    }
}
