package com.silentcorp.autotracker.controls;

import android.content.Context;
import android.content.res.TypedArray;

import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

import com.silentcorp.autotracker.R;
import com.silentcorp.autotracker.utils.DoubleNumber;
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
    private DoubleNumber value;
    private boolean isValueDecimal = false;
    private boolean isNullAllowed = false;
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
        
        value = new DoubleNumber();
        
        if (attrs != null)
        {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SuffixView, 0, 0);
            isValueDecimal = a.getBoolean(R.styleable.SuffixView_valueDecimal, false);
            isNullAllowed = a.getBoolean(R.styleable.SuffixView_nullAllowed, false);
            suffix = a.getString(R.styleable.SuffixView_suffix);
            
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
    public DoubleNumber getValue()
    {
        return value;
    }

    /**
     * Set this view value
     * 
     * @param newValue
     */
    public void setValue(DoubleNumber nValue)
    {
        if (nValue == null)
        {
            nValue = new DoubleNumber(null);
        }
        
        value = nValue;

        String sValue = Utils.format(nValue, isNullAllowed, isValueDecimal, true);

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
    public void setValueDecimal(boolean isValueDecimalArg)
    {
        isValueDecimal = isValueDecimalArg;
    }

    public void setNullAllowed(boolean isNullAllowedArg)
    {
        isNullAllowed = isNullAllowedArg;
    }
    
    public boolean isValueDecimal()
    {
        return isValueDecimal;
    }
    
    public boolean isNullAllowed()
    {
        return isNullAllowed;
    }
}
