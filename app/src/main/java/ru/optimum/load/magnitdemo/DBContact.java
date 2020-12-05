package ru.optimum.load.magnitdemo;

import android.provider.BaseColumns;

public class DBContact {
    private DBContact() {
    }
    public static class OpenSet implements BaseColumns {
        public static final String TABLE_NAME = "OpenSet";
        public static final String COLUMN_STATESTARTDATE = "STATESTARTDATE";
        public static final String COLUMN_COMPANY = "COMPANY";
        public static final String COLUMN_AFFILIATE = "AFFILIATE";
        public static final String COLUMN_DISTRICT = "DISTRICT";
        public static final String COLUMN_UNIT_NAME = "UNITNAME";
        public static final String COLUMN_SHOP = "SHOP";
        public static final String COLUMN_SERVICE_COMPONENT_CODE = "SERVICECOMPONENTCODE";
        public static final String COLUMN_RESPOFBUSINESSSUBDIV = "RESPOFBUSINESSSUBDIV";
        public static final String COLUMN_TYPE_OF_SET = "TYPEOFSET";
        public static final String COLUMN_COUNT = "Count";
        public static final String COLUMN_SLA75_EXPIRED_COUNT = "SLA75ExpiredCount";
        public static final String COLUMN_GROUP_SERV_COMP_CODE = "GROUPSERVCOMPCODE";
        public static final String COLUMN_SLA_EXPIRED_COUNT = "SLAExpiredCount";
        public static final String COLUMN_IS_VALID_VALUE = "__IsValidValue";
        public static final String COLUMN_BATCH_ID = "__BatchId";
    }

    public static class ProcessedSet implements BaseColumns {
        public static final String TABLE_NAME = "ProcessedSet";
        public static final String COLUMN_STATESTARTDATE = "STATESTARTDATE";
        public static final String COLUMN_UNIT_NAME = "UNITNAME";
        public static final String COLUMN_AFFILIATE = "AFFILIATE";
        public static final String COLUMN_COMPANY = "COMPANY";
        public static final String COLUMN_DISTRICT = "DISTRICT";
        public static final String COLUMN_SHOP = "SHOP";
        public static final String COLUMN_SERVICE_COMPONENT_CODE = "SERVICECOMPONENTCODE";
        public static final String COLUMN_TYPE_OF_SET = "TYPEOFSET";
        public static final String COLUMN_COUNT = "Count";
        public static final String COLUMN_RESPOFBUSINESSSUBDIV = "RESPOFBUSINESSSUBDIV";
        public static final String COLUMN_PROCESSING_OPERATOR = "PROCESSINGOPERATOR";
        public static final String COLUMN_SLA_NOT_EXPIRED_COUNT = "SLANotExpiredCount";
        public static final String COLUMN_PROCESS_EXECUTION = "PROCESSEXECUTION";
        public static final String COLUMN_WORK_TIME = "WORKTIME";
        public static final String COLUMN_WAIT_TIME = "WAITTIME";
        public static final String COLUMN_AFTER_TIME_COUNT = "AfterTimeCount";
        public static final String COLUMN_SLA75_EXPIRED_COUNT = "SLA75ExpiredCount";
        public static final String COLUMN_GROUP_SERV_COMP_CODE = "GROUPSERVCOMPCODE";
        public static final String COLUMN_PROCESS_STATE_COUNT = "ProcessStateCount";
        public static final String COLUMN_IS_VALID_VALUE = "__IsValidValue";
        public static final String COLUMN_BATCH_ID = "__BatchId";
    }

    public static class ReceiptSet implements BaseColumns {
        public static final String TABLE_NAME = "ReceiptSet";
        public static final String COLUMN_STATESTARTDATE = "STATESTARTDATE";
        public static final String COLUMN_COUNT = "Count";
        public static final String COLUMN_COMPANY = "COMPANY";
        public static final String COLUMN_AFFILIATE = "AFFILIATE";
        public static final String COLUMN_DISTRICT = "DISTRICT";
        public static final String COLUMN_UNIT_NAME = "UNITNAME";
        public static final String COLUMN_SHOP = "SHOP";
        public static final String COLUMN_SERVICE_COMPONENT_CODE = "SERVICECOMPONENTCODE";
        public static final String COLUMN_RESPOFBUSINESSSUBDIV = "RESPOFBUSINESSSUBDIV";
        public static final String COLUMN_TYPE_OF_SET = "TYPEOFSET";
        public static final String COLUMN_RECEIPT_COPY_COUNT = "ReceiptCopyCount";
        public static final String COLUMN_RECEIPT_WAS_COPY_COUNT = "ReceiptWasCopyCount";
        public static final String COLUMN_COUNT_AFTER_TIME = "CountAfterTime";
        public static final String COLUMN_SLA75_EXPIRED_COUNT = "SLA75ExpiredCount";
        public static final String COLUMN_SLA_TYPE = "SLAType";
        public static final String COLUMN_SLA_EXPIRED_COUNT = "SLAExpiredCount";
        public static final String COLUMN_GROUP_SERV_COMP_CODE = "GROUPSERVCOMPCODE";
        public static final String COLUMN_IS_VALID_VALUE = "__IsValidValue";
        public static final String COLUMN_BATCH_ID = "__BatchId";
    }
}
