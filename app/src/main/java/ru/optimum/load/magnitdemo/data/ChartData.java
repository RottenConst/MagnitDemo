package ru.optimum.load.magnitdemo.data;

import java.util.ArrayList;

public class ChartData {
    String title;
    ArrayList<Integer> counts;
    int count;

    public ChartData(String title, ArrayList<Integer> counts, int count) {
        this.title = title;
        this.counts = counts;
        this.count = count;
    }

    public ChartData() {
        this.title = "";
        this.counts = new ArrayList<>();
        this.count = 0;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<Integer> getCounts() {
        return counts;
    }

    public void setCounts(ArrayList<Integer> counts) {
        this.counts = counts;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
