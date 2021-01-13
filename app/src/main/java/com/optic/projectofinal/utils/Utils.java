package com.optic.projectofinal.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.OpenableColumns;
import android.util.DisplayMetrics;
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
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.gson.Gson;
import com.optic.projectofinal.R;
import com.optic.projectofinal.models.BasicInformationUser;
import com.optic.projectofinal.models.Category;
import com.optic.projectofinal.models.Resource;
import com.optic.projectofinal.models.Sex;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Currency;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class Utils {


    public  enum REGISTER{GOOGLE,FACEBOOK,EMAIL}
    public enum LANGUAGE{
        SPANISH("es-ES"),
        ENGLISH("en-US");
        private String code;
        LANGUAGE(String code) {
            this.code = code;
        }
        public String getCode() {
            return code;
        }

    }
    private static final String NAME_FILE_JSON_PROPERTIES = "properties.json";
    private static final String NODE_ALL_CATEGORIES = "all_categories";
    private static final String NODE_RESOURCES = "all_resources";
    public  static final int MAX_IMAGE_CAN_BE_SELECTED=10;
    public static final String TAG_LOG="own";

    public static void setAppLocale( Context context){
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN_MR1){
            config.setLocale(Locale.forLanguageTag(getLanguage(context)));
        } else {
            config.locale = Locale.forLanguageTag(getLanguage(context));
        }
        resources.updateConfiguration(config, dm);
    }

    public static <T> Map<String, Object> convertClassToMap(T obj){
        Map<String, Object> update = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL).convertValue(obj, Map.class);
        ArrayList<String> delete=new ArrayList<>();
        for (Map.Entry<String, Object> entry : update.entrySet()) {
            if(entry.getValue().toString().length()>0 &&entry.getValue().toString().charAt(0)=='0'){
                delete.add(entry.getKey());
            }
        }
        for(String del:delete){
            update.remove(del);
        }
        return update;
    }
    public static String convertDateFormat(String date)  {

        try {
            SimpleDateFormat format=new SimpleDateFormat("MM/dd/yyyy");
            SimpleDateFormat format2=new SimpleDateFormat("dd/MM/yyyy");
            Date fecha = format.parse("05/23/1998");
            return format2.format(fecha);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    public  static void downloadFile(String url,IDo function) {
        Log.d(TAG_LOG, "downloadFile: ENTRA METODO");
        new Thread(new Runnable() {
            
            @Override
            public void run() {
                try {
                    Log.d(TAG_LOG, "run: entra hilo");
                    URL u = new URL(url);
                    URLConnection conn = u.openConnection();
                    int contentLength = conn.getContentLength();
                    DataInputStream stream = new DataInputStream(u.openStream());
                    byte[] buffer = new byte[contentLength];
                    stream.readFully(buffer);
                    stream.close();
                    Log.d(TAG_LOG, "run: acaba hilo");
                    function.upload(buffer);
                } catch(FileNotFoundException e) {
                    Log.e(TAG_LOG, "run: "+e.getMessage() );
                    return; // swallow a 404
                } catch (IOException e) {
                    Log.e(TAG_LOG, "run: "+e.getMessage() );
                    return; // swallow a 404
                }
            }
        }).start();
    }



    public static String arrayToString(String[] strings) {
        String dev="";
        for(int i=0;i<strings.length;i++){
            dev+=strings[i];
            if(i!=strings.length-1){
                dev+=",";
            }
        }

        return dev;
    }
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
            try (Cursor cursor = context.getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
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
        InputStream is = null;
        try {
             is = context.getAssets().open(fileName);

            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            jsonString = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }finally {
            try {
                if(is!=null)
                    is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return jsonString;
    }
    private static <T> T getObjectFromJsonByKeyAndId(Context context, String key, int id,Class<T> type){
        String jsonData=Utils.getJsonFromAssets(context,NAME_FILE_JSON_PROPERTIES);
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
    public static String getOptionNotificationFromJSON(String chain){
        JSONObject object;
        try {
            object = new JSONObject(chain);
            return         object.getString("option");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    private static String getArrayStringJsonByKey(Context context,String key){
        String jsonData=Utils.getJsonFromAssets(context,NAME_FILE_JSON_PROPERTIES);
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
        return getArrayStringJsonByKey(context,NODE_ALL_CATEGORIES );
    }
    public static Category getCategoryByIdJson(Context context,int id){
        Category dev = getObjectFromJsonByKeyAndId(context, NODE_ALL_CATEGORIES, id, Category.class);
        dev.setTitleString(context);
        return dev;
    }
    public static String getArrayResourcesJson(Context context){
        return getArrayStringJsonByKey(context,NODE_RESOURCES );
    }
    public static Resource getResourcesByIdJson(Context context, int id){
        Resource dev = getObjectFromJsonByKeyAndId(context, NODE_RESOURCES, id, Resource.class);
        dev.loadData(context);
        return  dev;
    }
    public static  Uri getUriToDrawable(@NonNull Context context,
                                             @AnyRes int drawableId) {
        return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                "://" + context.getResources().getResourcePackageName(drawableId)
                + '/' + context.getResources().getResourceTypeName(drawableId)
                + '/' + context.getResources().getResourceEntryName(drawableId) );
    }

    public static String capitalizeString(String string) {
        char[] chars = string.toLowerCase().toCharArray();
        boolean found = false;
        for (int i = 0; i < chars.length; i++) {
            if (!found && Character.isLetter(chars[i])) {
                chars[i] = Character.toUpperCase(chars[i]);
                found = true;
            } else if (Character.isWhitespace(chars[i]) || chars[i]=='.' || chars[i]=='\'') {
                found = false;
            }
        }

        return String.valueOf(chars);

    }
    private static <T> List<T> getListItemsJson(Context context,String key, Class<T[]> type){

        return Arrays.asList(new Gson().fromJson(Utils.getArrayStringJsonByKey(context,key), type));
    }
    public static ArrayList<Category> getListCategoriesJson(Context context){
        List<Category> dev = getListItemsJson(context, NODE_ALL_CATEGORIES, Category[].class);
        for(Category i:dev){
            i.setTitleString(context);
        }
        return new ArrayList<>(dev);
    }
    public static ArrayList<Sex> getListSexJson(Context context){
        List<Sex> dev = getListItemsJson(context, "all_sex", Sex[].class);
        for(Sex i:dev){
            i.loadData(context);
        }

        return new ArrayList<>(dev);
    }
    public static Sex getSexByIdJson(Context context,int id){
        Sex dev = getObjectFromJsonByKeyAndId(context, "all_sex", id, Sex.class);
        dev.loadData(context);
        return dev;
    }
    public static ArrayList<Resource> getListResourcesJson(Context context){
        List<Resource> dev = getListItemsJson(context, NODE_RESOURCES, Resource[].class);
        for(Resource i:dev){
            i.loadData(context);
        }
        return new ArrayList<>(dev);
    }
    public static String getDateFormattedAdvance(Long timestamp,Context context){
        Date date = new Date(timestamp);
        SimpleDateFormat df2 = new SimpleDateFormat("HH:mm - dd MMMM yyyy",Locale.getDefault());
        return df2.format(date);
    }public static String getDateFormattedSimple(Long timestamp,Context context){
        Date date = new Date(timestamp);
        SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
        return df2.format(date);
    }
    public static long getTimestampFromString(String date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
        try {
            Date parsedDate = dateFormat.parse(date);
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
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    public static String getStringFromTimestamp(Long birthdate) {
        Date n = new Date(birthdate);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
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
        DecimalFormat format = new DecimalFormat("####0.00");
        return format.format(result);
    }

    public static String getFormatPrice(double num, Context context){
        return String.format("%.2f %s",num,getCurrency(context).getSymbol());
    }
    public static void changeTintIconToolbar(MenuItem item, int color) {
        Drawable icon = item.getIcon();
        icon.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        item.setIcon(icon);

    }
    private static String getNameProject(Context context){
        return context.getApplicationContext().getPackageName();
    }
    public static void generateDynamicLink(Context context,String id,String title,String description,String img,String titlePopUp) {

        FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse("https://projectofinal.page.link?id=" + id))
                .setDomainUriPrefix("https://projectofinal.page.link")
                .setAndroidParameters(
                        new DynamicLink.AndroidParameters.Builder()
                                .setMinimumVersion(1)
                                .build())
                .setIosParameters(
                        new DynamicLink.IosParameters.Builder(getNameProject(context))
                                .setAppStoreId("example")
                                .setMinimumVersion("1.0.1")
                                .build())
                .setSocialMetaTagParameters(
                        new DynamicLink.SocialMetaTagParameters.Builder()
                                .setTitle(title)
                                .setDescription(description)
                                .setImageUrl(Uri.parse(img))
                                .build())
                .buildShortDynamicLink()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Short link created
                        Uri shortLink = task.getResult().getShortLink();
                        //Uri flowchartLink = task.getResult().getPreviewLink();
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
                        Log.e(TAG_LOG, "generateDynamicLink erroer onComplete: ");
                    }
                });

    }
    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }
    public static void setPersistantBasicUserInformation(BasicInformationUser dto,Context context){
        SharedPreferences sharedPref = context.getSharedPreferences("basicUserInformation", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString("lastName", dto.getLastName());
        editor.putString("name", dto.getName());
        if(!dto.getPhotoUser().equals("nonPhoto"))
        editor.putString("photoUser", dto.getPhotoUser());
        editor.apply();
    }
    public static void updateImageProfileBasicUserInformation(String url,Context context){
        SharedPreferences sharedPref = context.getSharedPreferences("basicUserInformation", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("photoUser", url);
        editor.apply();
    }
    public static BasicInformationUser getPersistentBasicUserInformation(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences("basicUserInformation", MODE_PRIVATE);

        BasicInformationUser dto=new BasicInformationUser();
        dto.setName(sharedPref.getString("name", "name"));
        dto.setLastName(sharedPref.getString("lastName", "lastName"));
        dto.setPhotoUser(sharedPref.getString("photoUser", null));
        return dto;
    }
    public static void setLanguage(String language,Context context){
        SharedPreferences sharedPref = context.getSharedPreferences("language", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("language", language);
        editor.apply();
    }
    public static String getLanguage(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences("language", MODE_PRIVATE);
        return sharedPref.getString("language", LANGUAGE.SPANISH.getCode());
    }

    public static Currency getCurrency(Context context){
        return Currency.getInstance(getCurrentLocation(context));
    }
    public static Locale getCurrentLocation(Context context){
        return  Locale.forLanguageTag(getLanguage(context));
    }

}

