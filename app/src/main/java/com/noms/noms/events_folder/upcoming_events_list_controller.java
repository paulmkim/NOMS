package com.noms.noms.events_folder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.noms.noms.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Paul on 1/2/2016.
 */
public class upcoming_events_list_controller extends ArrayAdapter<upcoming_events_row_values>{
    private ArrayList<upcoming_events_row_values> list;
    Context context;

    public upcoming_events_list_controller(Context context, int textViewReourceID, ArrayList<upcoming_events_row_values> rowDataList)
    {
        super(context,textViewReourceID, rowDataList);
        this.list = new ArrayList<>();
        this.list.addAll(rowDataList);
        this.context = context;
    }

    public View getView(final int position, View convertView, ViewGroup parent)
    {
        upcoming_events_row_holder holder = new upcoming_events_row_holder();
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.layout_events_list, null);
        holder.name = (TextView) convertView.findViewById(R.id.event_name_upcoming_events);
        holder.date = (TextView) convertView.findViewById(R.id.event_date_upcoming_events);

        holder.name.setText(list.get(position).event_name);

        Date start_date = new Date();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try
        {
            start_date = format.parse(list.get(position).start_datetime);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        DateFormat month_and_day = new SimpleDateFormat("E, M/d h:mm a");
        String date = month_and_day.format(start_date);
        holder.date.setText(date);

        return convertView;
    }
}
