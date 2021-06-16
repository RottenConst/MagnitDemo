package ru.optimum.load.magnitdemo.screen.main.details.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ru.optimum.load.magnitdemo.R;
import ru.optimum.load.magnitdemo.app.DemoApp;
import ru.optimum.load.magnitdemo.data.Report;
import ru.optimum.load.magnitdemo.db.DatabaseWrapper;
import ru.optimum.load.magnitdemo.screen.adapters.AdapterReport;
import ru.optimum.load.magnitdemo.screen.adapters.SpinnerAdapterReport;

public class ReportOnSectorFragment extends Fragment {

    Spinner spinnerReport;
    SpinnerAdapterReport adapterSpinner;
    ImageView imageViewLogo;
    TextView tvInfoText;
    RecyclerView rvReports;
    AdapterReport adapterReport;

    String titleSector;
    List<Report> dataReports = new ArrayList<>();
    DatabaseWrapper dbWrite;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        titleSector = getArguments().getString("title");
        dbWrite = DemoApp.dbWrapper();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.reports_layout, container, false);
        spinnerReport = view.findViewById(R.id.spinner_reports);
        imageViewLogo = view.findViewById(R.id.logo_image);
        tvInfoText = view.findViewById(R.id.tv_info_text);
        rvReports = view.findViewById(R.id.rv_reports);

        imageViewLogo.setVisibility(View.GONE);
        tvInfoText.setVisibility(View.GONE);

        spinnerReport.setVisibility(View.GONE);
        rvReports.setVisibility(View.VISIBLE);

        dataReports = sortGroup();
        initRecyclerReport(getContext(), dataReports);
        adapterReport.notifyDataSetChanged();

        return view;
    }

    private void initRecyclerReport(Context context, List<Report> reports) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        adapterReport = new AdapterReport(context, reports, 1, new AdapterReport.OnItemClickListener() {
            @Override
            public void onItemClick(Report report) {
                Toast.makeText(getContext(), report.getName(), Toast.LENGTH_LONG).show();
            }
        });
        rvReports.setLayoutManager(linearLayoutManager);
        rvReports.setAdapter(adapterReport);
    }

    private List<Report> sortGroup() {
        boolean sorted = false;
        List<Report> groupReport = dbWrite.getReportOperatorToUnitName(titleSector);
        List<Report> topReport = new ArrayList<>();
        Report report;
        while (!sorted) {
            sorted = true;
            for (int i = 0; i < groupReport.size() - 1; i++ ) {
                if (groupReport.get(i).getCount() < groupReport.get(i+1).getCount()) {
                    report = new Report(groupReport.get(i).getName(), groupReport.get(i).getSlaNotExpiredCount(), groupReport.get(i).getCount());
                    groupReport.set(i, groupReport.get(i + 1));
                    groupReport.set(i + 1, report);
                    sorted = false;
                }
            }
        }
        for (int i = 0; i < 9; i++) {
            topReport.add(groupReport.get(i));
        }
        return topReport;
    }

    public static ReportOnSectorFragment newInstance(String title) {
        ReportOnSectorFragment reportOnSectorFragment = new ReportOnSectorFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        reportOnSectorFragment.setArguments(args);
        return reportOnSectorFragment;
    }
}
