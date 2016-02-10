package com.noms.noms.groups_folder;

/**
 * Created by Paul on 9/15/2015.
 */
public class AFTG_friend_row_values {
    public int id;
    public String first_name;
    public String last_name;

    public AFTG_friend_row_values(int id, String first_name, String last_name)
    {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
    }

    public String full_name()
    {
        return first_name + " " + last_name;
    }
}
