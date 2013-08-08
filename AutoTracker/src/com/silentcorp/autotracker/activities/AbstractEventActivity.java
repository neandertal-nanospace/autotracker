package com.silentcorp.autotracker.activities;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.silentcorp.autotracker.R;
import com.silentcorp.autotracker.beans.EventBean;
import com.silentcorp.autotracker.db.EventDB;
import com.silentcorp.autotracker.utils.Utils;

/**
 * Abstract parent for fuel event, service event, payment events activities
 * @author neandertal
 * 
 */
public abstract class AbstractEventActivity extends SherlockActivity
{
    // event bean
    protected EventBean event;
    // Map with the obligatory fields and their state - if filled - TRUE
    @SuppressLint("UseSparseArrays")
    protected Map<Integer, Boolean> obligatoryFields = new HashMap<Integer, Boolean>();

    ////////////////////
    // Methods to overwrite
    
    /**
     * Title for add event
     * @return
     */
    protected abstract int getAddTitleID();
    
    /**
     * Title for edit event
     * @return
     */
    protected abstract int getEditTitleID();
    
    /**
     * Activity layout ID
     * @return
     */
    protected abstract int getLayoutId();
    
    /**
     * Initialize obligatory components listeners
     */
    protected abstract void initListeners();
    
    /**
     * Initialize spinners
     */
    protected abstract void initChoices();
    
    /**
     * Create new event and set initial values
     * @return
     */
    protected abstract EventBean createNewEvent();
    
    /**
     * Loads event values to form views
     * @return
     */
    protected abstract void loadEventToForm();
    
    
    /**
     * Saves  form views to event
     * @return
     */
    protected abstract void saveFormToEvent();
    
    //////////////////
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        // back button in action bar
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // initial views settings
        initViews();
        
        // initialize any spinners and auto complete text views
        initChoices();
        
        // add listeners
        initListeners();
    }
    
    /**
     * Overwrite if needed
     */
    protected void initViews()
    {
        //Do nothing
    }
    
    /**
     * Verifies if all obligatory fields are state TRUE and enables the save
     * button
     */
    protected void checkObliagoryFieldsState()
    {
        boolean toEnable = true;
        Iterator<Boolean> iter = obligatoryFields.values().iterator();
        while (iter.hasNext())
        {
            if (Boolean.FALSE.equals(iter.next()))
            {
                toEnable = false;
                break;
            }
        }

        Button saveBtn = (Button) findViewById(R.id.save_button);
        saveBtn.setEnabled(toEnable);
    }
    
    @Override
    protected void onStart()
    {
        super.onStart();

        Log.d(AbstractEventActivity.class.getName(), "onStart()");

        Intent intent = getIntent();
        long eventID = intent.getLongExtra(Utils.KEY_SELECTED_ID, -1l);

        if (eventID < 0)
        {
            //change title
            setTitle(getAddTitleID());
            
            // called to create new event
            event = createNewEvent();
            Log.d(AbstractEventActivity.class.getName(), "Created new event.");
        }
        else
        {
            //change title
            setTitle(getEditTitleID());
            
            event = EventDB.readEvent(this, eventID, event);
            Log.d(AbstractEventActivity.class.getName(), "Read event from DB:" + eventID);
        }
        
        // called to load to form
        loadEventToForm();
    }
    
    /**
     * Called by "Cancel" button
     * 
     * @param view
     */
    public void onCancel(View view)
    {
        Log.d(AbstractEventActivity.class.getName(), "onCancel()");
        setResult(RESULT_CANCELED);
        finish();
    }
    
    /**
     * Called by "Save" button
     * 
     * @param view
     */
    public void onSave(View view)
    {
        Log.d(AbstractEventActivity.class.getName(), "onSave()");
        saveFormToEvent();

        // Update DB
        if (event.getId() < 0)
        {
            EventDB.createEvent(this, event);
        }
        else
        {
            EventDB.updateEvent(this, event);
        }

        // set result to calling activity
        Intent resultIntent = new Intent();
        resultIntent.putExtra(Utils.KEY_SELECTED_ID, event.getId());
        setResult(RESULT_OK, resultIntent);
        // finish activity
        finish();
    }
    
    /**
     * Back key pressed - cancel the operation.
     */
    @Override
    public void onBackPressed()
    {
        onCancel(null);
        return;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // back button in action bar
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return false;
    }
}
