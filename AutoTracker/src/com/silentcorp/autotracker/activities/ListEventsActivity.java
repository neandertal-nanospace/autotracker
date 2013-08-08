package com.silentcorp.autotracker.activities;

import java.util.Set;

import android.content.Intent;
import android.database.Cursor;
import android.support.v4.content.Loader;
import android.util.Log;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.silentcorp.autotracker.R;
import com.silentcorp.autotracker.db.EventDB;
import com.silentcorp.autotracker.list.AbstractDBAdapter;
import com.silentcorp.autotracker.list.EventsDBAdapter;
import com.silentcorp.autotracker.utils.EventType;
import com.silentcorp.autotracker.utils.Utils;

/**
 * List of fill-up, service and payments events
 * 
 * @author neandertal
 * 
 */
public class ListEventsActivity extends AbstractListActivity
{
    // Events loader id
    private static final int EVENTS_LOADER_ID = "EVENTS_LOADER_ID".hashCode();

    /**
     * Events loader id
     */
    @Override
    protected int getLoaderID()
    {
        return EVENTS_LOADER_ID;
    }

    /**
     * Return events loader
     */
    @Override
    protected Loader<Cursor> getCursorLoader()
    {
        return EventDB.getCursorLoader(this);
    }
    
    /**
     * Events DB adapter
     */
    @Override
    protected AbstractDBAdapter getDBAdapter()
    {
        return new EventsDBAdapter(this);
    }

    @Override
    protected void onAddItem()
    {
        //does nothing
    }
    
    /**
     * On success message
     */
    @Override
    protected String getOnSaveMessage()
    {
        return getResources().getString(R.string.text_event_saved);
    }

    /**
     * Confirm dialog question text
     */
    @Override
    protected String getConfirmDelMessage(int selected)
    {
        String question = getResources().getQuantityString(R.plurals.text_event_delete_question, selected);
        return String.format(question, selected);
    }

    /**
     * On delete success message
     */
    @Override
    protected String getOnDeleteMessage(int selected)
    {
        String question = getResources().getQuantityString(R.plurals.text_event_delete_success, selected);
        return String.format(question, selected);
    }

    /**
     * Delete events
     */
    @Override
    protected void deleteItems(Set<Long> ids)
    {
        EventDB.deleteEvents(this, ids);
    }

    // /////////////////////////
    // GUI

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getSupportMenuInflater().inflate(R.menu.list_events, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        boolean result = false;
        // back button in action bar
        if (item.getItemId() == android.R.id.home)
        {
            onCancel();
        }
        else if (item.getItemId() == R.id.filter_fuel_events)
        {
            // filter out fuel events
            onFilterFuelEvent(item);
            result = true;
        }
        else if (item.getItemId() == R.id.filter_maintenance_events)
        {
            // filter out maintenance events
            onFilterMaintenanceEvent(item);
            result = true;
        }
        else if (item.getItemId() == R.id.filter_repair_events)
        {
            // filter out repair events
            onFilterRepairEvent(item);
            result = true;
        }
        else if (item.getItemId() == R.id.filter_payment_events)
        {
            // filter out payment events
            onFilterPaymentEvent(item);
            result = true;
        }
        else if (item.getItemId() == R.id.add_fuel_event || item.getItemId() == R.id.add_fuel_action)
        {
            // start details activity to create new fuel event
            onAddFuelEvent();
            result = true;
        }
        else if (item.getItemId() == R.id.add_repair_event || item.getItemId() == R.id.add_repair_action)
        {
            // start details activity to create new item
            onAddRepairEvent();
            result = true;
        }
        else if (item.getItemId() == R.id.add_maintenance_event || item.getItemId() == R.id.add_maintenance_action)
        {
            // start details activity to create new item
            onAddMaintenanceEvent();
            result = true;
        }
        else if (item.getItemId() == R.id.add_payment_event || item.getItemId() == R.id.add_payment_action)
        {
            // start details activity to create new item
            onAddPaymentEvent();
            result = true;
        }
        else if (item.getItemId() == R.id.delete_action)
        {
            // delete selected items
            onDeleteItems();
            result = true;
        }
        else
        {
            result = super.onOptionsItemSelected(item);
        }
        return result;
    }

