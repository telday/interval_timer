package com.emomtimer;

import android.media.MediaPlayer;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.emomtimer.timer.EMOMTimer;
import com.emomtimer.timer.IntervalTimer;
import com.emomtimer.timer.IntervalTimerObserver;
import com.emomtimer.timer.TimerInterval;
import com.emomtimer.ui.IntervalTimerPagerAdapter;

public class MainActivity extends AppCompatActivity implements IntervalTimerObserver {
    private static MediaPlayer mediaPlayer;

    public static void playSound(){
        MainActivity.mediaPlayer.start();
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.alarmsound);

        EMOMTimer timer = new EMOMTimer(60, 10);
        timer.register(this);

        ViewPager pager = findViewById(R.id.viewPager);
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        FragmentManager fm = getSupportFragmentManager();
        pager.setAdapter(new IntervalTimerPagerAdapter(fm));
        tabLayout.setupWithViewPager(pager);


        timer.startNewTimer();
    }

    @Override
    public void notify(TimerInterval interval) {

    }
}
