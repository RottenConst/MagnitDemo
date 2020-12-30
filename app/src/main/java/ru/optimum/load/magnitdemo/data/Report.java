package ru.optimum.load.magnitdemo.data;

public class Report {

    String name;
    int slaNotExpiredCount;
    int count;
    public float procent;

    public Report(String name, int slaNotExpiredCount, int count) {
        this.name = name;
        this.slaNotExpiredCount = slaNotExpiredCount;
        this.count = count;
    }

    public float getProcent() {
        float valueOne =  (float) this.count;
        float onepProc = valueOne/ 100;
        float valueTwo = (float) this.slaNotExpiredCount;
        if (this.getCount() == this.getSlaNotExpiredCount()){
            this.procent = 100f;
        } else {
           procent = valueTwo/onepProc;
        }
        return procent;
    }

    public void setProcent(float procent) {
        this.procent = procent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSlaNotExpiredCount() {
        return slaNotExpiredCount;
    }

    public void setSlaNotExpiredCount(int slaNotExpiredCount) {
        this.slaNotExpiredCount = slaNotExpiredCount;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
