package com.noms.noms.user_info;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.noms.noms.R;
import com.noms.noms.events_folder.events;
import com.noms.noms.globals;
import com.noms.noms.groups_folder.group;
import com.noms.noms.main_folder.login;
import com.noms.noms.settings_folder.setting;
import com.noms.noms.userLocalStore;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class user_info extends AppCompatActivity {

    userLocalStore userLocalStore;

    TextView Tfull_name;
    TextView Temail;
    TextView Tbirthday;
    TextView Tgender;
    TextView Tnum_friends;
    TextView Tnum_groups;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        globals G = new globals();
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(G.get_main_color())));
        String title_HTML = "<font color=" + G.get_title_color() + ">User Profile</font>";
        getSupportActionBar().setTitle(Html.fromHtml(title_HTML));

        userLocalStore = new userLocalStore(this);

        Tfull_name = (TextView) findViewById(R.id.user_info_name_holder);
        Temail = (TextView) findViewById(R.id.user_info_email_holder);
        Tbirthday = (TextView) findViewById(R.id.user_info_birthday_holder);
        Tgender = (TextView) findViewById(R.id.user_info_gender_holder);
        Tnum_friends = (TextView) findViewById(R.id.user_info_friends_holder);
        Tnum_groups = (TextView) findViewById(R.id.user_info_groups_holder);

        Tfull_name.setText(userLocalStore.getUserName());
        Temail.setText("Email: " + userLocalStore.getUserEmail());
        Tbirthday.setText("DOB: " + userLocalStore.getUserBirthday());
        switch(userLocalStore.getUserGender())
        {
            case 1:
                Tgender.setText("Male");
                break;
            case 2:
                Tgender.setText("Female");
                break;
            case 3:
                Tgender.setText("Other");
                break;
        }

        int id = userLocalStore.getUserID();

        String file_name = "userFriends.php";
        ArrayList<String> key = new ArrayList<>(Arrays.asList("user"));
        ArrayList<String> value = new ArrayList<>(Arrays.asList(Integer.toString(id)));
        server_request serverRequest = new server_request(this);
        serverRequest.server_request(file_name, key, value, new callback() {
            @Override
            public void done(String response) {
                try {
                    JSONObject jObject = new JSONObject(response);
                    String status = jObject.getString("status");
                    if (!status.equals("success")) {
                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(user_info.this);
                        dialogBuilder.setMessage("Error");
                        dialogBuilder.setPositiveButton("Ok", null);
                        dialogBuilder.show();
                    } else {
                        //store stuff we need
                        String num_friends = jObject.getString("num_friends");
                        Tnum_friends.setText("Number of Friends: " + num_friends);
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }


        });
        file_name = "userGroups.php";
        key = new ArrayList<>(Arrays.asList("user"));
        value = new ArrayList<>(Arrays.asList(Integer.toString(id)));
        serverRequest = new server_request(this);
        serverRequest.server_request(file_name, key, value, new callback() {
            @Override
            public void done(String response) {
                try {
                    JSONObject jObject = new JSONObject(response);
                    String status = jObject.getString("status");
                    if (!status.equals("success")) {
                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(user_info.this);
                        dialogBuilder.setMessage("Error");
                        dialogBuilder.setPositiveButton("Ok", null);
                        dialogBuilder.show();
                    } else {
                        //store stuff we need
                        String num_groups = jObject.getString("num_groups");
                        Tnum_groups.setText("Number of Groups: " + num_groups);
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }


        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_info, menu);
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
                startActivity(new Intent(this, com.noms.noms.friends_folder.friends.class));
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
}
