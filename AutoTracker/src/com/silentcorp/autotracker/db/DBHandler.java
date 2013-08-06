package com.silentcorp.autotracker.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Create and upgrade the DB of the app.
 * @author neandertal
 */
public class DBHandler extends SQLiteOpenHelper
{
    // Database Version
    private static final int DATABASE_VERSION = 6;
 
    // Database Name
    private static final String DATABASE_NAME = "autotracker";
 
    /**
     * Constructor
     * @param context
     */
    public DBHandler(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        //Create tables
        VehicleDB.onCreate(db);
        NotificationDB.onCreate(db);
        EventDB.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        //Update tables
        VehicleDB.onUpgrade(db, oldVersion, newVersion);
        NotificationDB.onUpgrade(db, oldVersion, newVersion);
        EventDB.onUpgrade(db, oldVersion, newVersion);
    }

}
