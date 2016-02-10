package com.noms.noms.main_folder;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;

import com.noms.noms.R;
import com.noms.noms.globals;
import com.noms.noms.user;

public class register extends AppCompatActivity implements View.OnClickListener {

    EditText ETfirst_name,ETlast_name,ETemail,ETpassword,ETrepeat_password;
    NumberPicker NPbday_picker = null;
    NumberPicker NPbyear_picker = null;
    NumberPicker NPbmonth_picker = null;
    NumberPicker NPgender_picker = null;

    Button register_button = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        globals G = new globals();
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(G.get_main_color())));
        String title_HTML = "<font color=" + G.get_title_color() + ">Register</font>";
        getSupportActionBar().setTitle(Html.fromHtml(title_HTML));

        ETfirst_name = (EditText) findViewById(R.id.first_name_reg);
        ETlast_name = (EditText) findViewById(R.id.last_name_reg);
        ETemail = (EditText) findViewById(R.id.email_reg);
        ETpassword = (EditText) findViewById(R.id.password_reg);
        ETrepeat_password = (EditText) findViewById(R.id.rep_password_reg);
        //number picker
        NPbday_picker = (NumberPicker) findViewById(R.id.BD_picker_reg);
        NPbday_picker.setMaxValue(31); //TODO(post CB): change to be set based on month
        NPbday_picker.setMinValue(1);

        NPbyear_picker = (NumberPicker) findViewById(R.id.BY_picker_reg);
        NPbyear_picker.setMaxValue(2015); //TODO(post CB): change based on year
        NPbyear_picker.setMinValue(1900);

        NPbmonth_picker = (NumberPicker) findViewById(R.id.BM_picker_reg);
        NPbmonth_picker.setMaxValue(12);
        NPbmonth_picker.setMinValue(1);
        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        NPbmonth_picker.setDisplayedValues(months);

        NPgender_picker = (NumberPicker) findViewById(R.id.gender_picker_reg);
        NPgender_picker.setMaxValue(3);
        NPgender_picker.setMinValue(0);
        String[] genders = {"Choose gender", "Male", "Female", "Other"};
        NPgender_picker.setDisplayedValues(genders);
        NPgender_picker.setWrapSelectorWheel(false);

        register_button = (Button) findViewById(R.id.register_button_reg);
        register_button.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
        return true;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_button_reg:
                String first_name = ETfirst_name.getText().toString();
                String last_name = ETlast_name.getText().toString();
                String email = ETemail.getText().toString();
                String password = ETpassword.getText().toString();
                String repeat_password = ETrepeat_password.getText().toString();
                int b_month = NPbmonth_picker.getValue();
                int b_day = NPbday_picker.getValue();
                int b_year = NPbyear_picker.getValue();
                int gender = NPgender_picker.getValue();
                if(first_name.isEmpty())
                {
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(register.this);
                    dialogBuilder.setMessage("please fill out first name");
                    dialogBuilder.setPositiveButton("Ok", null);
                    dialogBuilder.show();
                    break;
                }
                if(last_name.isEmpty())
                {
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(register.this);
                    dialogBuilder.setMessage("please fill out last name");
                    dialogBuilder.setPositiveButton("Ok", null);
                    dialogBuilder.show();
                    break;
                }
                if(email.isEmpty())
                {
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(register.this);
                    dialogBuilder.setMessage("please fill out email");
                    dialogBuilder.setPositiveButton("Ok", null);
                    dialogBuilder.show();
                    break;
                }
                if(password.isEmpty())
                {
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(register.this);
                    dialogBuilder.setMessage("please fill out password");
                    dialogBuilder.setPositiveButton("Ok", null);
                    dialogBuilder.show();
                    break;
                }
                if(repeat_password.isEmpty())
                {
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(register.this);
                    dialogBuilder.setMessage("please fill out repeat password");
                    dialogBuilder.setPositiveButton("Ok", null);
                    dialogBuilder.show();
                    break;
                }
                if(gender == 0)
                {
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(register.this);
                    dialogBuilder.setMessage("please specify gender");
                    dialogBuilder.setPositiveButton("Ok", null);
                    dialogBuilder.show();
                    break;
                }

                if(!password.equals(repeat_password)) {
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(register.this);
                    dialogBuilder.setMessage("passwords do not match");
                    dialogBuilder.setPositiveButton("Ok", null);
                    dialogBuilder.show();
                    break;
                }
                user registerData = new user(first_name,last_name,email,b_month,b_day,b_year,gender);

                serverRequest serverRequest = new serverRequest(this);
                serverRequest.register(registerData,password, new boolCallback(){
                    @Override
                    public void done(boolean status)
                    {
                        if(!status)
                        {
                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(register.this);
                            dialogBuilder.setMessage("Error. Please try again");
                            dialogBuilder.setPositiveButton("Ok",null);
                            dialogBuilder.show();
                        }
                        else
                        {
                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(register.this);
                            dialogBuilder.setMessage("Account created!");
                            dialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            });
                            dialogBuilder.show();
                        }
                    }
                });

                break;


        }
    }
}