package com.example.paul.sendinfuture;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.UUID;

import database.BoxDbSchema.*;
import database.BoxBaseHelper;
import database.BoxCursorWrapper;
import database.BoxDbSchema;

/**
 * Created by Paul on 10.07.2016.
 */
public class BoxOffice {
    private static BoxOffice sBoxOffice;
    private SQLiteDatabase mDatabase;
    private Context mContext;

    public static BoxOffice get(Context context){
        if(sBoxOffice == null){
            sBoxOffice = new BoxOffice(context);
        }
        return sBoxOffice;
    }

    public void removeDeliveredBoxes(){
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        mDatabase.delete(BoxDbSchema.BoxTable.NAME, BoxTable.Cols.CALENDAR + " < " + calendar.getTimeInMillis()
                + " AND " + BoxTable.Cols.ISINFUTURE + " = 1", null);
    }

    private BoxOffice(Context context){
        mContext = context.getApplicationContext();
        mDatabase = new BoxBaseHelper(mContext)
                .getWritableDatabase();
    }

    public void addBox(Box box){
        ContentValues values = getContentValues(box);

        mDatabase.insert(BoxDbSchema.BoxTable.NAME, null, values);
    }

    public List<Box> getBoxes(){
        List<Box> boxes = new ArrayList<>();

        BoxCursorWrapper cursor = queryBoxes(null, null);

        try{
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                boxes.add(cursor.getBox());
                cursor.moveToNext();
            }
        }finally {
            cursor.close();
        }

        return boxes;
    }

    public Box getBox(UUID id){
        BoxCursorWrapper cursor = queryBoxes(
                BoxTable.Cols.UUID + " = ?",
                new String[]{id.toString()});

        try {
            if(cursor.getCount() == 0){
                return null;
            }

            cursor.moveToFirst();
            return cursor.getBox();
        }finally {
            cursor.close();
        }

    }

    public void updateBox(Box box){
        String uuidString = box.getId().toString();
        ContentValues values = getContentValues(box);

        mDatabase.update(BoxDbSchema.BoxTable.NAME, values, BoxDbSchema.BoxTable.Cols.UUID + " = ?", new String[]{uuidString});
    }

    private static ContentValues getContentValues(Box box){
        ContentValues values = new ContentValues();
        values.put(BoxTable.Cols.UUID, box.getId().toString());
        values.put(BoxTable.Cols.TITLE, box.getTitle());
        values.put(BoxTable.Cols.CALENDAR, box.getCalendarInMillis());
//        values.put(BoxTable.Cols.HOUR, box.getHour());
//        values.put(BoxTable.Cols.MINUTE, box.getMinute());
//        values.put(BoxTable.Cols.DATE, box.getDate().getTime());
        values.put(BoxTable.Cols.MESSAGE, box.getMessage());
        values.put(BoxTable.Cols.ISINFUTURE, box.isInFuture() ? 1 : 0);

        return values;
    }

    private BoxCursorWrapper queryBoxes(String whereClause, String[] whereArgs){
        Cursor cursor = mDatabase.query(
                BoxTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new BoxCursorWrapper(cursor);
    }
}
