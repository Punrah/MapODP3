package com.example.startup.mapodp.helper;

import android.content.Context;

/**
 * Created by Startup on 2/1/17.
 */

public class UserGlobal {

    private static UserSQLiteHandler db;

    public static User getUser(Context context)
    {
        db = new UserSQLiteHandler(context);
        return db.getUserDetails();

    }
}
