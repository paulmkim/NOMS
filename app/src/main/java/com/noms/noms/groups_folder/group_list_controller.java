package com.noms.noms.groups_folder;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.noms.noms.R;
import com.noms.noms.friends_folder.friends;
import com.noms.noms.userLocalStore;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Paul on 9/14/2015.
 */
public class group_list_controller extends ArrayAdapter<group_row_values> {
    private ArrayList<group_row_values> list;
    Context context;

    public group_list_controller(Context context, int textViewResourceID, ArrayList<group_row_values> rowDataList)
    {
        super(context,textViewResourceID, rowDataList);
        this.list = new ArrayList<>();
        this.list.addAll(rowDataList);
        this.context = context;
    }

    public View getView(final int position, View convertView, final ViewGroup parent)
    {
        group_list_row_holder holder = new group_list_row_holder();
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.layout_group_list, null);
        holder.name = (TextView) convertView.findViewById(R.id.friend_name_aftg);
        holder.delete_group = (Button) convertView.findViewById(R.id.dg_button_group);


        holder.delete_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                dialogBuilder.setMessage("Are you sure?");
                dialogBuilder.setNegativeButton("No", null);
                        dialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                userLocalStore userLocalStore = new userLocalStore(context);
                                int id = userLocalStore.getUserID();

                                String file_name = "deleteGroup.php";
                                ArrayList<String> key = new ArrayList<>(Arrays.asList("user", "group_name"));
                                ArrayList<String> value = new ArrayList<>(Arrays.asList(Integer.toString(id), list.get(position).name));
                                server_request serverRequest = new server_request(context);
                                serverRequest.server_request(file_name, key, value, new callback() {
                                    @Override
                                    public void done(String response) {
                                        try {
                                            JSONObject jObject = new JSONObject(response);
                                            String status = jObject.getString("status");
                                            if (!status.equals("success")) {
                                                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                                                dialogBuilder.setMessage("Error");
                                                dialogBuilder.setPositiveButton("Ok", null);
                                                dialogBuilder.show();
                                            } else {
                                                Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                                                Intent i = new Intent(parent.getContext(), group.class);
                                                parent.getContext().startActivity(i);
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();

                                        }
                                    }


                                });
                            }
                        });
                dialogBuilder.show();

            }
        });
        holder.name.setText(list.get(position).name);

        return convertView;
    }
}
