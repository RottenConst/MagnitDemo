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
import ru.optimum.load.magnitdemo.data.ChartData;

/*
    Адаптер для отображения общих данных на главном экране
 */
public class GridAdapterCard extends RecyclerView.Adapter<GridAdapterCard.GridHolderCard> {

    private final LayoutInflater inflater;
    private final Context context;
    private List<ChartData> data;

    public GridAdapterCard(Context context, List<ChartData> data) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @NonNull
    @Override
    public GridHolderCard onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_monitoring_cv, parent, false);
        return new GridHolderCard(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GridHolderCard holder, int position) {
        holder.onBindViewCard(context, position, data.get(position));
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public void setData(List<ChartData> data) {
        this.data.clear();
        this.data = data;
    }

    static class GridHolderCard extends RecyclerView.ViewHolder {

        CardView cardViewOfData;
        TextView nameOfData;
        TextView count;

        public GridHolderCard(@NonNull View itemView) {
            super(itemView);
            cardViewOfData = itemView.findViewById(R.id.cv_of_data);
            nameOfData = itemView.findViewById(R.id.tv_name_data);
            count = itemView.findViewById(R.id.tv_count_of_data);
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        private void onBindViewCard(Context context, int position, ChartData data) {
            nameOfData.setTextColor(context.getResources().getColor(R.color.white));
            count.setTextColor(context.getResources().getColor(R.color.white));
            switch (position) {
                case 0:
                    cardViewOfData.setCardBackgroundColor(context.getResources().getColor(R.color.cardViewOpen));
                    nameOfData.setText(data.getTitle());
                    count.setText(String.valueOf(data.getCount()));
                    break;
                case 1:
                    cardViewOfData.setCardBackgroundColor(context.getResources().getColor(R.color.cardViewReceived));
                    nameOfData.setText(data.getTitle());
                    count.setText(String.valueOf(data.getCount()));
                    break;
                case 2:
                    cardViewOfData.setCardBackgroundColor(context.getResources().getColor(R.color.cardViewInWork));
                    nameOfData.setText(data.getTitle());
                    count.setText(String.valueOf(data.getCount()));
                    break;
                case 3:
                    cardViewOfData.setCardBackgroundColor(context.getResources().getColor(R.color.cardViewComplete));
                    nameOfData.setText(data.getTitle());
                    count.setText(String.valueOf(data.getCount()));
                    break;
            }

        }
    }
}
