package com.example.paul.sendinfuture;

import android.provider.CalendarContract;
import android.text.format.DateFormat;

import java.util.Calendar;
import java.util.Date;

import java.util.GregorianCalendar;
import java.util.UUID;

/**
 * Created by Paul on 10.07.2016.
 */
public class Box {
    private UUID mid;
    private String mTitle;
//    private Date mDate;
    private boolean isInFuture;
    private String message;
    private GregorianCalendar mCalendar;

    public Box(){
        mid = UUID.randomUUID();
//        mDate = new Date();
        mCalendar = new GregorianCalendar();
        mCalendar.setTime(new Date());
    }

    public Box(UUID id){
        mid = id;
//        mDate = new Date();
        mCalendar = new GregorianCalendar();
        mCalendar.setTime(new Date());
    }

    public UUID getId() {
        return mid;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public boolean isInFuture() {
        return isInFuture;
    }

    public void setIsInFuture(boolean isInFuture) {
        this.isInFuture = isInFuture;
    }

    public Date getDate() {
        return mCalendar.getTime();
    }

    public String getDateString(){
        String dateFormat = "EEE, MMM dd";

        String dateString = DateFormat.format(dateFormat, mCalendar.getTime()).toString();
        return dateString;
    }

    public String getTimeString(){
        int hour = mCalendar.get(Calendar.HOUR_OF_DAY);
        int minute = mCalendar.get(Calendar.MINUTE);
        String minuteString;
        if(minute < 10){
            minuteString = "0" + Integer.toString(minute);
        }else{
            minuteString = Integer.toString(minute);
        }

        return hour + ":" + minuteString;
    }

    public void setTime(int hour, int minute){
        mCalendar.set(Calendar.HOUR_OF_DAY, hour);
        mCalendar.set(Calendar.MINUTE, minute);
    }

    public void setCalendar(long millis){
        mCalendar.setTimeInMillis(millis);
    }

    public long getCalendarInMillis(){
        return mCalendar.getTimeInMillis();
    }

    public int getHour(){
        return mCalendar.get(Calendar.HOUR_OF_DAY);
    }

    public int getMinute(){
        return mCalendar.get(Calendar.MINUTE);
    }

//    public void setDate(Date date) {
//        mDate = date;
//    }

    public void setDate(GregorianCalendar calendar) {
        mCalendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));
        mCalendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
        mCalendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
