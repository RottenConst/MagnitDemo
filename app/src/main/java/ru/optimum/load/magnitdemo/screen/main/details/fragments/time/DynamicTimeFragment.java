package ru.optimum.load.magnitdemo.screen.main.details.fragments.time;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

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
    private DatabaseWrapper dbWrapper;
    int process = 0;
    int avgProcess = 0;
    int workTime = 0;
    int avgWorkTime = 0;
    int waitTime = 0;
    int avgWaitTime = 0;
    Button btnSetDateFrom;
    Button btnSetDateBefore;
    Button btnShowDate;
    Calendar calendar;
    int setYear;
    int setMonth;
    int setDayOfMonth;
    String dateFrom;
    String dateBefore;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dynamic_time_fragment, container, false);
        dynamicChart = view.findViewById(R.id.dynamic_time_chart);
        chartGraf = new ChartGraf(dynamicChart);
        btnSetDateFrom = view.findViewById(R.id.btn_set_date_from);
        btnSetDateBefore = view.findViewById(R.id.btn_set_date_before);
        btnShowDate = view.findViewById(R.id.btn_show_data_graf);

        calendar = Calendar.getInstance();
        setYear = calendar.get(Calendar.YEAR);
        setMonth = calendar.get(Calendar.MONTH);
        setDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        dateFrom = "2020-01-01";
        dateBefore = setYear + "-" + setMonth + "-" + setDayOfMonth;

        dbWrapper = DemoApp.dbWrapper();
        initValues();

        chartGraf.drawChartTimeProcess(getContext(), process, avgProcess, workTime, avgWorkTime, waitTime, avgWaitTime);

        btnSetDateFrom.setOnClickListener(v ->
            new DatePickerDialog(getContext(), dateSetListenerFrom, setYear, setMonth, setDayOfMonth).show()
        );

        btnSetDateBefore.setOnClickListener(v ->
            new DatePickerDialog(getContext(), dateSetListenerBefore, setYear, setMonth, setDayOfMonth).show()
        );

        btnShowDate.setOnClickListener(v -> {
            initValuesWithDate(dateFrom, dateBefore);
            chartGraf.drawChartTimeProcess(getContext(), process, avgProcess, workTime, avgWorkTime, waitTime, avgWaitTime);
        });

        return view;
    }

    private void initValuesWithDate(String dateFrom, String dateBefore) {
        Cursor cursor = dbWrapper.getTimeOfProcessingWithDate(dateFrom, dateBefore);
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

    DatePickerDialog.OnDateSetListener dateSetListenerFrom = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            setYear = year;
            setMonth = month;
            setDayOfMonth = dayOfMonth;
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = new GregorianCalendar(setYear, setMonth, setDayOfMonth);
            dateFrom = df.format(calendar.getTime());
            btnSetDateFrom.setTextSize(14);
            btnSetDateFrom.setText("От: " + dateFrom);
        }
    };


    DatePickerDialog.OnDateSetListener dateSetListenerBefore = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            setYear = year;
            setMonth = month;
            setDayOfMonth = dayOfMonth;
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = new GregorianCalendar(setYear, setMonth, setDayOfMonth);
            dateBefore = df.format(calendar.getTime());
            btnSetDateBefore.setTextSize(14);
            btnSetDateBefore.setText("До: " + dateBefore);
        }
    };

    public static DynamicTimeFragment newInstance() {
        return new DynamicTimeFragment();
    }
}
