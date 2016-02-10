package com.noms.noms.friends_folder;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.noms.noms.R;
import com.noms.noms.events_folder.events;
import com.noms.noms.globals;
import com.noms.noms.groups_folder.group;
import com.noms.noms.main_folder.login;
import com.noms.noms.userLocalStore;
import com.noms.noms.user_info.user_info;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class add_friends extends AppCompatActivity implements View.OnClickListener {

    Button Badd_friend;
    EditText ETfriend_email;
    userLocalStore userLocalStore;
    int user_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friends);

        globals G = new globals();
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(G.get_main_color())));
        String title_HTML = "<font color=" + G.get_title_color() + ">Add Friend</font>";
        getSupportActionBar().setTitle(Html.fromHtml(title_HTML));

        Badd_friend = (Button) findViewById(R.id.Badd_friend_AF);
        ETfriend_email = (EditText) findViewById(R.id.ETfriend_email_AF);

        Badd_friend.setOnClickListener(this);

        userLocalStore = new userLocalStore(this);
        user_id = userLocalStore.getUserID();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_friends, menu);
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
        switch(v.getId()) {
            case R.id.Badd_friend_AF:
                String file_name = "addFriend.php";
                String email = ETfriend_email.getText().toString();
                ArrayList<String> key = new ArrayList<>(Arrays.asList("user", "friend_email"));
                ArrayList<String> value = new ArrayList<>(Arrays.asList(Integer.toString(user_id), email));
                server_request serverRequest = new server_request(this);
                serverRequest.server_request(file_name, key, value, new callback() {
                    @Override
                    public void done(String response) {

                        try {
                            JSONObject jObject = new JSONObject(response);
                            String status = jObject.getString("status");
                            if (status.equals("success")) {
                                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(add_friends.this);
                                dialogBuilder.setMessage("friend added");
                                dialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        startActivity(new Intent(add_friends.this, friends.class));
                                    }
                                });
                                dialogBuilder.show();
                            } else {
                                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(add_friends.this);
                                dialogBuilder.setMessage("error. try again");
                                dialogBuilder.setPositiveButton("Ok", null);
                                dialogBuilder.show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();

                        }
                    }


                });
        }
    }
}
