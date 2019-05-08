package com.demo.api.commons.utils;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Created by wanghw on 2019-03-11.
 */
public class DateUtil {
    public static DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    public static DateTimeFormatter timeFormatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static DateTimeFormatter mongoFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyyMMdd");
    public static SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat("yyyy-MM-dd");

    public static LocalDateTime timeParse2(String time){
        try {
            return LocalDateTime.parse(time,timeFormatter);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static LocalDateTime timeParse(String time){
        try {
            return LocalDateTime.parse(time,timeFormatter2);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    public static LocalDateTime mongoParse(String time){
        try {
            return LocalDateTime.parse(time,mongoFormat);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static Date mongoParseDate(String time){
        try {
            ZonedDateTime zonedDateTime = LocalDateTime.parse(time,mongoFormat).atZone(ZoneId.systemDefault());
            return Date.from(zonedDateTime.toInstant());
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static String timeFormat(LocalDateTime localDateTime){
        try {
            return timeFormatter.format(localDateTime);
        }catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }
    public static String timeFormat(Date date){
        try {
            return simpleDateFormat.format(date);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static String timeFormat2(Date date){
        try {
            return simpleDateFormat2.format(date);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static String timeFormat3(Date date){
        try {
            return simpleDateFormat3.format(date);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}