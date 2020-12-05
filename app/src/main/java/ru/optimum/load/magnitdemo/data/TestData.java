package ru.optimum.load.magnitdemo.data;

public class TestData {
    String district;
    int processed;
    int violation;
    float sla;

    public TestData(String district, int processed, int violation, float sla) {
        this.district = district;
        this.processed = processed;
        this.violation = violation;
        this.sla = sla;
    }

    public TestData() {
        this.district = "";
        this.processed = 0;
        this.violation = 0;
        this.sla = 0F;
    }

    public String getTitle() {
        return district;
    }

    public int getProcessed() {
        return processed;
    }

    public int getViolation() {
        return violation;
    }

    public float getSla() {
        return sla;
    }

    public void setTitle(String district) {
        if (district != null && district.equals("ВОЛЖСКИЙ ОКРУГ")){
            district = "Волжский округ";
        } else if (district != null && district.equals("ГК.")){
            district = "ГК";
        }
        this.district = district;
    }

    public void setProcessed(int processed) {
        this.processed = processed;
    }

    public void setViolation(int violation) {
        this.violation = violation;
    }

    public void setSla(float sla) {
        this.sla = sla;
    }
}
