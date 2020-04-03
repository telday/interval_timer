package com.emomtimer;

import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer;
    private TextView loopTimer;
    private TextView fullTimer;
    private CountDownTimer countDownTimer = null;

    private void playSound(){
        this.mediaPlayer.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button startButton = findViewById(R.id.startButton);
        Button stopButton = findViewById(R.id.stopButton);

        loopTimer = findViewById(R.id.loopTimer);
        fullTimer = findViewById(R.id.fullTimer);

        mediaPlayer = MediaPlayer.create(this.getApplicationContext(), R.raw.alarmsound);

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
                loopTimer.setText(getResources().getString(R.string.interval_time) + new Long(secondsPassed % loopLength).toString());
                fullTimer.setText(getResources().getString(R.string.total_time) + secondsPassed.toString());
            }

            @Override
            public void onFinish() {
                System.out.print("test");
            }
        }.start();
    }
}
