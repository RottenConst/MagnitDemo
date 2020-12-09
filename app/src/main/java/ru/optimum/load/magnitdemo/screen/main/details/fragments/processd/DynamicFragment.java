 package ru.optimum.load.magnitdemo.screen.main.details.fragments.processd;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import ru.optimum.load.magnitdemo.DBContact;
import ru.optimum.load.magnitdemo.R;
import ru.optimum.load.magnitdemo.app.DemoApp;
import ru.optimum.load.magnitdemo.data.DataOnCardView;
import ru.optimum.load.magnitdemo.db.DatabaseWrapper;
import ru.optimum.load.magnitdemo.help.ChartGraf;
import ru.optimum.load.magnitdemo.screen.adapters.SpinnerAdapterPeriod;
//фрагмент для отображения подробных данных на графике
 public class DynamicFragment extends Fragment {

    private static int dataBD;
    private int count;
    private int sla75Expired;
    private int slaExpired;
    private Calendar calendar;
    private int setYear;
    private int setMonth;
    private int setDayOfMonth;
    private String dateFrom;
    private String dateBefore;
    private DataOnCardView dataOnCardView;
    ChartGraf chartGraf;
    private BarChart dynamicChart;
    private ImageView legendGreen;
    private ImageView legendYellow;
    private ImageView legendRed;
    private TextView tvLegendGreen;
    private TextView tvLegendYellow;
    private TextView tvLegendRed;
    private Button btnSetDateFrom;
    private Button btnSetDateBefore;
    private Button btnShowData;
    private DatabaseWrapper databaseWrapper;

     @SuppressLint({"ResourceType", "UseCompatLoadingForDrawables"})
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dynamic_fragment, container, false);
        //графика
        dynamicChart = view.findViewById(R.id.dynamic_time_chart);
        //легенда графика
        legendGreen = view.findViewById(R.id.ic_legend_green);
        legendYellow = view.findViewById(R.id.ic_legend_yellow);
        legendRed = view.findViewById(R.id.ic_legend_red);
        //инициализация текста
        tvLegendGreen = view.findViewById(R.id.tv_legend_green);
        tvLegendYellow = view.findViewById(R.id.tv_legend_yellow);
        tvLegendRed = view.findViewById(R.id.tv_legend_red);
        //инициализация ккнопок
        btnSetDateFrom = view.findViewById(R.id.btn_dyn_date_from);
        btnSetDateBefore = view.findViewById(R.id.btn_dyn_date_before);
        btnShowData = view.findViewById(R.id.btn_dyn_show_data);

        calendar = Calendar.getInstance();
        setYear = calendar.get(Calendar.YEAR);
        setMonth = calendar.get(Calendar.MONTH);
        setDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        dateBefore = setYear + "-" + setMonth + "-" + setDayOfMonth;

        chartGraf = new ChartGraf(dynamicChart);
        databaseWrapper = DemoApp.dbWrapper();
        dataOnCardView = new DataOnCardView();

        initData(dataBD, "2020-01-01", "2020-12-31"); //инициализация данных за все время

        //отрисовать график за все время
        chartGraf.drawChart(getContext(), getColorsChart(), count, sla75Expired, slaExpired);
        setLegendChart(count, sla75Expired, slaExpired);

        btnSetDateFrom.setOnClickListener(v ->
                new DatePickerDialog(getContext(),
                        dateSetListenerFrom, setYear, setMonth, setDayOfMonth).show());

        btnSetDateBefore.setOnClickListener(v ->
                new DatePickerDialog(getContext(),
                        dateSetListenerBefore, setYear, setMonth, setDayOfMonth).show());

        btnShowData.setOnClickListener(v -> {
            initData(dataBD, dateFrom, dateBefore);
            chartGraf.drawChart(getContext(), getColorsChart(), count, sla75Expired, slaExpired);
            setLegendChart(count, sla75Expired, slaExpired);
        });


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    //получаем цвета графика
    private ArrayList<Integer> getColorsChart() {
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.GREEN);
        colors.add(Color.YELLOW);
        colors.add(Color.RED);
        return colors;
    }

    //изменяем описание графика
    @SuppressLint("SetTextI18n")
    private void setLegendChart(int legendValueGreen, int legendValueYellow, int legendValueRed) {
        legendGreen.setColorFilter(Color.GREEN);
        legendYellow.setColorFilter(Color.YELLOW);
        legendRed.setColorFilter(Color.RED);
        tvLegendGreen.setText("Без нарушения: " + legendValueGreen);
        tvLegendYellow.setText("75% от SLA истекло: " + legendValueYellow);
        tvLegendRed.setText("100% от SLA истекло: " + legendValueRed);
    }

    //запросс данных из бд и инициализация данных графика
    private void initData(int dataBD, String dateFrom, String dateBefore) {
        if (dataBD == 1) {
            count = databaseWrapper.getCount("OpenSet", dateFrom, dateBefore);
            sla75Expired = databaseWrapper.getSla75Expired("OpenSet", dateFrom, dateBefore);
            slaExpired = databaseWrapper.getSlaExpired("OpenSet", dateFrom, dateBefore);
            dataOnCardView.setValueGood(count);
            dataOnCardView.setSla75ExpiredCount(sla75Expired);
            dataOnCardView.setSlaExpiredCount(slaExpired);
        } else if (dataBD == 2) {
            count = databaseWrapper.getCount(DBContact.ProcessedSet.TABLE_NAME, dateFrom, dateBefore);
            sla75Expired = databaseWrapper.getSla75Expired(DBContact.ProcessedSet.TABLE_NAME, dateFrom, dateBefore);
            slaExpired = databaseWrapper.getSlaNotExpired(dateFrom, dateBefore);
        } else if (dataBD == 3) {
            count = databaseWrapper.getCount(DBContact.ReceiptSet.TABLE_NAME, dateFrom, dateBefore);
            sla75Expired = databaseWrapper.getSla75Expired(DBContact.ReceiptSet.TABLE_NAME, dateFrom, dateBefore);
            slaExpired = databaseWrapper.getSlaExpired(DBContact.ReceiptSet.TABLE_NAME, dateFrom, dateBefore);
        }
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

    public static DynamicFragment newInstance(int data) {
         dataBD = data;
         return new DynamicFragment();
    }
}
