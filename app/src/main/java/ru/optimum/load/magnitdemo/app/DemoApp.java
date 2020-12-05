package ru.optimum.load.magnitdemo.app;

import android.app.Application;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.util.Log;

import ru.cdc.optimum.Synchronization;
import ru.optimum.load.magnitdemo.db.DatabaseWrapper;

/*
    Главный класс приложения для запуска необходимых служб и быстрового доступа к ним
 */
public class DemoApp extends Application {
    private static DemoApp app;
    private static OptimumOpenHelper helper;
    private static DatabaseWrapper dbw;
    private static SyncManager syncManager;
    private static LoginManager loginManager;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        helper = new OptimumOpenHelper(getApplicationContext());
        SQLiteDatabase db = helper.getReadableDatabase();
        dbw = new DatabaseWrapper(db);
        //запуск служб автоторизации и синхронизацц
        syncManager = new SyncManager(getApplicationContext(), db);
        loginManager = new LoginManager(getApplicationContext(), db);
        //передача платформе контекста приложения
        Synchronization.setApplicationContext(getApplicationContext());
    }

    public SQLiteDatabase db() {
        return helper.getWritableDatabase();
    }

    public static DemoApp app() {
        return app;
    }

    public static DatabaseWrapper dbWrapper() {
        return  dbw;
    }

    public static SyncManager syncManager() { return  syncManager; }

    public static LoginManager loginManager() { return  loginManager; }

    public static SharedPreferences getSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(app);
    }
}
