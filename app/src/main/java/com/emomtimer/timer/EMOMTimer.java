package com.emomtimer.timer;

public class EMOMTimer extends IntervalTimer {
    public EMOMTimer(long workTimeSeconds, int numberOfIntervals) {
        super(workTimeSeconds, 0, numberOfIntervals);
    }
}
