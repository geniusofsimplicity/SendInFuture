package com.example.paul.sendinfuture;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Paul on 19.07.2016.
 */
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener{

    private static final String ARG_HOUR = "hour";
    private static final String ARG_MINUTE = "minute";
    private TimePicker mTimePicker;
    public static final String EXTRA_TIME = "com.example.paul.sendInFuture.TIME";

    public static TimePickerFragment newInstance(int hourOfDay, int minute){

        Bundle args = new Bundle();
        args.putInt(ARG_HOUR, hourOfDay);
        args.putInt(ARG_MINUTE, minute);

        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        Bundle args = getArguments();
        int hour = args.getInt(ARG_HOUR);
        int minute = args.getInt(ARG_MINUTE);

        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));

//        View v = LayoutInflater.from(getActivity())
//                .inflate(R.layout.dialog_time, null);

//        mTimePicker = (TimePicker) v.findViewById(R.id.dialog_time_time_picker);
//        mTimePicker.setHour(hour);
//        mTimePicker.setMinute(minute);

//        return new AlertDialog.Builder(getActivity())
//                .setView(v)
//                .setTitle(R.string.time_picker_title)
//                .setPositiveButton(android.R.string.ok,
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int which) {
////                                int hour = mTimePicker.getHour();
////                                int minute = mTimePicker.getMinute();
////                                Date date = new GregorianCalendar(0, 0, 0, hour, minute).getTime();
////                                sendResult(Activity.RESULT_OK, date);
//
//                            }
//                        }).create();
    }

    private void sendResult(int resultCode, GregorianCalendar time){
        if(getTargetFragment() == null){
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_TIME, time);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        sendResult(Activity.RESULT_OK, calendar);
    }
}
