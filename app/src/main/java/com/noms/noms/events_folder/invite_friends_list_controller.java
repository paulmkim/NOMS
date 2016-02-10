package com.noms.noms.events_folder;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.noms.noms.R;
import com.noms.noms.globals;

import java.util.ArrayList;

/**
 * Created by Paul on 9/14/2015.
 */
public class invite_friends_list_controller extends ArrayAdapter<invite_friends_row_values> {
    private ArrayList<invite_friends_row_values> list;
    private ArrayList<Integer> position_list;
    Context context;
    globals G;
    public invite_friends_list_controller(Context context, int textViewResourceID, ArrayList<invite_friends_row_values> rowDataList, ArrayList<Integer> position_list)
    {
        super(context,textViewResourceID, rowDataList);
        this.list = new ArrayList<>();
        this.list.addAll(rowDataList);
        this.context = context;
        this.position_list = position_list;
        G = new globals();
    }

    public View getView(final int position, View convertView, ViewGroup parent)
    {
        invite_friends_list_row_holder holder = new invite_friends_list_row_holder();
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.layout_aftng_list, null);
        holder.name = (TextView) convertView.findViewById(R.id.friend_name_AFTNG);

        holder.name.setText(list.get(position).full_name());
        if(position_list.contains(position))
        {
            convertView.setBackgroundColor(Color.parseColor(G.get_main_color()));
        }
        else
        {
            convertView.setBackgroundColor(Color.parseColor(G.get_grey_color()));
        }

        return convertView;
    }
}
