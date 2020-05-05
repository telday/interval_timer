package com.emomtimer.ui;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.emomtimer.IntervalTimerApplication;
import com.emomtimer.MainActivity;
import com.emomtimer.R;
import com.emomtimer.timer.IntervalTimer;
import com.emomtimer.timer.IntervalTimerObserver;
import com.emomtimer.timer.TimerInterval;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link IntervalTimerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IntervalTimerFragment extends Fragment implements IntervalTimerObserver {
    private TextView loopTimer;
    private TextView fullTimer;
    private ProgressBar intervalProgress;
    private IntervalTimer timer;

    public IntervalTimerFragment() { }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment IntervalTimer.
     */
    public static IntervalTimerFragment newInstance(MediaPlayer mediaPlayer) {
        IntervalTimerFragment fragment = new IntervalTimerFragment();
        //Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void registerMe(){
        if (this.timer != null){
            this.timer.register(this);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_interval_timer, container, false);
        Button startButton = view.findViewById(R.id.startButton);
        Button stopButton = view.findViewById(R.id.stopButton);

        loopTimer = view.findViewById(R.id.loopTimer);
        fullTimer = view.findViewById(R.id.fullTimer);
        intervalProgress = view.findViewById(R.id.intervalProgress);
        intervalProgress.setProgress(intervalProgress.getMax(), true);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer = IntervalTimerApplication.timer;
                registerMe();
                MainActivity.playSound();
                timer.startNewTimer();
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                timer.resetTimer();
                resetTimerText();
            }
        });

        return view;
    }

    private void resetTimerText(){
        fullTimer.setText(getResources().getString(R.string.total_time));
        loopTimer.setText(getResources().getString(R.string.interval_time));
    }

    private String getIntervalTimerTextFromSeconds(int seconds){
        int minutes = (seconds - (seconds % 60)) / 60;
        int hours = (minutes - (minutes % 60)) / 60;
        int reducedMinutes = minutes - (hours * 60);
        int reducedSeconds = seconds - (minutes * 60);
        String hourString = new String();
        if (hours != 0) {
            hourString = hourString.concat(String.format("%dh ", hours));
        }
        String minuteString = new String();
        if (reducedMinutes != 0 || hours != 0) {
            minuteString = minuteString.concat(String.format("%dm ", reducedMinutes));
        }
        return String.format("%s%s%ds", hourString, minuteString, reducedSeconds);
    }

    @Override
    public void notify(TimerInterval interval) {
        MainActivity.playSound();
    }

    @Override
    public void notify(float intervalPercentRemaining) {
        intervalProgress.setProgress(100 - (int)(intervalPercentRemaining * 100));
    }

    @Override
    public void notify(int secondsRemaining) {
        long intervalTimeRemaining = (secondsRemaining % this.timer.fullIntervalTimeSeconds);
        String intervalTimerText = getIntervalTimerTextFromSeconds((int)intervalTimeRemaining);
        String elapsedTimerText = getIntervalTimerTextFromSeconds(secondsRemaining);
        fullTimer.setText(elapsedTimerText);
        loopTimer.setText(intervalTimerText);
    }
}
