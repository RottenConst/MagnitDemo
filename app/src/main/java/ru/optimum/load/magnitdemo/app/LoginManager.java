package ru.optimum.load.magnitdemo.app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.NonNull;

import ru.cdc.optimum.PlatformVariables;
import ru.cdc.optimum.Synchronization;
import ru.cdc.optimum.auth.AuthenticationResult;
import ru.cdc.optimum.auth.Credentials;
/*
    Класс для подключения и аутентификации на платформе Optimum
 */
public class LoginManager implements Synchronization.Listener {

    public interface ILoginStatusListener {
        void updateLoginStatus();
    }

    private SQLiteDatabase db;
    private AuthenticationResult lastResult;
    private ILoginStatusListener listener = null;
    private String login = null;

    public LoginManager (Context context, SQLiteDatabase db) {
        this.db = db;
    }

    //передача логина и пароля на платформу
    @SuppressLint("MissingPermission")
    public void login (String loginString, String passwordString) {
        lastResult = null;
        login = loginString;
        Credentials credentials = new Credentials(loginString, passwordString);
        Synchronization.setCredentials(credentials);
        Synchronization.authenticate(db, true);
    }

    @Override
    public void onSynchronizationStart() {

    }

    @Override
    public void onSynchronizationError(@NonNull Synchronization.Error error) {

    }

    //завершение синхранизации
    @Override
    public void onComplete(@NonNull AuthenticationResult authenticationResult) {
        lastResult = authenticationResult;
        if (authenticationResult.getCode() == AuthenticationResult.Code.DISABLED || authenticationResult.getCode() == AuthenticationResult.Code.SUCCESS) {
            PlatformVariables.newInstance(db).set("@login", login).commit(db);
        }
        fireStatusUpdate();
    }

    @Override
    public void onSynchronizationEnd(@NonNull Synchronization.Result result) {

    }

    @Override
    public void onSynchronizationGroupEnd(@NonNull String s, @NonNull Synchronization.Result result) {

    }

    //Включение\выключение слушателя слушателя
    public void setLoginStatusListener(ILoginStatusListener listener) {
        if (listener != null)
        {
            Synchronization.registerSyncronizationHandler(this);
        } else {
            Synchronization.unregisterSyncronizationHandler(this);
        }
        this.listener = listener;
    }

    //запись статуса слушателя
    public void fireStatusUpdate() {
        if (listener != null) {
            listener.updateLoginStatus();
        }
    }

    //результат синхранизации
    public AuthenticationResult getLastResult() {
        return lastResult;
    };
}
