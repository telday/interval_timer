package com.emomtimer.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.emomtimer.IntervalTimerApplication;
import com.emomtimer.R;
import com.emomtimer.timer.IntervalTimer;

public class SettingsFragment extends Fragment {
    EditText workTime;
    EditText restTime;
    EditText intervals;
    View view;
    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    private int getWorkTime(){
        String text = workTime.getText().toString();
        if (text.equals("")){
            text = "0";
        }
        return Integer.valueOf(text);
    }

    private int getRestTime(){
        String text = restTime.getText().toString();
        if (text.equals("")){
            text = "0";
        }
        return Integer.valueOf(text);
    }

    private int getIntervals(){
        String text = intervals.getText().toString();
        if (text.equals("")){
            text = "0";
        }
        return Integer.valueOf(text);
    }

    private class SettingsTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) { }

        @Override
        public void afterTextChanged(Editable s) {
            long workTimeSeconds = getWorkTime();
            long restTimeSeconds = getRestTime();
            int intervals = getIntervals();
            IntervalTimer timer = new IntervalTimer(workTimeSeconds, restTimeSeconds, intervals);
            IntervalTimerApplication.timer = timer;
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.settings_fragment, container, false);
        workTime = view.findViewById(R.id.workTime);
        restTime = view.findViewById(R.id.restTime);
        intervals = view.findViewById(R.id.intervals);

        workTime.addTextChangedListener(new SettingsTextWatcher());
        restTime.addTextChangedListener(new SettingsTextWatcher());
        intervals.addTextChangedListener(new SettingsTextWatcher());
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
