package com.emomtimer.timer;

import android.os.CountDownTimer;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class IntervalTimer {
    private long workTimeSeconds;
    private long restTimeSeconds;
    private int numberOfIntervals;
    public long fullIntervalTimeSeconds;
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
    private void emit(float intervalPercentFinished){
        for (IntervalTimerObserver o: observerList) {
            o.notify(intervalPercentFinished);
        }
    }

    private void emit(int intervalSecondsRemaining){
        for (IntervalTimerObserver o: observerList) {
            o.notify(intervalSecondsRemaining);
        }
    }
    public void resetTimer(){
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

        private long intervalSecondsPassed(long millisUntilFinished){
            long secondsPassed = totalTimerRuntimeSeconds - millisecondsToSeconds(millisUntilFinished);
            return secondsPassed;
        }

        private float calculateIntervalPercentFinished(long millisUntilFinished){
            long secondsPassed = intervalSecondsPassed(millisUntilFinished);
            long intervalSecondsPassed = secondsPassed % fullIntervalTimeSeconds;
            return (float)intervalSecondsPassed / (float)fullIntervalTimeSeconds;
        }

        @Override
        public void onTick(long millisUntilFinished) {
            float intervalPercentFinished = calculateIntervalPercentFinished(millisUntilFinished);
            emit(intervalPercentFinished);
            int secondsRemaining = (int)millisecondsToSeconds(millisUntilFinished);
            emit(secondsRemaining);
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
        intervalTimer = new IntervalCountdownTimer(totalTimerRuntimeSeconds, 1);
        intervalTimer.start();
    }
}
