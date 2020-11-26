package com.optic.projectofinal.models;

import com.optic.projectofinal.R;
import com.optic.projectofinal.utils.Utils;

public class Category {
    private int id;
    private String title;
    private String image;
    private int idImage;
    public Category() {
    }
    public Category(String title,String image) {
        this.title = title;
        this.image =  image;

    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public int getIdImage() {
        return   Utils.getResId(image, R.drawable.class);
    }

    public void setIdImage(int idImage) {
        this.idImage = idImage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;

    }

    public int getIdTitle() {
        return   Utils.getResId(title, R.string.class);
    }
}
