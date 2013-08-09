package com.silentcorp.autotracker.controls;

import android.content.Context;
import android.content.res.TypedArray;

import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

import com.silentcorp.autotracker.R;
import com.silentcorp.autotracker.utils.Utils;


/**
 * Text view to display a number and a suffix. Same as NumberView but not able
 * to edit the number
 * 
 * @author neandertal
 * 
 */
public class SuffixView extends TextView
{
    private Double value;
    private boolean isDecimal = false;
    private String suffix = null;

    public SuffixView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    public SuffixView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(context, attrs);
    }

    public SuffixView(Context context)
    {
        super(context);
        init(context, null);
    }

    private void init(Context context, AttributeSet attrs)
    {
        setClickable(true);
        setFocusable(false);
        setGravity(Gravity.RIGHT);
        
        if (attrs != null)
        {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SuffixView, 0, 0);
            isDecimal = a.getBoolean(R.styleable.SuffixView_valueDecimal, false);
            suffix = a.getString(R.styleable.SuffixView_suffix);
            if (a.hasValue(R.styleable.SuffixView_value))
            {
                value = (double) a.getFloat(R.styleable.SuffixView_value, 0.0f);
            }
            
            a.recycle();
        }
    }

    /**
     * Set suffix.
     * 
     * @param suffArg
     */
    public void setSuffix(String suffArg)
    {
        suffix = suffArg;
    }

    /**
     * Get value
     * 
     * @return
     */
    public Double getValue()
    {
        return value;
    }

    /**
     * Get value
     * 
     * @return
     */
    public Integer getValueAsInteger()
    {
        if (value == null)
        {
            return null;
        }

        return value.intValue();
    }

    /**
     * Get value
     * 
     * @return
     */
    public Long getValueAsLong()
    {
        if (value == null)
        {
            return null;
        }

        return value.longValue();
    }

    /**
     * Set this view value
     * 
     * @param newValue
     */
    public void setValue(Number nValue)
    {
        Double newValue = (nValue == null) ? null : nValue.doubleValue();

        value = newValue;

        if (newValue == null)
        {
            newValue = Double.valueOf(0.0);
        }

        String sValue = null;
        if (isDecimal)
        {
            sValue = Utils.df.format(newValue);
        }
        else
        {
            sValue = Long.toString(newValue.longValue());
        }

        if (suffix != null)
        {
            sValue = sValue + " " + suffix;
        }

        setText(sValue);
    }

    /**
     * Forces refresh
     */
    public void refresh()
    {
        setValue(value);
    }

    /** Sets if this view values are decimal and should be formatted accordingly */
    public void setValueDecimal(boolean isDecimalArg)
    {
        isDecimal = isDecimalArg;
    }

}
