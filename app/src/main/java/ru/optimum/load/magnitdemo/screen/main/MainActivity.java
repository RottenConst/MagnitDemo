package ru.optimum.load.magnitdemo.screen.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.zip.Inflater;

import ru.optimum.load.magnitdemo.DBContact;
import ru.optimum.load.magnitdemo.R;
import ru.optimum.load.magnitdemo.app.DemoApp;
import ru.optimum.load.magnitdemo.app.SyncManager;
import ru.optimum.load.magnitdemo.data.MainData;
import ru.optimum.load.magnitdemo.db.DatabaseWrapper;
import ru.optimum.load.magnitdemo.screen.adapters.GridAdapterCard;
import ru.optimum.load.magnitdemo.screen.adapters.SpinnerAdapterPeriod;
import ru.optimum.load.magnitdemo.screen.main.details.fragments.MonitoringFragment;
import ru.optimum.load.magnitdemo.screen.main.details.fragments.processd.DynamicFragment;

/*
    Главный экран приложения
 */
public class MainActivity extends AppCompatActivity implements SyncManager.ISyncStatusListener, SwipeRefreshLayout.OnRefreshListener {

    private static boolean firstStart = true;
    BottomNavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        DemoApp.syncManager().setSyncStatusListener(this);// регистрируем слушатель синхранизации

        navigationView = findViewById(R.id.navigation_details);


    }

    //отключить слушатель при завершении
    @Override
    protected void onDestroy() {
        DemoApp.syncManager().setSyncStatusListener(null);
        super.onDestroy();
    }

    //запустить синхранизацию после запуска
    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (firstStart) {
            firstStart = false;
            DemoApp.syncManager().startSync();
        }
    }

       //вывод сообщений о статусе синхранизации с платформой
    @Override
    public void updateSyncStatus(SyncManager.SyncStatus status) {
        if (status.started) {
            showToast("SYNC STARTED");
            return;
        } else {
            String strResult = (status.lastSyncResult != null) ? status.lastSyncResult.name() : "No result";
            String strError = (status.lastSyncError != null) ? status.lastSyncResult.name() : "No error";
            showToast("SYNC RESULT: "+ strResult + ":" + strError);
            onRefresh();
        }

    }

    //вывод сообщений
    private void showToast (String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.show();
    }

    @Override
    public void onRefresh() {
        navigationView.getMenu().getItem(0).setChecked(true);
        getSupportFragmentManager().beginTransaction().replace(R.id.tab_container, MonitoringFragment.newInstance()).commit();
    }
}