package com.optic.projectofinal.modelsRetrofit;

import com.optic.projectofinal.models.Skill;

import java.util.List;

public class WorkerQueryModel {
    private String id;
    private String profileImage;
    private String name;
    private String lastName;
    private double price;
    private String about;
    private double averageValuation;
    private List<Skill> skills;
    private long lastConnection;
    private long timestamp;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public double getAverageValuation() {
        return averageValuation;
    }

    public void setAverageValuation(double averageValuation) {
        this.averageValuation = averageValuation;
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }

    public long getLastConnection() {
        return lastConnection;
    }

    public void setLastConnection(long lastConnection) {
        this.lastConnection = lastConnection;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "WorkerQueryModel{" +
                "id='" + id + '\'' +
                ", profileImage='" + profileImage + '\'' +
                ", name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", price=" + price +
                ", about='" + about + '\'' +
                ", averageValuation=" + averageValuation +
                ", skills=" + skills +
                ", lastConnection=" + lastConnection +
                ", timestamp=" + timestamp +
                '}';
    }
}
