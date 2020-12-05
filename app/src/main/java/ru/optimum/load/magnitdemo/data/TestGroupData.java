package ru.optimum.load.magnitdemo.data;

public class TestGroupData {
    String title;
    int time;
    int processed;

    public TestGroupData(String title, int time, int processed) {
        this.title = title;
        this.time = time;
        this.processed = processed;
    }

    public TestGroupData() {
        this.title = "";
        this.time = 0;
        this.processed = 0;
    }

    public String getTitle() {
        return title;
    }

    public int getTime() {
        return time;
    }

    public int getProcessed() {
        return processed;
    }

    public void setTitle(String title) {
        if (title == null){
            this.title = "";
        }else {
            this.title = title;
        }
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void setProcessed(int processed) {
        this.processed = processed;
    }
}
