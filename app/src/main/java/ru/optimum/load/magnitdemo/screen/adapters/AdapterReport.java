package ru.optimum.load.magnitdemo.screen.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ru.optimum.load.magnitdemo.R;
import ru.optimum.load.magnitdemo.data.ChartData;
import ru.optimum.load.magnitdemo.data.Report;

public class AdapterReport extends RecyclerView.Adapter<AdapterReport.ReportHolder> {

    public interface OnItemClickListener {
        void onItemClick(Report report);
    }

    List<Report> reports;
    LayoutInflater inflater;
    int thsReport;
    Context context;
    public OnItemClickListener listener = null;

    public AdapterReport(Context context, List<Report> reports, int thsReport, OnItemClickListener listener) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.reports = reports;
        this.thsReport = thsReport;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ReportHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_report_cv, parent, false);
        return new ReportHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportHolder holder, int position) {
        if (thsReport == 1) {
            sortReportOperator(reports);
            holder.onBindReportOperator(reports.get(position), listener);
        } else if (thsReport == 0) {
            sortReportGroup(reports);
            holder.onBindReportGroup(reports.get(position), listener);
        }
    }

    @Override
    public int getItemCount() {
        return reports.size();
    }

    private void sortReportOperator(List<Report> reports) {
        boolean sorted = false;
        Report report;
        while (!sorted) {
            sorted = true;
            for (int i = 0; i < reports.size() - 1; i++ ) {
                if (reports.get(i).getCount() < reports.get(i+1).getCount()) {
                    report = new Report(reports.get(i).getName(), reports.get(i).getSlaNotExpiredCount(), reports.get(i).getCount());
                    reports.set(i, reports.get(i + 1));
                    reports.set(i + 1, report);
                    sorted = false;
                }
            }
        }
    }

    private void sortReportGroup(List<Report> reports) {
        boolean sorted = false;
        List<Report> topReport = new ArrayList<>();
        Report report;
        while (!sorted) {
            sorted = true;
            for (int i = 0; i < reports.size() - 1; i++ ) {
                if (reports.get(i).getProcent() < reports.get(i+1).getProcent()) {
                    report = new Report(reports.get(i).getName(), reports.get(i).getSlaNotExpiredCount(), reports.get(i).getCount());
                    reports.set(i, reports.get(i + 1));
                    reports.set(i + 1, report);
                    sorted = false;
                }
            }
        }
    }

    public void setReports(List<Report> reports, int thsReport) {
        this.reports.clear();
        this.reports = reports;
        this.thsReport = thsReport;
    }

    static class ReportHolder extends RecyclerView.ViewHolder {

        CardView cvReport;
        TextView title;
        TextView count;
        TextView procent;
        ImageView iconArrow;
        ProgressBar sla;

        public ReportHolder(@NonNull View itemView) {
            super(itemView);
            cvReport = itemView.findViewById(R.id.cv_report);
            title = itemView.findViewById(R.id.tv_title);
            count = itemView.findViewById(R.id.tv_count);
            procent = itemView.findViewById(R.id.tv_procent);
            sla = itemView.findViewById(R.id.sla_progress);
            iconArrow = itemView.findViewById(R.id.imageView2);
        }


        @SuppressLint({"SetTextI18n", "DefaultLocale"})
        private void onBindReportOperator(Report report, OnItemClickListener listener) {
            if (report.getName().length() > 4 ) {
                title.setText(report.getName());
                count.setText(String.valueOf(report.getCount()));
                float valueOne = (float) (report.getCount() / 100);
                float valueTwo = (float) report.getSlaNotExpiredCount();
                float proc = valueTwo / valueOne;
                if (valueOne >= proc) {
                    iconArrow.setImageResource(R.drawable.ic_arrow_green_up);
                } else {
                    iconArrow.setImageResource(R.drawable.ic_arrow_red_down);
                }
                procent.setText(String.format("%.2f", proc) + "%");
                sla.setMax(report.getCount());
                sla.setProgress(report.getSlaNotExpiredCount());
            } else {
                cvReport.setVisibility(View.GONE);
            }
        }

        private void onBindReportGroup(Report report, OnItemClickListener listener) {
            if (!report.getName().isEmpty()) {
                iconArrow.setImageResource(R.drawable.ic_arrow_right);
                title.setText(report.getName());
                count.setText(String.valueOf(report.getCount()));
                procent.setText(String.format("%.2f", report.getProcent()) + "%");
                sla.setMax(report.getCount());
                sla.setProgress(report.getSlaNotExpiredCount());
                cvReport.setOnClickListener(v -> listener.onItemClick(report));
            }
        }
    }
}
