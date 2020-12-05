 package ru.optimum.load.magnitdemo.screen.main.details.fragments.processd;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;

import java.util.ArrayList;

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
    private DataOnCardView dataOnCardView;
    ChartGraf chartGraf;
    private BarChart dynamicChart;
    private ImageView legendGreen;
    private ImageView legendYellow;
    private ImageView legendRed;
    private TextView tvLegendGreen;
    private TextView tvLegendYellow;
    private TextView tvLegendRed;
    private Spinner intervalSpinner;
    private DatabaseWrapper databaseWrapper;

     @SuppressLint({"ResourceType", "UseCompatLoadingForDrawables"})
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dynamic_fragment, container, false);
        dynamicChart = view.findViewById(R.id.dynamic_time_chart);
        intervalSpinner = view.findViewById(R.id.period_of_time);
        legendGreen = view.findViewById(R.id.ic_legend_green);
        legendYellow = view.findViewById(R.id.ic_legend_yellow);
        legendRed = view.findViewById(R.id.ic_legend_red);
        tvLegendGreen = view.findViewById(R.id.tv_legend_green);
        tvLegendYellow = view.findViewById(R.id.tv_legend_yellow);
        tvLegendRed = view.findViewById(R.id.tv_legend_red);

        chartGraf = new ChartGraf(dynamicChart);
        databaseWrapper = DemoApp.dbWrapper();
        dataOnCardView = new DataOnCardView();
        initData(dataBD, ""); //инициализация данных за все время

        //отрисовать график за все время
        chartGraf.drawChart(getContext(), getColorsChart(), count, sla75Expired, slaExpired);
        setLegendChart(count, sla75Expired, slaExpired);
        initSpinner();

        //выбор период за какой период отображать данные
        intervalSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        initData(dataBD, ""); //запросить и проинициализироть данные в соответствии с датой
                        chartGraf.drawChart(getContext(), getColorsChart(), count, sla75Expired, slaExpired); //перирисовать график
                        setLegendChart(count, sla75Expired, slaExpired); //отобразить описание графика
                        break;
                    case 1:
                        initData(dataBD, "2020-10-07");
                        chartGraf.drawChart(getContext(), getColorsChart(), count, sla75Expired, slaExpired);
                        setLegendChart(count, sla75Expired, slaExpired);
                        break;
                    case 2:
                        initData(dataBD, "2020-10-01");
                        chartGraf.drawChart(getContext(), getColorsChart(), count, sla75Expired, slaExpired);
                        setLegendChart(count, sla75Expired, slaExpired);
                        break;
                    case 3:
                        initData(dataBD, "2020-08-01");
                        chartGraf.drawChart(getContext(), getColorsChart(), count, sla75Expired, slaExpired);
                        setLegendChart(count, sla75Expired, slaExpired);
                        break;
                    case 4:
                        initData(dataBD, "2020-06-01");
                        chartGraf.drawChart(getContext(), getColorsChart(), count, sla75Expired, slaExpired);
                        setLegendChart(count, sla75Expired, slaExpired);
                        break;
                    case 5:
                        initData(dataBD, "2020-01-01");
                        chartGraf.drawChart(getContext(), getColorsChart(), count, sla75Expired, slaExpired);
                        setLegendChart(count, sla75Expired, slaExpired);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    //инициализировать спиннер
    private void initSpinner() {
        String[] period = {"За все время","За последние 7 дней", "За месяц", "За 3 месяца", "За 6 месяцев", "За последний год"};
        SpinnerAdapterPeriod adapter = new SpinnerAdapterPeriod(getContext(), R.layout.row_spinner, period);
        intervalSpinner.setAdapter(adapter);
        adapter.notifyDataSetChanged();
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
    private void initData(int dataBD, String date) {
        if (dataBD == 1) {
            count = databaseWrapper.getCount("OpenSet", date);
            sla75Expired = databaseWrapper.getSla75Expired("OpenSet", date);
            slaExpired = databaseWrapper.getSlaExpired("OpenSet", date);
            dataOnCardView.setValueGood(count);
            dataOnCardView.setSla75ExpiredCount(sla75Expired);
            dataOnCardView.setSlaExpiredCount(slaExpired);
        } else if (dataBD == 2) {
            count = databaseWrapper.getCount(DBContact.ProcessedSet.TABLE_NAME, date);
            sla75Expired = databaseWrapper.getSla75Expired(DBContact.ProcessedSet.TABLE_NAME, date);
            slaExpired = databaseWrapper.getSlaNotExpired(date);
        } else if (dataBD == 3) {
            count = databaseWrapper.getCount(DBContact.ReceiptSet.TABLE_NAME, date);
            sla75Expired = databaseWrapper.getSla75Expired(DBContact.ReceiptSet.TABLE_NAME, date);
            slaExpired = databaseWrapper.getSlaExpired(DBContact.ReceiptSet.TABLE_NAME, date);
        }
    }

    public static DynamicFragment newInstance(int data) {
         dataBD = data;
         return new DynamicFragment();
    }
}
