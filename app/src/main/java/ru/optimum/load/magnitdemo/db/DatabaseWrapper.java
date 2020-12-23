package ru.optimum.load.magnitdemo.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import static ru.optimum.load.magnitdemo.DBContact.*;

/*
    Класс с основными запросами к бд
 */
public class DatabaseWrapper {
    private final SQLiteDatabase db;

    public DatabaseWrapper(SQLiteDatabase db) {
        this.db = db;
    }

    //получить сумму всех значений SLA 75% из таблицы
    public int getSla75Expired(String tableName, String dateFrom, String dateBefore) {
        Cursor cursor = db.query(tableName, new String[]{"date(STATESTARTDATE)", "sum(SLA75ExpiredCount)"}, "date(STATESTARTDATE) BETWEEN ? AND ?", new String[] {dateFrom, dateBefore}, null, null, null);
        int SLA75Expired = 0;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                SLA75Expired += cursor.getInt(1);
            }
            cursor.close();
        }
        return SLA75Expired;
    }

    public String getMaxDate() {
        String[] projection = {
                "max(date("+ReceiptSet.COLUMN_STATESTARTDATE+"))"};
        Cursor cursor = db.query(OpenSet.TABLE_NAME, projection, null,null,null, null, null);
        String date = "";
        if (cursor != null) {
            cursor.moveToFirst();
            date = cursor.getString(0);
            cursor.close();
        }
        return date;
    }

    public String getMinDate() {
        String[] projection = {
                "min(date("+ReceiptSet.COLUMN_STATESTARTDATE+"))"};
        Cursor cursor = db.query(OpenSet.TABLE_NAME, projection, null,null,null, null, null);
        String date = "";
        if (cursor != null) {
            cursor.moveToFirst();
            date = cursor.getString(0);
            cursor.close();
        }
        return date;
    }

    public int getCountOf(String tableName, String dateFrom, String dateBefore) {
        String[] projection = {
                "sum(Count)"
        };
        String selection = "date("+ OpenSet.COLUMN_STATESTARTDATE+") BETWEEN ? AND ?";
        String[] selectionArg = {dateFrom, dateBefore};
        Cursor cursor =  db.query(tableName, projection, selection, selectionArg, null, null, null);
        int count = 0;
        if (cursor != null) {
            cursor.moveToFirst();
            count += cursor.getInt(0);
            cursor.close();
        }
        return count;
    }

    public ArrayList<Integer> getCountsOf(String tablename, String dateFrom, String dateBefore) {
        String[] projection = {
                "sum(Count)"
        };
        String selection = "date(" + OpenSet.COLUMN_STATESTARTDATE + ") BETWEEN ? AND ?";
        String[] selectionArg = {dateFrom, dateBefore};
        String groupBy = OpenSet.COLUMN_STATESTARTDATE;
        Cursor cursor = db.query(tablename, projection, selection, selectionArg, groupBy, null, null);
        ArrayList<Integer> counts = new ArrayList<>();
        if (cursor != null) {
            cursor.moveToFirst();
            do {
                counts.add(cursor.getInt(0));
            }while (cursor.moveToNext());
            cursor.close();
        }
        return counts;
    }

    public ArrayList<Integer> getCompleteCounts(String dateFrom, String dateBefore) {
        String[] projection = {
                "sum(" + ProcessedSet.COLUMN_COUNT + ")",
                "sum(" + ProcessedSet.COLUMN_PROCESS_STATE_COUNT + ")"
        };
        String selection = "date(" + ProcessedSet.COLUMN_STATESTARTDATE + ") BETWEEN ? AND ?";
        String[] selectionArg = {dateFrom, dateBefore};
        String groupBy = ProcessedSet.COLUMN_STATESTARTDATE;
        Cursor cursor = db.query(ProcessedSet.TABLE_NAME, projection, selection, selectionArg, groupBy, null, null);
        ArrayList<Integer> complete = new ArrayList<>();
        if (cursor != null) {
            cursor.moveToFirst();
            do {
                int count = cursor.getInt(0);
                int inWork = cursor.getInt(1);
                complete.add(count - inWork);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return complete;
    }

    public ArrayList<Integer> getInWorkCounts(String dateFrom, String dateBefore) {
        String[] projection = {
                "sum(" + ProcessedSet.COLUMN_PROCESS_STATE_COUNT + ")"
        };
        String selection = "date(" + ProcessedSet.COLUMN_STATESTARTDATE + ") BETWEEN ? AND ?";
        String[] selectionArg = {dateFrom, dateBefore};
        String groupBy = ProcessedSet.COLUMN_STATESTARTDATE;
        Cursor cursor = db.query(ProcessedSet.TABLE_NAME, projection, selection, selectionArg, groupBy, null, null);
        ArrayList<Integer> inWork = new ArrayList<>();
        if (cursor != null) {
            cursor.moveToFirst();
            do {
                inWork.add(cursor.getInt(0));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return inWork;
    }

    public int[] getInWorkCount(String dateFrom, String dateBefore) {
        String[] projection = {
                "sum("+ ProcessedSet.COLUMN_COUNT+")",
                "sum("+ ProcessedSet.COLUMN_PROCESS_STATE_COUNT+")"
        };
        String selection = "date("+ ProcessedSet.COLUMN_STATESTARTDATE+") BETWEEN ? AND ?";
        String[] selectionArg = {dateFrom, dateBefore};
        Cursor cursor = db.query(ProcessedSet.TABLE_NAME, projection, selection, selectionArg, null, null, null);
        int[] counts = {0, 0};
        if (cursor != null) {
            cursor.moveToFirst();
            int count = cursor.getInt(0);
            int inWork = cursor.getInt(1);
            counts[0] = count;
            counts[1] = inWork;
            cursor.close();
        }
        return counts;
    }

    //фильтры
    public List<String> getCompanyFilters() {
        String[] projections = {
                OpenSet.COLUMN_COMPANY
        };
        String groupBy = OpenSet.COLUMN_COMPANY;
        Cursor cursor = db.query(OpenSet.TABLE_NAME, projections, null, null, groupBy, null, null);
        String company;
        List<String> companies = new ArrayList<>();
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    company = cursor.getString(0);
                    companies.add(company);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return companies;
    }

    public List<String> getTypeComplect() {
        String[] projections = {
                ProcessedSet.COLUMN_TYPE_OF_SET
        };
        String groupBy = ProcessedSet.COLUMN_TYPE_OF_SET;
        Cursor cursor = db.query(ProcessedSet.TABLE_NAME, projections, null, null, groupBy, null, null);
        String typeOfCoplect;
        List<String> typesOfComplects = new ArrayList<>();
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    typeOfCoplect = cursor.getString(0);
                    if (typeOfCoplect != null) typesOfComplects.add(typeOfCoplect);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return typesOfComplects;
    }

    public List<String> getFiliateFilters() {
        String[] projections = {
                OpenSet.COLUMN_AFFILIATE
        };
        String groupBy = OpenSet.COLUMN_AFFILIATE;
        Cursor cursor = db.query(OpenSet.TABLE_NAME, projections, null, null, groupBy, null, null);
        String filiate;
        List<String> filiates = new ArrayList<>();
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    filiate = cursor.getString(0);
                    filiates.add(filiate);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return filiates;
    }

    public List<String> getSubdivision() {
        String[] projections = {
                OpenSet.COLUMN_SHOP
        };
        String groupBy = OpenSet.COLUMN_SHOP;
        Cursor cursor = db.query(OpenSet.TABLE_NAME, projections, null, null, groupBy, null, null);
        String subdivision;
        List<String> subdivisions = new ArrayList<>();
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    subdivision = cursor.getString(0).contains("Инициализационная запись") ? null : cursor.getString(0);
                    if (subdivision != null) subdivisions.add(subdivision);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return subdivisions;
    }

    public List<String> getGroupOfProcess() {
        String[] projections = {
                ProcessedSet.COLUMN_UNIT_NAME
        };
        String groupBy = ProcessedSet.COLUMN_UNIT_NAME;
        Cursor cursor = db.query(ProcessedSet.TABLE_NAME, projections, null, null, groupBy, null, null);
        String groupOfProcess;
        List<String> groupsOfProcess = new ArrayList<>();
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    groupOfProcess = cursor.getString(0);
                    if (groupOfProcess != null) groupsOfProcess.add(groupOfProcess);

                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return groupsOfProcess;
    }

    public List<String> getDistrictFilters() {
        String[] projections = {
                OpenSet.COLUMN_DISTRICT
        };
        String groupBy = OpenSet.COLUMN_DISTRICT;
        Cursor cursor = db.query(OpenSet.TABLE_NAME, projections, null, null, groupBy, null, null);
        String district;
        List<String> districts = new ArrayList<>();
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    district = cursor.getString(0);
                    districts.add(district);

                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return districts;
    }

    public ArrayList<Integer> getCountsWithFilter(String tableName, String filter, String[] filterArg) {
        String[] projections = {
                "sum(Count)"
        };
        String selection = filter;
        String[] selectionArg = filterArg;
        String groupBy = OpenSet.COLUMN_STATESTARTDATE;
        Cursor cursor = db.query(tableName, projections, selection, selectionArg, groupBy, null, null);
        ArrayList<Integer> counts = new ArrayList<>();
        if (cursor != null) {
            cursor.moveToFirst();
            do {
                if (cursor.getCount() == 0) {
                    counts.add(0);
                } else {
                    counts.add(cursor.getInt(0));
                }
            }while (cursor.moveToNext());
            cursor.close();
        }
        return counts;
    }

    public int getCountWithFilter(String tableName, String filter, String[] filterArg) {
        String[] projections = {
                "sum(Count)"
        };
        String selection = filter;
        String[] selectionArg = filterArg;
        Cursor cursor = db.query(tableName, projections, selection, selectionArg, null, null, null);
        int count = 0;
        if (cursor != null) {
            cursor.moveToFirst();
            count += cursor.getInt(0);
            cursor.close();
        }
        return count;
    }

    public int[] getInWorkCountWithFilter(String filter, String[] filterArg) {
        String[] projection = {
                "sum("+ ProcessedSet.COLUMN_COUNT+")",
                "sum("+ ProcessedSet.COLUMN_PROCESS_STATE_COUNT+")"
        };
        String selection = filter;
        String[] selectionArg = filterArg;
        Cursor cursor = db.query(ProcessedSet.TABLE_NAME, projection, selection, selectionArg, null, null, null);
        int[] counts = {0, 0};
        if (cursor != null) {
            cursor.moveToFirst();
            int count = cursor.getInt(0);
            int inWork = cursor.getInt(1);
            counts[0] = count;
            counts[1] = inWork;
            cursor.close();
        }
        return counts;
    }

    public ArrayList<Integer> getInWorkCountsWithFilter(String filter, String[] filterArg) {
        String[] projection = {
                "sum(" + ProcessedSet.COLUMN_PROCESS_STATE_COUNT + ")"
        };
        String selection = filter;
        String[] selectionArg = filterArg;
        String groupBy = ProcessedSet.COLUMN_STATESTARTDATE;
        Cursor cursor = db.query(ProcessedSet.TABLE_NAME, projection, selection, selectionArg, groupBy, null, null);
        ArrayList<Integer> inWork = new ArrayList<>();
        if (cursor != null) {
            cursor.moveToFirst();
            do {
                if (cursor.getCount() == 0) {
                    inWork.add(0);
                } else {
                    inWork.add(cursor.getInt(0));
                }
            } while (cursor.moveToNext());
            cursor.close();
        }
        return inWork;
    }

    public ArrayList<Integer> getCompleteCountsWithFilter(String filter, String[] filterArg) {
        String[] projection = {
                "sum(" + ProcessedSet.COLUMN_COUNT + ")",
                "sum(" + ProcessedSet.COLUMN_PROCESS_STATE_COUNT + ")"
        };
        String selection = filter;
        String[] selectionArg = filterArg;
        String groupBy = ProcessedSet.COLUMN_STATESTARTDATE;
        Cursor cursor = db.query(ProcessedSet.TABLE_NAME, projection, selection, selectionArg, groupBy, null, null);
        ArrayList<Integer> complete = new ArrayList<>();
        if (cursor != null) {
            cursor.moveToFirst();
            do {
                if (cursor.getCount() == 0) {
                    complete.add(0);
                } else {
                    int count = cursor.getInt(0);
                    int inWork = cursor.getInt(1);
                    complete.add(count - inWork);
                }
            } while (cursor.moveToNext());
            cursor.close();
        }
        return complete;
    }

    //получить сумму всех значений SLA из таблицы
    public int getSlaExpired(String tableName, String dateFrom, String dateBefore) {
        Cursor cursor = db.query(tableName, new String[]{"date(STATESTARTDATE)", "sum(SLAExpiredCount)"}, "date(STATESTARTDATE) BETWEEN ? AND ?", new String[] {dateFrom, dateBefore}, null, null, null);
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
    public int getCount(String tableName, String dateFrom, String dateBefore) {
        String[] projections = {
                "sum(Count)"
        };
        String selection = "date(" + OpenSet.COLUMN_STATESTARTDATE + ") BETWEEN ? AND ?";
        String[] selectionArg = {dateFrom, dateBefore};
        Cursor cursor = db.query(tableName,  projections, selection, selectionArg, null, null, null);
        int openCount = 0;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                openCount = cursor.getInt(0);
            }
        }
        cursor.close();
        return openCount;
    }

    //Получить сумму всех значений SlaNotExpired из таблицы ProcessedSet
    public int getSlaNotExpired(String dateFrom, String dateBefore) {
        Cursor cursor = db.query(ProcessedSet.TABLE_NAME, new String[]{"date("+ ProcessedSet.COLUMN_STATESTARTDATE+")", "sum("+ ProcessedSet.COLUMN_SLA_NOT_EXPIRED_COUNT+")"}, "date(STATESTARTDATE) BETWEEN ? AND ?", new String[] {dateFrom, dateBefore}, null, null, null);
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
    public Cursor getValueOfDistrictProcessedSed(String dateFrom, String dateBefore) {
        String[] projection = {
                "date("+ ProcessedSet.COLUMN_STATESTARTDATE+")",
                ProcessedSet.COLUMN_DISTRICT,
                "sum("+ ProcessedSet.COLUMN_COUNT+")",
                "sum("+ ProcessedSet.COLUMN_SLA_NOT_EXPIRED_COUNT+")"
        };
        String selection = "date("+ ProcessedSet.COLUMN_STATESTARTDATE+") BETWEEN ? AND ?";
        String[] selectionArg = {dateFrom, dateBefore};
        String groupBy = ProcessedSet.COLUMN_DISTRICT;
        return db.query(ProcessedSet.TABLE_NAME, projection, selection, selectionArg, groupBy, null, null);
    }

    //
    public Cursor getValueOfGroupProcessedSet(String dateFrom, String dateBefore) {
        String[] projection = {
                "date("+ ProcessedSet.COLUMN_STATESTARTDATE+")",
                ProcessedSet.COLUMN_UNIT_NAME,
                "sum("+ ProcessedSet.COLUMN_COUNT+")",
                "sum("+ ProcessedSet.COLUMN_SLA_NOT_EXPIRED_COUNT+")"
        };
        String selection = "date("+ ProcessedSet.COLUMN_STATESTARTDATE+") BETWEEN ? AND ?";
        String[] selectionArg = {dateFrom, dateBefore};
        String groupBy = ProcessedSet.COLUMN_UNIT_NAME;
        return db.query(ProcessedSet.TABLE_NAME, projection, selection, selectionArg, groupBy, null, null);
    }

    public Cursor getValueOfDistrictReceiptSet(String dateFrom , String dateBefore) {
        String[] projection = {
                "date("+ ReceiptSet.COLUMN_STATESTARTDATE+")",
                ReceiptSet.COLUMN_DISTRICT,
                "sum("+ ReceiptSet.COLUMN_COUNT+")",
                "sum("+ ReceiptSet.COLUMN_SLA_EXPIRED_COUNT+")"
        };
        String selection = "date("+ ReceiptSet.COLUMN_STATESTARTDATE+") BETWEEN ? AND ?";
        String[] selectionArg = {dateFrom, dateBefore};
        String groupBy = ReceiptSet.COLUMN_DISTRICT;
        return db.query(ReceiptSet.TABLE_NAME, projection, selection, selectionArg, groupBy, null, null);
    }

    public Cursor getValueOfGroupReceiptSet(String dateFrom, String dateBefore) {
        String[] projection = {
                "date("+ ReceiptSet.COLUMN_STATESTARTDATE+")",
                ReceiptSet.COLUMN_UNIT_NAME,
                "sum("+ ReceiptSet.COLUMN_COUNT+")",
                "sum("+ ReceiptSet.COLUMN_SLA_EXPIRED_COUNT+")"
        };
        String selection = "date("+ ReceiptSet.COLUMN_STATESTARTDATE+") BETWEEN ? AND ?";
        String[] selectionArg = {dateFrom, dateBefore};
        String groupBy = ReceiptSet.COLUMN_UNIT_NAME;
        return db.query(ReceiptSet.TABLE_NAME, projection, selection, selectionArg, groupBy, null, null);
    }

    public Cursor getValueOfDistrictOpenSet(String dateFrom, String dateBefore) {
        String[] projection = {
                "date("+ OpenSet.COLUMN_STATESTARTDATE+")",
                OpenSet.COLUMN_DISTRICT,
                "sum("+ OpenSet.COLUMN_COUNT+")",
                "sum("+ OpenSet.COLUMN_SLA_EXPIRED_COUNT+")"
        };
        String selection = "date("+ OpenSet.COLUMN_STATESTARTDATE+") BETWEEN ? AND ?";
        String[] selectionArg = {dateFrom, dateBefore};
        String groupBy = OpenSet.COLUMN_DISTRICT;
        return db.query(OpenSet.TABLE_NAME, projection, selection, selectionArg, groupBy, null, null);
    }

    public Cursor getValueOfGroupOpenSet(String dateFrom, String dateBefore) {
        String[] projection = {
                "date("+ OpenSet.COLUMN_STATESTARTDATE+")",
                OpenSet.COLUMN_UNIT_NAME,
                "sum("+ OpenSet.COLUMN_COUNT+")",
                "sum("+ OpenSet.COLUMN_SLA_EXPIRED_COUNT+")"
        };
        String selection = "date("+ OpenSet.COLUMN_STATESTARTDATE+") >= ? AND date("+ OpenSet.COLUMN_STATESTARTDATE+") <= ?";
        String[] selectionArg = {dateFrom, dateBefore};
        String groupBy = OpenSet.COLUMN_UNIT_NAME;
        return db.query(OpenSet.TABLE_NAME, projection, selection, selectionArg, groupBy, null, null);
    }

    public int getProcessExecution() {
        Cursor cursor = db.query(ProcessedSet.TABLE_NAME, new String[] {"sum("+ ProcessedSet.COLUMN_PROCESS_EXECUTION+")"}, null, null,null,null,null);
        int processExecution = 0;
        if (cursor != null){
            cursor.moveToFirst();
            processExecution = cursor.getInt(0);
        }
        cursor.close();
        return processExecution;
    }

    public int getSLAPercent() {
        Cursor cursor = db.query(OpenSet.TABLE_NAME, new String[] {"sum("+ OpenSet.COLUMN_SLA_EXPIRED_COUNT+")", "sum("+ OpenSet.COLUMN_COUNT+")"}, null, null, null, null, null);
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
        Cursor cursor = db.query(ProcessedSet.TABLE_NAME, new String[]{ProcessedSet.COLUMN_WAIT_TIME}, null, null, null, null, null);
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
        Cursor cursor = db.query(ProcessedSet.TABLE_NAME, new String[]{ProcessedSet.COLUMN_WORK_TIME}, null, null, null, null, null);
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
        return db.query(ProcessedSet.TABLE_NAME, new String[] {
                "sum("+ ProcessedSet.COLUMN_PROCESS_EXECUTION+")",
                "avg("+ ProcessedSet.COLUMN_PROCESS_EXECUTION+")",
                "sum("+ ProcessedSet.COLUMN_WORK_TIME+")",
                "avg("+ ProcessedSet.COLUMN_WORK_TIME+")",
                "sum("+ ProcessedSet.COLUMN_WAIT_TIME+")",
                "avg("+ ProcessedSet.COLUMN_WAIT_TIME+")"}, null, null, null, null, null);
    }

    public Cursor getTimeOfProcessingWithDate(String dateFrom, String dateBefore){
        return db.query(ProcessedSet.TABLE_NAME, new String[] {
                "date("+ ProcessedSet.COLUMN_STATESTARTDATE+")",
                "sum("+ ProcessedSet.COLUMN_PROCESS_EXECUTION+")",
                "avg("+ ProcessedSet.COLUMN_PROCESS_EXECUTION+")",
                "sum("+ ProcessedSet.COLUMN_WORK_TIME+")",
                "avg("+ ProcessedSet.COLUMN_WORK_TIME+")",
                "sum("+ ProcessedSet.COLUMN_WAIT_TIME+")",
                "avg("+ ProcessedSet.COLUMN_WAIT_TIME+")"}, "date("+ ProcessedSet.COLUMN_STATESTARTDATE+")" + " BETWEEN ? AND ?", new String[] {dateFrom, dateBefore}, null, null, null);
    }

    public Cursor getTypeTime(String dateFrom, String dateBefore) {
        return db.query(ProcessedSet.TABLE_NAME, new String[] {
                "date("+ ProcessedSet.COLUMN_STATESTARTDATE+")",
                ProcessedSet.COLUMN_TYPE_OF_SET,
                "sum("+ ProcessedSet.COLUMN_PROCESS_EXECUTION+")",
                "sum("+ ProcessedSet.COLUMN_PROCESS_STATE_COUNT+")"
        }, "date("+ ProcessedSet.COLUMN_STATESTARTDATE+") BETWEEN ? AND ?", new String[] {dateFrom, dateBefore}, ProcessedSet.COLUMN_TYPE_OF_SET, null, null);
    }


    public Cursor getGroupTime(String dateFrom, String dateBefore) {
        return db.query(ProcessedSet.TABLE_NAME, new String[] {
                "date("+ ProcessedSet.COLUMN_STATESTARTDATE+")",
                ProcessedSet.COLUMN_UNIT_NAME,
                "sum("+ ProcessedSet.COLUMN_PROCESS_EXECUTION+")",
                "sum("+ ProcessedSet.COLUMN_PROCESS_STATE_COUNT+")"
        }, "date("+ ProcessedSet.COLUMN_STATESTARTDATE+") BETWEEN ? AND ?", new String[] {dateFrom, dateBefore}, ProcessedSet.COLUMN_UNIT_NAME, null, null);
    }


}
