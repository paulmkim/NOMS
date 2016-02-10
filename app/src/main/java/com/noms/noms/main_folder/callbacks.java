package com.noms.noms.main_folder;

import com.noms.noms.user;

/**
 * Created by Paul on 9/12/2015.
 */
interface boolCallback {
    void done(boolean status);
}

interface loginCallback {
    void done(user returnedUser);
}

interface user_friendsCallback{
    void done(Integer num_friends,boolean status);
}