package com.noms.noms.groups_folder;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Paul on 9/15/2015.
 */
public class server_request {
    ProgressDialog progressDialog;
    public static final String SERVER_ADDRESS = "http://www.nomsscript-env.elasticbeanstalk.com/";

    public server_request(Context context) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Processing");
        progressDialog.setMessage("Please wait");
    }

    public void server_request(String file_name, ArrayList<String> key, ArrayList<String> value, callback callback)
    {
        progressDialog.show();
        new SR_AsyncTask(file_name, key,value,callback).execute();
    }

    public class SR_AsyncTask extends AsyncTask<Void, Void, String> {
        String file_name;
        ArrayList<String> key;
        ArrayList<String> value;
        callback callback;

        public SR_AsyncTask(String file_name, ArrayList<String> key, ArrayList<String> value, callback callback) {
            this.file_name = file_name;
            this.key = key;
            this.value = value;
            this.callback = callback;
        }

        @Override
        protected String doInBackground(Void... params) {
            String response = "";
            try {
                URL url = new URL(SERVER_ADDRESS + file_name);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                Uri.Builder builder = new Uri.Builder();
                for(int i = 0; i < key.size(); i++) {
                    builder.appendQueryParameter(key.get(i), value.get(i));
                }
                String query = builder.build().getEncodedQuery();
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();

                conn.connect();
                Scanner inStream = new Scanner(conn.getInputStream());
                while (inStream.hasNextLine()) {
                    response += (inStream.nextLine());
                }
                Log.d("test", "response: " + response);
                return response;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String response) {
            progressDialog.dismiss();
            callback.done(response);
            super.onPostExecute(response);
        }
    }


}
