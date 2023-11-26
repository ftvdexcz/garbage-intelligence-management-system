package ptit.gms.utils;

import java.sql.Timestamp;

public class TimeUtils {
    public static long getCurrentTimestampMs(){
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return timestamp.getTime();
    }
}
