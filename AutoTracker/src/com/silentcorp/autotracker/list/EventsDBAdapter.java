package com.silentcorp.autotracker.list;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.FilterQueryProvider;
import android.widget.TextView;

import com.silentcorp.autotracker.R;
import com.silentcorp.autotracker.db.EventDB;
import com.silentcorp.autotracker.db.Utils;

/**
 * DB adapter to link the Events ListView to the events table in the DB
 * 
 * @author neandertal
 */
public class EventsDBAdapter extends AbstractDBAdapter
{
    private Context context;
    private SimpleDateFormat formatter;
    
    private long todayDate = 0;
    private long yesterdayDate = 0;

    // Are fuel events hidden?
    private boolean hideFE = false;
    // Are maintenance events hidden?
    private boolean hideME = false;
    // Are repair events hidden?
    private boolean hideRE = false;
    // Are payment events hidden?
    private boolean hidePE = false;

    public EventsDBAdapter(Context contextArg)
    {
        super(contextArg);

        context = contextArg;

        // Date formatter
        formatter = new SimpleDateFormat(Utils.getSelectedDateFormat(context));

        //initialize today date and yesterday date
        Calendar c = new GregorianCalendar();
        c.setTimeInMillis(Utils.getCurrentDate());
        todayDate = c.getTimeInMillis();
        c.add(Calendar.DATE, -1); 
        yesterdayDate = c.getTimeInMillis();
        
        // Set filter manager
        setFilterQueryProvider(new FilterQueryProvider()
        {
            @Override
            public Cursor runQuery(CharSequence constraint)
            {
                if (constraint == null)
                {
                    // Disable filtering - show all
                    return EventDB.getFilteredCursor(context, false, false, false, false);
                }

                return EventDB.getFilteredCursor(context, hideFE, hideME, hideRE, hidePE);
            }
        });
    }

    /**
     * Overwritten to show/hide the date tag (grouping by date)
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor)
    {
        super.bindView(view, context, cursor);

        //date label
        TextView dateLabel = (TextView) view.findViewById(R.id.list_item_group_header);
        
        Long thisDate = EventDB.getEventDate(cursor);
        Long prevDate = null;

        // previous row, for comparison
        if (cursor.getPosition() > 0 && cursor.moveToPrevious())
        {
            prevDate = EventDB.getEventDate(cursor);
            cursor.moveToNext();
        }

        // enable date label separator if it's the first record, or
        // current date is different from the previous one
        if (prevDate == null || !prevDate.equals(thisDate))
        {
            String dateString = null;
            if (thisDate == todayDate)
            {
                dateString = context.getString(R.string.text_today);
            }
            else if (thisDate == yesterdayDate)
            {
                dateString = context.getString(R.string.text_yesterday);
            }
            else
            {
                dateString = formatter.format(new Date(thisDate));
            }
            
            dateLabel.setVisibility(View.VISIBLE);
            dateLabel.setText(dateString);
        }
        else
        {
            dateLabel.setVisibility(View.GONE);
        }
    }

    /**
     * Set hide Fuel events on/off
     * 
     * @param hideFEArg
     */
    public void setHideFE(boolean hideFEArg)
    {
        hideFE = hideFEArg;
    }

    /**
     * Is hide Fuel events on/off
     * 
     * @return
     */
    public boolean isHideFE()
    {
        return hideFE;
    }

    /**
     * Set hide Maintenance events on/off
     * 
     * @param hideME
     */
    public void setHideME(boolean hideMEArg)
    {
        hideME = hideMEArg;
    }

    /**
     * Is hide Maintenance events on/off
     * 
     * @return
     */
    public boolean isHideME()
    {
        return hideME;
    }

    /**
     * Set hide Repair events on/off
     * 
     * @param hideRE
     */
    public void setHideRE(boolean hideREArg)
    {
        hideRE = hideREArg;
    }

    /**
     * Is hide Repair events on/off
     * 
     * @return
     */
    public boolean isHideRE()
    {
        return hideRE;
    }

    /**
     * Set hide Payment events on/off
     * 
     * @param hideME
     */
    public void setHidePE(boolean hidePEArg)
    {
        hidePE = hidePEArg;
    }

    /**
     * Is hide Payment events on/off
     * 
     * @return
     */
    public boolean isHidePE()
    {
        return hidePE;
    }

    /**
     * Header text for event.
     */
    public String getHeaderText(Cursor c)
    {
        return EventDB.constructHeaderText(c, context);
    }

    /**
     * Description of the event
     */
    public String getDescriptionText(Cursor c)
    {
        return EventDB.constructDescriptionText(c, context);
    }

    /**
     * Event value
     */
    @Override
    public String getValueText(Cursor c)
    {
        return EventDB.constructValueText(c, context);
    }
}
