package gms.ptit.utils;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

public class DateUtils {
    public static String formattedDate(Date date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return dateFormat.format(date);
    }

    public static String formattedTimestamp(Long timestamp){
        Instant instant = Instant.ofEpochMilli(timestamp);
        Date date = Date.from(instant);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return dateFormat.format(date);
    }
}
