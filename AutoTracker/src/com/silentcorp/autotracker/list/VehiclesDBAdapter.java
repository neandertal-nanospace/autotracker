package com.silentcorp.autotracker.list;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.TextView;

import com.silentcorp.autotracker.R;
import com.silentcorp.autotracker.db.VehicleDB;

/**
 * DB adapter to link the Vehicles ListView to the vehicles table in the DB
 * 
 * @author neandertal
 */
public class VehiclesDBAdapter extends AbstractDBAdapter
{

    public VehiclesDBAdapter(Context context)
    {
        super(context);
    }

    /**
     * Header text for vehicle is the vehicle's name.
     */
    public String getHeaderText(Cursor c)
    {
        return VehicleDB.constructHeaderText(c);
    }

    /**
     * Description of the vehicle is Make, Model, year, License plate (if these
     * are not NULL)
     */
    public String getDescriptionText(Cursor c)
    {
        return VehicleDB.constructDescriptionText(c);
    }

    /**
     * Overwritten to show/hide the sold tag (grouping by sold flag)
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor)
    {
        super.bindView(view, context, cursor);

        // sold vehicles group label
        TextView groupLabel = (TextView) view.findViewById(R.id.list_item_group_header);

        Boolean thisSellFlag = VehicleDB.getSellFlag(cursor);
        Boolean prevSellFlag = null;

        // previous row, for comparison
        if (cursor.getPosition() > 0 && cursor.moveToPrevious())
        {
            prevSellFlag = VehicleDB.getSellFlag(cursor);
            cursor.moveToNext();
        }

        // if sell flag is true and previous value is missing or FALSE
        // show group header
        if ((prevSellFlag == null || !prevSellFlag) && thisSellFlag)
        {
            String soldStr = context.getString(R.string.text_sold);
            groupLabel.setVisibility(View.VISIBLE);
            groupLabel.setText(soldStr);
        }
        else
        {
            groupLabel.setVisibility(View.GONE);
        }
    }
}
