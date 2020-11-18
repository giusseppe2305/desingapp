package com.optic.projectofinal.adapters;

public class SliderItem {
    String Description;
    int ImageUrl;

    public String getDescription() {
        return Description;
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
