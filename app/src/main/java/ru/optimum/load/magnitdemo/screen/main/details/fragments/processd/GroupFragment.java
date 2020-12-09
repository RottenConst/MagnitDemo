package ru.optimum.load.magnitdemo.screen.main.details.fragments.processd;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

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
import ru.optimum.load.magnitdemo.db.DatabaseWrapper;
import ru.optimum.load.magnitdemo.screen.adapters.AdapterDetailsList;

public class GroupFragment extends Fragment {

    private static int dataDB;
    RecyclerView tableGroupRV;
    AdapterDetailsList adapter;
    LinearLayoutManager layoutManager;
    Calendar calendar;
    int setYear;
    int setMonth;
    int setDayOfMonth;
    String dateFrom;
    String dateBefore;
    Button btnSetDateFrom;
    Button btnSetDateBefore;
    Button btnShowData;
    private DatabaseWrapper dbWrapper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.group_fragment, container, false);
        tableGroupRV = view.findViewById(R.id.table_recycler_group);
        btnSetDateFrom = view.findViewById(R.id.btn_group_set_date_from);
        btnSetDateBefore = view.findViewById(R.id.btn_group_set_date_before);
        btnShowData = view.findViewById(R.id.btn_group_show_data);

        calendar = Calendar.getInstance();
        setYear = calendar.get(Calendar.YEAR);
        setMonth = calendar.get(Calendar.MONTH);
        setDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        dateFrom = "2020-01-01";
        dateBefore = setYear + "-" + setMonth + "-" + setDayOfMonth;
        dbWrapper = DemoApp.dbWrapper();

        initRV(getTestData("2020-01-01", "2020-12-31"));


        btnSetDateFrom.setOnClickListener(v -> {
            new DatePickerDialog(getContext(), dateSetListenerFrom, setYear, setMonth, setDayOfMonth).show();
        });

        btnSetDateBefore.setOnClickListener(v -> {
            new DatePickerDialog(getContext(), DateSetListenerTwo, setYear, setMonth, setDayOfMonth).show();
        });

        btnShowData.setOnClickListener(v -> {
            adapter.setTestData(getTestData(dateFrom, dateBefore));
            adapter.notifyDataSetChanged();
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void initRV(List<TestData> testData) {
        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        adapter = new AdapterDetailsList(getContext(), testData);
        tableGroupRV.setLayoutManager(layoutManager);
        tableGroupRV.setAdapter(adapter);
    }

    private List<TestData> getTestData(String dateFrom, String dateBefore){
        List<TestData> testData = new ArrayList<>();
        if (dataDB == 1) {
            testData = getGroupValue(dbWrapper.getValueOfGroupOpenSet(dateFrom, dateBefore));
        } else if (dataDB == 2) {
            testData = getGroupValue(dbWrapper.getValueOfGroupProcessedSet(dateFrom, dateBefore));
        } else if (dataDB == 3) {
            testData = getGroupValue(dbWrapper.getValueOfGroupReceiptSet(dateFrom, dateBefore));
        }
        return testData;
    }

    private List<TestData> getGroupValue(Cursor cursor) {
        List<TestData> data = new ArrayList<>();
        float count;
        float violation;

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    TestData testData = new TestData();
                    testData.setTitle(cursor.getString(1));
                    count = cursor.getFloat(2);
                    violation = cursor.getFloat(3);

                    testData.setProcessed((int) count);
                    testData.setViolation((int) violation);
                    if (count != 0) {
                        float valueOne = (count - violation);
                        float valueTwo = count/100;
                        testData.setSla(valueOne / valueTwo);
                    }
                    if (testData.getTitle() != null) {
                        data.add(testData);
                    }
                    Log.d("DATE", cursor.getString(1) + " " + cursor.getString(2) + " " + cursor.getString(3));
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
            btnSetDateFrom.setTextSize(14);
            btnSetDateFrom.setText("От: " + dateFrom);
        }
    };

    DatePickerDialog.OnDateSetListener DateSetListenerTwo = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            setYear = year;
            setMonth = month;
            setDayOfMonth = dayOfMonth;
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = new GregorianCalendar(setYear, setMonth, setDayOfMonth);
            dateBefore = df.format(calendar.getTime());
            btnSetDateBefore.setTextSize(14);
            btnSetDateBefore.setText("До: " + dateBefore);
        }
    };

    public static GroupFragment newInstance(int data) {
        dataDB = data;
        return new GroupFragment();
    }
}
