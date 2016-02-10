package com.noms.noms;

import android.app.Application;

/**
 * Created by Paul on 1/23/2016.
 */
public class globals extends Application {
    private String main_color = "#ff751a";
    private String secondary_color = "#bfbfbf";
    private String grey = "#f2f2f2";
    private String title_color = "#000000";
    public String get_main_color(){
        return this.main_color;
    }
    public String get_secondary_color() { return this.secondary_color; }
    public String get_grey_color() {return this.grey; }
    public String get_title_color() {return this.title_color;}
}
