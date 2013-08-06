package com.silentcorp.autotracker.activities;

import java.util.Set;

import android.content.Intent;
import android.database.Cursor;
import android.support.v4.content.Loader;
import android.util.Log;

import com.actionbarsherlock.view.Menu;
import com.silentcorp.autotracker.R;
import com.silentcorp.autotracker.db.Utils;
import com.silentcorp.autotracker.db.VehicleDB;
import com.silentcorp.autotracker.list.AbstractDBAdapter;
import com.silentcorp.autotracker.list.VehiclesDBAdapter;

public class ListVehiclesActivity extends AbstractListActivity
{
    // Vehicles loader id
    private static final int VEHICLES_LOADER_ID = "VEHICLES_LOADER_ID".hashCode();

    // /////////////////////////
    // Abstract

    /**
     * Vehicle loader ID
     */
    @Override
    protected int getLoaderID()
    {
        return VEHICLES_LOADER_ID;
    };

    /**
     * Return vehicles cursor loader
     */
    @Override
    protected Loader<Cursor> getCursorLoader()
    {
        return VehicleDB.getCursorLoader(this);
    }

    /**
     * Vehicles DB Adapter
     */
    @Override
    protected AbstractDBAdapter getDBAdapter()
    {
        return new VehiclesDBAdapter(this);
    }

    /**
     * When add item button in the menu or in the header is pressed
     */
    @Override
    protected void onAddItem()
    {
        Log.d(ListVehiclesActivity.class.getName(), "onAddItem()");

        // start details activity to create new item
        Intent intent = new Intent(this, VehicleActivity.class);
        startActivityForResult(intent, CREATE_OR_EDIT_CODE);
    }
    
    /**
     * Called when needed to open a item for edit
     */
    @Override
    protected void onOpenItem(int position, long itemID)
    {
        Log.d(ListVehiclesActivity.class.getName(), "onOpenItem() with id:" + itemID);

        // start details activity to open for edit new item
        Intent intent = new Intent(this, VehicleActivity.class);
        intent.putExtra(Utils.KEY_SELECTED_ID, itemID);
        startActivityForResult(intent, CREATE_OR_EDIT_CODE);
    }
    
    /**
     * On success message
     */
    @Override
    protected String getOnSaveMessage()
    {
        return getResources().getString(R.string.text_vehicle_saved);
    }

    /**
     * Confirm dialog question text 
     */
    @Override
    protected String getConfirmDelMessage(int selected)
    {
        String question = getResources().getQuantityString(R.plurals.text_vehicle_delete_question, selected);
        return String.format(question, selected);
    }
    
    /**
     * On delete success message
     */
    @Override
    protected String getOnDeleteMessage(int selected)
    {
        String question = getResources().getQuantityString(R.plurals.text_vehicle_delete_success, selected);
        return String.format(question, selected);
    }
    
    /**
     * Delete items
     */
    @Override
    protected void deleteItems(Set<Long> ids)
    {
        VehicleDB.deleteVehicles(this, ids);
    }

    // /////////////////////////
    // GUI


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getSupportMenuInflater().inflate(R.menu.list_vehicles, menu);

        return true;
    }

}
