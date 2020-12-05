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
    Spinner periodSpinner;
    DatabaseWrapper databaseWrapper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.group_time_fragment, container, false);
        periodSpinner = view.findViewById(R.id.period_time_spinner_group);
        tableRV = view.findViewById(R.id.table_time_processed_group);
        databaseWrapper = DemoApp.dbWrapper();

        initRecyclerView(getData(""));

        initSpinner();
        periodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        adapter.setTestGroupData(getData("2020-00-00"));
                        adapter.notifyDataSetChanged();
                        break;
                    case 1:
                        adapter.setTestGroupData(getData("2020-10-07"));
                        adapter.notifyDataSetChanged();
                        break;
                    case 2:
                        adapter.setTestGroupData(getData("2020-10-01"));
                        adapter.notifyDataSetChanged();
                        break;
                    case 3:
                        adapter.setTestGroupData(getData("2020-08-01"));
                        adapter.notifyDataSetChanged();
                        break;
                    case 4:
                        adapter.setTestGroupData(getData("2020-06-01"));
                        adapter.notifyDataSetChanged();
                        break;
                    case 5:
                        adapter.setTestGroupData(getData("2020-01-01"));
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

    private List<TestGroupData> getData(String date) {
        List<TestGroupData> testData = new ArrayList<>();
        testData.add(initTable(databaseWrapper.getTypeTime("Банковские услуг%", date))); //
        testData.add(initTable(databaseWrapper.getTypeTime("Ремонт зданий сооружени%", date)));
        testData.add(initTable(databaseWrapper.getTypeTime("Возмещение стоимости ремонтных%", date)));
        testData.add(initTable(databaseWrapper.getTypeTime("Аренда оборудовани%", date)));
        testData.add(initTable(databaseWrapper.getTypeTime("Реклама в Интернет%", date)));
        return testData;
    }

    private void initRecyclerView(List<TestGroupData> testData) {
        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        adapter = new AdapterGroupList(getContext(), testData);
        tableRV.setLayoutManager(layoutManager);
        tableRV.setAdapter(adapter);
    }

    private TestGroupData initTable(Cursor cursor) {
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

    private void initSpinner() {
        String[] period = {"За все время", "За последние 7 дней", "За месяц", "За 3 месяца", "За 6 месяцев", "За последний год"};
        SpinnerAdapterPeriod adapterPeriod = new SpinnerAdapterPeriod(getContext(), R.layout.row_spinner, period);
        periodSpinner.setAdapter(adapterPeriod);
    }

    public static DistrictsTimeFragment newInstance() {
        return new DistrictsTimeFragment();
    }
}
