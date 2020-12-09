package ru.optimum.load.magnitdemo.screen.main.details.fragments.processd;

import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
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
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import ru.optimum.load.magnitdemo.R;
import ru.optimum.load.magnitdemo.app.DemoApp;
import ru.optimum.load.magnitdemo.data.TestData;
import ru.optimum.load.magnitdemo.db.DatabaseWrapper;
import ru.optimum.load.magnitdemo.screen.adapters.AdapterDetailsList;
import ru.optimum.load.magnitdemo.screen.adapters.SpinnerAdapterPeriod;
import ru.optimum.load.magnitdemo.screen.main.details.DetailsActivity;

/*
    Фрагмент для отображения данных по областям
 */
public class DistrictsFragment extends Fragment {

    private static int dataDB;
    RecyclerView tableRV;
    AdapterDetailsList adapter;
    LinearLayoutManager layoutManager;
    Calendar calendar;
    int setYear;
    int setMonth;
    int setDayOfMonth;
    String dateFrom;
    String dateBefore;
    Button btnDateFrom;
    Button btnDateBefore;
    Button btnShowData;
    private DatabaseWrapper dbWrapper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.districts_fragment, container, false);
        tableRV = view.findViewById(R.id.table_recycler);
        btnDateFrom = view.findViewById(R.id.btn_dis_set_date_from);
        btnDateBefore = view.findViewById(R.id.btn_dis_set_date_before);
        btnShowData = view.findViewById(R.id.btn_dis_show_date);

        calendar = Calendar.getInstance();
        setYear = calendar.get(Calendar.YEAR);
        setMonth = calendar.get(Calendar.MONTH);
        setDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        dateBefore = setYear + "-" + setMonth + "-" + setDayOfMonth;
        dbWrapper = DemoApp.dbWrapper();

        initRecyclerView(getData("2020-01-01", "2020-12-31")); //инициализация RV с данными за все время

        btnDateFrom.setOnClickListener(v -> {
            new DatePickerDialog(getContext(), dateSetListenerFrom, setYear, setMonth, setDayOfMonth).show();
        });

        btnDateBefore.setOnClickListener(v -> {
            new DatePickerDialog(getContext(), dateSetListenerBefore, setYear, setMonth, setDayOfMonth).show();
        });

        btnShowData.setOnClickListener(v -> {
            adapter.setTestData(getData(dateFrom, dateBefore));
            adapter.notifyDataSetChanged();
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    //получить данные для отображения
    private List<TestData> getData(String dateFrom, String dateBefore) {
        List<TestData> testData = new ArrayList<>();
        TestData data = new TestData();
        float sla = 0;
        int processed = 0;
        int violation = 0;
        if (dataDB == 1) { // в соответствии от полученного значения выбираем таблицу из которой запрашивать данные
            //запросс данныйх из бд, вычесление значений и добавление данных в массив
            testData = getDistrictValue(dbWrapper.getValueOfDistrictOpenSet(dateFrom, dateBefore));
            for (int i = 0; i < testData.size(); i++) {
                if (testData.get(i).getTitle().contains("ГК")){
                    data.setTitle(testData.get(i).getTitle());
                    sla += testData.get(i).getSla();
                    processed += testData.get(i).getProcessed();
                    violation += testData.get(i).getViolation();
                    testData.remove(i);
                }
            }
            data.setSla(sla);
            data.setViolation(violation);
            data.setProcessed(processed);
            testData.add(data);

            for (int i = 0; i < testData.size(); i++) {
                if (testData.get(i).getTitle().contains("МОСКВА ОКРУГ")) {
                    testData.remove(i);
                }
            }
        } else if (dataDB == 2) {
            testData = getDistrictValue(dbWrapper.getValueOfDistrictProcessedSed(dateFrom, dateBefore));
            for (int i = 0; i < testData.size(); i++) {
                if (testData.get(i).getTitle().contains("ГК")){
                    data.setTitle(testData.get(i).getTitle());
                    sla += testData.get(i).getSla();
                    processed += testData.get(i).getProcessed();
                    violation += testData.get(i).getViolation();
                    testData.remove(i);
                }
            }
            data.setSla(sla);
            data.setViolation(violation);
            data.setProcessed(processed);
            testData.add(data);

            for (int i = 0; i < testData.size(); i++) {
                if (testData.get(i).getTitle().contains("МОСКВА ОКРУГ")) {
                    testData.remove(i);
                }
            }
        } else if (dataDB == 3) {
            testData = getDistrictValue(dbWrapper.getValueOfDistrictReceiptSet(dateFrom, dateBefore));
            for (int i = 0; i < testData.size(); i++) {
                if (testData.get(i).getTitle().contains("ГК")){
                    data.setTitle(testData.get(i).getTitle());
                    sla += testData.get(i).getSla();
                    processed += testData.get(i).getProcessed();
                    violation += testData.get(i).getViolation();
                    testData.remove(i);
                }
            }
            data.setSla(sla);
            data.setViolation(violation);
            data.setProcessed(processed);
            testData.add(data);

            for (int i = 0; i < testData.size(); i++) {
                if (testData.get(i).getTitle().contains("МОСКВА ОКРУГ")) {
                    testData.remove(i);
                }
            }
        }
        return testData;
    }

    //Вычесление значений
    private List<TestData> getDistrictValue( Cursor cursor) {
        List<TestData> data = new ArrayList<>();
        float count;
        float violation;

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    TestData testData = new TestData();
                    testData.setTitle(cursor.getString(1)); //достать из курсора навание области
                    count = cursor.getFloat(2); //достать из курсора сумму count
                    violation = cursor.getFloat(3);

                    testData.setProcessed((int) count);
                    testData.setViolation((int) violation);
                    if (count != 0) {
                        float valueOne = (count - violation);
                        float valueTwo = count/100;
                        testData.setSla(valueOne / valueTwo);
                    }
                    if (testData.getTitle() != null || !testData.getTitle().contains("МОСКВА ОКРУГ")) {
                        data.add(testData);
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

    //инициализайя RecyclerView
    private void initRecyclerView(List<TestData> testData) {
        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        adapter = new AdapterDetailsList(getContext(), testData);
        tableRV.setLayoutManager(layoutManager);
        tableRV.setAdapter(adapter);
    }

    public static DistrictsFragment newInstance(int data) {
        dataDB = data;
        return new DistrictsFragment();
    }
}
