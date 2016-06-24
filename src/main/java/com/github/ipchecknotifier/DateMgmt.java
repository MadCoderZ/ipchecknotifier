package com.github.ipchecknotifier;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateMgmt {

    /**
     * Gets the current date in a fancy format.
     * @return
     */
    public static String getDate()
    {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }
}
