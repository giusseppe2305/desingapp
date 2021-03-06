package com.optic.projectofinal.models;

public class Valuation {
    private double speedContact;
    private double amiability;
    private double speedEndJob;
    private double punctuality;

    public Valuation() {
    }

    public Valuation(double speedContact, double amiability, double speedEndJob, double punctuality) {
        this.speedContact = speedContact;
        this.amiability = amiability;
        this.speedEndJob = speedEndJob;
        this.punctuality = punctuality;
    }
    public double getAverageTotal(){
        return (speedContact+amiability+speedEndJob+punctuality)/4;
    }
    public double getSpeedContact() {
        return speedContact;
    }

    public void setSpeedContact(double speedContact) {
        this.speedContact = speedContact;
    }

    public double getAmiability() {
        return amiability;
    }

    public void setAmiability(double amiability) {
        this.amiability = amiability;
    }

    public double getSpeedEndJob() {
        return speedEndJob;
    }

    public void setSpeedEndJob(double speedEndJob) {
        this.speedEndJob = speedEndJob;
    }

    public double getPunctuality() {
        return punctuality;
    }

    public void setPunctuality(double punctuality) {
        this.punctuality = punctuality;
    }
}
