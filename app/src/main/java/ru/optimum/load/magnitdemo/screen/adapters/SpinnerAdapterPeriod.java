package ru.optimum.load.magnitdemo.screen.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import ru.optimum.load.magnitdemo.R;

/*
    Адаптер для кастомного спинера
 */
public class SpinnerAdapterPeriod extends ArrayAdapter<String> {

    private final Context context;
    List<String> periods;
    
    public SpinnerAdapterPeriod(@NonNull Context context, int resource, @NonNull List<String> objects) {
        super(context, resource, objects);
        this.context = context;
        this.periods = objects;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row_spinner_period, parent, false);
        TextView label = view.findViewById(R.id.tv_interval);
        label.setText(periods.get(position));

        ImageView icon = view.findViewById(R.id.ic_calendar);
        icon.setImageResource(R.drawable.ic_calendar);

        return view;
    }
}
