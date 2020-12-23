package ru.optimum.load.magnitdemo.data;

public class MainData {
    String title;
    int count;

    public MainData(String title, int count) {
        this.title = title;
        this.count = count;
    }

    public MainData() {
        this.title = "";
        this.count = 0;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
