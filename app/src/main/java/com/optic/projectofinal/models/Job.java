package com.optic.projectofinal.models;

import java.util.ArrayList;

public class Job {

    public enum State{PUBLISHED,IN_PROGRESS,FINISHED}
    private String id;
    private String idUserOffer;
    private String idUserApply;
    private long timestamp;
    private String description;
    private String opinionUserOffer;
    private String opinionUserApply;
    private ArrayList<String> images;
    private Valuation valuation;
    private String title;
    private double totalPrice;
    private State state;
    private int category;
    private String subcategory;
    private String thumbnail;
    public Job() {

    }
    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    public String getIdUserApply() {
        return idUserApply;
    }

    public void setIdUserApply(String idUserApply) {
        this.idUserApply = idUserApply;
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

    public Valuation getValuation() {
        return valuation;
    }

    public void setValuation(Valuation valuation) {
        this.valuation = valuation;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
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
