package com.emomtimer;

import android.app.Application;

import com.emomtimer.timer.IntervalTimer;

public class IntervalTimerApplication extends Application {
    public static IntervalTimer timer = new IntervalTimer(60, 0, 10);
}
