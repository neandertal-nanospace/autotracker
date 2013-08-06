package com.silentcorp.autotracker.controls.spinneradapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.silentcorp.autotracker.R;

/**
 * Fuel spinner adapter
 * 
 * @author neandertal
 */
public class FuelSpinnerAdapter extends BaseAdapter
{
    private List<LabelValuePair> list;
    private Context context;
    private String disabledValue;

    /**
     * Creates fuel spinner adapter with full fuel list.
     * 
     * @param context
     * @param addEmptyChoice - add an empty choice
     */
    public FuelSpinnerAdapter(Context contextArg, boolean addEmptyChoice)
    {
        context = contextArg;
        list = new ArrayList<LabelValuePair>();
        String[] labels = context.getResources().getStringArray(R.array.fuel_type_labels);
        String[] values = context.getResources().getStringArray(R.array.fuel_type_values);

        for (int i = 0; i < values.length; i++)
        {
            LabelValuePair pair = new LabelValuePair(labels[i], values[i]);
            list.add(pair);
        }

        if (addEmptyChoice)
        {
            String empty = context.getResources().getString(R.string.label_empty);
            LabelValuePair pair = new LabelValuePair(empty, null);
            list.add(pair);
        }
    }

    /**
     * Creates an adapter with only these values as choices. Can be changed
     * dynamically
     * 
     * @param contextArg
     * @param values - allowed choices
     */
    public FuelSpinnerAdapter(Context contextArg, String[] allowedValues)
    {
        context = contextArg;
        list = new ArrayList<LabelValuePair>();
        String[] labels = context.getResources().getStringArray(R.array.fuel_type_labels);
        String[] values = context.getResources().getStringArray(R.array.fuel_type_values);
        List<String> valuesList = Arrays.asList(values);

        for (String v : allowedValues)
        {
            int i = valuesList.indexOf(v);
            if (i < 0)
            {
                continue;
            }

            LabelValuePair pair = new LabelValuePair(labels[i], values[i]);
            list.add(pair);
        }
    }

    /**
     * Set new list of choices
     * 
     * @param list
     */
    public void setNewChoices(String[] allowedValues)
    {
        list.clear();
        String[] labels = context.getResources().getStringArray(R.array.fuel_type_labels);
        String[] values = context.getResources().getStringArray(R.array.fuel_type_values);
        List<String> valuesList = Arrays.asList(values);

        for (String v : allowedValues)
        {
            int i = valuesList.indexOf(v);
            if (i < 0)
            {
                continue;
            }

            LabelValuePair pair = new LabelValuePair(labels[i], values[i]);
            list.add(pair);
        }
        notifyDataSetChanged();
    }

    /**
     * Set value, which will be disabled in the drop down
     * 
     * @param value
     */
    public void setDisabledValue(String value)
    {
        disabledValue = value;
    }

    /**
     * Get position in array for pair with this value
     * 
     * @param value
     * @return position of LabelvaluePair, when the values match, or -1 if not
     *         found
     */
    public int getPosition(String value)
    {
        for (int i = 0; i < list.size(); i++)
        {
            LabelValuePair pair = list.get(i);
            if (value == null)
            {
                if (pair.getValue() == null)
                {
                    return i;
                }
            }
            else if (value.equals(pair.getValue()))
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
    public LabelValuePair getItem(int position)
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
        View view = createViewFromResource(position, convertView, parent, android.R.layout.simple_spinner_dropdown_item);

        if (disabledValue != null && disabledValue.equals(getItem(position).getValue()))
        {
            view.setEnabled(false); // greys out the text
            view.setClickable(true); // makes item unclickable
        }
        else
        {
            view.setEnabled(true); 
            view.setClickable(false);
        }

        return view;
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
        text.setText(getItem(position).getLabel());
        return view;
    }
}
