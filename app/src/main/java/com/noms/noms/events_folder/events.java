package com.noms.noms.events_folder;

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
import android.widget.Toast;

import com.noms.noms.R;
import com.noms.noms.friends_folder.friends;
import com.noms.noms.globals;
import com.noms.noms.groups_folder.group;
import com.noms.noms.main_folder.login;
import com.noms.noms.settings_folder.setting;
import com.noms.noms.userLocalStore;
import com.noms.noms.user_info.user_info;

public class events extends AppCompatActivity implements View.OnClickListener{

    com.noms.noms.userLocalStore userLocalStore;
    Button Bnew_event;
    Button Bupcoming_event;
    int backPress = 0;

    globals G;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        G = new globals();
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(G.get_secondary_color())));
        String title_HTML = "<font color=" + G.get_title_color() + ">Home</font>";
        getSupportActionBar().setTitle(Html.fromHtml(title_HTML));

        userLocalStore = new userLocalStore(this);


        Bnew_event = (Button) findViewById(R.id.new_event_button_events);
        Bupcoming_event = (Button) findViewById(R.id.Upcoming_Events_button_events);

        Bnew_event.setOnClickListener(this);
        Bupcoming_event.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        backPress = (backPress + 1);

        if (backPress > 1) {
            userLocalStore.clearUserData();
            userLocalStore.setUserLoggedIn(false);
            super.onBackPressed();
        } else
            Toast.makeText(getApplicationContext(), " Press Back again to Logout ", Toast.LENGTH_SHORT).show(); //TODO: make it an alert instead
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!userLocalStore.getUserLoggedIn()) {
            startActivity(new Intent(events.this, login.class)); //TODO(postCB):this option does not allow user to press back
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_events, menu);
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
            case R.id.new_event_button_events:
                startActivity(new Intent(this, new_event.class));
                break;
            case R.id.Upcoming_Events_button_events:
                startActivity(new Intent(this, upcoming_events.class));
                break;
        }
    }
}
