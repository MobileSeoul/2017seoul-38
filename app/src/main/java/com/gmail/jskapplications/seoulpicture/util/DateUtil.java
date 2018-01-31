package com.gmail.jskapplications.seoulpicture.util;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by for on 2017-10-28.
 */

public class DateUtil {

    public static Date getTodayDate() {
        Calendar cal = Calendar.getInstance();
        return cal.getTime();
    }

    public static Date getTomorrowOfPostDate(String postDate) {
        if(postDate==null) return null;
        ParsePosition pos = new ParsePosition(0);
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = timeFormat.parse(postDate, pos);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, 1);
        return c.getTime();
    }

    public static Date getPostDate(String postDate) {
        if(postDate==null) return null;
        ParsePosition pos = new ParsePosition(0);
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = timeFormat.parse(postDate, pos);
        return date;
    }
}
