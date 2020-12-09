package ru.optimum.load.magnitdemo.screen.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ru.optimum.load.magnitdemo.DBContact;
import ru.optimum.load.magnitdemo.R;
import ru.optimum.load.magnitdemo.app.DemoApp;
import ru.optimum.load.magnitdemo.app.SyncManager;
import ru.optimum.load.magnitdemo.data.DataOnCardView;
import ru.optimum.load.magnitdemo.db.DatabaseWrapper;
import ru.optimum.load.magnitdemo.screen.adapters.GridAdapterCard;
import ru.optimum.load.magnitdemo.screen.main.details.DetailsActivity;
/*
    Главный экран приложения
 */
public class MainActivity extends AppCompatActivity implements SyncManager.ISyncStatusListener, SwipeRefreshLayout.OnRefreshListener {

    private ProgressBar progressSla;
    private TextView slaValue;
    private RecyclerView cardRecycler;
    private Button details;
    private TextView totalProcessTime;
    private TextView workTimeTV;
    private TextView totalTime;
    private final List<DataOnCardView> listOfDataCard = new ArrayList<>();
    GridAdapterCard adapterCard;
    private DatabaseWrapper dbWrite;

    private static boolean firstStart = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.monitoring_activity);
        DemoApp.syncManager().setSyncStatusListener(this);// регистрируем слушатель синхранизации

        dbWrite = DemoApp.dbWrapper();
        listOfDataCard.add(getDataOnCard(DBContact.OpenSet.TABLE_NAME));
        listOfDataCard.add(getDataOnCard(DBContact.ProcessedSet.TABLE_NAME));
        listOfDataCard.add(getDataOnCard(DBContact.ReceiptSet.TABLE_NAME));
        findAllView();

        initCardRecycle(listOfDataCard);
        int percent = dbWrite.getSLAPercent();
        setProgressSla(percent); //показать процент sla на прогресс баре
        int totalProcessingTimeNum = dbWrite.getTotalProcessingTime(); //Запрашиваем из бд общще время обработки
        int workTimeNum = dbWrite.getTotalWorkTime(); //Запрашиваем из бд среднее всемя обработки
        int processExecution = dbWrite.getProcessExecution(); //запрашиваем из бд среднее всемя ожидания
        //отобразить полученные значения
        totalProcessTime.setText(totalProcessingTimeNum + " ч.");
        workTimeTV.setText(workTimeNum + " ч.");
        totalTime.setText(processExecution + " ч.");


        //переход на экран для просмотра подробных сведений
        details.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), DetailsActivity.class);
            intent.putExtra("graf", false);
            startActivity(intent);
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
            showToast("SYNC RESULT: "+ strResult + ":" + strError);
            onRefresh();
        }

    }

    //нахйти все необходимые view
    private void findAllView() {
        cardRecycler = findViewById(R.id.monitoring_card_recycler);
        progressSla = findViewById(R.id.progressSla);
        slaValue = findViewById(R.id.value_sla);
        details = findViewById(R.id.more_details_btn);
        totalProcessTime = findViewById(R.id.average_waiting_num);
        workTimeTV = findViewById(R.id.tv_work_time);
        totalTime = findViewById(R.id.total_time_proccesing_num);
    }

    //инициализация Recycler View
    private void initCardRecycle(List<DataOnCardView> listOfDataCard) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        adapterCard = new GridAdapterCard(this, listOfDataCard);

        cardRecycler.setLayoutManager(gridLayoutManager);
        cardRecycler.setAdapter(adapterCard);
    }

    //запросс данных общих днных для педварительново отобращения на  графиках
    private DataOnCardView getDataOnCard(String tableName) {
        int count = dbWrite.getCount(tableName, "2020-01-01", "2020-12-31");
        int sla75 = dbWrite.getSla75Expired(tableName, "2020-01-01", "2020-12-31");
        int slaExpired;
        if (tableName.equals(DBContact.ProcessedSet.TABLE_NAME)) {
            slaExpired = dbWrite.getSlaNotExpired("2020-01-01", "2020-12-31");
        } else {
            slaExpired = dbWrite.getSlaExpired(tableName, "2020-01-01", "2020-12-31");
        }
        return new DataOnCardView(count + sla75 + slaExpired, count, sla75, slaExpired);
    }

    //изменение прогресс бара
    @SuppressLint("SetTextI18n")
    private void setProgressSla(int value) {
        slaValue.setText(value + "%");
        progressSla.setProgress(value);
    }

    //вывод сообщений
    private void showToast (String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.show();
    }

    @Override
    public void onRefresh() {
        listOfDataCard.clear();
        listOfDataCard.add(getDataOnCard(DBContact.OpenSet.TABLE_NAME));
        listOfDataCard.add(getDataOnCard(DBContact.ProcessedSet.TABLE_NAME));
        listOfDataCard.add(getDataOnCard(DBContact.ReceiptSet.TABLE_NAME));
        adapterCard.setData(listOfDataCard);
        adapterCard.notifyDataSetChanged();
        int percent = dbWrite.getSLAPercent();
        setProgressSla(percent); //показать процент sla на прогресс баре
        int totalProcessingTimeNum = dbWrite.getTotalProcessingTime(); //Запрашиваем из бд общще время обработки
        int workTimeNum = dbWrite.getTotalWorkTime(); //Запрашиваем из бд среднее всемя обработки
        int processExecution = dbWrite.getProcessExecution(); //запрашиваем из бд среднее всемя ожидания
        //отобразить полученные значения
        totalProcessTime.setText(totalProcessingTimeNum + " ч.");
        workTimeTV.setText(workTimeNum + " ч.");
        totalTime.setText(processExecution + " ч.");
    }
}