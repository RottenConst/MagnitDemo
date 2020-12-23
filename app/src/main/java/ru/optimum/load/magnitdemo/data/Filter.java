package ru.optimum.load.magnitdemo.data;

public class Filter {

    String category;
    String[] title;
    boolean[] isCheck;

    public Filter (String category, String[] title, boolean[] isCheck) {
        this.category = category;
        this.title = title;
        this.isCheck = isCheck;
    }

    public Filter() {
        this.category = "";
        this.title = new String[]{};
        this.isCheck = new boolean[]{};
    }

    public String[] getTitle() {
        return title;
    }

    public void setTitle(String[] title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean[] getCheck() {
        return isCheck;
    }

    public void setCheck(boolean[] check) {
        isCheck = check;
    }
}
