package com.noms.noms.forgot_password_folder;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.noms.noms.R;
import com.noms.noms.globals;
import com.noms.noms.main_folder.login;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class change_password extends AppCompatActivity implements View.OnClickListener {

    EditText ETpassword, ETrep_password;
    Button Bchange;

    Intent intent;
    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        globals G = new globals();
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(G.get_main_color())));
        String title_HTML = "<font color=" + G.get_title_color() + ">Change Password</font>";
        getSupportActionBar().setTitle(Html.fromHtml(title_HTML));

        intent = getIntent();
        email = intent.getStringExtra("email");

        ETpassword = (EditText) findViewById(R.id.pass_cp);
        ETrep_password = (EditText) findViewById(R.id.rep_pass_cp);
        Bchange = (Button) findViewById(R.id.change_button_cp);

        Bchange.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_change_password, menu);
        return true;
    }



    public void onClick(View v){
        switch(v.getId()){
            case R.id.change_button_cp:
                String password = ETpassword.getText().toString();
                String repPassword = ETrep_password.getText().toString();

                if(!password.equals(repPassword))
                {
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(change_password.this);
                    dialogBuilder.setMessage("Passwords do not match");
                    dialogBuilder.setPositiveButton("Ok",null);
                    dialogBuilder.show();
                    break;
                }
                String file_name = "changePassword.php";
                ArrayList<String> key = new ArrayList<>(Arrays.asList("email","password"));
                ArrayList<String> value = new ArrayList<>(Arrays.asList(email,password));
                server_request serverRequest = new server_request(this);
                serverRequest.server_request(file_name, key, value, new callback() {
                    @Override
                    public void done(String response) {

                        try {
                            JSONObject jObject = new JSONObject(response);
                            String status = jObject.getString("status");
                            if (status.equals("success")) {
                                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(change_password.this);
                                dialogBuilder.setMessage("password changed");
                                dialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        Intent i = new Intent(getApplicationContext(), login.class);
                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(i);
                                    }
                                });
                                dialogBuilder.show();
                            } else {
                                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(change_password.this);
                                dialogBuilder.setMessage("error. try again");
                                dialogBuilder.setPositiveButton("Ok", null);
                                dialogBuilder.show();
                            }
                        }
                        catch(Exception e){
                            e.printStackTrace();

                        }
                    }


                });
        }
    }
}
