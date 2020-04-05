package com.emomtimer;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v4.widget.CircularProgressDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Console;

public class MainActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer;
    private TextView loopTimer;
    private TextView fullTimer;
    private CountDownTimer countDownTimer = null;
    private ProgressBar intervalProgress;

    private void playSound(){
        this.mediaPlayer.start();
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button startButton = findViewById(R.id.startButton);
        Button stopButton = findViewById(R.id.stopButton);

        loopTimer = findViewById(R.id.loopTimer);
        fullTimer = findViewById(R.id.fullTimer);
        intervalProgress = (ProgressBar)findViewById(R.id.intervalProgress);
        intervalProgress.setProgress(intervalProgress.getMax(), true);

        mediaPlayer = MediaPlayer.create(this.getApplicationContext(), R.raw.alarmsound);
        final Context con = this.getApplicationContext();
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playSound();

                runTimer(60, 3600);
            }
        });
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                destroyTimer();
                Toast.makeText(con, new Integer(intervalProgress.getProgress()).toString(), Toast.LENGTH_LONG).show();
                resetTimerText();
            }
        });
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
                    playSound();
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
