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

public class SpinnerAdapterReport extends ArrayAdapter<String> {

    private final Context context;
    List<String> reports;

    public SpinnerAdapterReport(@NonNull Context context, int resource, @NonNull List<String> objects) {
        super(context, resource, objects);
        this.context = context;
        this.reports = objects;
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
        View view = inflater.inflate(R.layout.row_spinner_report, parent, false);
        TextView report = view.findViewById(R.id.tv_get_report);
        ImageView arrowDown = view.findViewById(R.id.iv_arrow_down);
        if (position > 0) {
            arrowDown.setVisibility(View.GONE);
        }
        report.setText(reports.get(position));
        return view;
    }
}
