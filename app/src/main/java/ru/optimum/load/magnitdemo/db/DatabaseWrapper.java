package ru.optimum.load.magnitdemo.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.util.Pair;

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.List;

import ru.optimum.load.magnitdemo.data.Report;

import static ru.optimum.load.magnitdemo.DBContact.*;

/*
    Класс с основными запросами к бд
 */
public class DatabaseWrapper {
    private final SQLiteDatabase db;

    public DatabaseWrapper(SQLiteDatabase db) {
        this.db = db;
    }

    public String getMaxDate() {
        String[] projection = {
                "date("+OpenSet.COLUMN_STATESTARTDATE+")"};
        Cursor cursor = db.query(OpenSet.TABLE_NAME, projection, null,null,null, null, null);
        String date = "";
        if (cursor != null) {
            cursor.moveToLast();
            date = cursor.getString(0);
            cursor.close();
        }
        return date;
    }

    public String getMinDate() {
        String[] projection = {
                "date("+ReceiptSet.COLUMN_STATESTARTDATE+")"};
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

    public int getSlaNotExpired(String dateFrom, String dateBefore) {
        String[] projection = {
                "sum(" + ProcessedSet.COLUMN_SLA_NOT_EXPIRED_COUNT + ")"
        };
        String selection = "date(" + ProcessedSet.COLUMN_STATESTARTDATE + ") BETWEEN ? AND ?";
        String[] selectionArg = {dateFrom, dateBefore};
        Cursor cursor = db.query(ProcessedSet.TABLE_NAME, projection, selection, selectionArg, null, null, null);
        int slaCount = 0;
        if (cursor != null) {
            cursor.moveToFirst();
            slaCount += cursor.getInt(0);
            cursor.close();
        }
        return slaCount;
    }

    public int getSla75Expired(String dateFrom, String dateBefore) {
        String[] projection = {
                "sum(" + ProcessedSet.COLUMN_SLA75_EXPIRED_COUNT + ")"
        };
        String selection = "date(" + ProcessedSet.COLUMN_STATESTARTDATE + ") BETWEEN ? AND ?";
        String[] selectionArg = {dateFrom, dateBefore};
        Cursor cursor = db.query(OpenSet.TABLE_NAME, projection, selection, selectionArg, null, null, null);
        int slaCount = 0;
        if (cursor != null) {
            cursor.moveToFirst();
            slaCount += cursor.getInt(0);
            cursor.close();
        }
        return slaCount;
    }

    public List<Pair<String, Integer>> getCountsOf(String tablename, String dateFrom, String dateBefore) {
        String[] projection = {
                "date(" + OpenSet.COLUMN_STATESTARTDATE + ")",
                "sum(Count)"
        };
        String selection = "date(" + OpenSet.COLUMN_STATESTARTDATE + ") BETWEEN ? AND ?";
        String[] selectionArg = {dateFrom, dateBefore};
        String groupBy = OpenSet.COLUMN_STATESTARTDATE;
        Cursor cursor = db.query(tablename, projection, selection, selectionArg, groupBy, null, null);
        List<Pair<String, Integer>> counts = new ArrayList<>();
        if (cursor != null) {
            cursor.moveToFirst();
            do {
                if (cursor.getCount() == 0) {
                    counts.add(new Pair<>("", 0));
                } else {
                    counts.add(new Pair<>(cursor.getString(0), cursor.getInt(1)));
                }
            }while (cursor.moveToNext());
            cursor.close();
        }
        return counts;
    }

    public List<Pair<String, Integer>> getCompleteCounts(String dateFrom, String dateBefore) {
        String[] projection = {
                "date(" + ProcessedSet.COLUMN_STATESTARTDATE +")",
                "sum(" + ProcessedSet.COLUMN_COUNT + ")",
                "sum(" + ProcessedSet.COLUMN_PROCESS_STATE_COUNT + ")"
        };
        String selection = "date(" + ProcessedSet.COLUMN_STATESTARTDATE + ") BETWEEN ? AND ?";
        String[] selectionArg = {dateFrom, dateBefore};
        String groupBy = ProcessedSet.COLUMN_STATESTARTDATE;
        Cursor cursor = db.query(ProcessedSet.TABLE_NAME, projection, selection, selectionArg, groupBy, null, null);
        List<Pair<String, Integer>> complete = new ArrayList<>();
        if (cursor != null) {
            cursor.moveToFirst();
            do {
                if (cursor.getCount() == 0) {
                    complete.add(new Pair<>("", 0));
                } else {
                    int count = cursor.getInt(1);
                    int inWork = cursor.getInt(2);
                    complete.add(new Pair<>(cursor.getString(0), count - inWork));
                }
            } while (cursor.moveToNext());
            cursor.close();
        }
        return complete;
    }

    public List<Pair<String, Integer>> getInWorkCounts(String dateFrom, String dateBefore) {
        String[] projection = {
                "date(" + ProcessedSet.COLUMN_STATESTARTDATE + ")",
                "sum(" + ProcessedSet.COLUMN_PROCESS_STATE_COUNT + ")"
        };
        String selection = "date(" + ProcessedSet.COLUMN_STATESTARTDATE + ") BETWEEN ? AND ?";
        String[] selectionArg = {dateFrom, dateBefore};
        String groupBy = ProcessedSet.COLUMN_STATESTARTDATE;
        Cursor cursor = db.query(ProcessedSet.TABLE_NAME, projection, selection, selectionArg, groupBy, null, null);
        List<Pair<String,Integer>> inWork = new ArrayList<>();
        if (cursor != null) {
            cursor.moveToFirst();
            do {
                if (cursor.getCount() == 0) {
                    inWork.add(new Pair<>("", 0));
                } else {
                    inWork.add(new Pair<>(cursor.getString(0), cursor.getInt(1)));
                }
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

    public int getSlaWithFilter(String filter, String[] filterArg) {
        String[] projections = {
                "sum(" + ProcessedSet.COLUMN_SLA_NOT_EXPIRED_COUNT + ")"
        };
        String selection = filter;
        String[] selectArg = filterArg;
        Cursor cursor = db.query(ProcessedSet.TABLE_NAME, projections, selection, selectArg, null, null, null);
        int slaCount = 0;
        if (cursor != null) {
            cursor.moveToFirst();
            slaCount += cursor.getInt(0);
            cursor.close();
        }
        return slaCount;
    }

    public int getSlaExpiredWithFilter(String filter, String[] filterArg) {
        String[] projections = {
                "sum(" + ProcessedSet.COLUMN_SLA75_EXPIRED_COUNT + ")"
        };
        String selection = filter;
        String[] selectArg = filterArg;
        Cursor cursor = db.query(OpenSet.TABLE_NAME, projections, selection, selectArg, null, null, null);
        int slaCount = 0;
        if (cursor != null) {
            cursor.moveToFirst();
            slaCount += cursor.getInt(0);
            cursor.close();
        }
        return slaCount;
    }

    public List<Pair<String,Integer>> getCountsWithFilter(String tableName, String filter, String[] filterArg) {
        String[] projections = {
                "date(" + ProcessedSet.COLUMN_STATESTARTDATE + ")",
                "sum(Count)"
        };
        String selection = filter;
        String[] selectionArg = filterArg;
        String groupBy = OpenSet.COLUMN_STATESTARTDATE;
        Cursor cursor = db.query(tableName, projections, selection, selectionArg, groupBy, null, null);
        List<Pair<String,Integer>> counts = new ArrayList<>();
        if (cursor != null) {
            cursor.moveToFirst();
            do {
                if (cursor.getCount() == 0) {
                    counts.add(new Pair<>("",0));
                } else {
                    counts.add(new Pair<>(cursor.getString(0), cursor.getInt(1)));
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

    public List<Pair<String,Integer>> getInWorkCountsWithFilter(String filter, String[] filterArg) {
        String[] projection = {
                "date(" + ProcessedSet.COLUMN_STATESTARTDATE + ")",
                "sum(" + ProcessedSet.COLUMN_PROCESS_STATE_COUNT + ")"
        };
        String selection = filter;
        String[] selectionArg = filterArg;
        String groupBy = ProcessedSet.COLUMN_STATESTARTDATE;
        Cursor cursor = db.query(ProcessedSet.TABLE_NAME, projection, selection, selectionArg, groupBy, null, null);
        List<Pair<String,Integer>> inWork = new ArrayList<>();
        if (cursor != null) {
            cursor.moveToFirst();
            do {
                if (cursor.getCount() == 0) {
                    inWork.add(new Pair<>("",0));
                } else {
                    inWork.add(new Pair<>(cursor.getString(0), cursor.getInt(0)));
                }
            } while (cursor.moveToNext());
            cursor.close();
        }
        return inWork;
    }

    public List<Pair<String,Integer>> getCompleteCountsWithFilter(String filter, String[] filterArg) {
        String[] projection = {
                "date(" + ProcessedSet.COLUMN_STATESTARTDATE + ")",
                "sum(" + ProcessedSet.COLUMN_COUNT + ")",
                "sum(" + ProcessedSet.COLUMN_PROCESS_STATE_COUNT + ")"
        };
        String selection = filter;
        String[] selectionArg = filterArg;
        String groupBy = ProcessedSet.COLUMN_STATESTARTDATE;
        Cursor cursor = db.query(ProcessedSet.TABLE_NAME, projection, selection, selectionArg, groupBy, null, null);
        List<Pair<String,Integer>> complete = new ArrayList<>();
        if (cursor != null) {
            cursor.moveToFirst();
            do {
                if (cursor.getCount() == 0) {
                    complete.add(new Pair<>("",0));
                } else {
                    int count = cursor.getInt(1);
                    int inWork = cursor.getInt(2);
                    complete.add(new Pair<>(cursor.getString(0), count - inWork));
                }
            } while (cursor.moveToNext());
            cursor.close();
        }
        return complete;
    }

   
    public ArrayList<Report> getReportTopOperator() {
        String[] projection = {
                ProcessedSet.COLUMN_PROCESSING_OPERATOR,
                "sum(" + ProcessedSet.COLUMN_SLA_NOT_EXPIRED_COUNT + ")",
                "sum(" + ProcessedSet.COLUMN_COUNT + ")",
        };
        String tables = ProcessedSet.TABLE_NAME;
        String groupBy = ProcessedSet.COLUMN_PROCESSING_OPERATOR;
        Cursor cursor = db.query(tables, projection, null, null, groupBy, null, null);
        ArrayList<Report> reports = new ArrayList<>();
        if (cursor != null) {
            cursor.moveToFirst();
            do {
                String name = cursor.getString(0);
                int sla = cursor.getInt(1);
                int slaExpiredCount = cursor.getInt(2);
                if (!name.equals(" ") || !name.contains("N/A")) {
                    reports.add(new Report(name, sla, sla + slaExpiredCount));
                }
            }while (cursor.moveToNext());
            cursor.close();
        }
        return reports;
    }

    public ArrayList<Report> getReportToGroupTop() {
        String[] projection = {
                ReceiptSet.COLUMN_UNIT_NAME,
                "sum(" + ReceiptSet.COLUMN_COUNT + ")",
                "sum(" + ReceiptSet.COLUMN_COUNT_AFTER_TIME + ")"
        };
        String tables = ReceiptSet.TABLE_NAME;
        String groupBy = ReceiptSet.COLUMN_UNIT_NAME;
        Cursor cursor = db.query(tables, projection, null, null, groupBy, null, null);
        ArrayList<Report> reports = new ArrayList<>();
        if (cursor != null) {
            cursor.moveToFirst();
            do {
                String name = cursor.getString(0);
                int count = cursor.getInt(1);
                int countAfterTime = cursor.getInt(2);
                if (name != null ) {
                    reports.add(new Report(name, count - countAfterTime, count ));
                }
            }while (cursor.moveToNext());
            cursor.close();
        }
        return reports;
    }

    public ArrayList<Report> getReportOperatorToUnitName(String unitName) {
        String[] projection = {
                ProcessedSet.COLUMN_PROCESSING_OPERATOR,
                "sum(" + ProcessedSet.COLUMN_SLA_NOT_EXPIRED_COUNT + ")",
                "sum(" + ProcessedSet.COLUMN_COUNT + ")"
        };
        String table = ProcessedSet.TABLE_NAME;
        String groupBy = ProcessedSet.COLUMN_PROCESSING_OPERATOR;
        String selection = ProcessedSet.COLUMN_UNIT_NAME + "= ?";
        String[] selectionArg = {unitName};
        Cursor cursor = db.query(table, projection, selection, selectionArg, groupBy, null, null);
        ArrayList<Report> reports = new ArrayList<>();
        if (cursor != null) {
            cursor.moveToFirst();
            do {
                String name = cursor.getString(0);
                int sla = cursor.getInt(1);
                int slaExpiredCount = cursor.getInt(2);
                if (!name.equals(" ") || !name.contains("N/A")) {
                    reports.add(new Report(name, sla, sla + slaExpiredCount));
                }
            }while (cursor.moveToNext());
            cursor.close();
        }
        return reports;
    }

}
