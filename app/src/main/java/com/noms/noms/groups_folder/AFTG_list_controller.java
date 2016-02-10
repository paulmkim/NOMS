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
public class AFTG_list_controller extends ArrayAdapter<AFTG_friend_row_values> {
    private ArrayList<AFTG_friend_row_values> list;
    Context context;

    public AFTG_list_controller(Context context, int textViewResourceID, ArrayList<AFTG_friend_row_values> rowDataList)
    {
        super(context,textViewResourceID, rowDataList);
        this.list = new ArrayList<>();
        this.list.addAll(rowDataList);
        this.context = context;
    }

    public View getView(final int position, View convertView, ViewGroup parent)
    {
        AFTG_friend_list_row_holder holder = new AFTG_friend_list_row_holder();
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.layout_aftg_list, null);
        holder.name = (TextView) convertView.findViewById(R.id.friend_name_aftg);


        holder.name.setText(list.get(position).full_name());

        return convertView;
    }
}
