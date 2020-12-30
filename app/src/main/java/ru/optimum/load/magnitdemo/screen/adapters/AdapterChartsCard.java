package ru.optimum.load.magnitdemo.screen.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import ru.optimum.load.magnitdemo.R;
import ru.optimum.load.magnitdemo.data.ChartData;

public class AdapterChartsCard extends RecyclerView.Adapter<AdapterChartsCard.ChartsHolder> {

    Context context;
    LayoutInflater inflater;
    List<ChartData> chartData;
    public boolean enableChartLayout;

    public AdapterChartsCard(Context context, List<ChartData> chartData, boolean enableChartLayout) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.chartData = chartData;
        this.enableChartLayout = enableChartLayout;
    }

    @NonNull
    @Override
    public ChartsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layout;
        View view;
        if (enableChartLayout) {
            layout = R.layout.item_charts_card;
            view = inflater.inflate(layout, parent, false);
        } else {
            layout = R.layout.item_monitoring_cv;
            view = inflater.inflate(layout, parent, false);
        }
        return new ChartsHolder(view, enableChartLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull ChartsHolder holder, int position) {
        if (enableChartLayout) {
            holder.onBindCharts(context, chartData.get(position), position);
        } else {
            holder.onBindViewCard(context, position, chartData.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return chartData.size();
    }

    public void setData(List<ChartData> chartData) {
        this.chartData.clear();
        this.chartData = chartData;
    }

    static class ChartsHolder extends RecyclerView.ViewHolder implements OnChartValueSelectedListener {

        CardView cardView;
        LineChart chart;
        TextView titleCard;
        TextView tvCountCard;
        ImageView ivArrow;
        TextView tvDateOfCount;

        public ChartsHolder(@NonNull View itemView, boolean enableChartCard) {
            super(itemView);
            if (enableChartCard) {
                cardView = itemView.findViewById(R.id.card_of_chart);
                chart = itemView.findViewById(R.id.chart_monitoring);
                titleCard = itemView.findViewById(R.id.tv_title_card);
                tvCountCard = itemView.findViewById(R.id.tv_count_card);
                ivArrow = itemView.findViewById(R.id.iv_arrow);
                tvDateOfCount = itemView.findViewById(R.id.tv_date_of_number);
            } else {
                cardView = itemView.findViewById(R.id.cv_of_data);
                titleCard = itemView.findViewById(R.id.tv_name_data);
                tvCountCard = itemView.findViewById(R.id.tv_count_of_data);
                ivArrow = itemView.findViewById(R.id.image_arrow);
            }
        }

        private void onBindViewCard(Context context, int position, ChartData data) {
            titleCard.setTextColor(context.getResources().getColor(R.color.white));
            tvCountCard.setTextColor(context.getResources().getColor(R.color.white));
            int lastIndex = data.getCounts().size() - 1;
            switch (position) {
                case 0:
                    cardView.setCardBackgroundColor(context.getResources().getColor(R.color.cardViewOpen));
                    titleCard.setText(data.getTitle());
                    tvCountCard.setText(String.valueOf(data.getCount()));
                    if (data.getCounts().get(0).second > data.getCounts().get(lastIndex).second) {
                        ivArrow.setImageResource(R.drawable.ic_arrow_red_down);
                    } else if (data.getCounts().get(0).equals(data.getCounts().get(lastIndex))) {
                        ivArrow.setImageResource(R.color.white);
                    } else {
                        ivArrow.setImageResource(R.drawable.ic_arrow_green_up);
                    }
                    break;
                case 1:
                    cardView.setCardBackgroundColor(context.getResources().getColor(R.color.cardViewReceived));
                    titleCard.setText(data.getTitle());
                    tvCountCard.setText(String.valueOf(data.getCount()));

                    if (data.getCounts().get(0).second > data.getCounts().get(lastIndex).second) {
                        ivArrow.setImageResource(R.drawable.ic_arrow_red_down);
                    } else if (data.getCounts().get(0).equals(data.getCounts().get(lastIndex))) {
                        ivArrow.setImageResource(R.color.white);
                    }
                    break;
                case 2:
                    cardView.setCardBackgroundColor(context.getResources().getColor(R.color.cardViewInWork));
                    titleCard.setText(data.getTitle());
                    tvCountCard.setText(String.valueOf(data.getCount()));
                    if (data.getCounts().get(0).second > data.getCounts().get(lastIndex).second) {
                        ivArrow.setImageResource(R.drawable.ic_arrow_red_down);
                    } else if (data.getCounts().get(0).equals(data.getCounts().get(lastIndex))) {
                        ivArrow.setImageResource(R.color.white);
                    } else {
                        ivArrow.setImageResource(R.drawable.ic_arrow_green_up);
                    }
                    break;
                case 3:
                    cardView.setCardBackgroundColor(context.getResources().getColor(R.color.cardViewComplete));
                    titleCard.setText(data.getTitle());
                    tvCountCard.setText(String.valueOf(data.getCount()));

                    if (data.getCounts().get(0).second > data.getCounts().get(lastIndex).second) {
                        ivArrow.setImageResource(R.drawable.ic_arrow_red_down);
                    } else if (data.getCounts().get(0).equals(data.getCounts().get(lastIndex))) {
                        ivArrow.setImageResource(R.color.white);
                    } else {
                        ivArrow.setImageResource(R.drawable.ic_arrow_green_up);
                    }
                    break;
            }

        }

        private void onBindCharts(Context context, ChartData chartData, int position) {
            setChartStyle(context, chartData);
            titleCard.setText(chartData.getTitle());
            tvCountCard.setText(String.valueOf(chartData.getCount()));
            int lastIndex = chartData.getCounts().size() - 1;
            if (chartData.getCounts().get(0).second > chartData.getCounts().get(lastIndex).second) {
                ivArrow.setImageResource(R.drawable.ic_arrow_red_down);
            } else if (chartData.getCounts().get(0).equals(chartData.getCounts().get(lastIndex))) {
                ivArrow.setImageResource(R.color.white);
            } else {
                ivArrow.setImageResource(R.drawable.ic_arrow_green_up);
            }
            switch (position) {
                case 0:
                    cardView.setCardBackgroundColor(context.getResources().getColor(R.color.cardViewOpen));
                    break;
                case 1:
                    cardView.setCardBackgroundColor(context.getResources().getColor(R.color.cardViewReceived));
                    break;
                case 2:
                    cardView.setCardBackgroundColor(context.getResources().getColor(R.color.cardViewInWork));
                    break;
                case 3:
                    cardView.setCardBackgroundColor(context.getResources().getColor(R.color.cardViewComplete));
                    break;
            }
        }

        private void setChartStyle(Context context, ChartData chartData) {

            chart.getAxisRight().setEnabled(false);
            chart.getAxisLeft().setEnabled(true);
            chart.getDescription().setEnabled(false);
            chart.getLegend().setEnabled(false);
            chart.setBackgroundColor(Color.WHITE);
            chart.setOnChartValueSelectedListener(this);
            chart.setDrawGridBackground(false);

            MarkerView markerView = new ru.optimum.load.magnitdemo.help.MarkerView(context, R.layout.custom_marker_view);
            markerView.setChartView(chart);
            chart.setMarker(markerView);

            chart.setDragEnabled(true);
            chart.setPinchZoom(true);
            chart.animateX(2000);

            XAxis xAxis = chart.getXAxis();
            xAxis.setEnabled(false);

            YAxis yAxis = chart.getAxisLeft();
            yAxis.setLabelCount(4, true);
            yAxis.setDrawLabels(false);
            yAxis.setDrawAxisLine(false);

            setDataChart(chartData);
        }

        private void setDataChart(ChartData chartData) {
            ArrayList<Entry> values = new ArrayList<>();
            List<Pair<String, Integer>> counts = chartData.getCounts();
            for (int i = 0; i < chartData.getCounts().size(); i++) {
                String[] date = counts.get(i).first.split("-");
                int year = Integer.parseInt(date[0]);
                int month = Integer.parseInt(date[1]);
                int day = Integer.parseInt(date[2]);
                Calendar mDate = new GregorianCalendar(year, month - 1, day);
                values.add(new Entry(i, counts.get(i).second, mDate.getTime()));
            }

            LineDataSet lineData;
            if (chart.getData() != null && chart.getData().getDataSetCount() > 0) {
                lineData = (LineDataSet) chart.getData().getDataSetByIndex(0);
                lineData.setValues(values);
                lineData.notifyDataSetChanged();
                chart.getData().notifyDataChanged();
            } else  {
                lineData = new LineDataSet(values, "");
                lineData.setColor(Color.RED);
                lineData.setCircleColor(Color.RED);
                lineData.setLineWidth(2f);
                lineData.setCircleRadius(3f);
                lineData.setFormLineWidth(4f);
                lineData.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
                lineData.setDrawValues(false);
            }

            LineData data = new LineData(lineData);
            chart.setData(data);
        }

        @Override
        public void onValueSelected(Entry e, Highlight h) {
            DateFormat df = new SimpleDateFormat("dd MMMM yyyy");
            tvDateOfCount.setText(df.format(e.getData()) + " года");
            Log.i("Entry selected", e.toString());
            Log.i("LOW HIGH", "low: " + chart.getLowestVisibleX() + ", high: " + chart.getHighestVisibleX());
            Log.i("MIN MAX", "xMin: " + chart.getXChartMin() + ", xMax: " + chart.getXChartMax() + ", yMin: " + chart.getYChartMin() + ", yMax: " + chart.getYChartMax());
        }

        @Override
        public void onNothingSelected() {
            tvDateOfCount.setText("Ничего не выбрано");
        }
    }
}
