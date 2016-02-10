package com.noms.noms.events_folder;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;

import com.noms.noms.R;
import com.noms.noms.friends_folder.friends;
import com.noms.noms.globals;
import com.noms.noms.groups_folder.group;
import com.noms.noms.main_folder.login;
import com.noms.noms.settings_folder.setting;
import com.noms.noms.userLocalStore;
import com.noms.noms.user_info.user_info;

public class new_event extends AppCompatActivity implements View.OnClickListener {

    com.noms.noms.userLocalStore userLocalStore;

    EditText ETevent_name, ETevent_address;
    Button Btoday, Btomorrow, Bday_after, Binvite_friends;
    NumberPicker NPstart_hour,NPstart_minute,NPstart_AP,NPdur_hour,NPdur_minute;

    int day;
    globals G;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);

        G = new globals();
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(G.get_main_color())));
        String title_HTML = "<font color=" + G.get_title_color() + ">New Event</font>";
        getSupportActionBar().setTitle(Html.fromHtml(title_HTML));

        userLocalStore = new userLocalStore(this);

        ETevent_name = (EditText) findViewById(R.id.event_name_TF_NE);
        ETevent_address = (EditText) findViewById(R.id.event_address_TF_NE);

        Btoday = (Button) findViewById(R.id.today_button_NE);
        Btomorrow = (Button) findViewById(R.id.tomorrow_button_NE);
        Bday_after = (Button) findViewById(R.id.day_after_button_NE);

        Btoday.setOnClickListener(this);
        Btomorrow.setOnClickListener(this);
        Bday_after.setOnClickListener(this);

        day = 0;
        Btoday.setBackgroundResource(R.drawable.button_main_color);
        Btomorrow.setBackgroundResource(R.drawable.button_secondary_color);
        Bday_after.setBackgroundResource(R.drawable.button_secondary_color);

        NPstart_hour = (NumberPicker) findViewById(R.id.hour_picker_NE);
        NPstart_hour.setMaxValue(12);
        NPstart_hour.setMinValue(1);

        NPstart_minute = (NumberPicker) findViewById(R.id.minute_picker_NE);
        NPstart_minute.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                return String.format("%02d", value);
            }
        });
        NPstart_minute.setMaxValue(59);
        NPstart_minute.setMinValue(0);

        NPstart_AP = (NumberPicker) findViewById(R.id.am_pm_picker_NE);
        NPstart_AP.setMaxValue(2);
        NPstart_AP.setMinValue(1);
        String[] am_pm = {"AM","PM"};
        NPstart_AP.setDisplayedValues(am_pm);

        NPdur_hour = (NumberPicker) findViewById(R.id.dur_hour_picker_NE);
        NPdur_hour.setMaxValue(24);
        NPdur_hour.setMinValue(0);

        NPdur_minute = (NumberPicker) findViewById(R.id.dur_minute_picker_NE);
        NPdur_minute.setMaxValue(3);
        NPdur_minute.setMinValue(0);
        String[] durations = {"0","15","30","45"};
        NPdur_minute.setDisplayedValues(durations);

        Binvite_friends = (Button) findViewById(R.id.invite_friends_button);
        Binvite_friends.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_event, menu);
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
            case R.id.today_button_NE:
                day = 0;
                Btoday.setBackgroundResource(R.drawable.button_main_color);
                Btomorrow.setBackgroundResource(R.drawable.button_secondary_color);
                Bday_after.setBackgroundResource(R.drawable.button_secondary_color);
                break;

            case R.id.tomorrow_button_NE:
                day = 1;
                Btoday.setBackgroundResource(R.drawable.button_secondary_color);
                Btomorrow.setBackgroundResource(R.drawable.button_main_color);
                Bday_after.setBackgroundResource(R.drawable.button_secondary_color);
                break;

            case R.id.day_after_button_NE:
                day = 2;
                Btoday.setBackgroundResource(R.drawable.button_secondary_color);
                Btomorrow.setBackgroundResource(R.drawable.button_secondary_color);
                Bday_after.setBackgroundResource(R.drawable.button_main_color);
                break;


            case R.id.invite_friends_button:
                String event_name = ETevent_name.getText().toString();
                String event_address = ETevent_address.getText().toString();
                int start_hour = NPstart_hour.getValue();
                int start_minute = NPstart_minute.getValue();
                int start_ampm = NPstart_AP.getValue();
                int dur_hour = NPdur_hour.getValue();
                int dur_minute_hold = NPdur_minute.getValue();
                int dur_minute = 0;
                switch(dur_minute_hold) {
                    case 0:
                        dur_minute = 0;
                        break;
                    case 1:
                        dur_minute = 15;
                        break;
                    case 2:
                        dur_minute = 30;
                        break;
                    case 3:
                        dur_minute = 45;
                        break;
                }

                if(event_name.isEmpty()) {
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(new_event.this);
                    dialogBuilder.setMessage("Please enter an event name");
                    dialogBuilder.setPositiveButton("Ok", null);
                    dialogBuilder.show();
                    break;
                }
                if(event_address.isEmpty())
                {
                    event_address = "NONE";
                }
                Intent i = new Intent(this, invite_friends.class);
                i.putExtra("event_name", event_name);
                i.putExtra("event_address", event_address);
                i.putExtra("day", day);
                i.putExtra("start_hour", start_hour);
                i.putExtra("start_minute", start_minute);
                i.putExtra("start_ampm", start_ampm);
                i.putExtra("dur_hour", dur_hour);
                i.putExtra("dur_minute", dur_minute);
                startActivity(i);

        }
    }
}