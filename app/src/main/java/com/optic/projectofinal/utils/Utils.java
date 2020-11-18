package com.optic.projectofinal.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;

import androidx.annotation.AnyRes;
import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.optic.projectofinal.models.Category;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;

public class Utils {
    public static int getResId(String resName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(resName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
    public static String getJsonFromAssets(Context context, String fileName) {
        String jsonString;
        try {
            InputStream is = context.getAssets().open(fileName);

            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            jsonString = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return jsonString;
    }
    public static String getArrayCategoriosJson(Context context){
        String jsonData=Utils.getJsonFromAssets(context,"properties.json");
        String cadenaJson="";
        try {
            JSONArray object=new JSONObject(jsonData).getJSONArray("all_categories");
            cadenaJson=object.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return cadenaJson;
    }
    public static Category getCategoryByIdJson(Context context,int id){
        String jsonData=Utils.getJsonFromAssets(context,"properties.json");
        try {
            JSONArray object=new JSONObject(jsonData).getJSONArray("all_categories");
            for(int i=0;i<object.length();i++){
                JSONObject obj=object.getJSONObject(i);
                if(obj.getInt("id")==id){
                    return new Gson().fromJson(obj.toString(),Category.class);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static final Uri getUriToDrawable(@NonNull Context context,
                                             @AnyRes int drawableId) {
        Uri imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                "://" + context.getResources().getResourcePackageName(drawableId)
                + '/' + context.getResources().getResourceTypeName(drawableId)
                + '/' + context.getResources().getResourceEntryName(drawableId) );
        return imageUri;
    }
}

