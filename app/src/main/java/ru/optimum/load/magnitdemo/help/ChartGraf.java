package ru.optimum.load.magnitdemo.help;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

import ru.optimum.load.magnitdemo.R;
/*
    Класс бля отрисоовки графиков
 */
public class ChartGraf {

    private BarChart dynamicChart;

    public ChartGraf(BarChart view) {
        this.dynamicChart = view;
    }

    //отрисовка графика с 3 параметрами
    public void drawChart(Context context, ArrayList<Integer> colors, int valueCount, int valueSLA75, int valueSLA) {
        dynamicChart.getDescription().setEnabled(false);
        dynamicChart.getAxisRight().setDrawLabels(true);
        dynamicChart.setDrawBarShadow(true);
        dynamicChart.setTouchEnabled(false);
        dynamicChart.setFitBars(true);
        dynamicChart.animateY(1000);
        dynamicChart.getLegend().setEnabled(false);

        XAxis xAxis = dynamicChart.getXAxis();
        xAxis.setDrawLabels(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(false);
        setDateChart(context, colors, valueCount, valueSLA75, valueSLA);
    }

    //изменяем значения подбераем цвета графика
    private void setDateChart(Context context, ArrayList<Integer> colors, int valueOne, int valueTwo, int valueTree) {
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, valueOne));
        entries.add(new BarEntry(1, valueTwo));
        entries.add(new BarEntry(2, valueTree));

        BarDataSet dataSet = new BarDataSet(entries, "");
        dataSet.setColors(colors);

        dataSet.setBarShadowColor(context.getResources().getColor(R.color.shadow));
        dataSet.setDrawValues(true);
        dataSet.setValueTextSize(16);
        dataSet.setValueTypeface(Typeface.DEFAULT_BOLD);
        dataSet.setValueTextColor(Color.BLACK);
        BarData data = new BarData(dataSet);

        dynamicChart.setData(data);
    }

    //отрисовка графика с 6 параметрами
    public void drawChartTimeProcess(Context context, int processTime, int avgProcessTime, int workTime, int avgWorkTime, int waitTime, int avgWaitTime) {
        dynamicChart.getDescription().setEnabled(false);
        dynamicChart.getAxisRight().setDrawLabels(false);
        dynamicChart.setDrawBarShadow(true);
        dynamicChart.setDrawValueAboveBar(true);
        dynamicChart.setTouchEnabled(false);
        dynamicChart.animateY(1000);
        dynamicChart.setExtraOffsets(0,0, 0,100);
        dynamicChart.getLegend().setEnabled(false);

        XAxis xAxis = dynamicChart.getXAxis();
        xAxis.setDrawLabels(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawLabels(true);
        setDateChartTimeProcess(context, processTime, avgProcessTime, workTime, avgWorkTime, waitTime, avgWaitTime);
    }
    //изменение данных графика
    private void setDateChartTimeProcess(Context context, int processTime, int avgProcessTime, int workTime, int avgWorkTime, int waitTime, int avgWaitTime) {
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(1, processTime));
        entries.add(new BarEntry(2, avgProcessTime));
        entries.add(new BarEntry(3, workTime));
        entries.add(new BarEntry(4, avgWorkTime));
        entries.add(new BarEntry(5, waitTime));
        entries.add(new BarEntry(6, avgWaitTime));

        BarDataSet dataSet = new BarDataSet(entries, "");
        dataSet.setColors(context.getResources().getColor(R.color.greenChart));
        dataSet.setDrawValues(true);
        dataSet.setBarShadowColor(context.getResources().getColor(R.color.grey));
        dataSet.setValueTextSize(16);
        dataSet.setValueTypeface(Typeface.DEFAULT_BOLD);
        dataSet.setValueTextColor(Color.BLACK);
        BarData data = new BarData(dataSet);

        dynamicChart.setData(data);
    }
}
