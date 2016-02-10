package com.noms.noms.events_folder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by Paul on 1/2/2016.
 */
public class events_comparator implements Comparator<upcoming_events_row_values>{
    public int compare(upcoming_events_row_values left, upcoming_events_row_values right)
    {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date left_date = format.parse(left.start_datetime);
            Date right_date = format.parse(right.start_datetime);
            return left_date.compareTo(right_date);
        }
        catch (Exception e) {
            e.printStackTrace();

        }
        return 0;
    }
}
