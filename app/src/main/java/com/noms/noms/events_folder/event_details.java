package com.noms.noms.events_folder;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.noms.noms.globals;
import com.noms.noms.userLocalStore;

import com.noms.noms.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class event_details extends AppCompatActivity implements View.OnClickListener{

    userLocalStore userLocalStore;
    Intent intent;

    TextView Tevent_name;
    TextView Tcreator_name;
    TextView Tdatetime;
    TextView Tduration;
    TextView Taddress;
    Button Bothers_going;
    Button Bothers_no_response;
    Button Bothers_not_going;
    Button Bgoing;
    Button Bnot_going;

    int creator_ID;
    String creator_name;
    String start_datetime;
    String event_name;
    String duration;
    String address;
    ArrayList<String> going_members;
    ArrayList<Integer> going_members_ids;
    ArrayList<String> not_going_members;
    ArrayList<Integer> not_going_members_ids;
    ArrayList<String> maybe_going_members;
    ArrayList<Integer> maybe_going_members_ids;
    Boolean creator;

    globals G;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        userLocalStore = new userLocalStore(this);

        intent = getIntent();
        creator_ID = intent.getIntExtra("creator_ID", 0);
        start_datetime = intent.getStringExtra("date");
        event_name = intent.getStringExtra("event_name");
        duration = intent.getStringExtra("duration");

        G = new globals();
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(G.get_main_color())));
        String title_HTML = "<font color=" + G.get_title_color() + ">Event: " + event_name + "</font>";
        getSupportActionBar().setTitle(Html.fromHtml(title_HTML));

        going_members = new ArrayList<>();
        going_members_ids = new ArrayList<>();
        not_going_members = new ArrayList<>();
        not_going_members_ids = new ArrayList<>();
        maybe_going_members = new ArrayList<>();
        maybe_going_members_ids = new ArrayList<>();

        creator = false;

        Tevent_name = (TextView) findViewById(R.id.event_details_event_name);
        Tdatetime = (TextView) findViewById(R.id.temp_date_event_details);
        Tduration = (TextView) findViewById(R.id.temp_duration_event_details);
        Tcreator_name = (TextView) findViewById(R.id.temp_creator_event_details);
        Taddress = (TextView) findViewById(R.id.temp_address_event_details);
        Bothers_going = (Button) findViewById(R.id.others_going_button_event_details);
        Bothers_no_response = (Button) findViewById(R.id.no_response_button_event_details);
        Bothers_not_going = (Button) findViewById(R.id.others_not_going_button_event_details);
        Bgoing = (Button) findViewById(R.id.going_button_event_details);
        Bnot_going = (Button) findViewById(R.id.not_going_button_event_details);


        int id = userLocalStore.getUserID();
        String file_name = "eventResponses.php";
        ArrayList<String> key = new ArrayList<>(Arrays.asList("user","event_name"));
        ArrayList<String> value = new ArrayList<>(Arrays.asList(Integer.toString(creator_ID), event_name));
        server_request serverRequest = new server_request(this);
        serverRequest.server_request(file_name, key, value, new callback() {
            @Override
            public void done(String response) {
                try {
                    JSONObject jObject = new JSONObject(response);
                    String status = jObject.getString("status");
                    if (!status.equals("success")) {
                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(event_details.this);
                        dialogBuilder.setMessage("Error");
                        dialogBuilder.setPositiveButton("Ok", null);
                        dialogBuilder.show();
                    } else {
                        String creator_first_name = jObject.getString("creator_first_name");
                        String creator_last_name = jObject.getString("creator_last_name");
                        creator_name = creator_first_name + " " + creator_last_name;
                        address = jObject.getString("address");

                        int num_people = jObject.getInt("num_people");
                        for (int i = 0; i < num_people; i++) {
                            String tempID = Integer.toString(i); //1 first name, 2 last name, 3 status
                            int person_id = jObject.getJSONArray(tempID).getInt(0);
                            String first_name = jObject.getJSONArray(tempID).getString(1);
                            String last_name = jObject.getJSONArray(tempID).getString(2);
                            int person_status = jObject.getJSONArray(tempID).getInt(3);
                            String full_name = first_name + " " + last_name;
                            switch (person_status) {
                                case 0:
                                    maybe_going_members_ids.add(person_id);
                                    maybe_going_members.add(full_name);
                                    break;
                                case 1:
                                    going_members_ids.add(person_id);
                                    going_members.add(full_name);
                                    break;
                                case 2:
                                    not_going_members_ids.add(person_id);
                                    not_going_members.add(full_name);
                                    break;
                                case 3:
                                    going_members_ids.add(person_id);
                                    going_members.add(full_name);
                                    break;
                            }
                        }
                        Tevent_name.setText(event_name);
                        Tcreator_name.setText(creator_name);
                        Tdatetime.setText(start_datetime);
                        Tduration.setText(duration);
                        Taddress.setText(address);

                        Bothers_going.setText("GOING(" + going_members.size() + ")");
                        Bothers_no_response.setText("INVITED(" + maybe_going_members.size() + ")");
                        Bothers_not_going.setText("NOT GOING(" + not_going_members.size() + ")");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        });

        Bothers_going.setOnClickListener(event_details.this);
        Bothers_no_response.setOnClickListener(event_details.this);
        Bothers_not_going.setOnClickListener(event_details.this);

        Bgoing.setOnClickListener(event_details.this);
        Bnot_going.setOnClickListener(event_details.this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_event_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return super.onOptionsItemSelected(item);
    }

    public void onClick(View v){
        switch(v.getId()){
            case R.id.going_button_event_details: {
                int id = userLocalStore.getUserID();
                String file_name = "respondToEvent.php";
                ArrayList<String> key = new ArrayList<>(Arrays.asList("user", "creator_ID", "event_name", "status"));
                ArrayList<String> value = new ArrayList<>(Arrays.asList(Integer.toString(id), Integer.toString(creator_ID), event_name, "1"));
                server_request serverRequest = new server_request(this);
                serverRequest.server_request(file_name, key, value, new callback() {
                    @Override
                    public void done(String response) {
                        try {
                            JSONObject jObject = new JSONObject(response);
                            String status = jObject.getString("status");
                            if (!status.equals("success")) {
                                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(event_details.this);
                                dialogBuilder.setMessage("Error");
                                dialogBuilder.setPositiveButton("Ok", null);
                                dialogBuilder.show();
                            } else {
                                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(event_details.this);
                                dialogBuilder.setMessage("Marked as Going");
                                dialogBuilder.setPositiveButton("Ok", null);
                                dialogBuilder.show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }


                });
                break;
            }
            case R.id.not_going_button_event_details: {
                int id = userLocalStore.getUserID();
                String file_name = "respondToEvent.php";
                ArrayList<String> key = new ArrayList<>(Arrays.asList("user", "creator_ID", "event_name", "status"));
                ArrayList<String> value = new ArrayList<>(Arrays.asList(Integer.toString(id), Integer.toString(creator_ID), event_name, "2"));
                server_request serverRequest = new server_request(this);
                serverRequest.server_request(file_name, key, value, new callback() {
                    @Override
                    public void done(String response) {
                        try {
                            JSONObject jObject = new JSONObject(response);
                            String status = jObject.getString("status");
                            if (!status.equals("success")) {
                                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(event_details.this);
                                dialogBuilder.setMessage("Error");
                                dialogBuilder.setPositiveButton("Ok", null);
                                dialogBuilder.show();
                            } else {
                                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(event_details.this);
                                dialogBuilder.setMessage("Marked as Not Going");
                                dialogBuilder.setPositiveButton("Ok", null);
                                dialogBuilder.show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }


                });
                break;
            }
            case R.id.others_going_button_event_details: {
                Intent i = new Intent(this, event_members.class);
                i.putExtra("type","Going");
                i.putExtra("num_people", going_members.size());
                i.putExtra("people", going_members);
                i.putExtra("people_ids", going_members_ids);
                startActivity(i);
                break;
            }
            case R.id.no_response_button_event_details: {
                Intent i = new Intent(this, event_members.class);
                i.putExtra("type","No Response");
                i.putExtra("num_people", maybe_going_members.size());
                i.putExtra("people", maybe_going_members);
                i.putExtra("people_ids", maybe_going_members_ids);
                startActivity(i);
                break;
            }
            case R.id.others_not_going_button_event_details: {
                Intent i = new Intent(this, event_members.class);
                i.putExtra("type","Not Going");
                i.putExtra("num_people", not_going_members.size());
                i.putExtra("people", not_going_members);
                i.putExtra("people_ids", not_going_members_ids);
                startActivity(i);
                break;
            }

        }
    }
}
