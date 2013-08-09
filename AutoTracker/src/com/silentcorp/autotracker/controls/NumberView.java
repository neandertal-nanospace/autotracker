package com.silentcorp.autotracker.controls;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;

import com.silentcorp.autotracker.R;
import com.silentcorp.autotracker.controls.numberpicker.NumberPickerDialog;
import com.silentcorp.autotracker.utils.Utils;

/**
 * Extends EditText to show number chooser dialog on click and to display the
 * numbers with metrics
 * 
 * @author neandertal
 * 
 */
public class NumberView extends EditText implements NumberPickerDialog.OnNumberSetListener
{
    private NumberPickerDialog dialog;

    private Double value;
    private boolean isDecimal = false;
    private String suffix = null;

    private OnNumberChangeListener ncListener;

    public NumberView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    public NumberView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(context, attrs);
    }

    public NumberView(Context context)
    {
        super(context);
        init(context, null);
    }

    private void init(Context context, AttributeSet attrs)
    {
        setClickable(true);

        dialog = new NumberPickerDialog(context);
        dialog.setOnNumberSetListener(this);

        setFocusable(false);
        setGravity(Gravity.RIGHT);

        setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.show();
            }
        });

        if (attrs != null)
        {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.NumberView, 0, 0);

            isDecimal = a.getBoolean(R.styleable.NumberView_valueDecimal, false);
            suffix = a.getString(R.styleable.NumberView_suffix);
            if (a.hasValue(R.styleable.NumberView_value))
            {
                value = (double) a.getFloat(R.styleable.NumberView_value, 0.0f);
            }
            String title = a.getString(R.styleable.NumberView_dialogTitle);
            setDialogTitle((title != null) ? title : "");

            if (a.hasValue(R.styleable.NumberView_step))
            {
                float step = a.getFloat(R.styleable.NumberView_step, 1);
                setStep((double) step);
            }
            
            if (a.hasValue(R.styleable.NumberView_rangeMin))
            {
                float min = a.getFloat(R.styleable.NumberView_rangeMin, Float.MIN_VALUE);
                setRangeMin((double) min);
            }
            
            if (a.hasValue(R.styleable.NumberView_rangeMax))
            {
                float max = a.getFloat(R.styleable.NumberView_rangeMax, Float.MAX_VALUE);
                setRangeMax((double) max);
            }
            
            a.recycle();
        }
    }

    /**
     * Called when a number is set
     * 
     * @param number
     */
    @Override
    public void onNumberSet(Double number)
    {
        Log.d(NumberView.class.getName(), "Number selected: " + number);

        setValue(number);
    }

    /**
     * Set title of this number view to be used in NumberPickerDialog
     * 
     * @param title
     */
    public void setDialogTitle(String title)
    {
        dialog.setTitle(title);
    }

    /**
     * Set suffix.
     * 
     * @param suffArg
     */
    public void setSuffix(String suffArg)
    {
        suffix = suffArg;
        dialog.setSuffix(suffArg);
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
        Number oldValue = value;
        value = (nValue == null) ? null : nValue.doubleValue();

        Double newValue = value;
        if (newValue == null)
        {
            newValue = Double.valueOf(0.0);
        }

        // set dialog value
        dialog.setCurrent(newValue);

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

        super.setText(sValue);

        fireNumberChange(oldValue, value);
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
        dialog.setValueDecimal(isDecimalArg);
    }

    /**
     * Sets low limits
     * 
     * @param low
     */
    public void setRangeMin(Number low)
    {
        dialog.setRangeMin(low.doubleValue());
    }

    /**
     * Sets high limits
     * 
     * @param high
     */
    public void setRangeMax(Number high)
    {
        dialog.setRangeMax(high.doubleValue());
    }
    
    /**
     * Set step for the number picker dialog
     * 
     * @param step
     */
    public void setStep(Double step)
    {
        dialog.setStep(step);
    }

    /**
     * Set number change listener
     * 
     * @param ncListArg
     */
    public void setNumberChangeListener(OnNumberChangeListener ncListArg)
    {
        ncListener = ncListArg;
    }

    /**
     * Fire event
     * 
     * @param oldValue
     * @param newValue
     */
    private void fireNumberChange(Number oldValue, Number newValue)
    {
        if (ncListener != null)
        {
            ncListener.onChange(oldValue, newValue);
        }
    }

    /**
     * Listener for when number is changed.
     * 
     * @author neandertal
     */
    public interface OnNumberChangeListener
    {
        public void onChange(Number oldValue, Number newValue);
    }
}
