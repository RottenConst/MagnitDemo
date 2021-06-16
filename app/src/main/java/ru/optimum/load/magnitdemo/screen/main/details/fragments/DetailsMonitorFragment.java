package ru.optimum.load.magnitdemo.screen.main.details.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appeaser.sublimepickerlibrary.datepicker.SelectedDate;
import com.appeaser.sublimepickerlibrary.helpers.SublimeOptions;
import com.appeaser.sublimepickerlibrary.recurrencepicker.SublimeRecurrencePicker;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import ru.optimum.load.magnitdemo.DBContact;
import ru.optimum.load.magnitdemo.R;
import ru.optimum.load.magnitdemo.app.DemoApp;
import ru.optimum.load.magnitdemo.data.ChartData;
import ru.optimum.load.magnitdemo.data.Filter;
import ru.optimum.load.magnitdemo.db.DatabaseWrapper;
import ru.optimum.load.magnitdemo.screen.adapters.AdapterChartsCard;
import ru.optimum.load.magnitdemo.screen.adapters.AdapterFilter;
import ru.optimum.load.magnitdemo.screen.adapters.SpinnerAdapterPeriod;
import ru.optimum.load.magnitdemo.utils.SharedPreferensStorage;

public class DetailsMonitorFragment extends Fragment implements OnChartValueSelectedListener {

    Spinner spinnerPeriod;
    SpinnerAdapterPeriod adapterPeriod;
    CardView cvFilters;

    String title;
    LineChart chart;
    TextView tvTitleChart;
    TextView tvCountChart;
    ImageView ivArrow;
    TextView tvDateOfCount;
    CardView cvChart;

    RecyclerView rvFilterList;
    TextView tvFilters;
    Button btnEnableFilter;
    ConstraintLayout lBottomFilters;
    BottomSheetBehavior bottomSheetFilters;

    List<ChartData> chartData = new ArrayList<>();
    Calendar calendar;
    private DatabaseWrapper dbWrite;
    List<String> filters;
    List<String[]> filterArg = new ArrayList<>();
    String startDate;
    String endDate;

    String[] filtersArgument;
    String selectFilter;
    AdapterFilter adapterFilter;
    TextView clearFilters;
    TextView tvFilterCompany;
    TextView tvFilterFiliate;
    TextView tvFilterTypeComplect;
    TextView tvFilterGroupProcessed;
    TextView tvFilterDistrict;
    TextView tvFilterPodrasdelenie;
    List<Filter> filtersTag = new ArrayList<>();

    Boolean filterEnabled;

    SelectedDate mSelectDate;
    String mRecurrenceOption, mRecurrenceRule;

