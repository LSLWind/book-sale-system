package lsl.java.web.utils;

import lsl.java.web.entity.Book;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateUtil {
    //日期格式
    public static String FORMAT_LONG = "yyyy-MM-dd HH:mm:ss";

    //获取当前时间
    public static String getCurrentDate(){
        SimpleDateFormat df = new SimpleDateFormat(FORMAT_LONG);
        Calendar calendar = Calendar.getInstance();
        return df.format(calendar.getTime());
    }

}
