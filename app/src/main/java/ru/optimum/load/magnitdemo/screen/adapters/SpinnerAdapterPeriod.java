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

import java.util.ArrayList;

import ru.optimum.load.magnitdemo.R;
import ru.optimum.load.magnitdemo.data.DataOnCardView;
import ru.optimum.load.magnitdemo.data.TestData;
/*
    Адаптер для кастомного спинера
 */
public class SpinnerAdapterPeriod extends ArrayAdapter<String> {

    private final Context context;
    String[] periods;
    
    public SpinnerAdapterPeriod(@NonNull Context context, int resource, @NonNull String[] objects) {
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
        View view = inflater.inflate(R.layout.row_spinner, parent, false);
        TextView label = view.findViewById(R.id.tv_interval);
        label.setText(periods[position]);

        ImageView icon = view.findViewById(R.id.ic_calendar);
        icon.setImageResource(R.drawable.ic_calendar);

        return view;
    }
}
