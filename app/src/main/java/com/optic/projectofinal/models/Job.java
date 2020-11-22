package com.optic.projectofinal.models;

import java.util.ArrayList;

public class Job {
    private String id;
    private String idUserOffer;
    private long timestamp;
    private String description;
    private ArrayList<String> images;

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

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }
}
