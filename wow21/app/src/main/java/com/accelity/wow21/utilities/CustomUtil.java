package com.accelity.wow21.utilities;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * Created by Rahul on 09-07-2016.
 */
public class CustomUtil {
    private String TIME;
    private String DATE;

    public static Toast toast = null;
    public static void showToast(Context context, String msg){
        if(toast != null)             //this will cancel the toast on the screen if one exists
            toast.cancel();
        toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        toast.show();
    }

    public static String convertTo2Decimal(Double dblnum){
        final NumberFormat nf = NumberFormat.getInstance();
        nf.setMinimumFractionDigits(2);
        nf.setMaximumFractionDigits(2);
        nf.setGroupingUsed(false);
        return nf.format(dblnum);
    }
    public static String getTime(String TimeStr){
        SimpleDateFormat targetTimeFormat = new SimpleDateFormat("hh:mm aa");
        Calendar cal = Calendar.getInstance();
        //TimeZone tz = cal.getTimeZone();
        //targetTimeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return targetTimeFormat.format(removeDust(TimeStr));
    }
    public static String getDate(String DateString){
        SimpleDateFormat targetDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cal = Calendar.getInstance();
        //TimeZone tz = cal.getTimeZone();
        //targetDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return targetDateFormat.format(removeDust(DateString));
    }
    public static String getFullDate(String DateString){
        SimpleDateFormat targetDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss aa");
        Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone();
        targetDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return targetDateFormat.format(removeDust(DateString));
    }
    public static String getPassbookDate(String DateString){
        Date fDate = removeDust(DateString);
        String fullDate = "";
        String stringMonth = (String) android.text.format.DateFormat.format("MMM", fDate);
        String stringDay = (String) android.text.format.DateFormat.format("dd", fDate);
        String stringTime = (String) android.text.format.DateFormat.format("hh:mm aa", fDate);
        fullDate = stringMonth+" "+stringDay+" at "+stringTime;
        return fullDate;
    }
    public static Date removeDust(String dateStr){
        String dealdate = dateStr;
        dealdate = dealdate.replaceAll("\\(|\\)", "");
        dealdate = dealdate.replace("Date", "");
        dealdate = dealdate.replaceAll("\\/","");
        Long miliseconds = Long.valueOf(dealdate);
        Timestamp tsp = new Timestamp(miliseconds);
        Date dateformat = new Date(tsp.getTime());
        return dateformat;
    }
    public static Long convertToTimeStamp(String dateStr){
        String dealdate = dateStr;
        dealdate = dealdate.replaceAll("\\(|\\)", "");
        dealdate = dealdate.replace("Date", "");
        dealdate = dealdate.replaceAll("\\/","");
        Long miliseconds = Long.valueOf(dealdate);
        //Timestamp tsp = new Timestamp(miliseconds);
        return miliseconds;
    }
    public static Double convertStringTo2Double(String strDouble){
        String convertStr =  String.format("%.2f", Double.parseDouble(strDouble));
        return Double.parseDouble(convertStr);
    }
    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public static String getAcualTimeLeftInHour(String dateStop) {
        String strTime = "0";
        Date d1 = null;
        Date d2 = null;
        try {
            d1 = new Date();
            String dealdate = dateStop;
            dealdate = dealdate.replaceAll("\\(|\\)", "");
            dealdate = dealdate.replace("Date", "");
            dealdate = dealdate.replaceAll("\\/","");
            d2 = new java.util.Date(dealdate);
            strTime = getDateDiff(d1, d2, TimeUnit.HOURS)+"";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strTime;
    }

    public static String getAcualTimeLeft(String dateStop){
        String strTime = "";
        try {
            String dealdate = dateStop;
            dealdate = dealdate.replaceAll("\\(|\\)", "");
            dealdate = dealdate.replace("Date", "");
            dealdate = dealdate.replaceAll("\\/","");
            long timestampString1 =  Long.parseLong(dealdate);
            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            Date value2 = new java.util.Date(timestampString1);
            Date value1 = new Date();
            //long diffTime = value2.getTime() - value1.getTime();
           // String dfff = getDateDiff(value1, value2, TimeUnit.HOURS)+""; // "+getDateDiff(value1, value2, TimeUnit.MINUTES)%60+" "+getDateDiff(value1, value2, TimeUnit.SECONDS)%3600;


            //long diff = d2.getTime() - d1.getTime();
            long diffDays = getDateDiff(value1, value2, TimeUnit.DAYS);
            long diffHours = getDateDiff(value1, value2, TimeUnit.HOURS);
            long diffMinutes = getDateDiff(value1, value2, TimeUnit.MINUTES)%60;
            long ttdM = getDateDiff(value1, value2, TimeUnit.MINUTES)%60 +1;
            long diffSeconds = (ttdM)*60 - getDateDiff(value1, value2, TimeUnit.SECONDS)%3600;

            if(diffHours != 0){
                if(diffHours < 10){
                    strTime = "0"+diffHours+":";
                }else{
                    strTime = diffHours+":";
                }
            }else{

            }
            if(diffMinutes != 0){
                if(diffMinutes < 10){
                    strTime = strTime+"0"+diffMinutes+":";
                }else{
                    strTime = strTime+diffMinutes+":";
                }
            }else{
                strTime = strTime+"00"+":";
            }
            if(diffSeconds != 0){
                if(diffSeconds < 10){
                    strTime = strTime+"0"+diffSeconds;
                }else{
                    strTime = strTime+diffSeconds;
                }
            }else{
                strTime = strTime+"00";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return strTime;
    }


    public static String getAcualTimeLeft22(Date value1, Date value2){
        String strTime = "";
        try {
            long diffDays = getDateDiff(value1, value2, TimeUnit.DAYS);
            long diffHours = getDateDiff(value1, value2, TimeUnit.HOURS);
            long diffMinutes = getDateDiff(value1, value2, TimeUnit.MINUTES)%60;
            long ttdM = getDateDiff(value1, value2, TimeUnit.MINUTES)%60 +1;
            long diffSeconds = (ttdM)*60 - getDateDiff(value1, value2, TimeUnit.SECONDS)%3600;

            if(diffHours != 0){
                if(diffHours < 10){
                    strTime = "0"+diffHours+":";
                }else{
                    strTime = diffHours+":";
                }
            }else{

            }
            if(diffMinutes != 0){
                if(diffMinutes < 10){
                    strTime = strTime+"0"+diffMinutes+"";
                }else{
                    strTime = strTime+diffMinutes+"";
                }
            }else{
                strTime = strTime+"00"+"";
            }
            if(diffSeconds != 0){
               // if(diffSeconds < 10){
                 //   strTime = strTime+"0"+diffSeconds;
               // }else{
               //     strTime = strTime+diffSeconds;
               // }
            }else{
               // strTime = strTime+"00";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return strTime;
    }

    public static long totalTimeDiff(String dateStop){
        String dealdate = dateStop;
        dealdate = dealdate.replaceAll("\\(|\\)", "");
        dealdate = dealdate.replace("Date", "");
        dealdate = dealdate.replaceAll("\\/","");
        long timestampString1 =  Long.parseLong(dealdate);
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date value2 = new java.util.Date(timestampString1);
        Date value1 = new Date();
        return  value2.getTime() - value1.getTime();
    }

    public static long totalTimeDiff2(String dateStop){
        long totalSec = 0;
        Date cDate = new Date();
        String dateStart = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss aa").format(cDate);
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss aa");
        Date d1 = null;
        Date d2 = null;
        try {
            d1 = format.parse(dateStart);
            d2 = format.parse(dateStop);
            totalSec = d1.getTime() - d2.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return totalSec;
    }
    public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
    }

    public static String testtt(String ggg){
        String dealdate = ggg;
        dealdate = dealdate.replaceAll("\\(|\\)", "");
        dealdate = dealdate.replace("Date", "");
        dealdate = dealdate.replaceAll("\\/","");
        long timestampString1 =  Long.parseLong(dealdate);
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date value2 = new java.util.Date(timestampString1);
        Date value1 = new Date();
        long diffTime = value2.getTime() - value1.getTime();
        String dfff = getDateDiff(value1, value2, TimeUnit.HOURS)+""; // "+getDateDiff(value1, value2, TimeUnit.MINUTES)%60+" "+getDateDiff(value1, value2, TimeUnit.SECONDS)%3600;
        return dfff;
    }

}
