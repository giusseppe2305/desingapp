package com.optic.projectofinal.models;

import java.util.ArrayList;
import java.util.Date;

public class JobDone {
    private String id;
    private String idUserOffer;
    private String idUserApply;
    private long timestamp;
    private String description;
    private String opinionUserOffer;
    private String opinionUserApply;
    private ArrayList<String> images;
    private Valuation valuation;

    public JobDone() {
        timestamp=new Date().getTime();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdUserOffer() {
        return idUserOffer;
    }

    public void setIdUserOffer(String idUserOffer) {
        this.idUserOffer = idUserOffer;
    }

    public String getIdUserApply() {
        return idUserApply;
    }

    public void setIdUserApply(String idUserApply) {
        this.idUserApply = idUserApply;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOpinionUserOffer() {
        return opinionUserOffer;
    }

    public void setOpinionUserOffer(String opinionUserOffer) {
        this.opinionUserOffer = opinionUserOffer;
    }

    public String getOpinionUserApply() {
        return opinionUserApply;
    }

    public void setOpinionUserApply(String opinionUserApply) {
        this.opinionUserApply = opinionUserApply;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public Valuation getValuation() {
        return valuation;
    }

    public void setValuation(Valuation valuation) {
        this.valuation = valuation;
    }
}
