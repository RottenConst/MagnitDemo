package ru.optimum.load.magnitdemo.screen.login;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.security.Permission;

import ru.cdc.optimum.ConnectionParameters;
import ru.cdc.optimum.PlatformVariables;
import ru.cdc.optimum.Synchronization;
import ru.cdc.optimum.auth.AuthenticationResult;
import ru.cdc.optimum.auth.Credentials;
import ru.optimum.load.magnitdemo.app.DemoApp;
import ru.optimum.load.magnitdemo.R;
import ru.optimum.load.magnitdemo.app.LoginManager;
import ru.optimum.load.magnitdemo.screen.main.MainActivity;

import static java.lang.String.format;
import static ru.cdc.optimum.Synchronization.Listener;
import static ru.cdc.optimum.Synchronization.Result;
import static ru.cdc.optimum.Synchronization.registerSyncronizationHandler;
import static ru.optimum.load.magnitdemo.app.LoginManager.*;

/*
    Экран для авторизации входа в приложение
 */
public class LoginActivity extends AppCompatActivity implements ILoginStatusListener, ActivityCompat.OnRequestPermissionsResultCallback {

    Button loginIn;
    private static boolean enter = false;

    // проверяем разрешения
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {}
        else { finish(); }
        return;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_input_layout);

        //проверяем разрешение для записи данных с платформы Optimum
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_PHONE_STATE}, 0);
        }
        loginIn = findViewById(R.id.btn_enter);
        DemoApp.loginManager().setLoginStatusListener(this);

        loginIn.setOnClickListener(v -> {
            DemoApp.loginManager().login("login", ""); //при клике прверяем логин и пароль на платформе Optimum
            enter = false;
        });
    }

    //онулить слушатель после входа
    @Override
    protected void onDestroy() {
        DemoApp.loginManager().setLoginStatusListener(null);
        super.onDestroy();
    }

    // если авторизаця выключина или прошла успешно переходим на главный экран приложения иначе выводится сообщение об ошибке
    @Override
    public void updateLoginStatus() {
        AuthenticationResult ares = DemoApp.loginManager().getLastResult();


        Log.i("LOGIN", getString(getAuthError(ares)));

        if (ares.getCode() == AuthenticationResult.Code.DISABLED | ares.getCode() == AuthenticationResult.Code.SUCCESS){
            enterLogin(this, enter);
            enter = true;
            finish();
        } else {
            String msg = format("%s", "ОШИБКА: " + getString(getAuthError(ares)));
            showToast(msg);
        }
    }

    //возможные ошибки
    private int getAuthError(AuthenticationResult error) {
        switch (error.getCode()) {
            case SUCCESS: return R.string.authentication_message_success;
            case ERROR: return R.string.authentication_message_error;
            case FAIL: return R.string.authentication_message_fail;
            case INVALID_CREDENTIALS: return R.string.authentication_message_invalid_credentials;
            case INVALID_SERVER_CONFIGURATION: return R.string.authentication_message_invalid_server_configuration;
            case OFFLINE_DENIED: return R.string.authentication_message_offline_denied;
            case OFFLINE_EXPIRED: return R.string.authentication_message_offline_expired;
            case OFFLINE_NOTEXISTS: return R.string.authentication_message_offline_notexists;
            default: return R.string.no_data;
        }
    }

    //функция перехода на главный экран
    private void enterLogin(Context context, boolean enter) {
        if (!enter) {
            Intent intent = new Intent(context, MainActivity.class);
            startActivity(intent);
        } else {
            return;
        }
    }

    //функция для вывода сообщения
    private void showToast (String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.show();
    }
}
