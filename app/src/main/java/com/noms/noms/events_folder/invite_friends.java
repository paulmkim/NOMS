package com.noms.noms.events_folder;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.noms.noms.R;
import org.json.JSONObject;

import com.noms.noms.friends_folder.friends;
import com.noms.noms.globals;
import com.noms.noms.groups_folder.group;
import com.noms.noms.main_folder.login;
import com.noms.noms.settings_folder.setting;
import com.noms.noms.userLocalStore;
import com.noms.noms.user_info.user_info;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class invite_friends extends AppCompatActivity implements View.OnClickListener {

    //intent values
    Intent intent;
    String event_name;
    String event_address;
    int day;
    int start_hour;
    int start_minute;
    int start_ampm;
    int dur_hour;
    int dur_minute;

    Boolean friend_tab = true;

    Button Bcreate_event;
    Button Bgroups;
    Button Bfriends;
    ListView list;
    ArrayList<invite_friends_row_values> friends;
    ArrayList<invite_friends_row_values> groups;

    ArrayList<Integer> added_friend_ids;
    ArrayList<Integer> added_friend_position;
    ArrayList<String> added_group_names;
    ArrayList<Integer> added_group_position;

    com.noms.noms.userLocalStore userLocalStore;

    globals G;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_friends);


        G = new globals();
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(G.get_main_color())));
        String title_HTML = "<font color=" + G.get_title_color() + ">Invite Friends</font>";
        getSupportActionBar().setTitle(Html.fromHtml(title_HTML));

        intent = getIntent();
        if(intent != null) {
            event_name = intent.getStringExtra("event_name");
            event_address = intent.getStringExtra("event_address");
            day = intent.getIntExtra("day", 0);
            start_hour = intent.getIntExtra("start_hour",0);
            start_minute = intent.getIntExtra("start_minute",0);
            start_ampm = intent.getIntExtra("start_ampm",0); //1 am 2 pm
            dur_hour = intent.getIntExtra("dur_hour",0);
            dur_minute = intent.getIntExtra("dur_minute", 0);
            }
        else{
            Log.d("test", "critical error:1"); //TODO(postCB): what should I do if intent is null?
        }

        friends = new ArrayList<>();
        groups = new ArrayList<>();
        added_friend_ids = new ArrayList<>();
        added_friend_position = new ArrayList<>();
        added_group_names = new ArrayList<>();
        added_group_position = new ArrayList<>();

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
                    groups = new ArrayList<invite_friends_row_values>();

                    JSONObject jObject = new JSONObject(response);
                    String status = jObject.getString("status");
                    if (!status.equals("success")) {
                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(invite_friends.this);
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
                            invite_friends_row_values temp = new invite_friends_row_values(false,-1,"","",group_name,num_members);
                            groups.add(temp);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }


        });


        //Server Request
        file_name = "userFriends.php";
        serverRequest = new server_request(this);
        serverRequest.server_request(file_name, key, value, new callback() {
            @Override
            public void done(String response) {
                try {
                    friends = new ArrayList<invite_friends_row_values>();

                    JSONObject jObject = new JSONObject(response);
                    String status = jObject.getString("status");
                    if (!status.equals("success")) {
                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(invite_friends.this);
                        dialogBuilder.setMessage("Error");
                        dialogBuilder.setPositiveButton("Ok", null);
                        dialogBuilder.show();
                    } else {
                        //store stuff we need
                        int num_friends = jObject.getInt("num_friends");

                        for (int i = 0; i < num_friends; i++) {
                            String tempID = Integer.toString(i);
                            int id = jObject.getJSONArray(tempID).getInt(0);
                            String first_name = jObject.getJSONArray(tempID).getString(1);
                            String last_name = jObject.getJSONArray(tempID).getString(2);
                            invite_friends_row_values temp = new invite_friends_row_values(true, id, first_name, last_name, "", -1);
                            friends.add(temp);
                        }
                        list = (ListView) findViewById(R.id.friends_list_invite_friends);
                        invite_friends_list_controller dataAdapter = new invite_friends_list_controller(invite_friends.this, R.id.friends_list_invite_friends, friends,added_friend_position);
                        list.setAdapter(dataAdapter);

                        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                if(friend_tab) {
                                    int temp_id = friends.get(position).id;
                                    if (added_friend_ids.remove(new Integer(temp_id))) {
                                        added_friend_position.remove(new Integer(position));
                                        list.getChildAt(position).setBackgroundColor(Color.parseColor(G.get_grey_color()));
                                    } else {
                                        added_friend_ids.add(temp_id);
                                        added_friend_position.add(position);
                                        list.getChildAt(position).setBackgroundColor(Color.parseColor(G.get_main_color()));
                                    }
                                }
                                else
                                {
                                    String temp_name = groups.get(position).group_name;
                                    if(added_group_names.remove(temp_name))
                                    {
                                        added_group_position.remove(new Integer(position));
                                        list.getChildAt(position).setBackgroundColor(Color.parseColor(G.get_grey_color()));
                                    }
                                    else {
                                        added_group_names.add(temp_name);
                                        added_group_position.add(position);
                                        list.getChildAt(position).setBackgroundColor(Color.parseColor(G.get_main_color()));
                                    }
                                }
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }


        });

        Bcreate_event = (Button) findViewById(R.id.create_event_button_invite_friends);
        Bgroups = (Button) findViewById(R.id.groups_invite_friends);
        Bfriends = (Button) findViewById(R.id.friends_invite_friends);

        Bcreate_event.setOnClickListener(invite_friends.this);
        Bgroups.setOnClickListener(invite_friends.this);
        Bfriends.setOnClickListener(invite_friends.this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_invite_friends, menu);
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
        invite_friends_list_controller dataAdapter_group;
        invite_friends_list_controller dataAdapter_friend;

        switch(v.getId()){
            case R.id.create_event_button_invite_friends:
                userLocalStore = new userLocalStore(this);
                int user_id = userLocalStore.getUserID();
                String file_name = "createEvent.php";
                //event_name
                //start_datetime
                Calendar c = Calendar.getInstance();
                c.add(Calendar.DATE, day);
                if(start_ampm == 1) //am
                    c.set(Calendar.HOUR, start_hour);
                else
                    c.set(Calendar.HOUR, start_hour + 12);
                c.set(Calendar.MINUTE, start_minute);
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String start_datetime = df.format(c.getTime());
                //duration
                String duration = dur_hour + "," + dur_minute + ",";
                //address
                String address = event_address;
                String invite_friends = "";
                for(int temp_friend_ids : added_friend_ids)
                {
                    invite_friends = invite_friends + temp_friend_ids + ",";
                }
                String invite_groups = "";
                for(String temp_group_names : added_group_names)
                {
                    invite_groups = invite_groups + temp_group_names + ",";
                }
                ArrayList<String> key = new ArrayList<>(Arrays.asList("user", "event_name", "start_datetime", "duration", "address", "invite_friends", "invite_groups"));
                ArrayList<String> value = new ArrayList<>(Arrays.asList(Integer.toString(user_id), event_name, start_datetime, duration, address, invite_friends, invite_groups));
                server_request serverRequest = new server_request(this);
                serverRequest.server_request(file_name, key, value, new callback() {
                    @Override
                    public void done(String response) {
                        try {
                            JSONObject jObject = new JSONObject(response);
                            String status = jObject.getString("status");
                            if (status.equals("success")) {
                                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(invite_friends.this);
                                dialogBuilder.setMessage("event created");
                                dialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        startActivity(new Intent(invite_friends.this, events.class));
                                    }
                                });
                                dialogBuilder.show();
                            } else {
                                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(invite_friends.this);
                                dialogBuilder.setMessage("error. try again");
                                dialogBuilder.setPositiveButton("Ok", null);
                                dialogBuilder.show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();

                        }
                    }


                });
                break;
            case R.id.groups_invite_friends:
                friend_tab = false;
                list = (ListView) findViewById(R.id.friends_list_invite_friends);
                dataAdapter_group = new invite_friends_list_controller(invite_friends.this, R.id.friends_list_invite_friends, groups,added_group_position);
                list.setAdapter(dataAdapter_group);
                break;

            case R.id.friends_invite_friends:
                friend_tab = true;
                list = (ListView) findViewById(R.id.friends_list_invite_friends);
                dataAdapter_friend = new invite_friends_list_controller(invite_friends.this, R.id.friends_list_invite_friends, friends,added_friend_position);
                list.setAdapter(dataAdapter_friend);
                break;
        }
    }
}
