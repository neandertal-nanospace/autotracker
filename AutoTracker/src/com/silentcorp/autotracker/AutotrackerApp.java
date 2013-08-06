package com.silentcorp.autotracker;

import android.app.Application;
import android.preference.PreferenceManager;

/**
 * Extends application.
 * 
 * @author neandertal
 */
public class AutotrackerApp extends Application
{
    /**
     *  called when opening the app
     */
    @Override
    public void onCreate()
    {
        super.onCreate();
        
        //Initialize default preferences
        PreferenceManager.setDefaultValues(this, R.xml.pref_general, false);
    }

}
