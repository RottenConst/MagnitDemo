package ru.optimum.load.magnitdemo.data;

public class DataOnCardView {
    int generalCountValue;
    int valueGood;
    int sla75ExpiredCount;
    int slaExpiredCount;

    public DataOnCardView(int generalCountValue, int valueGood, int sla75ExpiredCount, int slaExpiredCount) {
        this.generalCountValue = generalCountValue;
        this.valueGood = valueGood;
        this.sla75ExpiredCount = sla75ExpiredCount;
        this.slaExpiredCount = slaExpiredCount;
    }

    public DataOnCardView() {
        this.generalCountValue = 0;
        this.valueGood = 0;
        this.sla75ExpiredCount = 0;
        this.slaExpiredCount = 0;
    }

    public int getGeneralCountValue() {
        return generalCountValue;
    }

    public void setGeneralCountValue(int generalCountValue) {
        this.generalCountValue = generalCountValue;
    }

    public int getValueGood() {
        return valueGood;
    }

    public void setValueGood(int valueGood) {
        this.valueGood = valueGood;
    }

    public int getSla75ExpiredCount() {
        return sla75ExpiredCount;
    }

    public void setSla75ExpiredCount(int sla75ExpiredCount) {
        this.sla75ExpiredCount = sla75ExpiredCount;
    }

    public int getSlaExpiredCount() {
        return slaExpiredCount;
    }

    public void setSlaExpiredCount(int slaExpiredCount) {
        this.slaExpiredCount = slaExpiredCount;
    }
}
