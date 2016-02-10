package com.noms.noms.groups_folder;

/**
 * Created by Paul on 9/15/2015.
 */
public class group_info_row_values {
    public String first_name;
    public String last_name;
    public int member_id;

    public group_info_row_values(String first_name, String last_name, int member_id)
    {
        this.first_name = first_name;
        this.last_name = last_name;
        this.member_id = member_id;
    }

    public String full_name()
    {
        return first_name + " " + last_name;
    }
}
