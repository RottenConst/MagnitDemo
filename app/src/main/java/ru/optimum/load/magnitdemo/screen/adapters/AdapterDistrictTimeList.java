package ru.optimum.load.magnitdemo.screen.adapters;

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
import ru.optimum.load.magnitdemo.data.TimeTestData;

/*
    адаптер RV для отображения детальной информации
 */

public class AdapterDistrictTimeList extends RecyclerView.Adapter<AdapterDistrictTimeList.AdapterDistrictTimeHolder> {

    private final LayoutInflater inflater;
    private List<TimeTestData> testData;

    public AdapterDistrictTimeList(Context context, List<TimeTestData> data){
        inflater = LayoutInflater.from(context);
        testData = data;
    };

    @NonNull
    @Override
    public AdapterDistrictTimeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_table_district_time_rv, parent, false);
        return new AdapterDistrictTimeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterDistrictTimeHolder holder, int position) {
        TimeTestData data = testData.get(position);
        holder.onBindTV(data);
    }


    @Override
    public int getItemCount() {
        return testData.size();
    }

    public void setTestData(List<TimeTestData> data){
        this.testData = data;
    }

    static class AdapterDistrictTimeHolder extends RecyclerView.ViewHolder {

        TextView titleTv;
        TextView numProcessedTv;
        CardView cardView;

        public AdapterDistrictTimeHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_view_data);
            titleTv = itemView.findViewById(R.id.tv_name_time_table);
            numProcessedTv = itemView.findViewById(R.id.tv_time_processed_num);
        }

        public void onBindTV(TimeTestData timeTestData) {
            if (timeTestData.getName() != null) {
                if (timeTestData.getName().equals("") || timeTestData.getTime() == 0) {
                    cardView.setVisibility(View.INVISIBLE);
                } else {
                    cardView.setVisibility(View.VISIBLE);
                    titleTv.setText(timeTestData.getName());
                    numProcessedTv.setText(String.valueOf(timeTestData.getTime()));
                }
            }
        }
    }
}
