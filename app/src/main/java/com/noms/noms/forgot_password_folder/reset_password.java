package com.noms.noms.forgot_password_folder;

import android.app.AlertDialog;
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

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class reset_password extends AppCompatActivity implements View.OnClickListener {

    Button Bsend, Bchange_password;
    EditText ETemail, ETreset_code;

    int stored_reset_code = 0;
    String sent_email = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        globals G = new globals();
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(G.get_main_color())));
        String title_HTML = "<font color=" + G.get_title_color() + ">Forgot Password</font>";
        getSupportActionBar().setTitle(Html.fromHtml(title_HTML));

        ETemail = (EditText) findViewById(R.id.email_res_pass);
        Bsend = (Button) findViewById(R.id.send_button_res_pass);
        ETreset_code = (EditText) findViewById(R.id.reset_code_res_pass);
        Bchange_password = (Button) findViewById(R.id.change_pass_res_pass);

        Bsend.setOnClickListener(this);
        Bchange_password.setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_reset_password, menu);
        return true;
    }



    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send_button_res_pass:
                final String email = ETemail.getText().toString();

                String file_name = "passwordReset.php";
                ArrayList<String> key = new ArrayList<>(Arrays.asList("email"));
                ArrayList<String> value = new ArrayList<>(Arrays.asList(email));
                server_request serverRequest = new server_request(this);
                serverRequest.server_request(file_name, key, value, new callback() {
                    @Override
                    public void done(String response) {

                        try {
                            JSONObject jObject = new JSONObject(response);
                            String status = jObject.getString("status");
                            if (!status.equals("success")) {
                                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(reset_password.this);
                                dialogBuilder.setMessage("Failed to send. Try again");
                                dialogBuilder.setPositiveButton("Ok", null);
                                dialogBuilder.show();
                            } else {
                                int reset_code = jObject.getInt("reset_code");
                                stored_reset_code = reset_code;
                                sent_email = email;
                                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(reset_password.this);
                                dialogBuilder.setMessage("Email with the reset password was sent");
                                dialogBuilder.setPositiveButton("Ok", null);
                                dialogBuilder.show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();

                        }
                    }


                });
                break;

            case R.id.change_pass_res_pass:
                String input_code = ETreset_code.getText().toString();
                if(input_code.equals(Integer.toString(stored_reset_code)))
                {
                    Intent i = new Intent(reset_password.this, change_password.class);
                    i.putExtra("email", sent_email);
                    startActivity(i);
                }
                else
                {
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(reset_password.this);
                    dialogBuilder.setMessage("Wrong reset code");
                    dialogBuilder.setPositiveButton("Ok", null);
                    dialogBuilder.show();
                }
                break;
        }
    }
}
