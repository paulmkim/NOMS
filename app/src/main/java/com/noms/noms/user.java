package com.noms.noms;

/**
 * Created by Paul on 9/4/2015.
 */
public class user {

    public String first_name,last_name,email;
    public int id, birth_month,birth_day,birth_year,gender;

    public user(int id){
        this.id = id;
        this.first_name = "";
        this.last_name = "";
        this.email = "";
        this.birth_month = -1;
        this.birth_day = -1;
        this.birth_year = -1;
        this.gender = -1;
    }

    //for register
    public user(String first_name, String last_name, String email, int birth_month, int birth_day, int birth_year, int gender){
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.id = -1;
        this.birth_month = birth_month;
        this.birth_day = birth_day;
        this.birth_year = birth_year;
        this.gender = gender;
    }

    public user(int id, String first_name, String last_name, String email, int birth_month, int birth_day, int birth_year, int gender){
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.id = id;
        this.birth_month = birth_month;
        this.birth_day = birth_day;
        this.birth_year = birth_year;
        this.gender = gender;
    }
}
