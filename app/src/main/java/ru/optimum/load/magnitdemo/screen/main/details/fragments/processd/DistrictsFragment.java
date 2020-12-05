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

import ru.optimum.load.magnitdemo.R;
import ru.optimum.load.magnitdemo.app.DemoApp;
import ru.optimum.load.magnitdemo.data.TestData;
import ru.optimum.load.magnitdemo.db.DatabaseWrapper;
import ru.optimum.load.magnitdemo.screen.adapters.AdapterDetailsList;
import ru.optimum.load.magnitdemo.screen.adapters.SpinnerAdapterPeriod;
/*
    Фрагмент для отображения данных по областям
 */
public class DistrictsFragment extends Fragment {

    private static int dataDB;
    RecyclerView tableRV;
    AdapterDetailsList adapter;
    LinearLayoutManager layoutManager;
    Spinner periodSpinner;
    private DatabaseWrapper dbWrapper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.districts_fragment, container, false);
        periodSpinner = view.findViewById(R.id.period_spiner);
        tableRV = view.findViewById(R.id.table_recycler);
        dbWrapper = DemoApp.dbWrapper();

        initRecyclerView(getData("")); //инициализация RV с данными за все время
        initSpinner();

        //отображение данных в соответствии с выбранным периодом
        periodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        adapter.setTestData(getData("")); //изменить данные для адаптера
                        adapter.notifyDataSetChanged(); //обновить данные на фрагменте
                        break;
                    case 1:
                        adapter.setTestData(getData("2020-10-07"));
                        adapter.notifyDataSetChanged();
                        break;
                    case 2:
                        adapter.setTestData(getData("2020-10-01"));
                        adapter.notifyDataSetChanged();
                        break;
                    case 3:
                        adapter.setTestData(getData("2020-08-01"));
                        adapter.notifyDataSetChanged();
                        break;
                    case 4:
                        adapter.setTestData(getData("2020-06-01"));
                        adapter.notifyDataSetChanged();
                        break;
                    case 5:
                        adapter.setTestData(getData("2020-01-01"));
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

    //получить данные для отображения
    private List<TestData> getData(String date) {
        List<TestData> testData = new ArrayList<>();
        if (dataDB == 1) { // в соответствии от полученного значения выбираем таблицу из которой запрашивать данные
            //запросс данныйх из бд, вычесление значений и добавление данных в массив
            testData.add(getDistrictValue(dbWrapper.getValueOfDistrictOpenSet("М%", date)));
            testData.add(getDistrictValue(dbWrapper.getValueOfDistrictOpenSet("ГК%", date)));
            testData.add(getDistrictValue(dbWrapper.getValueOfDistrictOpenSet("Северо-Западный%", date)));
            testData.add(getDistrictValue(dbWrapper.getValueOfDistrictOpenSet("ВОЛЖСКИЙ ОКРУГ%", date)));
            testData.add(getDistrictValue(dbWrapper.getValueOfDistrictOpenSet("РЦ Ярославл%", date)));
            testData.add(getDistrictValue(dbWrapper.getValueOfDistrictOpenSet("РЦ Вороне%", date)));
        } else if (dataDB == 2) {
            testData.add(getDistrictValue(dbWrapper.getValueOfDistrictProcessedSed("М%", date)));
            testData.add(getDistrictValue(dbWrapper.getValueOfDistrictProcessedSed("ГК%", date)));
            testData.add(getDistrictValue(dbWrapper.getValueOfDistrictProcessedSed("РЦ Вороне%", date)));
            testData.add(getDistrictValue(dbWrapper.getValueOfDistrictProcessedSed("ВОЛЖСКИЙ ОКРУГ%", date)));
            testData.add(getDistrictValue(dbWrapper.getValueOfDistrictProcessedSed("Северо-Западный%", date)));
        } else if (dataDB == 3) {
            testData.add(getDistrictValue(dbWrapper.getValueOfDistrictReceiptSet("М%", date)));
            testData.add(getDistrictValue(dbWrapper.getValueOfDistrictReceiptSet("ГК%", date)));
            testData.add(getDistrictValue(dbWrapper.getValueOfDistrictReceiptSet("РЦ Вороне%", date)));
            testData.add(getDistrictValue(dbWrapper.getValueOfDistrictReceiptSet("РЦ Ярославл%", date)));
            testData.add(getDistrictValue(dbWrapper.getValueOfDistrictReceiptSet("ВОЛЖСКИЙ ОКРУГ%", date)));
            testData.add(getDistrictValue(dbWrapper.getValueOfDistrictReceiptSet("Северо-Западный%", date)));
        }
        return testData;
    }

    //Вычесление значений
    private TestData getDistrictValue( Cursor cursor) {
        TestData data = new TestData();
        float count = 0;
        float violation = 0;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                data.setTitle(cursor.getString(1)); //достать из курсора навание области
                count = cursor.getFloat(2); //достать из курсора сумму count
                violation = cursor.getFloat(3);
            }
            data.setProcessed((int) count);
            data.setViolation((int) violation);
            if (count != 0) {
                float valueOne = (count - violation);
                float valueTwo = count/100;
                if (valueOne == valueTwo) {
                    data.setSla(100F);
                } else {
                    data.setSla(valueOne / valueTwo); //вычислить % SLA
                }
            }
        }
        cursor.close();
        return data;
    }

    //инициализайя RecyclerView
    private void initRecyclerView(List<TestData> testData) {
        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        adapter = new AdapterDetailsList(getContext(), testData);
        tableRV.setLayoutManager(layoutManager);
        tableRV.setAdapter(adapter);
    }

    //инициализация Spinner
    private void initSpinner() {
        String[] period = {"За все время", "За последние 7 дней", "За месяц", "За 3 месяца", "За 6 месяцев", "За последний год"};
        SpinnerAdapterPeriod adapterPeriod = new SpinnerAdapterPeriod(getContext(), R.layout.row_spinner, period);
        periodSpinner.setAdapter(adapterPeriod);
    }

    public static DistrictsFragment newInstance(int data) {
        dataDB = data;
        return new DistrictsFragment();
    }
}
