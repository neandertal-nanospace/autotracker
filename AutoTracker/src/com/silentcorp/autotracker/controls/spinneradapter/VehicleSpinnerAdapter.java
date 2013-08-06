package com.silentcorp.autotracker.controls.spinneradapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.silentcorp.autotracker.R;
import com.silentcorp.autotracker.db.VehicleDB;

/**
 * Vehicle spinner adapter
 * 
 * @author neandertal
 */
public class VehicleSpinnerAdapter  extends BaseAdapter
{
    private List<NameIdPair> list;
    private Context context;

    /**
     * Creates vehicle spinner adapter with full vehicle names list. 
     * @param context
     * @param includeEmpty NOT_DEFINED choice
     */
    public VehicleSpinnerAdapter(Context contextArg, boolean includeEmpty)
    {
        context = contextArg;

        // Load vehicle names and IDs from DB
        list = VehicleDB.getVehiclesNameIDPair(contextArg);
        
        // Include empty choice at the end of the list
        if (includeEmpty)
        {
            String empty = contextArg.getResources().getString(R.string.label_empty);
            list.add(new NameIdPair(empty, null));
        }
    }

    /**
     * Get position in array for pair with this ID
     * 
     * @param value
     * @return position of NameIdPair, when the values match, or -1 if not
     *         found
     */
    public int getPosition(Long id)
    {
        for (int i = 0; i < list.size(); i++)
        {
            NameIdPair pair = list.get(i);
            if (id == null)
            {
                if (pair.getID() == null)
                {
                    return i;
                }
            }
            else if (id.equals(pair.getID()))
            {
                return i;
            }
        }

        return -1;
    }

    @Override
    public int getCount()
    {
        return list.size();
    }

    @Override
    public NameIdPair getItem(int position)
    {
        return list.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        return createViewFromResource(position, convertView, parent, android.R.layout.simple_spinner_item);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent)
    {
        return createViewFromResource(position, convertView, parent, android.R.layout.simple_spinner_dropdown_item);
    }
    
    private View createViewFromResource(int position, View convertView, ViewGroup parent, int resource)
    {
        View view;
        if (convertView == null)
        {
            LayoutInflater mInflater = LayoutInflater.from(context);
            view = mInflater.inflate(resource, parent, false);
        }
        else
        {
            view = convertView;
        }

        TextView text = (TextView) view;
        text.setText(getItem(position).getName());
        return view;
    }
}
