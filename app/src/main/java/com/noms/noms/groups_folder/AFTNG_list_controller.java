package com.noms.noms.groups_folder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.noms.noms.R;

import java.util.ArrayList;

/**
 * Created by Paul on 9/14/2015.
 */
public class AFTNG_list_controller extends ArrayAdapter<AFTNG_friend_row_values> {
    private ArrayList<AFTNG_friend_row_values> list;
    Context context;

    public AFTNG_list_controller(Context context, int textViewResourceID, ArrayList<AFTNG_friend_row_values> rowDataList)
    {
        super(context,textViewResourceID, rowDataList);
        this.list = new ArrayList<>();
        this.list.addAll(rowDataList);
        this.context = context;
    }

    public View getView(final int position, View convertView, ViewGroup parent)
    {
        AFTNG_friend_list_row_holder holder = new AFTNG_friend_list_row_holder();
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.layout_aftng_list, null);
        holder.name = (TextView) convertView.findViewById(R.id.friend_name_AFTNG);


        holder.name.setText(list.get(position).full_name());

        return convertView;
    }
}
