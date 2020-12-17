package com.optic.projectofinal.models;

public class SliderItem {
    private String Description;
    private int ImageUrl;
    private String image;

    public SliderItem(String image) {
        this.image=image;
    }

    public String getDescription() {
        return Description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public int getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(int imageUrl) {
        ImageUrl = imageUrl;
    }

    public SliderItem(String description, int imageUrl) {
        Description = description;
        ImageUrl = imageUrl;
    }
}
