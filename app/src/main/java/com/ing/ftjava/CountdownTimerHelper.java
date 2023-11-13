package com.ing.ftjava;

import android.os.CountDownTimer;
import android.widget.ProgressBar;

public class CountdownTimerHelper {
    private static final long COUNTDOWN_INTERVAL = 1000; // 1 saniye
    private static final long COUNTDOWN_TIME = 750000; // 60 saniye

    private CountDownTimer countDownTimer;
    private TimerListener timerListener;

    ProgressBar progressBar;

    public CountdownTimerHelper(TimerListener timerListener) {
        this.timerListener = timerListener;
    }

    public void startTimerWithTime(long timeInMillis) {
        countDownTimer = new CountDownTimer(timeInMillis, COUNTDOWN_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                long secondsRemaining = millisUntilFinished / 1000;
                int progress = (int) (secondsRemaining * 100 / (timeInMillis / 1000));
                timerListener.onTimerTick(secondsRemaining, progress);
            }

            @Override
            public void onFinish() {
                timerListener.onTimerFinish();
            }
        };

        countDownTimer.start();
    }

    public void startTimer() {
        countDownTimer = new CountDownTimer(COUNTDOWN_TIME, COUNTDOWN_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                long secondsRemaining = millisUntilFinished / 1000;
                int progress = (int) (secondsRemaining * 100 / (COUNTDOWN_TIME / 1000));
                timerListener.onTimerTick(secondsRemaining, progress);

            }

            @Override
            public void onFinish() {
                timerListener.onTimerFinish();
            }
        };

        countDownTimer.start();
    }

    public void cancelTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    public interface TimerListener {
        void onTimerTick(long secondsUntilFinished, int progress);

        void onTimerFinish();
    }
}
