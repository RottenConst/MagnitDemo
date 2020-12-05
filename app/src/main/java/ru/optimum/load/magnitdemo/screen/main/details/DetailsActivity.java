package ru.optimum.load.magnitdemo.screen.main.details;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import ru.optimum.load.magnitdemo.R;
import ru.optimum.load.magnitdemo.screen.main.MainActivity;
import ru.optimum.load.magnitdemo.screen.main.details.fragments.processd.DistrictsFragment;
import ru.optimum.load.magnitdemo.screen.main.details.fragments.processd.DynamicFragment;
import ru.optimum.load.magnitdemo.screen.main.details.fragments.processd.GroupFragment;
import ru.optimum.load.magnitdemo.screen.main.details.fragments.time.DistrictsTimeFragment;
import ru.optimum.load.magnitdemo.screen.main.details.fragments.time.DynamicTimeFragment;
import ru.optimum.load.magnitdemo.screen.main.details.fragments.time.GroupTimeFragment;
/*
    второстипенный для отображения экранов с детальными данными
 */
public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_activity);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        //по указанному значению определяется какой фрагмент показывать
        int data = intent.getIntExtra("data", 0);

        BottomNavigationView navigationView = findViewById(R.id.navigation_details);

        navigationView.getMenu().getItem(0).setChecked(true);
        if (data != 0) {
            actionBar.setTitle("Обработанные ПД");
            getSupportFragmentManager().beginTransaction().replace(R.id.details_container, DynamicFragment.newInstance(data)).commit();
        } else {
            actionBar.setTitle("Время Обработки");
            getSupportFragmentManager().beginTransaction().replace(R.id.details_container, DynamicTimeFragment.newInstance()).commit();
        }
        navigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.dynamics:
                    if (data != 0) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.details_container, DynamicFragment.newInstance(data)).commit();
                    } else {
                        getSupportFragmentManager().beginTransaction().replace(R.id.details_container, DynamicTimeFragment.newInstance()).commit();
                    }
                    navigationView.getMenu().getItem(0).setChecked(true);
                    break;
                case R.id.districts:
                    if (data != 0) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.details_container, DistrictsFragment.newInstance(data)).commit();
                    } else {
                        getSupportFragmentManager().beginTransaction().replace(R.id.details_container, DistrictsTimeFragment.newInstance()).commit();
                    }
                    navigationView.getMenu().getItem(1).setChecked(true);
                    break;
                case R.id.group:
                    if (data != 0) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.details_container, GroupFragment.newInstance(data)).commit();
                    } else {
                        getSupportFragmentManager().beginTransaction().replace(R.id.details_container, GroupTimeFragment.newInstance()).commit();
                    }
                    navigationView.getMenu().getItem(2).setChecked(true);
                    break;
            }
            return false;
        });
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:

                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
