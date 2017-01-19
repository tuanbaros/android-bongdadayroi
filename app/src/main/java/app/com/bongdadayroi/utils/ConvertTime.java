package app.com.bongdadayroi.utils;

import android.content.Context;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Administrator on 12/1/2015.
 */
public class ConvertTime {

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;
    private static final int WEEK_MILLIS = 7 * DAY_MILLIS;
    private static final long MONTH_MILLIS = (long)30 * DAY_MILLIS;
    private static final long YEAR_MILLIS = 12 * MONTH_MILLIS;


    public static void convertTime(String date, TextView time, Context context){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();

        try {
            calendar.setTime(format.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = (calendar.get(Calendar.MONTH)+1);

        time.setText("" + getGoodTime(hour) + ":" +
                "" + getGoodTime(minute) + " - " +
                "" + getGoodTime(day) + "/" +
                "" + getGoodTime(month) + "/" +
                "" + calendar.get(Calendar.YEAR));
    }


    public static void getTimeDifference(String pDate, TextView time){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT"));
        int year1 = calendar.get(Calendar.YEAR);
        int month1 = calendar.get(Calendar.MONTH)+1;
        int day1 = calendar.get(Calendar.DAY_OF_MONTH);
        int hour1 = calendar.get(Calendar.HOUR_OF_DAY);
        int minute1 = calendar.get(Calendar.MINUTE);
        int second1 = calendar.get(Calendar.SECOND);
        try {
            calendar.setTime(format.parse(pDate));
            int year2 = calendar.get(Calendar.YEAR);
            int month2 = calendar.get(Calendar.MONTH)+1;
            int day2 = calendar.get(Calendar.DAY_OF_MONTH);
            int hour2 = calendar.get(Calendar.HOUR_OF_DAY);
            int minute2 = calendar.get(Calendar.MINUTE);
            int second2 = calendar.get(Calendar.SECOND);

            if((year1 - year2)!= 0 ){
                if((year1-year2) == 1 && (month1<month2))
                    if(month1 == 1 && month2 ==12 && day1 < day2){
                        time.setText((day1 + 31 - day2) + " ngày trước");
                    }else
                        time.setText((month1+12-month2) + " tháng trước");
                else{
                    time.setText((year1 - year2) + " năm trước");
                }
            }else{
                if((month1-month2) != 0){
                    if((month1-month2) == 1 && (day1<day2))
                        time.setText((day1+30-day2) + " ngày trước");
                    else{
                        time.setText((month1 - month2) + " tháng trước");
                    }
                }else{
                    if((day1 - day2) != 0){
                        if((day1-day2) == 1 && (hour1<hour2))
                            time.setText((hour1+24-hour2) + " giờ trước");
                        else{
                            time.setText((day1 - day2) + " ngày trước");
                        }

                    }else{
                        if((hour1 - hour2) != 0){
                            if((hour1-hour2) == 1 && (minute1<minute2))
                                time.setText((minute1+60-minute2) + " phút trước");
                            else{
                                time.setText((hour1 - hour2) + " giờ trước");
                            }
                        }else{
                            if((minute1 - minute2) != 0){
                                time.setText((minute1 - minute2) + " phút trước");
                            }else{
                                if((second1 - second2) > 0){
                                    time.setText((second1 - second2) + " giây trước");
                                }else{
                                    time.setText("Vừa xong");
                                }
                            }
                        }
                    }
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private static String getGoodTime(int time){
        if(time < 10){
            return "0"+"" + time;
        }else{
            return ""+time;
        }
    }


    public static String getTimeAgoTest(String time, Context context){
        String timeAgo = "";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = format.parse(time);
            long militime = date.getTime();
            if (militime < 1000000000000L) {
                // if timestamp given in seconds, convert to millis
                militime *= 1000;
            }

            long now = System.currentTimeMillis();
            if (militime > now || militime <= 0) {
                return "Vừa xong";
            }

            // TODO: localize
            final long diff = now - militime;
            if (diff < MINUTE_MILLIS) {
                timeAgo = "Vừa xong";
            } else if (diff < 2 * MINUTE_MILLIS) {
                timeAgo = "1 phút trước";
            } else if (diff < 50 * MINUTE_MILLIS) {
                timeAgo = diff / MINUTE_MILLIS + " phút trước";
            } else if (diff < 90 * MINUTE_MILLIS) {
                timeAgo = "1 giờ trước";
            } else if (diff < 24 * HOUR_MILLIS) {
                timeAgo = diff / HOUR_MILLIS + " giờ trước";
            } else if (diff < 48 * HOUR_MILLIS) {
                timeAgo = "Hôm qua";
            } else if(diff < WEEK_MILLIS){
                timeAgo = diff/DAY_MILLIS + " ngày trước";
            } else if(diff < 2 * WEEK_MILLIS){
                timeAgo = "1 tuần trước";
            }else if (diff < MONTH_MILLIS){
                timeAgo = diff/WEEK_MILLIS + " tuần trước";
            }else if (diff < 2*MONTH_MILLIS) {
                timeAgo = "1 tháng trước";
            }else if (diff < YEAR_MILLIS){
                timeAgo = diff/MONTH_MILLIS + " tháng trước";
            }else{
                timeAgo = diff/YEAR_MILLIS + " năm trước";
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return timeAgo;
    }

    public static String getTimeAgo(String time, Context context){
        String timeAgo = "";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = format.parse(time);
            long militime = date.getTime();
            if (militime < 1000000000000L) {
                // if timestamp given in seconds, convert to millis
                militime *= 1000;
            }

            long now = System.currentTimeMillis();
            if (militime > now || militime <= 0) {
                return null;
            }

            // TODO: localize
            final long diff = now - militime;
            if (diff < MINUTE_MILLIS) {
                timeAgo = "Vừa xong";
            } else if (diff < 2 * MINUTE_MILLIS) {
                timeAgo = "1 phút trước";
            } else if (diff < 50 * MINUTE_MILLIS) {
                timeAgo = diff / MINUTE_MILLIS + " phút trước";
            } else if (diff < 90 * MINUTE_MILLIS) {
                timeAgo = "1 giờ trước";
            } else if (diff < 24 * HOUR_MILLIS) {
                timeAgo = diff / HOUR_MILLIS + " giờ trước";
            } else if (diff < 48 * HOUR_MILLIS) {
                timeAgo = "Hôm qua";
            } else {
                timeAgo = diff / DAY_MILLIS + " ngày trước";
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return timeAgo;
    }
}
