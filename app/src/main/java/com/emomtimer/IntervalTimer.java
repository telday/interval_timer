package com.emomtimer;

import android.content.Context;
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


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link IntervalTimer#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IntervalTimer extends Fragment {
    /* Example Params
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    */

    private TextView loopTimer;
    private TextView fullTimer;
    private CountDownTimer countDownTimer = null;
    private ProgressBar intervalProgress;

    public IntervalTimer() { }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment IntervalTimer.
     */
    public static IntervalTimer newInstance(MediaPlayer mediaPlayer) {
        IntervalTimer fragment = new IntervalTimer();
        Bundle args = new Bundle();
        /* Example of using params This would normally take param1 and param2

        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
         */
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* Example of using arguments
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        */
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_interval_timer, container, false);
        Button startButton = view.findViewById(R.id.startButton);
        Button stopButton = view.findViewById(R.id.stopButton);

        loopTimer = view.findViewById(R.id.loopTimer);
        fullTimer = view.findViewById(R.id.fullTimer);
        intervalProgress = (ProgressBar)view.findViewById(R.id.intervalProgress);
        intervalProgress.setProgress(intervalProgress.getMax(), true);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.playSound();
                runTimer(60, 3600);
            }
        });
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                destroyTimer();
                resetTimerText();
            }
        });
        return view;
    }

    private void resetTimerText(){
        fullTimer.setText(getResources().getString(R.string.total_time));
        loopTimer.setText(getResources().getString(R.string.interval_time));
    }
    private void destroyTimer(){
        if (countDownTimer != null){
            countDownTimer.cancel();
        }
        countDownTimer = null;
        intervalProgress.setProgress(intervalProgress.getMax(), true);
    }
    private String getIntervalTimerTextFromSeconds(int seconds){
        int minutes = (seconds - (seconds % 60)) / 60;
        int hours = (minutes - (minutes % 60)) / 60;
        int reducedMinutes = minutes - (hours * 60);
        int reducedSeconds = seconds - (minutes * 60);
        String hourString = new String();
        if (hours != 0) {
            hourString.concat(String.format("%dh ", hours));
        }
        String minuteString = new String();
        if (reducedMinutes != 0 || hours != 0) {
            minuteString.concat(String.format("%dm ", reducedMinutes));
        }
        return String.format("%s%s%ds", hourString, minuteString, reducedSeconds);
    }

    /**
     * Starts the on screen timer
     *
     * @param loopLength The length of the alarm loop in seconds
     * @param totalTime The total length of the timer in seconds
     */
    private void runTimer(final int loopLength, int totalTime){
        destroyTimer();

        countDownTimer = new CountDownTimer(totalTime * 1000, 1000) {
            private Long secondsPassed = new Long(0);
            @Override
            public void onTick(long millisUntilFinished) {
                secondsPassed++;
                if (secondsPassed % loopLength == 0){
                    MainActivity.playSound();
                }
                loopTimer.setText(getIntervalTimerTextFromSeconds(new Long(loopLength - (secondsPassed % loopLength)).intValue()));
                fullTimer.setText(getResources().getString(R.string.total_time) + secondsPassed.toString());
                double percentFinished = ((float)(secondsPassed % loopLength) / loopLength) * intervalProgress.getMax();
                intervalProgress.setProgress(intervalProgress.getMax() - (int)percentFinished, true);
            }

            @Override
            public void onFinish() {
                System.out.print("test");
            }
        }.start();
    }
}
