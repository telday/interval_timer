package com.emomtimer.timer;

public interface IntervalTimerObserver {
    public void notify(TimerInterval interval);
    public void notify(float intervalPercentRemaining);
    void notify(int secondsRemaining);
}
