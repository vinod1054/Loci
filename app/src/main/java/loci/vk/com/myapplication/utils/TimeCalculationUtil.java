package loci.vk.com.myapplication.utils;

import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import io.realm.RealmResults;
import loci.vk.com.myapplication.model.LocationAudit;

/**
 * Created by vinod on 21/2/16.
 */
public class TimeCalculationUtil {

    public static final String TAG="MyTimeCalculationUtil";
    public static final SimpleDateFormat yyyMMddhhmma = new SimpleDateFormat("yyyy/MM/dd hh:mm a");
    public static final SimpleDateFormat hhmma = new SimpleDateFormat("hh:mm a");
    public static final SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyy-MM-dd");

    public static long calculateTimeSpentInLocation(RealmResults<LocationAudit> locationAuditRealmService){

        long totalTime=0,tempTime=0;
        boolean isPrevious=false;
        long previousTimeStamp=0;
        Iterator<LocationAudit> iterator=locationAuditRealmService.iterator();
        if(iterator.hasNext()){
            previousTimeStamp=iterator.next().getTimeStamp();
        }
        while(iterator.hasNext()){
            LocationAudit locationAudit=iterator.next();
            Date temp=new Date(locationAudit.getTimeStamp());
            Log.i(TAG,"Date "+temp.toString()+" "+locationAudit.isSameAsPrevious());
            if(locationAudit.isSameAsPrevious())
                totalTime+=locationAudit.getTimeStamp()-previousTimeStamp;
            previousTimeStamp=locationAudit.getTimeStamp();
        }

        return totalTime/(1000*60);
    }

    public static String hoursAndMinutes(long minutes){
        StringBuffer sb=new StringBuffer("");
        if(minutes/60>9)
            sb.append(minutes/60+":");
        else if(minutes/60==0)
            ;
        else
            sb.append("0"+minutes/60+":");
        if(sb.toString().compareTo("")==0)
            return ""+minutes%60+" mins";
        if(minutes%60>9)
            sb.append(minutes%60+"");
        else
            sb.append("0"+minutes%60);
        sb.append(" hrs");
        return sb.toString();
    }


    public static String getyyyMMddhhmma(Date date){

        return yyyMMddhhmma.format(date);
    }

    public static String gethhmma(Date date){

        return hhmma.format(date);
    }

    public static Date getToday(){
        Date todayWithZeroTime=new Date();
        try {
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            todayWithZeroTime = formatter.parse(formatter.format(todayWithZeroTime));
        }
        catch (Exception e){
            Log.i(TAG,e.toString());
        }
        return  todayWithZeroTime;
    }

    public static String getDisplayDate(long timeStamp){

        if(timeStamp==getToday().getTime())
            return "Today";
        else if((getToday().getTime()-timeStamp)==(24*60*60*1000))
            return "Yesterday";
        else
            return yyyyMMdd.format(new Date(timeStamp));

    }

    public static String getDayInWeek(Date date){
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        int dayInt=calendar.get(Calendar.DAY_OF_WEEK);
        switch (dayInt){
            case Calendar.SUNDAY:
                return "Sunday";
            case Calendar.MONDAY:
                return "Monday";
            case Calendar.TUESDAY:
                return "Tuesday";
            case Calendar.WEDNESDAY:
                return "Wednesday";
            case Calendar.THURSDAY:
                return "Thursday";
            case Calendar.FRIDAY:
                return "Friday";
            case Calendar.SATURDAY:
                return "Saturday";
            default:
                return "";
        }
    }

}
