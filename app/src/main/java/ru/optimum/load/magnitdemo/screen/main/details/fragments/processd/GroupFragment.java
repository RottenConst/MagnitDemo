package ru.optimum.load.magnitdemo.screen.main.details.fragments.processd;

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

import ru.optimum.load.magnitdemo.DBContact;
import ru.optimum.load.magnitdemo.R;
import ru.optimum.load.magnitdemo.app.DemoApp;
import ru.optimum.load.magnitdemo.data.DataOnCardView;
import ru.optimum.load.magnitdemo.data.TestData;
import ru.optimum.load.magnitdemo.db.DatabaseWrapper;
import ru.optimum.load.magnitdemo.screen.adapters.AdapterDetailsList;
import ru.optimum.load.magnitdemo.screen.adapters.SpinnerAdapterPeriod;

public class GroupFragment extends Fragment {

    private static int dataDB;
    RecyclerView tableGroupRV;
    AdapterDetailsList adapter;
    LinearLayoutManager layoutManager;
    Spinner periodGroup;
    private DatabaseWrapper dbWrapper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.group_fragment, container, false);
        tableGroupRV = view.findViewById(R.id.table_recycler_group);
        periodGroup = view.findViewById(R.id.period_spinner_group);
        dbWrapper = DemoApp.dbWrapper();
        initSpinner();

        initRV(getTestData(""));

        periodGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        adapter.setTestData(getTestData(""));
                        adapter.notifyDataSetChanged();
                        break;
                    case 1:
                        adapter.setTestData(getTestData("2020-10-07"));
                        adapter.notifyDataSetChanged();
                        break;
                    case 2:
                        adapter.setTestData(getTestData("2020-10-01"));
                        adapter.notifyDataSetChanged();
                        break;
                    case 3:
                        adapter.setTestData(getTestData("2020-08-01"));
                        adapter.notifyDataSetChanged();
                        break;
                    case 4:
                        adapter.setTestData(getTestData("2020-06-01"));
                        adapter.notifyDataSetChanged();
                        break;
                    case 5:
                        adapter.setTestData(getTestData("2020-01-01"));
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

    private void initSpinner() {
        String[] period = {"За все время", "За последние 7 дней", "За месяц", "За 3 месяца", "За 6 месяцев", "За последний год"};
        SpinnerAdapterPeriod adapterPeriod = new SpinnerAdapterPeriod(getContext(), R.layout.row_spinner, period);
        periodGroup.setAdapter(adapterPeriod);
    }

    private List<TestData> getTestData(String date){
        List<TestData> testData = new ArrayList<>();
        if (dataDB == 1) {
            testData.add(getGroupValue(dbWrapper.getValueOfGroupOpenSet("Сектор учета поступления%", date))); //запрос из бд 
            testData.add(getGroupValue(dbWrapper.getValueOfGroupOpenSet("Сектор учета аренды%", date)));
            testData.add(getGroupValue(dbWrapper.getValueOfGroupOpenSet("Сектор учета оптовой%", date)));
            testData.add(getGroupValue(dbWrapper.getValueOfGroupOpenSet("Сектор учета непериодических услуг%", date)));
            testData.add(getGroupValue(dbWrapper.getValueOfGroupOpenSet("Сектор учета ввода%", date)));
            testData.add(getGroupValue(dbWrapper.getValueOfGroupOpenSet("Сектор учета банковских%", date)));
            testData.add(getGroupValue(dbWrapper.getValueOfGroupOpenSet("Сектор документального%", date)));
            testData.add(getGroupValue(dbWrapper.getValueOfGroupOpenSet("Сектор централизованных%", date)));
            testData.add(getGroupValue(dbWrapper.getValueOfGroupOpenSet("Сектор учета периодических%", date)));
        } else if (dataDB == 2) {
            testData.add(getGroupValue(dbWrapper.getValueOfGroupProcessedSed("Сектор учета аренды%", date)));
            testData.add(getGroupValue(dbWrapper.getValueOfGroupProcessedSed("Сектор учета оптовой%", date)));
            testData.add(getGroupValue(dbWrapper.getValueOfGroupProcessedSed("Сектор учета непериодических услуг%", date)));
            testData.add(getGroupValue(dbWrapper.getValueOfGroupProcessedSed("Сектор документального%", date)));
            testData.add(getGroupValue(dbWrapper.getValueOfGroupProcessedSed("Сектор учета банковских%", date)));
            testData.add(getGroupValue(dbWrapper.getValueOfGroupProcessedSed("Сектор учета периодических%", date)));
            testData.add(getGroupValue(dbWrapper.getValueOfGroupProcessedSed("Сектор учета поступления%", date)));
            testData.add(getGroupValue(dbWrapper.getValueOfGroupProcessedSed("Сектор централизованных%", date)));
        } else if (dataDB == 3) {
            testData.add(getGroupValue(dbWrapper.getValueOfGroupReceiptSet("Сектор учета аренды%", date)));
            testData.add(getGroupValue(dbWrapper.getValueOfGroupReceiptSet("Сектор учета оптовой%", date)));
            testData.add(getGroupValue(dbWrapper.getValueOfGroupReceiptSet("Сектор учета непериодических услуг%", date)));
            testData.add(getGroupValue(dbWrapper.getValueOfGroupReceiptSet("Сектор учета ввода%", date)));
            testData.add(getGroupValue(dbWrapper.getValueOfGroupReceiptSet("Сектор документального%", date)));
            testData.add(getGroupValue(dbWrapper.getValueOfGroupReceiptSet("Сектор учета периодических%", date)));
            testData.add(getGroupValue(dbWrapper.getValueOfGroupReceiptSet("Сектор учета банковских%", date)));
            testData.add(getGroupValue(dbWrapper.getValueOfGroupReceiptSet("Сектор централизованных%", date)));
            testData.add(getGroupValue(dbWrapper.getValueOfGroupReceiptSet("Сектор учета поступления%", date)));
        }
        return testData;
    }

    private TestData getGroupValue(Cursor cursor) {
        TestData data = new TestData();
        float count = 0;
        float violation = 0;


        if (cursor != null) {
            if (cursor.moveToFirst()) {
                data.setTitle(cursor.getString(1));
                count = cursor.getFloat(2);
                violation = cursor.getFloat(3);
            }
            data.setProcessed((int) count);
            data.setViolation((int) violation);
            if (count != 0) {
                float valueOne = (count - violation);
                float valueTwo = count/100;
                data.setSla(valueOne / valueTwo);
            }
        }
        cursor.close();
        return data;
    }

    public static GroupFragment newInstance(int data) {
        dataDB = data;
        return new GroupFragment();
    }
}
