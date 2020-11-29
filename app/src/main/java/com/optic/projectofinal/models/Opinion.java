package com.optic.projectofinal.models;

public class Opinion {
    private String idUserPutOpinion;
    private Valuation valuationWorker;
    private long timestamp;
    private String message;
    private double averageValuation;
    private boolean isFromJob;
    private String imageJob;
    private String titleJob;
    public Opinion() {
        isFromJob=false;
    }

    public String getIdUserPutOpinion() {
        return idUserPutOpinion;
    }

    public void setIdUserPutOpinion(String idUserPutOpinion) {
        this.idUserPutOpinion = idUserPutOpinion;
    }

    public Valuation getValuationWorker() {
        return valuationWorker;
    }

    public void setValuationWorker(Valuation valuationWorker) {
        this.valuationWorker = valuationWorker;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public double getAverageValuation() {
        return averageValuation;
    }

    public void setAverageValuation(double averageValuation) {
        this.averageValuation = averageValuation;
    }

    public boolean isFromJob() {
        return isFromJob;
    }

    public void setFromJob(boolean fromJob) {
        isFromJob = fromJob;
    }

    public String getImageJob() {
        return imageJob;
    }

    public void setImageJob(String imageJob) {
        this.imageJob = imageJob;
    }

    public String getTitleJob() {
        return titleJob;
    }

    public void setTitleJob(String titleJob) {
        this.titleJob = titleJob;
    }
}