    SublimePickerFragment.Callback mSublimePickerCallback = new SublimePickerFragment.Callback() {
        @Override
        public void onCancelled() {

        }

        @Override
        public void onDateTimeRecurrenceSet(SelectedDate selectedDate, int hourOfDay, int minute, SublimeRecurrencePicker.RecurrenceOption recurrenceOption, String recurrenceRule) {
            mSelectDate = selectedDate;
            mRecurrenceOption = recurrenceOption != null ? recurrenceOption.name() : "n/a";
            mRecurrenceRule = recurrenceRule != null ? recurrenceRule : "n/a";

            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            DateFormat format = new SimpleDateFormat("dd.MM.yyyy");
            startDate = df.format(mSelectDate.getStartDate().getTime());
            endDate = df.format(mSelectDate.getEndDate().getTime());
            chartData.clear();
            enableFilters(startDate, endDate, filterEnabled);
            initChart(chartData.get(0));

//            setSlaProgressBar(getSlaPercent(startDate, endDate), getSlaExpiredPercent(startDate, endDate));
            Toast.makeText(getContext(), startDate + " " + endDate, Toast.LENGTH_LONG).show();
            if (filters.size() == 5) {
                filters.add(5,format.format(mSelectDate.getStartDate().getTime()) + " - " + format.format(mSelectDate.getEndDate().getTime()) );
            } else {
                filters.set(5, format.format(mSelectDate.getStartDate().getTime()) + " - " + format.format(mSelectDate.getEndDate().getTime()) );
            }
            spinnerPeriod.setSelection(5);
            adapterPeriod.notifyDataSetChanged();
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chart_fragment, container, false);
        spinnerPeriod = view.findViewById(R.id.spinner_period_main);
        cvFilters = view.findViewById(R.id.card_set_filters);

        tvFilters = view.findViewById(R.id.text_filters);
        rvFilterList = view.findViewById(R.id.rv_filter_list);

        chart = view.findViewById(R.id.chart_monitoring_detail);
        tvCountChart = view.findViewById(R.id.text_count_card);
        ivArrow = view.findViewById(R.id.image_arrow);
        tvDateOfCount = view.findViewById(R.id.text_date_of_number);
        tvTitleChart = view.findViewById(R.id.text_title_card);
        cvChart = view.findViewById(R.id.card_of_detail);

        btnEnableFilter = view.findViewById(R.id.btn_set_filters);
        clearFilters = view.findViewById(R.id.text_clear_filter);
        tvFilterCompany = view.findViewById(R.id.text_filter_company);
        tvFilterFiliate = view.findViewById(R.id.text_filter_filiate);
        tvFilterTypeComplect = view.findViewById(R.id.text_filter_type_complect);
        tvFilterGroupProcessed = view.findViewById(R.id.text_filter_group_process);
        tvFilterDistrict = view.findViewById(R.id.text_filter_district);
        tvFilterPodrasdelenie = view.findViewById(R.id.text_filter_podrasd);

        lBottomFilters = view.findViewById(R.id.bottom_set_filters);


        bottomSheetFilters = BottomSheetBehavior.from(lBottomFilters);
        bottomSheetFilters.setPeekHeight(0);


//        if (filtersTag.size() == 0) {
//            filtersTag = getFilters();
//        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);

        rvFilterList.setLayoutManager(linearLayoutManager);
        adapterFilter = new AdapterFilter(getContext(), filtersTag);
        rvFilterList.setAdapter(adapterFilter);

        checkFilter(filterArg);
        adapterFilter.setFilters(filterArg);


        clearFilters.setOnClickListener(v -> {
            if (filterArg != null) {
                filterArg.clear();
                checkFilter(filterArg);
            }
            clearFilters(filtersTag);
            enableFilters(startDate, endDate, false);
            initChart(chartData.get(0));
        });

        cvFilters.setOnClickListener(v -> {
            if (bottomSheetFilters.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetFilters.setState(BottomSheetBehavior.STATE_EXPANDED);

            } else {
                bottomSheetFilters.setState(BottomSheetBehavior.STATE_COLLAPSED);
                bottomSheetFilters.setPeekHeight(0);

            }
        });

        btnEnableFilter.setOnClickListener(v -> {
            if (!filterEnabled) {
                filterEnabled = true;
            }
            enableFilters(startDate, endDate, filterEnabled);
            initChart(chartData.get(0));
            checkFilter(filterArg);
            bottomSheetFilters.setState(BottomSheetBehavior.STATE_COLLAPSED);
            bottomSheetFilters.setPeekHeight(0);

        });

        bottomSheetFilters.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_EXPANDED:
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        filters = new ArrayList<>();
        filters.add(startDate + " - " + endDate);
        filters.add("За день");
        filters.add("За неделю");
        filters.add("За месяц");
        filters.add("За все время");
        filters.add("Задать период");

        calendar = Calendar.getInstance();

        initSpinner();


        spinnerPeriod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        if (filterEnabled) {
                            Log.d("FILTER", "onItemSelected: 1");
                            enableFilters(startDate, endDate, true);
                            initChart(chartData.get(0));
                            checkFilter(filterArg);
                            adapterFilter.notifyDataSetChanged();
                        } else {
                            enableFilters(startDate, endDate, false);
                            initChart(chartData.get(0));
                        }
                        break;
                    case 1:
                        startDate = "2020-10-12";
                        endDate = "2020-10-13";
                        enableFilters(startDate, endDate, filterEnabled);
                        initChart(chartData.get(0));

                        break;
                    case 2:
                        startDate = "2020-09-06";
                        endDate = "2020-09-13";
                        enableFilters(startDate, endDate, filterEnabled);
                        initChart(chartData.get(0));

                        break;
                    case 3:

                        startDate = "2020-09-01";
                        endDate = "2020-09-31";

                        enableFilters(startDate, endDate, filterEnabled);
                        initChart(chartData.get(0));
//                        setSlaProgressBar(getSlaPercent(dateFormat.format(month.getTime()), dateFormat.format(thisMonth.getTime())), getSlaExpiredPercent(dateFormat.format(month.getTime()), dateFormat.format(thisMonth.getTime())));

                        break;
                    case 4:
                        startDate = dbWrite.getMinDate();
                        endDate = "2020-10-13";
                        enableFilters(startDate,endDate, filterEnabled);
                        initChart(chartData.get(0));

                        break;
                    case 5:
                        SublimePickerFragment pickerFragment = new SublimePickerFragment();
                        pickerFragment.setCallback(mSublimePickerCallback);
                        Pair<Boolean, SublimeOptions> optionsPair = getOptions();
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("SUBLIME_OPTIONS", optionsPair.second);
                        pickerFragment.setArguments(bundle);
                        pickerFragment.setStyle(DialogFragment.STYLE_NORMAL, 0);
                        pickerFragment.show(getFragmentManager(), "SUBLIME_PICKER");
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
//        enableFilters(iStartData, iEndData, filterEnabled);
//        initChart(chartData.get(0));
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferensStorage.init(getContext());
        startDate = getArguments().getString("startDate");
        endDate = getArguments().getString("endDate");
        title = getArguments().getString("title");
        filterEnabled = getArguments().getBoolean("filterEnabled");
        int size = getArguments().getInt("size");
        int sizeA = getArguments().getInt("sizeA");
        for (int i = 0; i < sizeA; i++) {
            String[] filt = SharedPreferensStorage.getListString("fa" + i);
            filterArg.add(filt);
        }

        for (int i = 0; i < size; i++) {
            Filter filter = SharedPreferensStorage.getFilter("filter" + i);
            filtersTag.add(filter);
        }

        SharedPreferensStorage.clearAll();
        dbWrite = DemoApp.dbWrapper();


//        String[] countStr = getArguments().getStringArray("countString");
//        int[] countInt = getArguments().getIntArray("countInt");
//        int count = getArguments().getInt("count");
//        List<Pair<String,Integer>> counts = new ArrayList<>();
//        for (int i = 0; i < countInt.length; i++) {
//            counts.add(new Pair<>(countStr[i], countInt[i]));
//        }
//        chartData.add(new ChartData(title, counts, count));
        setHasOptionsMenu(true);
    }

