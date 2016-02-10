package com.noms.noms.events_folder;

/**
 * Created by Paul on 1/2/2016.
 */
public class upcoming_events_row_values {
    public String event_name;
    public int attendance_status; //0: no response, 1: yes, 2: no, 3: creator
    public String start_datetime;
    public String end_datetime;
    public String address;
    public int creator;

    public upcoming_events_row_values(String event_name, int attendance_status, String start_datetime, String end_datetime, String address, int creator)
    {
        this.event_name = event_name;
        this.attendance_status = attendance_status;
        this.start_datetime = start_datetime;
        this.end_datetime = end_datetime;
        this.address = address;
        this.creator = creator;
    }
}
