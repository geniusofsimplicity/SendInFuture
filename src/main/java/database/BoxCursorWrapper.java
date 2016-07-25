package database;

import android.database.Cursor;
import android.database.CursorWrapper;
import database.BoxDbSchema.*;
import com.example.paul.sendinfuture.Box;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Paul on 10.07.2016.
 */
public class BoxCursorWrapper extends CursorWrapper{
    public BoxCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Box getBox(){
        String uuidString = getString(getColumnIndex(BoxTable.Cols.UUID));
        String title = getString(getColumnIndex(BoxTable.Cols.TITLE));
        long calendarInMillis = getLong(getColumnIndex(BoxTable.Cols.CALENDAR));
//        long date = getLong(getColumnIndex(BoxTable.Cols.DATE));
//        int hour = getInt(getColumnIndex(BoxTable.Cols.HOUR));
//        int minute = getInt(getColumnIndex(BoxTable.Cols.MINUTE));
        String message = getString(getColumnIndex(BoxTable.Cols.MESSAGE));
        int isInFuture = getInt(getColumnIndex(BoxTable.Cols.ISINFUTURE));

        Box box = new Box(UUID.fromString(uuidString));
        box.setTitle(title);
        box.setCalendar(calendarInMillis);
//        box.setDate(new Date(date));
//        box.setTime(hour, minute);
        box.setMessage(message);
        box.setIsInFuture(isInFuture != 0);

        return box;
    }
}