    private List<Filter> getFilters() {
        List<Filter> filtersTag = new ArrayList<>();
        filtersTag.addAll(getCompanyFilter());
        filtersTag.addAll(getFiliateFilter());
        filtersTag.addAll(getTypeComplects());
        filtersTag.addAll(getGroupOfProcess());
        filtersTag.addAll(getDistrictFilter());
        filtersTag.addAll(getSubdivision());

        return filtersTag;
    }

    private void clearFilters(List<Filter> filtersTag) {
        filtersTag.clear();
        filtersTag = getFilters();
        adapterFilter = new AdapterFilter(getContext(), filtersTag);
        rvFilterList.setAdapter(adapterFilter);
        adapterFilter.notifyDataSetChanged();
    }

    @SuppressLint("SetTextI18n")
    private void checkFilter(List<String[]> filterArg) {
        if (filterArg != null && !filterArg.isEmpty()) {
            for (int i = 0; i < filterArg.size(); i++) {
                switch (i) {
                    case 0:
                        if (filterArg.get(i).length > 0) {
                            tvFilterCompany.setVisibility(View.VISIBLE);
                            tvFilterCompany.setText("Организация(" + (filterArg.get(i).length -1) + ")");
                        } else tvFilterCompany.setVisibility(View.GONE);
                        break;
                    case 1:
                        if (filterArg.get(i).length > 0 ) {
                            tvFilterFiliate.setVisibility(View.VISIBLE);
                            tvFilterFiliate.setText("Филиал(" + (filterArg.get(i).length -1) + ")");
                        } else tvFilterFiliate.setVisibility(View.GONE);
                        break;
                    case 2:
                        if (filterArg.get(i).length > 0 ) {
                            tvFilterTypeComplect.setVisibility(View.VISIBLE);
                            tvFilterTypeComplect.setText("Тип комплекта(" + (filterArg.get(i).length - 1) + ")");
                        } else tvFilterTypeComplect.setVisibility(View.GONE);
                        break;
                    case 3:
                        if (filterArg.get(i).length > 0 ) {
                            tvFilterGroupProcessed.setVisibility(View.VISIBLE);
                            tvFilterGroupProcessed.setText("Группа Обработки(" + (filterArg.get(i).length - 1) + ")");
                        } else tvFilterGroupProcessed.setVisibility(View.GONE);
                        break;
                    case 4:
                        if (filterArg.get(i).length > 0 ) {
                            tvFilterDistrict.setVisibility(View.VISIBLE);
                            tvFilterDistrict.setText("Округ(" + (filterArg.get(i).length - 1) + ")");
                        } else tvFilterDistrict.setVisibility(View.GONE);
                        break;
                    case 5:
                        if (filterArg.get(i).length > 0 ) {
                            tvFilterPodrasdelenie.setVisibility(View.VISIBLE);
                            tvFilterPodrasdelenie.setText("Подразделение(" + (filterArg.get(i).length -1) + ")");
                        } else tvFilterPodrasdelenie.setVisibility(View.GONE);
                        break;
                }
            }
        } else  {
            tvFilterCompany.setVisibility(View.GONE);
            tvFilterFiliate.setVisibility(View.GONE);
            tvFilterTypeComplect.setVisibility(View.GONE);
            tvFilterGroupProcessed.setVisibility(View.GONE);
            tvFilterDistrict.setVisibility(View.GONE);
            tvFilterPodrasdelenie.setVisibility(View.GONE);
        }
    }

