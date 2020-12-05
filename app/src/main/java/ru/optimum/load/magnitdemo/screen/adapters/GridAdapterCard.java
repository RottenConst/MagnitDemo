package ru.optimum.load.magnitdemo.screen.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;

import ru.optimum.load.magnitdemo.R;
import ru.optimum.load.magnitdemo.data.DataOnCardView;
import ru.optimum.load.magnitdemo.screen.main.details.DetailsActivity;
/*
    Адаптер для отображения общих данных на главном экране
 */
public class GridAdapterCard extends RecyclerView.Adapter<GridAdapterCard.GridHolderCard> {

    private final LayoutInflater inflater;
    private Context context;
    private List<DataOnCardView> data;

    public GridAdapterCard(Context context, List<DataOnCardView> data){
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @NonNull
    @Override
    public GridHolderCard onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.monitoring_item_graf, parent, false);
        return new GridHolderCard(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GridHolderCard holder, int position) {
        holder.onBindViewCard(context, position, data.get(position));
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public void setData(List<DataOnCardView> data) {
        this.data = data;
    }

    static class GridHolderCard extends RecyclerView.ViewHolder {

        private final PieChart chart;
        private final TextView headline;
        private final TextView valueNumTV;
        private final ImageView iconArrow;
        private final Button detailsBtn;

        public GridHolderCard(@NonNull View itemView) {
            super(itemView);
            chart = itemView.findViewById(R.id.graf_monitoring);
            headline = itemView.findViewById(R.id.headline_tv);
            valueNumTV = itemView.findViewById(R.id.value_num_tv);
            iconArrow = itemView.findViewById(R.id.icon_arrow);
            detailsBtn = itemView.findViewById(R.id.more_details_graf_btn);
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        private void onBindViewCard(Context context, int position, DataOnCardView data) {
            switch (position) {
                case 0:
                    headline.setText("Открыто");
                    valueNumTV.setText(String.valueOf(data.getGeneralCountValue()));
                    iconArrow.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_arrow_green_up));
                    initChart(data);
                    detailsBtn.setOnClickListener(v -> {
                        Intent intent = new Intent(context, DetailsActivity.class);
                        intent.putExtra("data", 1); //парамет для определения откуда и какие данные отображать
                        context.startActivity(intent);
                    });
                    break;
                case 1:
                    headline.setText("Поступило");
                    valueNumTV.setText(String.valueOf(data.getGeneralCountValue()));
                    iconArrow.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_arrow_red_down));
                    initChart(data);
                    detailsBtn.setOnClickListener(v -> {
                        Intent intent = new Intent(context, DetailsActivity.class);
                        intent.putExtra("data", 2);
                        context.startActivity(intent);
                    });
                    break;
                case 2:
                    headline.setText("Обработано");
                    valueNumTV.setText(String.valueOf(data.getGeneralCountValue()));
                    iconArrow.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_arrow_green_up));
                    initChart(data);
                    detailsBtn.setOnClickListener(v -> {
                        Intent intent = new Intent(context, DetailsActivity.class);
                        intent.putExtra("data", 3);
                        context.startActivity(intent);
                    });
                    break;
//                case 3:
//                    headline.setText("В работе");
//                    valueNumTV.setText(String.valueOf(data.getGeneralCountValue()));
//                    iconArrow.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_arrow_red_down));
//                    detailsBtn.setOnClickListener(v -> {
//                        Intent intent = new Intent(context, DetailsActivity.class);
//                        intent.putExtra("graf", true);
//                        context.startActivity(intent);
//                    });
//                    break;
            }
        }

        //рисуем график
        private void initChart(DataOnCardView data) {
            chart.getDescription().setEnabled(false); //выключить описание
            chart.setDrawEntryLabels(false); //выключить название данных на графике
            chart.setExtraOffsets(15, 0,20, 50); //размер графика
            chart.setHoleRadius(70F); //толщина графика
            chart.setDrawRoundedSlices(true); //закругления
            chart.setTouchEnabled(false); // нажатие на график
            chart.setTransparentCircleRadius(100F); //оттенок графика
            chart.setRotationEnabled(false); // выключить поворот графика нажатием
            chart.animateY(1400, Easing.EaseInBack); //анимация

            //отображение описания графика
            Legend legend = chart.getLegend();
            legend.setYOffset(-40);
            legend.setYEntrySpace(6F);
            legend.setOrientation(Legend.LegendOrientation.VERTICAL);
            setChartData(data.getValueGood(), data.getSla75ExpiredCount(), data.getSlaExpiredCount());

        }

        //Добавление в график данных
        private void setChartData(int value1, int value2, int value3) {
            ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
            // добавление заначений и описания
            entries.add(new PieEntry(value1, "Без нарушения: " + value1));
            entries.add(new PieEntry(value2, "75% от SLA: " + value2));
            entries.add(new PieEntry(value3, "100% от SLA: " + value3));

            PieDataSet dataSet = new PieDataSet(entries, "");
            dataSet.setColors(getColorsChart()); //раскрасить график
            dataSet.setFormSize(14); // размер описания цветов графика
            PieData data = new PieData(dataSet);
            data.setDrawValues(false); //убрать значения с графика
            chart.setData(data); // добавлям данные
            chart.invalidate();
        }

        //установить цвета графика
        private ArrayList<Integer> getColorsChart() {
            ArrayList<Integer> colors = new ArrayList<>();
            colors.add(Color.GREEN);
            colors.add(Color.YELLOW);
            colors.add(Color.RED);
            return colors;
        }
    }
}
