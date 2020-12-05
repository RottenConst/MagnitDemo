package ru.optimum.load.magnitdemo.screen.main.details.fragments.time;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;

import ru.optimum.load.magnitdemo.R;
import ru.optimum.load.magnitdemo.app.DemoApp;
import ru.optimum.load.magnitdemo.db.DatabaseWrapper;
import ru.optimum.load.magnitdemo.help.ChartGraf;
import ru.optimum.load.magnitdemo.screen.adapters.SpinnerAdapterPeriod;
/*
    Отображение детальной информации о ввремени обработки на графике
 */
public class DynamicTimeFragment extends Fragment {

    ChartGraf chartGraf;
    private BarChart dynamicChart;
    private Spinner intervalSpinner;
    private DatabaseWrapper dbWrapper;
    int process = 0;
    int avgProcess = 0;
    int workTime = 0;
    int avgWorkTime = 0;
    int waitTime = 0;
    int avgWaitTime = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dynamic_time_fragment, container, false);
        dynamicChart = view.findViewById(R.id.dynamic_time_chart);
        intervalSpinner = view.findViewById(R.id.period_of_time_spinner);
        chartGraf = new ChartGraf(dynamicChart);
        dbWrapper = DemoApp.dbWrapper();
        initValues();
        initSpinner();
        intervalSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        initValuesWithDate("2020");
                        chartGraf.drawChartTimeProcess(getContext(), process, avgProcess, workTime, avgWorkTime, waitTime, avgWaitTime);
                        break;
                    case 1:
                        initValuesWithDate("2020-10-07");
                        chartGraf.drawChartTimeProcess(getContext(), process, avgProcess, workTime, avgWorkTime, waitTime, avgWaitTime);
                        break;
                    case 2:
                        initValuesWithDate("2020-10-01");
                        chartGraf.drawChartTimeProcess(getContext(), process, avgProcess, workTime, avgWorkTime, waitTime, avgWaitTime);
                        break;
                    case 3:
                        initValuesWithDate("2020-08-01");
                        chartGraf.drawChartTimeProcess(getContext(), process, avgProcess, workTime, avgWorkTime, waitTime, avgWaitTime);
                        break;
                    case 4:
                        initValuesWithDate("2020-06-01");
                        chartGraf.drawChartTimeProcess(getContext(), process, avgProcess, workTime, avgWorkTime, waitTime, avgWaitTime);
                        break;
                    case 5:
                        initValuesWithDate("2020-01-01");
                        chartGraf.drawChartTimeProcess(getContext(), process, avgProcess, workTime, avgWorkTime, waitTime, avgWaitTime);
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        chartGraf.drawChartTimeProcess(getContext(), process, avgProcess, workTime, avgWorkTime, waitTime, avgWaitTime);

        return view;
    }

    private void initSpinner() {
        String[] period = {"За все время", "За последние 7 дней", "За месяц", "За 3 месяца", "За 6 месяцев", "За последний год"};
        SpinnerAdapterPeriod adapter = new SpinnerAdapterPeriod(getContext(), R.layout.row_spinner, period);
        intervalSpinner.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void initValuesWithDate(String date) {
        Cursor cursor = dbWrapper.getTimeOfProcessingWithDate(date);
        if (cursor != null) {
            cursor.moveToFirst();
            process = cursor.getInt(1);
            avgProcess = cursor.getInt(2);
            workTime = cursor.getInt(3);
            avgWorkTime = cursor.getInt(4);
            waitTime = cursor.getInt(5);
            avgWaitTime = cursor.getInt(6);
        }
        cursor.close();
    }

    private void initValues() {
        Cursor cursor = dbWrapper.getTimeOfProcessing();
        if (cursor != null) {
            cursor.moveToFirst();
            process = cursor.getInt(0);
            avgProcess = cursor.getInt(1);
            workTime = cursor.getInt(2);
            avgWorkTime = cursor.getInt(3);
            waitTime = cursor.getInt(4);
            avgWaitTime = cursor.getInt(5);
        }
        cursor.close();
    }

    public static DynamicTimeFragment newInstance() {
        return new DynamicTimeFragment();
    }
}