    private List<ChartData> getDataCountsPeriod(String dateFrom, String dateBefore) {
        List<ChartData> countsMonitoring = new ArrayList<>();
        int[] counts = dbWrite.getInWorkCount(dateFrom, dateBefore);
        int count = counts[0];
        int inWork = counts[1];

        switch (title) {
            case "Открыто":
                ChartData openChart = new ChartData("Открыто", dbWrite.getCountsOf(DBContact.OpenSet.TABLE_NAME, dateFrom, dateBefore), dbWrite.getCountOf(DBContact.OpenSet.TABLE_NAME, dateFrom, dateBefore));
                countsMonitoring.add(openChart);
                break;
            case "Поступило":
                ChartData receivedData = new ChartData("Поступило", dbWrite.getCountsOf(DBContact.ReceiptSet.TABLE_NAME, dateFrom, dateBefore), dbWrite.getCountOf(DBContact.ReceiptSet.TABLE_NAME, dateFrom, dateBefore));
                countsMonitoring.add(receivedData);
                break;
            case "В работе":
                ChartData inWorks = new ChartData("В работе", dbWrite.getInWorkCounts(dateFrom, dateBefore), inWork);
                countsMonitoring.add(inWorks);
                break;
            case "Обработано":
                ChartData complete = new ChartData("Обработано", dbWrite.getCompleteCounts(dateFrom, dateBefore), count - inWork);
                countsMonitoring.add(complete);
        }

        return countsMonitoring;
    }

    private List<ChartData> getDataCountsFilter(String selectFilter, String[] filtersArgument) {
        List<ChartData> countsMonitoring = new ArrayList<>();
        int[] counts = dbWrite.getInWorkCountWithFilter(selectFilter, filtersArgument);
        int count = counts[0];
        int inWork = counts[1];
        switch (title) {
            case "Открыто":
                ChartData openChart = new ChartData("Открыто", dbWrite.getCountsWithFilter(DBContact.OpenSet.TABLE_NAME, selectFilter, filtersArgument), dbWrite.getCountWithFilter(DBContact.OpenSet.TABLE_NAME, selectFilter, filtersArgument));
                countsMonitoring.add(openChart);
                break;
            case "Поступило":
                ChartData receivedData = new ChartData("Поступило", dbWrite.getCountsWithFilter(DBContact.ReceiptSet.TABLE_NAME, selectFilter, filtersArgument), dbWrite.getCountWithFilter(DBContact.ReceiptSet.TABLE_NAME, selectFilter, filtersArgument));
                countsMonitoring.add(receivedData);
                break;
            case "В работе":
                ChartData inWorks = new ChartData("В работе", dbWrite.getInWorkCountsWithFilter(selectFilter, filtersArgument), inWork);
                countsMonitoring.add(inWorks);
                break;
            case "Обработано":
                ChartData complete = new ChartData("Обработано", dbWrite.getCompleteCountsWithFilter(selectFilter, filtersArgument), count - inWork);
                countsMonitoring.add(complete);
                break;
        }

        return countsMonitoring;
    }

