package com.optic.projectofinal.modelsRetrofit;

public class JobsQueryModel {
    private double average;
    private int count;

    public double getAverage() {
        return average;
    }

    public void setAverage(double average) {
        this.average = average;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public JobsQueryModel(double average, int count) {
        this.average = average;
        this.count = count;
    }

    @Override
    public String toString() {
        return "JobsQueryModel{" +
                "average=" + average +
                ", count=" + count +
                '}';
    }
}
