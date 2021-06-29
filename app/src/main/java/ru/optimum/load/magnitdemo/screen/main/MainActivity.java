package ru.optimum.load.magnitdemo.screen.main;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import ru.optimum.load.magnitdemo.R;
import ru.optimum.load.magnitdemo.app.DemoApp;
import ru.optimum.load.magnitdemo.app.SyncManager;
import ru.optimum.load.magnitdemo.screen.adapters.ViewPagerAdapter;
import ru.optimum.load.magnitdemo.screen.main.details.fragments.MonitoringFragment;
import ru.optimum.load.magnitdemo.screen.main.details.fragments.ReportsFragment;

/*
    Главный экран приложения
 */
public class MainActivity extends AppCompatActivity implements SyncManager.ISyncStatusListener, SwipeRefreshLayout.OnRefreshListener {

    private static boolean firstStart = true;
    BottomNavigationView navigationView;
//    TabLayout tabLayout;
//    ViewPager2 viewPager;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
//        viewPager = findViewById(R.id.viewpager);
//        tabLayout = findViewById(R.id.tab_container);

        actionBar = getSupportActionBar();
        DemoApp.syncManager().setSyncStatusListener(this);// регистрируем слушатель синхранизации

//        viewPager.setAdapter(new ViewPagerAdapter(this));
//
//        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
//            tab.setIcon(R.drawable.shape_oval);
//            if (position == 0) {
//                tab.setText("Мониторинг");
//            } else if (position == 1) {
//                tab.setText("Отчеты");
//            }
//        }).attach();

        navigationView = findViewById(R.id.navigation_details);
//        navigationView.getMenu().getItem(0).setChecked(true);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        navigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.monitoring:
                    navigationView.getMenu().getItem(0).setChecked(true);
                    actionBar.setTitle("Мониторинг");
                    getSupportFragmentManager().beginTransaction().replace(R.id.tab_container, MonitoringFragment.newInstance()).commit();
                    break;
                case R.id.reports:
                    navigationView.getMenu().getItem(1).setChecked(true);
                    actionBar.setTitle("Отчеты");
                    getSupportFragmentManager().beginTransaction().replace(R.id.tab_container, ReportsFragment.newInstance()).commit();
                    break;
            }
            return false;
        });

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
            showToast("SYNC RESULT: " + strResult + ":" + strError);
            onRefresh();
        }

    }

    //вывод сообщений
    private void showToast(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.show();
    }

    public void setTitleActionBar(String titleActionBar) {
        actionBar.setTitle(titleActionBar);
    }

    @Override
    public void onRefresh() {
        navigationView.getMenu().getItem(0).setChecked(true);
        actionBar.setTitle("Мониторинг");
        getSupportFragmentManager().beginTransaction().replace(R.id.tab_container, MonitoringFragment.newInstance()).commit();
    }
}