    private String getFilter(List<String[]> filterArg) {
        StringBuilder filter = new StringBuilder();
        filter.append("(date(STATESTARTDATE) BETWEEN ? AND ?) AND (");
        for (int i = 0; i < filterArg.size(); i++) {
            String[] filters = filterArg.get(i);
            if (filters.length > 1) {
                for (int c = 1; c < filters.length; c++){
                    filter.append(filters[0]).append(" = ? OR ");
                }
            }
        }
        int lastChar = filter.length();
        filter.delete(lastChar - 4, lastChar);
        filter.append(")");
        return filter.toString();
    }

    private String[] getFiltersArg(List<String[]> filtersArg, String startDate, String endDate) {
        StringBuilder arg = new StringBuilder(startDate + ";" + endDate);
        String[] filtersArgs;
        for (int i = 0; i < filtersArg.size(); i++) {
            String[] filters = filtersArg.get(i);
            if (filters.length > 1) {
                for (int c = 1; c < filters.length; c++) {
                    if (filters[c] != null) {
                        arg.append(";").append(filters[c]);
                    }
                }
            }
        }
        filtersArgs = arg.toString().split(";");
        for (int i = 0; i < filtersArgs.length; i++) {
            Log.d("FILTER INIT", filtersArgs[i]);
        }
        return filtersArgs;
    }

    private void enableFilters(String startDate, String endDate, boolean enable) {
        chartData.clear();
        if (enable) {
            filterArg = adapterFilter.getFilterArg();
            filtersArgument = getFiltersArg(filterArg, startDate, endDate);
            selectFilter = getFilter(filterArg);


//            Log.d("FILTERS MAIN", "Size = " + filtersArgument.length);
//            for (int i = 0; i < filtersArgument.length; i++) {
//                Log.d("FILTERS MAIN", "Filters = " + filtersArgument[i]);
//            }
            if (filtersArgument.length > 2) {
                filterEnabled = true;
                chartData = getDataCountsFilter(selectFilter, filtersArgument);

            } else {
                filterEnabled = false;
                chartData = getDataCountsPeriod(startDate, endDate);

            }
        } else {
            chartData = getDataCountsPeriod(startDate, endDate);
        }
        Log.d("TAG", "getChart " + chartData.size());
    }

    private List<Filter> getCompanyFilter() {
        List<Filter> filter = new ArrayList<>();
        List<String> company = dbWrite.getCompanyFilters();
        filter.add(new Filter());
        filter.get(0).setCategory("Организация");
        String[] titles = new String[company.size()];
        boolean[] isChecked = new boolean[company.size()];
        for (int i = 0; i < company.size(); i++) {
            titles[i] = company.get(i);
            isChecked[i] = false;
        }
        filter.get(0).setTitle(titles);
        filter.get(0).setCheck(isChecked);
        return filter;
    }

    private List<Filter> getTypeComplects() {
        List<Filter> filter = new ArrayList<>();
        List<String> types = dbWrite.getTypeComplect();
        filter.add(new Filter());
        filter.get(0).setCategory("Тип комплекта");
        String[] titles = new String[types.size()];
        boolean[] isChecked = new boolean[types.size()];
        for (int i = 0; i < types.size(); i++) {
            titles[i] = types.get(i);
            isChecked[i] = false;
        }
        filter.get(0).setTitle(titles);
        filter.get(0).setCheck(isChecked);
        return filter;
    }

