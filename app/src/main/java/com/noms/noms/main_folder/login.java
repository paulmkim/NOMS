package com.noms.noms.main_folder;

import android.app.AlertDialog;
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
import android.widget.TextView;

import com.noms.noms.R;
import com.noms.noms.events_folder.events;
import com.noms.noms.forgot_password_folder.reset_password;
import com.noms.noms.globals;
import com.noms.noms.user;
import com.noms.noms.userLocalStore;


public class login extends AppCompatActivity implements View.OnClickListener{

    Button BloginButton;
    EditText ETemail,ETpassword;
    TextView TVregisterText,TVforgotPassword;

    com.noms.noms.userLocalStore userLocalStore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();

        ETemail = (EditText) findViewById(R.id.email_login);
        ETpassword = (EditText) findViewById(R.id.password_login);
        BloginButton = (Button) findViewById(R.id.send_button_res_pass);
        TVregisterText = (TextView) findViewById(R.id.CNA_login);
        TVforgotPassword = (TextView) findViewById(R.id.forgot_pass_login);

        BloginButton.setOnClickListener(this);
        TVregisterText.setOnClickListener(this);
        TVforgotPassword.setOnClickListener(this);

        userLocalStore = new userLocalStore(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    public void onClick(View v) {
        switch(v.getId()){
            case R.id.send_button_res_pass:

                String email = ETemail.getText().toString();
                String password = ETpassword.getText().toString();
                //PHP call
                serverRequest serverRequest = new serverRequest(this);
                serverRequest.login(email,password, new loginCallback(){
                    @Override
                    public void done(user returnedUser)
                    {
                        if(returnedUser == null)
                        {
                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(login.this);
                            dialogBuilder.setMessage("Incorrect user detail");
                            dialogBuilder.setPositiveButton("Ok",null);
                            dialogBuilder.show();
                        }
                        else
                        {
                            userLocalStore.storeUserData(returnedUser);
                            userLocalStore.setUserLoggedIn(true);
                            startActivity(new Intent(login.this, events.class));
                        }
                    }
                });

                break;

            case R.id.CNA_login:
                startActivity(new Intent(this, register.class));
                break;

            case R.id.forgot_pass_login:
                startActivity(new Intent(this, reset_password.class));
                break;
        }
    }
}
