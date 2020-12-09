package ru.optimum.load.magnitdemo.screen.main.details.fragments.time;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import ru.optimum.load.magnitdemo.R;
import ru.optimum.load.magnitdemo.app.DemoApp;
import ru.optimum.load.magnitdemo.data.TestData;
import ru.optimum.load.magnitdemo.data.TestGroupData;
import ru.optimum.load.magnitdemo.data.TimeTestData;
import ru.optimum.load.magnitdemo.db.DatabaseWrapper;
import ru.optimum.load.magnitdemo.screen.adapters.AdapterDetailsList;
import ru.optimum.load.magnitdemo.screen.adapters.AdapterDistrictTimeList;
import ru.optimum.load.magnitdemo.screen.adapters.AdapterGroupList;
import ru.optimum.load.magnitdemo.screen.adapters.SpinnerAdapterPeriod;
/*
    Фрагмент отображающий детальную информацию о времени обработки по областям
 */
public class DistrictsTimeFragment extends Fragment {

    RecyclerView tableRV;
    AdapterGroupList adapter;
    LinearLayoutManager layoutManager;
    DatabaseWrapper databaseWrapper;
    Button btnDateFrom;
    Button btnDateBefore;
    Button btnShowData;
    Calendar calendar;
    int setYear;
    int setMonth;
    int setDayOfMonth;
    String dateFrom;
    String dateBefore;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.group_time_fragment, container, false);
        tableRV = view.findViewById(R.id.table_time_processed_group);
        databaseWrapper = DemoApp.dbWrapper();
        btnDateFrom = view.findViewById(R.id.btn_date_from);
        btnDateBefore = view.findViewById(R.id.btn_date_before);
        btnShowData = view.findViewById(R.id.btn_show_date);

        calendar = Calendar.getInstance();
        setYear = calendar.get(Calendar.YEAR);
        setMonth = calendar.get(Calendar.MONTH);
        setDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        dateFrom = "2020-01-01";
        dateBefore = setYear + "-" + setMonth + "-" + setDayOfMonth;

        initRecyclerView(getData(dateFrom, dateBefore));

        btnDateFrom.setOnClickListener(v -> {
            new DatePickerDialog(getContext(), dateSetListenerFrom, setYear, setMonth, setDayOfMonth).show();
        });

        btnDateBefore.setOnClickListener(v -> {
            new DatePickerDialog(getContext(), dateSetListenerBefore, setYear, setMonth, setDayOfMonth).show();
        });

        btnShowData.setOnClickListener(v -> {
            adapter.setTestGroupData(getData(dateFrom, dateBefore));
            adapter.notifyDataSetChanged();
        });


        return view;
    }

    private List<TestGroupData> getData(String dateFrom, String dateBefore) {
        List<TestGroupData> testData = new ArrayList<>();
        testData = initTable(databaseWrapper.getTypeTime(dateFrom, dateBefore));

        for (int i = 0; i < testData.size(); i++) {
            if (testData.get(i).getTime() == 0 && testData.get(i).getProcessed() == 0){
                testData.remove(i);
            }
        }
//        testData.add(initTable(databaseWrapper.getTypeTime("Банковские услуг%", date))); //
//        testData.add(initTable(databaseWrapper.getTypeTime("Ремонт зданий сооружени%", date)));
//        testData.add(initTable(databaseWrapper.getTypeTime("Возмещение стоимости ремонтных%", date)));
//        testData.add(initTable(databaseWrapper.getTypeTime("Аренда оборудовани%", date)));
//        testData.add(initTable(databaseWrapper.getTypeTime("Реклама в Интернет%", date)));
        return testData;
    }

    private void initRecyclerView(List<TestGroupData> testData) {
        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        adapter = new AdapterGroupList(getContext(), testData);
        tableRV.setLayoutManager(layoutManager);
        tableRV.setAdapter(adapter);
    }

    private List<TestGroupData> initTable(Cursor cursor) {
        List<TestGroupData> data = new ArrayList<>();
        if (cursor != null) {
            if (cursor.moveToFirst()){
                do {
                    TestGroupData testGroupData = new TestGroupData();
                    testGroupData.setTitle(cursor.getString(1));
                    testGroupData.setTime(cursor.getInt(3));
                    testGroupData.setProcessed(cursor.getInt(2));

                    if (testGroupData.getTitle() != null) {
                        data.add(testGroupData);
                    }

                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        return data;
    }

    DatePickerDialog.OnDateSetListener dateSetListenerFrom = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            setYear = year;
            setMonth = month;
            setDayOfMonth = dayOfMonth;
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = new GregorianCalendar(setYear, setMonth, setDayOfMonth);
            dateFrom = df.format(calendar.getTime());
            btnDateFrom.setTextSize(14);
            btnDateFrom.setText("От: " + dateFrom);
        }
    };


    DatePickerDialog.OnDateSetListener dateSetListenerBefore = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            setYear = year;
            setMonth = month;
            setDayOfMonth = dayOfMonth;
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = new GregorianCalendar(setYear, setMonth, setDayOfMonth);
            dateBefore = df.format(calendar.getTime());
            btnDateBefore.setTextSize(14);
            btnDateBefore.setText("До: " + dateBefore);
        }
    };

    public static DistrictsTimeFragment newInstance() {
        return new DistrictsTimeFragment();
    }
}