    private List<Filter> getSubdivision() {
        List<Filter> filter = new ArrayList<>();
        List<String> subdivisions = dbWrite.getSubdivision();
        filter.add(new Filter());
        filter.get(0).setCategory("Подразделение");
        String[] titles = new String[subdivisions.size()];
        boolean[] isChecked = new boolean[subdivisions.size()];
        for (int i = 0; i < subdivisions.size(); i++) {
            titles[i] = subdivisions.get(i);
            isChecked[i] = false;
        }
        filter.get(0).setTitle(titles);
        filter.get(0).setCheck(isChecked);
        return filter;
    }

    private List<Filter> getFiliateFilter() {
        List<Filter> filter = new ArrayList<>();
        List<String> filiates = dbWrite.getFiliateFilters();
        filter.add(new Filter());
        filter.get(0).setCategory("Филиал");
        String[] titles = new String[filiates.size()];
        boolean[] isChecked = new boolean[filiates.size()];
        for (int i = 0; i < filiates.size(); i++) {
            titles[i] = filiates.get(i);
            isChecked[i] = false;
        }
        filter.get(0).setTitle(titles);
        filter.get(0).setCheck(isChecked);
        return filter;
    }

    private List<Filter> getDistrictFilter() {
        List<Filter> filter = new ArrayList<>();
        List<String> districts = dbWrite.getDistrictFilters();
        filter.add(new Filter());
        filter.get(0).setCategory("Округ");
        districts.remove(2);
        districts.remove(6);
        String[] titles = new String[districts.size()];
        boolean[] isChecked = new boolean[districts.size()];
        for (int i = 0; i < districts.size(); i++) {
            titles[i] = districts.get(i);
            isChecked[i] = false;
        }
        filter.get(0).setTitle(titles);
        filter.get(0).setCheck(isChecked);
        return filter;
    }

    private List<Filter> getGroupOfProcess() {
        List<Filter> filter = new ArrayList<>();
        List<String> groupOfProcess = dbWrite.getGroupOfProcess();
        filter.add(new Filter());
        filter.get(0).setCategory("Группа Обработки");
        String[] titles = new String[groupOfProcess.size()];
        boolean[] isChecked = new boolean[groupOfProcess.size()];
        for (int i = 0; i < groupOfProcess.size(); i++) {
            titles[i] = groupOfProcess.get(i);
            isChecked[i] = false;
        }
        filter.get(0).setTitle(titles);
        filter.get(0).setCheck(isChecked);
        return filter;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
//        inflater.inflate(R.menu.menu_main, menu);
    }

    private void initChart(ChartData chartData) {
        setChartStyle(getContext(), chartData);
        tvTitleChart.setText(chartData.getTitle());
        tvCountChart.setText(String.valueOf(chartData.getCount()));
        int lastIndex = chartData.getCounts().size() - 1;
        if (chartData.getCounts().get(0).second > chartData.getCounts().get(lastIndex).second) {
            ivArrow.setImageResource(R.drawable.ic_arrow_red_down);
        } else if (chartData.getCounts().get(0).equals(chartData.getCounts().get(lastIndex))) {
            ivArrow.setImageResource(R.color.white);
        } else {
            ivArrow.setImageResource(R.drawable.ic_arrow_green_up);
        }
        switch (chartData.getTitle()) {
            case "Открыто":
                cvChart.setCardBackgroundColor(getContext().getResources().getColor(R.color.cardViewOpen));
                break;
            case "Поступило":
                cvChart.setCardBackgroundColor(getContext().getResources().getColor(R.color.cardViewReceived));
                break;
            case "В работе":
                cvChart.setCardBackgroundColor(getContext().getResources().getColor(R.color.cardViewInWork));
                break;
            case "Обработано":
                cvChart.setCardBackgroundColor(getContext().getResources().getColor(R.color.cardViewComplete));
                break;
        }
    }

    private void setChartStyle(Context context, ChartData chartData) {

        chart.getAxisRight().setEnabled(false);
        chart.getAxisLeft().setEnabled(true);
        chart.getDescription().setEnabled(false);
        chart.getLegend().setEnabled(false);
        chart.setBackgroundColor(Color.WHITE);
        chart.setOnChartValueSelectedListener(this);
        chart.setDrawGridBackground(false);

        MarkerView markerView = new ru.optimum.load.magnitdemo.help.MarkerView(context, R.layout.custom_marker_view);
        markerView.setChartView(chart);
        chart.setMarker(markerView);

        chart.setDragEnabled(true);
        chart.setPinchZoom(true);
        chart.animateX(2000);

        XAxis xAxis = chart.getXAxis();
        xAxis.setEnabled(false);

        YAxis yAxis = chart.getAxisLeft();
        yAxis.setLabelCount(4, true);
        yAxis.setDrawLabels(false);
        yAxis.setDrawAxisLine(false);
        setDataChart(chartData);
    }

