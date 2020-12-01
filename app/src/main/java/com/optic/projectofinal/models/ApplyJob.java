package com.optic.projectofinal.models;

public class ApplyJob {

    private String idWorkerApply;
    private String idJob;
    private String message;
    private double price;


    public String getIdWorkerApply() {
        return idWorkerApply;
    }

    public void setIdWorkerApply(String idWorkerApply) {
        this.idWorkerApply = idWorkerApply;
    }

    public String getIdJob() {
        return idJob;
    }

    public void setIdJob(String idJob) {
        this.idJob = idJob;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
