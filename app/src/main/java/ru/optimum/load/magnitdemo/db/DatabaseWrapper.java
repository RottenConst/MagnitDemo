package ru.optimum.load.magnitdemo.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

import ru.optimum.load.magnitdemo.DBContact;
/*
    Класс с основными запросами к бд
 */
public class DatabaseWrapper {
    private final SQLiteDatabase db;

    public DatabaseWrapper(SQLiteDatabase db) {
        this.db = db;
    }

    //получить сумму всех значений SLA 75% из таблицы
    public int getSla75Expired(String tableName, String date) {
        Cursor cursor = db.query(tableName, new String[]{"date(STATESTARTDATE)", "sum(SLA75ExpiredCount)"}, "date(STATESTARTDATE) >= ?", new String[] {date}, null, null, null);
        int SLA75Expired = 0;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                SLA75Expired += cursor.getInt(1);
            }
            cursor.close();
        }
        return SLA75Expired;
    }

    //получить сумму всех значений SLA из таблицы
    public int getSlaExpired(String tableName, String date) {
        Cursor cursor = db.query(tableName, new String[]{"date(STATESTARTDATE)", "sum(SLAExpiredCount)"}, "date(STATESTARTDATE) >= ?", new String[] {date}, null, null, null);
        int openSLAExpired = 0;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                openSLAExpired += cursor.getInt(1);
            }
        }
        cursor.close();
        return openSLAExpired;
    }

    //получить сумму всех значений Count из таблицы
    public int getCount(String tableName, String date) {
        Cursor cursor = db.query(tableName, new String[]{"date(STATESTARTDATE)", "sum(Count)"}, "date(STATESTARTDATE) >= ?", new String[] {date}, null, null, null);
        int openCount = 0;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                openCount = cursor.getInt(1);
            }
        }
        cursor.close();
        return openCount;
    }

    //Получить сумму всех значений SlaNotExpired из таблицы ProcessedSet
    public int getSlaNotExpired(String date) {
        Cursor cursor = db.query(DBContact.ProcessedSet.TABLE_NAME, new String[]{"date("+DBContact.ProcessedSet.COLUMN_STATESTARTDATE+")", "sum("+DBContact.ProcessedSet.COLUMN_SLA_NOT_EXPIRED_COUNT+")"}, "date(STATESTARTDATE) >= ?", new String[] {date}, null, null, null);
        int openSLAExpired = 0;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                openSLAExpired = cursor.getInt(1);
            }
        }
        cursor.close();
        return openSLAExpired;
    }

    //получаем знчения по области и дате, таблицы ProcessedSet
    public Cursor getValueOfDistrictProcessedSed(String nameOfDistrict, String date) {
        String[] projection = {
                DBContact.ProcessedSet.COLUMN_STATESTARTDATE,
                DBContact.ProcessedSet.COLUMN_DISTRICT,
                "sum("+DBContact.ProcessedSet.COLUMN_COUNT+")",
                "sum("+DBContact.ProcessedSet.COLUMN_SLA_NOT_EXPIRED_COUNT+")"
        };
        String selection = "date("+DBContact.ProcessedSet.COLUMN_STATESTARTDATE+") >= ? AND " + DBContact.ProcessedSet.COLUMN_DISTRICT + " like ?";
        String[] selectionArg = {date, nameOfDistrict};
        return db.query(DBContact.ProcessedSet.TABLE_NAME, projection, selection, selectionArg, null, null, null);
    }

    //
    public Cursor getValueOfGroupProcessedSed(String nameOfDistrict, String date) {
        String[] projection = {
                DBContact.ProcessedSet.COLUMN_STATESTARTDATE,
                DBContact.ProcessedSet.COLUMN_UNIT_NAME,
                "sum("+DBContact.ProcessedSet.COLUMN_COUNT+")",
                "sum("+DBContact.ProcessedSet.COLUMN_SLA_NOT_EXPIRED_COUNT+")"
        };
        String selection = "date("+DBContact.ProcessedSet.COLUMN_STATESTARTDATE+") >= ? AND " + DBContact.ProcessedSet.COLUMN_UNIT_NAME + " like ?";
        String[] selectionArg = {date, nameOfDistrict};
        return db.query(DBContact.ProcessedSet.TABLE_NAME, projection, selection, selectionArg, null, null, null);
    }

    public Cursor getValueOfDistrictReceiptSet(String nameOfDistrict, String date) {
        String[] projection = {
                DBContact.ReceiptSet.COLUMN_STATESTARTDATE,
                DBContact.ReceiptSet.COLUMN_DISTRICT,
                "sum("+DBContact.ReceiptSet.COLUMN_COUNT+")",
                "sum("+DBContact.ReceiptSet.COLUMN_SLA_EXPIRED_COUNT+")"
        };
        String selection = "date("+DBContact.ReceiptSet.COLUMN_STATESTARTDATE+") >= ? AND " + DBContact.ReceiptSet.COLUMN_DISTRICT + " like ?";
        String[] selectionArg = {date, nameOfDistrict};
        return db.query(DBContact.ReceiptSet.TABLE_NAME, projection, selection, selectionArg, null, null, null);
    }

    public Cursor getValueOfGroupReceiptSet(String nameOfDistrict, String date) {
        String[] projection = {
                DBContact.ReceiptSet.COLUMN_STATESTARTDATE,
                DBContact.ReceiptSet.COLUMN_UNIT_NAME,
                "sum("+DBContact.ReceiptSet.COLUMN_COUNT+")",
                "sum("+DBContact.ReceiptSet.COLUMN_SLA_EXPIRED_COUNT+")"
        };
        String selection = "date("+DBContact.ReceiptSet.COLUMN_STATESTARTDATE+") >= ? AND " + DBContact.ReceiptSet.COLUMN_UNIT_NAME + " like ?";
        String[] selectionArg = {date, nameOfDistrict};
        return db.query(DBContact.ReceiptSet.TABLE_NAME, projection, selection, selectionArg, null, null, null);
    }

    public Cursor getValueOfDistrictOpenSet(String nameOfDistrict, String date) {
        String[] projection = {
                DBContact.OpenSet.COLUMN_STATESTARTDATE,
                DBContact.OpenSet.COLUMN_DISTRICT,
                "sum("+DBContact.OpenSet.COLUMN_COUNT+")",
                "sum("+DBContact.OpenSet.COLUMN_SLA_EXPIRED_COUNT+")"
        };
        String selection = "date("+DBContact.OpenSet.COLUMN_STATESTARTDATE+") >= ? AND "+ DBContact.OpenSet.COLUMN_DISTRICT + " like ?";
        String[] selectionArg = {date, nameOfDistrict};
        return db.query(DBContact.OpenSet.TABLE_NAME, projection, selection, selectionArg, null, null, null);
    }


    public Cursor getValueOfGroupOpenSet(String nameOfDistrict, String date) {
        String[] projection = {
                DBContact.OpenSet.COLUMN_STATESTARTDATE,
                DBContact.OpenSet.COLUMN_UNIT_NAME,
                "sum("+DBContact.OpenSet.COLUMN_COUNT+")",
                "sum("+DBContact.OpenSet.COLUMN_SLA_EXPIRED_COUNT+")"
        };
        String selection = "date("+DBContact.OpenSet.COLUMN_STATESTARTDATE+") >= ? AND " + DBContact.OpenSet.COLUMN_UNIT_NAME + " like ?";
        String[] selectionArg = {date, nameOfDistrict};
        return db.query(DBContact.OpenSet.TABLE_NAME, projection, selection, selectionArg, null, null, null);
    }

    public int getProcessExecution() {
        Cursor cursor = db.query(DBContact.ProcessedSet.TABLE_NAME, new String[] {"sum("+DBContact.ProcessedSet.COLUMN_PROCESS_EXECUTION+")"}, null, null,null,null,null);
        int processExecution = 0;
        if (cursor != null){
            cursor.moveToFirst();
            processExecution = cursor.getInt(0);
        }
        cursor.close();
        return processExecution;
    }

    public int getSLAPercent() {
        Cursor cursor = db.query(DBContact.OpenSet.TABLE_NAME, new String[] {"sum("+DBContact.OpenSet.COLUMN_SLA_EXPIRED_COUNT+")", "sum("+DBContact.OpenSet.COLUMN_COUNT+")"}, null, null, null, null, null);
        int notSLA = 0;
        int sla = 0;
        int percent = 0;
        if (cursor != null) {
            cursor.moveToFirst();
            notSLA = cursor.getInt(0);
            sla = cursor.getInt(1);
            if (sla != 0){
                percent = (int) ((sla - notSLA)/(sla/100));
            }
        }
        cursor.close();
        return percent;
    }

    //Получить сумму всех значений WAITIME из таблицы ProcessedSet
    public int getTotalProcessingTime() {
        Cursor cursor = db.query(DBContact.ProcessedSet.TABLE_NAME, new String[]{DBContact.ProcessedSet.COLUMN_WAIT_TIME}, null, null, null, null, null);
        int waitTime = 0;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    waitTime += cursor.getInt(0);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        cursor.close();
        return waitTime;
    }

    //Получить сумму всех значений WORKTIME из таблицы ProcessedSet
    public int getTotalWorkTime() {
        Cursor cursor = db.query(DBContact.ProcessedSet.TABLE_NAME, new String[]{DBContact.ProcessedSet.COLUMN_WORK_TIME}, null, null, null, null, null);
        int workTime = 0;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    workTime += cursor.getInt(0);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        cursor.close();
        return workTime;
    }

    public Cursor getTimeOfProcessing(){
        return db.query(DBContact.ProcessedSet.TABLE_NAME, new String[] {
                "sum("+DBContact.ProcessedSet.COLUMN_PROCESS_EXECUTION+")",
                "avg("+ DBContact.ProcessedSet.COLUMN_PROCESS_EXECUTION+")",
                "sum("+DBContact.ProcessedSet.COLUMN_WORK_TIME+")",
                "avg("+DBContact.ProcessedSet.COLUMN_WORK_TIME+")",
                "sum("+DBContact.ProcessedSet.COLUMN_WAIT_TIME+")",
                "avg("+DBContact.ProcessedSet.COLUMN_WAIT_TIME+")"}, null, null, null, null, null);
    }

    public Cursor getTimeOfProcessingWithDate(String date){
        return db.query(DBContact.ProcessedSet.TABLE_NAME, new String[] {
                "date("+DBContact.ProcessedSet.COLUMN_STATESTARTDATE+")",
                "sum("+DBContact.ProcessedSet.COLUMN_PROCESS_EXECUTION+")",
                "avg("+ DBContact.ProcessedSet.COLUMN_PROCESS_EXECUTION+")",
                "sum("+DBContact.ProcessedSet.COLUMN_WORK_TIME+")",
                "avg("+DBContact.ProcessedSet.COLUMN_WORK_TIME+")",
                "sum("+DBContact.ProcessedSet.COLUMN_WAIT_TIME+")",
                "avg("+DBContact.ProcessedSet.COLUMN_WAIT_TIME+")"}, "date("+DBContact.ProcessedSet.COLUMN_STATESTARTDATE+")" + " >= ?", new String[] {date}, null, null, null);
    }

    public Cursor getTypeTime(String nameOfType, String date) {
        return db.query(DBContact.ProcessedSet.TABLE_NAME, new String[] {
                "date("+DBContact.ProcessedSet.COLUMN_STATESTARTDATE+")",
                DBContact.ProcessedSet.COLUMN_TYPE_OF_SET,
                "sum("+DBContact.ProcessedSet.COLUMN_PROCESS_EXECUTION+")",
                "sum("+DBContact.ProcessedSet.COLUMN_PROCESS_STATE_COUNT+")"
        }, "date("+DBContact.ProcessedSet.COLUMN_STATESTARTDATE+") >= ? AND " + DBContact.ProcessedSet.COLUMN_TYPE_OF_SET + " like ?", new String[] {date, nameOfType}, null, null, null);
    }


    public Cursor getGroupTime(String nameOfGroup, String date) {
        return db.query(DBContact.ProcessedSet.TABLE_NAME, new String[] {
                "date("+DBContact.ProcessedSet.COLUMN_STATESTARTDATE+")",
                DBContact.ProcessedSet.COLUMN_UNIT_NAME,
                "sum("+DBContact.ProcessedSet.COLUMN_PROCESS_EXECUTION+")",
                "sum("+DBContact.ProcessedSet.COLUMN_PROCESS_STATE_COUNT+")"
        }, "date("+DBContact.ProcessedSet.COLUMN_STATESTARTDATE+") >= ? AND " + DBContact.ProcessedSet.COLUMN_UNIT_NAME + " like ?", new String[] {date, nameOfGroup}, null, null, null);
    }


}
