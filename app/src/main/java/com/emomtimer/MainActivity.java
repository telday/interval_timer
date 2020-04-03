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
    }

    /**
     * Starts the on screen timer
     *
     * @param loopLength The length of the alarm loop in seconds
     * @param totalTime The total length of the timer in seconds
     */
    private void runTimer(final int loopLength, int totalTime){
        if (countDownTimer != null){
            countDownTimer.cancel();
        }
        countDownTimer = new CountDownTimer(totalTime * 1000, 1000) {
            private Long secondsPassed = new Long(0);
            @Override
            public void onTick(long millisUntilFinished) {
                secondsPassed++;
                if (secondsPassed % loopLength == 0){
                    playSound();
                }
                loopTimer.setText(new Long(secondsPassed % loopLength).toString());
                fullTimer.setText(secondsPassed.toString());
            }

            @Override
            public void onFinish() {
                System.out.print("test");
            }
        }.start();
    }
}
