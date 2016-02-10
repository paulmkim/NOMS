package com.noms.noms.events_folder;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.noms.noms.R;
import com.noms.noms.friends_folder.friends;
import com.noms.noms.globals;
import com.noms.noms.groups_folder.group;
import com.noms.noms.main_folder.login;
import com.noms.noms.settings_folder.setting;
import com.noms.noms.userLocalStore;
import com.noms.noms.user_info.user_info;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

public class upcoming_events extends AppCompatActivity implements View.OnClickListener{

    userLocalStore userLocalStore;
    ListView list;
    ArrayList<upcoming_events_row_values> going_events;
    ArrayList<upcoming_events_row_values> unsure_events;
    ArrayList<upcoming_events_row_values> not_going_events;
    ArrayList<upcoming_events_row_values> created_events;

    Button Bgoing_events;
    Button Bunsure_events;
    Button Bnot_going_events;

    int current_tab;

    globals G;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming_events);

        G = new globals();
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(G.get_main_color())));
        String title_HTML = "<font color=" + G.get_title_color() + ">Upcoming Events</font>";
        getSupportActionBar().setTitle(Html.fromHtml(title_HTML));

        userLocalStore = new userLocalStore(this);
        int id = userLocalStore.getUserID();

        String file_name = "userEvents.php";
        ArrayList<String> key = new ArrayList<>(Arrays.asList("user"));
        ArrayList<String> value = new ArrayList<>(Arrays.asList(Integer.toString(id)));
        server_request serverRequest = new server_request(this);
        serverRequest.server_request(file_name, key, value, new callback() {
            @Override
            public void done(String response) {
                try {
                    going_events = new ArrayList<upcoming_events_row_values>();
                    unsure_events = new ArrayList<upcoming_events_row_values>();
                    not_going_events = new ArrayList<upcoming_events_row_values>();
                    created_events = new ArrayList<upcoming_events_row_values>();

                    JSONObject jObject = new JSONObject(response);
                    String status = jObject.getString("status");
                    if (!status.equals("success")) {
                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(upcoming_events.this);
                        dialogBuilder.setMessage("Error");
                        dialogBuilder.setPositiveButton("Ok", null);
                        dialogBuilder.show();
                    } else {
                        //store stuff we need
                        int num_events = jObject.getInt("num_events");

                        for (int i = 0; i < num_events; i++) {
                            String tempID = Integer.toString(i);
                            String event_name = jObject.getJSONArray(tempID).getString(0);
                            int attendance_status = jObject.getJSONArray(tempID).getInt(1);
                            String start_datetime = jObject.getJSONArray(tempID).getString(2);
                            String end_datetime = jObject.getJSONArray(tempID).getString(3);
                            String address = jObject.getJSONArray(tempID).getString(4);
                            int creator = jObject.getJSONArray(tempID).getInt(5);

                            upcoming_events_row_values temp = new upcoming_events_row_values(event_name,attendance_status,start_datetime,end_datetime,address,creator);
                            switch(attendance_status) //0: no response, 1: yes, 2: no, 3: creator
                            {
                                case 0:
                                    unsure_events.add(temp);
                                    break;
                                case 1:
                                    going_events.add(temp);
                                    break;
                                case 2:
                                    not_going_events.add(temp);
                                    break;
                                case 3:
                                    going_events.add(temp);
                                    created_events.add(temp);
                                    break;
                            }
                        }
                        //sort unsure_events, going_events, and not_going_events
                        Collections.sort(unsure_events, new events_comparator());
                        Collections.sort(going_events, new events_comparator());
                        Collections.sort(not_going_events, new events_comparator());

                        list = (ListView) findViewById(R.id.events_list_upcoming_events);
                        upcoming_events_list_controller dataAdapter = new upcoming_events_list_controller(upcoming_events.this, R.id.events_list_upcoming_events, going_events);
                        list.setAdapter(dataAdapter);
                        current_tab = 1;
                        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                String event_name = "";
                                String start_date = "";
                                String end_date = "";
                                int creator_ID = -1;
                                switch(current_tab)
                                { //1: going, 2: unsure, 3:not_going
                                    case 1:
                                        event_name = going_events.get(position).event_name;
                                        start_date = going_events.get(position).start_datetime;
                                        end_date = going_events.get(position).end_datetime;
                                        creator_ID = going_events.get(position).creator;
                                        break;
                                    case 2:
                                        event_name = unsure_events.get(position).event_name;
                                        start_date = unsure_events.get(position).start_datetime;
                                        end_date = unsure_events.get(position).end_datetime;
                                        creator_ID = unsure_events.get(position).creator;
                                        break;
                                    case 3:
                                        event_name = not_going_events.get(position).event_name;
                                        start_date = not_going_events.get(position).start_datetime;
                                        end_date = not_going_events.get(position).end_datetime;
                                        creator_ID = not_going_events.get(position).creator;
                                        break;
                                }
                                Intent i = new Intent(upcoming_events.this,event_details.class);
                                i.putExtra("creator_ID",creator_ID);
                                Date start_date_time = new Date();
                                Date end_date_time = new Date();
                                DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                try
                                {
                                    start_date_time = format.parse(start_date);
                                    end_date_time = format.parse(end_date);
                                }
                                catch (Exception e) {
                                    e.printStackTrace();
                                }

                                DateFormat month_and_day = new SimpleDateFormat("E, M/d h:mm a");
                                String date = month_and_day.format(start_date_time);
                                i.putExtra("date",date);

                                long duration_in_milli = end_date_time.getTime() - start_date_time.getTime();
                                long duration_hours = duration_in_milli/(1000*60*60);
                                long duration_minutes = duration_in_milli/(1000*60) % 60;

                                String duration_string = "";
                                if(duration_hours == 0)
                                {
                                    if(duration_minutes == 0)
                                        duration_string = "indefinite";
                                    else if(duration_minutes == 1)
                                        duration_string = "1 minute";
                                    else
                                        duration_string = duration_minutes + " minutes";
                                }
                                else if(duration_hours == 1)
                                {
                                    if(duration_minutes == 0)
                                        duration_string = duration_hours + " hour";
                                    else if(duration_minutes == 1)
                                        duration_string = duration_hours + " hour 1 minute";
                                    else
                                        duration_string = duration_hours + " hour " + duration_minutes + " minutes";
                                }
                                else
                                {
                                    if(duration_minutes == 0)
                                        duration_string = duration_hours + " hours";
                                    else if(duration_minutes == 1)
                                        duration_string = duration_hours + " hours 1 minute";
                                    else
                                        duration_string = duration_hours + " hours " + duration_minutes + " minutes";
                                }
                                i.putExtra("duration", duration_string);
                                i.putExtra("event_name",event_name);

                                startActivity(i);
                            }

                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }


        });

        Bgoing_events = (Button) findViewById(R.id.going_button_upcoming_events);
        Bunsure_events = (Button) findViewById(R.id.unsure_button_upcoming_events);
        Bnot_going_events = (Button) findViewById(R.id.not_going_button_upcoming_events);

        Bgoing_events.setOnClickListener(upcoming_events.this);
        Bunsure_events.setOnClickListener(upcoming_events.this);
        Bnot_going_events.setOnClickListener(upcoming_events.this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_upcoming_events, menu);
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
        upcoming_events_list_controller dataAdapter;
        switch(v.getId()) {
            case R.id.going_button_upcoming_events:
                list = (ListView) findViewById(R.id.events_list_upcoming_events);
                dataAdapter = new upcoming_events_list_controller(upcoming_events.this, R.id.events_list_upcoming_events,going_events);
                list.setAdapter(dataAdapter);
                current_tab = 1;
                break;
            case R.id.unsure_button_upcoming_events:
                list = (ListView) findViewById(R.id.events_list_upcoming_events);
                dataAdapter = new upcoming_events_list_controller(upcoming_events.this, R.id.events_list_upcoming_events,unsure_events);
                list.setAdapter(dataAdapter);
                current_tab = 2;
                break;
            case R.id.not_going_button_upcoming_events:
                list = (ListView) findViewById(R.id.events_list_upcoming_events);
                dataAdapter = new upcoming_events_list_controller(upcoming_events.this, R.id.events_list_upcoming_events,not_going_events);
                list.setAdapter(dataAdapter);
                current_tab = 3;
                break;

        }
    }
}
