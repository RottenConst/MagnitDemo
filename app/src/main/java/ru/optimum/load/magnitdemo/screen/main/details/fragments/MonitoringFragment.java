package ru.optimum.load.magnitdemo.screen.main.details.fragments;

import android.annotation.SuppressLint;
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
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import ru.optimum.load.magnitdemo.R;
import ru.optimum.load.magnitdemo.app.DemoApp;
import ru.optimum.load.magnitdemo.data.ChartData;
import ru.optimum.load.magnitdemo.data.Filter;
import ru.optimum.load.magnitdemo.db.DatabaseWrapper;
import ru.optimum.load.magnitdemo.screen.adapters.AdapterChartsCard;
import ru.optimum.load.magnitdemo.screen.adapters.AdapterFilter;
import ru.optimum.load.magnitdemo.screen.adapters.SpinnerAdapterPeriod;

import static ru.optimum.load.magnitdemo.DBContact.*;

public class MonitoringFragment extends Fragment {

    Spinner spinnerPeriod;
    SpinnerAdapterPeriod adapterPeriod;
    CardView cvFilters;
    RecyclerView cardRecycler;
    RecyclerView rvFilterList;
    TextView tvFilters;
    Button btnEnableFilter;
    ConstraintLayout lBottomFilters;
    BottomSheetBehavior bottomSheetFilters;
    AdapterChartsCard adapterChardCard;
    Boolean enableChartView;
    List<ChartData> chartData;
    Calendar calendar;
    private DatabaseWrapper dbWrite;
    List<String> filters;
    List<String[]> filterArg;
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

    Boolean filterEnabled = false;

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
            adapterChardCard.setData(chartData);
            adapterChardCard.notifyDataSetChanged();
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
        View view = inflater.inflate(R.layout.main_monitorig_layout, container, false);
        spinnerPeriod = view.findViewById(R.id.spnr_period_main);
        cvFilters = view.findViewById(R.id.cv_set_filters);
        cardRecycler = view.findViewById(R.id.rv_monitoring);
        tvFilters = view.findViewById(R.id.tv_filters);
        rvFilterList = view.findViewById(R.id.rv_filter_list);

        btnEnableFilter = view.findViewById(R.id.btn_set_filters);
        clearFilters = view.findViewById(R.id.tv_clear_filter);
        tvFilterCompany = view.findViewById(R.id.tv_filter_company);
        tvFilterFiliate = view.findViewById(R.id.tv_filter_filiate);
        tvFilterTypeComplect = view.findViewById(R.id.tv_filter_type_complect);
        tvFilterGroupProcessed = view.findViewById(R.id.tv_filter_group_process);
        tvFilterDistrict = view.findViewById(R.id.tv_filter_district);
        tvFilterPodrasdelenie = view.findViewById(R.id.tv_filter_podrasd);

        lBottomFilters = view.findViewById(R.id.bottom_set_filters);
        bottomSheetFilters = BottomSheetBehavior.from(lBottomFilters);
        bottomSheetFilters.setPeekHeight(0);

        dbWrite = DemoApp.dbWrapper();

