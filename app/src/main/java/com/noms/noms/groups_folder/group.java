package com.noms.noms.groups_folder;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.noms.noms.R;
import com.noms.noms.events_folder.events;
import com.noms.noms.friends_folder.friends;
import com.noms.noms.globals;
import com.noms.noms.main_folder.login;
import com.noms.noms.settings_folder.setting;
import com.noms.noms.userLocalStore;
import com.noms.noms.user_info.user_info;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class group extends AppCompatActivity implements View.OnClickListener{

    Button Bcreate_group;
    ListView list;
    ArrayList<group_row_values> groups;

    userLocalStore userLocalStore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        globals G = new globals();
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(G.get_main_color())));
        String title_HTML = "<font color=" + G.get_title_color() + ">Groups</font>";
        getSupportActionBar().setTitle(Html.fromHtml(title_HTML));
        //Server Request
        userLocalStore = new userLocalStore(this);
        int id = userLocalStore.getUserID();

        String file_name = "userGroups.php";
        ArrayList<String> key = new ArrayList<>(Arrays.asList("user"));
        ArrayList<String> value = new ArrayList<>(Arrays.asList(Integer.toString(id)));
        server_request serverRequest = new server_request(this);
        serverRequest.server_request(file_name, key, value, new callback() {
            @Override
            public void done(String response) {
                try {
                    groups = new ArrayList<group_row_values>();

                    JSONObject jObject = new JSONObject(response);
                    String status = jObject.getString("status");
                    if (!status.equals("success")) {
                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(group.this);
                        dialogBuilder.setMessage("Error");
                        dialogBuilder.setPositiveButton("Ok", null);
                        dialogBuilder.show();
                    } else {
                        //store stuff we need
                        int num_groups = jObject.getInt("num_groups");

                        for (int i = 0; i < num_groups; i++) {
                            String tempID = Integer.toString(i);
                            String group_name = jObject.getJSONArray(tempID).getString(0);
                            int num_members = jObject.getJSONArray(tempID).getInt(1);
                            group_row_values temp = new group_row_values(group_name, num_members);
                            groups.add(temp);
                        }
                        list = (ListView) findViewById(R.id.group_list_group);
                        group_list_controller dataAdapter = new group_list_controller(group.this, R.id.friend_name_aftg, groups);
                        list.setAdapter(dataAdapter);
                        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                String group_name = groups.get(position).name;
                                Intent i = new Intent(group.this,group_info.class);
                                i.putExtra("group_name",group_name);
                                startActivity(i);
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }


        });

        Bcreate_group = (Button) findViewById(R.id.add_group_group);
        Bcreate_group.setOnClickListener(group.this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_group, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.home_main:
                Intent i = new Intent(getApplicationContext(), events.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                break;

            case R.id.user_profile_main:
                startActivity(new Intent(this, user_info.class));
                break;

            case R.id.friends_main:
                startActivity(new Intent(this, friends.class));
                break;

            case R.id.group_main:
                startActivity(new Intent(this,group.class));
                break;


            case R.id.logout_main:
                userLocalStore.clearUserData();
                userLocalStore.setUserLoggedIn(false);
                startActivity(new Intent(this, login.class));
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClick(View v){
        switch(v.getId()){
            case R.id.add_group_group:
                startActivity(new Intent(this, create_group.class));
        }
    }
}
