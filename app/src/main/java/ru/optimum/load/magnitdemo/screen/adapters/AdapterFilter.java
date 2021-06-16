package ru.optimum.load.magnitdemo.screen.adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ru.optimum.load.magnitdemo.R;
import ru.optimum.load.magnitdemo.data.Filter;

public class AdapterFilter extends RecyclerView.Adapter<AdapterFilter.FilterHolder> {

    List<Filter> filters;
    LayoutInflater inflater;
    Context context;
    static List<String[]> filtersArgs;

    public AdapterFilter(Context context, List<Filter> filters) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.filters = filters;
        filtersArgs = new ArrayList<>();
        filtersArgs.add(new String[0]);
        filtersArgs.add(new String[0]);
        filtersArgs.add(new String[0]);
        filtersArgs.add(new String[0]);
        filtersArgs.add(new String[0]);
        filtersArgs.add(new String[0]);
    }

    @NonNull
    @Override
    public FilterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_filter, parent, false);

        return new FilterHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FilterHolder holder, int position) {
        holder.onBindFilters(context, filters.get(position));
    }

    @Override
    public int getItemCount() {
        return filters.size();
    }

    public List<String[]> getFilterArg() {
        return filtersArgs;
    }

    public void setFilters(List<String[]> filtersArgs) {
        AdapterFilter.filtersArgs.clear();
        AdapterFilter.filtersArgs = filtersArgs;
    }

    static class FilterHolder extends RecyclerView.ViewHolder {

        TextView filter;
        TextView count_filters;


        public FilterHolder(@NonNull View itemView) {
            super(itemView);
            filter = itemView.findViewById(R.id.tv_title_filter);
            count_filters = itemView.findViewById(R.id.tv_count_filters);

        }

        private void onBindFilters(Context context, Filter filters) {
            String category = filters.getCategory();
            String[] titles = filters.getTitle();
            boolean[] isCheck = filters.getCheck();
            filter.setText(category);


            count_filters.setText("Все");
            count_filters.setOnClickListener(v -> {
                getAlertDialog(v.getContext(), category, titles, isCheck);
            });
        }

        @SuppressLint("NewApi")
        private void getAlertDialog(Context context, String category, String[] titles, boolean[] isCheck) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);


            builder.setTitle("Выбирите фильтры")
                    .setMultiChoiceItems(titles, isCheck, (dialog, which, isChecked) -> isCheck[which] = isChecked)
                    .setPositiveButton("Ок", (dialog, which) -> {

                        StringBuilder filtres = new StringBuilder();
                        String[] filterArg = new String[0];
                        switch (category) {
                            case "Организация":
                                filtres = new StringBuilder("COMPANY");
                                break;
                            case "Филиал":
                                filtres = new StringBuilder("AFFILIATE");
                                break;
                            case "Тип комплекта":
                                filtres = new StringBuilder("TYPEOFSET");
                                break;
                            case "Группа Обработки":
                                filtres = new StringBuilder("UNITNAME");
                                break;
                            case "Округ":
                                filtres = new StringBuilder("DISTRICT");
                                break;
                            case "Подразделение":
                                filtres = new StringBuilder("SHOP");
                                break;
                        }

                        int i = 0;
                        for (int c = 0; c < isCheck.length; c++) {

                            if (isCheck[c]) {
                                i += 1;
                                String name = titles[c];
                                filtres.append(";").append(name);
                                filterArg = filtres.toString().split(";");
                            }
                        }


                        if (i < 1 ) {
                            count_filters.setText("Все");
                            for (int c = 0; c < isCheck.length; c++){
                                isCheck[c] = false;
                            }
                            filterArg = new String[0];
                            switch (category) {
                                case "Организация":
                                    filtersArgs.set(0, filterArg);
                                    break;
                                case "Филиал":
                                    filtersArgs.set(1, filterArg);
                                    break;
                                case "Тип комплекта":
                                    filtersArgs.set(2, filterArg);
                                    break;
                                case "Группа Обработки":
                                    filtersArgs.set(3, filterArg);
                                    break;
                                case "Округ":
                                    filtersArgs.set(4, filterArg);
                                    break;
                                case "Подразделение":
                                    filtersArgs.set(5, filterArg);
                                    break;
                            }
                        } else {
                            count_filters.setText(String.valueOf(i));
                            switch (category) {
                                case "Организация":
                                    filtersArgs.set(0, filterArg);
                                    break;
                                case "Филиал":
                                    filtersArgs.set(1, filterArg);
                                    break;
                                case "Тип комплекта":
                                    filtersArgs.set(2, filterArg);
                                    break;
                                case "Группа Обработки":
                                    filtersArgs.set(3, filterArg);
                                    break;
                                case "Округ":
                                    filtersArgs.set(4, filterArg);
                                    break;
                                case "Подразделение":
                                    filtersArgs.set(5, filterArg);
                                    break;
                            }
                        }

                        Log.d("FILTERS", filterArg.length + " size = " + filtersArgs.size());

                    })
                    .setNegativeButton("Отмена", (dialog, which) -> dialog.cancel());
            builder.create();
            builder.show();
        }
    }
}
