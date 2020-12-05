package ru.optimum.load.magnitdemo.app;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.FileUtils;

import androidx.annotation.NonNull;

import ru.cdc.optimum.db.DbOpenHelper;
/*
 Класс для помощи управения бд
 */
public class OptimumOpenHelper extends DbOpenHelper {
    private static final String DATABASE_STRUCTURE_FILE = "PilotMobileRepDB2_1.xml";
    private static final String LOCAL_DATABASE_NAME = "mobile";

    public OptimumOpenHelper(@NonNull Context context) {
        super(context, DATABASE_STRUCTURE_FILE, LOCAL_DATABASE_NAME, null, 1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);
    }

}
