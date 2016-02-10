package com.noms.noms.groups_folder;

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

public class AFTG extends AppCompatActivity implements View.OnClickListener{

    ArrayList<AFTG_friend_row_values> friends;
    ArrayList<Integer> friend_ids;
    Intent intent;
    String group_name; //TODO
    ArrayList<Integer> group_member_ids; //TODO
    userLocalStore userLocalStore;
    int user_id;
    ListView list;

    Button Badd_to_group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friends_to_group);

        final globals G = new globals();
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(G.get_main_color())));
        String title_HTML = "<font color=" + G.get_title_color() + ">Add Friends to Group</font>";
        getSupportActionBar().setTitle(Html.fromHtml(title_HTML));

        friends = new ArrayList<>();
        friend_ids = new ArrayList<>();

        intent = getIntent();
        if(intent != null) {
            group_name = intent.getStringExtra("group_name");
            group_member_ids = intent.getIntegerArrayListExtra("group_member_ids");
        }
        else{
            Log.d("test", "critical error:1"); //TODO(postCB): what should I do if intent is null?
        }

        //Server Request
        userLocalStore = new userLocalStore(this);
        user_id = userLocalStore.getUserID();

        String file_name = "userFriends.php";
        ArrayList<String> key = new ArrayList<>(Arrays.asList("user"));
        ArrayList<String> value = new ArrayList<>(Arrays.asList(Integer.toString(user_id)));
        server_request serverRequest = new server_request(this);
        serverRequest.server_request(file_name, key, value, new callback() {
            @Override
            public void done(String response) {
                try {
                    JSONObject jObject = new JSONObject(response);
                    String status = jObject.getString("status");
                    if (!status.equals("success")) {
                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AFTG.this);
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
                            if(group_member_ids.contains(id))
                            {
                                continue;
                            }
                            else {
                                AFTG_friend_row_values temp = new AFTG_friend_row_values(id, first_name, last_name);
                                friends.add(temp);
                            }
                        }
                        list = (ListView) findViewById(R.id.friend_list_aftg);
                        AFTG_list_controller dataAdapter = new AFTG_list_controller(AFTG.this, R.id.friend_name_aftg, friends);
                        list.setAdapter(dataAdapter);

                        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                int temp_id = friends.get(position).id;
                                if(friend_ids.contains(temp_id)) {
                                    friend_ids.remove(new Integer(temp_id));
                                    list.getChildAt(position).setBackgroundColor(Color.parseColor(G.get_grey_color()));
                                }
                                else {
                                    friend_ids.add(temp_id);
                                    list.getChildAt(position).setBackgroundColor(Color.parseColor(G.get_main_color()));
                                }
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }


        });

        Badd_to_group = (Button) findViewById(R.id.add_to_group_button_aftg);
        Badd_to_group.setOnClickListener(AFTG.this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_friends_to_group, menu);
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

    public void onClick(View v){
        switch(v.getId()){
            case R.id.add_to_group_button_aftg:
                String file_name = "addToGroup.php";
                String group_members_encoded = "";
                for(int i = 0; i < friend_ids.size(); i++)
                {
                    group_members_encoded = group_members_encoded + Integer.toString(friend_ids.get(i)) + ",";
                }
                Log.d("test","user" + Integer.toString(user_id));
                Log.d("test","group_name" + group_name);
                Log.d("test","group_members" + group_members_encoded);
                ArrayList<String> key = new ArrayList<>(Arrays.asList("user","group_name","group_members"));
                ArrayList<String> value = new ArrayList<>(Arrays.asList(Integer.toString(user_id),group_name,group_members_encoded));
                server_request serverRequest = new server_request(this);
                serverRequest.server_request(file_name, key, value, new callback() {
                    @Override
                    public void done(String response) {
                        try {
                            JSONObject jObject = new JSONObject(response);
                            String status = jObject.getString("status");
                            if (!status.equals("success"))
                            {
                                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AFTG.this);
                                dialogBuilder.setMessage("Error");
                                dialogBuilder.setPositiveButton("Ok", null);
                                dialogBuilder.show();
                            }
                            else
                            {
                                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AFTG.this);
                                dialogBuilder.setMessage("Members Added");
                                dialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent i = new Intent(AFTG.this,group_info.class);
                                        i.putExtra("group_name", group_name);
                                        startActivity(i);
                                    }
                                });
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
