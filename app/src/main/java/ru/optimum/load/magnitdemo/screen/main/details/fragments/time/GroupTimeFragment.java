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
import ru.optimum.load.magnitdemo.db.DatabaseWrapper;
import ru.optimum.load.magnitdemo.screen.adapters.AdapterDetailsList;
import ru.optimum.load.magnitdemo.screen.adapters.AdapterGroupList;
import ru.optimum.load.magnitdemo.screen.adapters.SpinnerAdapterPeriod;

/*
    Фрагмент для отображения детальной информации о времени обработки по группам.
 */
public class GroupTimeFragment extends Fragment {

    RecyclerView tableGroupRV;
    AdapterGroupList adapter;
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
    DatabaseWrapper dbwrapper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.group_time_fragment, container, false);
        tableGroupRV = view.findViewById(R.id.table_time_processed_group);
        btnDateFrom = view.findViewById(R.id.btn_date_from);
        btnDateBefore = view.findViewById(R.id.btn_date_before);
        btnShowData = view.findViewById(R.id.btn_show_date);

        calendar = Calendar.getInstance();
        setYear = calendar.get(Calendar.YEAR);
        setMonth = calendar.get(Calendar.MONTH);
        setDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        dateFrom = "2020-01-01";
        dateBefore = setYear + "-" + setMonth + "-" + setDayOfMonth;

        dbwrapper = DemoApp.dbWrapper();

        initRV(getTestData("2020-01-01", "2020-12-31"));

        btnDateFrom.setOnClickListener(v ->
                new DatePickerDialog(getContext(), dateSetListenerFrom, setYear, setMonth, setDayOfMonth).show()
        );

        btnDateBefore.setOnClickListener(v ->
                new DatePickerDialog(getContext(), dateSetListenerBefore, setYear, setMonth, setDayOfMonth).show()
        );

        btnShowData.setOnClickListener(v -> {
            adapter.setTestGroupData(getTestData(dateFrom, dateBefore));
            adapter.notifyDataSetChanged();
        });
        return view;
    }

    private void initRV(List<TestGroupData> testData) {
        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        adapter = new AdapterGroupList(getContext(), testData);
        tableGroupRV.setLayoutManager(layoutManager);
        tableGroupRV.setAdapter(adapter);
    }

    private List<TestGroupData> getTestData(String dateFrom, String dateBefore) {
        List<TestGroupData> testData = new ArrayList<>();

        testData = initGroupData(dbwrapper.getGroupTime(dateFrom, dateBefore));
        for (int i = 0; i < testData.size(); i++) {
            if (testData.get(i).getTime() == 0 && testData.get(i).getProcessed() == 0){
                testData.remove(i);
            }
        }

//        testData.add(initGroupData(dbwrapper.getGroupTime("Сектор учета ввода в эксплуатацию оборудован%", date)));
//        testData.add(initGroupData(dbwrapper.getGroupTime("Сектор учета аренды и прочих услуг%", date)));
//        testData.add(initGroupData(dbwrapper.getGroupTime("Сектор учета оптовой реализации%", date)));
//        testData.add(initGroupData(dbwrapper.getGroupTime("Сектор документального%", date)));
//        testData.add(initGroupData(dbwrapper.getGroupTime("Сектор учета непериодических услуг%", date)));
//        testData.add(initGroupData(dbwrapper.getGroupTime("Сектор учета поступления имущества%", date)));
//        testData.add(initGroupData(dbwrapper.getGroupTime("Сектор учета расходов при импорте товара%", date)));
//        testData.add(initGroupData(dbwrapper.getGroupTime("Отдел договорного учёта%", date)));

        return testData;
    }

    private List<TestGroupData> initGroupData(Cursor cursor) {
        List<TestGroupData> data = new ArrayList<>();
        if (cursor != null) {
            cursor.moveToFirst();
            do {
                TestGroupData testGroupData = new TestGroupData();
                testGroupData.setTitle(cursor.getString(1));
                testGroupData.setTime(cursor.getInt(3));
                testGroupData.setProcessed(cursor.getInt(2));

                if (testGroupData.getTitle() != null || testGroupData.getTitle().equals("")) {
                    data.add(testGroupData);
                }
            }while (cursor.moveToNext());

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

    public static GroupTimeFragment newInstance() {
        return new GroupTimeFragment();
    }
}
