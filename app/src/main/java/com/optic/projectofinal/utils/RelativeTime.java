package com.optic.projectofinal.utils;

import android.app.Application;
import android.content.Context;

import com.optic.projectofinal.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class RelativeTime extends Application {

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;


    public static String getTimeAgo(long time) {
        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000;
        }

        long now = System.currentTimeMillis();
        if (time > now || time <= 0) {
            return "hace un momento";
        }

        // TODO: localize
        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return "Hace un momento";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "Hace un minuto";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return "Hace " + diff / MINUTE_MILLIS + " minutos";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "Hace una hora";
        } else if (diff < 24 * HOUR_MILLIS) {
            return "Hace " + diff / HOUR_MILLIS + " horas";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "Ayer";
        } else {
            return "Hace " + diff / DAY_MILLIS + " dias";
        }
    }

    public static String timeFormatAMPM(long time) {

        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");


        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000;
        }

        long now = System.currentTimeMillis();
        if (time > now || time <= 0) {
            String dateString = formatter.format(new Date(time));
            return dateString;
        }

        // TODO: localize
        final long diff = now - time;
        if (diff < 24 * HOUR_MILLIS) {
            String dateString = formatter.format(new Date(time));
            return dateString;
        } else if (diff < 48 * HOUR_MILLIS) {
            return "Ayer";
        } else {
            SimpleDateFormat formatter2 = new SimpleDateFormat("HH:mm");
            String dateString = formatter2.format(new Date(time));
            return dateString;
        }

    }
    public static String getTittleDate(Context context, long date) {
        Calendar now = Calendar.getInstance();
        now.setTime(new Date());

        Calendar compare = Calendar.getInstance();
        compare.setTime(new Date(date));

        if(now.get(Calendar.DAY_OF_YEAR)==compare.get(Calendar.DAY_OF_YEAR) &&
                now.get(Calendar.YEAR)==compare.get(Calendar.YEAR) ) {
            return context.getString(R.string.date_format_yesterday);
        }else if(now.get(Calendar.DAY_OF_YEAR)-1==compare.get(Calendar.DAY_OF_YEAR) &&
                now.get(Calendar.YEAR)==compare.get(Calendar.YEAR))   {
            return context.getString(R.string.date_format_today);
        }else {
            String formatText=String.format("dd '%s' MMMM '%s' yyyy",context.getString(R.string.date_format_of),context.getString(R.string.date_format_of));
            SimpleDateFormat format1 = new SimpleDateFormat(formatText,Locale.forLanguageTag(Utils.getLanguage(context)));
            return format1.format(date);
        }

    }
    public static int compare(long datePrevious,long date) {
        Calendar previous = Calendar.getInstance();
        previous.setTime(new Date(datePrevious));

        Calendar compare = Calendar.getInstance();
        compare.setTime(new Date(date));

        if(previous.get(Calendar.DAY_OF_YEAR)==compare.get(Calendar.DAY_OF_YEAR) &&
                previous.get(Calendar.YEAR)==compare.get(Calendar.YEAR) ) {
            return 0;
        }else    {
            return 1;
        }

    }
}
