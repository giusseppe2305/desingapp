package com.optic.projectofinal.models;

import android.content.Context;

import com.optic.projectofinal.R;
import com.optic.projectofinal.utils.Utils;

import java.util.Objects;

public class Resource {
    private int id;
    private String title;
    private String image;
    private int idImage;
    private String titleString;

    public String getTitleString() {
        return titleString;
    }

    public void setTitleString(String titleString) {
        this.titleString = titleString;
    }

    public Resource() {
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Resource resource = (Resource) o;
        return id == resource.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void setId(int id) {
        this.id = id;
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

    @Override
    public String toString() {
        return titleString;
    }

    public void loadData(Context context) {
        titleString= context.getString(Utils.getResId(title, R.string.class));
        idImage=Utils.getResId(image,R.drawable.class);
    }

    public int getIdImage() {
        return idImage;
    }

    public void setIdImage(int idImage) {
        this.idImage = idImage;
    }
}
