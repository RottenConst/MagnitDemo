package ru.optimum.load.magnitdemo.screen.main.details.fragments.time;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
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
    Spinner periodGroup;
    DatabaseWrapper dbwrapper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.group_time_fragment, container, false);
        tableGroupRV = view.findViewById(R.id.table_time_processed_group);
        periodGroup = view.findViewById(R.id.period_time_spinner_group);
        dbwrapper = DemoApp.dbWrapper();

        initSpinner();
        initRV(getTestData("2020-00-00"));

        periodGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        adapter.setTestGroupData(getTestData("2020-00-00"));
                        adapter.notifyDataSetChanged();
                        break;
                    case 1:
                        adapter.setTestGroupData(getTestData("2020-10-07"));
                        adapter.notifyDataSetChanged();
                        break;
                    case 2:
                        adapter.setTestGroupData(getTestData("2020-10-01"));
                        adapter.notifyDataSetChanged();
                        break;
                    case 3:
                        adapter.setTestGroupData(getTestData("2020-08-01"));
                        adapter.notifyDataSetChanged();
                        break;
                    case 4:
                        adapter.setTestGroupData(getTestData("2020-06-01"));
                        adapter.notifyDataSetChanged();
                        break;
                    case 5:
                        adapter.setTestGroupData(getTestData("2020-01-01"));
                        adapter.notifyDataSetChanged();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
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

    private void initSpinner() {
        String[] period = {"За все время", "За последние 7 дней", "За месяц", "За 3 месяца", "За 6 месяцев", "За последний год"};
        SpinnerAdapterPeriod adapterPeriod = new SpinnerAdapterPeriod(getContext(), R.layout.row_spinner, period);
        periodGroup.setAdapter(adapterPeriod);
    }

    private List<TestGroupData> getTestData(String date) {
        List<TestGroupData> testData = new ArrayList<>();

        testData.add(initGroupData(dbwrapper.getGroupTime("Сектор учета ввода в эксплуатацию оборудован%", date)));
        testData.add(initGroupData(dbwrapper.getGroupTime("Сектор учета аренды и прочих услуг%", date)));
        testData.add(initGroupData(dbwrapper.getGroupTime("Сектор учета оптовой реализации%", date)));
        testData.add(initGroupData(dbwrapper.getGroupTime("Сектор документального%", date)));
        testData.add(initGroupData(dbwrapper.getGroupTime("Сектор учета непериодических услуг%", date)));
        testData.add(initGroupData(dbwrapper.getGroupTime("Сектор учета поступления имущества%", date)));
        testData.add(initGroupData(dbwrapper.getGroupTime("Сектор учета расходов при импорте товара%", date)));
        testData.add(initGroupData(dbwrapper.getGroupTime("Отдел договорного учёта%", date)));

        return testData;
    }

    private TestGroupData initGroupData(Cursor cursor) {
        TestGroupData data = new TestGroupData();
        if (cursor != null) {
            cursor.moveToFirst();
            data.setTitle(cursor.getString(1));
            data.setTime(cursor.getInt(3));
            data.setProcessed(cursor.getInt(2));
        }
        cursor.close();
        return data;
    }

    public static GroupTimeFragment newInstance() {
        return new GroupTimeFragment();
    }
}
