package ru.optimum.load.magnitdemo.app;


import android.os.Handler;
import android.util.Log;

import java.util.Date;
/*
    Класс для настройки автоматической синхранизации
 */
public class AutoSync {
    private static final int AUTO_SYNC_PERIOD_IN_SECONDS = 15 * 60;
    private static final int DELAY_INTERVAL_IN_SECONDS = 5;

    private Handler handler;
    private Runnable autoSyncRunnable;

    public AutoSync() {
        handler = new Handler();
        autoSyncRunnable = new AutoSyncTask();
    }

    public void start() {
        handler.removeCallbacks(autoSyncRunnable);
        handler.postDelayed(autoSyncRunnable, DELAY_INTERVAL_IN_SECONDS * 1000);
        Log.i("AUTOSYNC", "Init auto sync at " + new Date());
    }

    public void stop() {
        handler.removeCallbacks(autoSyncRunnable);
    }

    private class AutoSyncTask implements Runnable {
        private long lastAttemptTime;

        public AutoSyncTask() {
            lastAttemptTime = System.currentTimeMillis();
        }

        @Override
        public void run() {
            int autoSyncPeriod = AUTO_SYNC_PERIOD_IN_SECONDS;

            long currentAttempt = System.currentTimeMillis();
            if (currentAttempt >= lastAttemptTime + autoSyncPeriod*1000) {
                Log.i("AUTOSYNC", "Start auto sync at " + new Date(currentAttempt));
                lastAttemptTime = currentAttempt;
                performSync();
            }
            handler.postDelayed(autoSyncRunnable, DELAY_INTERVAL_IN_SECONDS*1000);
            return;
        }
    }

    private void performSync() {
        String serverAddress = Prefs.getServerAddress();
        int serverPort = Prefs.getServerPort();

        DemoApp.syncManager().setParameters(serverAddress, serverPort, 5, true);
        DemoApp.syncManager().startSync();
    }
}
