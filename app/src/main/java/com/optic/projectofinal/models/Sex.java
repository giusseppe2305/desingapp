package com.optic.projectofinal.models;

import android.content.Context;

import com.optic.projectofinal.R;
import com.optic.projectofinal.utils.Utils;

public class Sex {
    private int id;
    private String title;
    private String titleString;

    public Sex(int id) {
    }

    public int getId() {
        return id;
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

    public String getTitleString() {
        return titleString;
    }

    public void loadData(Context context){
        titleString= context.getString(Utils.getResId(title, R.string.class));
    }

    @Override
    public String toString() {
        return getTitleString();
    }
}