    private void onAddPaymentEvent()
    {
        Log.d(ListEventsActivity.class.getName(), "onAddPaymentEvent()");

        // start details activity to create new item
        Intent intent = new Intent(this, PaymentEventActivity.class);
        startActivityForResult(intent, CREATE_OR_EDIT_CODE);
    }

    private void onAddMaintenanceEvent()
    {
        Log.d(ListEventsActivity.class.getName(), "onAddMaintenanceEvent()");

        // start details activity to create new item
        Intent intent = new Intent(this, MaintenanceEventActivity.class);
        startActivityForResult(intent, CREATE_OR_EDIT_CODE);
    }

    private void onAddRepairEvent()
    {
        Log.d(ListEventsActivity.class.getName(), "onAddRepairEvent()");

        // start details activity to create new item
        Intent intent = new Intent(this, RepairEventActivity.class);
        startActivityForResult(intent, CREATE_OR_EDIT_CODE);

    }

    private void onAddFuelEvent()
    {
        Log.d(ListEventsActivity.class.getName(), "onAddFuelEvent()");

        // start details activity to create new item
        Intent intent = new Intent(this, FuelEventActivity.class);
        startActivityForResult(intent, CREATE_OR_EDIT_CODE);
    }

    /**
     * Change text and icon and filter the list from fuel events
     * 
     * @param item
     */
    private void onFilterFuelEvent(MenuItem item)
    {
        EventsDBAdapter adapter = (EventsDBAdapter) listAdapter;

        // item.setIcon(iconRes);//TODO change icon
        item.setTitle((adapter.isHideFE()) ? R.string.text_hide_fill_up : R.string.text_show_fill_up);
        adapter.setHideFE(!adapter.isHideFE());
        adapter.getFilter().filter("");
    }

    /**
     * Change text and icon and filter the list from maintenance events
     * 
     * @param item
     */
    private void onFilterMaintenanceEvent(MenuItem item)
    {
        EventsDBAdapter adapter = (EventsDBAdapter) listAdapter;

        // item.setIcon(iconRes);//TODO change icon
        item.setTitle((adapter.isHideME()) ? R.string.text_hide_maintenance : R.string.text_show_maintenance);
        adapter.setHideME(!adapter.isHideME());
        adapter.getFilter().filter("");
    }

    /**
     * Change text and icon and filter the list from repair events
     * 
     * @param item
     */
    private void onFilterRepairEvent(MenuItem item)
    {
        EventsDBAdapter adapter = (EventsDBAdapter) listAdapter;

        // item.setIcon(iconRes);//TODO change icon
        item.setTitle((adapter.isHideRE()) ? R.string.text_hide_repairs : R.string.text_show_repairs);
        adapter.setHideRE(!adapter.isHideRE());
        adapter.getFilter().filter("");
    }

    /**
     * Change text and icon and filter the list from payment events
     * 
     * @param item
     */
    private void onFilterPaymentEvent(MenuItem item)
    {
        EventsDBAdapter adapter = (EventsDBAdapter) listAdapter;

        // item.setIcon(iconRes);//TODO change icon
        item.setTitle((adapter.isHidePE()) ? R.string.text_hide_payments : R.string.text_show_payments);
        adapter.setHidePE(!adapter.isHidePE());
        adapter.getFilter().filter("");
    }

    @Override
    protected void onOpenItem(int position, long itemID)
    {
        Log.d(ListEventsActivity.class.getName(), "onOpenItem() with id:" + itemID);

        Cursor c = (Cursor) listAdapter.getItem(position);
        EventType type = EventDB.getEventType(c);
        Intent intent = null;
        if (EventType.FUEL_EVENT.equals(type))
        {
            intent = new Intent(this, FuelEventActivity.class);
        }
        else if (EventType.MAINTENANCE_EVENT.equals(type))
        {
            intent = new Intent(this, MaintenanceEventActivity.class);
        }
        else if (EventType.REPAIR_EVENT.equals(type))
        {
            intent = new Intent(this, RepairEventActivity.class);
        }
        else if (EventType.PAYMENT_EVENT.equals(type))
        {
            intent = new Intent(this, PaymentEventActivity.class);
        }
        else
        {
            Log.w(ListEventsActivity.class.getName(), "Unknow type to open:" + type);
            return;
        }

        intent.putExtra(Utils.KEY_SELECTED_ID, itemID);
        startActivityForResult(intent, CREATE_OR_EDIT_CODE);
    }
}
