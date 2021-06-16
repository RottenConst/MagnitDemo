package ru.optimum.load.magnitdemo.screen.main.details.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ru.optimum.load.magnitdemo.R;
import ru.optimum.load.magnitdemo.app.DemoApp;
import ru.optimum.load.magnitdemo.data.ChartData;
import ru.optimum.load.magnitdemo.data.Report;
import ru.optimum.load.magnitdemo.db.DatabaseWrapper;
import ru.optimum.load.magnitdemo.screen.adapters.AdapterReport;
import ru.optimum.load.magnitdemo.screen.adapters.SpinnerAdapterReport;

public class ReportsFragment extends Fragment {

    Spinner spinnerReport;
    SpinnerAdapterReport adapterSpinner;
    ImageView imageViewLogo;
    TextView tvInfoText;
    RecyclerView rvReports;
    AdapterReport adapterReport;
    DatabaseWrapper dbWrapper;
    List<String> reports = new ArrayList<>();
    List<Report> dataReports = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //        reports.add("Отчет не выбран");
        reports.add("Динамика обработки по секторам");
        reports.add("Обработка сотрудников от плана");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.reports_layout, container, false);
        spinnerReport = view.findViewById(R.id.spinner_reports);
        imageViewLogo = view.findViewById(R.id.logo_image);
        tvInfoText = view.findViewById(R.id.tv_info_text);
        rvReports = view.findViewById(R.id.rv_reports);
        dbWrapper = DemoApp.dbWrapper();

        imageViewLogo.setVisibility(View.GONE);
        tvInfoText.setVisibility(View.GONE);
        rvReports.setVisibility(View.VISIBLE);

        initRecyclerReport(this.getContext(), dataReports);
        adapterSpinner = new SpinnerAdapterReport(getContext(), R.layout.row_spinner_report, reports);
        spinnerReport.setAdapter(adapterSpinner);


        spinnerReport.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        rvReports.setVisibility(View.VISIBLE);
//                        imageViewLogo.setVisibility(View.GONE);
//                        tvInfoText.setVisibility(View.GONE);
                        dataReports = sortGroup();
                        adapterReport.setReports(dataReports, position);
                        adapterReport.notifyDataSetChanged();
                        break;
                    case 1:
                        rvReports.setVisibility(View.VISIBLE);
//                          imageViewLogo.setVisibility(View.GONE);
//                          tvInfoText.setVisibility(View.GONE);
                        dataReports = dbWrapper.getReportTopOperator();
                        adapterReport.setReports(dataReports, position);
                        adapterReport.notifyDataSetChanged();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return view;
    }

    private List<Report> sortGroup() {
        boolean sorted = false;
        List<Report> groupReport = dbWrapper.getReportToGroupTop();
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


    private void initRecyclerReport(Context context, List<Report> reports) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        adapterReport = new AdapterReport(context, reports, 0, report -> {
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ReportOnSectorFragment reportOnSectorFragment = ReportOnSectorFragment.newInstance(report.getName());
            ft.addToBackStack("operator");
            ft.replace(R.id.tab_container, reportOnSectorFragment).commit();
        });
        rvReports.setLayoutManager(linearLayoutManager);
        rvReports.setAdapter(adapterReport);
    }

    public static Fragment newInstance() {
        return new ReportsFragment();
    }
}
