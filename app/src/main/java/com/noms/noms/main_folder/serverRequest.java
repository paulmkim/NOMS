package com.noms.noms.main_folder;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.noms.noms.user;

import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;


/**
 * Created by Paul on 9/12/2015.
 */
public class serverRequest {
    ProgressDialog progressDialog;
    public static final String SERVER_ADDRESS = "http://www.nomsscript-env.elasticbeanstalk.com/";

    public serverRequest(Context context) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Processing");
        progressDialog.setMessage("Please wait");
    }

    public void login(String email, String password, loginCallback userCallback) {
        progressDialog.show();
        new loginAsyncTask(email, password, userCallback).execute();
    }

    public class loginAsyncTask extends AsyncTask<Void, Void, user> {
        String email, password;
        loginCallback userCallback;

        public loginAsyncTask(String email, String password, loginCallback userCallback) {
            this.email = email;
            this.password = password;
            this.userCallback = userCallback;
        }

        @Override
        protected user doInBackground(Void... params) {
            String response = "";
            user returnedUser;

            try {
                URL url = new URL(SERVER_ADDRESS + "login.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                //HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("email", email)
                        .appendQueryParameter("password", password);

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

                JSONObject jObject = new JSONObject(response);
                String status = jObject.getString("status");
                int userID = jObject.getInt("id");
                if (status.equals("success")) {
                    String first_name = jObject.getString("first_name");
                    String last_name = jObject.getString("last_name");
                    String email = jObject.getString("email");
                    int birth_month = jObject.getInt("birth_month");
                    int birth_day = jObject.getInt("birth_day");
                    int birth_year = jObject.getInt("birth_year");
                    int gender = jObject.getInt("gender");
                    returnedUser = new user(userID, first_name, last_name, email, birth_month, birth_day, birth_year, gender);
                } else {
                    returnedUser = null;
                }

                return returnedUser;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(user returnedUser) {
            progressDialog.dismiss();
            userCallback.done(returnedUser);
            super.onPostExecute(returnedUser);
        }
    }

    public void register(user user, String password, boolCallback boolCallback) {
        progressDialog.show();
        new registerAsyncTask(user, password, boolCallback).execute();
    }

    public class registerAsyncTask extends AsyncTask<Void, Void, Boolean> {
        user user;
        boolCallback boolCallback;
        String password;

        public registerAsyncTask(user user, String password, boolCallback boolCallback) {
            this.user = user;
            this.boolCallback = boolCallback;
            this.password = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            String response = "";
            try {
                URL url = new URL(SERVER_ADDRESS + "registerUser.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("first_name", user.first_name)
                        .appendQueryParameter("last_name", user.last_name)
                        .appendQueryParameter("email", user.email)
                        .appendQueryParameter("password", password)
                        .appendQueryParameter("birth_month", Integer.toString(user.birth_month))
                        .appendQueryParameter("birth_day", Integer.toString(user.birth_day))
                        .appendQueryParameter("birth_year", Integer.toString(user.birth_year))
                        .appendQueryParameter("gender", Integer.toString(user.gender));

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

                JSONObject jObject = new JSONObject(response);
                String status = jObject.getString("status");

                if (status.equals("success")) {
                    return true;
                } else {
                    return false;
                }

            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean status) {
            progressDialog.dismiss();
            boolCallback.done(status);
            super.onPostExecute(status);
        }
    }





    public void user_friends(int id, user_friendsCallback user_friendsCallback) {
        progressDialog.show();
        new user_friendsAsyncTask(id, user_friendsCallback).execute();
    }

    public class user_friendsAsyncTask extends AsyncTask<Void, Void, Boolean> {
        int id;
        user_friendsCallback user_friendsCallback;

        public user_friendsAsyncTask(int id, user_friendsCallback user_friendsCallback) {
            this.id = id;
            this.user_friendsCallback = user_friendsCallback;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            String response = "";
            try {
                URL url = new URL(SERVER_ADDRESS + "userFriends.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("user", Integer.toString(id));

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

                JSONObject jObject = new JSONObject(response);

                String friend0 = jObject.getJSONArray("0").getString(1);
                Log.d("test", friend0);
                String status = jObject.getString("status");
                if (status.equals("success")) {
                    return true;
                } else {
                    return false;
                }

            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }
}
