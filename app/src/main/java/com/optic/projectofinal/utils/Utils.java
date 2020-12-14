package com.optic.projectofinal.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.annotation.AnyRes;
import androidx.annotation.NonNull;

import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.google.gson.Gson;
import com.optic.projectofinal.R;
import com.optic.projectofinal.models.Category;
import com.optic.projectofinal.models.Resource;
import com.optic.projectofinal.models.Sex;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Utils {
    private static final String TAG = "own";
    public  static int MAX_IMAGE_CAN_BE_SELECTED=10;

    public static int getResId(String resName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(resName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
    public static String getFileName(Uri uri,Context context) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }
    private static String getJsonFromAssets(Context context, String fileName) {
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
    private static <T> T getObjectFromJsonByKeyAndId(Context context, String key, int id,Class<T> type){
        String jsonData=Utils.getJsonFromAssets(context,"properties.json");
        try {
            JSONArray object=new JSONObject(jsonData).getJSONArray(key);
            for(int i=0;i<object.length();i++){
                JSONObject obj=object.getJSONObject(i);
                if(obj.getInt("id")==id){
                    return new Gson().fromJson(obj.toString(), type);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;

    }
    private static String getArrayStringJsonByKey(Context context,String key){
        String jsonData=Utils.getJsonFromAssets(context,"properties.json");
        String cadenaJson="";
        try {
            JSONArray object=new JSONObject(jsonData).getJSONArray(key);
            cadenaJson=object.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return cadenaJson;
    }
    public static String getArrayCategoriesJson(Context context){
        return getArrayStringJsonByKey(context,"all_categories" );
    }
    public static Category getCategoryByIdJson(Context context,int id){
        Category dev = getObjectFromJsonByKeyAndId(context, "all_categories", id, Category.class);
        dev.setTitleString(context);
        return dev;
    }
    public static String getArrayResourcesJson(Context context){
        return getArrayStringJsonByKey(context,"all_resources" );
    }
    public static Resource getResourcesByIdJson(Context context, int id){
        Resource dev = getObjectFromJsonByKeyAndId(context, "all_resources", id, Resource.class);
        dev.loadData(context);
        return  dev;
    }
    public static final Uri getUriToDrawable(@NonNull Context context,
                                             @AnyRes int drawableId) {
        Uri imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                "://" + context.getResources().getResourcePackageName(drawableId)
                + '/' + context.getResources().getResourceTypeName(drawableId)
                + '/' + context.getResources().getResourceEntryName(drawableId) );
        return imageUri;
    }

    public static String capitalizeString(String string) {
        char[] chars = string.toLowerCase().toCharArray();
        boolean found = false;
        for (int i = 0; i < chars.length; i++) {
            if (!found && Character.isLetter(chars[i])) {
                chars[i] = Character.toUpperCase(chars[i]);
                found = true;
            } else if (Character.isWhitespace(chars[i]) || chars[i]=='.' || chars[i]=='\'') { // You can add other chars here
                found = false;
            }
        }
        ;
        return String.valueOf(chars);

    }
    private static <T> List<T> getListItemsJson(Context context,String key, Class<T[]> type){
//        Type typeOfObjectsList = new TypeToken<T>() {
//        }.getType();
        return Arrays.asList(new Gson().fromJson(Utils.getArrayStringJsonByKey(context,key), type));
    }
    public static ArrayList<Category> getListCategoriesJson(Context context){
        List<Category> dev = getListItemsJson(context, "all_categories", Category[].class);
        for(Category i:dev){
            i.setTitleString(context);
        }
        ArrayList<Category> dev2 = new ArrayList<Category>();
        dev2.addAll(dev);
        return dev2;
    }
    public static ArrayList<Sex> getListSexJson(Context context){
        List<Sex> dev = getListItemsJson(context, "all_sex", Sex[].class);
        for(Sex i:dev){
            i.loadData(context);
        }
        ArrayList<Sex> dev2 = new ArrayList<Sex>();
        dev2.addAll(dev);
        return dev2;
    }
    public static Sex getSexByIdJson(Context context,int id){
        Sex dev = getObjectFromJsonByKeyAndId(context, "all_sex", id, Sex.class);
        dev.loadData(context);
        return dev;
    }
    public static ArrayList<Resource> getListResourcesJson(Context context){
        List<Resource> dev = getListItemsJson(context, "all_resources", Resource[].class);
        for(Resource i:dev){
            i.loadData(context);
        }
        ArrayList<Resource> dev2 = new ArrayList<Resource>();
        dev2.addAll(dev);
        return dev2;
    }
    public static String getDateFormatted(Long timestamp){
        Date date = new Date(timestamp);
        SimpleDateFormat df2 = new SimpleDateFormat("HH:mm - dd MMMM yyyy");
        String dateText = df2.format(date);
        return dateText;
    }
    public static long getTimestampFromString(String date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date parsedDate = dateFormat.parse("14/11/2020");
            return parsedDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static ArrayList<Resource> createListResourcesByIds(Context context, ArrayList<Integer> listR) {
        ArrayList<Resource> dev=new ArrayList<>();
        for(Integer i:listR){
            Resource resource=getResourcesByIdJson(context,i);
            resource.loadData(context);
            dev.add(resource);
        }
        return dev;
    }

    public static ArrayList<Integer> createListIntResourcesByList(ArrayList<Resource> listResources) {
        ArrayList<Integer> dev=new ArrayList<>();
        for(Resource i:listResources){
            dev.add(i.getId());
        }
        return dev;
    }

    public static Transformation<Bitmap> getTransformSquareRound() {
        return new MultiTransformation(new CenterCrop(), new RoundedCorners(30));
    }

    public static BaseRequestOptions<?> getOptionsGlide(boolean wantCache) {
        return   new RequestOptions()
                .placeholder(R.color.grey_100)    // replace with your placeholder image or remove if don't want to set
                .error(R.drawable.ic_error_404)     // replace with your placeholder image or remove if don't want to set
                .diskCacheStrategy(wantCache?DiskCacheStrategy.AUTOMATIC:DiskCacheStrategy.NONE);
    }

    public static String getMimeType(Uri uri,Context context) {
        ContentResolver cR = context.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        String type = mime.getExtensionFromMimeType(cR.getType(uri));
        return type;
    }

    public static String getStringFromTimestamp(Long birthdate) {
        Date n = new Date(birthdate);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(n);
    }

    public static void setEnabledAllViews(ViewGroup layout, boolean option) {
        layout.setEnabled(option);
        for (int i = 0; i < layout.getChildCount(); i++) {
            View child = layout.getChildAt(i);
            if (child instanceof ViewGroup) {
                setEnabledAllViews((ViewGroup) child,option);
            } else {
                child.setEnabled(option);
                child.setFocusable(option);
                child.setClickable(option);
            }
        }
    }
    public static String roundToHalf(double d) {
        double result = Math.round(d * 2) / 2.0;
        DecimalFormat formato = new DecimalFormat("####0.00");
        return formato.format(result);
    }

    public static void changeTintIconToolbar(MenuItem item, int color) {
        Drawable icon = item.getIcon();
        icon.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        item.setIcon(icon);

    }

    public static void generateDynamicLink(Context context,String id,String title,String description,String img,String titlePopUp) {
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

        Task<ShortDynamicLink> dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse("https://projectofinal.page.link?id=" + id))
                .setDomainUriPrefix("https://projectofinal.page.link")
                .setAndroidParameters(
                        new DynamicLink.AndroidParameters.Builder("com.optic.projectofinal")
                                .setMinimumVersion(1)
                                .build())
                .setIosParameters(
                        new DynamicLink.IosParameters.Builder("com.optic.projectofinal")
                                .setAppStoreId("whatever")
                                .setMinimumVersion("1.0.1")
                                .build())
                .setSocialMetaTagParameters(
                        new DynamicLink.SocialMetaTagParameters.Builder()
                                .setTitle(title)
                                .setDescription(description)
                                .setImageUrl(Uri.parse(img))
                                .build())
                .buildShortDynamicLink()
                .addOnCompleteListener(new OnCompleteListener<ShortDynamicLink>() {
                    @Override
                    public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                        if (task.isSuccessful()) {
                            // Short link created
                            Uri shortLink = task.getResult().getShortLink();
                            Uri flowchartLink = task.getResult().getPreviewLink();
                            Intent sendIntent = new Intent();
                            sendIntent.setAction(Intent.ACTION_SEND);
                            sendIntent.putExtra(Intent.EXTRA_TEXT, titlePopUp + ": " + shortLink.toString());
                            sendIntent.setType("text/plain");

                            Intent shareIntent = Intent.createChooser(sendIntent, null);
                            context.startActivity(shareIntent);
                        } else {
                            // Error
                            // ...
                            Toast.makeText(context, "ERROR", Toast.LENGTH_LONG).show();
                            Log.e(TAG, "generateDynamicLink erroer onComplete: " );
                        }
                    }
                });

    }


}

