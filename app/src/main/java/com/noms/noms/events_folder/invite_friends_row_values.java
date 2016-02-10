package com.noms.noms.events_folder;

/**
 * Created by Paul on 9/15/2015.
 */
public class invite_friends_row_values {
    public boolean friend; //true = friend, false = group

    //friend values
    public int id;
    public String first_name;
    public String last_name;

    //group values
    public String group_name;
    public int num_members;

    public invite_friends_row_values(boolean friend, int id, String first_name, String last_name, String group_name, int num_members)
    {
        this.friend = friend;
        if(friend) {
            this.id = id;
            this.first_name = first_name;
            this.last_name = last_name;
            this.group_name = "";
            this.num_members = -1;
        }
        else
        {
            this.id = -1;
            this.first_name = "";
            this.last_name = "";
            this.group_name = group_name;
            this.num_members = num_members;
        }
    }

    public String full_name()
    {
        if(friend)
            return first_name + " " + last_name;
        else
            return group_name;
    }
}
