package com.optic.projectofinal.models;

public class Skill {
    private String title;
    private int idCategory;
    private String idSubcategory;
    private double pricePerHour;
    public Skill() {
    }

    public double getPricePerHour() {
        return pricePerHour;
    }

    public void setPricePerHour(double pricePerHour) {
        this.pricePerHour = pricePerHour;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(int idCategory) {
        this.idCategory = idCategory;
    }

    public String getIdSubcategory() {
        return idSubcategory;
    }

    public void setIdSubcategory(String idSubcategory) {
        this.idSubcategory = idSubcategory;
    }

    public void update(Skill skill) {
        this.setTitle(skill.getTitle());
        this.setIdSubcategory(skill.getIdSubcategory());
        this.setIdCategory(skill.getIdCategory());
        this.setPricePerHour(skill.getPricePerHour());
    }
}
