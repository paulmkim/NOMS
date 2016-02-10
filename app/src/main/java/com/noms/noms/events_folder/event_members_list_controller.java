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
public class event_members_list_controller extends ArrayAdapter<event_members_row_values>{
    private ArrayList<event_members_row_values> list;
    Context context;

    public event_members_list_controller(Context context, int textViewReourceID, ArrayList<event_members_row_values> rowDataList)
    {
        super(context,textViewReourceID, rowDataList);
        this.list = new ArrayList<>();
        this.list.addAll(rowDataList);
        this.context = context;
    }

    public View getView(final int position, View convertView, ViewGroup parent)
    {
        event_members_list_row_holder holder = new event_members_list_row_holder();
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.layout_event_members_list, null);
        holder.name = (TextView) convertView.findViewById(R.id.member_name_event_members);
        holder.name.setText(list.get(position).name);
        return convertView;
    }
}
