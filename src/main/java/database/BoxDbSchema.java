package database;

/**
 * Created by Paul on 10.07.2016.
 */
public class BoxDbSchema {
    public static final class BoxTable{
        public static final String NAME = "boxes";

        public static final class Cols{
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String CALENDAR = "calendar";
//            public static final String DATE = "date";
//            public static final String HOUR = "hour";
//            public static final String MINUTE = "minute";
            public static final String MESSAGE = "message";
            public static final String ISINFUTURE = "isinfuture";

        }
    }
}
