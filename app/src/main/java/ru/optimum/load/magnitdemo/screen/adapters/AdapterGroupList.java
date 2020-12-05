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
import ru.optimum.load.magnitdemo.data.TestData;
import ru.optimum.load.magnitdemo.data.TestGroupData;

/*
    Адаптер для отображени детальной информации "по группам"
 */
public class AdapterGroupList extends RecyclerView.Adapter<AdapterGroupList.AdapterGroupHolder> {

    private final LayoutInflater inflater;
    private List<TestGroupData> testData;

    public AdapterGroupList(Context context, List<TestGroupData> data) {
        this.inflater = LayoutInflater.from(context);
        testData = data;
    }

    @NonNull
    @Override
    public AdapterGroupHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_table_group_rv, parent, false);
        return new AdapterGroupHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterGroupHolder holder, int position) {
        TestGroupData data = testData.get(position);
        holder.onBindTv(data);
    }

    @Override
    public int getItemCount() {
        return testData.size();
    }

    public void setTestGroupData(List<TestGroupData> testGroupData) {
        this.testData = testGroupData;
    }

    static class AdapterGroupHolder extends RecyclerView.ViewHolder {

        TextView titleTv;
        TextView timeTv;
        TextView processedTv;
        CardView cardView;

        public AdapterGroupHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_view_group_data);
            titleTv = itemView.findViewById(R.id.tv_name_group_table);
            timeTv = itemView.findViewById(R.id.tv_time_num);
            processedTv = itemView.findViewById(R.id.tv_processed_group_num);
        }

        private void onBindTv(TestGroupData data) {
            if (!data.getTitle().equals("")) {
                cardView.setVisibility(View.VISIBLE);
                titleTv.setText(data.getTitle());
                timeTv.setText(String.valueOf(data.getTime()));
                processedTv.setText(String.valueOf(data.getProcessed()));
            } else {
                cardView.setVisibility(View.INVISIBLE);
            }
        }
    }
}
