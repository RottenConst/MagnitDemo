package ru.optimum.load.magnitdemo.app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.NonNull;

import ru.cdc.optimum.ConnectionParameters;
import ru.cdc.optimum.PlatformVariables;
import ru.cdc.optimum.Synchronization;
import ru.cdc.optimum.auth.AuthenticationResult;

import static ru.cdc.optimum.Synchronization.*;
import static ru.cdc.optimum.Synchronization.Error;
/*
    Класс для запуска синхронизации приложения с платформой
 */
public class SyncManager implements Listener {
    public static final int MINUTES_TO_MILLISECONDS = 60 * 1000;
    public static final String SYNC_GROUP_DEFAULT = "default"; //группа синхранизации
    public static final String HOST = "mrm.logstream.ru"; // адресс сервера
    public static final int PORT = 11126; // порт сервера

    private static final String TAG = "SYNC_MNGR";

    public static class SyncStatus {
        public boolean started;
        public Result lastSyncResult;
        public Error lastSyncError;

        public SyncStatus(boolean started, Result lastSyncResult, Error lastSyncError) {
            this.started = started;
            this.lastSyncResult = lastSyncResult;
            this.lastSyncError = lastSyncError;
        }
    }

    public interface ISyncStatusListener {
        void updateSyncStatus(SyncStatus status);
    }

    private ISyncStatusListener listener = null;
    private SQLiteDatabase db;

    private boolean started = false;
    private Result lastSyncResult = null;
    private Error lastSyncError = null;

    public SyncManager(Context context, SQLiteDatabase db) {
        this.db = db;
        setApplicationContext(context);
        registerSyncronizationHandler(this);

        //параметры подключения к платформе
        ConnectionParameters cp = getConnectionParameters();
        cp.setHostName(HOST);
        cp.setPortNumber(PORT);
        cp.setConnectionTimeout(MINUTES_TO_MILLISECONDS);
        cp.setReadTimeout(MINUTES_TO_MILLISECONDS);
        cp.useSecureConnection(true); //true если подключен сертификат шифрования false елси нет
    }

    public void setSyncStatusListener(ISyncStatusListener listener) {
        this.listener = listener;
    }

    //Обновить статус
    private void fireStatusUpdate() {
        if (listener != null) {
            listener.updateSyncStatus(getStatus());
        }
    }

    public SyncStatus getStatus() {
        return  new SyncStatus(started, lastSyncResult, lastSyncError);
    }

    //метод для изменения параметров подключения к платформе
    public void setParameters(String serverAddress, int serverPort, int timeout, boolean useSecureConnection) {
        ConnectionParameters cp = Synchronization.getConnectionParameters();
        cp.setHostName(serverAddress);
        cp.setPortNumber(serverPort);
        cp.setConnectionTimeout(timeout * MINUTES_TO_MILLISECONDS);
        cp.setReadTimeout(timeout * MINUTES_TO_MILLISECONDS);
        cp.useSecureConnection(useSecureConnection);
    }

    //запуск синхранизации
    @SuppressLint("MissingPermission")
    public boolean startSync() {
        if (Synchronization.getResult() == null) {
            return false;
        }

        Log.i(TAG, "startSync()");
        started = true;
        lastSyncResult = null;
        lastSyncError = null;
        Log.i(TAG, "startedSync() " + started);

        Synchronization.execute(db, SYNC_GROUP_DEFAULT);
        return true;
    }

    public void close() {Synchronization.unregisterSyncronizationHandler(this);}

    @Override
    public void onSynchronizationStart() {
        Log.i(TAG, "onSynchronizationStart()");
        started = true;
        lastSyncResult = null;
        lastSyncError = null;
        fireStatusUpdate();
    }

    @Override
    public void onSynchronizationError(@NonNull Error error) {
        Log.i(TAG,"onSynchronizationError(" + error.name() + ")");
        lastSyncError = error;
        fireStatusUpdate();
    }

    @Override
    public void onComplete(@NonNull AuthenticationResult authenticationResult) {

    }

    //передача параметров если синхранизация прошла успешно
    @Override
    public void onSynchronizationEnd(@NonNull Result result) {
        started = false;
        lastSyncResult = result;
        Log.i(TAG, "onSynchronizationEnd(" + result.name() + ")");
        if (result == Result.SUCCESS) {
            String readableName = PlatformVariables.getString(db, "@login");
            if (readableName != null) {
                Synchronization.setDeviceReadableName(db, readableName);
            }
        }
        fireStatusUpdate();
    }

    @Override
    public void onSynchronizationGroupEnd(@NonNull String s, @NonNull Result result) {
        Log.i(TAG, "onSynchronizationGroupEnd(" + s + " - " + result.name() + ")");
    }
}
