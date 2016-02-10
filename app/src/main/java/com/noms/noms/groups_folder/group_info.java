package com.noms.noms.groups_folder;

import android.app.AlertDialog;
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

public class group_info extends AppCompatActivity implements View.OnClickListener{

    int user_id;
    Intent intent;
    String group_name;
    userLocalStore userLocalStore;
    ArrayList<group_info_row_values> group_members;
    ListView list;
    ArrayList<Integer> group_member_ids;

    Button add_to_group;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_info);

        intent = getIntent();
        if(intent != null)
        {
            group_name = intent.getStringExtra("group_name");
        }
        else
        {
            Log.d("test", "critical error:1");//TODO(postCB): what should I do if intent is null?
        }

        globals G = new globals();
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(G.get_main_color())));
        String title_HTML = "<font color=" + G.get_title_color() + ">Group: " + group_name + "</font>";
        getSupportActionBar().setTitle(Html.fromHtml(title_HTML));

        userLocalStore = new userLocalStore(this);
        user_id = userLocalStore.getUserID();

        String file_name = "groupMembers.php";
        ArrayList<String> key = new ArrayList<>(Arrays.asList("user","group_name"));
        ArrayList<String> value = new ArrayList<>(Arrays.asList(Integer.toString(user_id),group_name));
        server_request serverRequest = new server_request(this);
        serverRequest.server_request(file_name, key, value, new callback() {
            @Override
            public void done(String response) {
                try {
                    group_members = new ArrayList<>();
                    group_member_ids = new ArrayList<Integer>();

                    JSONObject jObject = new JSONObject(response);
                    String status = jObject.getString("status");
                    if (!status.equals("success")) {
                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(group_info.this);
                        dialogBuilder.setMessage("Error");
                        dialogBuilder.setPositiveButton("Ok", null);
                        dialogBuilder.show();
                    } else {
                        //store stuff we need
                        int num_members = jObject.getInt("num_members");
                        for (int i = 0; i < num_members; i++) {
                            String tempID = Integer.toString(i);
                            int id = jObject.getJSONArray(tempID).getInt(0);
                            String first_name = jObject.getJSONArray(tempID).getString(1);
                            String last_name = jObject.getJSONArray(tempID).getString(2);
                            group_info_row_values temp = new group_info_row_values(first_name, last_name, id);
                            group_members.add(temp);
                            group_member_ids.add(id);
                        }
                        Log.d("test","10");
                        list = (ListView) findViewById(R.id.group_info_list_group_info);
                        group_info_list_controller dataAdapter = new group_info_list_controller(group_info.this, R.id.member_name_group_info, group_members,group_name);
                        list.setAdapter(dataAdapter);
                        Log.d("test","14");

                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }


        });

        add_to_group = (Button) findViewById(R.id.add_to_group_button_group_info);
        add_to_group.setOnClickListener(group_info.this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_group_info, menu);
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

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_to_group_button_group_info:
                Intent i = new Intent(this, AFTG.class);
                i.putExtra("group_name", group_name);
                i.putExtra("group_member_ids",group_member_ids);
                startActivity(i);
        }
    }
}
