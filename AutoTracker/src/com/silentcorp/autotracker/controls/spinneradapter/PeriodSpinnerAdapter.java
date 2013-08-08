package com.silentcorp.autotracker.controls.spinneradapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.silentcorp.autotracker.R;

/**
 * Time period spinner adapter
 * 
 * @author neandertal
 */
public class PeriodSpinnerAdapter extends BaseAdapter implements ISpinnerAdapter
{
    private List<LabelValuePair> list;
    private Context context;

    /**
     * Creates period spinner adapter with full list.
     * 
     * @param context
     * @param addEmptyChoice - add an empty choice
     */
    public PeriodSpinnerAdapter(Context contextArg)
    {
        context = contextArg;
        list = new ArrayList<LabelValuePair>();
        String[] labels = context.getResources().getStringArray(R.array.period_repeat_type_labels);
        String[] values = context.getResources().getStringArray(R.array.period_repeat_type_values);

        for (int i = 0; i < values.length; i++)
        {
            LabelValuePair pair = new LabelValuePair(labels[i], values[i]);
            list.add(pair);
        }
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
        text.setText(getItem(position).getLabel());
        return view;
    }
}
