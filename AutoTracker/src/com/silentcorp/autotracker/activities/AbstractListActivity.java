package com.silentcorp.autotracker.activities;

import java.util.Set;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.silentcorp.autotracker.R;
import com.silentcorp.autotracker.list.AbstractDBAdapter;

public abstract class AbstractListActivity extends SherlockFragmentActivity implements LoaderCallbacks<Cursor>, DialogInterface.OnClickListener
{
    // Used to call detail activity and catch result
    protected static final int CREATE_OR_EDIT_CODE = 1234;

    // Cursor adapter
    protected AbstractDBAdapter listAdapter = null;

    // /////////////////////////
    // Abstract

    /**
     * Returns loader id for this activity
     * 
     * @return
     */
    abstract protected int getLoaderID();

    /**
     * return cursor loader for this list activity
     * @return
     */
    abstract protected Loader<Cursor> getCursorLoader();
    
    /**
     * Return DBAdapter implementation
     * 
     * @return
     */
    abstract protected AbstractDBAdapter getDBAdapter();

    /**
     * When add item button in the menu or in the header is pressed
     */
    abstract protected void onAddItem();

    /**
     * Called when needed to open a item for edit
     */
    abstract protected void onOpenItem(int position, long itemID);

    
    /**
     * Return success result message id
     * 
     * @return
     */
    abstract protected String getOnSaveMessage();

    /**
     * Message when successfully deleted items
     * @return
     */
    abstract protected String getOnDeleteMessage(int selected);
    
    /**
     * Confirmation question in dialog
     * @param selectedForDelete
     * @return
     */
    abstract protected String getConfirmDelMessage(int selectedForDelete);
    
    /**
     * Call to delete items
     * 
     * @param cr
     * @param ids
     */
    abstract protected void deleteItems(Set<Long> ids);

    // /////////////////////////
    // GUI

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        // back button in Sherlock action bar
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // initialize loader
        getSupportLoaderManager().initLoader(getLoaderID(), null, this);

        // init list
        initList();
    }

    /**
     * Loads and sets the list view cursor adapter
     * 
     * @param listView
     */
    private void initList()
    {
        // initialize and set adapter
        listAdapter = getDBAdapter();
        ListView itemsList = (ListView) findViewById(R.id.list_items);
        itemsList.setAdapter(listAdapter);

        // set on click listener to open details view
        itemsList.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> list, View view, int index, long id)
            {
                Log.d(AbstractListActivity.class.getName(), "onItemClick()");

                onOpenItem(index, id);
            }
        });
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        // Starts a new or restarts an existing Loader in this manager
        getSupportLoaderManager().restartLoader(getLoaderID(), null, this);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        Log.d(AbstractListActivity.class.getName(), "onPrepareOptionsMenu()");
        // disable/enable delete menu item
        MenuItem delItem = menu.findItem(R.id.delete_action);
        delItem.setEnabled(listAdapter.hasSelected());

        return super.onPrepareOptionsMenu(menu);
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
        else if (item.getItemId() == R.id.add_action || item.getItemId() == R.id.action_bar_add_action)
        {
            // start details activity to create new item
            onAddItem();
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

    /**
     * On return button pressed, back from header button or hardware back button
     * pressed
     * 
     * @param view
     */
    public void onCancel()
    {
        Log.d(AbstractListActivity.class.getName(), "onCancel()");
        setResult(RESULT_CANCELED);
        finish();
    }

    /**
     * Back key pressed - cancel the operation.
     */
    @Override
    public void onBackPressed()
    {
        onCancel();
        return;
    }

    /**
     * When delete button from the menu is pressed
     */
    protected void onDeleteItems()
    {
        Log.d(AbstractListActivity.class.getName(), "onDeleteItems()");

        int selected = listAdapter.getSelected().size();

        // build confirm dialog
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(R.string.text_confirm);
        alertDialog.setMessage(getConfirmDelMessage(selected));
        alertDialog.setIcon(R.drawable.abs__ic_go);// TODO my icon

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton(R.string.text_yes, this);

        // Setting Negative "NO" Button
        alertDialog.setNegativeButton(R.string.text_no, this);

        // Showing Alert Message
        alertDialog.show();
    }

    /**
     * Called when delete confirmation dialog buttons pressed
     */
    public void onClick(DialogInterface dialog, int which)
    {
        if (which == DialogInterface.BUTTON_POSITIVE)
        {
            int selected = listAdapter.getSelected().size();
            // delete items
            deleteItems(listAdapter.getSelected());
            // clear selection
            listAdapter.clearSelected();
            // refresh list
            getSupportLoaderManager().restartLoader(getLoaderID(), null, this);
            //show delete success message
            Toast toast = Toast.makeText(getApplicationContext(), getOnDeleteMessage(selected), Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        // If response to create or edit item event
        if (requestCode == CREATE_OR_EDIT_CODE && resultCode == RESULT_OK)
        {
            Toast toast = Toast.makeText(getApplicationContext(), getOnSaveMessage(), Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    // /////////////////////////
    // Data loader manager

    @Override
    public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1)
    {
        return getCursorLoader();
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor)
    {
        if (listAdapter != null && cursor != null)
        {
            listAdapter.swapCursor(cursor);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> arg0)
    {
        if (listAdapter != null)
        {
            listAdapter.swapCursor(null);
        }
    }
}
