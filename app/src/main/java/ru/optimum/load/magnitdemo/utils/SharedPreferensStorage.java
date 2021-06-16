package ru.optimum.load.magnitdemo.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.List;

import ru.optimum.load.magnitdemo.data.Filter;

public class SharedPreferensStorage {

    public static final String STORAGE_NAME = "DriverData";

    private static SharedPreferences settings = null;
    private static SharedPreferences.Editor editor = null;
    private static Context context = null;

    //инициализация SharedPreferences
    public static void init( Context cntxt ){
        context = cntxt;
    }

    private static void init(){
        settings = context.getSharedPreferences(STORAGE_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();
        editor.apply();
    }

    public static void addFilters(String name, Filter filter) {
        if( settings == null ){
            init();
        }
        editor.putInt(name + "sizet", filter.getTitle().length);
        editor.putInt(name + "sizec", filter.getCheck().length);
        editor.putString(name + "category", filter.getCategory());
        for (int i = 0; i < filter.getTitle().length; i++) {
            editor.putString(name + "title" + i, filter.getTitle()[i]);
        }
        for (int i = 0; i < filter.getCheck().length; i++) {
            editor.putBoolean(name + "chek" + i, filter.getCheck()[i]);
        }
        editor.apply();
    }

    public static Filter getFilter(String name) {
        if( settings == null ){
            init();
        }
        int sizet = settings.getInt(name + "sizet", 0);
        int sizec = settings.getInt(name + "sizec", 0);
        String category = settings.getString(name + "category", "");
        String[] title = new String[sizet];
        boolean[] check = new boolean[sizec];
        for (int i = 0; i < sizet; i++) {
            title[i] = settings.getString(name + "title" + i, "");
        }
        for (int i = 0; i < sizec; i++) {
            check[i] = settings.getBoolean(name + "chek" + i, false);
        }
        return new Filter(category, title, check);
    }

    public static void addListFilterArg(String name, String[] filterArg) {
        if( settings == null ){
            init();
        }
        editor.putInt(name, filterArg.length);
        for (int i = 0; i < filterArg.length; i++) {
            editor.putString(name + i, filterArg[i]);
        }
        editor.apply();
    }

    public static String[] getListString(String name) {
        if( settings == null ){
            init();
        }
        int size = settings.getInt(name, 0);
        String[] filtersArg = new String[size];
        for (int i = 0; i < size; i++) {
            filtersArg[i] = settings.getString(name + i, "");
        }
        return filtersArg;
    }

    //очистка всего кэша
    public static void clearAll(){
        if( settings == null ){
            init();
        }
        editor.clear();
        editor.apply();
    }
}
