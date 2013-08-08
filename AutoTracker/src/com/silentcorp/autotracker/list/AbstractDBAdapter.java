package com.silentcorp.autotracker.list;

import java.util.HashSet;
import java.util.Set;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.silentcorp.autotracker.R;
import com.silentcorp.autotracker.utils.Utils;

/**
 * Abstract DB adapter to link the ListView to the data in the DB
 * 
 * @author neandertal
 */
public abstract class AbstractDBAdapter extends CursorAdapter implements OnCheckedChangeListener
{
    // layout manager
    private LayoutInflater mInflater;

    // Contains the IDs of the selected elements
    private Set<Long> selectedElements = new HashSet<Long>();

    public AbstractDBAdapter(Context context)
    {
        super(context, null, true);

        mInflater = LayoutInflater.from(context);
    }

    /**
     * Called to read the data from the DB record and populate the views
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor)
    {
        Long id = Utils.readLong(cursor, Utils.COL_ID, null);
        
        CheckBox checkBox = (CheckBox) view.findViewById(R.id.list_item_check_box);
        // checked listener
        checkBox.setTag(id);
        checkBox.setOnCheckedChangeListener(this);
        //checked state
        checkBox.setChecked(selectedElements.contains(id));

       //ImageView icon = (ImageView) view.findViewById(R.id.list_item_icon);
        //TODO set custom icon
        
        TextView header = (TextView) view.findViewById(R.id.list_item_header);
        TextView description = (TextView) view.findViewById(R.id.list_item_description);
        TextView value = (TextView) view.findViewById(R.id.list_item_value);

        header.setText(getHeaderText(cursor));
        description.setText(getDescriptionText(cursor));
        value.setText(getValueText(cursor));
    }

    /**
     * Called to create a new list item view
     */
    @Override
    public View newView(Context arg0, Cursor arg1, ViewGroup arg2)
    {
        return mInflater.inflate(R.layout.list_element, null);
    }

    /**
     * Provides the header text
     * 
     * @param c DB cursor
     * @return the header text
     */
    public abstract String getHeaderText(Cursor c);

    /**
     * Provides the description text.
     * 
     * @param c DB cursor
     * @return the description text
     */
    public abstract String getDescriptionText(Cursor c);

    /**
     * Provides the value text
     * 
     * @param cursor DB cursor
     * @return value text, or NULL if none
     */
    public String getValueText(Cursor c)
    {
        return null;
    }

    /**
     * Called when a check box is checked/unchecked
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
    {
        Long idLong = (Long) buttonView.getTag();
        
        if (isChecked)
        {
            selectedElements.add(idLong);
        }
        else
        {
            selectedElements.remove(idLong);
        }
    }
    
    /**
     * Return selected IDs
     * @return
     */
    public Set<Long> getSelected()
    {
        return selectedElements;
    }
    
    /**
     * Clear selected IDs
     * @return
     */
    public void clearSelected()
    {
        selectedElements.clear();
    }
    
    /**
     * If selected items return true
     * @return
     */
    public boolean hasSelected()
    {
        return !selectedElements.isEmpty();
    }
}
