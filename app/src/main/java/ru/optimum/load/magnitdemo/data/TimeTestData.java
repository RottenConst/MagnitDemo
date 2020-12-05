package ru.optimum.load.magnitdemo.data;

public class TimeTestData {

    String name;
    int time;

    public TimeTestData(String name, int time) {
        this.name = name;
        this.time = time;
    }

    public  TimeTestData() {
        this.name = "";
        this.time = 0;
    }

    public String getName() {
        return name;
    }

    public int getTime() {
        return time;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
