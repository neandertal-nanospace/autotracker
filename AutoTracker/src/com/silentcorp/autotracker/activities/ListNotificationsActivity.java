package com.silentcorp.autotracker.activities;

import java.util.Set;

import android.content.Intent;
import android.database.Cursor;
import android.support.v4.content.Loader;
import android.util.Log;

import com.actionbarsherlock.view.Menu;
import com.silentcorp.autotracker.R;
import com.silentcorp.autotracker.db.NotificationDB;
import com.silentcorp.autotracker.list.AbstractDBAdapter;
import com.silentcorp.autotracker.list.NotificationsDBAdapter;
import com.silentcorp.autotracker.utils.Utils;

/**
 * Lists of notifications screen
 * 
 * @author neandertal
 * 
 */
public class ListNotificationsActivity extends AbstractListActivity
{
    // Notifications loader id
    private static final int NOTIF_LOADER_ID = "NOTIF_LOADER_ID".hashCode();

    /**
     * Notifications loader id
     */
    @Override
    protected int getLoaderID()
    {
        return NOTIF_LOADER_ID;
    }

    /**
     * Return Notifications loader
     */
    @Override
    protected Loader<Cursor> getCursorLoader()
    {
        return NotificationDB.getCursorLoader(this);
    }

    /**
     * Notifications DB adapter
     */
    @Override
    protected AbstractDBAdapter getDBAdapter()
    {
        return new NotificationsDBAdapter(this);
    }

    /**
     * When add item button in the menu or in the header is pressed
     */
    @Override
    protected void onAddItem()
    {
        Log.d(ListNotificationsActivity.class.getName(), "onAddItem()");

        // start details activity to create new item
        Intent intent = new Intent(this, NotificationActivity.class);
        startActivityForResult(intent, CREATE_OR_EDIT_CODE);
    }
    

    /**
     * Called when needed to open a item for edit
     */
    @Override
    protected void onOpenItem(int position, long itemID)
    {
        Log.d(ListNotificationsActivity.class.getName(), "onOpenItem() with id:" + itemID);

        // start details activity to open for edit new item
        Intent intent = new Intent(this, NotificationActivity.class);
        intent.putExtra(Utils.KEY_SELECTED_ID, itemID);
        startActivityForResult(intent, CREATE_OR_EDIT_CODE);
    }
    
    /**
     * On success message
     */
    @Override
    protected String getOnSaveMessage()
    {
        return getResources().getString(R.string.text_notification_saved);
    }

    /**
     * Confirm dialog question text
     */
    @Override
    protected String getConfirmDelMessage(int selected)
    {
        String question = getResources().getQuantityString(R.plurals.text_notification_delete_question, selected);
        return String.format(question, selected);
    }

    /**
     * On delete success message
     */
    @Override
    protected String getOnDeleteMessage(int selected)
    {
        String question = getResources().getQuantityString(R.plurals.text_notification_delete_success, selected);
        return String.format(question, selected);
    }

    /**
     * Delete notifications
     */
    @Override
    protected void deleteItems(Set<Long> ids)
    {
        NotificationDB.deleteNotifications(this, ids);
    }

    // /////////////////////////
    // GUI

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getSupportMenuInflater().inflate(R.menu.list_notifications, menu);

        return true;
    }
}
