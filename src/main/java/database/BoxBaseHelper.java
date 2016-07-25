package database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import database.BoxDbSchema.BoxTable;

/**
 * Created by Paul on 10.07.2016.
 */
public class BoxBaseHelper extends SQLiteOpenHelper{

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "boxBase.db";

    public BoxBaseHelper(Context context){
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("create table " + BoxTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                BoxTable.Cols.UUID + ", " +
                BoxTable.Cols.TITLE + ", " +
                BoxTable.Cols.CALENDAR + ", " +
//                BoxTable.Cols.DATE + ", " +
//                BoxTable.Cols.HOUR + ", " +
//                BoxTable.Cols.MINUTE + ", " +
                BoxTable.Cols.MESSAGE + ", " +
                BoxTable.Cols.ISINFUTURE +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }

}