    private void setDataChart(ChartData chartData) {
        ArrayList<Entry> values = new ArrayList<>();
        if (chartData.getCounts().size() > 100) {
            List<Pair<String, Integer>> counts = chartData.getCounts();
            for (int i = 0; i < chartData.getCounts().size(); i += 10) {
                String[] date = counts.get(i).first.split("-");
                int year = Integer.parseInt(date[0]);
                int month = Integer.parseInt(date[1]);
                int day = Integer.parseInt(date[2]);
                Calendar mDate = new GregorianCalendar(year, month - 1, day);
                values.add(new Entry(i, counts.get(i).second, mDate.getTime()));
            }
        } else {
            List<Pair<String, Integer>> counts = chartData.getCounts();
            for (int i = 0; i < chartData.getCounts().size(); i++) {
                String[] date = counts.get(i).first.split("-");
                int year = Integer.parseInt(date[0]);
                int month = Integer.parseInt(date[1]);
                int day = Integer.parseInt(date[2]);
                Calendar mDate = new GregorianCalendar(year, month - 1, day);
                values.add(new Entry(i, counts.get(i).second, mDate.getTime()));
            }
        }


        LineDataSet lineData;
        if (chart.getData() != null && chart.getData().getDataSetCount() > 0) {
            lineData = (LineDataSet) chart.getData().getDataSetByIndex(0);
            lineData.setValues(values);
            lineData.notifyDataSetChanged();
            chart.getData().notifyDataChanged();
        } else {
            lineData = new LineDataSet(values, "");
            lineData.setColor(Color.RED);
            lineData.setCircleColor(Color.RED);
            lineData.setLineWidth(2f);
            lineData.setCircleRadius(3f);
            lineData.setFormLineWidth(4f);
            lineData.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
            lineData.setDrawValues(false);
        }

        LineData data = new LineData(lineData);
        chart.setData(data);
    }


    Pair<Boolean, SublimeOptions> getOptions() {
        SublimeOptions options = new SublimeOptions();

        int displayOptions = SublimeOptions.ACTIVATE_DATE_PICKER;
        options.setPickerToShow(SublimeOptions.Picker.DATE_PICKER);
        options.setDisplayOptions(displayOptions);
        options.setCanPickDateRange(true);

        return new Pair<>(Boolean.TRUE, options);
    }

    private void initSpinner() {
        adapterPeriod = new SpinnerAdapterPeriod(getContext(), R.layout.row_spinner_period, filters);
        spinnerPeriod.setAdapter(adapterPeriod);
    }

    public static DetailsMonitorFragment newInstance(String title, String startDate, String endDate, Boolean filterEnabled, int size, int sizeA) {
        DetailsMonitorFragment detailsMonitorFragment = new DetailsMonitorFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("startDate", startDate);
        args.putString("endDate", endDate);
        args.putBoolean("filterEnabled", filterEnabled);
        args.putInt("size", size);
        args.putInt("sizeA", sizeA);
        detailsMonitorFragment.setArguments(args);
        return detailsMonitorFragment;
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        DateFormat df = new SimpleDateFormat("dd MMMM yyyy");
        tvDateOfCount.setText(df.format(e.getData()) + " года");
        Log.i("Entry selected", e.toString());
        Log.i("LOW HIGH", "low: " + chart.getLowestVisibleX() + ", high: " + chart.getHighestVisibleX());
        Log.i("MIN MAX", "xMin: " + chart.getXChartMin() + ", xMax: " + chart.getXChartMax() + ", yMin: " + chart.getYChartMin() + ", yMax: " + chart.getYChartMax());
    }

    @Override
    public void onNothingSelected() {
        tvDateOfCount.setText("Выберите точку на графике");
    }
}
