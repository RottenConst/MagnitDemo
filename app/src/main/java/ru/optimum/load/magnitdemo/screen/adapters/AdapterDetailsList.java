package ru.optimum.load.magnitdemo.screen.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.optimum.load.magnitdemo.R;
import ru.optimum.load.magnitdemo.data.TestData;
/*
    Адаптер RV для отображения детальной информации
 */
public class AdapterDetailsList extends RecyclerView.Adapter<AdapterDetailsList.AdapterDetailsHolder> {

    private final LayoutInflater inflater;
    private List<TestData> testData;

    public AdapterDetailsList(Context context, List<TestData> data) {
        this.inflater = LayoutInflater.from(context);
        testData = data;
    }

    @NonNull
    @Override
    public AdapterDetailsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_table_rv, parent, false);
        return new AdapterDetailsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterDetailsHolder holder, int position) {
        TestData data = testData.get(position);
        holder.bindTableDetails(data);
    }

    @Override
    public int getItemCount() {
        return testData.size();
    }

    public void setTestData(List<TestData> testData) {
        this.testData = testData;
    }

    static class AdapterDetailsHolder extends RecyclerView.ViewHolder {

        private TextView nameTV;
        private TextView inProcessedTV;
        private TextView violationTV;
        private TextView slaTV;
        private CardView cardView;

        public AdapterDetailsHolder(@NonNull View itemView) {
            super(itemView);
            cardView =itemView.findViewById(R.id.table_card);
            nameTV = itemView.findViewById(R.id.tv_name_time_table);
            inProcessedTV = itemView.findViewById(R.id.tv_processed_num);
            violationTV = itemView.findViewById(R.id.tv_violation_num);
            slaTV = itemView.findViewById(R.id.tv_sla_num);
        }

        @SuppressLint("SetTextI18n")
        public void bindTableDetails(TestData testData) {
            if (testData.getTitle() != null) {
                if (!testData.getTitle().equals("") || testData != null) {
                    cardView.setVisibility(View.VISIBLE);
                    nameTV.setText(testData.getTitle());
                    inProcessedTV.setText(String.valueOf(testData.getProcessed()));
                    violationTV.setText(String.valueOf(testData.getViolation()));
                    slaTV.setText(String.format("%.2f", testData.getSla()));
                }
            } else {
                cardView.setVisibility(View.INVISIBLE);
            }
        }
    }
}
