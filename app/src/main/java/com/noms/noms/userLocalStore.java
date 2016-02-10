package com.noms.noms;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Paul on 9/4/2015.
 */
public class userLocalStore {

    public static final String SP_NAME = "userDetails";
    SharedPreferences userLocalDatabase;

    public userLocalStore(Context context){
        userLocalDatabase = context.getSharedPreferences(SP_NAME,0);
    }

    public void storeUserData(user user){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putString("first_name", user.first_name);
        spEditor.putString("last_name", user.last_name);
        spEditor.putString("email", user.email);
        spEditor.putInt("id",user.id);
        spEditor.putInt("birth_month", user.birth_month);
        spEditor.putInt("birth_day", user.birth_day);
        spEditor.putInt("birth_year", user.birth_year);
        spEditor.putInt("gender", user.gender);
        spEditor.commit();
    }


    public boolean getUserLoggedIn(){
        if(userLocalDatabase.getBoolean("loggedIn",false) == true)
            return true;
        else
            return false;
    }

    public int getUserID(){
        return userLocalDatabase.getInt("id",0);
    }

    public String getUserName()
    {
        return userLocalDatabase.getString("first_name","") + " " + userLocalDatabase.getString("last_name","");
    }

    public String getUserEmail()
    {
        return userLocalDatabase.getString("email","");
    }

    public String getUserBirthday()
    {
        int birth_month = userLocalDatabase.getInt("birth_month",0);
        int birth_day = userLocalDatabase.getInt("birth_day",0);
        int birth_year = userLocalDatabase.getInt("birth_year",0);

        String birth_month_string = "";
        switch(birth_month)
        {
            case 1:
                birth_month_string = "January";
                break;
            case 2:
                birth_month_string = "February";
                break;
            case 3:
                birth_month_string = "March";
                break;
            case 4:
                birth_month_string = "April";
                break;
            case 5:
                birth_month_string ="May";
                break;
            case 6:
                birth_month_string = "June";
                break;
            case 7:
                birth_month_string = "July";
                break;
            case 8:
                birth_month_string = "August";
                break;
            case 9:
                birth_month_string = "September";
                break;
            case 10:
                birth_month_string = "October";
                break;
            case 11:
                birth_month_string = "November";
                break;
            case 12:
                birth_month_string = "December";
                break;
        }
        return birth_month_string + " " + birth_day + ", " + birth_year;
    }

    public int getUserGender()
    {
        return userLocalDatabase.getInt("gender",0);
    }

    public void setUserLoggedIn(boolean loggedIn){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putBoolean("loggedIn",loggedIn);
        spEditor.commit();
    }

    public void clearUserData(){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.clear();
        spEditor.commit();
    }
}
