package com.noms.noms.groups_folder;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.noms.noms.R;
import com.noms.noms.events_folder.events;
import com.noms.noms.friends_folder.friends;
import com.noms.noms.globals;
import com.noms.noms.main_folder.login;
import com.noms.noms.settings_folder.setting;
import com.noms.noms.userLocalStore;
import com.noms.noms.user_info.user_info;

public class create_group extends AppCompatActivity implements View.OnClickListener{

    com.noms.noms.userLocalStore userLocalStore;

    Button Badd_friends;
    EditText ETgroup_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        globals G = new globals();
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(G.get_main_color())));
        String title_HTML = "<font color=" + G.get_title_color() + ">Create Group</font>";
        getSupportActionBar().setTitle(Html.fromHtml(title_HTML));

        userLocalStore = new userLocalStore(this);

        ETgroup_name = (EditText) findViewById(R.id.new_group_name_group);
        Badd_friends = (Button) findViewById(R.id.add_to_new_group_group);
        Badd_friends.setOnClickListener(create_group.this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_group, menu);
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

            case R.id.friends_main:
                startActivity(new Intent(this, friends.class));
                break;

            case R.id.user_profile_main:
                startActivity(new Intent(this, user_info.class));
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
            case R.id.add_to_new_group_group:
                String group_name = ETgroup_name.getText().toString();
                if(group_name.isEmpty())
                {
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(create_group.this);
                    dialogBuilder.setMessage("Please Enter a Group Name");
                    dialogBuilder.setPositiveButton("Ok", null);
                    dialogBuilder.show();
                    break;
                }
                Intent i = new Intent(this, AFTNG.class);
                i.putExtra("group_name", group_name);
                startActivity(i);
        }
    }
}