        List<Filter> filtersTag = getFilters();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);

        rvFilterList.setLayoutManager(linearLayoutManager);
        adapterFilter = new AdapterFilter(getContext(), filtersTag);
        rvFilterList.setAdapter(adapterFilter);

        startDate = "2020-10-12";
        endDate = "2020-10-13";

        clearFilters.setOnClickListener(v -> {
            if (filterArg != null) {
                filterArg.clear();
                checkFilter(filterArg);
            }
            clearFilters(filtersTag);
            enableFilters(startDate, endDate, false);
            adapterChardCard.setData(chartData);
            adapterChardCard.notifyDataSetChanged();
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
            adapterChardCard.setData(chartData);
            adapterChardCard.notifyDataSetChanged();
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



        filters = new ArrayList<>(Arrays.asList("За день", "За неделю", "За месяц", "За все время", "Задать период"));
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        calendar = Calendar.getInstance();

        initSpinner();
        enableChartView = true;
        chartData = getDataCountsPeriod(startDate, endDate);
        initChartRecycle(chartData, false);

        spinnerPeriod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        startDate = "2020-10-12";
                        endDate = "2020-10-13";
                        enableFilters(startDate, endDate, filterEnabled);
                        adapterChardCard.setData(chartData);
                        adapterChardCard.notifyDataSetChanged();
                        break;
                    case 1:
                        startDate = "2020-10-06";
                        endDate = "2020-10-13";
                        enableFilters(startDate, endDate, filterEnabled);
                        adapterChardCard.setData(chartData);
                        adapterChardCard.notifyDataSetChanged();
                        break;
                    case 2:
                        Calendar thisMonth = new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) - 2, calendar.get(Calendar.DAY_OF_MONTH));
                        Calendar month = new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) - 3, calendar.get(Calendar.DAY_OF_MONTH));
                        Log.d("MONTH", "DATE = " + dateFormat.format(thisMonth.getTime()) + " " + dateFormat.format(month.getTime()));
                        enableFilters(dateFormat.format(month.getTime()), dateFormat.format(thisMonth.getTime()), filterEnabled);
                        adapterChardCard.setData(chartData);
                        adapterChardCard.notifyDataSetChanged();
                        break;
                    case 3:
                        startDate = dbWrite.getMinDate();
                        endDate = "2020-10-13";
                        enableFilters(startDate,endDate, filterEnabled);
                        adapterChardCard.setData(chartData);
                        adapterChardCard.notifyDataSetChanged();
                        break;
                    case 4:
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

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        ChartData openChart = new ChartData("Открыто", dbWrite.getCountsOf(OpenSet.TABLE_NAME, dateFrom, dateBefore), dbWrite.getCountOf(OpenSet.TABLE_NAME, dateFrom, dateBefore));
        countsMonitoring.add(openChart);

        ChartData receivedData = new ChartData("Поступило", dbWrite.getCountsOf(ReceiptSet.TABLE_NAME, dateFrom, dateBefore), dbWrite.getCountOf(ReceiptSet.TABLE_NAME, dateFrom, dateBefore));
        countsMonitoring.add(receivedData);

        int[] counts = dbWrite.getInWorkCount(dateFrom, dateBefore);
        int count = counts[0];
        int inWork = counts[1];

        ChartData inWorks = new ChartData("В работе", dbWrite.getInWorkCounts(dateFrom, dateBefore), inWork);
        countsMonitoring.add(inWorks);

        ChartData complete = new ChartData("Обработано", dbWrite.getCompleteCounts(dateFrom, dateBefore), count - inWork);
        countsMonitoring.add(complete);

        return countsMonitoring;
    }

    private List<ChartData> getDataCountsFilter(String selectFilter, String[] filtersArgument) {
        List<ChartData> countsMonitoring = new ArrayList<>();
        ChartData openChart = new ChartData("Открыто", dbWrite.getCountsWithFilter(OpenSet.TABLE_NAME, selectFilter, filtersArgument), dbWrite.getCountWithFilter(OpenSet.TABLE_NAME, selectFilter, filtersArgument));
        countsMonitoring.add(openChart);

        ChartData receivedData = new ChartData("Поступило", dbWrite.getCountsWithFilter(ReceiptSet.TABLE_NAME, selectFilter, filtersArgument), dbWrite.getCountWithFilter(ReceiptSet.TABLE_NAME, selectFilter, filtersArgument));
        countsMonitoring.add(receivedData);

        int[] counts = dbWrite.getInWorkCountWithFilter(selectFilter, filtersArgument);
        int count = counts[0];
        int inWork = counts[1];

        ChartData inWorks = new ChartData("В работе", dbWrite.getInWorkCountsWithFilter(selectFilter, filtersArgument), inWork);
        countsMonitoring.add(inWorks);

        ChartData complete = new ChartData("Обработано", dbWrite.getCompleteCountsWithFilter(selectFilter, filtersArgument), count - inWork);
        countsMonitoring.add(complete);

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

    //инициализация Recycler View
    private void initChartRecycle(List<ChartData> listOfChart, boolean enableChartView) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        if (enableChartView) {
            linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
            adapterChardCard = new AdapterChartsCard(getContext(), listOfChart, enableChartView);
            cardRecycler.setLayoutManager(linearLayoutManager);
        } else {
            gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
            adapterChardCard = new AdapterChartsCard(getContext(), listOfChart, enableChartView);
            cardRecycler.setLayoutManager(gridLayoutManager);
        }
        cardRecycler.setAdapter(adapterChardCard);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.set_view:
                if (enableChartView) {
                    item.setIcon(R.drawable.ic_card_set);
                    initChartRecycle(chartData, enableChartView);
                    adapterChardCard.notifyDataSetChanged();
                    enableChartView = false;
                } else {
                    item.setIcon(R.drawable.ic_grafic_set);
                    initChartRecycle(chartData, enableChartView);
                    adapterChardCard.notifyDataSetChanged();
                    enableChartView = true;
                }

                return enableChartView;
        }
        return true;
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

    public static Fragment newInstance() {
        return new MonitoringFragment();
    }
}
