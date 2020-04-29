package com.emomtimer.timer;

import android.os.CountDownTimer;

import java.util.ArrayList;
import java.util.List;

public class IntervalTimer {
    private long workTimeSeconds;
    private long restTimeSeconds;
    private int numberOfIntervals;
    private long fullIntervalTimeSeconds;
    private long totalTimerRuntimeSeconds;
    private CountDownTimer intervalTimer;
    private List<IntervalTimerObserver> observerList;
    private TimerInterval lastInterval;

    public IntervalTimer(long workTimeSeconds, long restTimeSeconds, int numberOfIntervals){
        this.workTimeSeconds = workTimeSeconds;
        this.restTimeSeconds = restTimeSeconds;
        this.numberOfIntervals = numberOfIntervals;
        this.fullIntervalTimeSeconds = workTimeSeconds + restTimeSeconds;
        this.totalTimerRuntimeSeconds = fullIntervalTimeSeconds * numberOfIntervals;
        this.lastInterval = null;
        observerList = new ArrayList<>();
    }

    public void register(IntervalTimerObserver observer){
        this.observerList.add(observer);
    }

    private void emit(TimerInterval interval){
        for (IntervalTimerObserver o: observerList) {
            o.notify(interval);
        }
    }

    private void resetTimer(){
        if (intervalTimer != null){
            intervalTimer.cancel();
        }
        intervalTimer = null;
    }

    private long secondsToMilliseconds(long seconds){
        return seconds * 1000;
    }

    private long millisecondsToSeconds(long milliseconds){
        return milliseconds / 1000;
    }

    private class IntervalCountdownTimer extends CountDownTimer {
        private TimerInterval lastInterval;
        /**
         * @param secondsInFuture    The number of seconds in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public IntervalCountdownTimer(long secondsInFuture, long countDownInterval) {
            super(secondsToMilliseconds(secondsInFuture), secondsToMilliseconds(countDownInterval));
        }

        private TimerInterval calculateCurrentInterval(long secondsPassed) {
            long remainingTimeInFullInterval = secondsPassed % fullIntervalTimeSeconds;
            if (remainingTimeInFullInterval <= workTimeSeconds){
                return TimerInterval.WorkTime;
            }else if(remainingTimeInFullInterval > workTimeSeconds){
                return TimerInterval.RestTime;
            }else{
                return TimerInterval.Completed;
            }
        }

        @Override
        public void onTick(long millisUntilFinished) {
            emit(TimerInterval.Tick);
            long secondsPassed = totalTimerRuntimeSeconds - millisecondsToSeconds(millisUntilFinished);
            TimerInterval currentInterval = this.calculateCurrentInterval(secondsPassed);
            if (currentInterval != this.lastInterval) {
                emit(currentInterval);
            }
            this.lastInterval = currentInterval;
        }

        @Override
        public void onFinish() {
            emit(TimerInterval.Completed);
        }
    }

    public void startNewTimer(){
        resetTimer();
        intervalTimer = new IntervalCountdownTimer(totalTimerRuntimeSeconds, fullIntervalTimeSeconds);
        intervalTimer.start();
    }
}
