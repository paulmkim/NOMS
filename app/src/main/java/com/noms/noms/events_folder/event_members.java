package com.noms.noms.events_folder;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.noms.noms.globals;
import com.noms.noms.userLocalStore;

import com.noms.noms.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class event_members extends AppCompatActivity {

    userLocalStore userLocalStore;
    ListView list;
    Intent intent;
    TextView Ttype;
    String type;
    int num_people;
    ArrayList<String> members;
    ArrayList<Integer> members_ids;
    ArrayList<event_members_row_values> members_for_list;
    globals G;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_members);


        members = new ArrayList<>();
        members_ids = new ArrayList<>();
        members_for_list = new ArrayList<>();
        Ttype = (TextView) findViewById(R.id.temp_type_event_members);

        userLocalStore = new userLocalStore(this);
        intent = getIntent();
        type = intent.getStringExtra("type");
        num_people = intent.getIntExtra("num_people", 0);
        members = intent.getStringArrayListExtra("people");
        members_ids = intent.getIntegerArrayListExtra("people_ids");

        G = new globals();
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(G.get_main_color())));
        String title_HTML = "<font color=" + G.get_title_color() + ">" + type + " Members</font>";
        getSupportActionBar().setTitle(Html.fromHtml(title_HTML));

        Ttype.setText(type);

        for(int i = 0; i < num_people; i++)
        {
            event_members_row_values temp = new event_members_row_values(members_ids.get(i),members.get(i));
            members_for_list.add(temp);
        }
        list = (ListView) findViewById(R.id.friend_list_event_members);
        event_members_list_controller dataAdapter = new event_members_list_controller(event_members.this,R.id.member_name_group_info, members_for_list);
        list.setAdapter(dataAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_event_members, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        return super.onOptionsItemSelected(item);
    }
}
